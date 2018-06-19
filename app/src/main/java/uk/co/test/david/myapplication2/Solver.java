package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.graphics.drawable.shapes.Shape;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by David on 19/04/2018.
 */

public class Solver extends AsyncTask {
    ImageView imageView;
    boolean corners;
    int containerWidth;
    int containerHeight;
    int missingPieces;
    LinkedList<Piece> pieces;
    public int[][] board;
    int[] colours;
    Random rand = new Random();
    int updates = 0;
    int max = 0;
    boolean[] placed;
    public boolean finished = false;
    private boolean fail = false;
    private String failMessage = "";
    int boardWidth;
    int boardHeight;

    private static final int EMPTY = -1;
    private static final int TEMPFORSCORING = -2;
    private static final int EDGE = -3;
    private static final int SQUAREOCCUPIED = -4;
    private static final int SQUAREFREE = -5;
//    private static final int  = -6;

    public Solver(ImageView imageView, boolean corners, int containerWidth, int containerHeight, int missingPieces){
        super();
        this.imageView = imageView;
        this.corners = corners;
        this.containerWidth = containerWidth;
        this.containerHeight = containerHeight;
        this.missingPieces = missingPieces;
        this.pieces = new LinkedList<>();
        for (boolean[][] shape:MainActivity.Shapes.shapes){
            this.pieces.push(new Piece(shape));
        }
        colours = new int[pieces.size()+1];
        placed = new boolean[pieces.size()];
        colours[0] = getEncodedColour(255,0,0,0);
        for (int i = 0; i < pieces.size(); i++){
            colours[i+1] = getEncodedColour(255, rand.nextInt(255)+1,rand.nextInt(255)+1,rand.nextInt(255)+1);
            placed[i] = false;
        }
        Random rand = new Random();
        int totalPieceArea = MainActivity.Shapes.getTotalPieceArea();
        double size = Math.sqrt(totalPieceArea);
//        int required = Integer.parseInt(MainActivity.problemName.split("_")[1].split("x")[0]);
//        Log.d("Size - Required", "" + size + " - " + required);
//        if (size != required){
//            fail = true;
//            failMessage = "Size Mismatch -  Found:" + size + ", Required: " + required;
//            if (size > required){
//                MainActivity.tests.addFirst(new FileLoader(imageView, MainActivity.problemName, MainActivity.QuantisationCalculator.tolerance*(rand.nextDouble()/2+0.5)*2));
//            } else {
//                MainActivity.tests.addFirst(new FileLoader(imageView, MainActivity.problemName, MainActivity.QuantisationCalculator.tolerance/((rand.nextDouble()/2+0.5)*2)));
//            }
//        }
        if (containerWidth != 0){
            boardWidth = containerWidth;
        } else {
            boardWidth = (int)size;
        }
        if (containerHeight != 0){
            boardHeight = containerHeight;
        } else {
            boardHeight = (int)size;
        }
        board = new int[boardWidth][boardHeight];
        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[0].length; y++){
                board[x][y] = EMPTY;
            }
        }
    }

    private void reinit(){
        placed = new boolean[pieces.size()];
        for (int i = 0; i < pieces.size(); i++){
            placed[i] = false;
        }
        board = new int[boardWidth][boardHeight];
        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[0].length; y++){
                board[x][y] = EMPTY;
            }
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
//        try {Thread.sleep(2000);}catch(Exception e){}
//        if (fail){
////            Log.d("Failure","Test Failed");
//            String[] result = {MainActivity.problemName,"Failed! - " + failMessage};
//            MainActivity.results.addLast(result);
//            return null;
//        }
//        try {Thread.sleep(2000);}catch (Exception e){};
        Log.d("Solver","Solving...");
        if (ShapeDisplayer.running){
            Log.d("Solver","Shape displayer already running!");
        }
//        Log.d("SolverTimer","Start");
//        for (int iteration = 0; iteration < 5; iteration++) {
//            reinit();
//            long start = System.currentTimeMillis();
//            Collections.shuffle(pieces);
            finished = solve(0);
//            long timeTaken = System.currentTimeMillis() - start;
//            String[] result = {MainActivity.problemName, "" + timeTaken};
//            Log.d("Results:",result[0] + " - " + result[1]);
//            MainActivity.results.addLast(result);
//        }
//        Log.d("SolverTimer","Stop");
//        MainActivity.printResults();
//        MainActivity.running = false;
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        Bitmap newImage = (Bitmap) o;
        Log.d("App", "Displaying new Image");
        imageView.setImageBitmap(newImage);
//        MainActivity.solveButton.setText("Solved!");
        if (MainActivity.tests.size() == 0){
            MainActivity.printResults();
        } else {
            MainActivity.tests.removeFirst().execute();
        }
    }
//
//    private boolean solve(int nextPiece){
////        try {
////            Thread.sleep(100);
////        } catch (Exception e){
////
////        }
//        if (nextPiece > max){
//            max = nextPiece;
//        }
//        if (nextPiece == max-2){
//            Log.d("Depth",""+nextPiece);
//        }
//        boolean[][] shape = pieces.get(nextPiece);
//        LinkedList<Move> moves = getMoves(shape,nextPiece);
//        Collections.sort(moves,new Comparator<int[]>() {
//            @Override
//            public int compare(int[] ints, int[] t1) {
//                return ints[3] - t1[3];
//            }
//        });
//        for (int[] move:moves){
//            insertPiece(move[0],move[1],move[2],nextPiece,shape);
//            if (nextPiece == pieces.size()-1){
//                // Finished?
//                Log.d("Solver","Finished");
//                ShapeDisplayer displayer = new ShapeDisplayer(imageView);
//                Object[] o = {board,colours};
//                displayer.execute(o);
//                return true;
//            }
//            if (solve(nextPiece+1)){
//                return true;
//            }
//            removePiece(move[0],move[1],move[2],nextPiece,shape);
//        }
//        return false;
//    }
    private boolean solve(int nextPiece){
//        if (isCancelled())
//            return false;
        //        try {
        //            Thread.sleep(100);
        //        } catch (Exception e){
        //
        //        }
        if (nextPiece > max){
            max = nextPiece;
        }
//        if (nextPiece == max-2){
//            Log.d("Depth",""+nextPiece);
//        }
        if (nextPiece >= pieces.size()){
            // Finished
            ShapeDisplayer displayer = new ShapeDisplayer(imageView);
            Object[] o = {board,colours};
            displayer.execute(o);
            MainActivity.solutions++;
//            Log.d("Solved!2", "Solutions: " + MainActivity.solutions);
            printBoard();
            return true;
        }
        if (placed[nextPiece]){
            return solve(nextPiece+1);
        }
        Piece piece = pieces.get(nextPiece);
        LinkedList<Move> moves = getMoves(piece,nextPiece);
        Collections.sort(moves,new Comparator<Move>() {
            @Override
            public int compare(Move m1, Move m2) {
                return m1.s - m2.s;
            }
        });
        for (Move move:moves){
//            if (isCancelled())
//                return false;
            for (Move m:move.otherRequirements){
//                Log.d("Solver","Inserting Other requirement, piece " + m.shapeNumber);
                insertPieceWithoutUpdate(m.x,m.y,m.r,m.shapeNumber, pieces.get(m.shapeNumber));
            }
            insertPiece(move.x,move.y,move.r,nextPiece,piece);
            if (nextPiece == pieces.size()-1){
                // Finished?
//                Log.d("Solver","Finished");
                ShapeDisplayer displayer = new ShapeDisplayer(imageView);
                Object[] o = {board,colours};
                displayer.execute(o);
                MainActivity.solutions++;
//                Log.d("Solved!", "Solutions: " + MainActivity.solutions);
                printBoard();
                return true;
            } else if (solve(nextPiece+1)){
                return true;
            }
            for (Move m:move.otherRequirements){
                removePieceWithoutUpdate(m.x,m.y,m.r,m.shapeNumber, pieces.get(m.shapeNumber));
            }
            removePiece(move.x,move.y,move.r,nextPiece,piece);
        }
        return false;
    }


//    private LinkedList<int[]> getMoves(boolean[][] shape, int shapeNumber){
//        LinkedList<int[]> moves = new LinkedList<>();
//        for (int x = 0; x < board.length; x++){
//            for (int y = 0; y < board[0].length; y++){
//                for (int r = 0; r < 4; r++) {
//                    if (canInsertPiece(x, y, r, shape)) {
//                        int s = score(x,y,r,shape,shapeNumber);
//                        if (s < 0){
//                            continue;
//                        }
//                        int[] pos = {x, y, r, s};
//                        moves.push(pos);
//                    }
//                }
//            }
//        }
////        Log.d("getMovesLength",""+moves.size());
////        printBoard();
//        return moves;
//    }


    private LinkedList<Move> getMoves(Piece piece, int shapeNumber){
        LinkedList<Move> moves = new LinkedList<>();
        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[0].length; y++){
                for (int r = 0; r < piece.rotations; r++) {
                    if (canInsertPiece(x, y, r, piece)) {
                        ScoreAndMoves sam = score(x,y,r,piece,shapeNumber);
                        int s = sam.score;
                        LinkedList<Move> reqs = sam.moves;
                        if (s < 0){
                            continue;
                        }
//                        int[][] reqs = null;
                        moves.push(new Move(x,y,r,shapeNumber,s,reqs));
                    }
                }
            }
        }
//        Log.d("getMovesLength",""+moves.size());
//        printBoard();
        return moves;
    }

    private boolean canInsertPiece(int xStart, int yStart, int rotation, Piece piece){
        return canInsertPiece(xStart,yStart,rotation,piece,-1);
    }

    private boolean canInsertPiece(int xStart, int yStart, int rotation, Piece piece, int spaceNumber){
        boolean[][] rotatedPiece = piece.getRotation(rotation);
        if ((xStart + rotatedPiece.length > board.length) || (yStart + rotatedPiece[0].length > board[0].length)){
            return false;
        }
        for (int x = 0; x < rotatedPiece.length; x++){
            for (int y = 0; y < rotatedPiece[0].length; y++){
                if (rotatedPiece[x][y]) {
//                    Log.d("CanInsertPiece","pieceLength"+rotatedPiece.length+",pieceHeight:"+rotatedPiece[0].length+",x:"+x+",y"+y+",boardX"+(xStart+x)+",boardY" + (yStart+y)+",xStart"+xStart+",yStart"+yStart);
                    if (board[xStart + x][yStart + y] != spaceNumber) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private void insertPieceWithoutUpdate(int xStart, int yStart, int rotation, int pieceNumber, Piece piece){
        boolean[][] rotatedPiece = piece.getRotation(rotation);
        for (int x = 0; x < rotatedPiece.length; x++){
            for (int y = 0; y < rotatedPiece[0].length; y++){
                if (rotatedPiece[x][y]) {
                    board[xStart + x][yStart + y] = pieceNumber;
                }
            }
        }
        if (pieceNumber >= 0) {
            placed[pieceNumber] = true;
        }
    }
    private void insertPiece(int xStart, int yStart, int rotation, int pieceNumber, Piece piece){
        insertPieceWithoutUpdate(xStart,yStart,rotation,pieceNumber,piece);
        updateImage();
    }

    private void removePieceWithoutUpdate(int xStart, int yStart, int rotation, int pieceNumber, Piece piece) {
        boolean[][] rotatedPiece = piece.getRotation(rotation);
        for (int x = 0; x < rotatedPiece.length; x++) {
            for (int y = 0; y < rotatedPiece[0].length; y++) {
                if (rotatedPiece[x][y]) {
                    board[xStart + x][yStart + y] = EMPTY;
                }
            }
        }
        if (pieceNumber >= 0) {
            placed[pieceNumber] = false;
        }
    }

    private void removePiece(int xStart, int yStart, int rotation, int pieceNumber, Piece piece){
        removePieceWithoutUpdate(xStart,yStart,rotation,pieceNumber,piece);
        updateImage();
    }

    private boolean[][] getRotatedPiece(boolean[][] piece, int rotation){
        boolean[][] rotatedPiece = piece;
        switch (rotation) {
            case 1:
                rotatedPiece = new boolean[piece[0].length][piece.length];
                for (int x = 0; x < piece[0].length; x++){
                    for (int y = 0; y < piece.length; y++){
                        rotatedPiece[x][y] = piece[y][piece[0].length-(x+1)];
                    }
                }
                break;
            case 2:
                rotatedPiece = new boolean[piece.length][piece[0].length];
                for (int x = 0; x < piece.length; x++){
                    for (int y = 0; y < piece[0].length; y++){
                        rotatedPiece[x][y] = piece[piece.length-(x+1)][piece[0].length-(y+1)];
                    }
                }
                break;
            case 3:
                rotatedPiece = new boolean[piece[0].length][piece.length];
                for (int x = 0; x < piece[0].length; x++){
                    for (int y = 0; y < piece.length; y++){
                        rotatedPiece[x][y] = piece[piece.length-(y+1)][x];
                    }
                }
                break;
        }
        return rotatedPiece;
    }

    private ScoreAndMoves score(int startX, int startY, int rotation, Piece piece,int shapeNumber){
        int score = 1;
        insertPieceWithoutUpdate(startX,startY,rotation,TEMPFORSCORING, piece);
//        int[] smallest = getSmallestRegionSize(shapeNumber);
//        score = smallest[0]-1;
//        if (!canPlaceAShapeIn(smallest[1],smallest[2],shapeNumber)){
//            score = -1;
//        }
        LinkedList<Move> moves = unfillableSquare(shapeNumber);
        if (moves == null){
            score = -1;
        }
        removePieceWithoutUpdate(startX,startY,rotation,TEMPFORSCORING, piece);
//        Log.d("Sore",""+score);
        ScoreAndMoves res = new ScoreAndMoves(score,moves);
//        if (moves != null) {
//            Log.d("Score", "Moves = " + moves.size());
//        } else {
//            Log.d("Score","Moves = null");
//        }
        return res;
    }

    private int[] getSmallestRegionSize(int shapeNumber){
        int smallest = board.length*board[0].length;
        int smallestX = 0;
        int smallestY = 0;
        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[0].length; y++){
                if (board[x][y] == -EMPTY){
//                    int s = getRegionSizeAndMark(x,y);//, shapeNumber);
                    int s = 0;
                    if (s < smallest){
                        smallest = s;
                    }
                }
            }
        }
        int[] result = {smallest,smallestX,smallestY};
        return result;
    }

    private LinkedList<int[]> getRegionSizeAndMark (int x, int y){//, int shapeNumber) {
        int size = 0;
        boolean[][] checked = new boolean[board.length][board[0].length];
        for (int xt = 0; xt < checked.length; xt++){
            for (int yt = 0; yt < checked[0].length; yt++){
                checked[xt][yt] = false;
            }
        }
        LinkedList<int[]> edges = new LinkedList();
        LinkedList<int[]> shape = new LinkedList();
        int[] start = {x,y};
        edges.add(start);
        checked[x][y] = true;
        while (edges.size() > 0){
            size += 1;
            int[] edge = edges.pop();
            shape.add(edge);
            board[edge[0]][edge[1]] = EDGE;
            int[][] possibleEdges = {{edge[0]+1,edge[1]},{edge[0]-1,edge[1]},{edge[0],edge[1]+1},{edge[0],edge[1]-1}};
            for (int[] possibleEdge:possibleEdges){
                if (possibleEdge[0] < 0 || possibleEdge[0] >= board.length || possibleEdge[1] < 0 || possibleEdge[1] >= board[0].length){
                    continue;
                }
                if (board[possibleEdge[0]][possibleEdge[1]] == EMPTY && !checked[possibleEdge[0]][possibleEdge[1]]){
                    edges.add(possibleEdge);
                    checked[possibleEdge[0]][possibleEdge[1]] = true;
                }
            }
        }

//        boolean result = canPlaceAShapeInSomeSpace(shapeNumber);

        for (int xt = 0; xt < board.length; xt++){
            for (int yt = 0; yt < board.length; yt++){
                if (board[xt][yt] == EDGE){
                    board[xt][yt] = EMPTY;
                }
            }
        }

//        if (!result){
//            size = 0;
//        }

//        return size;
        return shape;
    }

    private boolean canPlaceAShapeIn(int x, int y, int shapeNumber){
        boolean[][] checked = new boolean[board.length][board[0].length];
        for (int xt = 0; xt < checked.length; xt++){
            for (int yt = 0; yt < checked[0].length; yt++){
                checked[xt][yt] = false;
            }
        }
        LinkedList<int[]> edges = new LinkedList();
        int[] start = {x,y};
        edges.add(start);
        checked[x][y] = true;
        while (edges.size() > 0){
            int[] edge = edges.pop();
            board[edge[0]][edge[1]] = EDGE;
            int[][] possibleEdges = {{edge[0]+1,edge[1]},{edge[0]-1,edge[1]},{edge[0],edge[1]+1},{edge[0],edge[1]-1}};
            for (int[] possibleEdge:possibleEdges){
                if (possibleEdge[0] < 0 || possibleEdge[0] >= board.length || possibleEdge[1] < 0 || possibleEdge[1] >= board[0].length){
                    continue;
                }
                if (board[possibleEdge[0]][possibleEdge[1]] == -1 && !checked[possibleEdge[0]][possibleEdge[1]]){
                    edges.add(possibleEdge);
                    checked[possibleEdge[0]][possibleEdge[1]] = true;
                }
            }
        }

        boolean result = canPlaceAShapeInSomeSpace(shapeNumber);


        for (int xt = 0; xt < board.length; xt++){
            for (int yt = 0; yt < board.length; yt++){
                if (board[xt][yt] == EDGE){
                    board[xt][yt] = EMPTY;
                }
            }
        }
//        Log.d("CanPlaceAShape",""+result);
        return  result;
    }

    private boolean canPlaceAShapeInSomeSpace(int shapeNumber){
        for (int i = shapeNumber+1; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board.length; y++) {
                    for (int r = 0; r < piece.rotations; r++) {
                        if (canInsertPiece(x, y, r, piece, EDGE)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void updateImage(){
//        updates++;
//        if (updates%10000 != 0){
//            return;
//        }
//        try {Thread.sleep(100); } catch (Exception e) {}
        if (ShapeDisplayer.running){
            return;
        }
        ShapeDisplayer.running = true;
//        Log.d("Drawing Image",""+updates);
        Log.d("Solver","Drawing Image");
        ShapeDisplayer displayer = new ShapeDisplayer(imageView);
//        displayer.execute();

        Object[] o = {cloneBoard(board),colours};
        displayer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,o);
//        try {Thread.sleep(1000); } catch (Exception e) {}
    }

    public int[][] cloneBoard(int[][] board){
        int[][] newBoard = new int[board.length][board[0].length];
        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[0].length; y++){
                newBoard[x][y] = board[x][y];
            }
        }
        return  newBoard;
    }

    private int getEncodedColour(int A, int R, int G, int B) {
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    private void printBoard(){
//        return;
        printBoard(board);
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
//            Log.d("Board",s);
        }
    }
//    private boolean unfillableSquare(int shapeNumber){
//        boolean[][] reached = new boolean[board.length][board[0].length];
//        for (int x = 0; x < reached.length; x++){
//            for (int y = 0; y < reached[0].length; y++){
//                reached[x][y] = board[x][y] != -1;
//            }
//        }
//        for (int s = shapeNumber+1; s < pieces.size(); s++){
//            boolean[][] shape = pieces.get(s);
//            for (int x = 0; x < board.length-shape.length+1; x++){
//                for (int y = 0; y < board[0].length - shape[0].length+1; y++){
//                    for (int r = 0; r < 4; r++){
//                        if (canInsertPiece(x, y, r, shape)){
//                            for (int xs = 0; xs < shape.length; xs++){
//                                for (int ys = 0; ys < shape[0].length; ys++){
//                                    reached[x+xs][y+ys] |= shape[xs][ys];
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        int remainingSpaces = missingPieces;
//        LinkedList<LinkedList<int[]>> pieces = new LinkedList<>();
//        for (int x = 0; x < reached.length; x++){
//            for (int y = 0; y < reached[0].length; y++) {
//                if (!reached[x][y]){
//                    if (remainingSpaces <= 0){
//                        return true;
//                    }
//                    remainingSpaces -= 1;
//                    pieces.addLast(getRegionSizeAndMark(x,y));
//                    for (int[] sq:pieces.getLast()){
//                        reached[sq[0]][sq[1]] = true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    private LinkedList<Move> unfillableSquare(int shapeNumber){
        if (shapeNumber >= pieces.size()-1){
            return new LinkedList<>();
        }
        int[][] reached = new int[board.length][board[0].length];
        for (int x = 0; x < reached.length; x++){
            for (int y = 0; y < reached[0].length; y++){
                reached[x][y] = board[x][y] != EMPTY?SQUAREOCCUPIED:SQUAREFREE;
            }
        }
//        LinkedList<Move> moves = new LinkedList<>();
        for (int s = shapeNumber+1; s < pieces.size(); s++){
            Piece piece = pieces.get(s);
            for (int r = 0; r < piece.rotations; r++){
                boolean[][] rotatedShape = piece.getRotation(r);
                for (int x = 0; x < board.length-rotatedShape.length+1; x++){
                    for (int y = 0; y < board[0].length - rotatedShape[0].length+1; y++){
                        if (canInsertPiece(x, y, r, piece)){
//                            moves.add(new Move(x,y,r,s,0,null));
                            for (int xs = 0; xs < rotatedShape.length; xs++){
                                for (int ys = 0; ys < rotatedShape[0].length; ys++){
                                    if (rotatedShape[xs][ys]){
                                        if (reached[x+xs][y+ys] >= 0 && reached[x+xs][y+ys] != s){
                                            reached[x+xs][y+ys] = SQUAREOCCUPIED;
                                        } else if (reached[x+xs][y+ys] == SQUAREFREE){
                                            reached[x+xs][y+ys] = s;
                                        }
                                    }
//                                    reached[x+xs][y+ys] |= shape[xs][ys];
                                }
                            }
                        }
                    }
                }
            }
        }
        int remainingSpaces = missingPieces;
        LinkedList<LinkedList<int[]>> shapes = new LinkedList<>();
//        LinkedList<Integer> inserted = new LinkedList<>();
        LinkedList<Move> reqs = new LinkedList<>();
//        Log.d("Solver","Board");
//        printBoard();
//        printBoard(reached);
        int[] values = new int[this.pieces.size()];
        for (int i = 0; i < values.length; i++){
            values[i] = 0;
        }
        for (int x = 0; x < reached.length; x++) {
            for (int y = 0; y < reached[0].length; y++) {
                if (reached[x][y] == SQUAREFREE) {
                    if (remainingSpaces <= 0) {
                        return null;
                    }
                    remainingSpaces -= 1;
                    shapes.addLast(getRegionSizeAndMark(x, y));
                    for (int[] sq : shapes.getLast()) {
                        reached[sq[0]][sq[1]] = SQUAREOCCUPIED;
                    }
                } else {
                    if (reached[x][y] >= 0){
                        values[reached[x][y]]++;
                    }
                }
            }
        }
//                } else if (reached[x][y]>=0){
//                    if (inserted.contains(reached[x][y])){
//                        Log.d("Solver", "Returning Null - might be wrong: " + reached[x][y] + " at " + x + ", " + y);
//                        printBoard(reached);
//                        return null;
////                        continue;
//                    }
//                    int number = reached[x][y];
//                    inserted.add(number);
//                    Log.d("Solver", "Adding " + number + " to inserted at " + x + ", " + y);
//                    for (Move m:moves){
//                        if (m.shapeNumber == number){
//                            boolean[][] shape = this.pieces.get(m.shapeNumber);
//                            boolean[][] rotatedShape = getRotatedPiece(shape, m.r);
//                            boolean matches = true;
//                            boolean covers = false;
//                            for (int xt = 0; (xt < rotatedShape.length) && matches; xt++){
//                                for (int yt = 0; (yt < rotatedShape[0].length) && matches; yt++){
//                                    if (rotatedShape[xt][yt]) {
//                                        if (reached[m.x+xt][m.y+yt] != m.shapeNumber && reached[m.x+xt][m.y+yt] != SQUAREOCCUPIED){
//                                            Log.d("Solver","Does Not Match!");
//                                            matches = false;
//                                        }
//                                        if (m.x+xt == x && m.y+yt == y){
//                                            covers = true;
//                                        }
////                                        reached[m.x + xt][m.y + yt] = SQUAREOCCUPIED;
//                                    }
//                                }
//                            }
//                            if (!matches && covers){
//                                Log.d("Solver", "Caught Does Not Match");
//                                continue;
//                            }
//                            for (int xt = 0; xt < rotatedShape.length; xt++){
//                                for (int yt = 0; yt < rotatedShape[0].length; yt++){
//                                    if (rotatedShape[xt][yt]) {
//                                        if (reached[m.x+xt][m.y+yt] != m.shapeNumber && reached[m.x+xt][m.y+yt] != SQUAREOCCUPIED){
//                                            Log.d("Solver","Does Not Match! Fail!");
//                                        }
//                                        reached[m.x + xt][m.y + yt] = SQUAREOCCUPIED;
//                                        Log.d("Solver","Replacing at " + (m.x + xt) + ", " + (m.y + yt));
//                                    }
//                                }
//                            }
//                            reqs.add(m);
//                            break;
//                        }
//                    }
//
//                    printBoard(reached);
//                }
//            }
//        }
        for (int i = 0; i < values.length; i++){
            if (i == 0){
                continue;
            }
            int best = values[i];
            Piece piece = this.pieces.get(i);
            int bestx = 0;
            int besty = 0;
            int bestr = 0;
            for (int r = 0; r < piece.rotations && best != 0; r++) {
                boolean[][] rotatedShape = piece.getRotation(r);
                for (int x = 0; x < reached.length - (rotatedShape.length - 1) && best != 0; x++) {
                    for (int y = 0; y < reached[0].length - (rotatedShape[0].length - 1) && best != 0; y++) {
                        boolean canPlace = true;
                        for (int xt = 0; xt < rotatedShape.length && canPlace; xt++){
                            for (int yt = 0; yt < rotatedShape[0].length && canPlace; yt++){
                                if (rotatedShape[xt][yt]){
                                    if (board[x+xt][y+yt] != EMPTY){
                                        canPlace = false;
                                    }
                                }
                            }
                        }
                        if (!canPlace){
                            continue;
                        }
                        int remaining = values[i];
                        for (int xt = 0; xt < rotatedShape.length; xt++){
                            for (int yt = 0; yt < rotatedShape[0].length; yt++){
                                if (rotatedShape[xt][yt]){
                                    if (reached[x+xt][y+yt] == i){
                                        remaining--;
                                    }
                                }
                            }
                        }
                        if (remaining < best){
                            best = remaining;
                        }
                    }
                }
            }
//            Log.d("Solver","Shape:"+i+", Best:" + best + ", x:"+bestx+", y:"+besty+", r:"+bestr);
            if (best != 0){
                return null;
            }
        }
//        LinkedList reqs = new LinkedList<>();
        return reqs;
    }

    public void fillRegion(int[][] board, int x, int y, int with){
        int n = board[x][y];
        boolean[][] checked = new boolean[board.length][board[0].length];
        for (int xt = 0; xt < board.length; xt++){
            for (int yt = 0; yt < board[0].length; yt++){
                checked[xt][yt] = false;
            }
        }
        LinkedList<int[]> edges = new LinkedList<>();
        int[] start = {x,y};
        while (!edges.isEmpty()){
            int[] edge = edges.pop();
            checked[edge[0]][edge[1]] = true;
            if (board[edge[0]][edge[1]] == n){
                board[edge[0]][edge[1]] = with;
            } else {
                continue;
            }
            int[][] possibleEdges = {{edge[0]+1,edge[1]},{edge[0]-1,edge[1]},{edge[0],edge[1]+1},{edge[0],edge[1]-1}};
            for (int[] e: possibleEdges){
                if (e[0] < 0 || e[1] < 0 || e[0] >= board.length || e[1] >= board[0].length){
                    continue;
                }
                if (checked[e[0]][e[1]]){
                    continue;
                }
                edges.addFirst(e);
            }
        }
    }

    public void replaceNumber(int[][] board, int target, int replacement){
        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[0].length; y++){
                if (board[x][y] == target){
                    board[x][y] = replacement;
                }
            }
        }
    }
}
