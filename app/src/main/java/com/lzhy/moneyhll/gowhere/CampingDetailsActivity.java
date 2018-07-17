package com.lzhy.moneyhll.gowhere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilWebViewNoAd;

import static com.lzhy.moneyhll.constant.Constant.SHARE_CLICK_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setConfigCallback;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.DEALWITHMAP;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;

/**
 * 露营详情页
 */
public class CampingDetailsActivity extends MySwipeBackActivity {

    private WebView mWebView;
    private BaseTitlebar mTitlebar;
    private String url;

    private int id;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camping_details);
        addActivityCST(this);

        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        initView();
        initTitlebar();
        htmlDetails(url, mWebView, this, DEALWITHMAP);
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
        url = UrlAPI.APP_H5_HOST + "/project/camp/" + id;
        PrintLog.e("露营详情页:" + url);
    }

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("详情");
        mTitlebar.setRightButton(R.mipmap.share, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCustomStatisticsKV(CampingDetailsActivity.this, SHARE_CLICK_ID, "露营详情页");//统计
                UtilWebViewNoAd.webview_share(mWebView,"龙之游","诚邀您体验龙之游，各大应用市场均可下载!",url);
//                mWebView.evaluateJavascript("window.share_config", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String s) {
//                        if (false == StringUtils.isEmpty(s)) {
//                            // s值为：标题|图片URL|描述
//                            String[] split = s.replace("\"", "").split("\\|");
//
//                            String desc = "";
//
//                            PrintLog.e("露营详情页分享:" + s);
//                            if (split.length >= 3) {
//                                desc = split[2];
//                            }
//                            Utils.ShareWX(CampingDetailsActivity.this, split[0], split[1], desc, url, mWebView);
//
//                        }
//                    }
//                });
            }
        });
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setConfigCallback(null);
    }
}
