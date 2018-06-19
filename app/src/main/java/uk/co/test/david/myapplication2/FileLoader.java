package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by David on 26/05/2018.
 */

public class FileLoader extends AsyncTask {

    LinkedList<AsyncTask> tasks = new LinkedList<AsyncTask>();
    String puzzleName;
    ImageView imageView;
    int[] white = {255,255,255};
    public double tolerance = -1;

    public FileLoader(ImageView imageView, String puzzleName){
        Log.d("Creating Test",puzzleName);
        this.imageView = imageView;
        this.puzzleName = puzzleName;
    }
    public FileLoader(ImageView imageView, String puzzleName, double tolerance){
        Log.d("Creating Test",puzzleName);
        this.imageView = imageView;
        this.puzzleName = puzzleName;
        this.tolerance = tolerance;
    }

    @Override
    protected Bitmap doInBackground(Object[] objects) {
//        while (MainActivity.running){
//            try {
//                Log.d("Sleeping",puzzleName);
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        MainActivity.running = true;
        MainActivity.problemName = puzzleName;

        try {
            Log.d("App", "Loading");
            String path = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String filename = path + "/test/"+puzzleName;
            Log.d("image at",filename);
            Bitmap bmp =  BitmapFactory.decodeFile(filename);
            Log.d("App", "Got Image");
            if (bmp == null){
                Log.d("App", "Image is null");
            }
            return bmp;
        } catch (Exception e){
            Log.d("App",e.toString());
        }
        Log.d("App", "Failed");
        return null;
    }@Override
    protected void onPostExecute(Object o) {
        MainActivity.Shapes.shapes = new LinkedList<>();
        MainActivity.Shapes.container = null;
        MainActivity.QuantisationCalculator.lengths = new LinkedList<>();
        MainActivity.QuantisationCalculator.size = -1;

        tasks.addLast(new ImageScaler());
        tasks.addLast(new GaussianBlur());
        tasks.addLast(new GaussianBlur());
        tasks.addLast(new SobelFilter());
        tasks.addLast(new EdgeFinder());
        tasks.addLast(new GaussianBlur());
        tasks.addLast(new HoleFiller());
        tasks.addLast(new BackgroundFiller(10,10,white));
        tasks.addLast(new ShapeIdentifier());
        if (this.tolerance != -1) {
            MainActivity.QuantisationCalculator.tolerance = this.tolerance;
        } else {
            MainActivity.QuantisationCalculator.tolerance = MainActivity.QuantisationCalculator.defaultTolerance;
        }

        if (o == null){
            Log.d("App", "Image null");
        } else {
            Bitmap image = (Bitmap)o;
            Log.d("App", "Displaying Image");
            imageView.setImageBitmap(image);
            Bitmap newImage = image.copy(image.getConfig(), true);
            AsyncTask nextTask = tasks.removeFirst();
            Object[] objects = {newImage, imageView, tasks, null};
            nextTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,objects);
        }
    }
    private int[] getColours(int color){
        int A = (color >> 24) & 0xff; // or color >>> 24
        int R = (color >> 16) & 0xff;
        int G = (color >>  8) & 0xff;
        int B = (color      ) & 0xff;
        int[] c = {A,R,G,B};
        return c;
    }
    private int getEncodedColour(int[] c){
        return (c[0] & 0xff) << 24 | (c[1] & 0xff) << 16 | (c[2] & 0xff) << 8 | (c[3] & 0xff);
    }
    private int getEncodedColour(int A, int R, int G, int B){
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}
