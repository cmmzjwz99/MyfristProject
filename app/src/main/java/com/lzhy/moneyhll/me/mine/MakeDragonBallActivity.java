package com.lzhy.moneyhll.me.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.MakeBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckIDCard.IDCardValidate;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 我要赚龙珠
 */
public class MakeDragonBallActivity extends MySwipeBackActivity {
    private BaseTitlebar mTitlebar;

    private EditText name, phone, id_number, referrer_number;
    private Button but_sure, but_cancel;
    View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_make_dragon_ball);

        addActivityCST(this);
        initView();
        initInfo();
        initTitlebar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    //初始化用户信息
    private void initInfo() {
        String getUserMakeInfoUrl = UrlAPI.getUserMakeInfoUrl(UserInfoModel.getInstance().account);
        PrintLog.e("我要赚龙珠用户信息URL:" + getUserMakeInfoUrl);
        OkHttpUtils.get().url(getUserMakeInfoUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("我要赚龙珠用户信息URL:" + e + call.toString());
                Utils.toast(MakeDragonBallActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("我要赚龙珠用户信息URL onResponse:" + response);
                Type type = new TypeToken<Response1<MakeBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<MakeBean> resp = gson.fromJson(response, type);
                if (!resp.getErrCode().equals("200")) {
                    Utils.toast(MakeDragonBallActivity.this, resp.getMessage());
                    return;
                }
                name.setText(resp.getData().name);
                phone.setText(resp.getData().mobile);

                PrintLog.e("我要赚龙珠--------------手机号码:" + phone);

            }
        });
    }

    private void initView() {
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
//        phone.setText(UserInfoModel.getInstance().account);
        id_number = (EditText) findViewById(R.id.id_number);
        referrer_number = (EditText) findViewById(R.id.referrer_number);
        but_sure = (Button) findViewById(R.id.but_sure);
        but_cancel = (Button) findViewById(R.id.but_cancel);
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
        but_cancel.setOnClickListener(listener);
        but_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSureClick();
            }
        });
    }

    private void setSureClick() {

        if (name.getText().toString().length() == 0 ) {
            Toast.makeText(MakeDragonBallActivity.this, "请您输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        String tempIdCard = id_number.getText().toString();
        if (!IDCardValidate(tempIdCard)) {
            return;
        }
        if (referrer_number.getText().toString().length() == 0) {
            Toast.makeText(MakeDragonBallActivity.this, "请输入推荐人龙码", Toast.LENGTH_SHORT).show();
            return;
        }
        SureMakeData();
    }

    private void SureMakeData() {
        String makeDragonBallUrl = UrlAPI.getMakeDragonBallUrl(UserInfoModel.getInstance().getAccount(), name.getText().toString(),
                id_number.getText().toString(), referrer_number.getText().toString());
        PrintLog.e("我要赚龙珠URL:" + makeDragonBallUrl);
        OkHttpUtils.get().url(makeDragonBallUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("我要赚龙珠onError:" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("我要赚龙珠onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 response2 = gson.fromJson(response, type);

                if (!response2.getErrCode().equals("200")){
                    Utils.toast(MakeDragonBallActivity.this,response2.getMessage());
                    return;
                }

                UserInfoModel.getInstance().setServiceStatus(1);
                UserInfoModel.getInstance().sync();

                try{
                    Thread.sleep(1000);
                    finish();
                }catch (Exception e){

                }

            }
        });
    }

    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"我要赚龙珠");
    }
}
