package com.example.zhongweikang.beijingnew.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 *   内存中存储缓存，安卓中LruCache缓存集合，把常用的数据保存起来，不常用的回收
 */

public class MemoryCacheUtils {

      private LruCache<String ,Bitmap> mMempryCache;

    public MemoryCacheUtils(){
          /* 获取手机的虚拟内存*/
        int phoneMemory= (int) Runtime.getRuntime().maxMemory();
        int maxsize=phoneMemory/8; // 8分之一的内存用于缓存图片
         mMempryCache=new LruCache<String ,Bitmap>(maxsize){
            /*
            * 把图片的大小返回
            * */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

    }


    public void putBitmapMemory(String imageUrl,Bitmap bm){

        mMempryCache.put(imageUrl,bm);        // 将图片加入到缓存中
    }

/*
* 从内存中取一张图片
* */
    public Bitmap getBitmapMemory(String imageUrl){
           Bitmap bm=mMempryCache.get(imageUrl);

         return bm;
    }
}
