package com.example.zhongweikang.beijingnew.Fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.example.zhongweikang.beijingnew.Adapter.ContentPagerAdapter;
import com.example.zhongweikang.beijingnew.Pager.HomePager;
import com.example.zhongweikang.beijingnew.Pager.NewsPager;
import com.example.zhongweikang.beijingnew.Pager.SettingPager;
import com.example.zhongweikang.beijingnew.Pager.groPager;
import com.example.zhongweikang.beijingnew.Pager.servicePager;
import com.example.zhongweikang.beijingnew.Pager.BasePager;
import com.example.zhongweikang.beijingnew.MainActivity;
import com.example.zhongweikang.beijingnew.R;
import com.example.zhongweikang.beijingnew.View.Content_main_viewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Xutils工具的使用，主要是使用注解开发
 */

public class Main_Fragment extends BaseFragment {
    @ViewInject(R.id.main_viewpage)  // 手写的
    private Content_main_viewPager viewPager;
    @ViewInject(R.id.main_radiogroup)
    private  RadioGroup radioGroup;
    /* intitView 是必须要重写的方法*/
    List<BasePager>  list;
    @Override
    public View intitView() {
        View view=View.inflate(activity, R.layout.main_content_fragment,null);
            // 将view这个布局的对象注入到xUtils中去,这里是新版的用法，在fragment中使用
        x.view().inject(this,view);
        return view;
    }
    @Override
    public void initData() {
        // 监听单选按钮组中的按钮选中的变化
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case  R.id.home:
                        // 设置当前显示的页面
                        viewPager.setCurrentItem(0);
                        isEnabledSlideingMenu(false);  // 判定是否滑出的方法

                        break;
                    case  R.id.new_center:
                        viewPager.setCurrentItem(1);
                        isEnabledSlideingMenu(true);
                        break;
                    case  R.id.smart:
                        viewPager.setCurrentItem(2);
                        isEnabledSlideingMenu(true);
                        break;
                    case  R.id.grov:
                        viewPager.setCurrentItem(3);
                        isEnabledSlideingMenu(true);
                        break;
                    case R.id.setting:
                        viewPager.setCurrentItem(4);
                        isEnabledSlideingMenu(false);
                        break;
                }
            }
        });
         list=new ArrayList<>();
        list.add(new HomePager(activity));
        list.add(new NewsPager(activity));
        list.add(new servicePager(activity));
        list.add(new groPager(activity));
        list.add(new SettingPager(activity));
        ContentPagerAdapter adapter=new ContentPagerAdapter(list);
        viewPager.setAdapter(adapter);
         /* 设置首页被选中，拿到RadioGroup对象.check（要被选中的id）*/
        radioGroup.check(R.id.home);
        list.get(0).initData();// 同时在首页默认加载第0页的数据
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                  list.get(position).initData(); // 加载那个界面的数据
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter.notifyDataSetChanged();
    }
/* 是否启用侧滑菜单*/
    public void isEnabledSlideingMenu(boolean flag ){
           /* 把上下文实例转换成MainActivity对象*/
        MainActivity mainActivity=(MainActivity)activity;
        // MainActivity获取侧滑的控制器对象
        SlidingMenu fslidingMenu=mainActivity.getSlidingMenu();
        if (flag){  //  为ture是可滑出
                fslidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {//不可用
            fslidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }
    /* 获取新闻中心页面的实例对象*/

    public NewsPager getNewsCenterPagerInstance(){
         NewsPager newsPager= (NewsPager) list.get(1);
        return  newsPager;
    }
}
