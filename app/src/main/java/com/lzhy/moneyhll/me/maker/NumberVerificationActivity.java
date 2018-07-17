package com.lzhy.moneyhll.me.maker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.dialog.VerificationResultsDialog;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.R.id.tv_verify_order;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by lzy on 2016/12/12.
 * 数字码核销
 */
public class NumberVerificationActivity extends MySwipeBackActivity implements View.OnClickListener {

    private EditText etNumberCode;
    private EditText etWhatSys;
    private TextView tvVerifyOrder;
    private ImageView iv_ke_fu;

    private VerificationResultsDialog mDialog;

    private String phoneurl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);

        addActivityCST(this);
        initTitlebar();
        initView();
    }

    private void initView() {
        etNumberCode = (EditText) findViewById(R.id.et_number_code);
        etWhatSys = (EditText) findViewById(R.id.et_what_sys);
        iv_ke_fu = (ImageView) findViewById(R.id.iv_ke_fu);

        tvVerifyOrder = (TextView) findViewById(tv_verify_order);
        tvVerifyOrder.setOnClickListener(this);
        iv_ke_fu.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("数字码确认");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_verify_order:
                if (etNumberCode.getText().length() <= 0) {
                    Toast.makeText(NumberVerificationActivity.this, "请输入数字码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etNumberCode.getText().length() < 12) {
                    Toast.makeText(NumberVerificationActivity.this, "请输入12位数字码", Toast.LENGTH_SHORT).show();
                    return;
                }
                VerificationResults();
                // 确认订单
                break;
            case R.id.iv_ke_fu:
                phoneurl = Constant.PHONE_KEFU;
                CommonUtil.doCallPhone(NumberVerificationActivity.this, phoneurl);
                break;
        }
    }

    private void VerificationResults() {
        //核销
        StringBuilder checkCode = new StringBuilder(etNumberCode.getText().toString());
        checkCode.insert(4, " ");
        checkCode.insert(9, " ");
        String verificationUrl = UrlAPI.getVerificationUrl(UserInfoModel.getInstance().getId(), checkCode.toString().trim());
        PrintLog.e("222222", checkCode.toString().trim());
        PrintLog.e("核销URL:" + verificationUrl);
        OkHttpUtils.get().url(verificationUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("核销onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                PrintLog.e("核销onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 resp = gson.fromJson(response, type);
                setResult(resp.getErrCode());
            }
        });
    }

    //核销结果处理
    private void setResult(String code) {
        final VerificationResultsDialog.Builder builder = new VerificationResultsDialog.Builder(NumberVerificationActivity.this);
        if ("200".equals(code)) {
            builder.setResId(R.mipmap.success_result);
            builder.setResId_bg(R.drawable.bg_bottom_green_r5dp);
            builder.setText("订单确认成功");
            builder.setText1("噢耶！又成功一笔订单");
        } else {
            builder.setResId(R.mipmap.failure_result);
            builder.setResId_bg(R.drawable.bg_bottom_rad_r5dp);
            builder.setText("订单确认失败");
            builder.setText1("别灰心，再试一下");
        }
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        mDialog.dismiss();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mDialog = null;
            }
        });
    }
}
