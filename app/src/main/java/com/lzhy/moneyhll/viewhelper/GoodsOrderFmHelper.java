package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.MarkerOrderModel;

/**
 * Created by Administrator on 2016/12/13 0013.
 */

public class GoodsOrderFmHelper {
    private Context mContext;
    private View mView;

    private TextView tv_order_number;
    private TextView tv_copies;
    private TextView name;
    private TextView tv_bumber;
    private TextView tv_time;

    private TextView colour;

    public GoodsOrderFmHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        tv_order_number = (TextView) mView.findViewById(R.id.tv_order_number);
        tv_copies = (TextView) mView.findViewById(R.id.tv_copies);
        name = (TextView) mView.findViewById(R.id.name);
        tv_bumber = (TextView) mView.findViewById(R.id.tv_bumber);
        tv_time = (TextView) mView.findViewById(R.id.tv_time);
        colour = (TextView) mView.findViewById(R.id.colour);
    }

    //我是创客 商品订单
    public void updateViewGoods(MarkerOrderModel info) {
        tv_order_number.setText(info.orderCoding);
        name.setText(info.projectName);
        tv_bumber.setText("份数：" + info.quantity);
        tv_time.setText("顾客下单时间：" +info.reserveTime);
        switch (info.status) {
            //  待支付1 ；待发货2；已发货；已完结4；已失效5；未支付取消订单6； 退款取消订单7；
            case 1:
                tv_copies.setText("待支付");
                colour.setBackgroundColor(0xffff4723);
                break;
            case 2:
                tv_copies.setText("待发货");
                colour.setBackgroundColor(0xff0aabea);
                break;
            case 3:
                tv_copies.setText("已发货");
                colour.setBackgroundColor(0xfff6ce8a);
                break;
            case 4:
                tv_copies.setText("已签收");
                colour.setBackgroundColor(0xff00c285);
                break;
            case 5:
                tv_copies.setText("已失效");
                colour.setBackgroundColor(0xffcccccc);
                break;
            case 8:
                tv_copies.setText("已完结");
                colour.setBackgroundColor(0xffcccccc);
                break;
        }

    }

    //我是创客 游玩订单
    public void updateViewPlay(MarkerOrderModel info) {
        tv_order_number.setText(info.orderCoding);
        name.setText(info.projectName);
        tv_bumber.setText("份数：" + info.quantity);
        tv_time.setText("顾客下单时间：" + info.reserveTime);
        switch (info.status) {
            //待支付1 ；待确认；已完结3；已失效4；未支付取消订单5； 退款取消订单6；
            case 1:
                tv_copies.setText("待支付");
                colour.setBackgroundColor(0xffff4723);
                break;
            case 2:
                tv_copies.setText("待确认");
                colour.setBackgroundColor(0xff0aabeb);
                break;
            case 3:
                tv_copies.setText("已使用");
                colour.setBackgroundColor(0xff00c285);
                break;
            case 4:
                tv_copies.setText("已完结");
                colour.setBackgroundColor(0xfff6ce8a);
                break;
            case 5:
                tv_copies.setText("已失效");
                colour.setBackgroundColor(0xffcccccc);
                break;
        }

    }
}
