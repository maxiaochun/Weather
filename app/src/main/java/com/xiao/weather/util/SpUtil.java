package com.xiao.weather.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hasee on 2016/6/27.
 */
public class SpUtil {

    private static SharedPreferences sp;

    /**
     * 写入string数据到SharePreference
     * @param ctx
     * @param key
     * @param value
     */
    public static void putString(Context ctx ,String key,String value){
        if (sp == null){
            sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }

    /**
     * 读取string数据从SharePreference
     * @param ctx
     * @param key
     * @param defValue
     */
    public static String getString(Context ctx ,String key,String defValue){
        if (sp == null){
            sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
       return sp.getString(key,defValue);
    }

}
