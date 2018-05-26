package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 16/04/2018.
 */

public class EdgeFinder extends ProcessingTask {

    private int threshold = -1;

    public EdgeFinder(){
        super();
    }

    public EdgeFinder(int threshold){
        this.threshold = threshold;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task","EdgeFinder");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        Bitmap newImage = image.copy(image.getConfig(), true);
        int[][] map = new int[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                map[x][y] = getColours(image.getPixel(x, y))[1];
            }
        }
        int[] vals = new int[256];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                vals[map[x][y]]++;
            }
        }
        int total = image.getWidth() * image.getHeight();
        Log.d("Edge Finder", "total:"+total);
        int desired = (int)(total * 0.95);
        Log.d("Edge Finder", "desired:"+desired);
        if (threshold == -1) {
            threshold = 0;
            while (desired > 0) {
                desired -= vals[threshold];
                threshold++;
            }
        }
        Log.d("Edge Finder", "threshold:"+threshold);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int g = 0;
                if (map[x][y] > threshold){
                    g = 255;
                }
                newImage.setPixel(x, y, getEncodedColour(g, g, g));
            }
        }
        return newImage;
    }
}
