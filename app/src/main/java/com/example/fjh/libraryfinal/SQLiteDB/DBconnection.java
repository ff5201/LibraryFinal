package com.example.fjh.libraryfinal.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by FJH on 2017/5/3.
 */

public class DBconnection extends SQLiteOpenHelper {


    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "book.db";

    private static Context context;

    public DBconnection() {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void setContext(Context context) {
        DBconnection.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "create table book( ID varchar(30) not null , name varchar(30) not null , author varchar(30) not null, address varchar(30) not null )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getConnection() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }

    public void close(SQLiteDatabase db) {
        db.close();
    }
}
