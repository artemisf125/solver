package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.graphics.drawable.shapes.Shape;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by David on 20/04/2018.
 */

public class ShapeDisplayer extends AsyncTask {

    ImageView imageView;
    int[][] board;
    int[] colours;

    public static boolean running = false;

    public ShapeDisplayer(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        this.board = (int[][]) objects[0];
        this.colours = (int[])objects[1];
        running = true;
//        Log.d("Solver", "Trying to display new Image");
        int scale = 5;
        int width = board.length;
        int height = board[0].length;
        Bitmap image = Bitmap.createBitmap(width*scale, height*scale, Bitmap.Config.ARGB_8888);
//                for (int y = 0; y < board[0].length; y++){
//                    String s = "";
//                    for (int x = 0; x < board.length; x++){
//                        s += ""+(board[x][y] + 1);
//                    }
//                    Log.d("",s);
//                }
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                for (int xs = 0; xs < scale; xs++){
                    for (int ys = 0; ys < scale; ys++){
                        image.setPixel(x*scale+xs,y*scale+ys,colours[board[x][y]+1]);
                    }
                }
            }
        }
        return image;
    }
    @Override
    protected void onPostExecute(Object o) {
        Bitmap newImage = (Bitmap) o;
//        Log.d("Solver", "Displaying new Image");
        imageView.setImageBitmap(newImage);
        running = false;
    }
}
