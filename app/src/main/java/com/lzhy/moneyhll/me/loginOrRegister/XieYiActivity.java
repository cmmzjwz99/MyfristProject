package com.lzhy.moneyhll.me.loginOrRegister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by lzy on 2016/12/19.
 */
public final class XieYiActivity extends MySwipeBackActivity {

    private WebView wvXieYi;
    private String loadUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xie_yi);
        wvXieYi = (WebView) findViewById(R.id.wv_xieyi);
        initTitlebar();
        localHtml();
        addActivityCST(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
;
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"龙之游平台用户协议");
    }

     private void localHtml() {
         loadUrl = "file:///android_asset/RegisterProtocol.html";
        try {
            // 本地文件处理(如果文件名中有空格需要用+来替代)  
            /************************************************************
             *修改者;  龙之游 @ xu 596928539@qq.com
             *修改时间:2016/12/26 9:29
             *bug:加载本地页面后 跳转到官网  图片上面显示大片灰色
             *修复:  CommonUtil.setWebView(wvXieYi);
             ************************************************************/
            WebSettings webSettings = wvXieYi.getSettings();
            //支持javascript
            webSettings.setJavaScriptEnabled(true);
            // 设置可以支持缩放
            webSettings.setSupportZoom(true);
            // 设置出现缩放工具
            webSettings.setBuiltInZoomControls(true);
            //扩大比例的缩放
            webSettings.setUseWideViewPort(true);
            //自适应屏幕
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setLoadWithOverviewMode(true);
            wvXieYi.loadUrl(loadUrl);

        }catch(Exception ex){
            ex.printStackTrace();
        }
     }
    /************************************************************
     *修改者;  龙之游 @ xu 596928539@qq.com
     *修改时间:2016/12/26 9:34
     *bug: html 页面未拦截back
     *修复: html 页面未拦截back
     ************************************************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvXieYi.canGoBack()) {
            wvXieYi.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
