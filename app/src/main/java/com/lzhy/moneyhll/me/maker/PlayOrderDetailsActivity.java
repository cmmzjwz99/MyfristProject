package com.lzhy.moneyhll.me.maker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.dialog.BaseDialog;
import com.lzhy.moneyhll.model.MarkerOrderModel;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.R.id.tv_copies;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by lzy on 2016/12/14.
 * 创客方游玩类型订单
 */

public class PlayOrderDetailsActivity extends MySwipeBackActivity implements View.OnClickListener {

    private TextView tvOrderNumber;
    private TextView tvCopies;
    private SimpleDraweeView ivImage;
    private TextView tvProject;
    private TextView tvPrice;
    private TextView priceSymbol;
    private TextView tvOrderTime;
    private TextView tvPeople;
    private TextView tvPhone;
    private ImageView ivPhone;
    private TextView tvTS;
    private ImageView iv_ke_fu;
    private TextView tv_deliver_goods;

    private MarkerOrderModel model;
    private String phoneurl;
    private BaseDialog dismiss;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_order_details);

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
        model = (MarkerOrderModel) getIntent().getExtras().getSerializable("MakerProjectModel");
        tvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
        tvCopies = (TextView) findViewById(tv_copies);

        ivImage = (SimpleDraweeView) findViewById(R.id.image);
        tvProject = (TextView) findViewById(R.id.project_name);
        tvPrice = (TextView) findViewById(R.id.project_price);
        priceSymbol = (TextView) findViewById(R.id.price_symbol);

        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvPeople = (TextView) findViewById(R.id.tv_people);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        ivPhone = (ImageView) findViewById(R.id.iv_phone);
        iv_ke_fu = (ImageView) findViewById(R.id.iv_ke_fu);
        tv_deliver_goods = (TextView) findViewById(R.id.tv_deliver_goods);

        tvTS = (TextView) findViewById(R.id.tv_ti_shi);
        ivPhone.setOnClickListener(this);
        iv_ke_fu.setOnClickListener(this);
        tv_deliver_goods.setOnClickListener(this);

        upDataView();
    }

    //填充控件
    private void upDataView() {
        tvOrderNumber.setText(model.orderCoding);
        tvCopies.setText("份数：" + model.quantity);
        if (model.picture1 != null) {
            ivImage.setImageURI(model.picture1);
        }
        tvProject.setText(model.projectName);
        tvPrice.setText(model.payAmount + "");
        tvOrderTime.setText(model.reserveTime);
        tvPeople.setText(model.realName);
        tvPhone.setText(model.account);
        iv_ke_fu.setVisibility(View.GONE);
        tv_deliver_goods.setVisibility(View.GONE);
        switch (model.status) {
            //待支付1 ；待确认2；已确认3；已完结4；已失效5；
            case 1:
                priceSymbol.setText("待支付¥");
                tvTS.setText("");
                break;
            case 2:
                priceSymbol.setText("已付¥");
                tvTS.setText("请使用扫一扫功能核销顾客订单");
                break;
            case 3:
                priceSymbol.setText("已付¥");
                tvTS.setText("");
                //确认订单完结
                //tv_deliver_goods.setVisibility(View.VISIBLE);
                break;
            case 4:
                priceSymbol.setText("¥");
                tvTS.setText("");
                break;
            case 5:
                priceSymbol.setText("需付¥");
                tvTS.setText("30分钟未支付，订单已失效");
                break;
        }
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("订单详情");
    }

    //打电话
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_phone:
                phoneurl = "tel:" + model.telephone;

                CommonUtil.doCallPhone(PlayOrderDetailsActivity.this, phoneurl);
                break;
            case R.id.iv_ke_fu:

                CommonUtil.doCallPhone(PlayOrderDetailsActivity.this, phoneurl);
                break;
            case R.id.tv_deliver_goods:
                if (dismiss == null) {
                    final BaseDialog.Builder builder = new BaseDialog.Builder(PlayOrderDetailsActivity.this);
                    builder.setMessage("确认订单完结？");
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //确认完结
                                    sureEnd();
                                    dismiss.dismiss();
                                }
                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dismiss.dismiss();
                                }
                            });
                    dismiss = builder.create();
                    dismiss.show();
                } else {
                    dismiss.show();
                }
                break;
        }
    }

    private void sureEnd() {
        String sureSendUrl = UrlAPI.getSureSendUrl(model.id);
        PrintLog.e("确认完结URL:" + sureSendUrl);
        OkHttpUtils.get().url(sureSendUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("确认完结onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("确认完结onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();

                Gson gson = new Gson();
                Response2 resp = gson.fromJson(response, type);
                if ("200".equals(resp.getErrCode())) {
                    tv_deliver_goods.setVisibility(View.GONE);
                    Toast.makeText(PlayOrderDetailsActivity.this, "已完结", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PlayOrderDetailsActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
