package com.lzhy.moneyhll.home.makerproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MakerWebView;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.gowhere.CampingDetailsActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.loginOrRegister.LoginActivity;
import com.lzhy.moneyhll.me.loginOrRegister.bean.LoginBean;
import com.lzhy.moneyhll.me.loginOrRegister.bean.RegistSmsBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.playwhat.PlayWhatListActivity;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.lzhy.moneyhll.utils.Utils;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.LtApplication.driveId;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setConfigCallback;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.NODEAL;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.htmlDetails;

/**
 * 创客项目详情
 */
public class MakerDetailsActivity extends MySwipeBackActivity {
    private MakerWebView mWebView;
    private BaseTitlebar mTitlebar;
    private String url;
    private String shareurl;

    private ScrollView scrollview;
    private LinearLayout linear_bottom;
    private TextView tv_top;
    private EditText et_name;
    private EditText et_phone;
    private Button get_pass_code;
    private EditText et_pass_code;

    private Button make_order;

    private Intent intent;

    private int userId;
    private String account;
    private String smsCode;
    private String realName;

    private int id;
    private int projecttype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_maker_details);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        addActivityCST(this);
        initView();
        initTitlebar();
        initWebView();
    }

    private void initView() {
        intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getInt("id");
        projecttype = extras.getInt("type");

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        mWebView = (MakerWebView) findViewById(R.id.webview);
        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);
        tv_top = (TextView) findViewById(R.id.tv_top);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        get_pass_code = (Button) findViewById(R.id.get_pass_code);
        et_pass_code = (EditText) findViewById(R.id.et_pass_code);

        make_order = (Button) findViewById(R.id.make_order);

        make_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserInfoModel.getInstance().isLogin()) {
                    if (et_name.getText().length() <= 0) {
                        UtilToast.getInstance().showDragonInfo("请输入名字"); ;
                        return;
                    }
                    if (et_phone.getText().length() <= 0) {
                        UtilToast.getInstance().showDragonInfo("请输入电话号码"); ;
                        return;
                    }
                    if (et_pass_code.getText().length() <= 0) {
                        UtilToast.getInstance().showDragonInfo("请输入验证码"); ;
                        return;
                    }
                    LogOrRegister();
                } else {
                    ToNext();
                }
            }
        });

        get_pass_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_phone.getText().toString().length() <= 0) {
                    UtilToast.getInstance().showDragonInfo(getString(R.string.please_input_tel)); ;
                    return;
                }
                if (!Utils.isMobile(et_phone.getText().toString())) {
                    UtilToast.getInstance().showDragonInfo(getString(R.string.error_input_tel)); ;
                    return;
                }
                getPassCode();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        disparityLogin();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
        if (projecttype == 104) {
            url = UrlAPI.APP_H5_HOST + "/maker/gdetail/" + id;
            shareurl = UrlAPI.APP_H5_HOST + "/maker/wxgdetail?id=" + id;
        } else {
            url = UrlAPI.APP_H5_HOST + "/maker/detail/" + id;
            shareurl = UrlAPI.APP_H5_HOST + "/maker/wxdetail?id=" + id;
        }
        PrintLog.e("创客详情页:" + url);

        htmlDetails(url, mWebView, MakerDetailsActivity.this, NODEAL);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                make_order.setVisibility(View.VISIBLE);
                make_order.setText("提交订单");
                if (UserInfoModel.getInstance().isLogin()) {
                    linear_bottom.setVisibility(View.GONE);
                } else {
                    linear_bottom.setVisibility(View.VISIBLE);
                }

                mTitlebar.setRightButton(R.mipmap.share, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!UserInfoModel.getInstance().isLogin()) {
                            startActivity(new Intent(MakerDetailsActivity.this, LoginActivity.class));
                            return;
                        }
                        mWebView.evaluateJavascript("window.share_config", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                if (false == StringUtils.isEmpty(s)) {
                                    // s值为：标题|图片URL|描述
                                    String[] split = s.replace("\"", "").split("\\|");

                                    String desc = "";

                                    PrintLog.e("创客详情页分享:" + s);
                                    if (split.length >= 3) {
                                        desc = split[2];
                                    }
                                    if (!(UserInfoModel.getInstance().getServiceStatus() == 0)) {
                                        shareurl = shareurl + "&recommendId=" + UserInfoModel.getInstance().getId();
                                    } else {
                                        shareurl = shareurl + "&recommendId=0";
                                    }
                                    PrintLog.e(shareurl);
                                    Utils.ShareWX(MakerDetailsActivity.this, split[0], split[1], desc, shareurl, mWebView);
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                PrintLog.e(url.toString());
                if (url.indexOf("https://") != -1) {
                    java.util.HashMap<String, String> map = Utils.getMapByLink(url);
                    if (map == null || map.size() == 0) {
                        String[] split = url.split("/");
                        int id = Integer.valueOf(split[split.length - 1]);
                        Intent intent = new Intent(MakerDetailsActivity.this, CampingDetailsActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        if ("1022".equals(map.get("pid"))) {
                            intent.setClass(MakerDetailsActivity.this, MakerListActivity.class);

                        } else {
                            intent.setClass(MakerDetailsActivity.this, PlayWhatListActivity.class);
                            intent.putExtra("type", map.get("101,102"));
                            intent.putExtra("pid", map.get("pid"));
                        }
                        intent.putExtra("lat", map.get("lat"));
                        intent.putExtra("lng", map.get("lng"));
                        intent.putExtra("scope", "50");

                        startActivity(intent);
                    }
                }

                if (MakerDetailsActivity.this != null) {

                }
                return true;
            }
        });
    }

    private void getPassCode() {
        PrintLog.e("获取验证码url:" + UrlAPI.HOST_URL + "user/mallSendSms  " + et_phone.getText().toString());
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/mallSendSms")
                .addParams("mobile", et_phone.getText().toString()).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                UtilToast.getInstance().showDragonError("网络异常") ; ;
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("获取验证码:" + response);
                Gson gson = new Gson();
                Type type = new TypeToken<Response1<RegistSmsBean>>() {
                }.getType();
                Response1<RegistSmsBean> resp = gson.fromJson(response, type);
                if ("200".equals(resp.getErrCode())) {
                    userId = resp.getData().id;
                    UtilToast.getInstance().showDragonSuccess("验证码已发送,请注意查收!") ;
                } else {
                    UtilToast.getInstance().showDragonError(resp.getMessage()) ;
                }
            }
        });
    }

    private void LogOrRegister() {
        account = et_phone.getText().toString();
        smsCode = et_pass_code.getText().toString();
        realName = et_name.getText().toString();

        PrintLog.e(":" + " userId:" + userId + " account:" + account +
                " smsCode:" + smsCode + " realName:" + realName + " 后六位:" + account.substring(5, 11));
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/submitOrder")
                .addParams("userId", userId + "")
                .addParams("account", account)
                .addParams("smsCode", smsCode)
                .addParams("realName", realName)
                .addParams("pwd", Utils.getMD5Value(account.substring(5, 11)))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("创客onError:" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("创客onResponse:" + response);
                Type type = new TypeToken<Response1<LoginBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<LoginBean> response2 = gson.fromJson(response, type);
                if ("200".equals(response2.getErrCode())) {
                    if (response2.getData().getPassword() == null || response2.getData().getPassword().length() <= 0) {
                        UserInfoModel.getInstance().copy(response2.getData());
                        UserInfoModel.getInstance().sync();
                        ToNext();
                    } else {
                        login(response2.getData());
                    }

                } else {
                    UtilToast.getInstance().showDragonError(response2.getMessage()) ;
                }
            }
        });
    }

    /**
     * 登录
     */
    private void login(LoginBean bean) {

        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/login")
                .addParams("account", bean.account)
                .addParams("pwd", bean.getPassword())
                .addParams("driveId", driveId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                UtilToast.getInstance().showDragonError( "网络异常") ;
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e(response);
                Type type = new TypeToken<Response1<LoginBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<LoginBean> resp = gson.fromJson(response, type);
                if (resp.getErrCode() == null) {
                    return;
                }
                if (!resp.getErrCode().equals("200")) {
                    UtilToast.getInstance().showDragonError(resp.getMessage()) ;
                    return;
                }
                UtilToast.getInstance().showDragonSuccess(resp.getMessage()) ;
                UserInfoModel.getInstance().copy(resp.getData());
                UserInfoModel.getInstance().sync();
                ToNext();
            }

        });
        return;
    }

    public void ToNext() {
        Intent intent = new Intent(MakerDetailsActivity.this, BuyProjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("type", projecttype);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setConfigCallback(null);
    }
}
