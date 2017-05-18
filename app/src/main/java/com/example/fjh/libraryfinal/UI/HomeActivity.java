package com.example.fjh.libraryfinal.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fjh.libraryfinal.Control.ActivityCollector;
import com.example.fjh.libraryfinal.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);
        setTitle("欢迎使用手机图书系统");
        ActivityCollector.addActivity(this);
        Button btn_search=(Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        Button btn_add=(Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        Button btn_music=(Button)findViewById(R.id.btn_music);
        btn_music.setOnClickListener(this);
        Button btn_video=(Button)findViewById(R.id.btn_video);
        btn_video.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_search:
                intent=new Intent(v.getContext(),SearchActivity.class);
                v.getContext().startActivity(intent);
                break;
            case R.id.btn_add:
                intent=new Intent(v.getContext(),AddActivity.class);
                v.getContext().startActivity(intent);
                break;

            case R.id.btn_music:
               intent=new Intent(v.getContext(),MusicActivity.class);
                v.getContext().startActivity(intent);
                break;
            case R.id.btn_video:
                Log.d("fdas","fdsafas54654");
                intent=new Intent(v.getContext(),VIdeoActivity.class);
                v.getContext().startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "设置系统");
        menu.add(0, 2, 0, "退出系统");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:

                break;
            case 2:
                ActivityCollector.finishAll();
                break;
        }
        return true;
    }

}
