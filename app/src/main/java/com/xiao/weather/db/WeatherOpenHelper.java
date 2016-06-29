package com.xiao.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hasee on 2016/6/26.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper{

    public WeatherOpenHelper(Context context) {
        super(context, "addarea.db", null, 1);//创建数据库
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库中的表
        db.execSQL("create table area(_id integer primary key autoincrement,area varcher(20));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
