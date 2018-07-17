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

import com.lzhy.moneyhll.LtApplication;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.loginOrRegister.LoginActivity;
import com.ta.utdid2.android.utils.StringUtils;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.setWebView;

/**
 * 秒杀活动商品详情界面
 * Created by ycq on 2016/12/20.
 */

public class SnapItUpDetailsActivity extends MySwipeBackActivity {

    private WebView webView;
    private String urlString;
    public static Activity context;
    public ClearAdsTask asyncTaskHtml;
    private ImageView backIv;
    private RelativeLayout tilteRl;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_snapup_homebanner);
        context = this;
        addActivityCST(this);
        initTitlebar();

        if (!StringUtils.isEmpty(getIntent().getStringExtra("url")))
            initView();
            urlString = getIntent().getStringExtra("url");
            addWebData();
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
        tilteRl.setBackgroundColor(getResources().getColor(R.color.white));
        titleTv.setText("商品详情");
        titleTv.setTextColor(getResources().getColor(R.color.gray_333));
        backIv.setImageDrawable(getResources().getDrawable(R.mipmap.return_back));
    }


    //实例化控件,设置webview属性
    private void initView() {
        webView = (WebView) findViewById(R.id.snap_webv);
        setWebView(webView);
        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         urlString = url;
                                         Intent intent;
                                         if (url.contains("islogin")) {//判断是否登录
                                             if (!UserInfoModel.getInstance().isLogin()) {//若未登录则跳转登录界面
                                                 intent = new Intent(context, LoginActivity.class);
                                                 startActivity(intent);
                                             } else {
                                                 view.loadUrl(url);
                                             }
                                         } else if (url.contains("SnapItUpBuy")) {//跳转支付详情界面
                                             intent = new Intent(context, SnapItUpBuyActivity.class);
                                             intent.putExtra("url", url);
                                             context.startActivity(intent);
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

    /*加载webview页面*/
    private void addWebData() {
        asyncTaskHtml = new ClearAdsTask(webView, urlString);
        asyncTaskHtml.execute(urlString);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果已登录并且url中不包含用户ID ，重新加载商品详情界面
        if (UserInfoModel.getInstance().isLogin() && !urlString.contains("memberId") && !urlString.contains("SnapItUpBuy")) {
            int memberId = LtApplication.getInstance()
                    .getSharedPreferences("Login", LtApplication.MODE_PRIVATE).getInt("id", 0);
            urlString = urlString + "&memberId=" + memberId;
            addWebData();
        }

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
