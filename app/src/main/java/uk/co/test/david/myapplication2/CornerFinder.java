package uk.co.test.david.myapplication2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by David on 11/05/2018.
 */

public class CornerFinder extends ProcessingTask {

    Bitmap shapeImage;
    Bitmap image;
    Bitmap newImage;
    int maxR;

    public CornerFinder(Bitmap shapeImage) {
        super();
        this.shapeImage = shapeImage;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        image = (Bitmap) objects[0];
        imageView = (ImageView) objects[1];
        tasks = (LinkedList<AsyncTask>) objects[2];
        newImage = image.copy(image.getConfig(), true);
        maxR = (image.getWidth() + image.getHeight()) * HoughTransformer.RPrecision;
        int[][] rThetaSpace = HoughTransformer.getRThetaSpace(image);

        LinkedList<HoughPoint> buckets = new LinkedList();
        HoughPoint[][] rThetaSpacePoints = new HoughPoint[rThetaSpace.length][rThetaSpace[0].length];
        for (int x = 0; x < rThetaSpace.length; x++) {
            for (int y = 0; y < rThetaSpace[0].length; y++) {
                buckets.addLast(new HoughPoint(x, y, rThetaSpace[x][y]));
                rThetaSpacePoints[x][y] = buckets.getLast();
            }
        }
        Collections.sort(buckets);
//        Log.d("Sizes",""+buckets.getFirst()+","+buckets.getLast());
//        int threshold = buckets.get((int)(buckets.size()-1));
//        int threshold = buckets.get((int)(buckets.size()*0.99)).value;
//        Log.d("Sizes",""+threshold);
//        double longestTheta = 0;
        Bitmap values = Bitmap.createBitmap(newImage.getWidth(), newImage.getHeight(), newImage.getConfig());
        for (int x = 0; x < values.getWidth(); x++) {
            for (int y = 0; y < values.getHeight(); y++) {
                values.setPixel(x, y, black);
            }
        }
//        LinkedList<Integer> lengths = new LinkedList<>();
//        LinkedList<Integer> lengths2 = new LinkedList<>();
//        for (int r = 0; r < rThetaSpace.length; r++){
//            for (int t = 0; t < rThetaSpace[0].length; t++){
//                if (rThetaSpace[r][t] >= threshold) {
//                    int length = drawLine(r, t, values);
//                    lengths.push(length);
//                    lengths2.push(length);
////                    longestTheta = t;
//                }
//            }
//        }
//        Collections.sort(lengths2);
//        int lengthThreshold = lengths2.get((int)(lengths2.size()*0.99));
//        Log.d("Lengths",""+lengthThreshold);
//        int i = 0;
//        for (int r = 0; r < rThetaSpace.length; r++) {
//            for (int t = 0; t < rThetaSpace[0].length; t++) {
//                if (rThetaSpace[r][t] >= threshold) {
//                    if (lengths.get(i) > lengthThreshold) {
//                        drawLine(r, t);
//                    }
//                    i++;
//                }
//            }
//        }
//        Bitmap graph = Bitmap.createBitmap(rThetaSpace.length, rThetaSpace[0].length, image.getConfig());
//        for (int x = 0; x < rThetaSpace.length; x++){
//            for (int y = 0; y < rThetaSpace[0].length; y++){
//                graph.setPixel(x,y,getEncodedColour(rThetaSpace[x][y],rThetaSpace[x][y],rThetaSpace[x][y]));
//            }
//        }
//        for (int x = 0; x < rThetaSpace.length; x++) {
//            for (int y = 0; y < rThetaSpace[0].length; y++) {
//                if (rThetaSpace[x][y] >= threshold){
//                    int bestX = 0;
//                    int bestY = 0;
//                    int bestValue = 0;
//                    int radius = (int)(rThetaSpace.length/2);
//                    for (int xt = 0; xt < rThetaSpace.length; xt++){
//                        for (int yt = 0; yt < rThetaSpace[0].length; yt++){
//                            if ((xt-x)*(xt-x) + (yt-y)*(yt-y) < radius){
//                                if (rThetaSpace[xt][yt] > bestValue){
//                                    bestValue = rThetaSpace[xt][yt];
//                                    bestX = xt;
//                                    bestY = yt;
//                                }
//                                rThetaSpace[xt][yt] = 0;
//                            }
//                        }
//                    }
//                    drawLongestLine(x,y);
//                }
//            }
//        }
        int radius = 400;
        int difference = 20;
        while (difference > 0){
            HoughPoint strongest = buckets.getLast();
            int strength = strongest.value;
            Log.d("Strongest Value",""+strength);
            for (int x = -radius; x < radius; x++){
                for (int y = -radius; y < radius; y++){
                    if (x*x+y*y < radius && strongest.r+x >= 0 && strongest.r+x < rThetaSpacePoints.length && strongest.t+y >= 0 && strongest.t+y < rThetaSpacePoints[0].length){
                        rThetaSpacePoints[strongest.r+x][strongest.t+y].value = 0;
                    }
                }
            }
            drawLine(strongest.r,strongest.t);
            Collections.sort(buckets);
//            difference = strength - buckets.getLast().value;
            difference--;
            Log.d("difference",""+difference);
        }
        Bitmap graph = Bitmap.createBitmap(rThetaSpace.length, rThetaSpace[0].length, image.getConfig());
        for (int x = 0; x < rThetaSpace.length; x++){
            for (int y = 0; y < rThetaSpace[0].length; y++){
                graph.setPixel(x,y,getEncodedColour(rThetaSpacePoints[x][y].value*5,rThetaSpacePoints[x][y].value*5,rThetaSpacePoints[x][y].value*5));
            }
        }

        return newImage;
    }

    private void drawLongestLine(int r, int theta){
        double t = (((double)theta) / HoughTransformer.ThetaPrecision)*2*Math.PI;
        boolean[] partOfLineX = new boolean[image.getWidth()];
        boolean[] partOfLineY = new boolean[image.getHeight()];
        int[] ys = new int[image.getWidth()];
        int[] xs = new int[image.getHeight()];
        if (Math.sin(t) != 0) {
            for (int x = 0; x < image.getWidth(); x++) {
                int y = (int) ((((r - maxR) / HoughTransformer.RPrecision) - x * Math.cos(t)) / Math.sin(t));
                if (y >= 0 && y < image.getHeight()) {
                    if (getColours(image.getPixel(x, y))[2] > 32) {
                        partOfLineX[x] = true;
                        ys[x] = y;
                    }
                }
            }
        }
        if (Math.cos(t) != 0) {
            for (int y = 0; y < image.getHeight(); y++) {
                int x = (int) ((((r - maxR) / HoughTransformer.RPrecision) - y * Math.sin(t)) / Math.cos(t));
                if (x >= 0 && x < image.getWidth()) {
                    if (getColours(image.getPixel(x, y))[2] > 32) {
                        partOfLineY[y] = true;
                        xs[y] = x;
                    }
                }
            }
        }
        boolean[][] partsOfLine = {partOfLineX, partOfLineY};
        LinkedList<Line> lines = new LinkedList<>();
        for (int n = 0; n <= 1; n++) {
            int white = 0;
            int black = 0;
            boolean[] whites = new boolean[10];
            int lineStart = 0;
            int lineEnd = 0;
            for (int i = 0; i < partsOfLine[n].length; i++) {
                if (partsOfLine[n][i]) {
                    if (lineStart == -1) {
                        lineStart = i;
                    }
                    whites[i % 10] = true;
                } else {
                    whites[i % 10] = false;
                    white = 0;
                    black = 0;
                    for (int j = 0; j < whites.length; j++) {
                        if (whites[j]) {
                            white++;
                        } else {
                            black++;
                        }
                    }
                    if (white / black < 0.8 * whites.length) {
                        if (lineStart != -1) {
                            lineEnd = i - whites.length / 2;
                            if (lineEnd > lineStart) {
                                if (n == 0) {
                                    lines.push(new Line(lineStart, ys[lineStart], lineEnd, ys[lineEnd]));
                                } else {
//                                    lines.push(new Line(xs[lineStart], lineStart, xs[lineEnd], lineEnd));
                                }
                                lineStart = -1;
                            }
                        }
                        white = 0;
                        black = 0;
                    }
                }
            }
        }
        Line longest = new Line(0,0,0,0);
        double longestLength = 0;
        for (Line line:lines){
            if (line.length > longestLength){
                longestLength = line.length;
                longest = line;
            }
        }
        drawLine(r,theta,longest);
    }


    private void drawLine(int r, int theta){
        double t = (((double)theta) / HoughTransformer.ThetaPrecision)*2*Math.PI;
        if (Math.sin(t) != 0) {
            for (int x = 0; x < newImage.getWidth(); x++) {
                int y = (int) ((((r - maxR) / HoughTransformer.RPrecision) - x * Math.cos(t)) / Math.sin(t));
                if (y >= 0 && y < image.getHeight()) {
                    if (newImage.getPixel(x,y) != getEncodedColour(0,255,0)) {
                        newImage.setPixel(x, y, getEncodedColour(255, 0, 0));
                    }
                    if (newImage.getPixel(x,y) == getEncodedColour(255,255,255)) {
                        newImage.setPixel(x, y, getEncodedColour(255, 255, 0));
                    }
                }
            }
        }
        if (Math.cos(t) != 0) {
            for (int y = 0; y < image.getHeight(); y++) {
                int x = (int) ((((r - maxR) / HoughTransformer.RPrecision) - y * Math.sin(t)) / Math.cos(t));
                if (x >= 0 && x < newImage.getWidth()) {
                    if (newImage.getPixel(x,y) != getEncodedColour(0,255,0)) {
                        if (newImage.getPixel(x,y) != getEncodedColour(0,0,0) && newImage.getPixel(x,y) != getEncodedColour(255,0,0)) {
                            newImage.setPixel(x, y, getEncodedColour(255, 255, 0));
                        } else {
                            newImage.setPixel(x, y, getEncodedColour( 255, 0, 0));
                        }
                    }
                }
            }
        }
    }

    private int drawLine(int r, int theta, Bitmap values){
        Bitmap image = Bitmap.createBitmap(newImage.getWidth(),newImage.getHeight(),newImage.getConfig());
        for (int x = 0; x < values.getWidth(); x++){
            for (int y = 0; y < values.getHeight(); y++){
                image.setPixel(x,y,black);
            }
        }
        double t = (((double)theta) / HoughTransformer.ThetaPrecision)*2*Math.PI;
        if (Math.sin(t) != 0) {
            for (int x = 0; x < values.getWidth(); x++) {
                int y = (int) ((((r - maxR) / HoughTransformer.RPrecision) - x * Math.cos(t)) / Math.sin(t));
                if (y >= 0 && y < values.getHeight()) {
                    image.setPixel(x,y,white);
                }
            }
        }
        if (Math.cos(t) != 0) {
            for (int y = 0; y < values.getHeight(); y++) {
                int x = (int) ((((r - maxR) / HoughTransformer.RPrecision) - y * Math.sin(t)) / Math.cos(t));
                if (x >= 0 && x < values.getWidth()) {
                    image.setPixel(x,y,white);
                }
            }
        }
        int length = 0;
        for (int x = 0; x < values.getWidth(); x++){
            for (int y = 0; y < values.getHeight(); y++){
                if (image.getPixel(x,y) == white && newImage.getPixel(x,y) == white){
                    int[] colours = getColours(values.getPixel(x,y));
                    for (int i = 0; i < colours.length; i++){
                        length++;
                        colours[i]+=1;
                    }
                    values.setPixel(x,y,getEncodedColour(colours));
                }
            }
        }
        return length;
    }

    private void drawLine(int r, int theta, Line line){
        double t = (((double)theta) / HoughTransformer.ThetaPrecision)*2*Math.PI;
        if (Math.sin(t) != 0) {
            for (int x = line.start; x <= line.end; x++) {
                int y = (int) ((((r - maxR) / HoughTransformer.RPrecision) - x * Math.cos(t)) / Math.sin(t));
                if (y >= 0 && y < image.getHeight()) {
                    newImage.setPixel(x, y, getEncodedColour(0, 255, 0));
                }
            }
        }
        if (Math.cos(t) != 0) {
            int minY = line.startY;
            int maxY = line.endY;
            if (line.startY > line.endY){
                minY = line.endY;
                maxY = line.startY;
            }
            for (int y = minY; y <= maxY; y++) {
                int x = (int) ((((r - maxR) / HoughTransformer.RPrecision) - y * Math.sin(t)) / Math.cos(t));
                if (x >= 0 && x < image.getWidth()) {
                    newImage.setPixel(x, y, getEncodedColour(0, 0, 255));
                }
            }
        }
    }
}
