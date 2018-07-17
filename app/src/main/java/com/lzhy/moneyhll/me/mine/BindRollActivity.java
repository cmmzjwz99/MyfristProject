package com.lzhy.moneyhll.me.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.event.EventBindRoll;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.BindRollInfo;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.Fangchequan;
import static com.lzhy.moneyhll.constant.Constant.SACN_CLICK_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/10/26.
 * 绑定房车劵
 */
public class BindRollActivity extends MySwipeBackActivity implements View.OnClickListener {
    private EditText etRN;
    private EditText etRCN;
    private ImageView scan_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_roll);

        addActivityCST(this);
        initTitlebar();
        initView();
        //注册EventBus
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        etRN = (EditText) findViewById(R.id.et_roll_number);
        etRCN = (EditText) findViewById(R.id.et_roll_code_number);
        TextView tvBind = (TextView) findViewById(R.id.tv_bind);
        scan_iv = (ImageView) findViewById(R.id.scan_iv);

        scan_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BindRollActivity.this, ScanActivity.class);
                intent.putExtra("int", Fangchequan);
                startActivity(intent);
            }
        });
        tvBind.setOnClickListener(this);
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("绑定房车券");
    }

    @Override
    public void onClick(View view) {
        CommonUtil.setViewAlphaAnimation(view);
        String rollNum = etRN.getText().toString().trim();
        String rollCode = etRCN.getText().toString().trim();
        if (TextUtils.isEmpty(rollCode) || TextUtils.isEmpty(rollNum)) {
            Utils.toast(this, "房车券号码或激活码为空");
            return;
        }

        String getBindRollUrl = UrlAPI.getBindRollUrl(rollNum, rollCode, UserInfoModel.getInstance().getAccount());

        PrintLog.e("绑定房车卷URL:" + getBindRollUrl);
        OkHttpUtils.get().url(getBindRollUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("绑定房车券URL:" + e);
                Utils.toast(BindRollActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("绑定房车卷URL:" + response);

                Type type = new TypeToken<Response1<BindRollInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<BindRollInfo> resp = gson.fromJson(response, type);
                String errCode = resp.getErrCode();
                if (errCode == null) {
                    return;
                }

                if (!errCode.equals("200")) {
                    setCustomStatisticsKV(BindRollActivity.this, SACN_CLICK_ID,"绑定房车券失败");//统计
                    Utils.toast(BindRollActivity.this, resp.getMessage());
                    return;
                }
                setCustomStatisticsKV(BindRollActivity.this, SACN_CLICK_ID,"绑定房车券成功");//统计
                Utils.toast(BindRollActivity.this, resp.getMessage());
            }
        });
    }
    @Subscribe
    public void onEventMainThread(EventBindRoll event) {
        String number = event.getNumber();
        String numberCode = event.getNumberCode();
        if (number != null && numberCode != null) {
            int i = number.indexOf("?");
            etRN.setText(number.substring(i + 1, number.length()));
        }
        if (numberCode != null) {
            etRCN.setText(numberCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册EventBus
        EventBus.getDefault().unregister(this);
    }
}
