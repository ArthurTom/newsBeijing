package com.example.zhongweikang.beijingnew.newsDetailPage;

import android.app.Activity;
import android.content.Intent;
import android.drm.ProcessedData;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhongweikang.beijingnew.Bean.New_Content_Bean;
import com.example.zhongweikang.beijingnew.Bean.tab_newBean;
import com.example.zhongweikang.beijingnew.MainActivity;
import com.example.zhongweikang.beijingnew.Menu_mainContent.MenuAllBasePager;
import com.example.zhongweikang.beijingnew.R;
import com.example.zhongweikang.beijingnew.User_define.HscrollViewPager;
import com.example.zhongweikang.beijingnew.User_define.myRefreshListView;
import com.example.zhongweikang.beijingnew.utils.CacheUtils;
import com.example.zhongweikang.beijingnew.utils.Constant_Utils;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.id.list;
import static android.R.id.switchInputMethod;

/**
 * Created by zhongweikang on 2017/2/22.
 */

public class new_detail_base extends MenuAllBasePager {

    private New_Content_Bean.DataBean.ChildrenBean childrenBean;
    private String tab_beijing_url;
    @ViewInject(R.id.tab_beijing_viewpage)
    private HscrollViewPager tab_beijing_viewpage;
    @ViewInject(R.id.image_describ)
    private TextView image_describ;
    @ViewInject(R.id.tab_listview)
    private myRefreshListView listView;
    @ViewInject(R.id.tab_point)
    private LinearLayout tab_point_linearLayout;

    private tab_beijing_viewpageAdaper adaper;
    private List<tab_newBean.DataBean.TopnewsBean> topnews;
    private List<tab_newBean.DataBean.NewsBean> listData;
    private int PrePoint = 0;
    private boolean isEnableSlidingMenu = false; // 是否启动菜单

    private boolean isPullDownRefresh = false;   // 是否正在下拉刷新中

    private String moreUrl;

    private boolean isLoadingMore = false;  // 是否加载更多中
    private myListViewAdapter listAdapter;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int CurrentItem=(tab_beijing_viewpage.getCurrentItem()+1)%topnews.size();
            tab_beijing_viewpage.setCurrentItem(CurrentItem);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  handler.sendEmptyMessage(0) ;
                }
            },4000);
        }
    };

    public new_detail_base(Activity activity, New_Content_Bean.DataBean.ChildrenBean childrenBean, boolean isEnableSlidingMenu) {
        super(activity);
        this.childrenBean = childrenBean;
        this.isEnableSlidingMenu = isEnableSlidingMenu;
    }


    @Override
    public View initView() {
        // 将布局加载进来，将整体拆成了两个部分
        View view = View.inflate(MenuActivity, R.layout.tab_beijing, null);// 这个里面只有listview
        x.view().inject(this, view);
        View tab_beijing_top = View.inflate(MenuActivity, R.layout.tab_beijing_top, null); // 这里是listview将要加入的头部
        x.view().inject(this, tab_beijing_top);       // 注入，是为了查找头部中的id
        //  listView.addHeaderView(tab_beijing_top);     // 为listveiw加入头部，这个头部是个viewpager
        listView.addSecondHeaderView(tab_beijing_top);

      /*-----------------------------------------------------------------------------------------------------------*/
        /* 实现 这个抽象的方法*/
        listView.SetOnRefreshListener(new myRefreshListView.OnRefershListener() {
            @Override
            public void onPullDownRefresh() {
                isPullDownRefresh = true;
                getDataFormNet();
            }

            @Override
            public void onLoadingMore() {
                if (TextUtils.isEmpty(moreUrl)) {
                    Log.d("tag","加载中拿到的为空？"+moreUrl);
                    Toast.makeText(MenuActivity, "已经没有更多的数据了", Toast.LENGTH_SHORT).show();
                    MenuActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.onRefreshFinish(false);
                            adaper.notifyDataSetChanged();
                        }
                    });

                } else {
                    // 加载更多的数据
                    Log.d("qqqqqq", moreUrl);
                    getMoreDataFromNet();
                }
            }
        });

        /*-----------------------------添加点击事件--------------------------------------------------*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* position 应该－1,才是在listData中的真实数据*/
                int realposition=position-1;
                tab_newBean.DataBean.NewsBean newsItem=listData.get(realposition);
                Log.d("tittle",newsItem.getTitle()+"id"+newsItem.getId());
                String readid= String.valueOf(newsItem.getId());


                String storeData=CacheUtils.getString(MenuActivity,"Allread","");

                if (!storeData.contains(readid)){  // 如果不包含，才存储起来
                    String readed=null;
                    if (TextUtils.isEmpty(storeData)){
                        readed=readid+",";
                    }else {
                        readed=storeData+readid+",";
                    }
                    CacheUtils.putString(MenuActivity,"Allread",readed);
                }
                listAdapter.notifyDataSetChanged();
                Intent intent=new Intent(MenuActivity,News.class);
                intent.putExtra("url",newsItem.getUrl());
                MenuActivity.startActivity(intent);
            }

        });


        return view;    // 返回带listveiew 这个布局，
    }


    private void getMoreDataFromNet() {
        OkHttpClient client = new OkHttpClient();
        Request re = new Request.Builder().url(moreUrl).build();
        client.newCall(re).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure", "加载数据失败");
                MenuActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshFinish(false);
                        adaper.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("response", "返回成功");

                MenuActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshFinish(false);
                        adaper.notifyDataSetChanged();
                    }
                });

                String data1 = response.body().string();
                Log.d("data1", data1);
                isLoadingMore = true;
                processData(data1);  // 解析数据
            }
        });

    }

    @Override
    public void initData() {
        Log.d("地址", childrenBean.getUrl());
        tab_beijing_url = Constant_Utils.serverUrl + childrenBean.getUrl();

        Log.d("tab_beijing_url", tab_beijing_url);
        /*先拿到缓存中的额数据*/
        String jsonData = CacheUtils.getString(MenuActivity, "tab_new", null);
        // 如果不为空，利用缓存中的额数据 进行解析，然后在进行网络解析，如果为空的话直接网路请求数据
        if (!TextUtils.isEmpty(jsonData)) {
            processData(jsonData);
        }
        getDataFormNet();


    }


    /*使用Okhttp请求网络数据*/
    public void getDataFormNet() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(tab_beijing_url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("请求失败", e + "");
                if (isPullDownRefresh) {
                    MenuActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            isPullDownRefresh = false;
                            listView.onRefreshFinish(true);
                            adaper.notifyDataSetChanged();
                        }
                    });

                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // .toString使用两次会报异常
                //  Log.d("得到的数据",response.body().string());
                if (isPullDownRefresh) {

                    MenuActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isPullDownRefresh = false;
                            listView.onRefreshFinish(true);

                            adaper.notifyDataSetChanged();
                        }
                    });

                }

                String data = response.body().string();     // String 型的JSON数据


                // 将数据缓存起来
                CacheUtils.putString(MenuActivity, "tab_new", data);
              /* 处理网络请求的数据*/
                processData(data);
            }
        });

    }

    // 解析JSON 数据，并封装返回一个对象
    public tab_newBean ParseJson(String data) {
        Gson gsondata = new Gson();
        tab_newBean gosnBean = gsondata.fromJson(data, tab_newBean.class);

        moreUrl = gosnBean.getData().getMore();  // 加载更多数据的地址


        if (TextUtils.isEmpty(moreUrl)) {  // more有为空的情况，就说明没有新数据了
            moreUrl = null;              // 设为空
            Log.d("tag","moreUrl是为空的");
        } else {
            moreUrl = Constant_Utils.serverUrl + moreUrl;   // 得到跟多数据的地址
            Log.d("tag","moreUrl不为空"+moreUrl);
        }

        return gosnBean;
    }

    // 次方法为拿到String型的JSON数据，在此方法中调用ParseJson数据解析数据
    public void processData(final String data) {

        //解析的方法
        final tab_newBean bean = ParseJson(data);  //   调用Gson解析数据
        Log.d("tittle", bean.getData().getNews().get(0).getTitle());

        if (!isLoadingMore) {
            // 将顶部中的新闻中的viewPager显示出来
            topnews = bean.getData().getTopnews();
            Log.d("topnews", topnews.get(0).getTitle());
/* 由于 processData是在okhttp的异步请求中进行网络请求和解析，所以如果要进行UI界面的跟新需要使用下面的方法，这是个知识点，易错的*/
            MenuActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adaper = new tab_beijing_viewpageAdaper();
                    tab_beijing_viewpage.setAdapter(adaper);

/*--------------------------------------------------------------------------------------------------------*/
             /* 下面的是为加点的切换效果*/
                    tab_point_linearLayout.removeAllViews();  // 由于processData会被调用两次，所以先清空下
                    ImageView imageView;
                    LinearLayout.LayoutParams params;
                    for (int i = 0; i < topnews.size(); i++) {
                        imageView = new ImageView(MenuActivity);
                        params = new LinearLayout.LayoutParams(20, 20);
                        if (i != 0) {
                            params.leftMargin = 10;
                        }
                        imageView.setLayoutParams(params);
                        imageView.setBackgroundResource(R.drawable.viepage_small_point);
                        imageView.setEnabled(false);
                        tab_point_linearLayout.addView(imageView);
                        Log.d("5555", 5555 + "");
                    }
                    tab_point_linearLayout.getChildAt(PrePoint).setEnabled(true);
/*----------------------------------------设置viewpager的监听--------------------------------------------------------*/
                    tab_beijing_viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            image_describ.setText(topnews.get(position).getTitle());
                            tab_point_linearLayout.getChildAt(PrePoint).setEnabled(false);
                            tab_point_linearLayout.getChildAt(position).setEnabled(true);
                            PrePoint = position;

                           /* if (isEnableSlidingMenu) {  // 判定是否可以将左侧菜单抽出的方法
                                if (position == 0) {
                                    // 把菜单置为可用
                                    MainActivity mainActivity = (MainActivity) MenuActivity;
                                    SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
                                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

                                } else {
                                    // 把菜单屏蔽
                                    MainActivity mainActivity = (MainActivity) MenuActivity;
                                    SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
                                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                                }
                            }*/
                        }

                        @Override
                        public void onPageSelected(int position) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });


                    listData = bean.getData().getNews();
                    listAdapter = new myListViewAdapter();

                    adaper.notifyDataSetChanged();
                    listAdapter.notifyDataSetChanged();
                    MenuActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(listAdapter);


                        }
                    });


                }
            });
        } else {
            //把列表新闻先取出来，在加到以前的集合中，在刷新数据
            isLoadingMore = false;
            Log.d("111", "1111111111111111111111111111111111111111111111");
            List<tab_newBean.DataBean.NewsBean> tem = bean.getData().getNews();
            listData.addAll(tem);
            Log.d("leng", listData.size() + "");
            MenuActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listAdapter.notifyDataSetChanged();
                    adaper.notifyDataSetChanged();
                }
            });

        }
/* 开始轮播循环*/
         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                 handler.sendEmptyMessage(0);
             }
         },4000);
    }

    class tab_beijing_viewpageAdaper extends PagerAdapter {
        public tab_beijing_viewpageAdaper() {
        }

        @Override
        public int getCount() {
            return topnews.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(MenuActivity);
            imageView.setImageResource(R.drawable.home_scroll_default); // 在没加载完图片前设置的图片
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            /* 点击轮换中的一个界面，轮转停止，和播放*/
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            /* 移除消息队列中的所有消息和任务*/
                            handler.removeCallbacksAndMessages(null);
                            Log.d("aa","停止播放了");
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            // 动作丢失的操作时候，也可以轮转
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    /*重新打开轮换，直接发送个消息就可以*/
                                    handler.sendEmptyMessage(0);
                                }
                            },4000);
                            break;
                        case  MotionEvent.ACTION_UP:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    /*重新打开轮换，直接发送个消息就可以*/
                                    handler.sendEmptyMessage(0);
                                }
                            },4000);
                            break;
                    }
                    return true;
                }
            });

            /* 使用xutils加载网络上的图片*/
            tab_newBean.DataBean.TopnewsBean imag = topnews.get(position);

            ImageOptions imageOptions = new ImageOptions.Builder()
                    //  .setImageScaleType(ImageView.ScaleType.CENTER)
                    .build();
            Log.d("1111", imag.getTopimage());
            String a = imag.getTopimage().toString().substring(15);
            Log.d("2222", a);
            String b = "http://172.17.1.35";
            String address = b + a;
            x.image().bind(imageView, address, imageOptions);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    class myListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(MenuActivity, R.layout.list_item_beijing, null);
                viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.item_image);
                viewHolder.item_text_tittle = (TextView) convertView.findViewById(R.id.item_tex1);
                viewHolder.item_text_time = (TextView) convertView.findViewById(R.id.item_time);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.item_text_tittle.setText(listData.get(position).getTitle());
            viewHolder.item_text_time.setText(listData.get(position).getPubdate());
       /* 判定当前是否是已读的新闻*/
            String readable=CacheUtils.getString(MenuActivity,"Allread",null);
           if (!TextUtils.isEmpty(readable)&&readable.contains(listData.get(position).getId()+"")){
               viewHolder.item_text_tittle.setTextColor(Color.GRAY);
           }else {
               viewHolder.item_text_tittle.setTextColor(Color.BLACK);
           }
            ImageOptions Options = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.FIT_XY).build();
            String list_Picture_url = listData.get(position).getUrl().toString();
            String aa = listData.get(position).getListimage().toString().substring(15);
            String bb = "http://172.17.1.35";
            String picture_url = bb + aa;
            Log.d("picture_url", picture_url);

            Log.d("list_Picture_url", list_Picture_url);

            x.image().bind(viewHolder.itemImage, picture_url, Options);


            return convertView;
        }

        class ViewHolder {
            ImageView itemImage;
            TextView item_text_tittle, item_text_time;


        }
    }


    }

