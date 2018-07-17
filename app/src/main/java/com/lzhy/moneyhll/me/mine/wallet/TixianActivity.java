package com.lzhy.moneyhll.me.mine.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;
import com.lzhy.moneyhll.utils.UtilToast;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/10/26.
 * 提现
 */
public class TixianActivity extends MySwipeBackActivity implements View.OnClickListener {

    private TextView tvMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian);

        addActivityCST(this);
        initView();
        initTitlebar();
        initData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        initData();
        disparityLogin();
    }

    private void initView() {
        tvMoney = (TextView) findViewById(R.id.tv_money);
        TextView tv_ti_xian = (TextView) findViewById(R.id.tv_ti_xian);
        TextView tv_ti_xian_record = (TextView) findViewById(R.id.tv_ti_xian_record);

        tv_ti_xian.setOnClickListener(this);
        tv_ti_xian_record.setOnClickListener(this);
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"提现申请");
    }


    private void initData() {
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
                PrintLog.e("个人1信息URL:" + e);
                Utils.toast(TixianActivity.this, "网络异常，个人信息加载失败");
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
                    Utils.toast(TixianActivity.this, resp.getMessage());
                    return;
                }
                DecimalFormat d = new DecimalFormat("0");
                try {
                    String withDrawType = SharePrefenceUtils.getString(TixianActivity.this, "withDrawType");
                    if (withDrawType.equals("1")) {
                        String getInCome = SharePrefenceUtils.getString(TixianActivity.this, "getInCome");
                        tvMoney.setText(getInCome);
                    }
                    if (withDrawType.equals("2") || withDrawType.equals("")) {
                        tvMoney.setText(CommonUtil.FloattoInt(data.user.coins));
                    }
                } catch (Exception e) {
                    SharePrefenceUtils.put(TixianActivity.this, "withDrawType", "0");
                    tvMoney.setText(d.format(data.user.coins) + "");
                }

                PrintLog.e("resp.getData().noPayPwd-----------:" + resp.getData().noPayPwd);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ti_xian:
                if ((Double.valueOf(tvMoney.getText().toString())) > 0) {
                    Intent intent = new Intent(this, TiXianApplyActivity.class);
                    startActivity(intent);
                } else {
                    UtilToast.getInstance().showDragonInfo("您没有可提现的收益");
                }
                break;
            case R.id.tv_ti_xian_record:
                Intent intent1 = new Intent(this, TiXianRecordActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
