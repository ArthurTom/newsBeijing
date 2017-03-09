package com.example.zhongweikang.beijingnew.Pager;

import android.app.Activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.zhongweikang.beijingnew.MainActivity;
import com.example.zhongweikang.beijingnew.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 5个页面的级类，这5个界面都有相同布局提取出来的
 */

public class BasePager {

    @ViewInject(R.id.tittle)
    public TextView textView;  //标题控件
    @ViewInject(R.id.image_button)
    public ImageButton imagebutton; // 菜单按钮
    @ViewInject(R.id.fragement)
    public FrameLayout fragmentLayout;  // 正文内容

    public Activity mActivity;
    public View mView;

    public BasePager(Activity activity) {
        this.mActivity = activity;
        mView = initView(); // 拿到界面
    }

    /* 初始化当前的布局*/
    private View initView() {
        // 拿到当前页面的布局
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        x.view().inject(this, view);
        /* 左上角的图片按钮方法监听*/
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 这个按钮控制左侧菜单是否显示 */
                 MainActivity mpActivity=(MainActivity) mActivity;
                SlidingMenu slidingMenu=mpActivity.getSlidingMenu();
                slidingMenu.toggle();
            }
        });
        return view;
    }

    // 初始化数据，子类覆盖次方法，进行数据化填充
    public void initData() {

    }
}
