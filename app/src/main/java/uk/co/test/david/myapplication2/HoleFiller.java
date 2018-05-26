package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 16/04/2018.
 */

public class HoleFiller extends ProcessingTask {
    boolean[][] map;
    Bitmap newImage;

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task", "HoleFiller");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        newImage = image.copy(image.getConfig(), true);
        map = new boolean[image.getWidth()][image.getHeight()];
        for (int loop = 0; loop < 2; loop++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {
                for (int y = 1; y < image.getHeight() - 1; y++) {
                    map[x][y] = getColours(newImage.getPixel(x, y))[1]>32;
                }
            }
            for (int x = 1; x < image.getWidth() - 1; x++) {
                for (int y = 1; y < image.getHeight() - 1; y++) {
                    fill(x, y);
                }
            }
        }
        Log.d("HoleFiller","Done");
        return newImage;
    };

    private void fill(int x, int y){
        for (int i = -1; i <= 1; i++){
            if (map[x+i][y+1]){
                for (int j = -1; j <= 1; j++){
                    if (j == i){
                        continue;
                    }
                    if (map[x+j][y-1]){
                        newImage.setPixel(x,y,white);
                        return;
                    }
                }
            }
            if (map[x+i][y-1]){
                for (int j = -1; j <= 1; j++){
                    if (j == i){
                        continue;
                    }
                    if (map[x+j][y+1]){
                        newImage.setPixel(x,y,white);
                        return;
                    }
                }
            }
            if (map[x-1][y+i]){
                for (int j = -1; j <= 1; j++){
                    if (j == i){
                        continue;
                    }
                    if (map[x+1][y+j]){
                        newImage.setPixel(x,y,white);
                        return;
                    }
                }
            }
            if (map[x+1][y+i]){
                for (int j = -1; j <= 1; j++){
                    if (j == i){
                        continue;
                    }
                    if (map[x-1][y+j]){
                        newImage.setPixel(x,y,white);
                        return;
                    }
                }
            }
        }
    }
}
