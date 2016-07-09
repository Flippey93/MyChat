package com.flippey.mychat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/9 20:06
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{

    private String mTableName;

    public MySQLiteOpenHelper(Context context, String tablename) {
        this(context, "contact.db", null, 1);
        this.mTableName = tablename;
    }

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + mTableName + "(_id integer primary key ,c_contact varchar(30))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
