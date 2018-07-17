package com.lzhy.moneyhll.home.bannerbelow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.lzhy.moneyhll.MapActivity;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.utils.UtilWebViewNoAd;

import static com.lzhy.moneyhll.R.id.webview;
import static com.lzhy.moneyhll.api.UrlAPI.urlCompany;
import static com.lzhy.moneyhll.constant.Constant.MAP_CLICK_ID;
import static com.lzhy.moneyhll.constant.Constant.SHARE_CLICK_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.DEALWITHNORMAL;

/************************************************************
 *@Author; 龙之游 @ xu 596928539@qq.com
 * 时间:2017/1/11 13:02
 * 注释:公司简介
************************************************************/
public class ActivityComProfile extends MySwipeBackActivity {

    private WebView mWebView;
    private Button lzy_nav_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_profile);

        addActivityCST(this);
        mWebView = (WebView) findViewById(webview);
        lzy_nav_btn = (Button) findViewById(R.id.lzy_nav_btn);
        lzy_nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到地图   龙之游 30.2913530000,120.1859640000
                Intent intent = new android.content.Intent(ActivityComProfile.this, MapActivity.class);
                intent.putExtra("lat", "30.285441");
                intent.putExtra("lng", "120.179128");
                intent.putExtra("name", "龙之游");
                setCustomStatisticsKV(ActivityComProfile.this, MAP_CLICK_ID,"龙之游导航");
//                        MobclickAgent.onEvent(ProjcetDetailsActivity.this, "map_clicked");//统计定位被使用了多少次
                startActivity(intent);
            }
        });
        initTitlebar();

        UtilWebViewNoAd.htmlDetails(urlCompany,mWebView,this,DEALWITHNORMAL);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initTitlebar() {
        BaseTitlebar mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"龙之游介绍");
        mTitlebar.setRightButton(R.mipmap.share, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCustomStatisticsKV(ActivityComProfile.this, SHARE_CLICK_ID, null);//统计
                UtilWebViewNoAd.webview_share(mWebView,"龙之游","诚邀您体验龙之游，各大应用市场均可下载!",urlCompany);//webview中的分享
            }
        });
    }
}
