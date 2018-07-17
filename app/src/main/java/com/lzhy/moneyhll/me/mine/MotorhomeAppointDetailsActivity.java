package com.lzhy.moneyhll.me.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.lzhy.moneyhll.motorhome.MakeOrderActivity;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setConfigCallback;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.NODEAL;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;

/**
 * 房车预约详情
 */
public class MotorhomeAppointDetailsActivity extends MySwipeBackActivity {
    private WebView mWebView;
    private BaseTitlebar mTitlebar;

    private String url;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camping_details);

        addActivityCST(this);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        initView();
        initTitlebar();
        initWebView();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
        id = getIntent().getIntExtra("id", 0);
        url = UrlAPI.APP_H5_HOST + "/rv/lzy?branchid=" + id;
        PrintLog.e("房车网点预约详情页:" + url);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"预约");
    }

    private void initWebView() {
        htmlDetails(url, mWebView, this, NODEAL);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                PrintLog.e(url.toString());
                if (url.indexOf("lzy://") != -1) {
                    java.util.HashMap<String, String> map = Utils.getMapByLink(url);

                    if (map.size() > 0) {
                        if (UserInfoModel.getInstance().isLogin()) {
                            Intent intent = new android.content.Intent(MotorhomeAppointDetailsActivity.this, MakeOrderActivity.class);
                            intent.putExtra("id", map.get("id"));
                            intent.putExtra("begTime", map.get("begTime"));
                            intent.putExtra("endTime", map.get("endTime"));
                            PrintLog.e(map.get("id") + map.get("begTime"));
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(MotorhomeAppointDetailsActivity.this, LoginActivity.class));
                        }
                    }
                }
                if (url.indexOf("https://") != -1) {
                    String[] split = url.split("/");

                    Intent intent = new Intent(MotorhomeAppointDetailsActivity.this, MororhomeShowDetailsActivity.class);
                    intent.putExtra("name", "房车详情");
                    intent.putExtra("id", split[split.length - 1]);
                    startActivity(intent);
                }

                if (MotorhomeAppointDetailsActivity.this != null) {
                    //  Toast.makeText(context, "加载中...", Toast.LENGTH_SHORT).show();
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