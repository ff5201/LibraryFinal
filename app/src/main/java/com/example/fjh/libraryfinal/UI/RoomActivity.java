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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fjh.libraryfinal.Adapter.MsgAdapter;
import com.example.fjh.libraryfinal.Control.ActivityCollector;
import com.example.fjh.libraryfinal.Model.Msg;
import com.example.fjh.libraryfinal.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by FJH on 2017/5/18.
 */

public class RoomActivity extends AppCompatActivity {

    private ProgressDialog progressDialog = null;

    private asyncWaitForNet asyncWaitForNet;

    Socket socket;

    BufferedReader br;
    BufferedWriter bw;

    /*myHandler handler;*/

    private List<Msg> msgList=new ArrayList<Msg>();
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;

    Button btn_send;
    TextView inputText;

    boolean LinkedState=false;

    //String path="https://www.baidu.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_room);
        setTitle("聊天室");
        ActivityCollector.addActivity(this);

        progressDialog=new ProgressDialog(RoomActivity.this);
        asyncWaitForNet=new asyncWaitForNet();
        asyncWaitForNet.execute();
        /*handler =new myHandler();*/

        btn_send=(Button)findViewById(R.id.btn_send);
        inputText =(TextView)findViewById(R.id.input_text) ;
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=inputText.getText().toString();  //一个n坑了一晚上
                    try {
                        Log.d("545",s);
                        String content=inputText.getText().toString();
                        if(!"".equals(content)) {
                            Msg msg = new Msg(content, Msg.TYPE_SENT);
                            msgList.add(msg);
                            adapter.notifyItemInserted(msgList.size() - 1);//当有新消息是，刷新recyclerView中的显示
                            msgRecyclerView.scrollToPosition(msgList.size() - 1);//将RecyclerView定位到最后一行
                            inputText.setText("");//清空输入框中的内容
                            bw.write(s+"\n");//输出信息
                            bw.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });


        //initMsgs(); //初始化消息数据
        msgRecyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //如果异步任务不为空 并且状态是 运行时  ，就把他取消这个加载任务
        if(asyncWaitForNet !=null && asyncWaitForNet.getStatus() == AsyncTask.Status.RUNNING){
            asyncWaitForNet.cancel(true);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(LinkedState==true){
                bw.write("exit\n");
                bw.flush();
                br.close();
                bw.close();
                socket.close();
            }
            finish();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    class asyncWaitForNet extends AsyncTask<Void, String, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog=ProgressDialog.show(RoomActivity.this,"请求网络","正在请求网络",false);*/
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
            progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
            progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
            progressDialog.setTitle("请求网络");
            progressDialog.setMessage("正在请求网络!");
            progressDialog.show();
        }



       @Override
        protected Void doInBackground(Void... params) {
           while (true){
               try {
                   socket = new Socket("10.211.5.241", 8008);// 网络访问最好放在线程中
                   /*new Thread(new chatThread(socket)).start();// 启动子线程*/
                   br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                   bw= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
                   publishProgress("success");
                   break;
               }catch(IOException  e){
                   Log.d("连接出错","连接出错");
                   publishProgress("error");
               }
               try {
                   Thread.sleep(10000);
               }catch (InterruptedException e){
                   e.printStackTrace();
               }
           }
           try {
               String content=null;
               while ((content = br.readLine()) != null) {
                   Log.d("服务器返回消息",content);
                   //去除bufferWeiter的最后一个空格
                   if(!content.equals("")){
                        publishProgress(content);
                   }else {}
               }
           } catch (IOException e) {
               publishProgress("interrupt");
               e.printStackTrace();
           }catch (Exception e){
               publishProgress("interrupt");
               e.printStackTrace();
           }
           return null;
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
        protected void onProgressUpdate(String... values) {
            if(values[0].equals("success")){
                progressDialog.dismiss(); //隐藏
                LinkedState=true;
                Toast.makeText(getApplicationContext(),"网络已经连接",Toast.LENGTH_SHORT).show();
            }else if(values[0].equals("error")){
                progressDialog.setTitle("结果");
                progressDialog.setMessage("无网络,请检查网络!");
                progressDialog.show();
                LinkedState=false;
                Toast.makeText(getApplicationContext(),"无网络",Toast.LENGTH_SHORT).show();
            }else if(values[0].equals("interrupt")){
                try {
                    br.close();
                    bw.close();
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else{
                Log.d("Progress消息",values[0].toString());
                Msg msgr = new Msg(values[0].toString(), Msg.TYPE_RECEIVED);
                msgList.add(msgr);
                adapter.notifyItemInserted(msgList.size() - 1);//当有新消息是，刷新recyclerView中的显示
                msgRecyclerView.scrollToPosition(msgList.size() - 1);//将RecyclerView定位到最后一行
            }
        }



       /* @Override
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
        }*/
    }

    /*//更新手柄
    class myHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==123) {
                //tb.append(msg.getData().getString("chat"));
                Log.d("H消息",msg.getData().getString("chat"));
                Msg msgr = new Msg(msg.getData().getString("chat"), Msg.TYPE_RECEIVED);
                msgList.add(msgr);
                adapter.notifyItemInserted(msgList.size() - 1);//当有新消息是，刷新recyclerView中的显示
                msgRecyclerView.scrollToPosition(msgList.size() - 1);//将RecyclerView定位到最后一行
            }
        }
    }*/

    //客户端线程
   /* class chatThread implements Runnable {

        Socket socket;

        public chatThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                String content=null;
                while ((content = br.readLine()) != null) {
                    Log.d("服务器返回消息",content);
                    //去除bufferWeiter的最后一个空格
                    if(!content.equals("")){
                        Bundle bundle = new Bundle();
                        bundle.putString("chat", content);
                        Message msg = new Message();
                        msg.setData(bundle);
                        msg.what = 123;
                        handler.sendMessage(msg);
                    }else {}
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }*/


    /*private void initMsgs(){
        Msg msg1=new Msg("Hello gril",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2=new Msg("Hello. Who is that",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3=new Msg("This is Tom.Nice talking to you.", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }*/
}
