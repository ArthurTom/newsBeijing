package com.example.zhongweikang.beijingnew.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *  自定义的viewpager
 */

public class Content_main_viewPager extends ViewPager {

    public Content_main_viewPager(Context context) {
        super(context);
    }

    public Content_main_viewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* 是否拦截，用于处理大的viewpage导致里面的viewpager的冲突*/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
