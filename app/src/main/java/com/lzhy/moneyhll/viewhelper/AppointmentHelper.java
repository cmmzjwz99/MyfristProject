package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.dialog.BaseDialog;
import com.lzhy.moneyhll.model.AppointmentModel;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.lzhy.moneyhll.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.api.UrlAPI.getcancelOrderUrl;


/**
 * Created by Administrator on 2016/11/15 0015.
 */

public class AppointmentHelper {

    private Context mContext;
    private View mView;
    private int mAtate;

    private TextView order_number;
    private Button state;
    private SimpleDraweeView image;
    private TextView branch_name;
    private TextView car_number;
    private TextView car_address;
    private TextView phone_phone;
    private TextView get_user_name;
    private TextView set_user_name;
    private TextView get_time;
    private TextView set_time;

    private LinearLayout button_linear_one;
    private TextView tv_pay_number;
    private TextView tv_damages_money;
    private TextView tv_should_pay_money;
    private TextView tv_service_money;
    private TextView tv_should_pay_money_service;
    private TextView need_money;
    private Button bt_cancel_order;
    private Button to_pay;

    private LinearLayout button_linear_two;
    private LinearLayout linear;
    private TextView pay_number;
    private TextView damages_money;
    private TextView service_money;
    private TextView all_money;
    private Button cancel_order;
    public int type1 = 0;//1取消订单

    private TextView tishi1;
    private TextView tishi2;

    private BaseDialog dismiss;

    public AppointmentHelper(Context context, View view, int atate) {
        mContext = context;
        mView = view;
        mAtate = atate;
        findView();
    }

    private void findView() {
        order_number = (TextView) mView.findViewById(R.id.order_number);
        state = (Button) mView.findViewById(R.id.state);
        image = (SimpleDraweeView) mView.findViewById(R.id.image);
        branch_name = (TextView) mView.findViewById(R.id.branch_name);
        car_number = (TextView) mView.findViewById(R.id.car_number);
        car_address = (TextView) mView.findViewById(R.id.car_address);
        phone_phone = (TextView) mView.findViewById(R.id.phone_phone);
        get_user_name = (TextView) mView.findViewById(R.id.get_user_name);
        set_user_name = (TextView) mView.findViewById(R.id.set_user_name);
        get_time = (TextView) mView.findViewById(R.id.get_time);
        set_time = (TextView) mView.findViewById(R.id.set_time);
        button_linear_one = (LinearLayout) mView.findViewById(R.id.button_linear_one);
        tv_pay_number = (TextView) mView.findViewById(R.id.tv_pay_number);
        tv_damages_money = (TextView) mView.findViewById(R.id.tv_damages_money);
        tv_should_pay_money = (TextView) mView.findViewById(R.id.tv_should_pay_money);
        tv_service_money = (TextView) mView.findViewById(R.id.tv_service_money);
        tv_should_pay_money_service = (TextView) mView.findViewById(R.id.tv_should_pay_money_service);
        need_money = (TextView) mView.findViewById(R.id.need_money);
        bt_cancel_order = (Button) mView.findViewById(R.id.bt_cancel_order);
        to_pay = (Button) mView.findViewById(R.id.to_pay);
        button_linear_two = (LinearLayout) mView.findViewById(R.id.button_linear_two);
        pay_number = (TextView) mView.findViewById(R.id.pay_number);
        damages_money = (TextView) mView.findViewById(R.id.damages_money);
        service_money = (TextView) mView.findViewById(R.id.service_money);
        all_money = (TextView) mView.findViewById(R.id.all_money);
        cancel_order = (Button) mView.findViewById(R.id.cancel_order);

        tishi1 = (TextView) mView.findViewById(R.id.tishi1);
        tishi2 = (TextView) mView.findViewById(R.id.tishi2);

        linear = (LinearLayout) mView.findViewById(R.id.linear);
        linear.setVisibility(View.GONE);
        if (mAtate == 9) {
            button_linear_one.setVisibility(View.VISIBLE);
            button_linear_two.setVisibility(View.GONE);
        } else {
            button_linear_one.setVisibility(View.GONE);
            button_linear_two.setVisibility(View.VISIBLE);
            if (mAtate != 3) {
                cancel_order.setVisibility(View.GONE);
            } else {
                cancel_order.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateView(final AppointmentModel b) {
        order_number.setText("订单号：" + b.orderId);
        branch_name.setText("所属网点：" + b.area);
        if (b.imageUrl != null) {
            image.setImageURI(b.imageUrl);
        } else {
            image.setImageURI("");
        }
        car_number.setText("车牌号码：" + b.carNum);
        car_address.setText(b.areaAdd);
        phone_phone.setText(b.areaTel);
        get_user_name.setText(b.mentionCar);
        set_user_name.setText(b.returnCar);
        get_time.setText(b.startTime);
        set_time.setText(b.endTime);
        if (mAtate == 9) {
            tv_pay_number.setText(b.usePrice + "元");
            tv_damages_money.setText(b.margin + "元");
            tv_should_pay_money.setText(b.marginPay + "元");
            tv_service_money.setText(b.serve + "元");
            tv_should_pay_money_service.setText(b.servePay + "元");
            need_money.setText(b.needPay + "元");
        } else {
            pay_number.setText(b.usePrice + "元");
            damages_money.setText(b.margin + "元");
            service_money.setText(b.serve + "元");
            all_money.setText(b.servePay + "元");
        }

        switch (b.status) {
            case 9:
                state.setText("代缴清订单");
                tishi1.setText("您还需支付相关费用，支付完成后才能正常提车出行。");
                break;
            case 3:
                state.setText("待提车订单");
                tishi2.setText("请您带上提车人的身份证、驾驶员的驾驶证。办理手续和交接车辆以及熟悉车辆操作需要一个小时左右，" +
                        "请您出行前提前到达预约提车点。");
                break;
            case 4:
                state.setText("旅途中订单");
                tishi2.setText("您的房车已提取，现已在行程中，请注意形式安全。");
                break;
            case 5:
                state.setText("待结算订单");
                tishi2.setText("由于您在房车使用中造成房车受损，需结算受损部分金额。");
                break;
            case 6:
                state.setText("已完结订单");
                tishi2.setText("您的订单已完成，欢迎您的下次预约。");
                break;
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dismiss == null) {
                    final BaseDialog.Builder builder = new BaseDialog.Builder(mContext);
                    builder.setMessage("取消该预约会造成一定损失，详情请咨询客服。");

                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    cancelOrder(b.orderId, b.status);
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
        };
        cancel_order.setOnClickListener(listener);
        bt_cancel_order.setOnClickListener(listener);
        to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WXPayEntryActivity.class);

                Bundle extras = new Bundle();
                extras.putString("orderNumber", b.orderId);
                extras.putDouble("totalPrice", b.needPay);
                extras.putString("description", b.carName);
                extras.putString("orderId", b.id + "");
                extras.putString("flag", "appointmentorder");
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });
    }

    private void cancelOrder(String orderId, int status) {
        String orderUrl = getcancelOrderUrl(orderId, status);
        PrintLog.e("取消预约URL:" + orderUrl);
        OkHttpUtils.get().url(orderUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("取消预约:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("取消预约:" + response);
                Type type = new TypeToken<Response1<ListModel<AppointmentModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<AppointmentModel>> resp = gson.fromJson(response, type);
                if ("200".equals(resp.getErrCode())) {
                    UtilToast.getInstance().showDragonSuccess("取消成功");
//                    if (mOnClickListener != null) {
//                        type1 = 1;
//                        mOnClickListener.onClick(null);
//                    }
                }
            }
        });
    }

//    private View.OnClickListener mOnClickListener;
//
//    public void setOnUpdate(View.OnClickListener listener) {
//        mOnClickListener = listener;
//    }
}
