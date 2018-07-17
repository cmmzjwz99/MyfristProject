package com.lzhy.moneyhll.me.mine.wallet;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.gowhere.WherePlaySelectProAdapter;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.TiXianBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;
import com.lzhy.moneyhll.utils.Utils;
import com.ta.utdid2.android.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilCheckMix.editTextCheck;

/**
 * Created by cmm on 2016/10/27.
 * 填写提现信息
 */
public class TiXianApplyActivity extends MySwipeBackActivity implements View.OnClickListener {

    protected ListView mListView;
    private Drawable drawableUp, drawableDown;

    private List<String> banks = new ArrayList<>();
    private String[] openes = new String[]{"工商银行", "农业银行", "建设银行", "中国银行", "交通银行", "招商银行", "平安银行"};
    private String pro = "";
    private TextView btnSelectPro;
    private PopupWindow mWhere;
    private View popView;
    private TextView tvBankBranch,tv_cancel,tv_choose;
    private TextView tvName;
    private TextView tvBankNumber;
    private TextView tvAmount;
    private String withDrawType;

    private String bankBranch;
    private String name;
    private String bankNumber;
    private String bankName;
    private String amountNumber;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        addActivityCST(this);
        initView();
        initPopWindows();
        initTitlebar();
    }

    private void initView() {
        withDrawType = getIntent().getStringExtra("withDrawType");
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_choose = (TextView) findViewById(R.id.tv_choose);
        tvBankBranch = (TextView) findViewById(R.id.et_information);
        tvName = (TextView) findViewById(R.id.et_user_name);
        tvBankNumber = (TextView) findViewById(R.id.et_user_number);
        tvAmount = (TextView) findViewById(R.id.et_min_number);
        tv_cancel.setOnClickListener(this);
        tv_choose.setOnClickListener(this);
        drawableUp = getResources().getDrawable(R.mipmap.ic_up);
        drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        drawableDown = getResources().getDrawable(R.mipmap.ic_down);
        drawableDown.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        btnSelectPro = (TextView) findViewById(R.id.tv_select_pro);
        btnSelectPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelectPro.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableUp, null);
                mWhere.showAsDropDown(btnSelectPro);
            }
        });


    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"提现申请");
    }

    //初始化适配器
    private void initPopWindows() {
        banks.clear();
        Collections.addAll(banks, openes);
        popView = LayoutInflater.from(this).inflate(R.layout.ppw_band, null);
        NoScorllGridView gridView = (NoScorllGridView) popView.findViewById(R.id.gridview);
        gridView.setNumColumns(1);
        final WherePlaySelectProAdapter adapter = new WherePlaySelectProAdapter(TiXianApplyActivity.this, banks);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pro = adapter.clearSelect(i);
                btnSelectPro.setText(pro);
                mWhere.dismiss();
            }
        });


        mWhere = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mWhere.setBackgroundDrawable(new BitmapDrawable());

        mWhere.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btnSelectPro.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableDown, null);
            }
        });
    }

    @Override
    public void onClick(View v) {
//        beforeClickInMine();
        switch (v.getId()) {
            case R.id.tv_choose: //确认
                bankName = btnSelectPro.getText().toString();
                bankBranch = tvBankBranch.getText().toString();
                name = tvName.getText().toString();
                bankNumber = tvBankNumber.getText().toString();
                amountNumber = tvAmount.getText().toString();
                /****************
                 *修改者:  ycq
                 *修改时间: 2017.01.09
                 *修改原因: 输入信息限制判定
                 ****************/
                if (StringUtils.isEmpty(bankName) || StringUtils.isEmpty(name) || StringUtils.isEmpty(bankNumber) || StringUtils.isEmpty(amountNumber)) {
                    Utils.toast(TiXianApplyActivity.this, "您输入的信息不完整");
                    return;
                }
                amount = Double.parseDouble(amountNumber);


                if (!editTextCheck(bankBranch, TiXianApplyActivity.this,"开户行网点信息不能输入非法字符"))return;
                if (!editTextCheck(name, TiXianApplyActivity.this,"名字不能输入非法字符")) return;

                if (amount < 1) {
                    Utils.toast(TiXianApplyActivity.this, "提现最低金额为1");
                    return;
                }

                String typeUrl = "";
                String withDrawType = SharePrefenceUtils.getString(TiXianApplyActivity.this, "withDrawType");

                if (withDrawType.equals("1")) {
                    String getCashUrl = UrlAPI.getCashUrl(UserInfoModel.getInstance().getId(), UserInfoModel.getInstance().serviceStatus,
                            bankName, bankBranch, name, bankNumber, amount);
                    typeUrl = getCashUrl;

                }
                if (withDrawType.equals("2") || withDrawType.equals("0")) {
                    String getWithDrawUrl = UrlAPI.getWithDrawUrl(UserInfoModel.getInstance().getId(), UserInfoModel.getInstance().serviceStatus,
                            bankName, bankBranch, name, bankNumber, amount);
                    typeUrl = getWithDrawUrl;
                }

                PrintLog.e("提------------现URL:" + typeUrl);
                OkHttpUtils.get().url(typeUrl).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("提**********现:" + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("提-----------------现:" + response);
                        Type type = new TypeToken<Response1<TiXianBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<TiXianBean> resp = gson.fromJson(response, type);
                        if ("龙币提现申请成功".equals(resp.getMessage()) || "服务商提现申请成功".equals(resp.getMessage())) {
                            Utils.toast(TiXianApplyActivity.this, resp.getMessage());
                            finish();
                        } else {
                            Utils.toast(TiXianApplyActivity.this, resp.getMessage());
                        }
                    }
                });

                break;
            case R.id.tv_cancel://取消
                finish();
                break;

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
}
