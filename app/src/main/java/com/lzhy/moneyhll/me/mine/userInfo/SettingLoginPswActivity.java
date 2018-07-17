package com.lzhy.moneyhll.me.mine.userInfo;

import android.os.Bundle;
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
import com.lzhy.moneyhll.me.mine.bean.UserLonginBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/11/2.
 * 设置登录密码（个人信息）
 */

public class SettingLoginPswActivity extends MySwipeBackActivity implements View.OnClickListener {
    //    private int memberId;
    private EditText etOldPsw;
    private EditText etNewPsw;
    private EditText etVreifyPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_login_psw);

        addActivityCST(this);
        initTitlebar();
        initView();

    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        TextView verifyRevamp = (TextView) findViewById(R.id.tv_vreify_revamp);
        etOldPsw = (EditText) findViewById(R.id.et_old_psw);
        etNewPsw = (EditText) findViewById(R.id.et_new_psw);
        etVreifyPsw = (EditText) findViewById(R.id.et_vreify_psw);
        verifyRevamp.setOnClickListener(this);
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"修改密码");
    }

    @Override
    public void onClick(View view) {
//        beforeClickInMine();
        if (etOldPsw.getText().toString().length() < 6) {
            Utils.toast(this, "请输入6~16位旧密码");
            return;
        }
        if (etNewPsw.getText().toString().length() < 6) {
            Utils.toast(this, "请输入6~16位新密码");
            return;
        }
        if (!etVreifyPsw.getText().toString().equals(etNewPsw.getText().toString())) {
            Utils.toast(this, "新密码和确认密码不一致");
            return;
        }

        final String oldPsw = Utils.getMD5Value(etOldPsw.getText().toString().trim());
        final String newPsw = Utils.getMD5Value(etNewPsw.getText().toString().trim());
        final String verifyPsw = Utils.getMD5Value(etVreifyPsw.getText().toString().trim());

        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/editPwd")
                .addHeader("accessToken", UserInfoModel.getInstance().token)
                .addParams("memberId", UserInfoModel.getInstance().getId() + "")
                .addParams("oldPwd", oldPsw)
                .addParams("newPwd", newPsw)
                .addParams("checkPwd", verifyPsw)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("登录密码URL:" + e);
                Utils.toast(SettingLoginPswActivity.this, "网络异常，修改失败");
            }

            @Override
            public void onResponse(String response, int id) {

                PrintLog.e("修改登+++++11111111111111111++++++录密码:" + UrlAPI.HOST_URL + "user/editPwd" + UserInfoModel.getInstance().getId() + oldPsw + newPsw + verifyPsw + UserInfoModel.getInstance().token);

                PrintLog.e("修改登+++++++++++录密码:" + response);
                Type type = new TypeToken<Response1<UserLonginBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<UserLonginBean> resp = gson.fromJson(response, type);
                String errCode = resp.getErrCode();

                if ("200".equals(errCode)) {
                    Utils.toast(SettingLoginPswActivity.this, resp.getMessage());
                    finish();
                    return;
                } else {
                    Utils.toast(SettingLoginPswActivity.this, resp.getMessage());
                }
            }
        });
    }
}
