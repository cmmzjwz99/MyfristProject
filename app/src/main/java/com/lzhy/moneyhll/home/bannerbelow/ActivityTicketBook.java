package com.lzhy.moneyhll.home.bannerbelow;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import static com.lzhy.moneyhll.api.UrlAPI.DriveInsuranceShowUrl;
import static com.lzhy.moneyhll.api.UrlAPI.TicketBookShowUrl;
import static com.lzhy.moneyhll.api.UrlAPI.TouristVisaShowUrl;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.setWebView;


public class ActivityTicketBook extends MySwipeBackActivity {

    private  String urlPath = "file:///android_asset/error_page.html";

    private WebView  mWebview;
    private String wv_tag;
    private BaseTitlebar titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);

        titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mWebview = (WebView) findViewById(R.id.ticketBoo_webview);

        intiUrl();
        addActivityCST(this);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        new NewAsyncTask().execute(urlPath);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void intiUrl() {
        Intent intent = getIntent();
        if (intent != null) {
            wv_tag = intent.getStringExtra("wv_tag");
            if (wv_tag.equals(getString(R.string.drive_insurance))) {
                urlPath = DriveInsuranceShowUrl();
                initTitlebar(getString(R.string.drive_insurance));
            }else if (wv_tag.equals(getString(R.string.tour_visa))) {
                urlPath = TouristVisaShowUrl();
                initTitlebar(getString(R.string.tour_visa));
                titlebar.setVisibility(View.GONE);//不显示标题栏
            }else if (wv_tag.equals(getString(R.string.ticket_book))) {
                titlebar.setVisibility(View.GONE);
                urlPath = TicketBookShowUrl();
            }
        }
    }
    private void initTitlebar(String string) {
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle(string);
    }

    class NewAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPostExecute(String url)
        {
            super.onPostExecute(url);
            if (url != null) {
                mWebview.loadUrl(url);
                setWebView(mWebview);
                mWebview.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                    @Override
                    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                        Log.i("ticketBook_webview", "shouldInterceptRequest: "+url);
                        if (url.equals("https://touch.qunar.com/")||url.equals("http://m.cct.cn/") ) {
                            finish();
                        }
                        /*if (url.equals("http://m.cct.cn/visa/faguo-10415.html")) {
                            mWebview.goBack();// 返回前一个页面
                        }*/
                        return super.shouldInterceptRequest(view, url);
                    }
                });
            }
        }

        @Override
        protected String doInBackground(String... params)
        {
            return getJsonData(params[0]);
        }

        /**
         * 从 URL 中获取数据
         *
         * @param url
         * @return
         */
        private String getJsonData(String url)
        {
            String ticketBookUrl = "";
            try
            {
                String jsonString = readStream(new URL(url).openStream());
                JSONObject jsonObject;
                jsonObject = new JSONObject(jsonString);
                ticketBookUrl = jsonObject.getString("data");
            }
            catch(IOException e)
            {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ticketBookUrl;
        }


        /**
         * 从 inpustStream 获取的信息
         *
         * @param is
         * @return
         */
        private String readStream(InputStream is){
            InputStreamReader isr;
            String result = "";
            try{
                isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                while((line = br.readLine()) != null){
                    result += line;
                }

            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
