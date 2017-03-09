package com.example.zhongweikang.beijingnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.zhongweikang.beijingnew.utils.CacheUtils;

public class welcome extends Activity {
   private ImageView welcomeImage;
    private RelativeLayout rela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        init();
    }
    /* 加载拿到控件方法*/
    private void initView() {
      welcomeImage= (ImageView) findViewById(R.id.welcomeImage);
        rela= (RelativeLayout) findViewById(R.id.activity_main);
    }
      /*加载动画方法*/
    public void init() {
        /* 动画合集*/
        AnimationSet animationset = new AnimationSet(false);
        animationset.setDuration(1000);
       /* 旋转动画*/
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true); //设置动画停止在动画结束处
        /* 缩放动画*/
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        /* 渐变色*/
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
      /*为控件添加动画属性设置*/
        animationset.addAnimation(rotateAnimation);
        animationset.addAnimation(scaleAnimation);
        animationset.addAnimation(alphaAnimation);

       /*控件开始动画*/
        welcomeImage.startAnimation(animationset);

        /* 动画的监听事件*/
        animationset.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /* 动画结束的时候判定，是进入主界面还是引导界面*/
                boolean IsOpenFirst= CacheUtils.getIsOpenFirst(welcome.this);
               if (IsOpenFirst){
                   Log.d("111","跳转到引导界面");
                 Intent intent=new Intent(welcome.this,LedActivity.class);
                   startActivity(intent);
                   finish();

               }else {

                   Log.d("111","跳转到主界面");

                   Intent intent=new Intent(welcome.this,MainActivity.class);
                   startActivity(intent);
                   finish();
               }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
