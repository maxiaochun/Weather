package com.xiao.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xiao.weather.R;
import com.xiao.weather.db.Bean;
import com.xiao.weather.db.ForcastListBean;
import com.xiao.weather.db.WeatherBean;
import com.xiao.weather.db.WeatherDao;
import com.xiao.weather.util.ConstantValue;
import com.xiao.weather.util.HttpUtil;
import com.xiao.weather.util.SpUtil;
import com.xiao.weather.util.UrlUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/6/26.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {

    private TextView mContent;
    private ImageView mAdd;
    private TextView mArea;
    private TextView mDate;
    private TextView mWeather;
    private TextView mTemp;
    private TextView mUDtime;

    private String area;
    private String content1;
    private String texthightemp;
    private GridView mGridView;
    private String content;
    private JSONObject jsonObject;
    private WeatherBean bean;
    private TextView mTest;
    private List<Bean.RetDataBean.ForecastBean> forecast;
    private ArrayList<ForcastListBean> beanArrayList;
    private GridView mIndex;
    private String[] indexValues;
    private int[] imageIDs;
    private String[] indexItem;
    private ImageButton mShare;

    //第一次连接服务器，返回当天天气信息及城市码
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            //得到json数据
            String content = data.getString("content");

            try {
                //解析数据
                jsonObject = new JSONObject(content);
                parserData(jsonObject);

                //填充数据
                mUDtime.setText("更新时间：" + bean.getTime());
                mArea.setText(bean.getCity());
                mWeather.setText(bean.getWeather());
                mDate.setText(bean.getDate());
                mTemp.setText(bean.getL_tmp() + "°~" + bean.getH_tmp() + "°");

                //第2次连接服务器
                String citycode = bean.getCitycode();
                String url_1 = UrlUtil.http1 + area + "&citycode=" + citycode;
                HttpUtil.getDataFromService(url_1, handler1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    //第二次连接服务器返回数据
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Toast.makeText(WeatherActivity.this, "^^^^", Toast.LENGTH_SHORT).show();
            //得到天气预报及建议信息
            Bundle data1 = msg.getData();
            content1 = data1.getString("content");
            //第二次解析数据
            processData();
            mGridView.setAdapter(new MyAdapter());
            mIndex.setAdapter(new IndexAdapter());

            Toast.makeText(WeatherActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initView();
        initData();

        mAdd.setOnClickListener(this);
        mShare.setOnClickListener(this);


    }

    private void initView() {

        mAdd = (ImageButton) findViewById(R.id.ib__weather_add);
        mShare = (ImageButton) findViewById(R.id.ib_share);
        mArea = (TextView) findViewById(R.id.tv_area);
        mDate = (TextView) findViewById(R.id.tv_date);
        mWeather = (TextView) findViewById(R.id.tv_weather);
        mTemp = (TextView) findViewById(R.id.tv_temp);
        mUDtime = (TextView) findViewById(R.id.tv_updatetime);
        mGridView = (GridView) findViewById(R.id.gv_forcast);
        mIndex = (GridView) findViewById(R.id.gv_index);
    }


    private void initData() {
        //从sp中获取area数据
        area = SpUtil.getString(getApplicationContext(), ConstantValue.AREA, "");
        if (!area.isEmpty()) {
            String url = UrlUtil.http + area;
            //第一次获取数据
            HttpUtil.getDataFromService(url, handler);
        }

    }

    //第一次解析数据
    private void parserData(JSONObject json) {


        bean = new WeatherBean();

        try {
            //返回数据中ErrNum为0表示有该城市数据，-1表示没有找到该城市
            int errNum = json.getInt("errNum");
            if (errNum == -1) {
                Toast.makeText(WeatherActivity.this, "该城市信息不存在，请重新选择！",Toast.LENGTH_SHORT).show();
                //修改sp中数据（否则下次启动有异常）
                String default_area = SpUtil.getString(
                        getApplicationContext(), ConstantValue.DEFAULTAREA, "");//获取默认地区

                SpUtil.putString(getApplicationContext(), ConstantValue.AREA, default_area);//将默认地区设置为查询地区

                startActivity(new Intent(getApplicationContext(), QueryActivity.class));
                finish();
            } else if (errNum == 0) {
                //该地区信息存在，判断如果数据库中没有该城市，则添加到数据库
                //获取操作数据库的对象
                WeatherDao weatherDao = WeatherDao.getInstance(getApplicationContext());

                boolean contains = weatherDao.findAll().contains(area);
                if (contains == false) {//数据库中不包括该城市
                    //增加该城市到数据库
                    weatherDao.insert(area);
                    SpUtil.putString(getApplicationContext(),ConstantValue.DEFAULTAREA,area);//将该数据（即最新数据作为默认区域）
                }
                //标记不是第一次登陆
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.IS_FIRST_LOAD, false);

                JSONObject basic = json.getJSONObject("retData");

                bean.setCity(basic.getString("city"));
                bean.setTime(basic.getString("time"));
                bean.setWeather(basic.getString("weather"));
                bean.setDate(basic.getString("date"));
                bean.setH_tmp(basic.getString("h_tmp"));
                bean.setL_tmp(basic.getString("l_tmp"));
                bean.setSunset(basic.getString("sunset"));
                bean.setCitycode(basic.getString("citycode"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //第二次解析数据,并对天气预报和当日详情信息进行封装
    private void processData() {
        Gson gson = new Gson();
        Bean bean = gson.fromJson(content1, Bean.class);

        //天气预报需要展示的数据类型
        forecast = bean.getRetData().getForecast();//未来预报集合
        Bean.RetDataBean.HistoryBean history = bean.getRetData().getHistory().get(6);//前一天天气
        Bean.RetDataBean.TodayBean today = bean.getRetData().getToday();

        beanArrayList = new ArrayList<>(6); //需要展示的数据集合

        beanArrayList.add(new ForcastListBean());
        beanArrayList.add(new ForcastListBean());
        beanArrayList.add(new ForcastListBean());
        beanArrayList.add(new ForcastListBean());
        beanArrayList.add(new ForcastListBean());
        beanArrayList.add(new ForcastListBean());

        //填充前一天数据
        beanArrayList.get(0).setWeek("昨天");
        beanArrayList.get(0).setDate(history.getDate());
        beanArrayList.get(0).setType(history.getType());
        beanArrayList.get(0).setLowtemp(history.getLowtemp());
        beanArrayList.get(0).setHightemp(history.getHightemp());

        //填充当天数据
        beanArrayList.get(1).setWeek("今天");
        beanArrayList.get(1).setDate(today.getDate());
        beanArrayList.get(1).setType(today.getType());
        beanArrayList.get(1).setLowtemp(today.getLowtemp());
        beanArrayList.get(1).setHightemp(today.getHightemp());

        //填充未来四天天气
        for (int i = 2; i < 6; i++) {
            beanArrayList.get(i).setWeek(forecast.get(i - 2).getWeek());
            beanArrayList.get(i).setDate(forecast.get(i - 2).getDate());
            beanArrayList.get(i).setType(forecast.get(i - 2).getType());
            beanArrayList.get(i).setLowtemp(forecast.get(i - 2).getLowtemp());
            beanArrayList.get(i).setHightemp(forecast.get(i - 2).getHightemp());
        }

        //当天详细信息
        List<Bean.RetDataBean.TodayBean.IndexBean> index = bean.getRetData().getToday().getIndex();//指数集合
        String ganmao = index.get(0).getIndex();//感冒指数值
        String ziwaixian = index.get(1).getIndex();//紫外线强度
        String chuanyi = index.get(2).getIndex();//穿衣指数
        String yundong = index.get(3).getIndex();//运动指数

        indexItem = new String[]{"感冒指数", "日晒强度", "穿衣指数", "运动指数"};
        indexValues = new String[]{ganmao, ziwaixian, chuanyi, yundong};
        imageIDs = new int[]{R.mipmap.icon_gaomao, R.mipmap.icon_ziwaixian, R.mipmap.icon_chuanyi, R.mipmap.icon_yundong};

    }

    //按钮点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib__weather_add:
                startActivity(new Intent(getApplicationContext(), AddAreaActivity.class));
                break;
            case R.id.ib_share:
                //分享信息

        }

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return beanArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return beanArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(), R.layout.forcast_item, null);
            TextView mWeek = (TextView) view.findViewById(R.id.tv_week);
            TextView mDate = (TextView) view.findViewById(R.id.tv_forcast_date);
            TextView mTemp = (TextView) view.findViewById(R.id.tv_forcast_temp);
            TextView mType = (TextView) view.findViewById(R.id.tv_typt);
            ImageView mWeather = (ImageView) view.findViewById(R.id.iv_weather);

            mWeek.setText(beanArrayList.get(i).getWeek());

            String date = beanArrayList.get(i).getDate();
            String time = date.substring(5);
            mDate.setText(time);

            String lowtemp = beanArrayList.get(i).getLowtemp();
            String mLowtemp = lowtemp.substring(0, 2);
            String hightemp = beanArrayList.get(i).getHightemp();
            String mHightemp = hightemp.substring(0, 2);
            mTemp.setText(mLowtemp + "°/" + mHightemp + "°");

            String type = beanArrayList.get(i).getType();
            mType.setText(type);

            if (type.equals("晴")) {
                mWeather.setImageResource(R.mipmap.icon_sun);
            } else if (type.equals("多云")) {
                mWeather.setImageResource(R.mipmap.icon_duoyun);
            } else if (type.equals("阴")) {
                mWeather.setImageResource(R.mipmap.icon_yin);
            } else if (type.equals("阵雨") || type.equals("雷阵雨")) {
                mWeather.setImageResource(R.mipmap.icon_leizhenyu);
            } else if (type.equals("小雨") || type.equals("小到中雨")) {
                mWeather.setImageResource(R.mipmap.icon_xiaoyu);
            } else if (type.equals("中雨") || type.equals("中到大雨")) {
                mWeather.setImageResource(R.mipmap.icon_zhongyu);
            } else if (type.equals("大雨")) {
                mWeather.setImageResource(R.mipmap.icon_dayu);
                return view;
            } else {
                mWeather.setImageResource(R.mipmap.icon_cancel);
            }
            return view;
        }
    }

    private class IndexAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return indexValues.length;
        }

        @Override
        public Object getItem(int i) {
            return indexValues[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(), R.layout.myindex_item, null);
            ImageView iv_index_img = (ImageView) view.findViewById(R.id.iv_index_img);
            TextView tv_index_value = (TextView) view.findViewById(R.id.tv_index_value);
            TextView tv_index_type = (TextView) view.findViewById(R.id.tv_index_type);

            iv_index_img.setBackgroundResource(imageIDs[i]);
            if (!indexValues[i].isEmpty()) {
                tv_index_value.setText(indexValues[i]);
            } else {
                tv_index_value.setText("正常");
            }
            tv_index_type.setText(indexItem[i]);
            return view;
        }
    }
}