package com.lzhy.moneyhll.me.mine.give;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.dialog.PayWadDialog;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.GiveInfo;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.UtilCheckIDCard.isNumeric;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by cmm on 2016/11/10.
 * 赠送龙珠或房车劵
 */
public class GiveFriendActivity extends Activity implements View.OnClickListener {

    private EditText etPearNum;
    private EditText etCouponNum;
    private TextView tvGiftAccount;
    private String couponNum;
    private String pearNum;

    private String giftAccount;
    private TextView tvName;
    private String passWord;
    private PayWadDialog dismiss;
    private InputMethodManager imm;
    private TextView tvGive;
    private String avatar,realName;
    private SimpleDraweeView iv_head_portrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_friend);
        initView();
        initTitlebar();
    }

    private void initView() {
        Intent intent = getIntent();

        avatar = intent.getStringExtra("avatar");
        giftAccount = intent.getStringExtra("giftAccount");
        realName=intent.getStringExtra("realName");
        imm = (InputMethodManager) getSystemService(GiveFriendActivity.this.INPUT_METHOD_SERVICE);
        tvGiftAccount = (TextView) findViewById(R.id.tv_gift_account);
        tvName = (TextView) findViewById(R.id.tv_name);
        etPearNum = (EditText) findViewById(R.id.et_pear_num);
        etCouponNum = (EditText) findViewById(R.id.et_couponNum);
        tvGive = (TextView) findViewById(R.id.tv_give);
        tvGiftAccount.setText(giftAccount);
        tvName.setText(realName);
        iv_head_portrait = (SimpleDraweeView) findViewById(R.id.iv_head_portrait);
        if (avatar != null) {
            iv_head_portrait.setImageURI(avatar);
        } else {
            iv_head_portrait.setImageURI("");
        }
        tvGive.setOnClickListener(this);
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
    }


    @Override
    public void onClick(View view) {
        pearNum = etPearNum.getText().toString().trim();
        couponNum = etCouponNum.getText().toString().trim();
        if (pearNum.isEmpty() && couponNum.isEmpty()) {
            Utils.toast(this, "赠送数量为空");
            return;
        }
        if (!isNumeric(couponNum) || ! isNumeric(pearNum)) {
            return;
        }

        if (pearNum.equals("0")) {
            Utils.toast(this, "赠送数量不能为0");
            return;
        }
        if (couponNum.equals("0")) {
            Utils.toast(this, "房车卷数量不能为0");
            return;
        }
        showSetPswDialog();
    }


    private void showSetPswDialog() {

        if (UserInfoModel.getInstance().payPwd == null) {
            Utils.toast(this, "您还没有支付密码，请先设置支付密码");
            return;
        }

        if (dismiss == null) {
            final PayWadDialog.Builder builder = new PayWadDialog.Builder(GiveFriendActivity.this);
            builder.setWatcher(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() == 6) {
                        passWord = editable.toString();
                        giveResult();
                        dismiss.dismiss();
                    }
                }
            });
            dismiss = builder.create();
            dismiss.show();
        } else {
            dismiss.show();
        }
        dismiss.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                imm.hideSoftInputFromWindow(((View) tvGive.getParent()).getWindowToken(), 0);
            }
        });
    }

    private void giveResult() {

        String md5Value = Utils.getMD5Value(passWord);
        PrintLog.e("赠送-------------------URL:" + md5Value);

        String giveUrl = UrlAPI.getGiveUrl(UserInfoModel.getInstance().account, giftAccount, pearNum, couponNum, md5Value);
        PrintLog.e("赠送URL:" + giveUrl);
        PrintLog.e("赠送====================URL:" + giveUrl);
        OkHttpUtils.get().url(giveUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("赠送URL:" + e);
                Utils.toast(GiveFriendActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("赠送URL:" + response);
                dismiss = null;
                Type type = new TypeToken<Response1<GiveInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<GiveInfo> resp = gson.fromJson(response, type);
                String errCode = resp.getErrCode();
                if (errCode == null) {
                    return;
                }

                if (!errCode.equals("200")) {
                    Utils.toast(GiveFriendActivity.this, resp.getMessage());
                    return;
                }

                Utils.toast(GiveFriendActivity.this, resp.getMessage());
                finish();
                UserInfoModel.getInstance().sync();
            }
        });
    }

    @Override
    protected void onResume() {
        disparityLogin();
        super.onResume();
    }
}
