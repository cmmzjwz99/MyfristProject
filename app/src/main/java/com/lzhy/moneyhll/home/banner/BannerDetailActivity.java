package com.lzhy.moneyhll.home.banner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.DEALWITHMAP;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.DEALWITHNORMAL;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;

/**
 * banner广告页
 */
public final class BannerDetailActivity extends MySwipeBackActivity {
    private WebView mWebView;
    private String url;
    private String name;

    private BaseTitlebar titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        addActivityCST(this);
        mWebView = (WebView) findViewById(R.id.banner_webview);
        WebSettings settings = mWebView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(true);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
        if (url.contains("lzyhll.com")) {
            htmlDetails(url, mWebView, this, DEALWITHMAP);
        } else {
            htmlDetails(url, mWebView, this, DEALWITHNORMAL);
        }
        initTitlebar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

    //初始化头
    private void initTitlebar() {
        titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setTitle(name);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
