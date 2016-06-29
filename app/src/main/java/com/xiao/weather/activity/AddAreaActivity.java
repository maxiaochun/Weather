package com.xiao.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiao.weather.R;
import com.xiao.weather.db.WeatherDao;
import com.xiao.weather.util.ConstantValue;
import com.xiao.weather.util.SpUtil;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/6/26.
 */
public class AddAreaActivity extends Activity implements View.OnClickListener {

    private ImageButton mAdd;
    private ImageButton mEdit;
    private ImageButton mBack;
    private ListView mAreaList;
    private ArrayList<String> areaList;
    private int[] mWaitImageIds;
    private ImageButton mDelete;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(AddAreaActivity.this, areaList.toString(), Toast.LENGTH_SHORT).show();

            adapter = new AreaAdapter();
            mAreaList.setAdapter(adapter);


        }
    };
    private AreaAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addarea);

        initView();

        mAdd.setOnClickListener(this);
        mBack.setOnClickListener(this);
        initData();
        //设置条目点击事件和删除操作


    }

    private void initView() {
        mAdd = (ImageButton) findViewById(R.id.ib_addarea_add);
        mBack = (ImageButton) findViewById(R.id.iv_back);
        mAreaList = (ListView) findViewById(R.id.area_list);
        //设置条目点击事件
        mAreaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击后将该城市添加到sp,返回weather页面
                String area = areaList.get(i);
                SpUtil.putString(getApplicationContext(), ConstantValue.AREA,area);
                //跳转页面
                startActivity(new Intent(getApplicationContext(),WeatherActivity.class));

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_addarea_add:
                //点击添加按钮，跳转到查询页面
                startActivity(new Intent(getApplicationContext(), QueryActivity.class));
                break;

            case R.id.iv_back:
                //点击返回按钮，返回上一页
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                break;

            default:
                break;
        }
    }

    /**
     * 获取listview所需的数据
     */
    private void initData() {

        //封装listview 图片数据
        mWaitImageIds = new int[]{R.mipmap.area1,R.mipmap.area2,R.mipmap.area3,
                R.mipmap.area4, R.mipmap.area5,R.mipmap.area6,R.mipmap.area7,R.mipmap.area8};

        //获取数据库中的数据
        new Thread(){
            @Override
            public void run() {
                //获取操作数据库的对象
                WeatherDao weatherDao = WeatherDao.getInstance(getApplicationContext());
                //查询所有数据
                areaList = weatherDao.findAll();
                //通知子线程
                handler.sendEmptyMessage(0);
            }
        }.start();

    }

    private class AreaAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return areaList.size();
        }

        @Override
        public Object getItem(int i) {
            return areaList.get(i);

        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(), R.layout.area_list, null);
            ImageView mWait = (ImageView) view.findViewById(R.id.iv_wait);
            TextView mDataArea = (TextView) view.findViewById(R.id.tv_data_area);
            mDelete = (ImageButton) view.findViewById(R.id.ib_delete);

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //1.数据库删除
                    WeatherDao Dao = WeatherDao.getInstance(getApplicationContext());
                    Dao.delete(areaList.get(i));
                    //2.集合中删除
                    areaList.remove(i);
                    //3.通知适配器刷新
                    if (adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            if(areaList.size() < 8){
                mWait.setBackgroundResource(mWaitImageIds[i]);
            }else {
                mWait.setBackgroundResource(mWaitImageIds[0]);
            }
            mDataArea.setText(areaList.get(i));


            return view;
        }
    }
}
