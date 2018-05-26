package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 16/04/2018.
 */

public class BackgroundFiller extends ProcessingTask {

    public int x;
    public int y;
    public int[] colour;

    public BackgroundFiller(int x, int y, int[] colour){
        super();
        this.x = x;
        this.y = y;
        this.colour = colour;
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
        boolean[][] visited = new boolean[image.getWidth()][image.getHeight()];

        int baseColour = map[x][y];
        Log.d("EdgeFinder","BaseColour:" + baseColour);
        LinkedList<CoOrdinate> toDo = new LinkedList<>();
        toDo.push(new CoOrdinate(x,y));
        visited[x][y] = true;

        while(!toDo.isEmpty()){
            CoOrdinate current = toDo.removeFirst();
            if (map[current.x][current.y] == baseColour){
                newImage.setPixel(current.x,current.y,getEncodedColour(this.colour));
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

        return newImage;
    }
}