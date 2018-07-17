package com.lzhy.moneyhll.me.mine.give;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.GiveInfo;
import com.lzhy.moneyhll.me.mine.bean.UesrBean;
import com.lzhy.moneyhll.me.mine.bean.User;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.Base64;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.lzhy.moneyhll.utils.Utils;
import com.ta.utdid2.android.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by cmm on 2016/10/26.
 * 赠送，填写赠送人
 */
public class GiveActivity extends MySwipeBackActivity implements View.OnClickListener {

    private EditText etFrendNum;
    private TextView tvFinsh;
    private String giftAccount;

    private SimpleDraweeView ivHead;
    private TextView tvRoll;
    private TextView tvPearls;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        initView();
        initInfo();
        initTitlebar();

    }

    @Override
    protected void onResume() {
        disparityLogin();
        super.onResume();
        initInfo();

    }
    private void initInfo() {
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
                .addHeader("accessToken", UserInfoModel.getInstance().token)
                .addParams("req", Base64.getBase64(param))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("个2人信息URL:" + e);
                UtilToast.getInstance().showDragonError("网络异常，个人信息加载失败！") ;            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("获取个人信息URL:" + response);
                response = Base64.getFromBase64(response);
                Type type = new TypeToken<Response1<UesrBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<UesrBean> resp = gson.fromJson(response, type);
                String errCode = resp.getErrCode();
                if (errCode == null) {
                    return;
                }
                if (!errCode.equals("200")) {
                    Utils.toast(GiveActivity.this, resp.getMessage());
                    finish();
                    UserInfoModel.getInstance().clear();
                    UserInfoModel.getInstance().sync();
                    return;
                }
                User user = resp.getData().user;
                //格式化double型,取整
                DecimalFormat d = new DecimalFormat("0");

                ivHead.setImageURI(user.avatar);
                tvRoll.setText(user.carTicket + "");
                tvPearls.setText(d.format(user.pearls) + "");
                if (!StringUtils.isEmpty(user.nickName))
                    name.setText(user.nickName);
            }
        });
    }

    private void initView() {
        tvFinsh = (TextView) findViewById(R.id.tv_finsh);
        etFrendNum = (EditText) findViewById(R.id.et_frend_number);
        name = (TextView) findViewById(R.id.name);
        tvFinsh.setOnClickListener(this);

        ivHead = (SimpleDraweeView) findViewById(R.id.iv_head_portrait);
        tvRoll = (TextView) findViewById(R.id.tv_motor_number);
        tvPearls = (TextView) findViewById(R.id.tv_long_zhu_number);
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("赠送给好友");
        titlebar.setRightText("记录", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GiveActivity.this, GiveRecordActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {
        giftAccount = etFrendNum.getText().toString().trim();
        if (giftAccount.length() <= 0) {
            UtilToast.getInstance().showDragonInfo(getString(R.string.please_input_tel)); ;

            return;
        }
        if (!Utils.isMobile(giftAccount)) {
            UtilToast.getInstance().showDragonInfo(getString(R.string.error_input_tel)) ;
            return;
        }

        String giveNextUrl = UrlAPI.getGiveNextUrl(giftAccount, UserInfoModel.getInstance().getId());

        PrintLog.e("下一步URL:" + giveNextUrl);
        OkHttpUtils.get().url(giveNextUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("下一步URL:" + e);
                UtilToast.getInstance().showDragonInfo("网络异常") ;

            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("下一步URL:" + response);
                Type type = new TypeToken<Response1<GiveInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<GiveInfo> resp = gson.fromJson(response, type);
                String errCode = resp.getErrCode();
                if (errCode == null) {
                    return;
                }

                if (!errCode.equals("200")) {
                    Utils.toast(GiveActivity.this, resp.getMessage());
                    return;
                }

                Intent intent = new Intent(GiveActivity.this, GiveFriendActivity.class);
                intent.putExtra("giftAccount", giftAccount);
                intent.putExtra("avatar", resp.getData().avatar);
                intent.putExtra("realName", resp.getData().realName);
                startActivity(intent);
            }
        });
    }
}
