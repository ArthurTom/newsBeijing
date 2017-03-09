package com.example.zhongweikang.beijingnew.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.renderscript.Sampler;

/**
 * 缓存工具类
 */

public class CacheUtils {


    public  static  SharedPreferences sharedPreferences;

    public static boolean getIsOpenFirst(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("led", Context.MODE_PRIVATE);
               return true;
        }

        sharedPreferences.getString("led",null);
        return false;
    }


    public static void putIsOpenFirst(Context context) {
       if (sharedPreferences==null){
           sharedPreferences=context.getSharedPreferences("led",Context.MODE_PRIVATE);
       }
      SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("led",false);
        editor.commit();
    }

    /**
    * 向SharedPreferences中存储一个字符串
     * */
    public static  void putString(Context context,String key,String Value){
           if (sharedPreferences==null){
               sharedPreferences=context.getSharedPreferences("Cache",Context.MODE_PRIVATE);
           }
           SharedPreferences.Editor editor=sharedPreferences.edit();
           editor.putString(key,Value);
           editor.commit();
    }

    public static String getString(Context context,String key,String defvalue){
        if (sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences("Cache",Context.MODE_PRIVATE);
        }
        String a=sharedPreferences.getString(key,defvalue);
        return  a;
    }

}
