package com.example.ebrahim_elgaml.simplevideoplayer;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.MediaCodec;
import android.media.MediaCodec.*;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


// Original video url : https://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121201/yeoqfrqmbkljvtxzte9n.mp4
// Added stream video : http://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121373/hbad26lqqiyepau4mfu8.mp4

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private MediaController mediaControlsOriginal;
    private static final File OUTPUT_DIR = Environment.getExternalStorageDirectory();
    private MyVideoView videoViewOriginal;
    private ProgressDialog progressDialogOriginal;
    private MediaController mediaControlsNew;
    private VideoView videoViewNew;
    private ProgressDialog progressDialogNew;
    //private final String originalPath = "https://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121201/yeoqfrqmbkljvtxzte9n.mp4";
    private final String originalPath = "/sdcard/testing/test.mp4";
    //private final String newPath = "http://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121373/hbad26lqqiyepau4mfu8.mp4";
//    private String newPath = "http://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458828046/a2mwuu7vbvfdsbfhg7qg.mp4";
    private String newPath = "/sdcard/testing/test.mp4";
    private SurfaceView surfaceView ;
    private PlayerThread mPlayer = null;
    ArrayList<ByteBuffer> byteBuffers = new ArrayList<ByteBuffer>();
    private MediaExtractor mx;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        videoViewOriginal = (MyVideoView)findViewById(R.id.videoViewOriginal);
        videoViewNew = (VideoView)findViewById(R.id.videoViewNew);
        setContentView(R.layout.activity_main);
        playVideoOriginal(originalPath);
        playVideoNew(newPath);
        Log.i("VideoInfo", "MyClass.getView() — get item number ");
       // getVideoData();
        Log.d("BUFFERSIZE", "" + byteBuffers.size());
        videoViewNew.getHolder().addCallback(this);

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mPlayer == null) {
            mPlayer = new PlayerThread(holder.getSurface());
            mPlayer.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mPlayer != null) {
            mPlayer.interrupt();
        }
    }
    public void playVideoOriginal(String videopath) {
        try {
            progressDialogOriginal = ProgressDialog.show(this, "",
                    "Buffering video...", false);
            progressDialogOriginal.setCancelable(true);
            getWindow().setFormat(PixelFormat.TRANSLUCENT);

            mediaControlsOriginal = new MediaController(this);
            videoViewOriginal = (MyVideoView)findViewById(R.id.videoViewOriginal);

            mediaControlsOriginal.setAnchorView(videoViewOriginal);
            Uri video = Uri.parse(videopath);
            videoViewOriginal.setMediaController(mediaControlsOriginal);
            videoViewOriginal.setVideoURI(video);

            videoViewOriginal.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    progressDialogOriginal.dismiss();
                   // videoViewOriginal.setAlpha(1.0f);

                    videoViewOriginal.start();
                }
            });

        } catch (Exception e) {
            progressDialogOriginal.dismiss();
            System.out.println("Video Play Error :" + e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }
    public void playVideoNew(String videopath) {
        try {
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            mediaControlsNew = new MediaController(this);
            videoViewNew = (VideoView)findViewById(R.id.videoViewNew);
            mediaControlsNew.setAnchorView(videoViewNew);
            videoViewNew.setMediaController(mediaControlsNew);
            videoViewNew.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                }
            });
        } catch (Exception e) {
            progressDialogNew.dismiss();
            System.out.println("Video Play Error :" + e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void getVideoData(){
        mx = new MediaExtractor();
        try {
            mx.setDataSource(newPath);
            mx.selectTrack(0);
            ByteBuffer bf = ByteBuffer.allocate(1024 * 1024);
            while(mx.readSampleData(bf, 0) >= 0){
                Byte myByte = 0;
                for(; bf.hasRemaining();){
                    if(myByte >= 128) {
                        myByte = 0;
                    }else{
                        myByte++;
                    }
                    bf.put(myByte);
                }
                byteBuffers.add(bf);
                mx.advance();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class PlayerThread extends Thread {
        private MediaExtractor extractor;
        private MediaCodec decoder;
        private Surface surface;
        int c = 0 ;
        public PlayerThread(Surface surface) {
            this.surface = surface;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            extractor = new MediaExtractor();
            try {
                extractor.setDataSource(newPath);
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                MediaFormat format = extractor.getTrackFormat(0);
                String mime = format.getString(MediaFormat.KEY_MIME);

                extractor.selectTrack(0);
                //decoder = MediaCodec.createDecoderByType(mime);
                decoder = MediaCodec.createByCodecName("OMX.google.h264.decoder");
                decoder.configure(format, surface, null, 0);
               // decoder.createInputSurface();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (decoder == null) {
                Log.e("DecodeActivity", "Can't find video info!");
                return;
            }

            decoder.start();

            ByteBuffer[] inputBuffers = decoder.getInputBuffers();
            ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
            BufferInfo info = new BufferInfo();
            boolean isEOS = false;
            long startMs = System.currentTimeMillis();

            while (!Thread.interrupted()) {
                if (!isEOS) {
                    int inIndex = decoder.dequeueInputBuffer(10000);
                    if (inIndex >= 0) {
                        ByteBuffer m = ByteBuffer.allocate((int) (320 * 180 * 1.5));
                        int sampleSize = extractor.readSampleData(m, 0);
                        byte myByte = -122;
                        int k = 0;
                        if(sampleSize> 0 ) {
                            byte[] g = new byte[sampleSize];
                            while (k < sampleSize) {
                                g[k] = m.get(k);
                                k++;
                            }
//                        //m.rewind();
//                            if (c % 6 == 0) {
//                                int j = 0;
//                                while (j < sampleSize) {
//                                    m.put(j, myByte);
//                                    j++;
//                                }
//                            }
                            c++;
                            if(c%2 == 0) {
                                int color = 0 ;
                                for (int a = 0; a < g.length/4; a++) {
                                    //g[a] = (byte) (g[a]/4);
                                    g[a] = (byte) ((color++) & 0xFF);
                                }
                            }
//                            for (int a = 0; a < g.length/8; a++) {
//                                g[a] = (byte) (g[a]/4);
//                            }
//                        while(m.hasRemaining()) {
//                                if (myByte >= 127) {
//                                    myByte = -122;
//                                } else {
//                                    myByte++;
//                                }
//                                m.put(myByte);
//                        }
                            Log.i("NEW", "DOne with sample " + c);
                            ByteBuffer buffer = inputBuffers[inIndex];
                            //inputBuffers[inIndex] = m;
                            //int sampleSize = extractor.readSampleData(buffer, 0);
                            buffer.clear();
                            buffer.put(g);
                        }else{
                            ByteBuffer buffer = inputBuffers[inIndex];
                            extractor.readSampleData(buffer, 0);
                        }
//                        //m.rewind();
//                        ByteBuffer buffer = inputBuffers[inIndex];
//                        //inputBuffers[inIndex] = m;
//                        //int sampleSize = extractor.readSampleData(buffer, 0);
//                        buffer.clear();
//                        buffer.put(g);
//                        Byte myByte = -126;
                        if (sampleSize < 0) {
                            // We shouldn't stop the playback at this point, just pass the EOS
                            // flag to decoder, we will get it again from the
                            // dequeueOutputBuffer
//                            buffer.rewind();
//                            while(buffer.hasRemaining()) {
//                                if (myByte >= 127) {
//                                    myByte = 0;
//                                } else {
//                                    myByte++;
//                                }
//                                buffer.put(myByte);
//                            }
                            Log.i("DecodeActivity", "InputBuffer BUFFER_FLAG_END_OF_STREAM");
                            decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            isEOS = true;
                        } else {
//                            buffer.rewind();
//                            Log.i("DecodeActivity", "Before loop");
//                            while(buffer.hasRemaining()) {
////                                if (myByte >= 127) {
////                                    myByte = 0;
////                                } else {
////                                    myByte++;
////                                }
//                               // buffer.put(myByte);
//                            }
                            Log.i("DecodeActivity", "After loop");
                           // buffer.rewind();
                            decoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                            //c++;
                            extractor.advance();
                        }
                    }
                }

                int outIndex = decoder.dequeueOutputBuffer(info, 10000);
                switch (outIndex) {
                    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                        Log.d("DecodeActivity", "INFO_OUTPUT_BUFFERS_CHANGED");
                        outputBuffers = decoder.getOutputBuffers();
                        break;
                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                        Log.d("DecodeActivity", "New format " + decoder.getOutputFormat());
                        break;
                    case MediaCodec.INFO_TRY_AGAIN_LATER:
                        Log.d("DecodeActivity", "dequeueOutputBuffer timed out!");
                        break;
                    default:
                        ByteBuffer buffer = outputBuffers[outIndex];
                        Log.v("DecodeActivity", "We can't use this buffer but render it due to the API limit, " + buffer);

                        // We use a very simple clock to keep the video FPS, or the video
                        // playback will be too fast
                        while (info.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
//                        byte myByte = 0;
//                        while(buffer.hasRemaining()) {
//                                if (myByte >= 127) {
//                                    myByte = 0;
//                                } else {
//                                    myByte++;
//                                }
//                                buffer.put(myByte);
//                            }
                        decoder.releaseOutputBuffer(outIndex, true);
                        break;
                }

                // All decoded frames have been rendered, we can stop playing now
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.d("DecodeActivity", "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                    break;
                }
            }

            decoder.stop();
            decoder.release();
            extractor.release();
        }


        }
    }
