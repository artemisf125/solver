package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.graphics.drawable.shapes.Shape;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 17/04/2018.
 */

public class ShapeQuantiser extends ProcessingTask {

    Bitmap shapeImage;

    public ShapeQuantiser(Bitmap shapeImage){
        this.shapeImage = shapeImage;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task", "GetShapeTask");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
//        Bitmap newImage = image.copy(image.getConfig(), true);
        boolean[][] map = new boolean[shapeImage.getWidth()][shapeImage.getHeight()];
        for (int x = 0; x < shapeImage.getWidth(); x++) {
            for (int y = 0; y < shapeImage.getHeight(); y++) {
                map[x][y] = getColours(shapeImage.getPixel(x, y))[1] > 32;
            }
        }

        int stepSize = MainActivity.QuantisationCalculator.getStepSize();
        Log.d("ShapeQuantiser","" + shapeImage.getWidth() + "," + shapeImage.getHeight() + ":" + stepSize);
        int posX = stepSize/2;
        int posY = stepSize/2;
        int width = Math.round((float)(shapeImage.getWidth())/stepSize);
        int height = Math.round((float)(shapeImage.getHeight())/stepSize);
        int stepSizeX = shapeImage.getWidth()/width;
        int stepSizeY = shapeImage.getHeight()/height;
        Log.d("ShapeQuansier","height: "+height);
        boolean[][] shape = new boolean[width][height];
        int x = 0;
        int y = 0;
        int red = getEncodedColour(255,0,0);
        int green = getEncodedColour(0,255,0);
        Log.d("ShapeQuansier","y: "+y + ", posY: " + posY);
        int baseX;
        int baseY;
        while (y < height){
            posX = stepSizeX/2;
            x = 0;
            baseY = y*stepSizeY;
            while (x < width) {
                shape[x][y] = map[posX][posY];
                baseX = x*stepSizeX;
                int white = 0;
                int black = 0;
                for (int boxX = 0; boxX < stepSizeX && baseX+boxX < shapeImage.getWidth(); boxX++){
                    for (int boxY = 0; boxY < stepSizeX && baseY+boxY < shapeImage.getHeight(); boxY++){
                        if (map[baseX + boxX][baseY + boxY]){
                            white++;
                        } else {
                            black++;
                        }
                    }
                }
                shape[x][y] = white >= black;
                for (int edgeX = 0; edgeX < stepSizeX && baseX+edgeX < shapeImage.getWidth(); edgeX++){
                    shapeImage.setPixel(baseX + edgeX,baseY,green);
                }
                int edge = baseY + stepSizeY - 1;
                if (edge >= shapeImage.getHeight()){
                    edge = shapeImage.getHeight()-1;
                }
                for (int edgeX = 0; edgeX < stepSizeX && baseX+edgeX < shapeImage.getWidth(); edgeX++){
                    shapeImage.setPixel(baseX + edgeX,edge,green);
                }
                for (int edgeY = 0; edgeY < stepSizeX && baseY+edgeY < shapeImage.getHeight(); edgeY++){
                    shapeImage.setPixel(baseX,baseY + edgeY,green);
                }
                edge = baseX + stepSizeX - 1;
                if (edge >= shapeImage.getWidth()){
                    edge = shapeImage.getWidth()-1;
                }
                for (int edgeY = 0; edgeY < stepSizeY && baseY+edgeY < shapeImage.getHeight(); edgeY++){
                    shapeImage.setPixel(edge,baseY + edgeY,green);
                }
                shapeImage.setPixel(posX,posY,red);
                x++;
                posX += stepSizeX;
        }
            y++;
            posY += stepSizeY;
            Log.d("ShapeQuansier","y: "+y + ", posY: " + posY);
        }
        MainActivity.Shapes.shapes.push(shape);
        if (tasks.size() == 0){
            tasks.push(new DisplayShapesTask());
        }
        return shapeImage;
    }

//    @Override
//    public void onPostExecute(Object o){
//        Bitmap newImage = (Bitmap) o;
//        Log.d("App", "Displaying new Image");
//        imageView.setImageBitmap(newImage);
//        try { Thread.sleep(2000); } catch (Exception e) {}
//        if (tasks.size() == 0){
//            Solver solver = new Solver(imageView, true, 0, 0, 0);
//            solver.execute();
//            return;
//        }
//        AsyncTask nextTask = tasks.removeFirst();
//        Object[] objects = {newImage, imageView, tasks, returnMap};
//        nextTask.execute(objects);
//    }

}
