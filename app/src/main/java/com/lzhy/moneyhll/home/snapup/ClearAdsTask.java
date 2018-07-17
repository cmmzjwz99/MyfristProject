package com.lzhy.moneyhll.home.snapup;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.lzhy.moneyhll.home.data.GetURLString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lzhy.moneyhll.constant.Constant.regexStr;

/**
 * 去除广告异步任务
 * Created by ycq on 2016/12/30.
 */

public class ClearAdsTask extends AsyncTask<String, Void, String> {
    private WebView webView;
    private String urlString;

    public ClearAdsTask(WebView webView, String urlString) {
        this.webView = webView;
        this.urlString = urlString;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        String html = GetURLString.ReadStreamOfJson(strings[0]);
        return html;

    }

    @Override
    protected void onPostExecute(String html) {
        super.onPostExecute(html);
        /************************************************************
         *@Author; 龙之游 @ xu 596928539@qq.com
         * 时间:2016/12/19 20:07
         * 注释:  正则处理到的html源码  字符串 清除恶意脚本 lzhyapp
         ************************************************************/

        Pattern p = Pattern.compile(regexStr);
        Matcher m = p.matcher(html);
        while (m.find()) {
            if (!(m.group().contains("lzyhll.com"))) {
                html = html.replace(m.group(), "");
            }
        }
        webView.loadDataWithBaseURL(urlString, html,
                "text/html", "utf-8", "file:///android_asset/error_page.html");
    }
}