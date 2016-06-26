package com.xiao.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiao.weather.R;
import com.xiao.weather.util.HttpUtil;
import com.xiao.weather.util.UrlUtil;

/**
 * Created by hasee on 2016/6/26.
 */
public class WeatherActivity extends Activity {

    private TextView mContent;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(WeatherActivity.this, "!!!!!!!!!", Toast.LENGTH_SHORT).show();
            Bundle data = msg.getData();
            String content = data.getString("content");
            mContent.setText(content);
        }
    };
    private ImageView mAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initView();
        initData();

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddAreaActivity.class));
            }
        });

    }

    private void initView() {
        mContent = (TextView) findViewById(R.id.textview);
        mAdd = (ImageView) findViewById(R.id.iv_add);
    }

    private void initData() {
        HttpUtil.getDataFromService(UrlUtil.allcitys, handler);
    }

}
