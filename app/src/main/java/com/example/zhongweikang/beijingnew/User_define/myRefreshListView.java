package com.example.zhongweikang.beijingnew.User_define;

import android.content.ContentUris;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhongweikang.beijingnew.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.paddingTop;

/**
 * Created by zhongweikang on 2017/2/24.
 */

public class myRefreshListView extends ListView {

    private int DownX, DownY = -1;
    private LinearLayout Headerview;
    private int headTop;
    private View pullDownHeadView;
    private View secondHeaderView;
    private int ListViewOnScreenY = -1;

    private final int refersh = 0; // 下拉刷新
    private final int release_refesh = 1;  // 释放刷新
    private final int refershing = 2; // 正在刷新
    private int currentState = 0;  // 当前刷新状态

    private TextView time;
    private TextView tittle;
    private ImageView image1;
    private ProgressBar progressBar;

    private RotateAnimation UpAnmia,downAnmia;
    private OnRefershListener mOnRefreshListener;  // 回调的事件的对象
    private  View footView;
    private int footerViewHeight;
    private boolean isLoadingMore=false;    //
    public myRefreshListView(Context context) {
        super(context);
        addRfeshListViewHead();
        initFooterView();
    }

    public myRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addRfeshListViewHead();
        initFooterView();
    }

    public myRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addRfeshListViewHead();
        initFooterView();
    }

    /* 添加头布局*/
    public void addRfeshListViewHead() {
        Headerview = (LinearLayout) inflate(getContext(), R.layout.add_refreshlist_head, null);
        pullDownHeadView = Headerview.findViewById(R.id.head_lin);  // 头部对象
        time = (TextView) Headerview.findViewById(R.id.head_time);
        time.setText("最后的刷新时间"+getCurrentTime());
        tittle = (TextView) Headerview.findViewById(R.id.head_tittle);
        image1 = (ImageView) Headerview.findViewById(R.id.head_fr);
        progressBar = (ProgressBar) Headerview.findViewById(R.id.head_pro);
        /* 侧量头部的长度*/
        pullDownHeadView.measure(0, 0);
        headTop = pullDownHeadView.getMeasuredHeight();  // 侧量头部的高度
        pullDownHeadView.setPadding(0, -headTop, 0, 0);     // 隐藏头布局

        myRefreshListView.this.addHeaderView(Headerview);
        initAnimation(); // 初始化头布局的动画
    }

    /* 设置动画的方法*/
    private void initAnimation() {
        // 向上的动画
        UpAnmia=new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        UpAnmia.setFillAfter(true);
        UpAnmia.setDuration(1000);

         // 向下的动画
         downAnmia=new RotateAnimation(-180,-360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        UpAnmia.setFillAfter(true);
        UpAnmia.setDuration(1000);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                DownY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if (DownY == -1) {
                    DownY = (int) ev.getY();

                }
                int moveY = (int) ev.getY();
                int diffY = DownY - moveY;    // 向下滑动为负数
                // 判定当前是否正在刷新中，如果
                if (currentState==refershing){
                    // 当前正在刷新中，不执行下拉，直接跳出
                    break;
                }
                // 判断轮播图是否完全显示了
                boolean isDisplay = isDisplaySecondHeaderView();
                if (!isDisplay) {
                    // 没有完全显示，不执行下来刷新头操作，直接跳出Switch
                    break;
                    // 完全显示，则走下面的方法
                }
                if (diffY < 0) {
                    int paddingTop = -headTop - diffY;     // 距离上部的高度
                    if (paddingTop > 0 && currentState != release_refesh) {  // 完全显示并且当前的状态不是释放刷新的状态的时候，进入松开刷新状态
                        currentState = release_refesh; // 当先的状态改变成释放刷新
                        refreshHeadViewState();
                    } else if (paddingTop < 0 && currentState != refersh) {   // 没有完全实现，并且当前的状态是不在刷新的时候，不会进入松开刷新状态

                        currentState = refersh;  // 当前的状态改变为要刷新
                        refreshHeadViewState();
                    }

                    pullDownHeadView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                DownY = -1;
                if (currentState==refersh){  // 下拉刷新，，将头布局隐藏
                    pullDownHeadView.setPadding(0,-headTop,0,0);

                }
                else if (currentState==release_refesh){
                    // 进入到正在刷新的状态
                    currentState=refershing;
                    refreshHeadViewState();
                    pullDownHeadView.setPadding(0,0,0,0);
                    // 调用用户的回调事件，用于刷新数据
                    if (mOnRefreshListener!=null){
                        mOnRefreshListener.onPullDownRefresh();

                    }
                }

                break;
        }


        return super.onTouchEvent(ev);


    }

    /* 根据当前头布局的状态，改变头布局*/
    private void refreshHeadViewState() {
        switch (currentState) {
            case refersh:    // 下拉刷新
                image1.startAnimation(downAnmia);
                tittle.setText("下拉刷新");
                break;
            case release_refesh:  // 释放刷新
                image1.startAnimation(UpAnmia);
                tittle.setText("松开刷新");
                break;
            case refershing:  // 正在刷新中
                image1.clearAnimation();
                image1.setVisibility(GONE);
                progressBar.setVisibility(VISIBLE);
                tittle.setText("正在刷新中");
                break;

        }
    }

    /* 添加第二个头布局*/
    public void addSecondHeaderView(View SecondHeaderView) {
        this.secondHeaderView = SecondHeaderView;
        Headerview.addView(SecondHeaderView);    // 将传过来的额
    }

    /* 判断轮播图是否完全显示*/
    public boolean isDisplaySecondHeaderView() {
        // 获取ListView在屏幕中y轴的值

        int[] loactionXY = new int[2];  // 0 位 x值，1，为Y的值
        if (ListViewOnScreenY == -1) {
            this.getLocationOnScreen(loactionXY);
            ListViewOnScreenY = loactionXY[1];  // 取出Y值

        }

        // 获取轮播图在屏幕中的Y轴的值
        secondHeaderView.getLocationOnScreen(loactionXY);
        int mSecondHeaderViewOnScreen = loactionXY[1];
        if (ListViewOnScreenY <= mSecondHeaderViewOnScreen) {  // 完全显示的时候
            return true;
        } else {
            return false;
        }
    }
    /* 回调 ,当前的ListView刷新的回调接口*/
      public interface OnRefershListener{
          /*
          * 当下拉刷新时触发此方法
          * */
         void onPullDownRefresh();

        /*
        * 加载更多的方法*/
        void onLoadingMore();
    }

    public void SetOnRefreshListener(OnRefershListener listener){
               this.mOnRefreshListener=listener;
    }
    /* 刷新完数据后，将头布局隐藏掉或者将脚布局隐藏掉*/
   public void onRefreshFinish( boolean isSuccess){
       // 判定是加载更多或者下拉刷新

       if (isLoadingMore){    // 加载更多中为真，就隐藏掉
           footView.setPadding(0,-footerViewHeight,0,0);
           isLoadingMore=false;
       }else {  // 为假就是，就是设置头布局的设置
           pullDownHeadView.setPadding(0,-headTop,0,0);
           currentState=refersh;
           progressBar.setVisibility(GONE);
           image1.setVisibility(VISIBLE);
           tittle.setText("下拉刷新");

           if (isSuccess){
               time.setText("最后刷新时间"+getCurrentTime());
           }


       }

   }

    public String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.d("sdf",sdf.format(new Date()));
            return sdf.format(new Date());
    }

    /* 添加脚布局*/
    private void initFooterView(){
        footView=inflate(getContext(),R.layout.add_refreshlist_foot,null);
         footView.measure(0,0);
         footerViewHeight=footView.getMeasuredHeight();
        footView.setPadding(0,-footerViewHeight,0,0);
        this.addFooterView(footView);

        /*为这个自定义的listView设置滚动事件*/
        this.setOnScrollListener(new OnScrollListener() {
            /* 滚动时间改变的时候的，第一个参数是listView这个事件，scrollState是状态*/
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滚动停止时，或者惯性滑动时，ListView最后一个显示的太欧姆索引为getCout-1时
                if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
                    if (getLastVisiblePosition()==getCount()-1&& !isLoadingMore){  // 滑动到最后Item,只有没有加载的时候才可以触发继续加载
                        Log.d("tag","滚动到底部了");

                        isLoadingMore=true;  // 标志，为true时，在进就进不来了
                        footView.setPadding(0,0,0,0);  // 显示脚步
                        setSelection(getCount());
                        /* 调用使用者的回调事件*/
                        if (mOnRefreshListener!=null){
                            mOnRefreshListener.onLoadingMore();

                        }
                    }
                }



            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
}
