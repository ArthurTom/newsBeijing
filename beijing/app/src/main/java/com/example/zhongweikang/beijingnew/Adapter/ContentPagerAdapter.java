package com.example.zhongweikang.beijingnew.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhongweikang.beijingnew.Pager.BasePager;

import java.util.List;

/**
 * 这是viewPager的adpter，用于数据的填充
 */

public class ContentPagerAdapter extends PagerAdapter {

    private List<BasePager> mlist;

    public ContentPagerAdapter(List<BasePager> mlist) {
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        BasePager basePagerView = mlist.get(position);
        container.addView(basePagerView.mView);
    //    basePagerView.initData();  //  初始化数据,不能再这里加载，次方法会预加载
        return basePagerView.mView;
    }
}
