package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;
import android.content.pm.ActivityInfo;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private static final String url = "rtsp://192.168.1.101:554/user=fabiano_password=fabiano_channel=1_stream=0&amp;onvif=0.sdp?real_st";

    private LibVLC libVlc;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        libVlc = new LibVLC(this);
        mediaPlayer = new MediaPlayer(libVlc);
        videoLayout = findViewById(R.id.videoLayout);
    }

    protected void onStart() {
        super.onStart();

        // Attach the MediaPlayer to the VideoLayout
        mediaPlayer.attachViews(videoLayout, null, false, false);

        // Create a new Media instance and set the media options
        Media media = new Media(libVlc, Uri.parse(url));
        media.setHWDecoderEnabled(true, false);
        media.addOption(":network-caching=250");

        // Set the media to the MediaPlayer and start playing
        mediaPlayer.setMedia(media);
        media.release();
        mediaPlayer.play();

        // Go to full screen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        videoLayout.setFitsSystemWindows(false);
    }


    @Override
    protected void onStop() {
        super.onStop();

        // Stop the MediaPlayer and detach it from the VideoLayout
        mediaPlayer.stop();
        mediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release the MediaPlayer and the LibVLC instance
        mediaPlayer.release();
        libVlc.release();
    }
}
