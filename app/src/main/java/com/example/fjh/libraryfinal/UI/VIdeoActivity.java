package com.example.fjh.libraryfinal.UI;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.fjh.libraryfinal.R;
import com.example.fjh.libraryfinal.Tools.DensityUtil;

/**
 * Created by FJH on 2017/5/17.
 */

public class VIdeoActivity extends AppCompatActivity {

    private RelativeLayout rlv;
    private VideoView videoView;

    //本地视频
    final String videoPath = Environment.getExternalStorageDirectory().getPath()+"/xaq-djc.mp4";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video);
        setTitle("视频播放");

        videoView=(VideoView)findViewById(R.id.videoView);
        rlv=(RelativeLayout)findViewById(R.id.video_rl);
        //初始化
        initVideo();

        //开始播放
        videoView.start();
        //输入焦点聚焦
        videoView.requestFocus();
    }

    public void initVideo(){
        try {
            //设置视频控制器
            videoView.setMediaController(new MediaController(this));
            //设置视频路径
            videoView.setVideoPath(videoPath);
            //播放完成监听
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //重复播放
                    Toast.makeText(videoView.getContext(),"视频播放完成！",Toast.LENGTH_SHORT).show();
                    videoView.setVideoPath(videoPath);
                    videoView.start();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoView == null) {
            return;
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//横屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            rlv.getLayoutParams().height = (int) width;
            rlv.getLayoutParams().width = (int) height;
        } else {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this,210.f);
            rlv.getLayoutParams().height = (int) height;
            rlv.getLayoutParams().width = (int) width;
        }
    }

    @Override
    protected void onDestroy() {
        if(videoView!=null){
            videoView.stopPlayback();
        }
        super.onDestroy();
    }
}
