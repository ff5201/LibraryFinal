package com.example.fjh.libraryfinal.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;

import com.example.fjh.libraryfinal.Control.ActivityCollector;
import com.example.fjh.libraryfinal.Control.DBController;
import com.example.fjh.libraryfinal.Model.Book;
import com.example.fjh.libraryfinal.R;
import com.example.fjh.libraryfinal.SQLiteDB.DBconnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by FJH on 2017/5/14.
 */

public class AddActivity extends AppCompatActivity {

    EditText txtID;
    EditText txtName;
    EditText txtAuthor;
    EditText txtAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add);
        setTitle("添加图书");
        DBconnection.setContext(this.getApplicationContext());
        ActivityCollector.addActivity(this);
        txtID=(EditText)findViewById(R.id.txt_add_id);
        txtName=(EditText)findViewById(R.id.txt_add_name);
        txtAuthor=(EditText)findViewById(R.id.txt_add_author);
        txtAddress=(EditText)findViewById(R.id.txt_add_address);


        //修改才用
        try {
            Book book=(Book) getIntent().getSerializableExtra("Book");
            if(book.getID()!=""){
                TextView tv=(TextView)findViewById(R.id.hideID);
                tv.setText(book.getID().toString());
                txtID.setText(book.getID().toString());
                txtName.setText(book.getName().toString());
                txtAuthor.setText(book.getAuthor().toString());
                txtAddress.setText(book.getAddress().toString());
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }

        //返回
        Button btn_goback=(Button)findViewById(R.id.btn_add_goback);
        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),HomeActivity.class));
            }
        });

        //清空
        Button btn_clear=(Button)findViewById(R.id.btn_add_clearTxt);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtID.setText("");
                txtName.setText("");
                txtAuthor.setText("");
                txtAddress.setText("");
            }
        });

        //存内存
        Button btn_ROMSava=(Button)findViewById(R.id.btn_add_ROMSave);
        btn_ROMSava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data="图书编号："+txtID.getText().toString()+"\n";
                data+="图书名："+txtName.getText().toString()+"\n";
                data+="图书作者："+txtAuthor.getText().toString()+"\n";
                data+="藏书地点："+txtAddress.getText().toString()+"\n";
                boolean i=saveROM(data);
                if(i==true){
                    Toast.makeText(v.getContext(),"存ROM成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(v.getContext(),"存ROM失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //存外存
        Button btn_SDsava=(Button)findViewById(R.id.btn_add_SDSava);
        btn_SDsava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data="图书编号："+txtID.getText().toString()+"\n";
                data+="图书名："+txtName.getText().toString()+"\n";
                data+="图书作者："+txtAuthor.getText().toString()+"\n";
                data+="藏书地点："+txtAddress.getText().toString()+"\n";
                savaSD(data);
            }
        });

        //存数据库
        Button btn_insert=(Button)findViewById(R.id.btn_add_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = txtID.getText().toString();
                String Name = txtName.getText().toString();
                String Author= txtAuthor.getText().toString();
                String Address=txtAddress.getText().toString();
                DBController controller = new DBController();
                if (ID.equals("") || Name.equals("") || Author.equals("")|| Address.equals("")) {
                    Log.d("error","图书信息不能为空");
                    new Builder(AddActivity.this).setMessage("图书信息不能为空！").show();
                } else {
                    if (controller.addBook(ID, Name, Author,Address)) {
                        txtID.setText("");
                        txtName.setText("");
                        txtAuthor.setText("");
                        txtAddress.setText("");
                        buildBuilder("添加成功，是否继续添加图书","返回首页","继续添加");
                    } else {
                        Log.d("error","图书信息不全");
                        new Builder(AddActivity.this).setMessage("图书信息不全！").show();
                    }
                }
            }
        });

        //修改
        Button btn_update=(Button)findViewById(R.id.btn_add_modify);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv=(TextView)findViewById(R.id.hideID);
                if(!tv.getText().toString().equals("")){
                    String hideID=tv.getText().toString();
                    String ID = txtID.getText().toString();
                    String Name = txtName.getText().toString();
                    String Author= txtAuthor.getText().toString();
                    String Address=txtAddress.getText().toString();
                    DBController controller = new DBController();
                    Log.d("修改时的图书信息",hideID+ID+Name+Author+Address);
                    if (ID.equals("") || Name.equals("") || Author.equals("")|| Address.equals("")) {
                        Log.d("error","图书信息不能为空");
                        new Builder(AddActivity.this).setMessage("图书信息不能为空！").show();
                    } else {
                        if (controller.setBook(ID, Name, Author, Address, hideID)) {
                            tv.setText(txtID.getText().toString());
                            buildBuilder("修改成功，是否继续修改该图书", "返回首页", "继续修改");
                        } else {
                            Log.d("error", "修改失败");
                            new Builder(AddActivity.this).setMessage("修改失败！").show();
                        }
                    }
                }
                else{
                    new Builder(AddActivity.this).setMessage("不符合修改条件！").show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public boolean saveROM(String data){
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            String fileName="data";
            out=openFileOutput(fileName, Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
            return true;
        }catch (IOException ee){
            ee.printStackTrace();
            return false;
        }finally {
            try{
                if(writer!=null){
                   writer.close();
                }
            }catch (IOException ee){
                ee.printStackTrace();
            }
        }
    }

    public void savaSD(String data){
        //判断SD卡是否被挂载
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //获取SD卡根目录路径
            String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d("SD卡根目录",sdpath);
            String DEFAULT_FILENAME="abc.txt";
            try{
                File file = new File(Environment.getExternalStorageDirectory(),DEFAULT_FILENAME);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));  //true为追加，false为重新
                bw.write(data);
                bw.flush();
                bw.close();
                Log.d("存入SD状态","存入成功");
            }catch (IOException ee){
                ee.printStackTrace();
            }
            Log.d("挂载状态","挂载成功");
        }else{
            Log.d("挂载状态","挂载失败");
        }
    }


    //对话框
    public void buildBuilder(String message,String sbt,String cel){
        Builder builder = new Builder(AddActivity.this);
        builder.setTitle(message);
        builder.setNegativeButton(sbt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whitcButton) {
                startActivity(new Intent(AddActivity.this,HomeActivity.class));
            }
        });
        builder.setPositiveButton(cel, null);
        builder.show();
    }



}
