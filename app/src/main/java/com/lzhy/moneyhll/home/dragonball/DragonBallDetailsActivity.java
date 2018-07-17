package com.lzhy.moneyhll.home.dragonball;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import static com.lzhy.moneyhll.constant.Constant.SHARE_CLICK_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setConfigCallback;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.NODEAL;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;

/**
 * 龙珠商城商品详情页
 */
public class DragonBallDetailsActivity extends MySwipeBackActivity {
    private BaseTitlebar mTitlebar;
    private Button suer_pay;
    private String mUrl;
    private int id;
    private WebView mWebView;
    private int stockout;
    private Dialog dialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrintLog.e("DragonBallDetailsActivity" + CommonUtil.getCurProcessName(DragonBallDetailsActivity.this));

        setContentView(R.layout.act_dragonball_details);
        addActivityCST(this);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        addActivityCST(this);
        initView();
        initTitlebar();
        initWebView();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
        suer_pay = (Button) findViewById(R.id.suer_pay);
        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        stockout = intent.getIntExtra("stockout", 0);
        mUrl = UrlAPI.APP_H5_HOST + "/mall/detail/" + id;
        PrintLog.e("龙珠商城商品详情页:" + mUrl);
        dialog = new AlertDialog.Builder(DragonBallDetailsActivity.this, AlertDialog.THEME_TRADITIONAL).setTitle("提示")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                }).setMessage("商品缺货").create();

        suer_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stockout == 1) {
                    dialog.show();
                    return;
                }
                Intent intent = new Intent(DragonBallDetailsActivity.this, GoodsSureActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar, "详情");
    }

    private void initWebView() {
//        Log.i("nnn", "initWebView: " + mUrl);
        htmlDetails(mUrl, mWebView, this, NODEAL);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                mTitlebar.setRightButton(R.mipmap.share, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setCustomStatisticsKV(DragonBallDetailsActivity.this, SHARE_CLICK_ID, "龙珠商城分享");//统计
                        mWebView.evaluateJavascript("window.share_config", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                if (false == StringUtils.isEmpty(s)) {
                                    // s值为：标题|图片URL|描述
                                    String[] split = s.replace("\"", "").split("\\|");

                                    String desc = "";

                                    PrintLog.e("龙珠商城详情页分享:" + s);
                                    if (split.length >= 3) {
                                        desc = split[2];
                                    }
                                    Utils.ShareWX(DragonBallDetailsActivity.this, split[0], split[1], desc, mUrl, mWebView);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setConfigCallback(null);
    }
}
