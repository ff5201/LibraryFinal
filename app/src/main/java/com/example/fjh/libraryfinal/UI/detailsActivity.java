package com.example.fjh.libraryfinal.UI;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.fjh.libraryfinal.Model.Book;
import com.example.fjh.libraryfinal.R;

/**
 * Created by FJH on 2017/5/15.
 */

public class detailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_details);
        setTitle("图书详情");
        Book book=(Book) getIntent().getSerializableExtra("Book");
        if(!book.getID().equals("")){
            TextView tv=(TextView)findViewById(R.id.detail);
            tv.setText("图书编号:"+book.getID()+"\n"+"图书名:"+book.getName()+"\n"+"图书作者:"+book.getAuthor()+"\n"+"藏书地点:"+book.getAddress());
        }
    }
}
