package com.example.zhongweikang.beijingnew.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *  本地缓存
 */

public class LocalCacehUtils {

    private final  String cache_dir="/it";

    /*
    * 存图片
    * */
    public void putBitmapLocal(String imageURL, Bitmap bm){

        try {
            String fileName=MD5Encoder.encode(imageURL);
            File file=new File(cache_dir,fileName);
            File paretnFile=file.getParentFile();
            if (!paretnFile.exists()){
                file.getParentFile().mkdirs();
            }
            FileOutputStream f=new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG,0,f);  // 将图片压缩后，放到

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 本地缓存，获取本地缓存内容的方法，返回Bitmap对象图片
    * */
    public Bitmap getBitmaoFromLocal(String imageUrl){
        try {
            String fileName=MD5Encoder.encode(imageUrl); // 将图片的地址转化为特定文件的文件名称
            File file=new File(cache_dir,fileName);  //   创新文件，包括地址和文件名称
            if (file.exists()){    // 如果文件是存在的
                // 文件存在，有缓存，读取出来，并返 回
                FileInputStream is=new FileInputStream(file);   // 使用文件流读出内容
                Bitmap bm=BitmapFactory.decodeStream(is);   // 将读出的文件流转化为Bitmap图片
                return bm;   // 当本地文件存在的时候，返回图片，没有存在时候，
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // 没有存在的时候，返回空
    }
}
