package com.example.zhongweikang.beijingnew.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 網絡緩存的工具類
 */

public class NetCacheUtils {

    private Handler handler;
    public static final int SUCCESS = 0;
    public static final int FAILE = 1;
    public ExecutorService mExecutors; // 开启一个线程池，共10个线程
    private  LocalCacehUtils localCacehUtils;  // 本地缓存对象
    private MemoryCacheUtils memoryCacheUtils;  // 内存缓存对象

    public NetCacheUtils(Handler handler,LocalCacehUtils localCacehUtils,MemoryCacheUtils memoryCacheUtils) {          // 構造方法， 同時拿到handler
        this.handler = handler;
        this.localCacehUtils=localCacehUtils;
        this.memoryCacheUtils=memoryCacheUtils;
        mExecutors= (ExecutorService) Executors.newFixedThreadPool(10); // 开启一个线程池，共10个线程
    }


    /*
    * 使用字線程去請求網絡，吧圖片抓取出來，然後返還給主線程
    * */
    public void getBitmapFromNet(String imageUrl, int position) {

     //   new Thread(new InternetRunnable(imageUrl, position)).start(); // 開啟一個字線程
      mExecutors.execute(new InternetRunnable(imageUrl, position));   // 让线程池执行任务

    }

    class InternetRunnable implements Runnable {  // 一个内部类实现Runnable接口方法
        private int position;
        private String imageUrl;

        public InternetRunnable(String imageUrl, int position) {
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {
            HttpURLConnection connection=null;
            try {
                /* 网络请求的方法*/
                URL url = new URL(imageUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    Log.d("successful", "請求成功");
                    InputStream in = connection.getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(in);

                    // 發送到主線程中顯示
                    Message message = handler.obtainMessage();
                    message.obj = bm;
                    message.what = SUCCESS;
                    message.arg1 = position;

                    handler.sendMessage(message);
                    // 向內存中存
                    memoryCacheUtils.putBitmapMemory(imageUrl,bm);

                    // 向本地存一份
                    localCacehUtils.putBitmapLocal(imageUrl,bm);

                }
            } catch (Exception e) {
                e.printStackTrace();
                /* 如果有异常时候，然后发送*/
                Message msg=handler.obtainMessage();
                msg.arg1=position;
                msg.what=FAILE;
                handler.sendMessage(msg);
            }finally {
                /* 已经连接后，关闭连接*/
                if (connection!=null){
                    connection.disconnect(); // 斷開連接
                }
            }

        }
    }
}

