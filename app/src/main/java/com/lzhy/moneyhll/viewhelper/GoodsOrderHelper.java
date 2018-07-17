package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.dialog.BaseDialog;
import com.lzhy.moneyhll.custom.dialog.ReturnedGoodsDialog;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.model.GoodsOrderModel;
import com.lzhy.moneyhll.model.PayGoodsModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.STATUS_BARTER;
import static com.lzhy.moneyhll.constant.Constant.STATUS_BE_OVER;
import static com.lzhy.moneyhll.constant.Constant.STATUS_CANCLE;
import static com.lzhy.moneyhll.constant.Constant.STATUS_DELIVERED;
import static com.lzhy.moneyhll.constant.Constant.STATUS_DRAWBACK;
import static com.lzhy.moneyhll.constant.Constant.STATUS_PAY_SUCCESS;
import static com.lzhy.moneyhll.constant.Constant.STATUS_SIGNED;
import static com.lzhy.moneyhll.constant.Constant.STATUS_TERMINATED;
import static com.lzhy.moneyhll.constant.Constant.STATUS_WAIT_DELIVER;
import static com.lzhy.moneyhll.constant.Constant.STATUS_WAIT_PAY;
import static com.lzhy.moneyhll.constant.Constant.STATUS_WAIT_SIGN;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public class GoodsOrderHelper {
    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/18 17:37
     * 注释:  数字改为常量  文表意
     ************************************************************/

    private Context mContext;
    private View mView;

    private TextView order_number;
    private TextView after_sales;
    private TextView goods_price;
    private TextView goods_number;
    private TextView goods_describe;
    private TextView text;
    private SimpleDraweeView mDraweeView;
    private View parent;

    private TextView sure_receive;
    private TextView to_pay;
    private GoodsOrderModel b;

    private BaseDialog dismiss;
    private ReturnedGoodsDialog ReturneDismiss;

    public int type1 = 0;//1确认收货 2取消订单 3退货

    public GoodsOrderHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        order_number = (TextView) mView.findViewById(R.id.order_number);
        after_sales = (TextView) mView.findViewById(R.id.after_sales11);

        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        goods_describe = (TextView) mView.findViewById(R.id.goods_describe);
        goods_price = (TextView) mView.findViewById(R.id.goods_price);
        goods_number = (TextView) mView.findViewById(R.id.goods_number);

        text = (TextView) mView.findViewById(R.id.text);
        parent = (View) text.getParent();

        sure_receive = (TextView) mView.findViewById(R.id.sure_receive);
        to_pay = (TextView) mView.findViewById(R.id.to_pay);

    }

    public void updateView(final GoodsOrderModel model) {
        this.b = model;

        parent.setVisibility(View.VISIBLE);
        sure_receive.setVisibility(View.VISIBLE);
        after_sales.setVisibility(View.VISIBLE);
        to_pay.setVisibility(View.VISIBLE);

        order_number.setText("订单号：" + b.orderCoding);
        String all_price = "订单价格: ";
        if (model.cashPrice > 0) {
            all_price = all_price + model.cashPrice + "人民币+";
        }
        if (model.coinPrice > 0) {
            all_price = all_price + model.coinPrice + "龙币+";
        }
        if (model.pearlPrice > 0) {
            all_price = all_price + model.pearlPrice + "龙珠";
        }
        if (all_price.endsWith("+"))
            all_price = all_price.substring(0, all_price.length() - 1);

        if (model.cashPrice <= 0 && model.coinPrice <= 0 && model.pearlPrice <= 0) {
            all_price = "";
        }
        if(model.promotionPrice>0){
            goods_price.setText("秒杀价: "+ model.promotionPrice + "龙珠");
        }else{
            goods_price.setText(all_price);
        }

        goods_number.setText("×" + b.quantity);
        goods_describe.setText(b.title);
        if (b.imageUrl != null) {
            mDraweeView.setImageURI(Uri.parse(b.imageUrl));
        } else {
            mDraweeView.setImageURI("");
        }

        setListenr(model);

        /*****************************************************
         * 部门 ：龙之游 @ 技术部
         * @Author xu 安卓开发工程师
         * 时间：   2016/12/18 0018 17:08
         * 修改： 定义规范化，订单状态  待发货4  代签收18  已签收20 已完结8 退换货16  数字字母化
         *****************************************************/
        switch (b.status) {
            case STATUS_PAY_SUCCESS://1支付成功
                to_pay.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                text.setText("您已付款，请等待订单处理");
                after_sales.setText("取消");
                break;
            case STATUS_WAIT_PAY://待支付2
                sure_receive.setVisibility(View.GONE);
                text.setText("你还未支付，请支付人民币余款");
                after_sales.setText("取消");
                break;
            case STATUS_WAIT_DELIVER://待发货4
                after_sales.setText("取消");
                to_pay.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                text.setText("亲爱的小伙伴，我们正在发货，请耐心等待哦！");
                break;
            case STATUS_DELIVERED://已发货6
                to_pay.setVisibility(View.GONE);
                after_sales.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                text.setText("已发货，请注意查收！");
                break;
            case STATUS_BE_OVER:// 已完结8
                to_pay.setVisibility(View.GONE);
                after_sales.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                text.setTextColor(Color.BLACK);
                text.setText("订单已完结");
                break;
            case STATUS_TERMINATED://已终止10
                to_pay.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                after_sales.setVisibility(View.GONE);
                text.setText("订单已退款");
                break;
            case STATUS_DRAWBACK://已退款14
                to_pay.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                after_sales.setVisibility(View.GONE);
                text.setText("订单已退款");
                break;
            case STATUS_BARTER://退换货16
                to_pay.setVisibility(View.GONE);
                after_sales.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                text.setText("订单已退换货");
                break;
            case STATUS_WAIT_SIGN://代签收18
                to_pay.setVisibility(View.GONE);
                after_sales.setText("退换货");
                text.setText("已经发货啦，小伙伴们请注意查收哦！");
                break;
            case STATUS_SIGNED://已签收20
                to_pay.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                after_sales.setText("退换货");
                text.setText("我们的龙珠商城往后会有更多优质商品，还请多多关顾哦！");
                break;
            case STATUS_CANCLE://30
                to_pay.setVisibility(View.GONE);
                sure_receive.setVisibility(View.GONE);
                after_sales.setVisibility(View.GONE);
                text.setText("订单已取消");
                break;
            default:
                parent.setVisibility(View.GONE);
                break;
        }
    }

    private void setListenr(final GoodsOrderModel model) {
        after_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("退换货".equals(after_sales.getText().toString())) {
                    if (ReturneDismiss == null) {
                        final ReturnedGoodsDialog.Builder builder = new ReturnedGoodsDialog.Builder(mContext);

                        builder.setPositiveButton("提交申请",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        returnGoods(model.orderCoding, model.status, builder.getReason());
                                    }
                                });
                        ReturneDismiss = builder.create();
                        ReturneDismiss.show();
                    } else {
                        ReturneDismiss.show();
                    }
                } else if ("取消".equals(after_sales.getText().toString())) {
                    if (dismiss == null) {
                        final BaseDialog.Builder builder = new BaseDialog.Builder(mContext);
                        builder.setMessage("要取消此订单？");

                        builder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        cancelOrder();
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
            }
        });
        sure_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dismiss == null) {
                    final BaseDialog.Builder builder = new BaseDialog.Builder(mContext);
                    builder.setMessage("确认收货？");
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SureReceiveGoods(b.orderCoding, b.status);
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
        });

        to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, WXPayEntryActivity.class);
                Bundle extras = new Bundle();
                extras.putString("orderNumber", b.orderCoding);
                extras.putDouble("totalPrice", b.cashPrice*b.quantity);
                extras.putString("description", b.title);
                extras.putString("orderId", b.id + "");
                extras.putString("flag", "appointmentorder");
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });
    }

    private void cancelOrder() {
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "mall/cancelOrder")
                .addParams("memberId", UserInfoModel.getInstance().getId() + "").addParams("orderId", b.orderCoding)
                .addParams("status", b.status + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("取消商品订单onError:" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e(response);
                Type type = new TypeToken<Response1<PayGoodsModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<PayGoodsModel> reaq = gson.fromJson(response, type);
                if ("200".equals(reaq.getErrCode())) {
                    Toast.makeText(mContext, reaq.getMessage(), Toast.LENGTH_SHORT).show();
                    if (mOnClickListener != null) {
                        type1 = 2;//这什么意思。。。。
                        mOnClickListener.onClick(null);
                    }
                } else {
                    Toast.makeText(mContext, reaq.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //确认收货状态更改
    private void SureReceiveGoods(final String orderId, int status) {
        String goodsOrderListUrl = UrlAPI.getSureReceiveGoodsUrl(orderId, status);
        PrintLog.e("确认收货URL:" + goodsOrderListUrl);
        OkHttpUtils.get().url(goodsOrderListUrl).build().execute(new StringCallback() {
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
                    Toast.makeText(mContext, "确认收货", Toast.LENGTH_LONG).show();
                    if (mOnClickListener != null) {
                        type1 = 1;
                        mOnClickListener.onClick(null);
                    }
                } else {
                    Toast.makeText(mContext, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //退货
    private void returnGoods(final String orderId, int status, String remark) {
        String goodsOrderListUrl = UrlAPI.getReturnGoodsUrl(orderId, status, remark);
        PrintLog.e("退货URL:" + goodsOrderListUrl);
        OkHttpUtils.get().url(goodsOrderListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("退货:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("退货:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 resp = gson.fromJson(response, type);
                if ("200".equals(resp.getErrCode())) {
                    Toast.makeText(mContext, "退货成功", Toast.LENGTH_LONG).show();
                    ReturneDismiss.dismiss();
                    if (mOnClickListener != null) {
                        type1 = 3;
                        mOnClickListener.onClick(null);
                    }
                } else {
                    Toast.makeText(mContext, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private View.OnClickListener mOnClickListener;

    public void setOnUpdate(View.OnClickListener listener) {
        mOnClickListener = listener;
    }
}
