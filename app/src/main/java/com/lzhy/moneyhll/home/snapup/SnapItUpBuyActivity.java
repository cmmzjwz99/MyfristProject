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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.AddressActivity;
import com.lzhy.moneyhll.model.AddressModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.motorhome.PayResultActivity;
import com.lzhy.moneyhll.utils.Base64;
import com.ta.utdid2.android.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.setWebView;

/**
 * 秒杀活动支付界面
 * Created by ycq on 2016/12/20.
 */

public class SnapItUpBuyActivity extends MySwipeBackActivity {

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
            urlString = getIntent().getStringExtra("url");
        initView();
        getDefaultAddress();
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
        titleTv.setText("订单确认");
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

                                         if (url.contains("isAddress")) {//跳转地址界面获取收获地址

                                             intent = new Intent(context, AddressActivity.class);
                                             intent.putExtra("type", "snap");
                                             context.startActivityForResult(intent, 10010);

                                         } else if (url.contains("paysuccess")) {//支付成功跳转
                                             intent = new Intent(context, PayResultActivity.class);
                                             intent.putExtra("flag", "snapup");
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

    //地址跳转结果回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10010 && resultCode == 10010) {
            //获取返回收获地址信息
            Bundle extras = data.getExtras();
            AddressModel moder = (AddressModel) extras.getSerializable("AddressModel");
            setAddress(moder, "choose");
            addWebData();
        }
    }

    //拼接地址
    private void setAddress(AddressModel moder, String type) {

        StringBuffer addrBuf = new StringBuffer();
        addrBuf.append(moder.province);
        addrBuf.append(moder.city);
        addrBuf.append(moder.district);
        addrBuf.append(moder.addresss);

        if (!StringUtils.isEmpty(urlString))
            if (type.equals("choose")) {
                urlString = urlString.replace("memberId=", "memberId=" + String.valueOf(moder.userId));//添加用户ID
            }
                urlString = urlString.replace("address=", "address=" + encodeAddr(addrBuf.toString()))//添加地址
                        .replace("receiverName=", "receiverName=" + encodeAddr(moder.name))//添加联系人姓名
                        .replace("telephone=", "telephone=" + moder.phone);//添加手机号
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

    //获取默认地址
    private void getDefaultAddress() {
        /****************
         *修改者:  ycq
         *修改时间: 2017.01.09
         *修改原因: 接口用户信息加密
         * Describe:param  传递的参数
                    Base64.getBase64(param)  参数加密
                    Base64.getFromBase64(response)  参数解密
         ****************/
        JSONObject param = new JSONObject();
        try {
            param.put("userId", UserInfoModel.getInstance().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String defaultAddreaaUrl = UrlAPI.getDefaultAddreaaUrl(Base64.getBase64(param));
        OkHttpUtils.get().url(defaultAddreaaUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                addWebData();
            }

            @Override
            public void onResponse(String response, int id) {
                response = Base64.getFromBase64(response);
                Type type = new TypeToken<Response1<AddressModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<AddressModel> resp = gson.fromJson(response, type);
                AddressModel data = resp.getData();
                if (data != null) {
                    setAddress(data, "default");
                }
                addWebData();
            }
        });
    }

    /*加载web view数据*/
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

    //将中文转换为UTF-8格式
    private String encodeAddr(String str) {
        String urlStr = "";
        try {
            if (isChinese(str)) {
                urlStr += URLEncoder.encode(str, "UTF-8") + "";
            } else {
                urlStr += str + "";
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlStr;
    }

    //判断是否是中文字符
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c) == true) {
                return isChinese(c);
            } else {
                if (i == (ch.length - 1)) {
                    return isChinese(c);
                } else {
                    continue;
                }

            }

        }
        return false;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
