package com.xiao.weather.db;

/**
 * Created by hasee on 2016/6/27.
 */
public class WeatherBean {

    private String city;
    private String time;//更新时间
    private String weather;//天气
//    private String tmp;//温度
//    private String WD;//风向
//    private String WS;//风力
    private String h_tmp;//最高温度
    private String l_tmp;//最低温度
    private String date;//日期
//    private String sunrise;//日出时间
    private String citycode;//城市代码


    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    private String sunset;//日落时间

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getH_tmp() {
        return h_tmp;
    }

    public void setH_tmp(String h_tmp) {
        this.h_tmp = h_tmp;
    }

    public String getL_tmp() {
        return l_tmp;
    }

    public void setL_tmp(String l_tmp) {
        this.l_tmp = l_tmp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
