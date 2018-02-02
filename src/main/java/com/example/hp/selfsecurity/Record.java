package com.example.hp.selfsecurity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import java.io.File;
import java.io.IOException;


public class Record extends ActionBarActivity {

    private MediaPlayer mediaPlayer;
    private MediaRecorder recorder;
    private String OUT_FILE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        OUT_FILE= Environment.getExternalStorageDirectory()+"/audiorecorder.3gpp";
    }

    public void buttonTapped(View view)
    {
        switch (view.getId())
        {
            case R.id.bStartRecord:
                    try{
                    beginRecording();
                }
                catch(Exception e){
                    e.printStackTrace(); }
                    break;

            case R.id.bStopRecord:
                    try{
                    stopRecording();
                }
                catch(Exception e){
                    e.printStackTrace(); }
                    break;
                case R.id.bStartPlaying:
                    try{
                    beginPlaying();
                    }
                catch(Exception e){
                    e.printStackTrace(); }
                    break;

            case R.id.bStopPlaying:
                    try{
                    stopPlaying();
                }
                catch(Exception e){
                    e.printStackTrace(); }
                break;
        }

    }


    public void beginRecording() throws IOException {
        ditchMediaRecorder();
        File out_file=new File(OUT_FILE);
        if(out_file.exists())
            out_file.delete();

        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(OUT_FILE);
        recorder.prepare();
        recorder.start();    }

    private void ditchMediaRecorder() {
        if(recorder!=null)
            recorder.release();
    }

    public void stopRecording(){
        if(recorder!=null)
            recorder.stop();
    }

    public void beginPlaying() throws IOException {
        ditchMediaPlayer();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setDataSource(OUT_FILE);
        mediaPlayer.prepare();
        mediaPlayer.start();

    }

    private void ditchMediaPlayer() {
        if(mediaPlayer!=null)
            try {
                mediaPlayer.release();
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }

    public void stopPlaying(){}
}
