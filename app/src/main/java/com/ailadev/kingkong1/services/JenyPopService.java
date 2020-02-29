package com.ailadev.kingkong1.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class JenyPopService extends Service implements RecognitionListener {

    private static final String TAG = "JenyPopService";
    private SpeechRecognizer speechRecognizer;
    private Intent voice;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void beep(boolean state){
        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if (state == true){
//            unmute
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            amanager.setStreamMute(AudioManager.STREAM_RING, false);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }else{
//            mute
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            amanager.setStreamMute(AudioManager.STREAM_RING, true);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Jalan",Toast.LENGTH_LONG).show();
        setSpeechRecognizer();
        return START_REDELIVER_INTENT;
    }

    public void setSpeechRecognizer(){
        if (speechRecognizer!=null){
            speechRecognizer.destroy();
        }
        beep(false);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speechRecognizer.setRecognitionListener(this);
        setVoice();
        speechRecognizer.startListening(voice);
    }

    public void setVoice(){
        voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                .getPackage().getName());
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d(TAG, "onReadyForSpeech: ");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech: ");
    }

    @Override
    public void onRmsChanged(float v) {
        Log.d(TAG, "onRmsChanged: "+v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.d(TAG, "onBufferReceived: ");
    }

    @Override
    public void onEndOfSpeech() {
        beep(true);
        Log.d(TAG, "onEndOfSpeech: ");
    }

    @Override
    public void onError(int error) {
        String mError = "";
        String mStatus = "Error detected";
        switch (error) {
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                mError = " network timeout";
                setSpeechRecognizer();
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                mError = " network" ;
                return;
            case SpeechRecognizer.ERROR_AUDIO:
                mError = " audio";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                mError = " server";
                setSpeechRecognizer();
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                mError = " client";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                mError = " speech time out" ;
                setSpeechRecognizer();
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                mError = " no match" ;
                setSpeechRecognizer();
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                mError = " recogniser busy" ;
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                mError = " insufficient permissions" ;
                break;
        }
        Log.i(TAG,  "Error: " +  error + " - " + mError);
    }

    @Override
    public void onResults(Bundle results) {
        beep(true);
        ArrayList text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(text.get(0).equals("okay")){
//            Intent intent = new Intent(this, Main2Activity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            setSpeechRecognizer();
        };
        setSpeechRecognizer();
        Log.d(TAG, "onResults " + text.get(0));
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Log.d(TAG, "onPartialResults: "+bundle);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
