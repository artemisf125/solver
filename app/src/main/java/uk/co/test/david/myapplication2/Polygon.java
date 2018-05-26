package uk.co.test.david.myapplication2;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by David on 13/05/2018.
 */

public class Polygon {
    LinkedList<CoOrdinate> points;
    public double[] lengths;
    public double[] angles;
    public double[] reverseLengths;
    public double[] reverseAngles;
    public int size;

    public Polygon(LinkedList<CoOrdinate> points){
        size = points.size();
        lengths = new double[size];
        angles = new double[size];
        this.points = points;
//        CoOrdinate previous = points.getLast();
//        CoOrdinate prevPrevious = points.get(points.size()-2);
        double prevAngle = 0;
//        int i = 0;
//        for (CoOrdinate point:points){
//            Log.d("Corner:",""+point.x+","+point.y);
////            double a = Math.sqrt(Math.pow(point.x-previous.x,2)+Math.pow(point.y-previous.y,2));
////            Log.d("a:",""+a);
////            double b = Math.sqrt(Math.pow(previous.x-prevPrevious.x,2)+Math.pow(previous.y-prevPrevious.y,2));
////            Log.d("b:",""+b);
////            double c = Math.sqrt(Math.pow(point.x-prevPrevious.x,2)+Math.pow(point.y-prevPrevious.y,2));
////            Log.d("c:",""+c);
//            lengths[i] = Math.sqrt(Math.pow(point.x-previous.x,2)+Math.pow(point.y-previous.y,2));
//            angles[i] = (((Math.atan2(((double)(point.y-previous.y)),(double)(point.x-previous.x)) + prevAngle)+Math.PI)%(Math.PI*2))-Math.PI;
//            prevAngle = angles[i];
////            Log.d("d:",""+(a*a+b*b-c*c)/2*a*b);
////            angles[i] = Math.acos((a*a+b*b-c*c)/2*a*b);
////            prevPrevious = previous;
//            previous = point;
//            i++;
//        }
        for (int i = 0; i <= points.size()*3; i++){
            CoOrdinate current = points.get(i%size);
            CoOrdinate next = points.get((i+1)%points.size());
            CoOrdinate previous = points.get((i-1+points.size())%points.size());
            lengths[i%size] = Math.sqrt(Math.pow(current.x-next.x,2)+Math.pow(current.y-next.y,2));
            double angle = getAngle(next.y-current.y,next.x-current.x) - prevAngle;
//            angle = ((angle+Math.PI)%(Math.PI*2))-Math.PI;
            prevAngle += angle;
            angles[i%size] = angle;
        }
        this.reverseAngles = new double[size];
        this.reverseLengths = new double[size];
        for (int i = 0; i < size; i++){
            reverseAngles[i] = angles[(size-i)%size];
            reverseLengths[i] = lengths[(size-1-i)%size];
        }
    }

    public double getAngle(double y,double x){
//        if (x == 0){
//            if
//            return Math.PI/2;
//        }
//        if (x < 0) {
//            if (y >= 0){
//                return Math.PI/2 - Math.atan(y/x);
//            } else {
//                return Math.PI/2 + Math.atan(y/x);
//            }
//        }
        return ((Math.atan2(y,x)+Math.PI*2)%(Math.PI*2));
    }

    public PolyMatch findMatches(Polygon other){
        if (this.size < other.size){
            return other.findMatches(this);
        }
        int bestLength = 0;
        int bestOtherStart = 0;
        int bestThisStart = 0;
        for (int i = 0; i < size; i++){
            int[] match = getMatch(other,i);
            if (match[1] > bestLength){
                bestLength = match[1];
                bestOtherStart = match[0];
                bestThisStart = i;
            }
        }
//        int[] result = {bestThisStart,bestOtherStart,bestLength};
        return new PolyMatch(this,other,bestThisStart,bestOtherStart,bestLength);
    }

     private int[] getMatch(Polygon other, int start){
         int maxLength = 0;
         int bestStart = 0;
         boolean match = false;
         int length = 0;
         for (int i = 0; i < other.size; i++){
             if (similar((angles[(i+start)%size]+Math.PI*2)%(Math.PI*2),((-other.reverseAngles[i])+Math.PI*2)%(Math.PI*2))){
                if (match){
//                    length += 1;
                } else {
                    match = true;
                    length = 1;
                }
             } else {
                 if (match){
                     match = false;
                     if (length > maxLength){
                         maxLength = length;
                         bestStart = i;
                     }
                     length = 0;
                 } else {

                 }
             }
             if (similar(lengths[(i+start)%size],other.reverseLengths[i])){
                 if (match){
                     length += 1;
                 } else {
                     match = true;
                     length = 1;
                 }
             } else {
                 if (match){
                     match = false;
                     if (length > maxLength){
                         maxLength = length;
                         bestStart = i;
                     }
                     length = 0;
                 } else {

                 }
             }
         }
         int[] result = {bestStart,maxLength};
         return result;
    }

    private boolean similar(double a, double b){
//        Log.d("Similar:",""+(a)+","+(b)+":"+Math.abs((a/b)-1));
        if (b == 0){
            if (a > 0.1){
                return false;
            } else {
                return true;
            }
        }
        return Math.abs(((a)/(b))-1) < 0.1;
    }

    public static Polygon mergeMatch(PolyMatch match){
        LinkedList<CoOrdinate> points = new LinkedList<>();
//        for (int i = 0; i < match.start1; i++){
//            points.addLast(match.p1.points.get(i));
//        }
//        for (int i = match.start2+match.length; i >= 0; i--){
//            points.addLast(match.p2.points.get(i));
//        }
//        for (int i = match.p2.size-1; i >= match.start2; i--){
//            points.addLast(match.p2.points.get(i));
//        }
//        for (int i = match.start1+match.length; i < match.p1.size; i++){
//            points.addLast(match.p1.points.get(i));
//        }
        for (double a:match.p1.angles){
            Log.d("P1",""+toDegrees(capAngle(a)));
        }
        for (double a:match.p2.reverseAngles){
            Log.d("P2",""+toDegrees(capAngle(a)));
        }
        int x = 0;
        int y = 0;
        double angle = 0;
        points.addLast(new CoOrdinate(x,y));
        for (int i = 0; i < match.start1; i++){
            angle += capAngle(match.p1.angles[i]);
            Log.d("Angle",""+toDegrees(capAngle(match.p1.angles[i])));
            x += match.p1.lengths[i]*Math.cos(angle);
            y += match.p1.lengths[i]*Math.sin(angle);
            Log.d("Corner",""+x+","+y);
            points.addLast(new CoOrdinate(x,y));
        }
        angle -= Math.PI - capAngle(match.p1.angles[match.start1]) - capAngle(match.p2.reverseAngles[((match.start2-match.length)+match.p2.reverseAngles.length)%match.p2.reverseAngles.length]);
        Log.d("Angle",""+toDegrees(capAngle(Math.PI - match.p1.angles[match.start1] - match.p2.reverseAngles[(match.start2-match.length+match.p2.reverseAngles.length)%match.p2.reverseAngles.length])));
        x += match.p2.reverseLengths[(match.start2-match.length+match.p2.reverseAngles.length)%match.p2.reverseAngles.length]*Math.cos(angle);
        y += match.p2.reverseLengths[(match.start2-match.length+match.p2.reverseAngles.length)%match.p2.reverseAngles.length]*Math.sin(angle);
//        Log.d("Point",""+(match.start2-match.length));
        Log.d("Corner",""+x+","+y);
        points.addLast(new CoOrdinate(x,y));
        for (int i = match.start2-match.length-1; i >= 0; i--){
            angle += capAngle(match.p2.reverseAngles[i]);
            Log.d("Angle",""+toDegrees(capAngle(match.p2.reverseAngles[i])));
            x += match.p2.reverseLengths[i]*Math.cos(angle);
            y += match.p2.reverseLengths[i]*Math.sin(angle);
//            Log.d("Point",""+i);
            Log.d("Corner",""+x+","+y);
            points.addLast(new CoOrdinate(x,y));
        }
        for (int i = match.p2.size-1; i > match.start2; i--){
            angle += capAngle(match.p2.reverseAngles[i]);
            Log.d("Angle",""+toDegrees(capAngle(match.p2.reverseAngles[i])));
            x += match.p2.reverseLengths[i]*Math.cos(angle);
            y += match.p2.reverseLengths[i]*Math.sin(angle);
//            Log.d("Point",""+i);
            Log.d("Corner",""+x+","+y);
            points.addLast(new CoOrdinate(x,y));
        }
        angle -= capAngle(Math.PI - match.p1.angles[match.start1+match.length] - match.p2.reverseAngles[match.start2]);
        Log.d("Angle",""+toDegrees(capAngle(Math.PI - match.p1.angles[match.start1+match.length] - match.p2.reverseAngles[match.start2])));
        x += match.p1.lengths[match.start1+match.length]*Math.cos(angle);
        y += match.p1.lengths[match.start1+match.length]*Math.sin(angle);
//        Log.d("Point",""+(match.start1+match.length));
        Log.d("Corner",""+x+","+y);
        points.addLast(new CoOrdinate(x,y));
        for (int i = match.start1+match.length+1; i < match.p1.size; i++){
            angle += capAngle(match.p1.angles[i]);
            Log.d("Angle",""+toDegrees(capAngle(match.p1.angles[i])));
            x += match.p1.lengths[i]*Math.cos(angle);
            y += match.p1.lengths[i]*Math.sin(angle);
//            Log.d("Point",""+i);
            Log.d("Corner",""+x+","+y);
            points.addLast(new CoOrdinate(x,y));
        }
        return new Polygon(points);
    }

    public static double toDegrees(double angle){
        return angle*180/Math.PI;
    }

    public static double capAngle(double angle){
        return (angle+2*Math.PI)%Math.PI;
    }
}
