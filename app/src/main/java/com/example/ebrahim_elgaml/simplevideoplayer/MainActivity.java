package com.example.ebrahim_elgaml.simplevideoplayer;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

// Original video url : https://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121201/yeoqfrqmbkljvtxzte9n.mp4
// Added stream video : http://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121373/hbad26lqqiyepau4mfu8.mp4

public class MainActivity extends AppCompatActivity {
    MediaPlayer mp = new MediaPlayer();
    private MediaController mediaControlsOriginal;
    private VideoView videoViewOriginal;
    private ProgressDialog progressDialogOriginal;
    private MediaController mediaControlsNew;
    private VideoView videoViewNew;
    private ProgressDialog progressDialogNew;
    private final String originalPath = "https://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121201/yeoqfrqmbkljvtxzte9n.mp4";
    private final String newPath = "http://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121373/hbad26lqqiyepau4mfu8.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        videoViewOriginal = (VideoView)findViewById(R.id.videoViewOriginal);
        videoViewNew = (VideoView)findViewById(R.id.videoViewNew);
        setContentView(R.layout.activity_main);
        playVideoOriginal(originalPath);
        playVideoNew(newPath);
//        try{
//            playVideo("https://res.cloudinary.com/ebrahim-elgaml/video/upload/v1458121201/yeoqfrqmbkljvtxzte9n.mp4");
//        }catch(Exception e){
//            e.printStackTrace();
//            Context context = getApplicationContext();
//            CharSequence text = "Not found file ";
//            int duration = Toast.LENGTH_LONG;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//        }
    }
    public void playVideoOriginal(String videopath) {
        try {
            progressDialogOriginal = ProgressDialog.show(this, "",
                    "Buffering video...", false);
            progressDialogOriginal.setCancelable(true);
            getWindow().setFormat(PixelFormat.TRANSLUCENT);

            mediaControlsOriginal = new MediaController(this);
            videoViewOriginal = (VideoView)findViewById(R.id.videoViewOriginal);
            mediaControlsOriginal.setAnchorView(videoViewOriginal);
            Uri video = Uri.parse(videopath);
            videoViewOriginal.setMediaController(mediaControlsOriginal);
            videoViewOriginal.setVideoURI(video);

            videoViewOriginal.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    progressDialogOriginal.dismiss();
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
            progressDialogNew = ProgressDialog.show(this, "",
                    "Buffering video...", false);
            progressDialogNew.setCancelable(true);
            getWindow().setFormat(PixelFormat.TRANSLUCENT);

            mediaControlsNew = new MediaController(this);
            videoViewNew = (VideoView)findViewById(R.id.videoViewNew);
            mediaControlsNew.setAnchorView(videoViewNew);
            Uri video = Uri.parse(videopath);
            videoViewNew.setMediaController(mediaControlsNew);
            videoViewNew.setVideoURI(video);

            videoViewNew.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    progressDialogNew.dismiss();
                    videoViewNew.start();
                }
            });

        } catch (Exception e) {
            progressDialogNew.dismiss();
            System.out.println("Video Play Error :" + e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }
}
