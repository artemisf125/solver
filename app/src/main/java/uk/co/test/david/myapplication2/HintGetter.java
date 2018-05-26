package uk.co.test.david.myapplication2;


import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by David on 20/05/2018.
 */

public class HintGetter extends AsyncTask {

    ImageView imageView;
    Solver solver;
    int[][] board;

    public HintGetter(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        solver = (Solver)params[0];
//        try {
//            Thread.sleep(100000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            Object result = solver.get(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Log.d("Finished?",""+solver.finished);
        if (!solver.finished){
            return null;
        }
        Log.d("Finished?",""+solver.finished);
        board = solver.cloneBoard(solver.board);
        printBoard(board);
        int[] colours = {getEncodedColour(0,0,0),getEncodedColour(0,0,255),getEncodedColour(0,255,0)};
        int[] pieces = {0,getMostNeighbouringPiece(0)};
        Log.d("...","...");
        printBoard(board);
        Log.d("...","...");
        Log.d("Piece",""+pieces[1]);
        removeAllBut(pieces);
        printBoard(board);
        Log.d("...","...");
        replace(pieces[1],1);
        Log.d("Piece",""+pieces[1]);
        printBoard(board);
        Log.d("...","...");
        Object[] o = {board,colours};
        return o;
    }
    @Override
    protected void onPostExecute(Object o) {
        MainActivity.hintButton.setText("Get Hint");
        if (o == null){
            solver.cancel(true);
            return;
        }
        ShapeDisplayer displayer = new ShapeDisplayer(imageView);
        displayer.execute((Object[])o);
    }

    private int getEncodedColour(int R, int G, int B) {
        return (255 & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    private int getMostNeighbouringPiece(int piece){
        LinkedList<int[]> squares = new LinkedList<>();
        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[0].length; y++){
                if (board[x][y] == piece){
                    int[] pos = {x,y};
                    squares.addLast(pos);
                }
            }
        }
        LinkedList<int[]> checkedSquares = new LinkedList<>();
        int[] neighbours = new int[MainActivity.Shapes.shapes.size()];
        for (int i = 0; i < neighbours.length; i++){
            neighbours[i] = 0;
        }
        for (int[] pos:squares){
            if (checkedSquares.contains(pos)){
                continue;
            }
            int[][] neighbourSquares = {{pos[0]-1,pos[1]},{pos[0]+1,pos[1]},{pos[0],pos[1]-1},{pos[0],pos[1]+1}};
            for (int[] square:neighbourSquares){
                if (square[0] < 0 || square[0] >= board.length || square[1] < 0 || square[1] >= board[0].length){
                    continue;
                }
                if (checkedSquares.contains(square) || board[square[0]][square[1]] == piece){
                    continue;
                }
                checkedSquares.add(square);
                neighbours[board[square[0]][square[1]]]++;
            }
        }
        int biggestNum = 0;
        int biggestPos = 0;
        for (int i = 0; i < neighbours.length; i++){
            if (neighbours[i] > biggestNum){
                biggestNum = neighbours[i];
                biggestPos = i;
            }
        }
        return biggestPos;
    }

    private void removeAllBut(int[] nums){
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length;  j++){
                boolean remove = true;
                for (int k = 0; k < nums.length; k++){
                    if (board[i][j] == nums[k]){
                        remove = false;
                    }
                }
                if (remove){
                    board[i][j] = -1;
                }
            }
        }
    }

    private void replace(int target, int replacement){
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length;  j++){
                if (board[i][j] == target){
                    board[i][j] = replacement;
                }
            }
        }
    }


    private void printBoard(int[][] board){
//        return;
        for (int y = 0; y < board[0].length; y++){
            String s = "";
            for (int x = 0; x < board.length; x++){
                if (board[x][y] >= 0 && board[x][y] < 10){
                    s+= " ";
                }
                s += board[x][y];
            }
            Log.d("Board",s);
        }
    }
}
