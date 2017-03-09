package com.example.zhongweikang.beijingnew.Pager;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongweikang.beijingnew.Pager.BasePager;
import com.example.zhongweikang.beijingnew.R;

/**
 * 界面一： 用于继承BasePager然后同时设置标题和按钮的隐藏
 */

public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }




    @Override
    public void initData() {
        textView.setText("智慧北京");
        TextView t=new TextView(mActivity);
        t.setText("nihoahohhkhafkhsdkfhk;skf");
        t.setGravity(Gravity.CENTER);
        fragmentLayout.addView(t);


    }


}
