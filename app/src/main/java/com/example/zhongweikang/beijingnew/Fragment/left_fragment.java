package com.example.zhongweikang.beijingnew.Fragment;


import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhongweikang.beijingnew.Bean.New_Content_Bean;
import com.example.zhongweikang.beijingnew.MainActivity;
import com.example.zhongweikang.beijingnew.Pager.NewsPager;
import com.example.zhongweikang.beijingnew.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import java.util.List;

/**
 * Created by zhongweikang on 2017/2/16.
 */

public class left_fragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private List<New_Content_Bean.DataBean> mlistData;
    private ListView mListView;
    private left_adapter adapter;
    private int CurrentSelectItem;

    @Override
    public View intitView() {
        mListView = new ListView(activity);
        mListView.setBackgroundColor(Color.BLUE);
        mListView.setPadding(30, 40, 0, 0);
        mListView.setSelected(true);

        return mListView;
    }

    /* 这个方法是设置左侧菜单的数据，用于接收数据的*/
    public void setDataList(List<New_Content_Bean.DataBean> dataList) {

        this.mlistData = dataList;
        // 初始化默认选中菜单对应的页面
        switchMenuDetailPager(0);
        mListView.setOnItemClickListener(this);
        adapter = new left_adapter(mlistData);
        mListView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("1", "你点击了第一个时间" + position);

        CurrentSelectItem = position;
        adapter.notifyDataSetChanged();
        // 把菜单缩回去
        MainActivity mdapterActivity =(MainActivity) activity;
        SlidingMenu slidingMenu=mdapterActivity.getSlidingMenu();
        slidingMenu.toggle(); // 控制菜单隐藏和显示的开关，自动切换成相反的状态的方法
        //把相应的界面显示出来
        switchMenuDetailPager(position);
    }

    /* 根据索引来切换菜单对应的详情界面*/
    private void switchMenuDetailPager(int position){
        MainActivity mdapterActivity =(MainActivity) activity;
        Main_Fragment mainfragment=mdapterActivity.getMain_Fragment();
        NewsPager newpager=mainfragment.getNewsCenterPagerInstance();
        newpager.switchCurrentPager(position);
    }

    /* 左侧的adapter适配器*/
    class left_adapter extends BaseAdapter {

        private List<New_Content_Bean.DataBean> d;

        public left_adapter(List<New_Content_Bean.DataBean> dataList) {
            this.d = dataList;
        }

        @Override
        public int getCount() {
            return d.size();
        }

        @Override
        public Object getItem(int position) {
            return d.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(activity, R.layout.left_list_item, null);
            New_Content_Bean.DataBean a = d.get(position);
            TextView c = (TextView) view.findViewById(R.id.te);
            c.setText(a.getTitle());
             Log.d("CurrentSelectItem",CurrentSelectItem+"");
            /*点击哪项和当前的相同就设置设置为选中状态*/
            if (CurrentSelectItem == position) {
                c.setEnabled(true);
            } else {
                c.setEnabled(false);
            }
            return view;

        }
    }

}
