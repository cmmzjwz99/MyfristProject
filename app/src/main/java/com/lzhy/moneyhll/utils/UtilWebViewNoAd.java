package com.lzhy.moneyhll.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzhy.moneyhll.MapActivity;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.home.dragonball.DragonBallDetailsActivity;
import com.lzhy.moneyhll.home.makerproject.MakerDetailsActivity;
import com.lzhy.moneyhll.home.makerproject.MakerListActivity;
import com.lzhy.moneyhll.playwhat.PlayWhatListActivity;
import com.lzhy.moneyhll.playwhat.ProjcetDetailsActivity;
import com.ta.utdid2.android.utils.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lzhy.moneyhll.constant.Constant.regexStr;
import static com.lzhy.moneyhll.home.data.GetURLString.ReadStreamOfJson;
import static com.lzhy.moneyhll.utils.CommonUtil.doCallPhone;

/****************************************************************************
 * Created by xu on 2017/1/6.
 * Function:
 ***************************************************************************/

public class UtilWebViewNoAd {

    private static Activity activity;
    /************************************************************
     * 创建者;龙之游 @ xu 596928539@qq.com
     * 修改时间:2017/1/9 10:28
     * 注释:  处理不同的页面
     * 类型标识符改一下 0 正常配置，1处理地图，2……依次类推；使用的时候用静态常量
     ************************************************************/
    public final static int DEALWITHNORMAL = 0;//正常加载网页
    public final static int DEALWITHMAP = 1;//处理地图导航的页面
    public final static int NODEAL = 2;//不在公共方法中处理，在H5页面进行拦截

    /************************************************************
     * @param url
     * @param webView
     * @param context
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/19 22:20
     * 注释:  * WebView详情页  剔除 js注入的广告
     ************************************************************/
    public static void htmlDetails(@NotNull String url, @NotNull WebView webView, @NotNull final Activity context, @Nullable int type) {
        webView.loadUrl(url);
        setWebView(webView);
        activity = context;

        /************************************************************
         *@Author; 龙之游 @ xu 596928539@qq.com
         * 时间:2016/12/19 20:10
         * type  2不设置setWebViewClient   1设置正常加载网页   0设置处理地图导航的页面
         * 注释: 处理url 剔除 恶意 脚本
         ************************************************************/
        new AsyncTaskHtml(webView, type).execute(url);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });

    }

    /****************************************************************************
     * Created by xu on 2016/12/19.
     * Function:静态内部类 AsyncTaskHtml  配合  htmlDetails 去除恶意脚本
     ***************************************************************************/

    static class AsyncTaskHtml extends AsyncTask<String, Void, String> {
        private WebView mWebView;
        private String htmlString;
        private int mtype;

        private String baseUrl;

        public AsyncTaskHtml(WebView webView, int type) {
            mWebView = webView;
            mtype = type;
        }

        @Override
        protected String doInBackground(String... strings) {
            baseUrl = strings[0];
            return ReadStreamOfJson(strings[0]);

        }

        @Override
        protected void onPostExecute(String html) {
            super.onPostExecute(html);
            /************************************************************
             *@Author; 龙之游 @ xu 596928539@qq.com
             * 时间:2016/12/19 20:07
             * 注释:  正则处理到的html源码  字符串
             ************************************************************/
            ReadStreamOfJson(html);
            Pattern p = Pattern.compile(regexStr);
            Matcher m = p.matcher(html);
            while (m.find()) {
                if (!(m.group().contains("lzyhll.com"))) {
                    html = html.replace(m.group(), "");
                }
            }

            if (mtype == DEALWITHNORMAL) {
                dealWithNormal(mWebView);
            } else if (mtype == DEALWITHMAP) {//处理含有地图导航和拨打电话的H5
                dealWithMap(mWebView);
            }

            String strUrl = baseUrl;
            try {
                URL url = new URL(strUrl);
                strUrl = String.format("%s://%s", url.getProtocol(), url.getHost());
//                Log.i("strUrl", "onPostExecute: " + strUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            mWebView.loadDataWithBaseURL(strUrl, html,
                    "text/html", "utf-8", "file:///android_asset/error_page.html");
        }
    }

    private static void dealWithNormal(WebView mWebView) {
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
                activity.startActivity(intent);
//                context.finish();
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.endsWith(".apk")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private static void dealWithMap(WebView mWebView) {
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.endsWith(".apk")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                PrintLog.e("详情" + url.toString());
                if (url.indexOf("lzy://") != -1) {
                    java.util.HashMap<String, String> map = Utils.getMapByLink(url);
                    if (map.size() > 0) {
                        //跳转到地图
                        Intent intent = new android.content.Intent(activity, MapActivity.class);
                        intent.putExtra("lat", map.get("lat"));
                        intent.putExtra("lng", map.get("lng"));
                        intent.putExtra("name", map.get("name"));
                        PrintLog.e(map.get("lat") + map.get("lng"));
                        activity.startActivity(intent);
                    }
                }
                if (url.indexOf("https://") != -1) {
                    java.util.HashMap<String, String> map = Utils.getMapByLink(url);
                    if (map == null || map.size() == 0) {
                        String[] split = url.split("/");
                        int id = Integer.valueOf(split[split.length - 1]);
                        Intent intent = new android.content.Intent(activity, ProjcetDetailsActivity.class);
                        intent.putExtra("id", id);
                        activity.startActivity(intent);
                    }if(map.size()==1){
                        /**
                         * 新春特惠，需跳转到创客详情页或商品详情页
                         */
                        if (url.contains("maker/detail") || url.contains("maker/gdetail")) {
                            Intent intent = new Intent(activity, MakerDetailsActivity.class);
                            Bundle extras = new Bundle();
                            if (url.contains("maker/detail")) {
                                extras.putInt("type", 103);
                            } else {
                                extras.putInt("type", 104);
                            }
                            int id = Integer.valueOf(map.get("id"));
                            PrintLog.e("创客id：" + id);
                            extras.putInt("id", id);
                            intent.putExtras(extras);
                            activity.startActivity(intent);
                        } if (url.contains("mall/detail")) {
                            Intent intent = new Intent(activity, DragonBallDetailsActivity.class);
                            int id = Integer.valueOf(map.get("id"));
                            intent.putExtra("id", id);
                            activity.startActivity(intent);
                        }
                    }
                    if (map.size() >= 3) {
                        Intent intent = new android.content.Intent();
                        if ("1022".equals(map.get("pid"))) {
                            intent.setClass(activity, MakerListActivity.class);
                        } else {
                            intent.setClass(activity, PlayWhatListActivity.class);
                            intent.putExtra("type", map.get("type"));
                            intent.putExtra("pid", map.get("pid"));
                        }
                        intent.putExtra("lat", map.get("lat"));
                        intent.putExtra("lng", map.get("lng"));
                        intent.putExtra("scope", "50");
                        activity.startActivity(intent);
                    }
                }
                if (url.indexOf("tel:") != -1) {
                    doCallPhone(activity, url);
                }
                return true;
            }
        });
    }

    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/20 14:06
     * 注释:  适配处理 未使用
     ************************************************************/
    private static String getHtmlData(String bodyHTML) {
        Log.i("ttt", "getHtmlData: " + bodyHTML);
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        //return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
        return bodyHTML;
    }

    /************************************************************
     * 创建者;龙之游 @ xu 596928539@qq.com
     * 修改时间:2017/1/6 11:49
     * 注释:webview设置
     ************************************************************/
    public static void setWebView(WebView webView) {
        String cacheDirPath = Constant.Diskcache;
        File dir = new File(cacheDirPath);
        if (!dir.exists()) dir.mkdirs();
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(100);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        //设置 缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        //不在webiew中保存密码
        webSettings.setSavePassword(false);
        //设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
    }

    /*************************************************************
     * 创建者;龙之游 @ xu 596928539@qq.com
     * 修改时间:2017/1/18 18:26
     *  分享
     * @param mWebview   传一个 webview
     * @param defTitle   默认的抬头
     * @param defContent 默认的简介
     ************************************************************/
    public static void webview_share(final WebView mWebview, final String defTitle, final String defContent,final String shareUrl) {
        mWebview.evaluateJavascript("window.share_config", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                if (false == StringUtils.isEmpty(s)) {
                    // s值为：标题|图片URL|描述
                    String[] split = s.replace("\"", "").split("\\|");
                    String title = "";//公司名称
                    String logoUrl = "";
                    String content = "";

                    PrintLog.e("公司简介:" + title);
                    if (split.length == 1) {
                        title = split[0];
                        content = defContent;
                    }else if (split.length == 2) {
                        title = split[0];
                        logoUrl = split[1];
                        content = defContent;
                    }else if (split.length == 3) {
                        title = split[0];
                        logoUrl = split[1];
                        content = split[2];
                    } else{
                        title = defTitle;
                        content = defContent;
                    }
                    Utils.ShareWX(UtilActivityManager.getInstance().getCurrentActivity(), title, logoUrl, content, shareUrl, mWebview);
                }
            }
        });
    }
}
