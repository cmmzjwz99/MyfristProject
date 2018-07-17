package com.lzhy.moneyhll.motorhome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.home.motorhomeshow.MororhomeShowDetailsActivity;
import com.lzhy.moneyhll.me.loginOrRegister.LoginActivity;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setConfigCallback;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.NODEAL;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;

/**
 * 租房车 房车预约详情页
 */
public final class MotorhomeDetailsActivity extends MySwipeBackActivity {
    private WebView mWebView;
    private BaseTitlebar mTitlebar;

    private String url;
    private int id;
    private Intent intent;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camping_details);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        addActivityCST(this);
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
        mWebView = (WebView) findViewById(R.id.webview);
        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name=intent.getStringExtra("name");
        url = UrlAPI.APP_H5_HOST + "/rv/detail/" + id;
        PrintLog.e("房车预约详情页:" + url);
    }

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("预约");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWebView() {

        htmlDetails(url, mWebView, this, NODEAL);
//        Log.i("onCreate", "initWebView: "+url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                PrintLog.e(url.toString());

                if (url.indexOf("lzy://") != -1) {
                    java.util.HashMap<String, String> map = Utils.getMapByLink(url);

                    if (UserInfoModel.getInstance().isLogin()) {
                        if (map.size() > 0) {
                            Intent intent = new Intent(MotorhomeDetailsActivity.this, MakeOrderActivity.class);
                            intent.putExtra("id", map.get("id"));
                            intent.putExtra("begTime", map.get("begTime"));
                            intent.putExtra("endTime", map.get("endTime"));
                            startActivity(intent);
                        }
                    } else {
                        startActivity(new Intent(MotorhomeDetailsActivity.this, LoginActivity.class));
                    }
                }
                if (url.indexOf("https://") != -1) {
                    //房车详情
                    String[] split = url.split("/");
                    Intent intent = new Intent(MotorhomeDetailsActivity.this, MororhomeShowDetailsActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("id", split[split.length - 1]);
                    startActivity(intent);
                }


                if (MotorhomeDetailsActivity.this != null) {

                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setConfigCallback(null);
    }
}
