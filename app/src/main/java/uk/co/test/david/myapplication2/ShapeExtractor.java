package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 17/04/2018.
 */

public class ShapeExtractor extends ProcessingTask {

    Bitmap shapeImage;
    double theta;

    public ShapeExtractor(Bitmap shapeImage, double theta){
        super();
        this.shapeImage = shapeImage;
        this.theta = theta;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task", "ShapeExtractor");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        boolean[][] map = new boolean[shapeImage.getWidth()][shapeImage.getHeight()];
        for (int x = 0; x < shapeImage.getWidth(); x++) {
            for (int y = 0; y < shapeImage.getHeight(); y++) {
                map[x][y] = getColours(shapeImage.getPixel(x, y))[1] > 32;
            }
        }
        int width = shapeImage.getWidth();
        int height = shapeImage.getHeight();
        int size = (int)(Math.sqrt(Math.pow(width,2)+Math.pow(height,2)));
        Bitmap newImage = Bitmap.createBitmap(size,size,shapeImage.getConfig());
        int black = getEncodedColour(0,0,0);
        int white = getEncodedColour(255,255,255);
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                newImage.setPixel(x,y,black);
            }
        }
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                int oldX = x - size/2;
                int oldY = y - size/2;
                int rotatedX = (int)(oldX * Math.cos(theta) - oldY * Math.sin(theta)) + width/2;
                int rotatedY = (int)(oldX * Math.sin(theta) + oldY * Math.cos(theta)) + height/2;
                if (rotatedX < 0 || rotatedX >= width || rotatedY < 0 || rotatedY >= height){
                    continue;
                }
                if (map[rotatedX][rotatedY]) {
                    newImage.setPixel(x, y, white);
                }
            }
        }
        int minX = size;
        int maxX = 0;
        int minY = size;
        int maxY = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (getColours(newImage.getPixel(x,y))[1] > 32){
                    if (x < minX){
                        minX = x;
                    }
                    if (x > maxX){
                        maxX = x;
                    }
                    if (y < minY){
                        minY = y;
                    }
                    if (y > maxY){
                        maxY = y;
                    }
                }
            }
        }
        int newWidth = maxX - minX + 1;
        int newHeight = maxY - minY + 1;
        Bitmap newSmallerImage = Bitmap.createBitmap(newWidth,newHeight,shapeImage.getConfig());
        for (int x = 0; x < newWidth; x++){
            for (int y = 0; y < newHeight; y++){
                if (getColours(newImage.getPixel(x + minX,y + minY))[1] > 32){
                    newSmallerImage.setPixel(x,y,white);
                } else {
                    newSmallerImage.setPixel(x,y,black);
                }
            }
        }
        MainActivity.QuantisationCalculator.lengths.push(newWidth);
        MainActivity.QuantisationCalculator.lengths.push(newHeight);
        tasks.addLast(new ShapeQuantiser(newSmallerImage));
        return newSmallerImage;
    }
}
