package com.example.zhongweikang.beijingnew.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhongweikang on 2017/2/15.
 */

public class ViewPageAdapter extends PagerAdapter {
    private  Context context;
    private  List<ImageView> list;

    public ViewPageAdapter(Context context, List<ImageView> list){
        this.context=context;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
         container.addView( list.get(position));

        return  list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }
}
