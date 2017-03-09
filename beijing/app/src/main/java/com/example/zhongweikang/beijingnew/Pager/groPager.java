package com.example.zhongweikang.beijingnew.Pager;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zhongweikang on 2017/2/17.
 */

public class groPager extends BasePager {
    public groPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("政府");
        imagebutton.setVisibility(View.GONE);
        TextView t=new TextView(mActivity);
        t.setText("nihoahohhkhafkhsdkfhk;skf");
        t.setGravity(Gravity.CENTER);
        fragmentLayout.addView(t);
    }
}
