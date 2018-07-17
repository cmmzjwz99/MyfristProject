package com.lzhy.moneyhll.me.makerorder;

import android.content.DialogInterface;
import android.os.Bundle;
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

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by lzy on 2016/12/9.
 * 创客商品订单详情
 */

public class MakerGoodsOrderDetailActivity extends MySwipeBackActivity implements View.OnClickListener {

    private TextView tvOrderNumber;
    private TextView tvCopies;
    private SimpleDraweeView ivImage;
    private TextView tvProjectName;
    private TextView tvProjectPrice;
    private TextView price_symbol;

    private TextView tvOrderState;
    private TextView tvOrderTime;
    private TextView tvPeople;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvTS;
    private LinearLayout llVerify;
    private ImageView iv_ke_fu;

    private MarkerOrderModel mModel;
    private BaseDialog dismiss;
    private String phoneurl;
    public int type1 = 0;//1确认收货

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipt_order);
        initTitlebar();
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    //初始化布局
    private void initView() {
        mModel = (MarkerOrderModel) getIntent().getExtras().getSerializable("MakerGoodsModel");

        tvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
        tvCopies = (TextView) findViewById(R.id.tv_copies);

        ivImage = (SimpleDraweeView) findViewById(R.id.image);
        tvProjectName = (TextView) findViewById(R.id.project_name);
        tvProjectPrice = (TextView) findViewById(R.id.project_price);
        price_symbol = (TextView) findViewById(R.id.price_symbol);

        tvOrderState = (TextView) findViewById(R.id.tv_order_state);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvPeople = (TextView) findViewById(R.id.tv_people);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvAddress = (TextView) findViewById(R.id.tv_address);

        tvTS = (TextView) findViewById(R.id.tv_ti_shi);
        iv_ke_fu = (ImageView) findViewById(R.id.iv_ke_fu);

        llVerify = (LinearLayout) findViewById(R.id.ll_verify);
        llVerify.setOnClickListener(this);
        iv_ke_fu.setOnClickListener(this);
        upDataView();
    }

    private void upDataView() {
        tvOrderNumber.setText("订单号：" + mModel.orderCoding);
        tvCopies.setText("份数：" + mModel.quantity);
        if (mModel.picture1 != null) {
            ivImage.setImageURI(mModel.picture1);
        }
        tvProjectName.setText(mModel.projectName);
        tvProjectPrice.setText(String.format("%.2f", mModel.totalAmount) + "");

        price_symbol.setText("已付¥");
        tvOrderTime.setText(mModel.createTime);

        tvPeople.setText(mModel.receiverName);
        tvPhone.setText(mModel.telephone);
        tvAddress.setText(mModel.expressAddress);
        iv_ke_fu.setVisibility(View.VISIBLE);
        switch (mModel.status) {
            case 2:
                iv_ke_fu.setVisibility(View.GONE);
                tvOrderState.setText("待发货");
                llVerify.setVisibility(View.GONE);
                tvTS.setText("创客正在发货请耐心等候...");
                break;
            case 3:
                tvTS.setVisibility(View.VISIBLE);
                tvOrderState.setText("待收货");
                tvTS.setText("7天后订单自动确认收货，如果有问题，请联系客服。");
                break;
            case 4:
                llVerify.setVisibility(View.GONE);
                tvTS.setVisibility(View.GONE);
                tvOrderState.setText("已签收");
                break;
            case 8:
                llVerify.setVisibility(View.GONE);
                tvTS.setVisibility(View.GONE);
                tvOrderState.setText("已完结");
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
            //确认收货
            case R.id.ll_verify:
                if (dismiss == null) {
                    final BaseDialog.Builder builder = new BaseDialog.Builder(MakerGoodsOrderDetailActivity.this);
                    builder.setMessage("确认收货？");

                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //确认收货
                                    SureReceiveGoods(mModel.id);
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
            case R.id.iv_ke_fu:
                phoneurl = Constant.PHONE_KEFU;

                CommonUtil.doCallPhone(MakerGoodsOrderDetailActivity.this, phoneurl);
                break;
        }
    }

    //确认收货状态更改
    private void SureReceiveGoods(final int orderId) {
        String sureReceiveUrl = UrlAPI.getSureReceiveUrl(orderId);
        PrintLog.e("确认收货URL:" + sureReceiveUrl);
        OkHttpUtils.get().url(sureReceiveUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("确认收货:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("确认收货:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 resp = gson.fromJson(response, type);
                if ("200".equals(resp.getErrCode())) {
                    tvOrderState.setText("已签收");
                    llVerify.setVisibility(View.GONE);
                    tvTS.setVisibility(View.GONE);
                    Toast.makeText(MakerGoodsOrderDetailActivity.this, "确认签收", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MakerGoodsOrderDetailActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
