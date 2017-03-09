package com.example.zhongweikang.beijingnew.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhongweikang.beijingnew.R;

import org.xutils.view.annotation.ViewInject;

/**
 * BaseFragmeny 是一个基类，提供给其他的Fragment使用
 */

public  abstract class BaseFragment extends Fragment {

    public Activity activity;
    /*这个方法是将fragment绑定到activity中*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         activity=getActivity();//上个方法中已经绑定了activity，所以可以使用这个方法得到
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

            View v=intitView();
            return  v;

    }




    /* 在这个方法中，activity已经初始化了，当前可以初始化Fragment的数据了*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public abstract View intitView();

    public void initData() {
    }

}
