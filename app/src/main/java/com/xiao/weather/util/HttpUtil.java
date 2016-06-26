package com.xiao.weather.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hasee on 2016/6/26.
 */
public class HttpUtil {

    private static String result;

    public static void getDataFromService(final String url , final Handler handler){

        new Thread(new Runnable() {
            @Override
            public void run() {
                request(url,handler);
            }
        });
    }

    /**
     * 请求网络
     * @param url
     * @param handler
     */
    public static void request(String url ,Handler handler){
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();

        try {
            URL url1 = new URL(url);
            HttpURLConnection connection =(HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey","46f381e0f57b540e8e288081876bfbd1");
            connection.connect();

            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String strRead = null;
            while ((strRead = reader.readLine())!= null){
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();//从网络获取的数据

            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("content",result);
            msg.setData(bundle);//将数据内容传递到msg
            handler.sendMessage(msg);//将msg发送到主线程

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
