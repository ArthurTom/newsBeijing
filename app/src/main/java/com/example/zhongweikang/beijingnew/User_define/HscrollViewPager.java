package com.example.zhongweikang.beijingnew.User_define;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager ,用于消除viewPager中嵌套的viewpager间的冲突问题
 */

public class HscrollViewPager extends ViewPager {
    float downX, downY;

    public HscrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 取消父容器对子空间的冲突相应
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
/*
*  上下拖动，父元素可拦截当前的事件
*  左右拖动：不可拦截事件，为第0页的时候，从左向右欢动，不拦截
*            为最后一页的时候，从右到左滑动，不拦截事件
*
*
* */
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                downX = ev.getX();
                downY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if(downY==-1){
                    downX = ev.getX();
                    downY = ev.getY();
                }

                float moveX = ev.getX();
                float moveY = ev.getY();
                float diffX=moveX-downX;
                float diffY=moveY-downY;
                if (Math.abs(diffX)>Math.abs(diffY)){  // 绝对值
                    // 横向滑动，父不可拦截
                   // getParent().requestDisallowInterceptTouchEvent(true);
                    if (getCurrentItem()==0&&diffX>0){
                       getParent().requestDisallowInterceptTouchEvent(false);
                        // 如何获取最后的那页
                    }else if (getCurrentItem()==getAdapter().getCount()-1&&diffX<0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                }else{
                    //竖着滑动，父可拦截时间，
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
