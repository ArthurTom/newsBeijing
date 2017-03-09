package com.example.zhongweikang.beijingnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zhongweikang.beijingnew.Adapter.ViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LedActivity extends Activity {


    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.led_button)
    Button ledButton;
    @BindView(R.id.led_linearLayout)
    LinearLayout ledLinearLayout;
    @BindView(R.id.activity_led)
    RelativeLayout activityLed;
    @BindView(R.id.red_point)
    View redPoint;
    private ImageView imageView;
    List<ImageView> list;
    private int Pointp_JJ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_led);
        ButterKnife.bind(this);


        int[] imageID = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        list = new ArrayList<>();
        // 获得视图树观察者，观察当整个布局的layout
        redPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {    // 此方法只需要执行ICI就可以
                // 把当前的监听事件从视图树中移除
                redPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        for (int i = 0; i < imageID.length; i++) {
            imageView = new ImageView(this);

            imageView.setImageResource(imageID[i]);
            list.add(imageView);
            /* 线性布局中添加灰色的点*/
            View view = new View(this); // 代码中创建view
            /**/
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            if (i != 0) {
                params.leftMargin = 20;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.point);
            ledLinearLayout.addView(view);
        }


        ViewPageAdapter adapter = new ViewPageAdapter(LedActivity.this, list);
        viewpage.setAdapter(adapter);
        /* 为viewpage设置监听事件*/
        viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
             Log.d("onPageScrolled","position="+position+"positionOffset+"+positionOffset+"positionOffsetPixels"+positionOffsetPixels);
             // 点移动距离=间距*比值
                Pointp_JJ=ledLinearLayout.getChildAt(1).getLeft()-ledLinearLayout.getChildAt(0).getLeft();
                Log.d("width",Pointp_JJ+"");
                // 距离红点最左面的距离
                int length = (int) (Pointp_JJ*(position+positionOffset));
                 RelativeLayout.LayoutParams  layoutParams= (RelativeLayout.LayoutParams) redPoint.getLayoutParams();  // 得到红点的属性对象
                 layoutParams.leftMargin=length; // 设置红点距离左边的距离
                redPoint.setLayoutParams(layoutParams); // 设置红点的属性

            }

            @Override
            public void onPageSelected(int position) {
                /* 当引导界面最后一个时，显示体验按钮*/
               if (position==list.size()-1){
                   ledButton.setVisibility(View.VISIBLE);
               }else {
                   ledButton.setVisibility(View.GONE);
               }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

              /*体验按钮的跳转监听事件*/
        ledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LedActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
