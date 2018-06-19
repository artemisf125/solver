package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 16/04/2018.
 */

public class ImageScaler extends ProcessingTask {

    private String puzzleName;

    @Override
    protected Object doInBackground(Object[] objects) {
//        while (MainActivity.running){
//            try {
//                Log.d("Sleeping",puzzleName);
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        MainActivity.running = true;
//        Log.d("Running",puzzleName);
//        MainActivity.problemName = puzzleName;
        Log.d("Task","ImageScaler");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        int size = image.getWidth()*image.getHeight();
        Log.d("Task","Size:"+size);
        int scale = (int)Math.sqrt(size/300000);
        if (scale <= 0){
            return scaleUp(image,(int)(1.0/Math.sqrt(((double)size)/1800000)));
        }
        Log.d("Task","Scale:"+scale);
        Bitmap newImage = Bitmap.createBitmap((int)image.getWidth()/scale,(int)image.getHeight()/scale,image.getConfig());
        for (int x = 0; x < newImage.getWidth(); x++){
            for (int y = 0; y < newImage.getHeight(); y++){
                newImage.setPixel(x,y,image.getPixel(x*scale,y*scale));
            }
        }
        return newImage;
    }

    private Bitmap scaleUp(Bitmap image, int scale){
        Bitmap newImage = Bitmap.createBitmap(image.getWidth()*scale,image.getHeight()*scale,image.getConfig());
        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++){
                for (int xt = 0; xt < scale; xt++){
                    for (int yt = 0; yt < scale; yt++){
                        newImage.setPixel(x*scale+xt,y*scale+yt,image.getPixel(x,y));
                    }
                }
            }
        }
        return newImage;
    }
}
