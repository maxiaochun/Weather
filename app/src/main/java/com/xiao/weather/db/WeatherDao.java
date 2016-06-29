package com.xiao.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/6/28.
 */
public class WeatherDao {

    private  WeatherOpenHelper weatherOpenHelper;

    //单例模式
    //1.私有化构造方法
    private WeatherDao(Context context){
        //创建数据库及表结构
        weatherOpenHelper = new WeatherOpenHelper(context);
    }
    //2.声明一个当前类的对象
    private static WeatherDao weatherDao = null;
    //3.提供一个方法，如果当前类的对象为空，创建新的
    public static WeatherDao getInstance(Context context){
        if (weatherDao == null){
             weatherDao = new WeatherDao(context);
        }
        return weatherDao;
    }

    /**
     * 增加一个条目
     * @param area
     */
    public void insert(String area){
    //1.开启数据库，准备写入操作
    SQLiteDatabase db =  weatherOpenHelper.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put("area",area);
    db.insert("area",null,values);

    db.close();
}

    /**
     * 删除
     * @param area
     */
    public void delete(String area){
        SQLiteDatabase db =  weatherOpenHelper.getWritableDatabase();
        db.delete("area","area = ?",new String[]{area});
        db.close();
    }

    /**
     * 查询
     * @param
     */
    public ArrayList<String> findAll(){
        SQLiteDatabase db =  weatherOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("area", new String[]{"area"}, null, null, null, null, "_id desc");//最后一个参数指按id倒序排列

        ArrayList<String> areaList = new ArrayList<>();
        while (cursor.moveToNext()){
            String mArea = cursor.getString(0);
            areaList.add(mArea);
        }
        cursor.close();
        db.close();

        return areaList;
    }

}