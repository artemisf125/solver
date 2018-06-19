package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 16/04/2018.
 */

public abstract class ProcessingTask extends AsyncTask {

    public ImageView imageView;
    public LinkedList<AsyncTask> tasks;
    public int[][][] returnMap = null;
    public int white = getEncodedColour(255,255,255);
    public int black = getEncodedColour(0,0,0);

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        Bitmap newImage = (Bitmap) o;
//        Log.d("App", "Displaying new Image");
        imageView.setImageBitmap(newImage);
//        try { Thread.sleep(2000); } catch (Exception e) {}
        if (tasks.size() == 0){
//            if (MainActivity.cornersBox.isChecked()){
//                Solver solver = new Solver(imageView,true,0,0,0);
//                solver.execute();
//            } else {
//                PolySolver solver = new PolySolver(imageView, MainActivity.Polygons.polygons);
//                solver.execute();
//            }
            return;
        }
        AsyncTask nextTask = tasks.removeFirst();
        Object[] objects = {newImage, imageView, tasks, returnMap};
        nextTask.execute(objects);
    }

    protected int[] getColours(int color) {
        int A = (color >> 24) & 0xff; // or color >>> 24
        int R = (color >> 16) & 0xff;
        int G = (color >> 8) & 0xff;
        int B = (color) & 0xff;
        int[] c = {R, G, B};
        return c;
    }

    protected int getEncodedColour(int[] c) {
        return (255 & 0xff) << 24 | (c[0] & 0xff) << 16 | (c[1] & 0xff) << 8 | (c[2] & 0xff);
    }

    protected int getEncodedColour(int R, int G, int B) {
        return (255 & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    protected int[] toHSL(int[] rgb){
        double[] fixedRGB = new double[3];
        for (int i = 0; i < 3; i++){
            fixedRGB[i] = (double)(rgb[i])/255;
        }
        double min = fixedRGB[0];
        double max = fixedRGB[0];
        int maxCol = 0;
        for (int i = 1; i < 3; i++){
            if (fixedRGB[i] < min){
                min = fixedRGB[i];
            }
            if (fixedRGB[i] > max){
                max = fixedRGB[i];
                maxCol = i;
            }
        }
        double lum = (min+max)/2;
        double sat;
        if (min == max){
            sat = 0;
        } else {
            if (lum > 0.5){
                sat = (max-min)/(max+min);
            } else {
                sat = (max-min)/(2.0-max-min);
            }
        }
        double hue;
        if (maxCol == 0){
            hue = (fixedRGB[1]-fixedRGB[2])/(max-min);
        } else if (maxCol == 1){
            hue = 2.0 + (fixedRGB[2]-fixedRGB[0])/(max-min);
        } else {
            hue = 4.0 + (fixedRGB[0]-fixedRGB[1])/(max-min);
        }
        int[] result = {(int)(hue*100),(int)(sat*100),(int)(lum*100)};
        return result;
    }
}
