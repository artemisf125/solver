package uk.co.test.david.myapplication2;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by David on 03/05/2018.
 */

public class Piece {
    private LinkedList<boolean[][]> shapes;
    public int rotations;

    public Piece(boolean[][] shape){
        shapes = new LinkedList<>();
        for (int r = 0; r < 4; r++){
            boolean[][] s = rotate(shape,r);
//            if (!shapes.contains(s)){
//                shapes.push(s);
//            }
            boolean contains = false;
            for (boolean[][] s2:shapes){
                if (s2.length == s.length && s2[0].length == s[0].length  && !contains){
                    boolean matches = true;
                    for (int x = 0; x < s2.length && matches; x++){
                        for (int y = 0; y < s2[0].length && matches; y++){
                            if (s2[x][y] != s[x][y]){
                                matches = false;
                            }
                        }
                    }
                    contains |= matches;
                }
            }
            if (!contains) {
                shapes.push(s);
            }
        }
        rotations = shapes.size();
        Log.d("Rotations",""+rotations);
    }

    public boolean[][] getRotation(int i){
        return shapes.get(i);
    }

    private boolean[][] rotate(boolean[][] s, int r){
        if (r > 4){
            r -= 4;
            s = getMirror(s);
        }
        return getRotatedPiece(s,r);
    }

    private boolean[][] getMirror(boolean[][] s){
        boolean[][] result = new boolean[s.length][s[0].length];
        for (int x = 0; x < s.length; x++){
            for (int y = 0; y < s[0].length; y++){
                result[s.length-(x+1)][y] = s[x][y];
            }
        }
        return result;
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
}
