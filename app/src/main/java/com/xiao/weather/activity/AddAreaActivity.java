package com.xiao.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xiao.weather.R;

/**
 * Created by hasee on 2016/6/26.
 */
public class AddAreaActivity extends Activity {

    private ImageView mAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addarea);

        initView();

        //点击添加按钮，跳转区域查询页面
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),QueryActivity.class));
            }
        });
    }

    private void initView() {
        mAdd = (ImageView) findViewById(R.id.iv_add);
    }
}
