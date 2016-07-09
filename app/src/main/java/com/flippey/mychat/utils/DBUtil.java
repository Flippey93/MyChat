package com.flippey.mychat.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.flippey.mychat.db.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/9 20:12
 */
public class DBUtil {

    //保存到数据库
    public static void updateTable(Context context, String username, List<String> contacts) {
        if (contacts == null || contacts.size() < 1) {
            return;
        }
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(context, username);
        SQLiteDatabase db =
                mySQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        //若数据库存在，则先删除数据库
        db.delete(username, null, null);
        ContentValues values = new ContentValues();
        for (int i = 0; i < contacts.size(); i++) {
            values.put("c_contact", contacts.get(i));
            db.insert(username,null,values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static ArrayList<String> getContacts(String username, Context context) {
        ArrayList<String> contacts = new ArrayList<>();
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(context, username);
        SQLiteDatabase rb = mySQLiteOpenHelper.getReadableDatabase();
        //如果数据库不存在则创建数据库
        rb.execSQL("create table if not exists "+username+"(_id integer primary key ,c_contact varchar(30))");
        Cursor cursor = rb.query(username, new String[]{"c_contact"}, null, null, null, null,
                "c_contact");
        while (cursor.moveToNext()) {
            String contact = cursor.getString(0);
            contacts.add(contact);
        }
        cursor.close();
        rb.close();
        return contacts;
    }
}
