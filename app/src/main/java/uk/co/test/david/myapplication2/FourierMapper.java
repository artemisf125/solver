package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;

/**
 * Created by David on 12/05/2018.
 */

public class FourierMapper extends ProcessingTask {
    int ox;
    int oy;
    int[][] rThetaSpace;
    int thetaPrecision = 550;
    int rPrecision = 2;
    int maxR;
    Bitmap shapeImage;
    Bitmap image;
    Bitmap newImage;
    boolean[][] map;

    public FourierMapper(Bitmap shapeImage) {
        super();
        this.shapeImage = shapeImage;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        newImage = image.copy(image.getConfig(), true);
        ox = image.getWidth()/2;
        oy = image.getHeight()/2;
//        ox = 0;
//        oy = 0;
        int oxt = 0;
        int oyt = 0;
        int ot = 0;
        maxR = (image.getWidth() + image.getHeight())*rPrecision/2;
        rThetaSpace = new int[maxR][thetaPrecision+1];
        map = new boolean[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                map[x][y] = getColours(image.getPixel(x, y))[1] > 10;
            }
        }
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y]){
                    oxt += x;
                    oyt += y;
                    ot++;
                }
            }
        }
        ox = oxt/ot;
        oy = oyt/ot;

//        for (int x = 0; x < map.length; x++){
//            for (int y = 0; y < map[0].length; y++){
//                if (map[x][y]){
//                    double dx = x - ox;
//                    double dy = y - oy;
//                    double t = Math.atan2(dy,dx);
//                    double r = Math.sqrt(dx*dx+dy*dx);
//                    Log.d("Theta",""+t);
//                    addToRThetaSpace(r,t);
//                }
//            }
//        }
        for (int r = 0; r < rThetaSpace.length; r++){
            for (int t = 0; t < rThetaSpace[0].length; t++){
//                Log.d("RT:",""+r+","+t);
                double theta = (((double)(t)/thetaPrecision)-0.5)*2*Math.PI;
                double radius = r/rPrecision;
                int x = (int)(radius*Math.cos(theta));
                int y = (int)(radius*Math.sin(theta));
                if (x+ox < 0 || x+ox >= map.length || y+oy < 0 || y+oy >= map[0].length){
                    continue;
                }
                if (map[x+ox][y+oy]){
                    rThetaSpace[r][t] = 50;
                }
//                newImage.setPixel(x+ox,y+oy,getEncodedColour(0,255,0));
            }
        }
        Bitmap graph = Bitmap.createBitmap(rThetaSpace[0].length, rThetaSpace.length, image.getConfig());
        for (int x = 0; x < rThetaSpace.length; x++){
            for (int y = 0; y < rThetaSpace[0].length; y++){
//                graph.setPixel(y,rThetaSpace.length-(x+1),getEncodedColour(rThetaSpace[x][y]*5,rThetaSpace[x][y]*5,rThetaSpace[x][y]*5));
                graph.setPixel(y,rThetaSpace.length-(x+1),black);
                if (x == rThetaSpace.length/2){
                    graph.setPixel(y,rThetaSpace.length-(x+1),getEncodedColour(125,125,125));
                }
            }
        }
        int[] heights = new int[rThetaSpace[0].length];
        for (int t = 0; t < rThetaSpace[0].length; t++){
            for (int r = 0; r < rThetaSpace.length; r++){
                if (rThetaSpace[r][t] == 50){
                    heights[t] = r;
                }
            }
        }
        int outerDeviation = 20;
        int innerDeviation = 5;
        LinkedList<CoOrdinate> corners = new LinkedList<>();
        for (int t = 0; t < heights.length; t++){
            boolean max = true;
            for (int d = -outerDeviation; d < outerDeviation; d++){
                if (Math.abs(d) > innerDeviation && heights[(t+d+heights.length)%heights.length] > heights[t]-2){
                    max = false;
                }
            }
            graph.setPixel(t,rThetaSpace.length-(heights[t]+1),white);
            if (max) {
//                corners.addLast(getCoOrdinate(heights[t],t));
//                graph.setPixel(t,rThetaSpace.length-(heights[t]+1),getEncodedColour(255,0,0));
            }
        }
        int[] differentials = new int[heights.length];
        int range = 5;
        for (int i = 0; i < differentials.length; i++){
//            differentials[i] = 0;
//            for (int n = -range; n < range; n++){
//                if (n >= 0) {
//                    differentials[i] += heights[(i + n) % heights.length];
//                } else {
//                    differentials[i] -= heights[(i+n+heights.length) % heights.length];
//                }
//            }
            differentials[i] = heights[(i + range) % heights.length] - heights[(i-range+heights.length) % heights.length];
//            differentials[i] = heights[i]+heights[(i+1)%heights.length]-heights[(i-1+heights.length)%heights.length]-heights[(i-2+heights.length)%heights.length];
            graph.setPixel(i,rThetaSpace.length/2-((differentials[i]*15)/range),getEncodedColour(0,0,255));
        }
        int[] ddifferentials = new int[heights.length];
        for (int i = 0; i < ddifferentials.length; i++){
            ddifferentials[i] = 0;
//            for (int n = -range; n < range; n++){
//                if (n >= 0) {
//                    ddifferentials[i] += differentials[(i + n) % differentials.length];
//                } else {
//                    ddifferentials[i] -= differentials[(i+n+heights.length) % differentials.length];
//                }
//            }
            ddifferentials[i] = differentials[(i + range) % differentials.length] - differentials[(i-range+differentials.length) % differentials.length];
            graph.setPixel(i,rThetaSpace.length/2-((ddifferentials[i])*10/range),getEncodedColour(0,255,0));
        }
        boolean negative = false;
        int negativeLength = 0;
        int average = 0;
        int averageRange = 5;
        for (int i = -averageRange; i < averageRange; i++){
            average += ddifferentials[(i+ddifferentials.length)%ddifferentials.length];
        }
        for (int i = 0; i < ddifferentials.length || negative; i++) {
            average += ddifferentials[(i+averageRange)%ddifferentials.length] - ddifferentials[(i-averageRange+ddifferentials.length)%ddifferentials.length];
            graph.setPixel(i%ddifferentials.length,rThetaSpace.length/2-((average)/range),getEncodedColour(255,0,255));
            if (negative){
                if (average < -2*averageRange){
                    negativeLength++;
                } else {
                    if (negativeLength > 5) {
                        for (int r = 0; r < rThetaSpace.length; r++) {
                            graph.setPixel((i-negativeLength/2)%ddifferentials.length, r, getEncodedColour(255, 255, 0));
                        }
                        corners.addLast(getCoOrdinate(heights[(i-negativeLength/2 + heights.length)%heights.length],i%ddifferentials.length-negativeLength/2));
                    }
                    negative = false;
                    negativeLength = 0;
                }
            } else {
                if (average < -2*averageRange){
                    negative = true;
                    negativeLength = 0;
                }
            }
        }
        for (CoOrdinate corner:corners){
            newImage.setPixel(corner.x,corner.y,getEncodedColour(255,0,0));
            newImage.setPixel(corner.x+1,corner.y,getEncodedColour(255,0,0));
            newImage.setPixel(corner.x-1,corner.y,getEncodedColour(255,0,0));
            newImage.setPixel(corner.x,corner.y+1,getEncodedColour(255,0,0));
            newImage.setPixel(corner.x,corner.y-1,getEncodedColour(255,0,0));
        }
        MainActivity.Polygons.polygons.add(new Polygon(corners));
        return newImage;
    }


    private void addToRThetaSpace(double r, double t){
        Log.d("theta",""+(t/(2*Math.PI) + 0.5));
        rThetaSpace[(int)(r+maxR)][(int)((t/(2*Math.PI) + 0.5)*thetaPrecision)] = 50;
    }

    private CoOrdinate getCoOrdinate(int r, int t){
        double radius = r/rPrecision;
        double theta = (((double)(t)/thetaPrecision)-0.5)*2*Math.PI;
        return new CoOrdinate((int)(ox+radius*Math.cos(theta)),(int)(oy+radius*Math.sin(theta)));
    }
}
