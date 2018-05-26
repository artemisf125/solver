package uk.co.test.david.myapplication2;

import java.util.LinkedList;

/**
 * Created by David on 02/05/2018.
 */

public class ScoreAndMoves {
    public int score;
    public LinkedList<Move> moves;

    public ScoreAndMoves(int score, LinkedList<Move> moves){
        this.score = score;
        this.moves = moves;
    }
}
