package uk.co.test.david.myapplication2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import android.opengl.*;
import android.graphics.Color;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.LinkedList;

import android.hardware.camera2.*;

import static android.content.ContentValues.TAG;


public class MainActivity extends Activity {
    LinkedList<AsyncTask> tasks = new LinkedList<AsyncTask>();
    public static Button solveButton;
    public static Button hintButton;
    public static Button takePhotoButton;
    public static CheckBox cornersBox;
    public static EditText containerWidth;
    public static EditText containerHeight;
    public static EditText missingPieces;
    public static String Url = "https://writelatex.s3.amazonaws.com/hrxywfmmqywm/uploads/1/24904902/6.bmp?X-Amz-Expires=14400&X-Amz-Date=20180526T180953Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJF667VKUK4OW3LCA/20180526/us-east-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=3d2b9a9a7b3ec7545fd36741a58b236c79c4bbce283e891d9989ad2242cd461e";
//    public static String Url = "https://writelatex.s3.amazonaws.com/zqmkcwksysvp/uploads/985/19552918/1.jpg?X-Amz-Expires=14400&X-Amz-Date=20180520T133700Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJF667VKUK4OW3LCA/20180520/us-east-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=5a83cd54026355140d959423ead4446c69b08f90408b4bccd44cc69955757531";
    public static int solutions = 0;

    public Uri mImageUri;
    public Bitmap image;
    public ImageView imageView;

    public static class QuantisationCalculator{
        public static LinkedList<Integer> lengths;
        private static int size = -1;
        private static double tolerance = 0.2;

        public QuantisationCalculator() {
             lengths = new LinkedList<>();
        }
        public static int getStepSize(){
            if (size != -1){
                return size;
            }
            int greatestNumber = 0;
            double testNumber = 1;
            int smallestNumber = -1;
            for (int l:lengths){
                Log.d("QuantisationCalculator",""+l);
                if (smallestNumber == -1){
                    smallestNumber = l;
                }
                if (l < smallestNumber){
                    smallestNumber = l;
                }
            }
            while(testNumber <= (smallestNumber*(1+tolerance))){
                Log.d("QuantisationCalculator","trying "+testNumber);
                boolean valid = true;
                for (int l:lengths){
                    if (Math.abs(1-((((double)l)/testNumber)/(Math.round(((double)l)/testNumber)))) > tolerance){
                        Log.d("QuantisationCalculator","failed on " + l + " = " +(Math.abs(1-((((double)l)/testNumber)/(Math.round(((double)l)/testNumber))))));
                        valid = false;
                        break;
                    }
                }
                if (valid){
                    greatestNumber = (int)testNumber;
                }
                testNumber++;
            }
            size = greatestNumber;
            Log.d("QuantisationCalculator","result: "+size);
            return size;
        }
    }

    public static class Shapes{
        public static LinkedList<boolean[][]> shapes;
        public static boolean[][] container;
        public Shapes(){
            shapes = new LinkedList<>();
        }
        public static int getTotalPieceArea(){
            int result = 0;
            for (boolean[][] shape:shapes){
                for (int x = 0; x < shape.length; x++){
                    for (int y = 0; y < shape[0].length; y++){
                        if (shape[x][y]){
                            result += 1;
                        }
                    }
                }
            }
            for (boolean[][] shape:shapes){
                int size = 0;
                for (int x = 0; x < shape.length; x++){
                    for (int y = 0; y < shape[0].length; y++){
                        if (shape[x][y]){
                            size += 1;
                        }
                    }
                }
                if (size == result/2){
                    container = shape;
                    result /= 2;
                    shapes.remove(container);
                    break;
                }
            }
            return result;
        }
    }

    public static class Polygons{
        public  static LinkedList<Polygon> polygons = new LinkedList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuantisationCalculator sizeCalc = new QuantisationCalculator();
        final Shapes shapes = new Shapes();

        imageView = (ImageView)findViewById(R.id.iv);
        solveButton = (Button)findViewById(R.id.button3);
        hintButton = (Button)findViewById(R.id.button);
        takePhotoButton = (Button)findViewById(R.id.button2);
        cornersBox = (CheckBox) findViewById(R.id.checkBox2);
        containerWidth = (EditText) findViewById(R.id.editText);
        containerHeight = (EditText) findViewById(R.id.editText2);
        missingPieces = (EditText) findViewById(R.id.editText3);

        takePhotoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File photo;
                try
                {
                    // place where to store camera taken picture
                    photo = this.createTemporaryFile("picture", ".jpg");
                    photo.delete();
                }
                catch(Exception e)
                {
                    Log.v(TAG, "Can't create file to take picture! - " + e.toString());
                    return;
                }
                mImageUri = Uri.fromFile(photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                //start camera intent
//                CameraActivity c = new CameraActivity();
                MainActivity.this.startActivityForResult(intent,0);
            }
            private File createTemporaryFile(String part, String ext) throws Exception
            {
                File tempDir= Environment.getExternalStorageDirectory();
                tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
                if(!tempDir.exists())
                {
                    tempDir.mkdirs();
                }
                return File.createTempFile(part, ext, tempDir);
            }
        });

        solveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                for (boolean[][] shape:Shapes.pieces){
//                    for (int y = 0; y < shape[0].length; y++){
//                        String line = "";
//                        for (int x = 0; x < shape.length; x++){
//                            line += shape[x][y]?"X":" ";
//                        }
//                        Log.d("ButtonPress",line);
//                    }
//                    Log.d("ButtonPress","----");
//                }
//                Log.d("ButtonPress", "" + Shapes.pieces.size() + "pieces found.");
//            }
            @Override
            public void onClick(View view){
                int cwidth;
                int cheight;
                int numMissingPieces;
                boolean cornersChecked;

                cornersChecked = cornersBox.isChecked();
                try {
                    cwidth = Integer.parseInt(containerWidth.getText().toString());
                } catch (NumberFormatException e){
                    cwidth = 0;
                }
                try {
                    cheight = Integer.parseInt(containerHeight.getText().toString());
                } catch (NumberFormatException e){
                    cheight = 0;
                }
                try {
                    numMissingPieces =Integer.parseInt(missingPieces.getText().toString());
                } catch (NumberFormatException e){
                    numMissingPieces = 0;
                }
                if (cornersBox.isChecked()) {
                    Solver solver = new Solver(imageView, cornersChecked, cwidth, cheight, numMissingPieces);
                    solver.execute();
                } else {
                    PolySolver solver = new PolySolver(imageView, Polygons.polygons);
                    solver.execute();
                }
                solveButton.setText("Solving!");
                solveButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                    }
                });
            }
        });
        hintButton.setOnClickListener(new View.OnClickListener() {
            //            @Override
//            public void onClick(View view) {
//                for (boolean[][] shape:Shapes.pieces){
//                    for (int y = 0; y < shape[0].length; y++){
//                        String line = "";
//                        for (int x = 0; x < shape.length; x++){
//                            line += shape[x][y]?"X":" ";
//                        }
//                        Log.d("ButtonPress",line);
//                    }
//                    Log.d("ButtonPress","----");
//                }
//                Log.d("ButtonPress", "" + Shapes.pieces.size() + "pieces found.");
//            }
            @Override
            public void onClick(View view){
                int cwidth;
                int cheight;
                int numMissingPieces;
                boolean cornersChecked;

                if (cornersBox.isChecked()) {
                    cornersChecked = cornersBox.isChecked();
                    try {
                        cwidth = Integer.parseInt(containerWidth.getText().toString());
                    } catch (NumberFormatException e){
                        cwidth = 0;
                    }
                    try {
                        cheight = Integer.parseInt(containerHeight.getText().toString());
                    } catch (NumberFormatException e){
                        cheight = 0;
                    }
                    try {
                        numMissingPieces =Integer.parseInt(missingPieces.getText().toString());
                    } catch (NumberFormatException e){
                        numMissingPieces = 0;
                    }
                    int size = 0;
                    boolean[][] biggest = Shapes.shapes.getFirst();
                    for(boolean[][] s:Shapes.shapes){
                        int ss = 0;
                        for (int i = 0; i < s.length; i++){
                            for (int j = 0; j < s[0].length; j++){
                                if (s[i][j]){
                                    ss++;
                                }
                            }
                        }
                        if (ss > size){
                            size = ss;
                            biggest = s;
                        }
                    }
                    Shapes.shapes.remove(biggest);
                    Shapes.shapes.addFirst(biggest);
                    ImageView tempView = new ImageView(imageView.getContext());
                    Solver solver = new Solver(tempView, cornersChecked, cwidth, cheight, numMissingPieces);
                    solver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);
                    HintGetter hintGetter = new HintGetter(imageView);
                    hintGetter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,solver);
                    hintButton.setText("Getting Hint!");
                } else {
                    // Not Available Yet!
                }
            }
        });

        int[] white = {255,255,255};

        tasks.addLast(new ImageScaler());
        tasks.addLast(new GaussianBlur());
        tasks.addLast(new GaussianBlur());
        tasks.addLast(new SobelFilter());
        tasks.addLast(new EdgeFinder());
        tasks.addLast(new GaussianBlur());
        tasks.addLast(new HoleFiller());
        tasks.addLast(new BackgroundFiller(0,0,white));
        tasks.addLast(new ShapeIdentifier());

        AsyncTask task = new AsyncTask() {
            @Override
            protected Bitmap doInBackground(Object[] objects) {
                try {
                    Log.d("App", "Loading");
//                    URL url = new URL("https://www.doc.ic.ac.uk/~mjw03/PersonalWebpage/Pics/puzzle.jpg");
                    URL url = new URL(Url);
                    URLConnection connection = url.openConnection();
//                    connection.setDoOutput(true);
//                    connection.connect();
//                    connection.getContent();
//                    Log.d("App", streamToString(connection.getInputStream()));
//                    Log.d("App",connection.getContentType());
//                    Log.d("App",""+connection.getContentLength());
//                    Thread.sleep(100);
//                    Log.d("App",""+connection.getContentLength());
                    Bitmap bmp = BitmapFactory.decodeStream(connection.getInputStream());
                    Log.d("App", "Got Image");
                    if (bmp == null){
                        Log.d("App", "Image is null");
                    }
                    return bmp;
                } catch (MalformedURLException e) {
                    Log.d("App",e.toString());
                } catch (IOException e) {
                    Log.d("App",e.toString());
                } catch (Exception e){
                    Log.d("App",e.toString());
                }
                Log.d("App", "Failed");
                return null;
            }
                public String streamToString(InputStream is) throws IOException {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                }

            @Override
            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
                if (o == null){
                    Log.d("App", "Image null");
                } else {
                    Bitmap image = (Bitmap)o;
                    Log.d("App", "Displaying Image");
                    ImageView imageView = (ImageView)findViewById(R.id.iv);
                    imageView.setImageBitmap(image);
                    Bitmap newImage = image.copy(image.getConfig(), true);
                    AsyncTask nextTask = tasks.removeFirst();
                    Object[] objects = {newImage, imageView, tasks, null};
                    nextTask.execute(objects);
//                    GaussianBlur blur = new GaussianBlur();
//                    Object[] objects = {newImage, imageView};
//                    blur.execute(objects);
//                    SobelFilter sobelFilter = new SobelFilter();
//                    Object[] objects2 = {newImage, imageView};
//                    sobelFilter.execute(objects2);
                }
            }
            private int[] getColours(int color){
                int A = (color >> 24) & 0xff; // or color >>> 24
                int R = (color >> 16) & 0xff;
                int G = (color >>  8) & 0xff;
                int B = (color      ) & 0xff;
                int[] c = {A,R,G,B};
                return c;
            }
            private int getEncodedColour(int[] c){
                return (c[0] & 0xff) << 24 | (c[1] & 0xff) << 16 | (c[2] & 0xff) << 8 | (c[3] & 0xff);
            }
            private int getEncodedColour(int A, int R, int G, int B){
                return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
            }
        };
        task.execute();
    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    public void grabImage(ImageView imageView)
    {
        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            imageView.setImageBitmap(bitmap);
            image = bitmap;
        }
        catch (Exception e)
        {
            Log.d(TAG, "Failed to load", e);
        }
    }
    //called after camera intent finished
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        //MenuShootImage is user defined menu option to shoot image
        if(requestCode==0 && resultCode==RESULT_OK)
        {
            //... some code to inflate/create/find appropriate ImageView to place grabbed image
            this.grabImage(imageView);

            AsyncTask nextTask = tasks.removeFirst();
            Object[] objects = {image, imageView, tasks, null};
            nextTask.execute(objects);
//            startActivity(new Intent(MainActivity.this, CameraActivity.class));
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}