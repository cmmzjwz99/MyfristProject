package com.lzhy.moneyhll.me.loginOrRegister;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.manager.ActivityManagerCST;
import com.lzhy.moneyhll.me.loginOrRegister.bean.LoginBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.lzhy.moneyhll.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

import static com.lzhy.moneyhll.LtApplication.driveId;
import static com.lzhy.moneyhll.LtApplication.isOtherDevice;
import static com.lzhy.moneyhll.constant.Constant.LOCATION_CODE;
import static com.lzhy.moneyhll.constant.Constant.LOGIN_CLICK_ID;
import static com.lzhy.moneyhll.home.MainActivity.tabHome;
import static com.lzhy.moneyhll.home.MainActivity.tabMine;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.CommonUtil.setViewAlphaAnimation;

/**
 * Created by cmm on 2016/11/19.
 */

/************************************************************
 * @Author; 龙之游 @ xu 596928539@qq.com
 * 时间:2016/12/20 19:22
 * 注释: 该类应该提供登录相关的功能  密码登录和验证码登录 还有注册
 ************************************************************/

public final class LoginActivity extends MySwipeBackActivity implements View.OnClickListener {

    private TextView tv_login;
    private TextView tv_forget_password;
    private EditText et_phoneNum;
    private EditText et_password;


    private LinearLayout llAccount;
    private LinearLayout llPhone;
    private EditText mobile;
    private Button getcode;
    private EditText code;
    private RadioButton account_login;
    private RadioButton phone_fast_login;
    private InputMethodManager imm;

    //private String driveId;     // = "13481583546588558877912154852"

    private int i = 60;
    private Timer timer;


    private Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //handler处理消息
            if (msg.what < 60 && msg.what > 0) {
                getcode.setText("(" + msg.what + ")秒后重发");
                getcode.setClickable(false);
            }
            if (msg.what == 0) {
                //在handler里可以更改UI组件
//                finish();
                getcode.setClickable(true);
                getcode.setText("获取验证码");
                timer.cancel();
                i = 60;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //隐藏标题栏
        addActivityCST(this);
        //申请权限
        if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE)) {

            repuestPermission(LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE);
        }else {
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            driveId = TelephonyMgr.getDeviceId();
//            Log.i("onClick", "onCreate: "+driveId);
        }

        initView();
        initTitlebar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfoModel.getInstance().clear();
    }

    //初始化头
    private void initTitlebar() {
        final BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setTitle("账号登录");
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOtherDevice && tabMine.isChecked()) {
                    ActivityManagerCST.AppExit();
                    tabHome.setChecked(true);
                } else {
                    finish();
                }
            }
        });
        titlebar.setRightText("注册", this);
    }


    //初始化登陆界面
    private void initView() {

        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        mobile = (EditText) findViewById(R.id.edt_mobile);
        getcode = (Button) findViewById(R.id.tv_getcode1);
        code = (EditText) findViewById(R.id.edt_code);

        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        et_phoneNum = (EditText) findViewById(R.id.edt_acount_phone_num);
        et_password = ((EditText) findViewById(R.id.login_password));

        llAccount = (LinearLayout) findViewById(R.id.ll_account);
        llPhone = (LinearLayout) findViewById(R.id.ll_phone);

        account_login = (RadioButton) findViewById(R.id.account_login);
        phone_fast_login = (RadioButton) findViewById(R.id.phone_fast_login);
        RadioGroup rg_login = (RadioGroup) findViewById(R.id.rg_login);

        account_login.setOnClickListener(this);
        phone_fast_login.setOnClickListener(this);
        getcode.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);

        et_password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    tv_login.performClick();
                    return true;
                }
                return false;
            }
        });
    }


    //点击登录 或 忘记密码
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //账号登陆
            case R.id.account_login:
                llAccount.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.GONE);
                break;

            //手机快捷登陆
            case R.id.phone_fast_login:
                llAccount.setVisibility(View.GONE);
                llPhone.setVisibility(View.VISIBLE);
                break;

            //注册
            case R.id.right_text:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            //登录
            case R.id.tv_login:
                setViewAlphaAnimation(tv_login);
                login();
                break;

            //忘记密码
            case R.id.tv_forget_password:
                startActivity(new Intent(this, ForgetPassWordActivity.class));
                break;

            //获取验证码
            case R.id.tv_getcode1:
                setViewAlphaAnimation(getcode);
                sendCode();
                break;
        }
    }


    /**
     * 发送验证码
     */
    private void sendCode() {
        if (mobile.getText().toString().length() <= 0) {
            Utils.toast(this, getString(R.string.please_input_tel));
            return;
        }
        if (!Utils.isMobile(mobile.getText().toString())) {
            Utils.toast(this, getString(R.string.error_input_tel));
            return;
        }

        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/sendSms")
                .addParams("phoneNum", mobile.getText().toString())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("获取验证码:" + e);
                Utils.toast(LoginActivity.this, "短信验证码发送失败");
            }

            @Override
            public void onResponse(String response, int id) {

                Type type = new TypeToken<Response1<LoginBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<LoginBean> resp = gson.fromJson(response, type);

                if (!resp.getErrCode().equals("200")) {
                    Utils.toast(LoginActivity.this, resp.getMessage());
                } else {
                    Utils.toast(LoginActivity.this, resp.getMessage());
                    myTimer();
                }
            }
        });
    }

    /**
     * 验证码 定时器
     */
    private void myTimer() {
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

        }, 10, 1000);
    }

    /**
     * 登录
     */
    private void login() {
        final String phoneNum = et_phoneNum.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        String etMobile = mobile.getText().toString().trim();
        String etCode = code.getText().toString().trim();

        //账号登陆
        if (account_login.isChecked()) {
            if (phoneNum.length() <= 0 || password.length() <= 0) {
                Utils.toast(this, "您的账号或密码为空！");
                return;
            }
            if (!Utils.isMobile(phoneNum)) {
                Utils.toast(this, getString(R.string.error_input_tel));
                return;
            }
            if (password.length() < 6) {
                Utils.toast(this, "请输入6~16位密码");
                return;
            }
            LoginBean loginBean = new LoginBean();
            long time = Utils.getNowTimeTemp();

            loginBean.setTime(time);
            loginBean.setDevice(Constant.DEVICE);
            loginBean.setAccount(phoneNum);
            //密码md5加密
            String password1 = Utils.getMD5Value(password);
            OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/login")
                    .addParams("account", phoneNum)
                    .addParams("pwd", password1)
                    .addParams("driveId", driveId)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Utils.toast(LoginActivity.this, "网络异常");
                }

                @Override
                public void onResponse(String response, int id) {
                    PrintLog.e("登录" + response);
                    Log.i("userInfoq", "onResponse: "+response);
                    Type type = new TypeToken<Response1<LoginBean>>() {
                    }.getType();
                    Gson gson = new Gson();
                    Response1<LoginBean> resp = gson.fromJson(response, type);
                    if (resp.getErrCode() == null) {
                        return;
                    }
                    if (!resp.getErrCode().equals("200")) {
                        Utils.toast(LoginActivity.this, resp.getMessage());
                        return;
                    }
                    Utils.toast(LoginActivity.this, resp.getMessage());//登录成功提示
                    UserInfoModel.getInstance().copy(resp.getData());
                    UserInfoModel.getInstance().sync();
                    center();

                }

            });
            return;
        }

        //手机快捷登陆
        if (etCode.equals("") || etMobile.equals("")) {
            Utils.toast(this, "手机号码或验证码为空");
            return;
        }
        if (!Utils.isMobile(etMobile)) {
            Utils.toast(this, getString(R.string.error_input_tel));
            return;
        }
        if (etCode.length() != 6) {
            Utils.toast(this, "请输入6位验证码");
            return;
        }

        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/loginForPhone")
                .addParams("phoneNum", etMobile)
                .addParams("sms", etCode)
                .addParams("driveId", driveId)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("登陆:" + e);
                        UtilToast.getInstance().showDragonError("网络异常") ;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("登录" + response);
                        try {
                            Type type = new TypeToken<Response1<LoginBean>>() {
                            }.getType();
                            Gson gson = new Gson();
                            Response1<LoginBean> resp = gson.fromJson(response, type);
                            String errCode = resp.getErrCode();
                            if (errCode == null) {
                                MobclickAgent.onProfileSignOff();
                                return;
                            }
                            if (!errCode.equals("200")) {
                                UtilToast.getInstance().showDragonInfo(resp.getMessage());
                                return;
                            }
                            Utils.toast(LoginActivity.this, resp.getMessage());
                            UserInfoModel.getInstance().copy(resp.getData());
                            UserInfoModel.getInstance().sync();
                            Log.i("login_after", "onResponse: "+UserInfoModel.getInstance().getUserId());
                            center();
                        } catch (Exception e) {
                        }
                    }
                });
    }


    /**
     * 跳转到非会员中心界面
     */

    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/21 15:07
     * 注释: 走的都是这个方法
     ************************************************************/
    public void center() {
//        starUpLoginCheck();//登录成功后 启动定时任务
        isOtherDevice = false;
        setCustomStatisticsKV(this, LOGIN_CLICK_ID, "用户登录");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            /************************************************************
             *修改者;  龙之游 @ xu 596928539@qq.com
             *修改时间:2016/12/22 11:47
             *bug: 用户被挤掉后 返回  会停留在
             *修复: 如果没有登陆则信息清空  或者根据状态判断 强制用户重启或登录
             ************************************************************/

            // TODO_XBB: 2016/12/22  返回键bug  用户被挤掉后 返回  会停留在
            if (tabMine.isChecked() && isOtherDevice) {
//                Log.i("onKeyDown", "onKeyDown: ");
                tabHome.performClick();
                finish();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                driveId = TelephonyMgr.getDeviceId();
            } else {
                Toast.makeText(this, "请开启权限", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    driveId = TelephonyMgr.getDeviceId();
                } else {

                    Toast.makeText(this, "请先开启相关权限，此权限不涉及用户隐私", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

}
