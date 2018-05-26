package uk.co.test.david.myapplication2;

import java.util.LinkedList;

/**
 * Created by David on 02/05/2018.
 */

public class Move {
    public int x;
    public int y;
    public int r;
    public int shapeNumber;
    public int s;
    public LinkedList<Move> otherRequirements;

    public Move(int x, int y, int r, int shapeNumber, int s, LinkedList<Move> otherReqs){
        this.x = x;
        this.y = y;
        this.r = r;
        this.shapeNumber = shapeNumber;
        this.s = s;
        this.otherRequirements = otherReqs;
    }
}
