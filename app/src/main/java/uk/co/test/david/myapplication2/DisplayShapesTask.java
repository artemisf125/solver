package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 19/04/2018.
 */

public class DisplayShapesTask extends ProcessingTask {


    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task", "GetShapeTask");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];

        int n = (int)Math.ceil(Math.sqrt((MainActivity.Shapes.shapes.size())));
        LinkedList<LinkedList<boolean[][]>> grid = new LinkedList<>();
        for (int i = 0; i < n; i++){
            LinkedList<boolean[][]> row = new LinkedList<>();
            for (int j = 0; j < n; j++){
                if (i*n+j < MainActivity.Shapes.shapes.size()) {
                    row.add(MainActivity.Shapes.shapes.get(i * n + j));
                }
            }
            grid.add(row);
        }

        int maxX = 0;
        int maxY = 0;

        for (int y = 0; y < grid.size(); y++){
            LinkedList<boolean[][]> row = grid.get(y);
            int currentX = 0;
            for (int x = 0; x < row.size(); x++){
                currentX += row.get(x).length + 1;
            }
            if (currentX > maxX){
                maxX = currentX;
            }
        }

        for (int x = 0; x < grid.size(); x++){
            int currentY = 0;
            for (int y = 0; y < n; y++){
                if (grid.get(y).size() > x) {
                    currentY += grid.get(y).get(x)[0].length + 1;
                }
            }
            if (currentY > maxY){
                maxY = currentY;
            }
        }

        int scale = 5;

        Bitmap newImage = Bitmap.createBitmap(maxX * scale, maxY * scale, image.getConfig());
//        for (int x = 0; x < maxX; x++){
//            for (int y = 0; y < maxY; y++){
//                newImage.setPixel(x,y,black);
//            }
//        }

        int offsetY = 0;
        for (int y = 0; y < grid.size(); y++){
            LinkedList<boolean[][]> row = grid.get(y);
            int offsetX = 0;
            int rowMaxY = 0;
            for (int x = 0; x < row.size(); x++){
                boolean[][] shape = row.get(x);
                for (int sx = 0; sx < shape.length; sx++){
                    for (int sy = 0; sy < shape[0].length; sy++){
                        if (shape[sx][sy]) {
                            for (int xs = 0; xs < scale; xs++){
                                if ((sx + offsetX) * scale + xs >= newImage.getWidth()){
                                    continue;
                                }
                                for (int ys = 0; ys < scale; ys++){
                                    if ((sy + offsetY) * scale + ys >= newImage.getHeight()){
                                        continue;
                                    }
                                    newImage.setPixel((sx + offsetX) * scale + xs, (sy + offsetY) * scale + ys, black);
                                }
                            }
                        } else {
                            for (int xs = 0; xs < scale; xs++){
                                if ((sx + offsetX) * scale + xs >= newImage.getWidth()){
                                    continue;
                                }
                                for (int ys = 0; ys < scale; ys++){
                                    if ((sy + offsetY) * scale + ys >= newImage.getHeight()){
                                        continue;
                                    }
                                    newImage.setPixel((sx + offsetX) * scale + xs, (sy + offsetY) * scale + ys, white);
                                }
                            }
                        }
                    }
                }
                offsetX += shape.length + 1;
                if (shape[0].length > rowMaxY){
                    rowMaxY = shape[0].length;
                }
            }
            offsetY += rowMaxY + 1;
        }

        return newImage;
    }
}
