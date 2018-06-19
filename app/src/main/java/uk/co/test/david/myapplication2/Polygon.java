package uk.co.test.david.myapplication2;

import android.util.Log;

import java.util.LinkedList;
import java.util.Random;

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
    private int colour;
    public int[] colours;
    LinkedList<LinkedList<CoOrdinate>> subShapes = new LinkedList<>();

    public Polygon(LinkedList<CoOrdinate> points){
        for (int i = 0; i < points.size(); i++){
            CoOrdinate p1 = points.get(i);
            CoOrdinate p2 = points.get((i+1)%points.size());
            if (p1.closeTo(p2)){
                points.remove(i);
                i-=1;
            }
        }
        subShapes.add(points);
        size = points.size();
        lengths = new double[size];
        angles = new double[size];
        colours = new int[size];
        Random r = new Random();
        this.colour = getEncodedColour(r.nextInt(255),r.nextInt(255),r.nextInt(255));
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
            angle = capAngle(angle);
//            angle = ((angle+Math.PI)%(Math.PI*2))-Math.PI;
            prevAngle += angle;
            prevAngle = capAngle(prevAngle);
            angles[i%size] = angle;
        }

        this.reverseAngles = new double[size];
        this.reverseLengths = new double[size];
        for (int i = 0; i < size; i++){
            reverseAngles[i] = capAngle(-angles[(size-i)%size]);
            reverseLengths[i] = lengths[(size-1-i)%size];
            colours[i] = colour;
        }
    }

    public static double getAngle(double y,double x){
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

    public LinkedList<PolyMatch> findMatches(Polygon other){
        if (this.size < other.size){
            return other.findMatches(this);
        }
//        int bestLength = 0;
//        int bestOtherStart = 0;
//        int bestThisStart = 0;
        LinkedList<PolyMatch> matches = new LinkedList<>();
        for (int i = 0; i < size; i++){
            LinkedList<int[]> matchList = getMatches(other,i);
            for (int[] match:matchList) {
                if (match[1] > 0) {
                    matches.add(new PolyMatch(this, other, (i + match[0] + 1) % size, other.lengths.length + 1 - (match[0] + match[1]), match[1]));
                }
            }
//            if (match[1] > bestLength){
//                bestLength = match[1];
//                bestOtherStart = match[0];
//                bestThisStart = i;
//            }
        }
//        int[] result = {bestThisStart,bestOtherStart,bestLength};
//        return new PolyMatch(this,other,bestThisStart,bestOtherStart,bestLength);
        return matches;
    }

     private LinkedList<int[]> getMatches(Polygon other, int start){
         LinkedList<int[]> results = new LinkedList<>();
         int maxLength = 0;
         int bestStart = 0;
         boolean match = true;
         int length = 0;
         int mstart = 0;
         for (int i = 0; i < other.size; i++){
             if (similarAngle((angles[(i+start)%size]+Math.PI*2)%(Math.PI*2),((other.reverseAngles[i])+Math.PI*2)%(Math.PI*2))){
                if (match){
//                    length += 1;
                } else {
                    match = true;
//                    length = 1;
                    length = 0;
                    mstart = i;
                }
             } else {
                 if (match){
                     match = false;
                     int[] result = {mstart,length};
                     results.add(result);
//                     if (length > maxLength){
//                         maxLength = length;
//                         bestStart = mstart;
//                     }
                     length = 0;
                 } else {

                 }
             }
             if (similarLength(lengths[(i+start)%size],other.reverseLengths[i])){
                 if (match){
                     length += 1;
                 } else {
                     match = true;
                     length = 1;
                     mstart = i;
                 }
             } else {
                 if (match){
                     match = false;
                     int[] result = {mstart,length};
                     results.add(result);
//                     if (length > maxLength){
//                         maxLength = length;
//                         bestStart = mstart;
//                     }
                     length = 0;
                 } else {

                 }
             }
         }
//         int[] result = {bestStart,maxLength};
         return results;
    }

    private boolean similarLength(double a, double b){
        double threshold = 0.1;
//        Log.d("Similar:",""+(a)+","+(b)+":"+Math.abs((a/b)-1) + ":" + (Math.abs(((a)/(b))-1) < threshold));
        if (b == 0){
            if (a > threshold){
                return false;
            } else {
                return true;
            }
        }
        return Math.abs(((a)/(b))-1) < threshold;
    }

    private boolean similarAngle(double a, double b){
//        Log.d("Similar:",""+(a)+","+(b)+":"+Math.abs((a/b)-1));
        double threshold = 0.2;
        if (b == 0){
            if (a > threshold){
                return false;
            } else {
                return true;
            }
        }
        return Math.abs(((a)/(b))-1) < threshold;
    }

    public static Polygon mergeMatch(PolyMatch match){
        LinkedList<CoOrdinate> points = new LinkedList<>();
        LinkedList<Integer> colours = new LinkedList<>();
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
        if (match.p1 != null && false){
            double x = 0;
            double y = 0;
            double angle = 0;
            points.addLast(new CoOrdinate((int)x,(int)y));
            colours.addLast(match.p1.colours[0]);
            for (int i = 0; i < match.p1.lengths.length; i++){
                angle += capAngle(match.p1.angles[i]);
                angle = capAngle(angle);
                Log.d("Angle",""+toDegrees(capAngle(match.p1.angles[i])));
                x += match.p1.lengths[i]*Math.cos(angle);
                y += match.p1.lengths[i]*Math.sin(angle);
                Log.d("Corner",""+x+","+y);
                points.addLast(new CoOrdinate((int)x,(int)y));
                colours.addLast(match.p1.colours[(i)%match.p1.colours.length]);
            }
            Polygon res = new Polygon(points);
//            String s1 = "";
//            for (double l:match.p1.angles){
//                s1 += ", " + l;
//            }
//            String s3 = "";
//            for (double l:res.angles){
//                s3 += ", " + l;
//            }
//            Log.d("ShapeMerge","s1: "+s1);
//            Log.d("ShapeMerge","s3: "+s3);
            return res;
        }
//        for (double a:match.p1.angles){
//            Log.d("P1",""+toDegrees(capAngle(a)));
//        }
//        for (double a:match.p2.reverseAngles){
//            Log.d("P2",""+toDegrees(capAngle(a)));
//        }
        double x = 0;
        double y = 0;
        double angle = 0;
        points.addLast(new CoOrdinate((int)x,(int)y));
        colours.addLast(match.p1.colours[0]);
        int offset = match.start1+match.length - 1;
        double[] p1angles = new double[match.p1.angles.length];
        for (int i = 0; i < p1angles.length; i++){
            p1angles[i] = match.p1.angles[(i+offset+match.p1.angles.length)%match.p1.angles.length];
        }
        double[] p1lengths = new double[match.p1.lengths.length];
        for (int i = 0; i < p1lengths.length; i++){
            p1lengths[i] = match.p1.lengths[(i+offset+match.p1.lengths.length)%match.p1.lengths.length];
        }
        offset = match.start2+match.length - 1;
        double[] p2angles = new double[match.p2.angles.length];
        for (int i = 0; i < p2angles.length; i++){
            p2angles[i] = match.p2.angles[(i+offset+match.p2.angles.length)%match.p2.angles.length];
        }
        double[] p2lengths = new double[match.p2.lengths.length];
        for (int i = 0; i < p2lengths.length; i++){
            p2lengths[i] = match.p2.lengths[(i+offset+match.p2.lengths.length)%match.p2.lengths.length];
        }
        for (int i = 0; i < p1angles.length - match.length; i++){
            angle += capAngle(p1angles[i]);
            angle = capAngle(angle);
//            Log.d("Angle",""+toDegrees(capAngle(p1angles[i])));
            x += p1lengths[i]*Math.cos(angle);
            y += p1lengths[i]*Math.sin(angle);
//            Log.d("Corner",""+x+","+y);
            points.addLast(new CoOrdinate((int)x,(int)y));
            colours.addLast(match.p1.colours[(i+match.start1+match.length)%match.p1.colours.length]);
        }
        for (int i = 0; i < p2angles.length - match.length -1; i++){
            if (i == 0){
                angle += capAngle(Math.PI+p1angles[(p1angles.length - match.length)%p1angles.length]+p2angles[i]);
            } else {
                angle += capAngle(p2angles[i]);
            }
            angle = capAngle(angle);
//            Log.d("Angle",""+toDegrees(capAngle(p2angles[i])));
            x += p2lengths[i]*Math.cos(angle);
            y += p2lengths[i]*Math.sin(angle);
//            Log.d("Corner",""+x+","+y);
            points.addLast(new CoOrdinate((int)x,(int)y));
            colours.addLast(match.p2.colours[(i+match.start2+match.length)%match.p2.colours.length]);
        }
//        for (int i = 0; i < match.start1; i++){
//            angle += capAngle(match.p1.angles[i]);
//            angle = capAngle(angle);
//            Log.d("Angle",""+toDegrees(capAngle(match.p1.angles[i])));
//            x += match.p1.lengths[i]*Math.cos(angle);
//            y += match.p1.lengths[i]*Math.sin(angle);
//            Log.d("Corner",""+x+","+y);
//            points.addLast(new CoOrdinate((int)x,(int)y));
//            colours.addLast(match.p1.colours[(i+1)%match.p1.colours.length]);
//        }
//        angle += Math.PI + match.p1.angles[match.start1] + match.p2.angles[((match.start2-match.length)+match.p2.angles.length)%match.p2.angles.length];
//        angle = capAngle(angle);
//        Log.d("Angle",""+toDegrees(capAngle(Math.PI - match.p1.angles[match.start1] - match.p2.angles[(match.start2-match.length+match.p2.angles.length)%match.p2.angles.length])));
//        x += match.p2.reverseLengths[(match.start2-match.length+match.p2.angles.length)%match.p2.angles.length]*Math.cos(angle);
//        y += match.p2.reverseLengths[(match.start2-match.length+match.p2.angles.length)%match.p2.angles.length]*Math.sin(angle);
////        Log.d("Point",""+(match.start2-match.length));
//        Log.d("Corner",""+x+","+y);
//        points.addLast(new CoOrdinate((int)x,(int)y));
//        colours.addLast(match.p1.colours[match.start1]);
//        for (int i = match.start2-match.length-1; i >= 0; i--){
//            angle += capAngle(match.p2.angles[i]);
//            angle = capAngle(angle);
//            Log.d("Angle",""+toDegrees(capAngle(match.p2.angles[i])));
//            x += match.p2.reverseLengths[i]*Math.cos(angle);
//            y += match.p2.reverseLengths[i]*Math.sin(angle);
////            Log.d("Point",""+i);
//            Log.d("Corner",""+x+","+y);
//            points.addLast(new CoOrdinate((int)x,(int)y));
//            colours.addLast(match.p2.colours[(i+1)%match.p2.colours.length]);
//        }
//        for (int i = match.p2.size-1; i > match.start2; i--){
//            angle += capAngle(match.p2.angles[i]);
//            angle = capAngle(angle);
//            Log.d("Angle",""+toDegrees(capAngle(match.p2.angles[i])));
//            x += match.p2.reverseLengths[i]*Math.cos(angle);
//            y += match.p2.reverseLengths[i]*Math.sin(angle);
////            Log.d("Point",""+i);
//            Log.d("Corner",""+x+","+y);
//            points.addLast(new CoOrdinate((int)x,(int)y));
//            colours.addLast(match.p2.colours[(i+1)%match.p2.colours.length]);
//        }
//        angle += Math.PI + match.p1.angles[(match.start1+match.length)%match.p1.angles.length] + match.p2.angles[match.start2];
//        angle = capAngle(angle);
//        Log.d("Angle",""+toDegrees(capAngle(Math.PI - match.p1.angles[(match.start1+match.length)%match.p1.angles.length] - match.p2.angles[match.start2])));
//        x += match.p1.lengths[(match.start1+match.length)%match.p1.lengths.length]*Math.cos(angle);
//        y += match.p1.lengths[(match.start1+match.length)%match.p1.lengths.length]*Math.sin(angle);
////        Log.d("Point",""+(match.start1+match.length));
//        Log.d("Corner",""+x+","+y);
//        points.addLast(new CoOrdinate((int)x,(int)y));
//        colours.addLast(match.p2.colours[(match.start1+match.length)%match.p2.colours.length]);
//        for (int i = match.start1+match.length+1; i < match.p1.size; i++){
//            angle += capAngle(match.p1.angles[i]);
//            angle = capAngle(angle);
//            Log.d("Angle",""+toDegrees(capAngle(match.p1.angles[i])));
//            x += match.p1.lengths[i]*Math.cos(angle);
//            y += match.p1.lengths[i]*Math.sin(angle);
////            Log.d("Point",""+i);
//            Log.d("Corner",""+x+","+y);
//            points.addLast(new CoOrdinate((int)x,(int)y));
//            colours.addLast(match.p1.colours[(i+1)%match.p1.colours.length]);
//        }
        Polygon res = new Polygon(points);
//        for (int i = 0; i < res.colours.length; i++){
//            res.colours[i] = colours.get(i);
//        }
//        String s1 = "[";
//        for (double l:match.p1.lengths){
//            s1 += ", " + l;
//        }
//        s1 += "], [";
//        for (double l:p1angles){
//            s1 += ", " + l;
//        }
//        s1 += "]";
//        String s2 = "[";
//        for (double l:match.p2.lengths){
//            s2 += ", " + l;
//        }
//        s2 += "], [";
//        for (double l:p2angles){
//            s2 += ", " + l;
//        }
//        s2 += "]";
//        String s3 = "[";
//        for (double l:res.lengths){
//            s3 += ", " + l;
//        }
//        s3 += "], [";
//        for (double l:res.angles){
//            s3 += ", " + l;
//        }
//        s3 += "]";
//        Log.d("ShapeMerge",""+match.start1 + "," + match.start2 + " : " + match.length);
//        Log.d("ShapeMerge","s1: "+s1);
//        Log.d("ShapeMerge","s2: "+s2);
//        Log.d("ShapeMerge","s3: "+s3);
//        Log.d("atan",""+toDegrees(capAngle(getAngle(0,1))));
//        Log.d("atan",""+toDegrees(capAngle(getAngle(1,1))));
//        Log.d("atan",""+toDegrees(capAngle(getAngle(1,0))));
//        Log.d("atan",""+toDegrees(capAngle(getAngle(1,-1))));
//        Log.d("atan",""+toDegrees(capAngle(getAngle(0,-1))));
//        Log.d("atan",""+toDegrees(capAngle(getAngle(-1,-1))));
//        Log.d("atan",""+toDegrees(capAngle(getAngle(-1,0))));
//        Log.d("atan",""+toDegrees(capAngle(getAngle(-1,1))));
//        Log.d("atan","fin");
        LinkedList<LinkedList<CoOrdinate>> newSubshapes = new LinkedList<>();
        newSubshapes.addAll(match.p1.subShapes);
        newSubshapes.addAll(match.p2.subShapes);
        res.subShapes = newSubshapes;
        return res;
    }

    public static double toDegrees(double angle){
        return angle*180/Math.PI;
    }

    public static double capAngle(double angle){
//        Log.d("Angles",""+angle+"="+(angle+2*Math.PI)%(2*Math.PI));
        while (angle < 0){
            angle+=2*Math.PI;
        }
        return angle%(2*Math.PI);
    }

    protected int getEncodedColour(int R, int G, int B) {
        return (255 & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    public boolean isInvalid(){
//        Log.d("IsInvalid","Called");
        LinkedList<Line> lines = new LinkedList<>();
        for (int i = 0; i < points.size(); i++){
            lines.add(new Line(points.get(i),points.get((i+1)%points.size())));
        }
        for (Line line1 : lines){
            for (Line line2 : lines) {
                if (line1 == line2){
                    continue;
                }
                if (line1.intersects(line2)){
//                    Log.d("IsInvalid","returning true");
                    return true;
                }
            }
        }
//        Log.d("IsInvalid","returning false");
        return false;
    }

    public double getArea(){
        double total = 0;
        for (int i = 0; i < points.size(); i++) {
            total += (points.get(i).x * points.get((i+1)%points.size()).y) - (points.get(i).y * points.get((i+1)%points.size()).x);
        }
        return 0.5 * total;
    }

    public double longestLine(){
        double longest = 0;
        for (CoOrdinate point1:points){
            for (CoOrdinate point2:points){
                if (point1 == point2){
                    continue;
                }
                Line l = new Line(point1,point2);
                if (l.length > longest){
                    longest = l.length;
                }
            }
        }
        return longest;
    }
}
