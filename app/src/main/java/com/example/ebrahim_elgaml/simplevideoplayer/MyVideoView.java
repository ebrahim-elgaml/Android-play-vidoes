package com.example.ebrahim_elgaml.simplevideoplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewManager;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by ebrahim-elgaml on 3/16/16.
 */
public class MyVideoView extends VideoView{
    private Bitmap mFrameBitmap;
    private Canvas mResultCanvas;
    private int i = 0;
    private final int mMaxUVMarkers = 11;
    private float mRotationDegrees = (360.0f/(float)mMaxUVMarkers);
    private int mUVIndex = 0;
    private Bitmap mLightMarkerBitmap;
    private Bitmap mDarkMarkerBitmap; //!< Dark "off" marker
    private final int mDrawBitmapWidth = 300;
    private final int mDrawBitmapHeight = 300; //!< Default height of view
    private Matrix mDrawingMatrix;
    private Paint mDrawingPaint;
    private Bitmap mResultBitmap; //!< Bitmap to draw markers on
    private Rect mDrawRect;
    public MyVideoView(Context context) {
        super(context);
        setWillNotDraw(false);
        init(context);
    }
    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init(context);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        init(context);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
        init(context);
    }
    private void init(Context context){
        //Images
        mUVIndex = 1;
        mLightMarkerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.iiii);
        mDarkMarkerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.iiii);

        mResultBitmap = Bitmap.createBitmap(mDrawBitmapWidth, mDrawBitmapHeight, Bitmap.Config.ARGB_8888);
        mResultCanvas = new Canvas(mResultBitmap);

        mDrawingPaint = new Paint();
        mDrawingPaint.setAntiAlias(true);
        mDrawingPaint.setDither(true);
        mDrawingPaint.setFilterBitmap(true);

        mDrawingMatrix = new Matrix();

        mDrawRect = new Rect(0, 0, mDrawBitmapWidth, mDrawBitmapHeight);
    }

    public void onDraw(Canvas canvas){
        //Toast.makeText(this.getContext(), "sdasadasddsa", Toast.LENGTH_LONG).show();
//        mFrameBitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_4444);
//        canvas.setBitmap(mFrameBitmap);
        i++;
//        mResultCanvas.drawColor(Color.TRANSPARENT);
//        canvas.drawColor(Color.TRANSPARENT);
//
//        //Draw each marker
//        for(int i = 0; i < mMaxUVMarkers; i++){
//            Bitmap marker = getMarkerBitmap(i);
//
//            int centerX = (mDrawBitmapWidth/2) - (marker.getWidth()/2);
//            int centerY = (mDrawBitmapHeight/2) - (marker.getHeight()/2);
//            mDrawingMatrix.reset();
//            mDrawingMatrix.postTranslate(centerX, centerY);
//            mDrawingMatrix.postTranslate(0, -centerY);
//            mDrawingMatrix.postRotate(mRotationDegrees*(float)i, mDrawBitmapWidth/2, mDrawBitmapHeight/2);
//            mResultCanvas.drawBitmap(marker, mDrawingMatrix, mDrawingPaint);
//        }
//
//        //After drawing wheel to result bitmap, draw the result bitmap to the view's canvas.
//        //This allows the view to be resized to whatever size the view needs to be.
//        canvas.drawBitmap(mResultBitmap, null, mDrawRect, mDrawingPaint);

        Log.i("VideoInfoON", "Width : " + canvas.getWidth());
        Log.i("VideoInfoON", "Height : " + canvas.getHeight());
        Log.i("VideoInfoON", "Position : " + getCurrentPosition());

//        //canvas.setBitmap(mFrameBitmap);
//        Paint p = new Paint();
//        canvas.drawCircle(0, 0, 5, p);
       // canvas.setBitmap(mFrameBitmap);
        mFrameBitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        //mFrameBitmap = new Bitmap();
        Canvas c = new Canvas();
        c.setBitmap(mFrameBitmap);
        c.save();
        super.onDraw(c);

//        canvas.setBitmap(mFrameBitmap);
        Log.i("VideoInfoON", "mframe : " + mFrameBitmap.toString());
        int[] pixels = new int [mFrameBitmap.getHeight() * mFrameBitmap.getWidth()];
        mFrameBitmap.getPixels(pixels, 0, mFrameBitmap.getWidth(), 0, 0, mFrameBitmap.getWidth(), mFrameBitmap.getHeight());
        mFrameBitmap.eraseColor(Color.BLUE);
        c.restore();
        Log.i("VideoInfoON", "Index : " + i);
        Log.i("VideoInfoON", "pixels : " + arrayToString(pixels));

    }
    public void draw(Canvas canvas){

//        //Toast.makeText(this.getContext(), "sdasadasddsa", Toast.LENGTH_LONG).show();
////        mFrameBitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_4444);
////        canvas.setBitmap(mFrameBitmap);
//        i++;
////        mResultCanvas.drawColor(Color.TRANSPARENT);
////        canvas.drawColor(Color.TRANSPARENT);
////
////        //Draw each marker
////        for(int i = 0; i < mMaxUVMarkers; i++){
////            Bitmap marker = getMarkerBitmap(i);
////
////            int centerX = (mDrawBitmapWidth/2) - (marker.getWidth()/2);
////            int centerY = (mDrawBitmapHeight/2) - (marker.getHeight()/2);
////            mDrawingMatrix.reset();
////            mDrawingMatrix.postTranslate(centerX, centerY);
////            mDrawingMatrix.postTranslate(0, -centerY);
////            mDrawingMatrix.postRotate(mRotationDegrees*(float)i, mDrawBitmapWidth/2, mDrawBitmapHeight/2);
////            mResultCanvas.drawBitmap(marker, mDrawingMatrix, mDrawingPaint);
////        }
////
////        //After drawing wheel to result bitmap, draw the result bitmap to the view's canvas.
////        //This allows the view to be resized to whatever size the view needs to be.
////        canvas.drawBitmap(mResultBitmap, null, mDrawRect, mDrawingPaint);
//
//        Log.i("VideoInfo", "Width : " + canvas.getWidth());
//        Log.i("VideoInfo", "Height : " + canvas.getHeight());
//        Log.i("VideoInfo", "Position : " + getCurrentPosition());
//
////        //canvas.setBitmap(mFrameBitmap);
////        Paint p = new Paint();
////        canvas.drawCircle(0, 0, 5, p);
//        // canvas.setBitmap(mFrameBitmap);
//        mFrameBitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
//        //mFrameBitmap = new Bitmap();
//        Canvas c = new Canvas();
//        c.setBitmap(mFrameBitmap);
//        c.save();
      //  canvas.setBitmap(mFrameBitmap);
        super.draw(canvas);

//        canvas.setBitmap(mFrameBitmap);
//        Log.i("VideoInfo", "mframe : " + mFrameBitmap.toString());
//        int[] pixels = new int [mFrameBitmap.getHeight() * mFrameBitmap.getWidth()];
//        mFrameBitmap.getPixels(pixels, 0, mFrameBitmap.getWidth(), 0, 0, mFrameBitmap.getWidth(), mFrameBitmap.getHeight());
//        mFrameBitmap.eraseColor(Color.BLUE);
//       // canvas.setBitmap(mFrameBitmap);
//        mFrameBitmap.setPixels(setColor(pixels), 0, mFrameBitmap.getWidth(), 0, 0, mFrameBitmap.getWidth(), mFrameBitmap.getHeight());
//        mFrameBitmap.getPixels(pixels, 0, mFrameBitmap.getWidth(), 0, 0, mFrameBitmap.getWidth(), mFrameBitmap.getHeight());
       // super.draw(canvas);
        //Paint p = new Paint();
      //  c.drawBitmap(mFrameBitmap, 0f, 0f, p);
        //c.restore();
        Log.i("VideoInfo", "Index : " + i);
//        Log.i("VideoInfo", "pixels : " + arrayToString(pixels));

    }
    public String arrayToString(int[] a){
        String s = "";
        for(int p = 0; p < 200 ; p++){
            s+= ", "+ a[p];
        }
//        for(int j = 0 ; j<a.length ; j++){
//            s += ", "+ a[j];
//        }
        return s;
    }
    public int[] setColor(int[] a){
        int c = 10066329;
        for(int p = 0; p < 200 ; p++){
            a[p] = c;
            c++;
        }
        return a;
    }


    private Bitmap getMarkerBitmap(int index){
        if (index < mUVIndex){
            return mLightMarkerBitmap;
        }else{
            return mDarkMarkerBitmap;
        }
    }




}
