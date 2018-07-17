package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.MarkerOrderModel;
import com.lzhy.moneyhll.wxapi.WXPayEntryActivity;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by Administrator on 2016/11/26 0026.
 * 创客商品订单
 */

public class MakerOrderHelper {

    private Context mContext;
    private View mView;

    private SimpleDraweeView mDraweeView;
    private TextView project_name;
    private TextView project_price;
    private TextView price_symbol;

    private TextView order_number;
    private TextView number;
    private TextView order_time;
    private TextView order_state;
    private Button to_pay;
    private MarkerOrderModel mModel;

    public MakerOrderHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        project_name = (TextView) mView.findViewById(R.id.project_name);
        project_price = (TextView) mView.findViewById(R.id.project_price);

        price_symbol = (TextView) mView.findViewById(R.id.price_symbol);
        order_number = (TextView) mView.findViewById(R.id.order_number);
        number = (TextView) mView.findViewById(R.id.number);
        order_time = (TextView) mView.findViewById(R.id.order_time);
        order_state = (TextView) mView.findViewById(R.id.order_state);
        to_pay = (Button) mView.findViewById(R.id.to_pay);
    }

    //我的创客商品订单
    public void  updateViewGoods(final MarkerOrderModel info) {
        this.mModel = info;
        setImage();
        setView();
        if (info.status == 1) {
            // 待支付1 ；待发货2；待收货3；已完结4；已失效5；
            price_symbol.setText("需付¥");
            to_pay.setVisibility(View.VISIBLE);
            order_state.setText("待支付");
        } else if (info.status == 2) {
            price_symbol.setText("已付¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("待发货");
        } else if (info.status == 3) {
            price_symbol.setText("已付¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("待收货");
        } else if (info.status == 4) {
            price_symbol.setText("已付¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("已签收");
        } else if (info.status == 5) {
            price_symbol.setText("¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("已失效");
        }else if (info.status == 8) {
            price_symbol.setText("已付¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("已完结");
        }
    }

    //我的创客游玩订单
    public void updateViewProject(final MarkerOrderModel info) {
        this.mModel = info;
        setImage();
        setView();
        //待支付1 ；待使用2；已完结3；已失效5；
        if (info.status == 1) {
            price_symbol.setText("需付¥");
            to_pay.setVisibility(View.VISIBLE);
            order_state.setText("待支付");
        } else if (info.status == 2) {
            price_symbol.setText("已付¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("待使用");
        } else if (info.status == 3) {
            price_symbol.setText("已付¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("已使用");
        } else if (info.status == 4) {
            price_symbol.setText("¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("已完结");
        }else if (info.status == 5) {
            price_symbol.setText("¥");
            to_pay.setVisibility(View.GONE);
            order_state.setText("已失效");
        }
    }

    private void setImage() {
        if (!StringUtils.isEmpty(mModel.picture1)) {
            mDraweeView.setImageURI(mModel.picture1);
        }
    }

    private void setView() {
        project_name.setText(mModel.projectName);
        project_price.setText(String.format("%.2f", mModel.totalAmount) + "");
        order_time.setText("下单时间：" + mModel.createTime);
        order_number.setText("订单号：" + mModel.orderCoding);
        number.setText("份数：" + mModel.quantity);

        to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WXPayEntryActivity.class);
                Bundle extras = new Bundle();
                extras.putString("orderNumber", mModel.orderCoding);
                extras.putDouble("totalPrice", mModel.totalAmount);
                extras.putString("description", mModel.projectName);
                extras.putString("orderId", mModel.id + "");
                extras.putString("flag", "makerorder");
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });
    }
}
