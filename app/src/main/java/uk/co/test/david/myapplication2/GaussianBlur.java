package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.os.AsyncTask;

import java.util.LinkedList;

/**
 * Created by David on 16/04/2018.
 */

public class GaussianBlur extends ProcessingTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task","GaussianBlur");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        int[][][] map;
        int[][] map2 = new int[image.getWidth()][image.getHeight()];
        if (objects[3] != null){
            map = (int[][][])objects[3];
        } else {
            Log.d("Sizes","Width:"+image.getWidth()+",height:"+image.getHeight());
            map = new int[image.getWidth()][image.getHeight()][3];

            int[] pixels = new int[image.getHeight()*image.getWidth()];
            image.getPixels(pixels,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());

            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    map[x][y] = getColours(pixels[y*image.getWidth()+x]);
                }
            }
        }
        int[] pixels2 = new int[image.getHeight()*image.getWidth()];

        for (int cn = 0; cn < 3; cn++) {

            for (int y = 0; y < image.getHeight(); y++) {
//                int[] cnacc = {map[0][y][0] + map[1][y][0] + map[2][y][0], map[0][y][1] + map[1][y][1] + map[2][y][1], map[0][y][2] + map[1][y][2] + map[2][y][2]};
                int cnacc = map[0][y][cn] + map[1][y][cn] + map[2][y][cn];
//                map2[1][y][0] = cnacc[0];
//                map2[1][y][1] = cnacc[1];
//                map2[1][y][2] = cnacc[2];
                map2[1][y] = cnacc;
                for (int x = 2; x < image.getWidth() - 1; x++) {
//                    for (int cn = 0; cn < 3; cn++) {
//                        cnacc[cn] += map[x + 1][y][cn] - map[x - 2][y][cn];
//                        map2[x][y][cn] = cnacc[cn];
                    cnacc += map[x + 1][y][cn] - map[x - 2][y][cn];
                    map2[x][y] = cnacc;
//                    }
//                image.setPixel(x, y, getEncodedColour(255, (int) cnacc[1]/3, (int) cnacc[2]/3, (int) cnacc[3]/3));
                }
            }
            for (int x = 0; x < image.getWidth(); x++) {
//                int[] cnacc = {map2[x][0][0] + map2[x][1][0] + map2[x][2][0], map2[x][0][1] + map2[x][1][1] + map2[x][2][1], map2[x][0][2] + map2[x][1][2] + map2[x][2][2]};
                int cnacc = map2[x][0] + map2[x][1] + map2[x][2];
//                map[x][1][0] = cnacc[0];
//                map[x][1][1] = cnacc[1];
//                map[x][1][2] = cnacc[2];
                map[x][1][cn] = cnacc;
                map[x][0] = getColours(black);
                map[x][image.getHeight() - 1] = getColours(black);
                for (int y = 2; y < image.getHeight() - 1; y++) {
//                    for (int cn = 0; cn < 3; cn++) {
//                        cnacc[cn] += map2[x][y + 1][cn] - map2[x][y - 2][cn];
//                        map[x][y][cn] = cnacc[cn] / 9;
//                    }
                    cnacc += map2[x][y + 1] - map2[x][y - 2];
                    map[x][y][cn] = cnacc/9;
//                    image.setPixel(x, y, getEncodedColour((int) cnacc[0] / 9, (int) cnacc[1] / 9, (int) cnacc[2] / 9));
                }
            }
        }

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixels2[y*image.getWidth()+x] = getEncodedColour(map[x][y]);
//                image.setPixel(x, y, getEncodedColour(255, (int) map3[x][y][1], (int) map3[x][y][2], (int) map3[x][y][3]));
            }
        }


        returnMap = map;

        image.setPixels(pixels2,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());



//        for (int x = 1; x < image.getWidth() - 1; x++) {
//            for (int y = 1; y < image.getHeight() - 1; y++) {
//                double[] g = {0,0,0};
//                for (int cn = 1; cn <= 3; cn++) {
//                    for (int xf = 0; xf < 3; xf++) {
//                        for (int yf = 0; yf < 3; yf++) {
//                            g[cn-1] += map[x + xf - 1][y + yf - 1][cn];
//                        }
//                    }
//                }
//
//                image.setPixel(x, y, getEncodedColour(255, (int) g[0]/9, (int) g[1]/9, (int) g[2]/9));
//            }
//        }
        return image;
    }
}
