package com.xiao.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.xiao.weather.R;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout mView;//背景图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化控件
        mView = (RelativeLayout) findViewById(R.id.background);

        //设置缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5F,
                Animation.RELATIVE_TO_SELF,0.5F);
        scaleAnimation.setDuration(1000);//持续2s
        scaleAnimation.setFillAfter(true);//保持动画结束状态

        //设置渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);

        //设置动画集合
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);

        //启动动画
        mView.startAnimation(set);

        //监听动画
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束后，跳转至主页面
            @Override
            public void onAnimationEnd(Animation animation) {

                startActivity(new Intent(getApplicationContext(),WeatherActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
