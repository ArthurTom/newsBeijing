package com.example.zhongweikang.beijingnew.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * 圖片緩存类放法
 */

public class BitmapCacheUtils {


    /*1 根據地址去內存中取圖片,
    * 2  根據URL 去本地種取,
    * 3.最後去網絡中取,取到后發送給主縣城中顯示,同時向內存中存一份,本地存一份
    *
    * */
    private NetCacheUtils mNetCacheUtils; // 网络请求对象
    private LocalCacehUtils mLocalCacheUtils;  // 本地缓存对象
   private  MemoryCacheUtils memoryCacheUtils;  // 内存缓存对象

    public BitmapCacheUtils(Handler handler) {    // 夠造方法，初始換網絡請求的同時，傳給它handler
        memoryCacheUtils=new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacehUtils();  // 实例化本地缓存对象
        mNetCacheUtils = new NetCacheUtils(handler, mLocalCacheUtils,memoryCacheUtils);   // 实例化网络缓存对象
    }

    public Bitmap getBitmapFromUrl(String url, int position) {     // 得到圖片的地址
        /* 先根据URl,去内存中取,如果取到直接返回*/
        Bitmap bm=memoryCacheUtils.getBitmapMemory(url);
        if (bm!=null){
            Log.d("memory","从内存总获取");
            return bm;

        }
        /* 首先得到本地缓存中的缓存*/
        bm = mLocalCacheUtils.getBitmaoFromLocal(url);
        if (bm != null) {
            Log.d("loacal", "从本地取得的照片");
            return bm;  // 一return 下面的网络请求得到的图片不会再质走了

        }
       /*如果获取本地缓存中的方法得不到缓存，在进行网络得到缓存*/
        mNetCacheUtils.getBitmapFromNet(url, position);    // 這個方法中調用網絡請求中的方法，同時將圖片地址傳給它

        return null;

    }
}
