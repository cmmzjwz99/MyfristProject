package com.lzhy.moneyhll.home.motorhomeshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.utils.PrintLog;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setConfigCallback;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.DEALWITHNORMAL;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;

/**
 * 房车车型展示详情页
 */
public class MororhomeShowDetailsActivity extends MySwipeBackActivity {

    private WebView mWebView;
    private BaseTitlebar mTitlebar;
    private String url;
    private String id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camping_details);

        addActivityCST(this);
        setConfigCallback((WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        initView();
        initTitlebar();
        initWebView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        mWebView= (WebView) findViewById(R.id.webview);
        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        url= UrlAPI.APP_H5_HOST + "/rv/show/"+id;
        PrintLog.e("房车车型展示详情页:"+url);
    }

    private void initTitlebar() {
        mTitlebar= (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,name);
    }
    private void initWebView() {
        htmlDetails(url,mWebView,this,DEALWITHNORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setConfigCallback(null);
    }
}
