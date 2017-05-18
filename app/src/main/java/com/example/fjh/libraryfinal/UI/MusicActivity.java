package com.example.fjh.libraryfinal.UI;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.fjh.libraryfinal.R;

/**
 * Created by FJH on 2017/5/16.
 */

public class MusicActivity extends AppCompatActivity {

    private final String TAG="音乐播放器";

    private Button btn_start;
    private Button btn_pause;
    private Button btn_stop;

    private TextView txt_playing_time;
    private TextView txt_total_time;

    private int totalTime = 0;
    //进度条
    private SeekBar playingProcess;
    //创建MediaPlayer
    private MediaPlayer mPlayer =new MediaPlayer();
    //创建Handler
    private Handler myhandler=new Handler();
    private refreshTimePosThread r=new refreshTimePosThread();
    private Thread thread;
    //音乐是否播放完成标准
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music);
        setTitle("音乐播放");

        txt_playing_time=(TextView)findViewById(R.id.playingTime);
        txt_total_time=(TextView)findViewById(R.id.totalTime) ;

        playingProcess=(SeekBar)findViewById(R.id.seekbar);
        playingProcess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayer.seekTo(playingProcess.getProgress()*1000);
                updateTimePos();
            }
        });

        btn_start=(Button)findViewById(R.id.btn_music_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayMusic();
            }
        });
        btn_pause=(Button)findViewById(R.id.btn_music_pause);
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPauseMusic();
            }
        });
        btn_stop=(Button)findViewById(R.id.btn_music_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStopMusic();
            }
        });
        initMediaPlayer();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.stop();
            mPlayer.release();
        }
    }

    //初始化音乐播放器
    public void initMediaPlayer(){
        try{
            String file_Path="/storage/emulated/0/xaq_xtj.mp3";
            //String s= Environment.getExternalStorageDirectory().getAbsolutePath();
           // Log.d(TAG,s);
            mPlayer.setDataSource(file_Path);
            mPlayer.prepare();
            setTotalTime();
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }
    //设置总时间
    public void setTotalTime(){
        totalTime=mPlayer.getDuration()/1000;
        Log.d(TAG,String.valueOf(totalTime));
        String pos = String.valueOf(totalTime/60/10)+String.valueOf(totalTime/60%10)
                +':'+String.valueOf(totalTime%60/10)+String.valueOf(totalTime%60%10);
        txt_total_time.setText(pos);
        playingProcess.setProgress(0);
        playingProcess.setMax(totalTime);
    }
    //进度度条进度设置
    public void updateTimePos(){
        int timePos=playingProcess.getProgress()+1;
        if(timePos>=totalTime-1){
            timePos=0;
            flag=false;
        }
        playingProcess.setProgress(timePos);
        String pos = String.valueOf(timePos/60/10)+String.valueOf(timePos/60%10)
                    +':'+String.valueOf(timePos%60/10)+String.valueOf(timePos%60%10);
        txt_playing_time.setText(pos);
    }
    //seekProgress监控

    //进度条进度子线程Runnable
    class refreshTimePosThread implements Runnable {
        public void run() {
            Log.d(TAG, "run");
            while (flag && playingProcess.getProgress() < totalTime - 1) {
                try {
                    Log.d(TAG, "sleep");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d(TAG, e.toString());
                    e.printStackTrace();
                }
                Log.d(TAG, "post");
                myhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateTimePos();
                    }
                });
            }
        }
    }
    //开始播放
    public void setPlayMusic(){
        if(!mPlayer.isPlaying()){
            mPlayer.start();
            flag=true;
            thread=new Thread(r);
            thread.start();
        }
    }
    //暂停播放
    public void setPauseMusic(){
        if(mPlayer.isPlaying()){
            mPlayer.pause();
            flag=false;
        }
    }
    //停止播放
    public void setStopMusic(){
        if(btn_stop.getText().equals("停止")){
            mPlayer.stop();
            btn_stop.setText("重播");
        }else if(btn_stop.getText().equals("重播")){
            mPlayer.reset();
            initMediaPlayer();
            mPlayer.start();
            btn_stop.setText("停止");
        }
    }

}


