package com.example.zhongweikang.beijingnew.Pager;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhongweikang.beijingnew.Pager.BasePager;

/**
 * Created by zhongweikang on 2017/2/17.
 */

public class servicePager extends BasePager {
    public servicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("智慧服务");
        imagebutton.setVisibility(View.GONE);
        TextView t=new TextView(mActivity);
        t.setText("nihoahohhkhafkhsdkfhk;skf");
        t.setGravity(Gravity.CENTER);
        fragmentLayout.addView(t);
    }
}
