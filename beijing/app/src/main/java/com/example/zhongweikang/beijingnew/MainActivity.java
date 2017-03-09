package com.example.zhongweikang.beijingnew;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.zhongweikang.beijingnew.Fragment.Main_Fragment;
import com.example.zhongweikang.beijingnew.Fragment.left_fragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * slidingmenu工程引入的方法：1： 将里面的lib导入到file -  import modul中，将libs包中的classpath 'com.android.tools.build:gradle:0.12.+'
 * 同时改成 compileSdkVersion 19，buildToolsVersion "19.1.0"
 * 2. extends SlidingFragmentActivity
 * 3,将报错的地方更改为public
 * 4.
 */

public class MainActivity extends SlidingFragmentActivity {
    private final String Left_fragmenbt_context = "Left_fragmenbt_context";
    private final String Main_Fragment_context = "Main_Fragment_context";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_main);// 配置左侧菜单
        SlidingMenu menu = getSlidingMenu();  // 得到slidingMenu对象
        menu.setMode(SlidingMenu.LEFT);// 设置其模式为左侧侧滑出汗
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏可以抽出
        menu.setBehindOffset(400);// 主屏幕在界面留下的宽度
        replace();
    }

    /* 将fragmentlayout替换成Fragment*/
    public void replace() {
        FragmentManager fragManger = getSupportFragmentManager();
        FragmentTransaction bt = fragManger.beginTransaction();
           /* 替换的布局文件为fragmennt,同时为这个fragment设置id标签*/
        bt.replace(R.id.left_main, new left_fragment(), Left_fragmenbt_context);
        bt.replace(R.id.activity_main, new Main_Fragment(), Main_Fragment_context);
        bt.commit();
    }

    /* 获取左侧菜单的Fragment的对象的实例*/
    public left_fragment getleftFragment() {
        FragmentManager fm = getSupportFragmentManager();
        left_fragment left_fragment = (com.example.zhongweikang.beijingnew.Fragment.left_fragment) fm.findFragmentByTag(Left_fragmenbt_context);

        return left_fragment;
    }

    /*获取正文中的fragment的对象的实例*/
    public Main_Fragment getMain_Fragment() {
        FragmentManager fm = getSupportFragmentManager();
        Main_Fragment main_fragment = (Main_Fragment) fm.findFragmentByTag(Main_Fragment_context);
        return main_fragment;
    }
}
