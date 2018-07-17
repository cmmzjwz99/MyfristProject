package com.lzhy.moneyhll.me.mine.userInfo;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.UesrBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.TimerTask;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/11/13.
 * 重置支付密码
 */

public class ResetPayPwdActivity extends MySwipeBackActivity implements View.OnClickListener {


    private String etSms;
    private String payPwd;
    private EditText etPhoneNum;
    private EditText edGetCode;
    private EditText edPayPsw;
    private TextView tvGetCode;

    //-----------------------------------------------

    private int i = 60;
    java.util.Timer timer;
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //handler处理消息
            if (msg.what < 60 && msg.what > 0) {
                tvGetCode.setText("(" + msg.what + ")秒后重发");
                tvGetCode.setClickable(false);
            }
            if (msg.what == 0) {
                //在handler里可以更改UI组件
//                finish();
                tvGetCode.setClickable(true);
                tvGetCode.setText("获取验证码");
                timer.cancel();
                i = 60;
            }

        }
    };

    //------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pay_psw);
        addActivityCST(this);
        initTitlebar();
        initView();
    }
    @Override
    protected void onResume() {
        disparityLogin();
        super.onResume();
    }

    //初始化标题
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"重置支付密码");
    }


    //初始化布局
    private void initView() {
        etPhoneNum = (EditText) findViewById(R.id.et_mobile);
        edGetCode = (EditText) findViewById(R.id.et_get_code);
        edPayPsw = (EditText) findViewById(R.id.et_pay_psw);
        tvGetCode = (TextView) findViewById(R.id.tv_getcode);
        TextView tvVreify = (TextView) findViewById(R.id.tv_vreify_revamp);


        tvVreify.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //获取验证码
            case R.id.tv_getcode:
                if (!Utils.isMobile(etPhoneNum.getText().toString())) {
                    Utils.toast(this, getString(R.string.error_input_tel));
                    return;
                }

                OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/doResetPayPwd")
                        .addParams("mobile", etPhoneNum.getText().toString())
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("获取验证码:" + e);
                        Utils.toast(ResetPayPwdActivity.this, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("获取验证码:" + response);

                        Type type = new TypeToken<Response1<UesrBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<UesrBean> resp = gson.fromJson(response, type);
                        String errCode = resp.getErrCode();
                        if (errCode == null) {
                            return;
                        }
                        if (!errCode.equals("200")) {
                            Utils.toast(ResetPayPwdActivity.this, resp.getMessage());
                            return;
                        }
                        Utils.toast(ResetPayPwdActivity.this, resp.getMessage());
                        myTimer();

                    }
                });

                break;


            //确认重置
            case R.id.tv_vreify_revamp:

                etSms = edGetCode.getText().toString().trim();
                payPwd = edPayPsw.getText().toString().trim();


                if (!Utils.isMobile(etPhoneNum.getText().toString())) {
                    Utils.toast(this, getString(R.string.error_input_tel));
                    return;
                }
                if (etSms.length() != 6) {
                    Utils.toast(this, "请输入6位验证码");
                    return;
                }
                if (payPwd.length() != 6) {
                    Utils.toast(this, "请输入6位支付密码");
                    return;
                }
                String md5Value = Utils.getMD5Value(payPwd);

                OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/resetPayPwd")
                        .addHeader("accessToken", UserInfoModel.getInstance().token)
                        .addParams("phoneNum", etPhoneNum.getText().toString())
                        .addParams("sms", etSms)
                        .addParams("newPayPwd", md5Value)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("确认重置:" + e);
                        Utils.toast(ResetPayPwdActivity.this, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("确认重置:" + response);


                        Type type = new TypeToken<Response1<UesrBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<UesrBean> resp = gson.fromJson(response, type);
                        String errCode = resp.getErrCode();
                        if (errCode == null) {
                            return;
                        }
                        if (!errCode.equals("200")) {
                            Utils.toast(ResetPayPwdActivity.this, resp.getMessage());
                            return;
                        }
                        Utils.toast(ResetPayPwdActivity.this, resp.getMessage());

                        try {
                            Thread.sleep(1000);

                        } catch (Exception e) {
//                            Utils.toast(ResetPayPwdActivity.this,"短信验证码输入//////////////错误");
                        }
                        finish();
                    }
                });
                break;
        }

    }

    /**
     * 定时器
     */
    private void myTimer() {
        // 定义计时器
        timer = new java.util.Timer();

        // 定义计划任务，根据参数的不同可以完成以下种类的工作：在固定时间执行某任务，在固定时间开始重复执行某任务，重复时间间隔可控，在延迟多久后执行某任务，在延迟多久后重复执行某任务，重复时间间隔可控
        timer.schedule(new TimerTask() {

            // TimerTask 是个抽象类,实现的是Runable类
            @Override
            public void run() {
                Log.i("yao", Thread.currentThread().getName());
                //定义一个消息传过去
                Message msg = new Message();
                msg.what = i--;
                handler.sendMessage(msg);
            }

        }, 1000, 1000);
    }
}
