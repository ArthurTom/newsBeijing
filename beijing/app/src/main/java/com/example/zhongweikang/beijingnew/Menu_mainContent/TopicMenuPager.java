package com.example.zhongweikang.beijingnew.Menu_mainContent;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * Created by zhongweikang on 2017/2/21.
 */

public class TopicMenuPager extends MenuAllBasePager{


    public TopicMenuPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        TextView textView=new TextView(MenuActivity);
        textView.setText("我是坐车点的666");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    @Override
    public void initData() {

    }
}
