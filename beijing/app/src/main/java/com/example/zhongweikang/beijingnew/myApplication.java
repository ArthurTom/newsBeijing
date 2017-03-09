package com.example.zhongweikang.beijingnew;

import android.app.Application;

import org.xutils.x;

/**
 * Created by zhongweikang on 2017/2/21.
 */

public class myApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
