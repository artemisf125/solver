package uk.co.test.david.myapplication2;

/**
 * Created by David on 16/04/2018.
 */

public class Line {
    public int start;
    public int startY;
    public int end;
    public int endY;
    public double length;

    public Line(int start, int startY, int end, int endY){
        this.start = start;
        this.startY = startY;
        this.end = end;
        this.endY = endY;
        this.length = Math.sqrt(Math.pow(end-start,2) + Math.pow(endY-startY,2));
    }
}
