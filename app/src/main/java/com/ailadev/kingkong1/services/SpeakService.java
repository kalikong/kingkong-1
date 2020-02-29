package com.ailadev.kingkong1.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

public class SpeakService extends IntentService {
    private MediaRecorder mRecorder;
    private static final String TAG = "SpeakService";

    public SpeakService() {
        super("oke");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String data = intent.getDataString();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile("/dev/null");
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.d(TAG, "onHandleIntent: "+e.getMessage());
        }
        Log.d(TAG, "onHandleIntent: "+mRecorder.getMaxAmplitude());
    }
}
