package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.studentappmvvm.R;
import com.google.android.material.navigation.NavigationView;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

import java.io.File;

public class StreamActivity extends AppCompatActivity implements Button.OnClickListener, ConnectCheckerRtsp, SurfaceHolder.Callback,
        View.OnTouchListener {

    private Integer[] orientations = new Integer[] { 0, 90, 180, 270 };

    private RtspCamera1 rtspCamera1;
    private SurfaceView surfaceView;
    private Button bStartStop, bRecord;
    private EditText etUrl;
    private String currentDateAndTime = "";
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/rtmp-rtsp-stream-client-java"); // directory for saving records

    // options menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RadioGroup rgChannel;
    private RadioButton rbTcp, rbUdp;
    private Spinner spResolution;
    private CheckBox cbEchoCanceler, cbNoiseSuppressor, cbHardwareRotation;
    private EditText etVideoBitrate, etFps, etAudioBitrate, etSampleRate;
    private String lastVideoBitrate;
    private TextView tvBitrate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_stream);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setOnTouchListener(this);
        rtspCamera1 = new RtspCamera1(surfaceView, this);
        prepareOptionsMenuViews();
    }

    private void prepareOptionsMenuViews() {
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onConnectionSuccessRtsp() {

    }

    @Override
    public void onConnectionFailedRtsp(String reason) {

    }

    @Override
    public void onNewBitrateRtsp(long bitrate) {

    }

    @Override
    public void onDisconnectRtsp() {

    }

    @Override
    public void onAuthErrorRtsp() {

    }

    @Override
    public void onAuthSuccessRtsp() {

    }
}
