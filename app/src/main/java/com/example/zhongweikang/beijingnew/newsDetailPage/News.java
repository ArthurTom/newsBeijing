package com.example.zhongweikang.beijingnew.newsDetailPage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhongweikang.beijingnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * webView界面
 */

public class News extends Activity implements View.OnClickListener {
    String URL;
    @BindView(R.id.tittle)
    TextView tittle;
    @BindView(R.id.image_back)
    ImageButton imageback;
    @BindView(R.id.image_Share)
    ImageButton imageShare;
    @BindView(R.id.image_TextSize)
    ImageButton imageTextSize;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.image_prograbase)
    ProgressBar imagePrograbase;
    private int CurrentChecked = 2;  // 当前选中的
    private int tempChecked;
    WebSettings set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.new_item_intent);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String newURL = intent.getStringExtra("url");
        Log.d("newURL", newURL);

        String starUrl = "http://172.17.1.35";
        URL = starUrl + newURL.substring(15);
        Log.d("URL", URL);
        imageback.setOnClickListener(this);
        imageShare.setOnClickListener(this);
        imageTextSize.setOnClickListener(this);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                imagePrograbase.setVisibility(View.GONE);
            }
        });
        // 获取WebView的辅助类
        set = webview.getSettings();
        set.setJavaScriptEnabled(true);  // 设置支持javaScript,网页中更多可以点击
        set.setBuiltInZoomControls(true); //  设置 放大缩小的功能
        set.setUseWideViewPort(true);  // 设置双击放大缩小功能
        webview.loadUrl(URL);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_Share:
                showShare();


                break;
            case R.id.image_TextSize:
                ChangeTextSize();

                break;
        }
    }

    private void ChangeTextSize() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置字体大小");
        builder.setIcon(R.drawable.setting_press);
        final String[] item = {"超大号字体", "大号字体", "中号字体", "小号字体", "超小号字体"};
        builder.setSingleChoiceItems(item, CurrentChecked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("字体", item[which]);
                tempChecked = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CurrentChecked=tempChecked;
                TextSize();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        builder.show();

    }

    public void TextSize() {
        switch (CurrentChecked) {
            case 0:
                set.setTextSize(WebSettings.TextSize.LARGEST);
                break;
            case 1:
                set.setTextSize(WebSettings.TextSize.LARGER);
                break;
            case 2:
                set.setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case 3:
                set.setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case 4:
                set.setTextSize(WebSettings.TextSize.SMALLEST);
                break;
        }
    }



        private void showShare() {
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
            oks.setTitle("这是我集成的分享");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我是分享文本");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
              // 启动分享GUI
            oks.show(this);
        }

}
