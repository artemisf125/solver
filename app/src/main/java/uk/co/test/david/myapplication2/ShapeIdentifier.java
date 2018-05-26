package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by David on 16/04/2018.
 */

public class ShapeIdentifier extends ProcessingTask {
    boolean[][] map;
    Bitmap newImage;
    Bitmap image;
    LinkedList<Integer> colours;
    Random r = new Random();
    LinkedList<LinkedList<CoOrdinate>> shapes;
    LinkedList<Bitmap> shapeImages;

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task", "HoleFiller");
        image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        colours = new LinkedList<>();
        shapes = new LinkedList<>();
        newImage = image.copy(image.getConfig(), true);
        map = new boolean[image.getWidth()][image.getHeight()];
        int white = getEncodedColour(255,255,255);
        int black = getEncodedColour(0,0,0);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                map[x][y] = getColours(newImage.getPixel(x, y))[1] < 32;
                if (getColours(newImage.getPixel(x, y))[1] >= 32){
                    newImage.setPixel(x,y,white);
                }
            }
        }
        int n = 0;
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                if (getColours(newImage.getPixel(x, y))[1] == 0){
                    fillHole(x,y);
                }
            }
        }
        Log.d("HoleFiller", "Done");

        shapeImages = new LinkedList<>();
        for (LinkedList<CoOrdinate> shape:shapes){
            int minX = image.getWidth();
            int minY = image.getHeight();
            int maxX = 0;
            int maxY = 0;
            for (CoOrdinate c:shape){
                if (c.x < minX){
                    minX = c.x;
                }
                if (c.y < minY){
                    minY = c.y;
                }
                if (c.x > maxX){
                    maxX = c.x;
                }
                if (c.y > maxY){
                    maxY = c.y;
                }
            }
            int width = maxX - minX + 1;
            int height = maxY - minY + 1;

            for (CoOrdinate c:shape){
                c.x -= minX;
                c.y -= minY;
            }

            Bitmap shapeImage = Bitmap.createBitmap(width+10,height+10, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width + 10; x++){
                for (int y = 0; y < height + 10; y++){
                    shapeImage.setPixel(x,y,black);
                }
            }
            for (CoOrdinate c:shape){
                shapeImage.setPixel(c.x+5,c.y+5,white);
            }
            shapeImages.addLast(shapeImage);
        }

        for (Bitmap shapeImage:shapeImages){
            tasks.addFirst(new GetShapeTask(shapeImage));
        }

        return newImage;
    }

    public void fillHole(int x, int y){

        boolean[][] visited = new boolean[image.getWidth()][image.getHeight()];

//        int[] baseColour = map[x][y];
        LinkedList<CoOrdinate> toDo = new LinkedList<>();
        toDo.push(new CoOrdinate(x,y));
        visited[x][y] = true;
        LinkedList<CoOrdinate> shape = new LinkedList<>();

        while(!toDo.isEmpty()){
            CoOrdinate current = toDo.removeFirst();
            if (map[current.x][current.y]){
                shape.add(current);
//                newImage.setPixel(current.x,current.y,getEncodedColour(this.colour));
                CoOrdinate[] nextCoOrdinates = {new CoOrdinate(current.x+1,current.y),new CoOrdinate(current.x-1,current.y),new CoOrdinate(current.x,current.y+1),new CoOrdinate(current.x,current.y-1)};
                for (CoOrdinate c:nextCoOrdinates){
                    if (c.x < 0 || c.x >= image.getWidth() || c.y <0 || c.y >= image.getHeight()){
                        continue;
                    }
                    if (!visited[c.x][c.y]){
                        toDo.addLast(c);
                        visited[c.x][c.y] = true;
                    }
                }
            }
        }
        for (CoOrdinate pos:shape) {
            map[pos.x][pos.y] = true;
        }
        if (shape.size() > 500){
            int colour = newColour();
            for (CoOrdinate pos:shape) {
                newImage.setPixel(pos.x, pos.y, colour);
            }
            shapes.addLast(shape);
        } else {
            for (CoOrdinate pos:shape) {
                newImage.setPixel(pos.x, pos.y, getEncodedColour(255, 255, 255));
            }
        }
    }

    private int newColour(){
        int colour = getEncodedColour(r.nextInt(255)+1,r.nextInt(255)+1,r.nextInt(255)+1);
        while (colours.contains(colour)){
            colour = getEncodedColour(r.nextInt(255)+1,r.nextInt(255)+1,r.nextInt(255)+1);
        }
        colours.push(colour);
        return colour;
    }
}
