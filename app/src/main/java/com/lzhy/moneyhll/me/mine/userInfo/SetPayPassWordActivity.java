package com.lzhy.moneyhll.me.mine.userInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.lzhy.moneyhll.utils.Base64;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.CommonUtil.setViewAlphaAnimation;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/11/11.
 * 设置支付密码界面
 */
public class SetPayPassWordActivity extends MySwipeBackActivity implements View.OnClickListener {

    private LinearLayout psw;
    private EditText edNewPayPsw;
    private EditText edVerifyPayPsw;
    private EditText edOldPayPsw;
    private BaseTitlebar titlebar;
    private TextView tvVerifyRe;
    private TextView tvResetPayPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pay_psw);

        addActivityCST(this);
        initUserInfo();
        initTitlebar();
        initView();
    }

    private void initView() {
        psw = (LinearLayout) findViewById(R.id.ll_old_pay_pwd);
        edOldPayPsw = (EditText) findViewById(R.id.et_old_pay_psw);
        edNewPayPsw = (EditText) findViewById(R.id.et_new_pay_psw);
        edVerifyPayPsw = (EditText) findViewById(R.id.et_vreify_pay_psw);
        tvVerifyRe = (TextView) findViewById(R.id.tv_vreify_revamp);
        tvResetPayPwd = (TextView) findViewById(R.id.tv_reset_pay_pwd);
    }


    //初始化标题
    private void initTitlebar() {
        titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"修改密码");
    }

    //初始化布局
    private void setListenr() {
        //设置支付密码
        tvVerifyRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewAlphaAnimation(tvVerifyRe);
//                beforeClickInMine();
                String newPayPsw = Utils.getMD5Value(edNewPayPsw.getText().toString());
                String verifyPayPsw = Utils.getMD5Value(edVerifyPayPsw.getText().toString());
                if ( edNewPayPsw.getText().length() == 0 || edVerifyPayPsw.getText().length() == 0) {
                    UtilToast.getInstance().showDragonInfo("密码不能为空");
                    return;
                }

                if (edNewPayPsw.getText().toString().length()!= 6 ) {
                    UtilToast.getInstance().showDragonInfo("请输入6位支付密码");
                    return;
                }
                if (edVerifyPayPsw.getText().toString().length()!= 6 ) {
                    UtilToast.getInstance().showDragonInfo("请输入6位支付密码");
                    return;
                }
                if (!newPayPsw.equals(verifyPayPsw)) {
                    UtilToast.getInstance().showDragonInfo("密码不一致");
                    return;
                }

                OkHttpUtils.post().url(UrlAPI.HOST_URL+ "user/setPayPwd")
                        .addHeader("accessToken" , UserInfoModel.getInstance().token)
                        .addParams("memberId" , UserInfoModel.getInstance().getId() + "")
                        .addParams("payPwd" , newPayPsw)
                        .addParams("checkPayPwd" , verifyPayPsw)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("设置成功:" + e);
                        UtilToast.getInstance().showDragonInfo("网络异常，设置失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("设置成功:" + response);
                        Type type = new TypeToken<Response1<UesrBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<UesrBean> resp = gson.fromJson(response, type);
                        String errCode = resp.getErrCode();
                        if (errCode == null){
                            return;
                        }
                        if (!errCode.equals("200")){
                            Utils.toast(SetPayPassWordActivity.this, resp.getMessage());
                            return;
                        }
                        UtilToast.getInstance().showDragonSuccess("resp.getMessage()") ;
                        UserInfoModel.getInstance().setPayPwd(edNewPayPsw.getText().toString());
                        UserInfoModel.getInstance().sync();

                        PrintLog.e(UserInfoModel.getInstance().getPayPwd()+edNewPayPsw.getText().toString());

                        try {
                            Thread.sleep(1000);
                            finish();
                        }catch (Exception e){

                        }
                    }
                });
            }
        });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //修改支付密码
            case R.id.tv_vreify_revamp:

                String  oldPayPsw = Utils.getMD5Value(edOldPayPsw.getText().toString());
                String newPayPsw = Utils.getMD5Value(edNewPayPsw.getText().toString());
                String verifyPayPsw = Utils.getMD5Value(edVerifyPayPsw.getText().toString());

                /************************************************************
                 *修改者;  龙之游 @ xu 596928539@qq.com
                 *修改时间:2016/12/17 16:34
                 *修复bug:  对 "" 加密后的长度位32  没有做“”值的判断
                 ************************************************************/
                if (edOldPayPsw.getText().toString().length() == 0 || edNewPayPsw.getText().toString().length() == 0 || edVerifyPayPsw.getText().toString().length() == 0) {
                    UtilToast.getInstance().showDragonSuccess("密码不能为空") ;

                    return;
                }

                if (edNewPayPsw.getText().toString().length()!= 6 ) {
                    UtilToast.getInstance().showDragonSuccess("请输入6位支付密码") ;
                    return;
                }
                if (edVerifyPayPsw.getText().toString().length()!= 6 ) {
                    UtilToast.getInstance().showDragonSuccess("请输入6位支付密码") ;
                    return;
                }
                if (!newPayPsw.equals(verifyPayPsw)) {
                    UtilToast.getInstance().showDragonSuccess("密码不一致") ;
                    return;
                }

//                Log.i("xxx", "onClick: -----------------"+Constant.HOST_URL);
                OkHttpUtils.post().url(UrlAPI.HOST_URL+ "user/editPayPwd")
                        .addParams("memberId" , UserInfoModel.getInstance().getId() + "")
                        .addParams("oldPayPwd" , oldPayPsw) .addParams("newPayPwd" , newPayPsw)
                        .addParams("checkPayPwd" , verifyPayPsw)
                        .addHeader("accessToken" , UserInfoModel.getInstance().token)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("个人信息保存URL:" + e);
                        UtilToast.getInstance().showDragonError("网络异常，修改失败"); ;
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Type type = new TypeToken<Response1<UesrBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<UesrBean> resp = gson.fromJson(response, type);
                        String errCode = resp.getErrCode();
                        if ("200".equals(errCode)) {
                            UtilToast.getInstance().showDragonSuccess(resp.getMessage()) ;
                            UserInfoModel.getInstance().setPayPwd(edNewPayPsw.getText().toString());
                            UserInfoModel.getInstance().sync();
                            PrintLog.e(UserInfoModel.getInstance().getPayPwd()+edNewPayPsw.getText().toString());
                            finish();
                            return;
                        } else {
                            UtilToast.getInstance().showDragonError(resp.getMessage()) ;
                        }
                    }
                });
                break;


            //重置支付密码
            case R.id.tv_reset_pay_pwd:
                startActivity(new Intent(this, ResetPayPwdActivity.class));
                break;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    //获取用户信息
    private void initUserInfo() {
        /****************
         *修改者:  ycq
         *修改时间: 2017.01.09
         *修改原因: 接口用户信息加密
         * Describe:param  传递的参数
                    Base64.getBase64(param)  参数加密
                    Base64.getFromBase64(response)  返回数据解密
         ****************/
        JSONObject param = new JSONObject();
        try {
            param.put("memberId", UserInfoModel.getInstance().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "v1/user/userInfo")
                .addParams("req", Base64.getBase64(param))
                .addHeader("accessToken" , UserInfoModel.getInstance().token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("4个人信息URL:" + e);
//                Utils.toast(getContext(), "服务器异常，个人信息加载失败");
            }

            @Override
            public void onResponse(String response, int id) {
                response=Base64.getFromBase64(response);
                PrintLog.e("获取个人信息URL:" + response);
                Type type = new TypeToken<Response1<UesrBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<UesrBean> resp = gson.fromJson(response, type);
                UesrBean data = resp.getData();
                String errCode = resp.getErrCode();

                if (errCode == null) {
                    return;
                }
                if (!errCode.equals("200")) {
                    UtilToast.getInstance().showDragonError(resp.getMessage()) ;
                    return;
                }

                try {
                    if (data.noPayPwd.equals("")) {
                        UtilToast.getInstance().showDragonError("您还没有支付密码") ;
                        titlebar.setTitle("设置密码");
                        psw.setVisibility(View.GONE);
                        tvResetPayPwd.setVisibility(View.GONE);
                        tvVerifyRe.setText("确  认");
                        setListenr();
                    }
                }catch (Exception e){
                    UtilToast.getInstance().showDragonInfo("修改支付密码"); ;
                    psw.setVisibility(View.VISIBLE);
                    tvResetPayPwd.setVisibility(View.VISIBLE);
                    tvVerifyRe.setText("确认重置");
                    tvVerifyRe.setOnClickListener(SetPayPassWordActivity.this);
                    tvResetPayPwd.setOnClickListener(SetPayPassWordActivity.this);
                }

                UserInfoModel.getInstance().setPayPwd(data.user.paypwd);
                UserInfoModel.getInstance().sync();
            }
        });
    }
}
