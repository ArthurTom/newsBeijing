package com.example.zhongweikang.beijingnew.Menu_mainContent;

import android.app.Activity;
import android.view.View;

/**
 * 四个界面的父类
 */

public abstract class MenuAllBasePager {
    public Activity MenuActivity;
    public View rView;
    public MenuAllBasePager(Activity activity) {
        this.MenuActivity = activity;
        rView =initView();
    }

    /*抽象的方法，子类必须实现实现各自不同的布局 */
    public abstract View initView();
    // 子类覆盖此方法，实现数据初始化

    public void initData(){

    }

}
