package com.example.fjh.libraryfinal.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fjh.libraryfinal.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;


/**
 * Created by FJH on 2017/5/18.
 */

public class RoomActivity extends AppCompatActivity {

    private ProgressDialog progressDialog = null;

    private asyncWaitForNet asyncWaitForNet;

    Socket client;

    myHandler handler;

    //String path="https://www.baidu.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("聊天室");
        /*Button btn_send=(Button)findViewById(R.id.serd_msg);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="肥肥";
                if (client != null) {
                    try {
                        client.getOutputStream().write(s.getBytes("utf-8"));// 获取从客户端得到的数据
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

        progressDialog=new ProgressDialog(RoomActivity.this);
        asyncWaitForNet=new asyncWaitForNet();
        asyncWaitForNet.execute();
        handler =new myHandler();
    }



    class asyncWaitForNet extends AsyncTask<Void, Integer, Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog=ProgressDialog.show(RoomActivity.this,"请求网络","正在请求网络",false);*/
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
            progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
            progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
            progressDialog.setTitle("请求网络");
            progressDialog.setMessage("正在请求网络!");
            progressDialog.show();
        }


       @Override
        protected Boolean doInBackground(Void... params) {
           try {
               client = new Socket("116.10.19.213", 8008);// 网络访问最好放在线程中
               new chatThread(client).start();// 启动子线程
               return true;
           }catch(Exception e){
               return false;
           }
            /*try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                if (code == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
               return false;
            }*/
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                progressDialog.dismiss(); //隐藏
                Toast.makeText(getApplicationContext(),"网络已经连接",Toast.LENGTH_SHORT).show();
            }else{
                progressDialog.setTitle("结果");
                progressDialog.setMessage("无网络,请检查网络!");
                progressDialog.show();
                Toast.makeText(getApplicationContext(),"无网络",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //更新手柄
    class myHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==123) {
                Log.d("H消息",msg.getData().getString("chat"));
            }
        }
    }

    //客户端线程
    class chatThread extends Thread {
        Socket socket;

        public chatThread(Socket socket) {
            super();
            this.socket = socket;
        }

        public void run() {
            super.run();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (true) {
                    String content = br.readLine();
                    if (content != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("chat", content);
                        Message msg = new Message();
                        msg.setData(bundle);
                        msg.what = 123;
                        handler.sendMessage(msg);
                    }

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
