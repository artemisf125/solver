package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 17/05/2018.
 */

class PolyDisplayer extends AsyncTask {

    ImageView imageView;
    int[][] board;
    int[] colours;
    public static boolean running = false;

    public PolyDisplayer(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Polygon[] polys = (Polygon[]) objects;
        LinkedList<int[]>[] points = new LinkedList[polys.length];
        for (int i = 0; i < polys.length; i++) {
            points[i] = new LinkedList<>();
            int minx = 0;
            int maxx = 0;
            int miny = 0;
            int maxy = 0;
            int[] pos = {0,0};
            points[i].addLast(pos);
            int[] lastPos = pos;
            double angle = 0;
            Log.d("PolySize",""+polys[i].size);
            for (int j = 0; j < polys[i].size; j++) {
                angle += polys[i].angles[j];
                int nextX = lastPos[0] + (int)(polys[i].lengths[j]*Math.cos(angle));
                int nextY = lastPos[1] + (int)(polys[i].lengths[j]*Math.sin(angle));
                Log.d("next",""+nextX+","+nextY);
                if (nextX > maxx){
                    maxx = nextX;
                }
                if (nextX < minx){
                    minx = nextX;
                }
                if (nextY > maxy){
                    maxy = nextY;
                }
                if (nextY < miny){
                    miny = nextY;
                }
                int[] nextPos = {nextX,nextY};
                points[i].addLast(nextPos);
                lastPos = nextPos;
            }
            int rangex = maxx-minx;
            Log.d("rangex",""+rangex);
            double scalex = 100.0/rangex;
            Log.d("scalex",""+scalex);
            int rangey = maxy-miny;
            Log.d("rangey",""+rangey);
            double scaley = 100.0/rangey;
            Log.d("scaley",""+scaley);
            for (int[] point:points[i]){
                point[0] -= minx;
                point[1] -= miny;
                point[0] *= scalex;
                point[1] *= scaley;
                Log.d("Point",""+point[0]+","+point[1]);
            }
            Log.d("PolySize","after: " + points[i].size());
        }
        Bitmap bitmap = Bitmap.createBitmap(300,100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0,0,299,99,paint);
        paint.setColor(Color.WHITE);
        Log.d("PolySize",""+polys.length);
        for (int i = 0; i < polys.length; i++) {
            Path path = new Path();
            Log.d("PolySize","size: " + points[i].size());
            for (int j = 0; j < points[i].size(); j++) {
                int[] p = points[i].get(j);
                if (j == 0) {
                    path.moveTo(p[0] + i * 100, p[1]);
                } else {
                    path.lineTo(p[0] + i * 100, p[1]);
                }
                Log.d("Point",""+(p[0]+i*100)+","+p[1]);
            }
            canvas.drawPath(path, paint);
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Object o) {
        Bitmap newImage = (Bitmap) o;
//        Log.d("Solver", "Displaying new Image");
        imageView.setImageBitmap(newImage);
        running = false;
    }
}
