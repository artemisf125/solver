package uk.co.test.david.myapplication2;

import android.util.Log;

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

    public Line(CoOrdinate start, CoOrdinate end){
        this.start = start.x;
        this.startY = start.y;
        this.end = end.x;
        this.endY = end.y;
        this.length = Math.sqrt(Math.pow(this.end-this.start,2) + Math.pow(endY-startY,2));
    }

    public boolean intersects(Line line){
        // Approximation to deal with the case of vertical lines.
        double xChange = (end-start);
        if (xChange == 0){
            xChange = 0.01;
        }
        double xChange2 = (line.end-line.start);
        if (xChange2 == 0){
            xChange2 = 0.01;
        }

        double g1 = ((double)(endY - startY))/xChange;
        double g2 = ((double)(line.endY - line.startY))/xChange2;
        if (g1 == g2 || g1 == -g2){
            return false;
        }
        if (start == line.start && startY == line.startY || start == line.end && startY == line.endY || end == line.start && endY == line.startY || end == line.end && endY == line.endY){
            return false;
        }
        /*
        x1 + n == x2 + m
        y1 + g1*n == y2 + m*g2
        x1 - x2 + n == m
        y1 + g1*n == y2 +(x1 - x2 + n)*g2
        y1 + g1*n == y2 + x1*g2 - x2*g2 + n*g2
        g1*n - g2*n == y2 + x1*g2 - x2*g2 - y1
        (g1 - g2)*n == y2 + x1*g2 - x2*g2 - y1
        n == (y2 + x1*g2 - x2*g2 - y1)/(g1 - g2)
        */
        double n = (line.startY + start*g2 - line.start*g2 - startY)/(g1 - g2);
        double m = start - line.start + n;
//        Log.d("n:",""+n + " : " + (end-start) + ":"  + (n > 0 && n < (end-start)));
//        if ((n > 1 && n < (end-start) - 1)){
//            Log.d("LineIntersection", "" + (n > 0 && n < (end-start) && m > 0 && m < (line.end-line.start)) + " : " + start + "," + startY + "-" + end + "," + endY + ":" + line.start + "," + line.startY + "-" + line.end + "," + line.endY);
//        }
        double n2 = n / (end-start);
        double m2 = m / (line.end-line.start);
        return n2 > 0 && n2 < 1 && m2 > 0 && m2 < 1;
    }
}
