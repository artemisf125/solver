package uk.co.test.david.myapplication2;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by David on 13/05/2018.
 */

public class PolySolver extends AsyncTask {

    public LinkedList<Polygon> polygons;
    public ImageView imageView;
    public double boardSize;

    public PolySolver(ImageView imageView, LinkedList<Polygon> polygons){
        this.imageView = imageView;
        this.polygons = polygons;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        solve();
        return null;
    }

    public void solve() {
//        while (polygons.size() > 1) {
//            LinkedList<PolyMatch> matches = new LinkedList<>();
//            for (Polygon poly : polygons) {
//                for (Polygon poly2 : polygons) {
//                    if (poly == poly2) {
//                        continue;
//                    }
//                    PolyMatch res = poly.findMatches(poly2);
//                    matches.push(res);
//                    Log.d("Matches", "" + res.p1 + "," + res.p2 + "," + res.start1 + "," + res.start2 + ":" + res.length);
//                }
//            }
//            Collections.sort(matches);
//            PolyMatch match = matches.getFirst();
//            Log.d("Matches", "" + match.p1 + "," + match.p2 + "," + match.start1 + "," + match.start2 + ":" + match.length);
//            Polygon result = match.getResult();
//            updateImage(match.p1, match.p2, result);
//            polygons.remove(match.p1);
//            polygons.remove(match.p2);
//            polygons.push(result);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        double totalArea = 0;
        for (Polygon polygon:polygons){
            totalArea += polygon.getArea();
        }
        this.boardSize = Math.sqrt(totalArea);
        for (Polygon p:polygons){
            Log.d("Longest",""+p.longestLine());
        }
        Log.d("BoardSize",""+boardSize);
        solve(polygons);
        Log.d("Solver","solver terminated");
    }

    public boolean solve(LinkedList<Polygon> polygons) {
        LinkedList<PolyMatch> matches = getMatches(polygons);
//        Log.d("Matches:", "" + matches.size());
        for (PolyMatch match : matches) {
            Polygon newPiece = match.getResult();
            LinkedList<Polygon> newPieces = (LinkedList<Polygon>)polygons.clone();
            newPieces.remove(match.p1);
            newPieces.remove(match.p2);
            newPieces.add(newPiece);
            updateImage(match.p1,match.p2,newPiece);
//                try {Thread.sleep(200);} catch (Exception e){}
//            Log.d("Longest",""+newPiece.longestLine());
            if (newPiece.isInvalid() || newPiece.longestLine() > boardSize*1.1) {
//                Log.d("Solver","Invalid");
//                try {Thread.sleep(1000);} catch (Exception e){}
                continue;
            } else {
//                Log.d("Solver","Valid");
//                try {Thread.sleep(5000);} catch (Exception e){}
            }
//            Log.d("size",""+newPieces.size() + "," + polygons.size());
            if (finished(newPieces)){
                Log.d("Solver","Finished!");
                return true;
            }
            boolean solved = solve(newPieces);
            if (solved) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<PolyMatch> getMatches(LinkedList<Polygon> polygons) {
        LinkedList<PolyMatch> matches = new LinkedList<>();
        for (Polygon poly : polygons) {
            for (Polygon poly2 : polygons) {
                if (poly == poly2) {
                    continue;
                }
//                PolyMatch res = poly.findMatches(poly2);
                matches.addAll(poly.findMatches(poly2));
//                Log.d("Matches", "" + res.p1 + "," + res.p2 + "," + res.start1 + "," + res.start2 + ":" + res.length);
            }
        }
        Collections.sort(matches);
        return matches;
    }

    public boolean finished(LinkedList<Polygon> polygons){
        return polygons.size() == 1;
    }



//        LinkedList<CoOrdinate> cs = new LinkedList<>();
//        cs.push(new CoOrdinate(0,0));
//        cs.push(new CoOrdinate(50,0));
//        cs.push(new CoOrdinate(50,50));
//        cs.push(new CoOrdinate(100,50));
//        cs.push(new CoOrdinate(100,100));
//        cs.push(new CoOrdinate(0,100));

//        cs.addLast(new CoOrdinate(142,0));
//        cs.addLast(new CoOrdinate(174,21));
//        cs.addLast(new CoOrdinate(148,39));
//        cs.addLast(new CoOrdinate(188,51));
//        cs.addLast(new CoOrdinate(188,64));
//        cs.addLast(new CoOrdinate(195,68));
//        cs.addLast(new CoOrdinate(200,81));
//        cs.addLast(new CoOrdinate(180,94));
//        cs.addLast(new CoOrdinate(138,100));
//        cs.addLast(new CoOrdinate(110,84));
//        cs.addLast(new CoOrdinate(100,51));
//        cs.addLast(new CoOrdinate(104,38));
//        cs.addLast(new CoOrdinate(118,14));

//        cs.addLast(new CoOrdinate(266,533));
//        cs.addLast(new CoOrdinate(381,437));
//        cs.addLast(new CoOrdinate(289,359));
//        cs.addLast(new CoOrdinate(430,308));
//        cs.addLast(new CoOrdinate(430,248));
//        cs.addLast(new CoOrdinate(456,233));
//        cs.addLast(new CoOrdinate(471,175));
//        cs.addLast(new CoOrdinate(403,117));
//        cs.addLast(new CoOrdinate(255,91));
//        cs.addLast(new CoOrdinate(156,160));
//        cs.addLast(new CoOrdinate(118,305));
//        cs.addLast(new CoOrdinate(133,363));
//        cs.addLast(new CoOrdinate(184,472));
//
//        Polygon p = new Polygon(cs);
//        double[] angles = {100,-120,70,-60,45,65,30,45,40,30,10,40};
//        for (int i = 0; i < angles.length; i++){
//            angles[i] = (angles[i]*Math.PI)/180;
//        }
//        String a2 = "";
//        for (double angle:angles){
//            a2+=", "+(angle*180/Math.PI);
//        }
//        Log.d("Angles",a2);
//        double[] lengths = {27.784887978899608, 38.27531841800928, 31.622776601683793, 41.7612260356422, 13.0, 8.06225774829855, 13.92838827718412, 23.853720883753127, 42.42640687119285, 32.2490309931942, 34.48187929913333, 13.601470508735444};
////        for (int i = 0; i < angles.length; i++){
////            lengths[i] = (lengths[i]*30);
////        }
////        p.angles = angles;
////        p.lengths = lengths;
////        p.size = lengths.length;
//        String l = "";
//        for (double length:p.lengths){
//            l+=", "+length;
//        }
//        Log.d("Lengths",l);
//        String a = "";
//        for (double angle:p.angles){
//            a+=", "+(angle*180/Math.PI);
//        }
//        Log.d("Angles",a);
//        updateImage(match.p1,match.p2,match.getResult());
//        Log.d("atan",""+atan(1,0)+","+atan(1,1)+","+atan(0,1)+","+atan(-1,1)+","+atan(-1,0)+","+atan(-1,-1)+","+atan(0,-1)+","+atan(1,-1));
//    }

    private double atan(double x, double y){
        return ((Math.atan2(y,x)+Math.PI*2)%(Math.PI*2))*180/Math.PI;
    }

    private void updateImage(Polygon p1, Polygon p2, Polygon p3){
//        updates++;
//        if (updates%10000 != 0){
//            return;
//        }
//        try {Thread.sleep(100); } catch (Exception e) {}
        if (PolyDisplayer.running){
            return;
        }
        PolyDisplayer.running = true;
//        Log.d("Drawing Image",""+updates);
        PolyDisplayer displayer = new PolyDisplayer(imageView);
//        displayer.execute();

        Polygon[] polys = {p1,p2,p3};
        Object[] o = polys;
        displayer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,o);
//        try {Thread.sleep(1000); } catch (Exception e) {}
    }
}
