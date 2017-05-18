package com.example.fjh.libraryfinal.Control;

import android.util.Log;


import com.example.fjh.libraryfinal.Model.Book;
import com.example.fjh.libraryfinal.Model.BookList;

import java.util.ArrayList;


public class DBController {
    //查找全部图书
    public BookList searchBook() {
        BookList bookList = BookList.getBookList();
        return bookList;
    }

    //查询部分图书
    public BookList getTable(String selection , String[] selectionArgs){
        BookList bookList=BookList.getBookList(selection,selectionArgs);
        Log.d("book",String.valueOf(bookList.size()));
        return bookList;
    }


    //删除图书
    public boolean deleteBook(String id) {
        BookList bookList = BookList.getBookList();
        if (bookList.delete(id)) {
            return true;
        }
        return false;
    }

    //添加图书
    public boolean addBook(String id, String name, String author,String address) {
        BookList bookList = BookList.getBookList();
        Book book = new Book(id, name,author,address);
        if (bookList.insert(book)) {
            return true;
        } else {
            return false;
        }
    }

    //修改图书
    public boolean setBook(String id, String name, String author,String address,String hideID) {
        BookList bookList = BookList.getBookList();
        Book book = new Book(id, name, author,address);
        Log.d("控制器修改",book.getID());
        if (bookList.set(book,hideID)) {
            return true;
        } else {
            return false;
        }
    }

}
