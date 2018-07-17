package com.lzhy.moneyhll.me.maker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
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

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by lzy on 2016/12/14.
 * 创客方商品订单详情
 */

public class GoodsOrderDetailsActivity extends MySwipeBackActivity implements View.OnClickListener {

    private TextView tvOrderNumber;
    private TextView tvCopies;
    private SimpleDraweeView ivImage;
    private TextView tvProject;
    private TextView tvPrice;
    private TextView price_symbol;
    private TextView tvOrderState;
    private TextView tvOrderTime;
    private TextView tvPeople;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvTS;
    private LinearLayout llVerify;
    private TextView tvVerify;
    private ImageView iv_ke_fu;
    private ImageView ivPhone;

    private String phoneurl;
    private BaseDialog dismiss;

    private MarkerOrderModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_order_detail);
        initTitlebar();
        initView();
        addActivityCST(this);
    }

    private void initView() {
        model = (MarkerOrderModel) getIntent().getExtras().getSerializable("MakerProjectModel");
        tvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
        tvCopies = (TextView) findViewById(R.id.tv_copies);

        ivImage = (SimpleDraweeView) findViewById(R.id.image);
        tvProject = (TextView) findViewById(R.id.project_name);
        tvPrice = (TextView) findViewById(R.id.project_price);
        price_symbol = (TextView) findViewById(R.id.price_symbol);

        tvOrderState = (TextView) findViewById(R.id.tv_order_state);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvPeople = (TextView) findViewById(R.id.tv_people);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvAddress = (TextView) findViewById(R.id.tv_address);

        tvTS = (TextView) findViewById(R.id.tv_ti_shi);
        iv_ke_fu = (ImageView) findViewById(R.id.iv_ke_fu);

        llVerify = (LinearLayout) findViewById(R.id.ll_deliver_goods);
        tvVerify = (TextView) findViewById(R.id.tv_deliver_goods);

        ivPhone = (ImageView) findViewById(R.id.iv_phone);
        ivPhone.setOnClickListener(this);
        tvVerify.setOnClickListener(this);
        iv_ke_fu.setOnClickListener(this);

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
        price_symbol.setText("已付¥");
        tvOrderTime.setText(model.reserveTime);
        tvPeople.setText(model.receiverName);
        tvPhone.setText(model.telephone);
        tvTS.setVisibility(View.VISIBLE);
//  待支付1 ；待发货2；待收货3；已签收4；已失效5；已完结8；
        switch (model.status) {
            case 1:
                tvOrderState.setText("待支付");
                tvTS.setVisibility(View.GONE);
                llVerify.setVisibility(View.GONE);
                break;
            case 2:
                tvOrderState.setText("待发货");
                tvTS.setVisibility(View.GONE);
                llVerify.setVisibility(View.VISIBLE);
                tvVerify.setText("发货");
                break;
            case 3:
                tvOrderState.setText("已发货");
                tvTS.setText("若顾客未及时确认收货，系统会在7天后自动确认哦~");
                llVerify.setVisibility(View.GONE);
                break;
            case 4:
                tvOrderState.setText("已签收");
                tvTS.setVisibility(View.GONE);
                llVerify.setVisibility(View.GONE);
                //确认订单完结
                //llVerify.setVisibility(View.VISIBLE);
                tvVerify.setText("完结订单");
                break;
            case 5:
                tvOrderState.setText("已失效");
                tvTS.setVisibility(View.GONE);
                llVerify.setVisibility(View.GONE);
                break;
            case 8:
                tvOrderState.setText("已完结");
                tvTS.setVisibility(View.GONE);
                llVerify.setVisibility(View.GONE);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //发货
            case R.id.tv_deliver_goods:
                if ("发货".equals(tvVerify.getText().toString())) {
                    if (dismiss == null) {
                        final BaseDialog.Builder builder = new BaseDialog.Builder(GoodsOrderDetailsActivity.this);
                        builder.setMessage("商品已发货？");
                        builder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //确认发货
                                        sureSend();
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
                } else if ("完结订单".equals(tvVerify.getText().toString())) {
                    if (dismiss == null) {
                        final BaseDialog.Builder builder = new BaseDialog.Builder(GoodsOrderDetailsActivity.this);
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
                }
                break;

            //打电话
            case R.id.iv_phone:
                phoneurl = "tel:" + model.telephone;

                CommonUtil.doCallPhone(GoodsOrderDetailsActivity.this, phoneurl);
                break;
            case R.id.iv_ke_fu:
                phoneurl = Constant.PHONE_KEFU;

                CommonUtil.doCallPhone(GoodsOrderDetailsActivity.this, phoneurl);
                break;
        }
    }

    private void sureSend() {
        String sureSendUrl = UrlAPI.getSureSendUrl(model.id);
        PrintLog.e("确认发货URL:" + sureSendUrl);
        OkHttpUtils.get().url(sureSendUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("确认发货onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("确认发货onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();

                Gson gson = new Gson();
                Response2 resp = gson.fromJson(response, type);
                if ("200".equals(resp.getErrCode())) {
                    tvOrderState.setText("已发货");
                    llVerify.setVisibility(View.GONE);
                    tvTS.setVisibility(View.VISIBLE);
                    tvTS.setText("若顾客未及时确认收货，系统会在7天后自动确认哦~");
                    Toast.makeText(GoodsOrderDetailsActivity.this, "已发货", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(GoodsOrderDetailsActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
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
                    tvOrderState.setText("已完结");
                    Toast.makeText(GoodsOrderDetailsActivity.this, "已完结", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(GoodsOrderDetailsActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
}
