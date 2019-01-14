package com.example.aparna.recording;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.AppCompatButton;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.mRecord)
    ImageView mRecord;
    @BindView(R.id.mPlay)
    ImageView mPlay;
    @BindView(R.id.textView)
    TextView textView;


    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private static String mFileName = null;

    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Record to the external cache directory for visibility
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "audiorecordtest.3gp";
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }
    @OnClick({R.id.mRecord, R.id.mPlay})
    public void onButtonPressed (View v){
            if (v.getId() == R.id.mRecord) {
                if (mRecord.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.record).getConstantState()) {
                    //if(mRecord.getTag()=="1") {
                    mRecord.setImageResource(R.drawable.record_tap);
                    recordMessage();
                }else {
                //} else if (mRecord.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.record_tap).getConstantState()) {
                    mRecord.setImageResource(R.drawable.record);
                    mRecorder.release();
                    mRecorder = null;



                }
            } else if (v.getId() == R.id.mPlay) {
                if (mPlay.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.play).getConstantState()) {
                    mPlay.setImageResource(R.drawable.pause);
                    playMessage();
                }
                else{
                    mPlayer.release();
                    mPlayer=null;
                }

            }
        }
    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
    public void playMessage(){
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }
    public void recordMessage(){
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();

        } catch (IOException e) {
            //textView.setText(e.getLocalizedMessage() + mFileName);
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }





}