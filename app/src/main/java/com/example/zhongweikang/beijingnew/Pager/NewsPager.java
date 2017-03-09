package com.example.zhongweikang.beijingnew.Pager;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.zhongweikang.beijingnew.Bean.New_Content_Bean;
import com.example.zhongweikang.beijingnew.Fragment.left_fragment;
import com.example.zhongweikang.beijingnew.MainActivity;
import com.example.zhongweikang.beijingnew.Menu_mainContent.InterMenuPage;
import com.example.zhongweikang.beijingnew.Menu_mainContent.MenuAllBasePager;
import com.example.zhongweikang.beijingnew.Menu_mainContent.NewMenuDetalPager;
import com.example.zhongweikang.beijingnew.Menu_mainContent.TopicMenuPager;
import com.example.zhongweikang.beijingnew.Menu_mainContent.photoMenuPager;
import com.example.zhongweikang.beijingnew.utils.CacheUtils;
import com.example.zhongweikang.beijingnew.utils.Constant_Utils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongweikang on 2017/2/17.
 */

public class NewsPager extends BasePager {
    List<MenuAllBasePager> MenuPagerList;
    List<New_Content_Bean.DataBean> data;

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("新闻");
        imagebutton.setVisibility(View.VISIBLE);
        // 在请求网络之前，将本地的缓存数据取出来
        String CacheData = CacheUtils.getString(mActivity, "newData", null);
        if (!TextUtils.isEmpty(CacheData)) {    // 本地缓存不为空
            processData(CacheData);  // 调用解析数据
        }

        getNetData();

    }

    /* 获取新闻网络数据的方法*/
    private void getNetData() {

        String url = Constant_Utils.serverUrl + Constant_Utils.newsUrl;
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("result", result);
                // 将拿到的网络数据缓存到本地
                CacheUtils.putString(mActivity, "newData", result);
                processData(result);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    /* 网络数据的解析*/
    protected void processData(String result) {

        Gson gson = new Gson();
        New_Content_Bean newBean = gson.fromJson(result, New_Content_Bean.class);
        Log.d("newBean", newBean.getData().get(0).getChildren().get(0).getTitle());
        //获得左侧菜单数据
        data = newBean.getData();
        //初始化左侧菜单对应的页面，共四个页面，新闻，专题，组图，互动
        MenuPagerList = new ArrayList<>();
        MenuPagerList.add(new NewMenuDetalPager(mActivity, data.get(0).getChildren()));   //  将数据在新建的时候传过去
        MenuPagerList.add(new InterMenuPage(mActivity));
        MenuPagerList.add(new photoMenuPager(mActivity));
        MenuPagerList.add(new TopicMenuPager(mActivity));


        // 把左侧菜单数据传递给左侧的fragment处理
        MainActivity Newactivity = (MainActivity) mActivity;  // 拿到activity
        left_fragment left = Newactivity.getleftFragment();  // 拿到fragment
        left.setDataList(data);


    }

    /* 根据位置设置界面*/
    public void switchCurrentPager(int position) {
        // 设置当前标题
        String tittle = data.get(position).getTitle();
        textView.setText(tittle);

        MenuAllBasePager menuAllBasePager = MenuPagerList.get(position); // 拿到的界面转化成父类
        fragmentLayout.removeAllViews(); // 再添加布局前移除所有的子布局
        fragmentLayout.addView(menuAllBasePager.rView);  // 将界面添加进去
        menuAllBasePager.initData(); // 添加数据
        if (position == 2) {
            // 当前的位置为组图的的时候，显示
            image_change.setVisibility(View.VISIBLE);
            image_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 得到photoMenuPager这个对象
                    photoMenuPager pager = (photoMenuPager) MenuPagerList.get(2);
                    pager.switchListOrGrid((ImageButton) v);
                }
            });

        } else {
            //当前的位置不为组图的时候，不显示
            image_change.setVisibility(View.GONE);
        }
    }

}
