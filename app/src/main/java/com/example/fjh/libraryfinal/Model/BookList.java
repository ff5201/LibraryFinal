package com.example.fjh.libraryfinal.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fjh.libraryfinal.SQLiteDB.DBconnection;

import java.util.ArrayList;

/**
 * Created by FJH on 2017/5/2.
 */

public class BookList extends ArrayList<Book> {

    private static final long serialVersionUID = 1L;

    private static BookList bookList = null;
    private static boolean flag=true;


    private BookList() {

    }

    public static BookList getBookList() {
        if (bookList == null||flag==false) {
            bookList = new BookList();
            DBconnection dBconnection = new DBconnection();
            SQLiteDatabase db = dBconnection.getConnection();
            Cursor cursor = db.query("book", null, null, null, null, null, null);
            cursor.moveToFirst();
            do{
                int idNum = cursor.getColumnIndex("ID");
                int nameNum = cursor.getColumnIndex("name");
                int authorNum = cursor.getColumnIndex("author");
                int addressNum=cursor.getColumnIndex("address");
                String id = cursor.getString(idNum);
                String name = cursor.getString(nameNum);
                String author = cursor.getString(authorNum);
                String address=cursor.getString(addressNum);
                Book book = new Book(id, name, author,address);
                bookList.add(book);
            }while (cursor.moveToNext());
            dBconnection.close(db);
        }
        flag=true;
        return bookList;
    }

    public static BookList getBookList(String selection , String[] selectionArgs) {
        bookList = new BookList();
        DBconnection dBconnection = new DBconnection();
        SQLiteDatabase db = dBconnection.getConnection();
        Cursor cursor = db.query("book", null, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            do{
                int idNum = cursor.getColumnIndex("ID");
                int nameNum = cursor.getColumnIndex("name");
                int authorNum = cursor.getColumnIndex("author");
                int addressNum=cursor.getColumnIndex("address");
                String id = cursor.getString(idNum);
                String name = cursor.getString(nameNum);
                String author = cursor.getString(authorNum);
                String address=cursor.getString(addressNum);
                Book book = new Book(id, name, author,address);
                bookList.add(book);
            }while (cursor.moveToNext());
        }else{

        }
        dBconnection.close(db);
        Log.d("flag","false");
        flag=false;
        return bookList;
    }


    /**
     * 查找ID
     */
    private boolean checkID(String ID) {
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            if (book.getID().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到图书位置
     */
    private int getIndex(String ID) {
        int i = 0;
        for (; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            if (book.getID().equals(ID)) {
                break;
            }
        }
        return i;
    }

    /**
     * 查找名称
     */
    public boolean checkNmae(String name) {
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            if (book.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean insert(Book book) {
        if (checkID(book.getID())) {
            return false;
        } else {
            bookList.add(book);
            String id = book.getID();
            String name = book.getName();
            String author = book.getAuthor();
            String address=book.getAddress();
            DBconnection dBconnection = new DBconnection();
            SQLiteDatabase db = dBconnection.getConnection();
            String sql = "insert into book (ID,name,author,address) values ('" + id + "','" + name + "','" + author + "','"+address+"')";
            db.execSQL(sql);
            db.close();
            return true;
        }
    }

    public boolean delete(String id) {
        if (checkID(id)) {
            bookList.remove(getIndex(id));
            DBconnection dBconnection = new DBconnection();
            SQLiteDatabase db = dBconnection.getConnection();
            String sql = "delete from book where ID = '" + id + "'";
            db.execSQL(sql);
            return true;
        } else {
            return false;
        }
    }

    public boolean set(Book book,String hideID) {
        if (checkID(hideID)) {
            String id = book.getID();
            String name = book.getName();
            String author = book.getAuthor();
            String address=book.getAddress();
            Log.d("model修改数据",hideID);
            int index = getIndex(hideID);
            bookList.set(index, book);
            DBconnection dBconnection = new DBconnection();
            SQLiteDatabase db = dBconnection.getConnection();
            String sql = "update book set name='" + name + "',author='" + author + "',address = '"+address+"',ID = '"+id+"' where ID = '" + hideID + "'";
            Log.d("model修改数据sql",sql);
            db.execSQL(sql);
            return true;
        } else {
            return false;
        }
    }
}
