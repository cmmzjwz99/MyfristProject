package com.lzhy.moneyhll.me.loginOrRegister;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.me.mine.bean.UserLonginBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by cmm on 2016/10/25.
 */

public final class ResetPassWordActivity extends MySwipeBackActivity implements View.OnClickListener {

    private CheckBox cbPwdiv;
    private TextView tvFinish;
    private EditText etConfirmPwd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);

        initView();
        initTitlebar();
        addActivityCST(this);
        tvFinish.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        etConfirmPwd = (EditText) findViewById(R.id.ed_pwd);
        cbPwdiv = (CheckBox) findViewById(R.id.cb_pwdiv);
        cbPwdiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    etConfirmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    etConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        tvFinish = (TextView) findViewById(R.id.tv_finsh);
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
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

        String confirmPwd = etConfirmPwd.getText().toString();

        if(confirmPwd.length()<=0){
            Utils.toast(this,"密码不能为空");
            return;
        }
        if(confirmPwd.length()<6){
            Utils.toast(this,"密码不能少于6位");
            return;
        }
        if(confirmPwd.length()>16){
            Utils.toast(this,"密码不能多于16位");
            return;
        }


        //获取ForgetPassWordActivity的电话号码

        String md5Value = Utils.getMD5Value(confirmPwd);

        PrintLog.e("md5Value:" + md5Value);

        String phoneNum = getIntent().getStringExtra("mobile");
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/ResetlogPwd")
                .addParams("mobile", phoneNum)
                .addParams("pwd", md5Value)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("确认URL:" + e);
                Utils.toast(ResetPassWordActivity.this,"网络异常");

            }
            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("确认URL:" + response);


                Type type = new TypeToken<Response1<UserLonginBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<UserLonginBean> resp = gson.fromJson(response, type);
                String errCode = resp.getErrCode();
                if (errCode == null){
                    return;
                }
                if (!errCode.equals("200")){
                    Utils.toast(ResetPassWordActivity.this, resp.getMessage());
                    return;
                }

                Utils.toast(ResetPassWordActivity.this, resp.getMessage());

                try{
                    Thread.sleep(1000);
                    finish();
                } catch (Exception e){

                }


            }
        });


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
