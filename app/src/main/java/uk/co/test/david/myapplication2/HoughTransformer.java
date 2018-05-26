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

public class HoughTransformer {

    public static final int ThetaPrecision = 150;
    public static final int RPrecision = 2;

    public static int[][] getRThetaSpace(Bitmap image){
        int thetaPrecision;
        int rPrecision;
        int maxR;
        int[][] rThetaSpace;
//        Bitmap newImage;
//        Bitmap shapeImage;

        Log.d("Task","EdgeFinder");
//        imageView = (ImageView) objects[1];
//        tasks = (LinkedList<AsyncTask>) objects[2];
//        newImage = image.copy(image.getConfig(), true);
        int[][] map = new int[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                map[x][y] = getColours(image.getPixel(x, y))[1];
            }
        }

        maxR = (image.getWidth() + image.getHeight())*RPrecision;
        rThetaSpace = new int[maxR*2][ThetaPrecision];
        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++){
                if (map[x][y] == 255){
                    addToRThetaSpace(x,y,maxR,rThetaSpace);
                }
            }
        }

//        LinkedList<Integer> buckets = new LinkedList();
//        for (int x = 0; x < rThetaSpace.length; x++){
//            for (int y = 0; y < rThetaSpace[0].length; y++){
//                buckets.addLast(rThetaSpace[x][y]);
//            }
//        }
//        Collections.sort(buckets);
        return rThetaSpace;
//        for (Integer i:buckets){
//            Log.d("Buckets",i.toString());
//        }
//        Log.d("Bukets Length", ""+buckets.size());
//        int threshold = buckets.getLast();
//        int threshold = buckets.get((int)(buckets.size() * 0.999));
//        Log.d("Threshold", ""+threshold);
//        for (int r = 0; r < rThetaSpace.length; r++){
//            for (int t = 0; t < rThetaSpace[0].length; t++){
//                if (rThetaSpace[r][t] > threshold) {
//                    drawLongestLine(r, t);
//                }
//            }
//        }
//        double longestTheta = 0;
//        for (int r = 0; r < rThetaSpace.length; r++){
//            for (int t = 0; t < rThetaSpace[0].length; t++){
//                if (rThetaSpace[r][t] == threshold) {
//                    drawLongestLine(r, t, thetaPrecision, rPrecision, maxR, image);
//                    longestTheta = t;
//                }
//            }
//        }
//        double t = (((double)longestTheta) / thetaPrecision)*2*Math.PI;
//        for (int r = 0; r < rThetaSpace.length; r++){
//            for (int t = 0; t < rThetaSpace[0].length; t++){
//                if (rThetaSpace[r][t] > threshold) {
//                    drawLine(r, t);
//                }
//            }
//        }

//        Log.d("LineFinder","Done");
//        tasks.addFirst(new ShapeExtractor(shapeImage, t));
//        return newImage;
    }

    private static void addToRThetaSpace(int x, int y, int maxR, int[][] rThetaSpace){
        for (int t = 0 ; t < ThetaPrecision; t++){
            double theta = (((double)t) / ThetaPrecision)*2*Math.PI;
            double r = x*Math.cos(theta) + y * Math.sin(theta);
            rThetaSpace[(int)(r*RPrecision) + maxR][t] += 1;
        }
    }

//    private static void drawLongestLine(int r, int theta, int thetaPrecision, int rPrecision, int maxR, Bitmap image){
//        double t = (((double)theta) / thetaPrecision)*2*Math.PI;
//        boolean[] partOfLineX = new boolean[image.getWidth()];
//        boolean[] partOfLineY = new boolean[image.getHeight()];
//        int[] ys = new int[image.getWidth()];
//        int[] xs = new int[image.getHeight()];
//        if (Math.sin(t) != 0) {
//            for (int x = 0; x < image.getWidth(); x++) {
//                int y = (int) ((((r - maxR) / rPrecision) - x * Math.cos(t)) / Math.sin(t));
//                if (y >= 0 && y < image.getHeight()) {
//                    if (getColours(image.getPixel(x, y))[2] > 32) {
//                        partOfLineX[x] = true;
//                        ys[x] = y;
//                    }
//                }
//            }
//        }
//        if (Math.cos(t) != 0) {
//            for (int y = 0; y < image.getHeight(); y++) {
//                int x = (int) ((((r - maxR) / rPrecision) - y * Math.sin(t)) / Math.cos(t));
//                if (x >= 0 && x < image.getWidth()) {
//                    if (getColours(image.getPixel(x, y))[2] > 32) {
//                        partOfLineY[y] = true;
//                        xs[y] = x;
//                    }
//                }
//            }
//        }
//        boolean[][] partsOfLine = {partOfLineX, partOfLineY};
//        LinkedList<Line> lines = new LinkedList<>();
//        for (int n = 0; n <= 1; n++) {
//            int white = 0;
//            int black = 0;
//            boolean[] whites = new boolean[10];
//            int lineStart = 0;
//            int lineEnd = 0;
//            for (int i = 0; i < partsOfLine[n].length; i++) {
//                if (partsOfLine[n][i]) {
//                    if (lineStart == -1) {
//                        lineStart = i;
//                    }
//                    whites[i % 10] = true;
//                } else {
//                    whites[i % 10] = false;
//                    white = 0;
//                    black = 0;
//                    for (int j = 0; j < whites.length; j++) {
//                        if (whites[j]) {
//                            white++;
//                        } else {
//                            black++;
//                        }
//                    }
//                    if (white / black < 0.8 * whites.length) {
//                        if (lineStart != -1) {
//                            lineEnd = i - whites.length / 2;
//                            if (lineEnd > lineStart) {
//                                if (n == 0) {
//                                    lines.push(new Line(lineStart, ys[lineStart], lineEnd, ys[lineEnd]));
//                                } else {
////                                    lines.push(new Line(xs[lineStart], lineStart, xs[lineEnd], lineEnd));
//                                }
//                                lineStart = -1;
//                            }
//                        }
//                        white = 0;
//                        black = 0;
//                    }
//                }
//            }
//        }
//        Line longest = new Line(0,0,0,0);
//        double longestLength = 0;
//        for (Line line:lines){
//            if (line.length > longestLength){
//                longestLength = line.length;
//                longest = line;
//            }
//        }
////        drawLine(r,theta,longest);
//    }
//
//    private static void drawLine(int r, int theta, int thetaPrecision, int rPrecision, int maxR, Bitmap image, Bitmap newImage){
//        double t = (((double)theta) / thetaPrecision)*2*Math.PI;
//        if (Math.sin(t) != 0) {
//            for (int x = 0; x < image.getWidth(); x++) {
//                int y = (int) ((((r - maxR) / rPrecision) - x * Math.cos(t)) / Math.sin(t));
//                if (y >= 0 && y < image.getHeight()) {
//                    if (newImage.getPixel(x,y) != getEncodedColour(0,255,0)) {
//                        newImage.setPixel(x, y, getEncodedColour(255, 0, 0));
//                    }
//                    if (newImage.getPixel(x,y) == getEncodedColour(255,255,255)) {
//                        newImage.setPixel(x, y, getEncodedColour(255, 255, 0));
//                    }
//                }
//            }
//        }
//        if (Math.cos(t) != 0) {
//            for (int y = 0; y < image.getHeight(); y++) {
//                int x = (int) ((((r - maxR) / rPrecision) - y * Math.sin(t)) / Math.cos(t));
//                if (x >= 0 && x < image.getHeight()) {
//                    if (newImage.getPixel(x,y) != getEncodedColour(0,255,0)) {
//                        if (newImage.getPixel(x,y) != getEncodedColour(0,0,0) && newImage.getPixel(x,y) != getEncodedColour(255,0,0)) {
//                            newImage.setPixel(x, y, getEncodedColour(255, 255, 0));
//                        } else {
//                            newImage.setPixel(x, y, getEncodedColour( 255, 0, 0));
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private static void drawLine(int r, int theta, Line line, int thetaPrecision, int rPrecision, int maxR, Bitmap image, Bitmap newImage){
//        double t = (((double)theta) / thetaPrecision)*2*Math.PI;
//        if (Math.sin(t) != 0) {
//            for (int x = line.start; x <= line.end; x++) {
//                int y = (int) ((((r - maxR) / rPrecision) - x * Math.cos(t)) / Math.sin(t));
//                if (y >= 0 && y < image.getHeight()) {
//                    newImage.setPixel(x, y, getEncodedColour(0, 255, 0));
//                }
//            }
//        }
//        if (Math.cos(t) != 0) {
//            int minY = line.startY;
//            int maxY = line.endY;
//            if (line.startY > line.endY){
//                minY = line.endY;
//                maxY = line.startY;
//            }
//            for (int y = minY; y <= maxY; y++) {
//                int x = (int) ((((r - maxR) / rPrecision) - y * Math.sin(t)) / Math.cos(t));
//                if (x >= 0 && x < image.getWidth()) {
//                    newImage.setPixel(x, y, getEncodedColour(0, 0, 255));
//                }
//            }
//        }
//    }

    protected static int[] getColours(int color) {
        int A = (color >> 24) & 0xff; // or color >>> 24
        int R = (color >> 16) & 0xff;
        int G = (color >> 8) & 0xff;
        int B = (color) & 0xff;
        int[] c = {R, G, B};
        return c;
    }

//    protected static int getEncodedColour(int[] c) {
//        return (255 & 0xff) << 24 | (c[0] & 0xff) << 16 | (c[1] & 0xff) << 8 | (c[2] & 0xff);
//    }
//
//    protected static int getEncodedColour(int R, int G, int B) {
//        return (255 & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
//    }
}
