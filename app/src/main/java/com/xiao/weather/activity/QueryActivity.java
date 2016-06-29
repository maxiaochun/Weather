package com.xiao.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xiao.weather.R;
import com.xiao.weather.util.ConstantValue;
import com.xiao.weather.util.SpUtil;

/**
 * Created by hasee on 2016/6/26.
 */
public class QueryActivity extends Activity{

    private ImageButton mCancel;
    private ImageButton mQuery;
    private EditText mEditText;
    private String mAddArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        initView();

        //点击查询按钮，记录数据，返回天气页
        mQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //editText数据
                mAddArea = mEditText.getText().toString();

                //全部地区数据库比对。。。。。

                SpUtil.putString(getApplicationContext(), ConstantValue.AREA, mAddArea);
//                Toast.makeText(QueryActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),WeatherActivity.class));

            }
        });

        //点击取消按钮，返回上一页
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddAreaActivity.class));
            }
        });


    }

    private void initView() {
        mEditText = (EditText) findViewById(R.id.et_city);
        mQuery = (ImageButton) findViewById(R.id.ib_query);
        mCancel = (ImageButton) findViewById(R.id.ib_cancel);
    }
}
