package com.lzhy.moneyhll.home.snapup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.ta.utdid2.android.utils.StringUtils;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.setWebView;

/**
 * 秒杀活动专场界面
 * Created by ycq on 2016/12/20.
 */

public class SnapItUpActivity extends MySwipeBackActivity {

    private WebView webView;
    public Activity context;
    public ClearAdsTask asyncTaskHtml;
    private ImageView backIv;
    private RelativeLayout tilteRl;
    private TextView titleTv;
    private String SNAP_HOST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_snapup_homebanner);
        context = this;
        addActivityCST(this);
        SNAP_HOST=getIntent().getStringExtra("url");//获取HTML页面网址
        initTitlebar();
        initView();
        if (!StringUtils.isEmpty(SNAP_HOST))
        asyncTaskHtml = new ClearAdsTask(webView,SNAP_HOST);
        asyncTaskHtml.execute(SNAP_HOST);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }



    //加载头部标题
    private void initTitlebar() {
        backIv = (ImageView) findViewById(R.id.snap_title_iv);
        tilteRl = (RelativeLayout) findViewById(R.id.snap_title_rl);
        titleTv = (TextView) findViewById(R.id.snap_title_tv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        titleTv.setText("活动专场");
        tilteRl.setBackground(getResources().getDrawable(R.mipmap.snap_title_bg));
        titleTv.setTextColor(getResources().getColor(R.color.white));
        backIv.setImageDrawable(getResources().getDrawable(R.mipmap.snap_title_back_iv));
    }



    //实例化控件,设置webview属性
    private void initView() {
        webView = (WebView) findViewById(R.id.snap_webv);
        setWebView(webView);
        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                         Intent intent;
                                         if (url.contains("SnapItUpDetail")) {//跳转秒杀商品详情界面
                                             intent = new Intent(context, SnapItUpDetailsActivity.class);
                                             intent.putExtra("url", url);
                                             startActivity(intent);
                                         } else {
                                             view.loadUrl(url);
                                         }
                                         return true;
                                     }

                                     @Override
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                         super.onReceivedError(view, errorCode, description, failingUrl);
                                         view.loadUrl("file:///android_asset/error_page.html");
                                     }

                                 }
        );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (asyncTaskHtml != null)
            asyncTaskHtml.cancel(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }


}
