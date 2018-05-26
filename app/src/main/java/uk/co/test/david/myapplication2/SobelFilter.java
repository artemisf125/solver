package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.util.LinkedList;

import uk.co.test.david.myapplication2.R;

/**
 * Created by David on 16/04/2018.
 */

public class SobelFilter extends ProcessingTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task","SobelFilter");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        if (objects.length > 3) {
            returnMap = (int[][][]) objects[3];
        }
        Bitmap newImage = image.copy(image.getConfig(), true);
        int[][][] map ;
        if (returnMap == null) {
            map = new int[image.getWidth()][image.getHeight()][4];
            if (objects.length > 3) {
                map = (int[][][]) objects[3];
            } else {
                for (int x = 0; x < image.getWidth(); x++) {
                    for (int y = 0; y < image.getHeight(); y++) {
                        map[x][y] = getColours(image.getPixel(x, y));
                    }
                }
            }
        } else {
            map = returnMap;
        }
        int[][] xFilter = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        int[][] yFilter = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                double gx = 0;
                double gy = 0;
                double cnx = 0;
                double cny = 0;
                for (int cn = 0; cn < 3; cn++) {
                    for (int xf = 0; xf < 3; xf++) {
                        for (int yf = 0; yf < 3; yf++) {
                            cnx += map[x + xf - 1][y + yf - 1][cn] * xFilter[xf][yf];
                            cny += map[x + xf - 1][y + yf - 1][cn] * yFilter[xf][yf];
                        }
                    }
                }
                gx += cnx / 3;
                gy += cny / 3;

                double g = Math.sqrt(gx * gx + gy * gy);
//                            Log.d("Image","" + getEncodedColour(255,(int)g,(int)g,(int)g));
                newImage.setPixel(x, y, getEncodedColour((int) g, (int) g, (int) g));
            }
        }
        returnMap = null;
        return newImage;
    }
}
