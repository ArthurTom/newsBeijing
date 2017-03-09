package com.example.zhongweikang.beijingnew.Menu_mainContent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhongweikang.beijingnew.Bean.photoBean;
import com.example.zhongweikang.beijingnew.R;
import com.example.zhongweikang.beijingnew.utils.BitmapCacheUtils;
import com.example.zhongweikang.beijingnew.utils.CacheUtils;
import com.example.zhongweikang.beijingnew.utils.Constant_Utils;
import com.example.zhongweikang.beijingnew.utils.NetCacheUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by zhongweikang on 2017/2/21.
 */

public class photoMenuPager extends MenuAllBasePager {
    @ViewInject(R.id.photo_listview)
    private ListView photo_listView;
    @ViewInject(R.id.photo_gridView)
    private GridView photo_gridView;

    private List<photoBean.DataBean.NewsBean> list;
    private photoAdapter p_adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SUCCESS:   // 網絡緩存成功
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;

                    ImageView imageView = (ImageView) photo_listView.findViewWithTag(position);

                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                    break;
                case NetCacheUtils.FAILE:
                    Toast.makeText(MenuActivity, "第"+msg.arg1+"請求失敗", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };
    private BitmapCacheUtils bitmapCacheUtils;


    public photoMenuPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(MenuActivity, R.layout.photos, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        String getResult = CacheUtils.getString(MenuActivity, "photo", null);
        if (!TextUtils.isEmpty(getResult)) {
            analysis(getResult);
        }
        // 从网络中获取数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        String url = Constant_Utils.serverUrl + Constant_Utils.photoUrl;
        Log.d("tag-1", url);
        RequestParams request = new RequestParams(url);
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("result", result);
                CacheUtils.putString(MenuActivity, "photo", result);
                analysis(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("faile", "faile");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void analysis(String result) {
        photoBean a = pareseJSON(result);
        list = a.getData().getNews();
        p_adapter = new photoAdapter();
        photo_listView.setAdapter(p_adapter);


    }

    public photoBean pareseJSON(String result) {
        Gson gson = new Gson();
        photoBean bean = gson.fromJson(result, photoBean.class);
        return bean;
    }


    class photoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new mViewHolder();
                convertView = LayoutInflater.from(MenuActivity).inflate(R.layout.photo_list_item, null);
                viewHolder.cardView_imageView = (ImageView) convertView.findViewById(R.id.cardView_imageView);
                viewHolder.cardView_textView = (TextView) convertView.findViewById(R.id.cardView_TextView);
                convertView.setTag(viewHolder);
            }
            viewHolder = (mViewHolder) convertView.getTag();
            viewHolder.cardView_imageView.setTag(position);
            viewHolder.cardView_textView.setText(list.get(position).getTitle());
            ImageOptions imageOptions = new ImageOptions.Builder()
                    /*.setImageScaleType(ImageView.ScaleType.CENTER)
                    .setFailureDrawableId(R.drawable.back)*/
                    .build();
            String phUrl = list.get(position).getListimage().toString().substring(15);
            String pb = "http://172.17.1.35";
            Log.d("5555", pb + phUrl);
            // 這個是根據網絡請求額加載的圖片
            // x.image().bind(viewHolder.cardView_imageView, pb + phUrl, imageOptions);
            //  使用三級緩存機制進行網絡請求的方法
            bitmapCacheUtils = new BitmapCacheUtils(handler);
            Bitmap bitmap = bitmapCacheUtils.getBitmapFromUrl(pb + phUrl, position);
            if (bitmap != null) {
                // 當前圖片不為空的時候，是從內存和本地種獲取的
                viewHolder.cardView_imageView.setImageBitmap(bitmap);
            }
            return convertView;
        }
    }

    class mViewHolder {
        ImageView cardView_imageView;
        TextView cardView_textView;
    }

    private boolean isSingleColums = true; //  是否是单列的

    public void switchListOrGrid(ImageButton v) {
        if (isSingleColums) {
            // 当前是单列，切换成两列
            isSingleColums = false;
            photo_listView.setVisibility(View.GONE);
            photo_gridView.setVisibility(View.VISIBLE);
            photo_gridView.setAdapter(p_adapter);
            v.setImageResource(R.drawable.icon_pic_list_type);
            // 切换图片

        } else {
            // 当前是两列，切换成单列
            isSingleColums = true;
            photo_gridView.setVisibility(View.GONE);
            photo_listView.setVisibility(View.VISIBLE);
            photo_listView.setAdapter(p_adapter);
            // 切换图片
            v.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }
}
