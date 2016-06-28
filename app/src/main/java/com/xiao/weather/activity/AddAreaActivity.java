package com.xiao.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.xiao.weather.R;
import com.xiao.weather.util.ConstantValue;
import com.xiao.weather.util.SpUtil;

/**
 * Created by hasee on 2016/6/26.
 */
public class AddAreaActivity extends Activity implements View.OnClickListener {

    private ImageButton mAdd;
    private ImageButton mEdit;
    private ImageButton mBack;
    private ListView mAreaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addarea);

        initView();

        mAdd.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mEdit.setOnClickListener(this);

        initData();

    }

    private void initView() {
        mAdd = (ImageButton) findViewById(R.id.iv_add);
        mBack = (ImageButton) findViewById(R.id.iv_back);
        mEdit = (ImageButton) findViewById(R.id.iv_edit);
        mAreaList = (ListView) findViewById(R.id.area_list);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_add:
                //点击添加按钮，跳转到查询页面
                startActivity(new Intent(getApplicationContext(), QueryActivity.class));
                break;

            case R.id.iv_back:
                //点击返回按钮，返回上一页
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                break;

            case R.id.iv_edit:
                //点击编辑按钮，。。。。（待定）
                break;

            default:
                break;
        }
    }

    /**
     * 获取listview所需的数据
     */
    private void initData() {
        String area = SpUtil.getString(getApplicationContext(), ConstantValue.AREA, "");

        //将数据添加到数据库


    }

}
