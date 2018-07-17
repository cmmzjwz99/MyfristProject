package com.lzhy.moneyhll.me.loginOrRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.me.loginOrRegister.bean.LoginBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.TimerTask;

import okhttp3.Call;

import static com.lzhy.moneyhll.R.id.tv_next;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/10/24.
 * 忘记密码
 */

public class ForgetPassWordActivity extends MySwipeBackActivity implements View.OnClickListener {

    private TextView tvNext;
    private EditText edtCode;
    private EditText etPhoneNum;
    private Button tvGetCode;

    //-----------------------------------------------

    private   int i = 60;
    java.util.Timer timer;
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //handler处理消息
            if(msg.what < 60 && msg.what > 0){
                tvGetCode.setText("(" + msg.what + ")秒后重发");
                tvGetCode.setClickable(false);
            }
            if(msg.what == 0){
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
        setContentView(R.layout.activity_forget);
        initView();
        initTitlebar();
        addActivityCST(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        tvNext = (TextView) findViewById(tv_next);
        edtCode = (EditText) findViewById(R.id.edt_code);
        etPhoneNum = (EditText) findViewById(R.id.edt_phone_number);
        tvGetCode = (Button) findViewById(R.id.tv_getcode);

        tvNext.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        TextView register = (TextView) findViewById(R.id.right_text);
        register.setText("注册");
        register.setOnClickListener(this);
        titlebar.setLeftTextButton("返回",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("找回密码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //下一步
            case tv_next:
                if (etPhoneNum.getText().toString().equals("")){
                    Utils.toast(this,"手机号码为空");
                    return;
                }
                if(edtCode.getText().toString().length() != 6  ){
                    Utils.toast(this,"请您输入6位验证码");
                    return;
                }

                OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/doResetlogPwd")
                        .addParams("account", etPhoneNum.getText().toString()).addParams("sms", edtCode.getText().toString())
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("下一步URL:" + e);
                        Utils.toast(ForgetPassWordActivity.this,"网络异常");

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("下一步URL:" + response);
                        Type type = new TypeToken<Response1<LoginBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<LoginBean> resp = gson.fromJson(response, type);
                        String errCode = resp.getErrCode();
                        if (errCode == null){
                            return;
                        }
                        if (!errCode.equals("200")){
                            Utils.toast(ForgetPassWordActivity.this,resp.getMessage());
                            return;
                        }
                        Intent intent = new Intent(ForgetPassWordActivity.this,ResetPassWordActivity.class);
                        intent.putExtra("mobile",etPhoneNum.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            //注册
            case R.id.right_text:
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
                break;

            //获取验证码
            case R.id.tv_getcode:
                if(etPhoneNum.getText().toString().length()<=0){
                    Utils.toast(this,getString(R.string.please_input_tel));
                    return;
                }
                if(!Utils.isMobile(etPhoneNum.getText().toString())){
                    Utils.toast(this,getString(R.string.error_input_tel));
                    return;
                }

//                String getFindCodeUrl = UrlAPI.getFindCodeUrl(etPhoneNum.getText().toString());

                OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/sendEmsForResetPwd")
                        .addParams("account", etPhoneNum.getText().toString()).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("获取验证码:" + e);
                        Utils.toast(ForgetPassWordActivity.this,"验证码发送异常");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("获取验证码:" + response);
                        Type type = new TypeToken<Response1<LoginBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<LoginBean> resp = gson.fromJson(response, type);
                        if (! resp.getErrCode().equals("200")){
                            Utils.toast(ForgetPassWordActivity.this,resp.getMessage());
                            return;
                        }
                        Utils.toast(ForgetPassWordActivity.this,resp.getMessage());
                        myTimer();
                    }
                });
                break;
        }
    }

    /**
     * 定时器
     */
    private void myTimer(){
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

    /**
     * 显示dialog
     */
    private ProgressDialog dialog;
    protected void showLoading(){
        dialog = ProgressDialog.show(this, null, "数据正在加载，请稍候...", true, false);
    }

    /**
     * 隐藏dialog
     */
    protected void hideLoading() {
        if(null == dialog){
            return;
        }
        dialog.dismiss();
    }


    /**
     * 错误处理
     */
    protected void showEorror(Result result){
        if(null == result){
            Utils.toast(this,"网络异常，请检查您的网络状态");
            hideLoading();
            return;
        }
        Utils.toast(this,result.getMessage());
    }



}
