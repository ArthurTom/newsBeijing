package com.example.zhongweikang.beijingnew.Menu_mainContent;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zhongweikang.beijingnew.Bean.New_Content_Bean;
import com.example.zhongweikang.beijingnew.MainActivity;
import com.example.zhongweikang.beijingnew.R;
import com.example.zhongweikang.beijingnew.newsDetailPage.new_detail_base;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.db.converter.ShortColumnConverter;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻的界面
 */

public class NewMenuDetalPager extends MenuAllBasePager {
    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;
    @ViewInject(R.id.new_pager_main)
    private ViewPager new_pager_main_viewPager;
    @ViewInject(R.id.next_btn)
    private ImageButton next_tab;

    List<New_Content_Bean.DataBean.ChildrenBean> children;  // 数据

    public NewMenuDetalPager(Activity activity, List<New_Content_Bean.DataBean.ChildrenBean> children) {
        super(activity);
        this.children = children;   // 拿到数据
    }


    @Override
    public View initView() {
        View view = View.inflate(MenuActivity, R.layout.newdetail, null);  // 将 新闻详细设计的界面引入
        x.view().inject(this, view);
        /* 点击按钮切换到下一个页签*/
        next_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_pager_main_viewPager.setCurrentItem(new_pager_main_viewPager.getCurrentItem() + 1);
            }
        });

        return view;
    }

    List<new_detail_base> m;  // 集合放入基类型为new_detail_base的

    @Override
    public void initData() {
        // 准备页签的数据，里面存放的是每个标签
        m = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            // 只有在北京这个时候，才可以开启slidingMenu，由i==0是否为真判定是否开启
            m.add(new new_detail_base(MenuActivity, children.get(i),i==0));

        }

        newMenuDetalPageAdapter Nadapter = new newMenuDetalPageAdapter(m);
        new_pager_main_viewPager.setAdapter(Nadapter);
        // 为指示器关联viewPager对象,关联之后数据由关联的adapter提供
        mIndicator.setViewPager(new_pager_main_viewPager);
        new_pager_main_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity newMenuDeatilPagerMainActivity = (MainActivity) MenuActivity;
                SlidingMenu slidingMenu = newMenuDeatilPagerMainActivity.getSlidingMenu();
                if (position == 0) {
                    // 当为北京的页签的时候，滑动左滑，可以触发，是左侧菜单拖拽出来

                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

                } else {
                    // 当不为北京的页签的时候，左滑，左侧菜单不触发，只触发viewpage左滑事件
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    /* 设置一个pageadatper 用于填充新闻内容中的viewpage*/
    class newMenuDetalPageAdapter extends PagerAdapter {

        List<new_detail_base> m;

        public newMenuDetalPageAdapter(List<new_detail_base> m) {
            this.m = m;
        }

        /* 此方法给指示器提供页签数据,这个方法是指示器中有的*/
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return m.size();
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
            new_detail_base m_pager = m.get(position);
            container.addView(m_pager.rView);
            m_pager.initData();  // 加载数据
            return m_pager.rView;
        }
    }


}