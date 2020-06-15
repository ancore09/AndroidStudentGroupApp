package com.example.studentappmvvm.ui;

import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Rational;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentappmvvm.R;
import com.pedro.vlc.VlcListener;
import com.pedro.vlc.VlcVideoLibrary;

import java.util.Arrays;

public class PlayerActivity extends AppCompatActivity implements VlcListener, View.OnClickListener {

    private VlcVideoLibrary vlcVideoLibrary;
    private Button bStartStop;
    private Button bEnterPIP;
    private EditText etEndpoint;
    SurfaceView surfaceView;

    private String[] options = new String[]{":fullscreen"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_player);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        bStartStop = (Button) findViewById(R.id.b_start_stop);
        bEnterPIP = (Button) findViewById(R.id.b_pip);
        bStartStop.setOnClickListener(this);
        bEnterPIP.setOnClickListener(this);
        etEndpoint = (EditText) findViewById(R.id.et_endpoint);

        vlcVideoLibrary = new VlcVideoLibrary(this, this, surfaceView);
        vlcVideoLibrary.setOptions(Arrays.asList(options));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_start_stop:
                if (!vlcVideoLibrary.isPlaying()) {
                    vlcVideoLibrary.play(etEndpoint.getText().toString());
                    bStartStop.setText("Stop player");
                } else {
                    vlcVideoLibrary.stop();
                    bStartStop.setText("Start player");
                }
                break;
            case R.id.b_pip:
                Display d = getWindowManager().getDefaultDisplay();
                Point p = new Point();
                d.getSize(p);
                int width = p.x;
                int height = p.y;

                Rational ratio = new Rational(width, height);
                PictureInPictureParams.Builder pip_Builder = new PictureInPictureParams.Builder();
                pip_Builder.setAspectRatio(ratio).build();
                enterPictureInPictureMode(pip_Builder.build());
                break;
        }
    }
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        if (isInPictureInPictureMode) {
            bStartStop.setVisibility(View.INVISIBLE);
            bEnterPIP.setVisibility(View.INVISIBLE);
            etEndpoint.setVisibility(View.INVISIBLE);
        } else {
            bStartStop.setVisibility(View.VISIBLE);
            bEnterPIP.setVisibility(View.VISIBLE);
            etEndpoint.setVisibility(View.VISIBLE);
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show();
        vlcVideoLibrary.stop();
        bStartStop.setText("Start player");
    }
}
