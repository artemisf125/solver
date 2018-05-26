package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 16/04/2018.
 */

public class GetShapeTask  extends ProcessingTask {

    Bitmap shapeImage;
    boolean checked;

    public GetShapeTask(Bitmap shapeImage){
        super();
        this.shapeImage = shapeImage;
        this.checked = MainActivity.cornersBox.isChecked();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("Task", "GetShapeTask");
        Bitmap image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
//        Bitmap newImage = image.copy(image.getConfig(), true);
        int[][][] map = new int[shapeImage.getWidth()][shapeImage.getHeight()][4];
        for (int x = 0; x < shapeImage.getWidth(); x++) {
            for (int y = 0; y < shapeImage.getHeight(); y++) {
                map[x][y] = getColours(shapeImage.getPixel(x, y));
            }
        }
        try {
//            Thread.sleep(5000);
        } catch (Exception e){

        }
        if (checked) {
            tasks.addFirst(new LineFinder(shapeImage));
        } else {
//            tasks.addFirst(new CornerFinder(shapeImage));
            tasks.addFirst(new FourierMapper(shapeImage));
        }
        tasks.addFirst(new EdgeFinder(10));
        tasks.addFirst(new SobelFilter());
        return shapeImage;
    }


    @Override
    protected void onPostExecute(Object o) {
        Bitmap newImage = (Bitmap) o;
        Log.d("App", "Displaying new Image");
        imageView.setImageBitmap(shapeImage);
//        try { Thread.sleep(4000); } catch (Exception e) {}
        if (tasks.size() == 0){
            return;
        }
        AsyncTask nextTask = tasks.removeFirst();
        Object[] objects = {newImage, imageView, tasks};
        nextTask.execute(objects);
    }
}
