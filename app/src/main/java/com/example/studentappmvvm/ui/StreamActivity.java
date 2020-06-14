package com.example.studentappmvvm.ui;

import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.studentappmvvm.R;
import com.google.android.material.navigation.NavigationView;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StreamActivity extends AppCompatActivity implements Button.OnClickListener, ConnectCheckerRtmp, SurfaceHolder.Callback,
        View.OnTouchListener {

    private Integer[] orientations = new Integer[] { 0, 90, 180, 270 };

    private RtmpCamera1 rtmpCamera1;
    private SurfaceView surfaceView;
    private Button bStartStop, bRecord;
    private EditText etUrl;
    private String currentDateAndTime = "";
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mercury-records"); // directory for saving records

    // options menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RadioGroup rgChannel;
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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setOnTouchListener(this);
        rtmpCamera1 = new RtmpCamera1(surfaceView, this);
        prepareOptionsMenuViews();

        tvBitrate = findViewById(R.id.tv_bitrate);
        etUrl = findViewById(R.id.et_rtp_url);
        etUrl.setHint("rtmp://endpoint");
        bStartStop = findViewById(R.id.b_start_stop);
        bStartStop.setOnClickListener(this);
        bRecord = findViewById(R.id.b_record);
        bRecord.setOnClickListener(this);
        Button switchCamera = findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(this);
    }

    private void prepareOptionsMenuViews() {
        drawerLayout = findViewById(R.id.activity_custom);
        navigationView = findViewById(R.id.nv_rtp);
        navigationView.inflateMenu(R.menu.options_rtmp);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.rtsp_streamer, R.string.rtsp_streamer) {

            public void onDrawerOpened(View drawerView) {
                actionBarDrawerToggle.syncState();
                lastVideoBitrate = etVideoBitrate.getText().toString();
            }

            public void onDrawerClosed(View view) {
                actionBarDrawerToggle.syncState();
                if (lastVideoBitrate != null && !lastVideoBitrate.equals(etVideoBitrate.getText().toString()) && rtmpCamera1.isStreaming()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        int bitrate = Integer.parseInt(etVideoBitrate.getText().toString()) * 1024;
                        rtmpCamera1.setVideoBitrateOnFly(bitrate);
                        Toast.makeText(StreamActivity.this, "New bitrate: " + bitrate, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StreamActivity.this, "Bitrate on fly ignored, Required min API 19", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // checkboxes
        cbEchoCanceler = (CheckBox) navigationView.getMenu().findItem(R.id.cb_echo_canceler).getActionView();
        cbNoiseSuppressor = (CheckBox) navigationView.getMenu().findItem(R.id.cb_noise_suppressor).getActionView();
        cbHardwareRotation = (CheckBox) navigationView.getMenu().findItem(R.id.cb_hardware_rotation).getActionView();

        // radio buttons
        rgChannel = (RadioGroup) navigationView.getMenu().findItem(R.id.channel).getActionView();

        // spinners
        spResolution = (Spinner) navigationView.getMenu().findItem(R.id.sp_resolution).getActionView();

        ArrayAdapter<Integer> orientationAdapter = new ArrayAdapter<>(this, R.layout.spinner_popup_item);
        orientationAdapter.addAll(orientations);

        ArrayAdapter<String> resolutionAdapter = new ArrayAdapter<>(this, R.layout.spinner_popup_item);
        List<String> list = new ArrayList<>();
        for (Camera.Size size : rtmpCamera1.getResolutionsBack()) {
            list.add(size.width + "X" + size.height);
        }
        resolutionAdapter.addAll(list);
        spResolution.setAdapter(resolutionAdapter);
        // edit texts
        etVideoBitrate = (EditText) navigationView.getMenu().findItem(R.id.et_video_bitrate).getActionView();
        etFps = (EditText) navigationView.getMenu().findItem(R.id.et_fps).getActionView();
        etAudioBitrate = (EditText) navigationView.getMenu().findItem(R.id.et_audio_bitrate).getActionView();
        etSampleRate = (EditText) navigationView.getMenu().findItem(R.id.et_samplerate).getActionView();
        etVideoBitrate.setText("2500");
        etFps.setText("30");
        etAudioBitrate.setText("128");
        etSampleRate.setText("44100");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            case R.id.microphone:
                if (!rtmpCamera1.isAudioMuted()) {
                    item.setIcon(getResources().getDrawable(R.drawable.icon_microphone_off));
                    rtmpCamera1.disableAudio();
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.icon_microphone));
                    rtmpCamera1.enableAudio();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_start_stop:
                if (!rtmpCamera1.isStreaming()) {
                    bStartStop.setText("Stop stream");
                    if (rtmpCamera1.isRecording() || prepareEncoders()) {
                        rtmpCamera1.startStream(etUrl.getText().toString());
                    } else {
                        // If you see this all time when you start stream,
                        // it is because your encoder device don't support the configuration
                        // in video encoder maybe color format.
                        // If you have more encoder go to VideoEncoder or AudioEncoder class,
                        // change encoder and try
                        Toast.makeText(this, "Error preparing stream, This device cant do it", Toast.LENGTH_SHORT).show();
                        bStartStop.setText("Start stream");
                    }
                } else {
                    bStartStop.setText("Start stream");
                    rtmpCamera1.stopStream();
                }
                break;
            case R.id.b_record:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (!rtmpCamera1.isRecording()) {
                        try {
                            if (!folder.exists()) {
                                folder.mkdir();
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                            currentDateAndTime = sdf.format(new Date());
                            if (!rtmpCamera1.isStreaming()) {
                                if (prepareEncoders()) {
                                    rtmpCamera1.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                                    bRecord.setText("Stop record");
                                    Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Error preparing stream, This device cant do it", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                rtmpCamera1.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                                bRecord.setText("Stop record");
                                Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            rtmpCamera1.stopRecord();
                            bRecord.setText("Start record");
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        rtmpCamera1.stopRecord();
                        bRecord.setText("Start record");
                        Toast.makeText(this, "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "You need min JELLY_BEAN_MR2(API 18) for do it...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_camera:
                try {
                    rtmpCamera1.switchCamera();
                } catch (CameraOpenException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private boolean prepareEncoders() {
        Camera.Size resolution = rtmpCamera1.getResolutionsBack().get(spResolution.getSelectedItemPosition());
        int width = resolution.width;
        int height = resolution.height;
        return rtmpCamera1.prepareVideo(width, height, Integer.parseInt(etFps.getText().toString()),
                Integer.parseInt(etVideoBitrate.getText().toString()) * 1024,
                cbHardwareRotation.isChecked(), CameraHelper.getCameraOrientation(this))
                && rtmpCamera1.prepareAudio(Integer.parseInt(etAudioBitrate.getText().toString()) * 1024,
                Integer.parseInt(etSampleRate.getText().toString()),
                rgChannel.getCheckedRadioButtonId() == R.id.rb_stereo, cbEchoCanceler.isChecked(),
                cbNoiseSuppressor.isChecked());
    }

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(() -> {
            Toast.makeText(StreamActivity.this, "Connection success", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onConnectionFailedRtmp(String reason) {
        runOnUiThread(() -> {
            Toast.makeText(StreamActivity.this, "Connection failed. " + reason, Toast.LENGTH_SHORT)
                    .show();
            rtmpCamera1.stopStream();
            bStartStop.setText("Start stream");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                    && rtmpCamera1.isRecording()) {
                rtmpCamera1.stopRecord();
                bRecord.setText("Start record");
                Toast.makeText(StreamActivity.this,
                        "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();
                currentDateAndTime = "";
            }
        });
    }

    @Override
    public void onNewBitrateRtmp(long bitrate) {
        runOnUiThread(() -> {
            tvBitrate.setText(bitrate + " bps");
        });
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(() -> {
            Toast.makeText(StreamActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                    && rtmpCamera1.isRecording()) {
                rtmpCamera1.stopRecord();
                bRecord.setText("Start record");
                Toast.makeText(StreamActivity.this,
                        "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();
                currentDateAndTime = "";
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        rtmpCamera1.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && rtmpCamera1.isRecording()) {
            rtmpCamera1.stopRecord();
            bRecord.setText("Start record");
            Toast.makeText(this,
                    "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
            currentDateAndTime = "";
        }
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
            bStartStop.setText("Start stream");
        }
        rtmpCamera1.stopPreview();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (event.getPointerCount() > 1) {
            if (action == MotionEvent.ACTION_MOVE) {
                rtmpCamera1.setZoom(event);
            }
        } else {
            if (action == MotionEvent.ACTION_UP) {
                // todo place to add auto focus functional.
            }
        }
        return true;
    }

    @Override
    public void onAuthErrorRtmp() {

    }

    @Override
    public void onAuthSuccessRtmp() {

    }
}
