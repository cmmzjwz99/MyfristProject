package com.lzhy.moneyhll.home.bannerbelow;

import android.os.Bundle;
import android.webkit.WebView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.DEALWITHNORMAL;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;


public class ActivityDetailPage extends MySwipeBackActivity {

    private WebView mWebView;
    private BaseTitlebar mTitlebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camping_details);

        addActivityCST(this);
        mTitlebar= (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"详情");
        mWebView = (WebView) findViewById(R.id.webview);
        htmlDetails(getIntent().getStringExtra("detail_url"),mWebView,this,DEALWITHNORMAL);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
}
