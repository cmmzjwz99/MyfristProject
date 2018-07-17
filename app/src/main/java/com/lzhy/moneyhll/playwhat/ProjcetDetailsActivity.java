package com.lzhy.moneyhll.playwhat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzhy.moneyhll.MapActivity;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.gowhere.CampingDetailsActivity;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.HashMap;

import static com.lzhy.moneyhll.constant.Constant.MAP_CLICK_ID;
import static com.lzhy.moneyhll.constant.Constant.SHARE_CLICK_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setConfigCallback;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.NODEAL;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;

/**
 * 项目详情页
 */
public final class ProjcetDetailsActivity extends MySwipeBackActivity {
    private WebView mWebView;
    private BaseTitlebar mTitlebar;
    private RelativeLayout rl_phone_refer;
    private TextView tv_purchase_rush;

    private String mUrl;
    private int id;
    private String phoneurl;
    private Intent intent;
    private String consumerHotline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_project_details);

        addActivityCST(this);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
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
        rl_phone_refer = (RelativeLayout) findViewById(R.id.rl_phone_refer);
        tv_purchase_rush = (TextView) findViewById(R.id.tv_purchase_rush);

        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        consumerHotline = intent.getStringExtra("consumerHotline");
        phoneurl = "tel:" + consumerHotline;
        mUrl = UrlAPI.APP_H5_HOST + "/project/item/" + id;
        PrintLog.e("项目详情页:" + mUrl);

        tv_purchase_rush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProjcetDetailsActivity.this, "暂不支持购买", Toast.LENGTH_SHORT).show();
            }
        });

        rl_phone_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonUtil.doCallPhone(ProjcetDetailsActivity.this, phoneurl);

            }
        });
    }

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("详情");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWebView() {
        htmlDetails(mUrl, mWebView, this, NODEAL);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mTitlebar.setRightButton(R.mipmap.share, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setCustomStatisticsKV(ProjcetDetailsActivity.this, SHARE_CLICK_ID, null);//统计

//                Toast.makeText(ProjcetDetailsActivity.this, "555", Toast.LENGTH_SHORT).show();
                        mWebView.evaluateJavascript("window.share_config", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                if (false == StringUtils.isEmpty(s)) {
                                    // s值为：标题|图片URL|描述
                                    String[] split = s.replace("\"", "").split("\\|");

                                    String desc = "";

                                    PrintLog.e("露营详情页分享:" + s);
                                    if (split.length >= 3) {
                                        desc = split[2];
                                    }
                                    Utils.ShareWX(ProjcetDetailsActivity.this, split[0], split[1], desc, mUrl, mWebView);
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                PrintLog.e("详情" + url.toString());
                if (url.indexOf("lzy://") != -1) {
                    HashMap<String, String> map = Utils.getMapByLink(url);
                    if (map.size() > 0) {
                        //跳转到地图
                        Intent intent = new android.content.Intent(ProjcetDetailsActivity.this, MapActivity.class);
                        intent.putExtra("lat", map.get("lat"));
                        intent.putExtra("lng", map.get("lng"));
                        intent.putExtra("name", map.get("name"));
                        PrintLog.e(map.get("lat") + map.get("lng"));
                        setCustomStatisticsKV(ProjcetDetailsActivity.this, MAP_CLICK_ID, "导航");
//                        MobclickAgent.onEvent(ProjcetDetailsActivity.this, "map_clicked");//统计定位被使用了多少次
                        startActivity(intent);
                    }
                }
                if (url.indexOf("https://") != -1) {
                    String[] split = url.split("/");

                    int id = Integer.valueOf(split[split.length - 1]);
                    Intent intent = new android.content.Intent(ProjcetDetailsActivity.this, CampingDetailsActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }

                if (ProjcetDetailsActivity.this != null) {

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
