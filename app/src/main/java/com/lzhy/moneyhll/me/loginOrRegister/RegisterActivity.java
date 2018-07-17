
package com.lzhy.moneyhll.me.loginOrRegister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.me.loginOrRegister.bean.RegistBean;
import com.lzhy.moneyhll.me.loginOrRegister.bean.RegistSmsBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.TimerTask;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.REGISTER_CLICK_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.CommonUtil.setViewAlphaAnimation;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilCheckMix.editTextCheck;

/**
 * Created by cmm on 2016/10/24.
 */

public final class RegisterActivity extends MySwipeBackActivity implements View.OnClickListener {

    private Button btnGetCode;
    private EditText edtMobile;
    private EditText edtName;
    private EditText edtPassword;
    private EditText edtCode;
    private TextView tvRegister;

    private CheckBox cbCheck;
    private String driveId;

//-----------------------------------------------

    private   int i = 60;
    java.util.Timer timer;
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //handler处理消息
            if(msg.what < 60 && msg.what > 0){
                btnGetCode.setText("(" + msg.what + ")秒后重发");
                btnGetCode.setClickable(false);
            }
            if(msg.what == 0){
                //在handler里可以更改UI组件
//                finish();
                btnGetCode.setClickable(true);
                btnGetCode.setText("获取验证码");
                timer.cancel();
                i = 60;
            }
        }
    };
    private TextView tvXieYi;

    //------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initTitlebar();
        addActivityCST(this);
        //获取设备的唯一标识
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        driveId = TelephonyMgr.getDeviceId();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        btnGetCode = (Button) findViewById(R.id.tv_getcode);
        edtMobile = (EditText) findViewById(R.id.edt_mobile);
        edtName = (EditText) findViewById(R.id.edt_nick_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtCode = (EditText) findViewById(R.id.edt_code);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        cbCheck = (CheckBox) findViewById(R.id.cb_check);
        tvXieYi = (TextView) findViewById(R.id.tv_xieyi);
        tvXieYi.setText(Html.fromHtml("我已阅读并同意"+"<u>"+"《龙之游用户注册协议》"+"</u>"));
        tvXieYi.setOnClickListener(this);

        btnGetCode.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        titlebar.setTitle(getString(R.string.register));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //获取验证码
            case R.id.tv_getcode:
                sendCode();
                break;

                //完成注册
            case R.id.tv_register:
                setViewAlphaAnimation(tvRegister);
                String mobile = edtMobile.getText().toString().trim();
                String code = edtCode.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String password = edtPassword.getText().toString();
                if (!editTextCheck(name, this,"名字不能输入非法字符")) {
                    return;
                }
                if (!cbCheck.isChecked()){
                    Utils.toast(this, "亲，您得同意协议哦！");
                    return;
                }
                if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(code) || TextUtils.isEmpty
                        (name)|| TextUtils.isEmpty(password)){
                    Utils.toast(this, "请您填写完整!");
                    return;
                }
                if (edtCode.getText().toString().trim().length() != 6){
                    Utils.toast(this, "请您输入6位验证码!");
                    return;
                }
                if (edtPassword.getText().toString().trim().length() < 6){
                    Utils.toast(this, "请输入6~16位密码");
                    return;
                }
                String md5Value = Utils.getMD5Value(password);
                PrintLog.e("完成------------id2----------------注册URL:" + SharePrefenceUtils.getInt(this,"registCode"));
                OkHttpUtils.post().url(UrlAPI.HOST_URL+ "user/Regist")
                        .addParams("id" , SharePrefenceUtils.getInt(this,"registCode") +"")
                        .addParams("mobile" , mobile).addParams("sms" , code)
                        .addParams("name" , name).addParams("pwd" , md5Value)
                        .addParams("driveId" , driveId)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("完成注册:" + e);
                        Utils.toast(RegisterActivity.this,"注册失败");
                        setCustomStatisticsKV(RegisterActivity.this, REGISTER_CLICK_ID,e.getMessage());//统计
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("完成注册:" + response);
                        Type type = new TypeToken<Response1<RegistBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<RegistBean> resp = gson.fromJson(response, type);
                        String errCode = resp.getErrCode();
                        if (errCode == null){
                            setCustomStatisticsKV(RegisterActivity.this, REGISTER_CLICK_ID,resp.getMessage());//统计
                            return;
                        }
                        if (!errCode.equals("200")){
                            Utils.toast(RegisterActivity.this,resp.getMessage());
                            setCustomStatisticsKV(RegisterActivity.this, REGISTER_CLICK_ID,resp.getMessage());//统计
                            return;
                        }

                        setCustomStatisticsKV(RegisterActivity.this, REGISTER_CLICK_ID,resp.getMessage());//统计
                        Utils.toast(RegisterActivity.this,resp.getMessage());

                        try {
                            Thread.sleep(1000);

                            finish();
                        }catch (Exception e){

                        }
                    }
                });
                break;

            case R.id.tv_xieyi:
                startActivity(new Intent(this,XieYiActivity.class));
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode(){
        if(edtMobile.getText().toString().length()<=0){
            Utils.toast(this,getString(R.string.please_input_tel));
            return;
        }
        if(!Utils.isMobile(edtMobile.getText().toString())){
            Utils.toast(this,getString(R.string.error_input_tel));
            return;
        }

        OkHttpUtils.post().url(UrlAPI.HOST_URL+ "user/doRegist")
                .addParams("mobile" , edtMobile.getText().toString()).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("获取验证码:" + e);
                Utils.toast(RegisterActivity.this,"网络异常");
            }
            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("获取验证码:" + response);
                try {

                    Gson gson = new Gson();
                    Type type = new TypeToken<Response1<RegistSmsBean>>() {
                    }.getType();
                    Response1<RegistSmsBean> resp = gson.fromJson(response, type);

                    PrintLog.e("-------resp.getData().data.id---------:" + resp.getData().id);

                    SharePrefenceUtils.put(RegisterActivity.this,"registCode",resp.getData().id);

                    if (resp.getErrCode() == null){
                        return;
                    }

                    if (!resp.getErrCode().equals("200")){
                        Utils.toast(RegisterActivity.this,resp.getMessage());
                        return;
                    }

                    Utils.toast(RegisterActivity.this,resp.getMessage());
                    myTimer();

                }catch (Exception e){
                    Utils.toast(RegisterActivity.this,"此账号已注册");
                }

            }
        });
    }


    /**
     * 定时器
     */
    private void myTimer(){
        setViewAlphaAnimation(btnGetCode);
        // 定义计时器
        timer = new java.util.Timer();

        // 定义计划任务，根据参数的不同可以完成以下种类的工作：在固定时间执行某任务，在固定时间开始重复执行某任务，重复时间间隔可控，在延迟多久后执行某任务，在延迟多久后重复执行某任务，重复时间间隔可控
        timer.schedule(new TimerTask() {

            // TimerTask 是个抽象类,实现的是Runable类
            @Override
            public void run() {
//                Log.i("yao", Thread.currentThread().getName());
                //定义一个消息传过去
                Message msg = new Message();
                msg.what = i--;
                handler.sendMessage(msg);
            }

        }, 1000, 1000);
    }
}
