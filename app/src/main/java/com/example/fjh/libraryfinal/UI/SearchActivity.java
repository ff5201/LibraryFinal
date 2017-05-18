package com.example.fjh.libraryfinal.UI;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fjh.libraryfinal.Control.ActivityCollector;
import com.example.fjh.libraryfinal.Control.DBController;
import com.example.fjh.libraryfinal.Model.Book;
import com.example.fjh.libraryfinal.Model.BookList;
import com.example.fjh.libraryfinal.R;
import com.example.fjh.libraryfinal.SQLiteDB.DBconnection;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by FJH on 2017/5/14.
 */

public class SearchActivity extends AppCompatActivity {

    EditText txtID;
    EditText txtName;
    EditText txtAuthor;
    EditText txtAddress;
    RadioGroup rg;
    private TableRow temptr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        setTitle("图书查询");
        DBconnection.setContext(this.getApplicationContext());
        createTable(BookList.getBookList());


        txtID=(EditText)findViewById(R.id.ID);
        txtName=(EditText)findViewById(R.id.name);
        txtAuthor=(EditText)findViewById(R.id.author);
        txtAddress=(EditText)findViewById(R.id.address) ;

        //查找按钮
        Button btn_submit=(Button)findViewById(R.id.btn_search_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = "1=1";
                String[] selectionArgs = null;
                ArrayList<String> selectionList = new ArrayList<String>();
                Log.d("selection","点击");
                if(!txtID.getText().toString().equals("")){
                    selection = selection+" and ID=?";
                    selectionList.add(txtID.getText().toString());
                }
                if(!txtName.getText().toString().equals("")){
                    selection = selection+" and name=?";
                    selectionList.add(txtName.getText().toString());
                }
                if(!txtAuthor.getText().toString().equals("")){
                    selection = selection+" and author=?";
                    selectionList.add(txtAuthor.getText().toString());
                }
                if(!txtAddress.getText().toString().equals("")){
                    selection=selection+" and address=?";
                    selectionList.add(txtAddress.getText().toString());
                }
                if(selectionList.size()>0){
                    selectionArgs = new String[selectionList.size()];
                    selectionList.toArray(selectionArgs);
                }
                else
                    selection = "";
                Log.d("selection",selection);
                searchData(selection , selectionArgs);
            }
        });



        //清空按钮
        Button btn_cancel=(Button)findViewById(R.id.btn_search_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtID.setText("");
                txtName.setText("");
                txtAuthor.setText("");
                txtAddress.setText("");
                rg=(RadioGroup)findViewById(R.id.search_radio);
                rg.clearCheck();
            }
        });

        //返回按钮
        Button btn_return=(Button)findViewById(R.id.btn_search_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),HomeActivity.class));
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //初始化表格
    private void createTable(BookList bookList) {
        TableLayout tb = (TableLayout) findViewById(R.id.booktable);
        Log.d("1",String.valueOf(bookList.size()));
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            String id = book.getID();
            String name = book.getName();
            String author = book.getAuthor();
            String address=book.getAddress();
            TableRow row = new TableRow(this);
            TextView tid = new TextView(this);
            TextView tname = new TextView(this);
            TextView tauthor = new TextView(this);
            TextView taddress = new TextView(this);
            tid.setText(id);
            tid.setGravity(Gravity.CENTER);
            tid.setHeight(100);
            tname.setText(name);
            tname.setGravity(Gravity.CENTER);
            tauthor.setText(author);
            tauthor.setGravity(Gravity.CENTER);
            taddress.setText(address);
            taddress.setGravity(Gravity.CENTER);
            row.addView(tid);
            row.addView(tname);
            row.addView(tauthor);
            row.addView(taddress);
            tb.addView(row,tb.getChildCount()-1);
            //表格行长按监听
           /*row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //TableRow tr=(TableRow)v;
                   // TextView tv=(TextView) tr.getChildAt(0);
                   // Toast.makeText(getApplicationContext(),tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("fds","anle");
                    return true;
                }
            });*/
            row.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    //获取当前行
                    temptr=(TableRow)v;
                    menu.add(0,2,0,"详情");
                    menu.add(0,3,0,"修改");
                    menu.add(0,4,0,"删除");
                    menu.add(0,5,0,"借书");
                }
            });
        }
        TextView tv=(TextView)findViewById(R.id.total) ;
        tv.setText("共"+bookList.size()+"本书");
    }

    //清空表格
    public void clearTable(){
        TableLayout tb = (TableLayout) findViewById(R.id.booktable);
        //Log.d("行数",String.valueOf(tb.getChildCount()));
        tb.removeViews(2,tb.getChildCount()-3);
    }

    //长按表格菜单
    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        setTitle("菜单");
        menu.add(0,2,0,"详情");
        menu.add(0,3,0,"修改");
        menu.add(0,4,0,"删除");
        menu.add(0,5,0,"借书");
        super.onCreateContextMenu(menu, v, menuInfo);
    }*/

    //选择后的操作
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String id,name,author,address;
        TextView tv=(TextView)temptr.getChildAt(0);
        id=tv.getText().toString();
        tv=(TextView)temptr.getChildAt(1);
        name=tv.getText().toString();
        switch (item.getItemId()){
            case 2:
                detailsBook(id);
                Log.d("上下文菜单","详情");
                break;
            case 3:
                updateBook(id);
                Log.d("上下文菜单","修改");
                break;
            case 4:
                deleteBook(id);
                Log.d("上下文菜单","删除");
                break;
            case 5:
                Toast.makeText(getApplicationContext(),"你成功借阅"+name,Toast.LENGTH_SHORT).show();
                Log.d("上下文菜单","借书");
                break;
        }
        return true;
    }
    public void detailsBook(String id){
        BookList bookList=BookList.getBookList();
        Book book=new Book("","","","");
        for (Book b:bookList
             ) {
            if(b.getID().equals(id)){
                book=b;
            }
        }
        Intent intent=new Intent(SearchActivity.this,detailsActivity.class);
        Bundle mExtra=new Bundle();
        mExtra.putSerializable("Book",book);
        intent.putExtras(mExtra);
        startActivity(intent);
    }
    public void deleteBook(String id){
        DBController controller=new DBController();
        if(controller.deleteBook(id)){
            Log.d("删除状态","删除成功");
            clearTable();
            createTable(BookList.getBookList());
        }else{
            Log.d("删除状态","删除失败");
        }
    }
    public void updateBook(String id){
        BookList bookList=BookList.getBookList();
        Book book=new Book("","","","");
        for (Book b:bookList
                ) {
            if(b.getID().equals(id)){
                book=b;
            }
        }
        Intent intent=new Intent(SearchActivity.this,AddActivity.class);
        Bundle mExtra=new Bundle();
        mExtra.putSerializable("Book",book);
        intent.putExtras(mExtra);
        startActivity(intent);
    }

    //查找书籍
    public void searchData(String selection , String[] selectionArgs){
        clearTable();
        DBController controller=new DBController();
        BookList books = controller.getTable(selection,selectionArgs);
        Log.d("book",String.valueOf(books.size()));
        createTable(books);
    }



}
