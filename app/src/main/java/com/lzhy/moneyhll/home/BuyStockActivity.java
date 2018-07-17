package com.lzhy.moneyhll.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.loginOrRegister.LoginActivity;
import com.lzhy.moneyhll.model.BuyStockModel;
import com.lzhy.moneyhll.model.PayGoodsModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.lzhy.moneyhll.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 购买房车劵
 */
public class BuyStockActivity extends MySwipeBackActivity {
    private BaseTitlebar mTitlebar;
    private SimpleDraweeView mDraweeView;
    private TextView use_day;
    private TextView bard_number;
    private TextView sign_up;
    private TextView number;
    private TextView sign_dowm;
    private TextView price;
    private Button payment;
    private int num = 1;
    private int day = 1;
    private double ballnum;
    private double pri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_buy_stock);
        addActivityCST(this);
        initTitlebar();
        initView();
        SetListener();
        LoadingData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        mDraweeView = (SimpleDraweeView) findViewById(R.id.image);
        use_day = (TextView) findViewById(R.id.use_day);
        bard_number = (TextView) findViewById(R.id.bard_number);
        sign_up = (TextView) findViewById(R.id.sign_up);
        number = (TextView) findViewById(R.id.number);
        price = (TextView) findViewById(R.id.price);
        payment = (Button) findViewById(R.id.payment);

        number.setText(num + "");
        use_day.setText(" 房车使用 " + day + "天");

        sign_dowm = (TextView) findViewById(R.id.sign_dowm);
        CommonUtil.setViewHeight(BuyStockActivity.this, mDraweeView);
    }

    private void SetListener() {
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num++;
                day++;
//                ballnum = ballnum + 1000;
//                pri = pri + 2000;
                double ball = ballnum * num;
                double allPri = pri * num;

                //采用四舍五入的方式，取两位小数
                BigDecimal bg = new BigDecimal(allPri);
                double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                number.setText(num + "");
                use_day.setText(" 房车使用 " + day + "天");
                bard_number.setText(" 赠送龙珠 " + ball);
                price.setText("¥" + value);
            }
        });
        sign_dowm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num == 1) {
                    return;
                } else {
                    num--;
                    number.setText(num + "");
                    day--;
//                    ballnum = ballnum - 1000;
//                    pri = pri - 2000;
                    double ball = ballnum * num;
                    double allPri = pri * num;

                    //采用四舍五入的方式，取两位小数
                    BigDecimal bg = new BigDecimal(allPri);
                    double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    System.out.println("bigDecimal==============" + value);

                    number.setText(num + "");
                    use_day.setText(" 房车使用 " + day + "天");
                    bard_number.setText(" 赠送龙珠 " + ball);
                    price.setText("¥" + value);
                }
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserInfoModel.getInstance().isLogin()) {
                    MakeStockOrder();
                } else {
                    startActivity(new Intent(BuyStockActivity.this, LoginActivity.class));
                }
            }
        });
    }

    private void MakeStockOrder() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("memberId", String.valueOf(UserInfoModel.getInstance().getId()));
        map.put("num", String.valueOf(num));

        String linkString = CommonUtil.createLinkString(map) + "lzhyapp.hll.html";
        PrintLog.e(UserInfoModel.getInstance().getId() + "  " + num + "  " + linkString + "  " + Utils.getMD5Value(linkString));

        OkHttpUtils.post().url(UrlAPI.HOST_URL + "carcoupon/payment/cash")
                .addParams("memberId", UserInfoModel.getInstance().getId() + "")
                .addParams("num", num + "").addParams("key", Utils.getMD5Value(linkString))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("生成房车劵订单CashonError:" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("生成房车劵订单CashonResponse:" + response);
                Type type = new TypeToken<Response1<PayGoodsModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<PayGoodsModel> reaq = gson.fromJson(response, type);
                if ("200".equals(reaq.getErrCode())) {
                    Intent intent = new Intent(BuyStockActivity.this, WXPayEntryActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("orderNumber", reaq.getData().orderNumber);
                    extras.putDouble("totalPrice", reaq.getData().paymentCash);
                    extras.putString("description", reaq.getData().title);
                    extras.putString("orderId", reaq.getData().parentId + "");
                    extras.putString("flag", "buystock");
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    Toast.makeText(BuyStockActivity.this, reaq.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("购买房车劵");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void LoadingData() {
        String buyStock = UrlAPI.HOST_URL+"carcoupon/buyDefault";
        PrintLog.e("购买房车劵URL:" + buyStock);
        OkHttpUtils.get().url(buyStock).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("购买房车劵URL:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("购买房车劵:" + response);
                Type type = new TypeToken<Response1<BuyStockModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<BuyStockModel> resp = gson.fromJson(response, type);
                BuyStockModel data = resp.getData();
                updataView(data);
            }
        });
    }

    private void updataView(BuyStockModel data) {
        if (data != null) {
            PrintLog.e("购买房车劵updataView:" + data.imageUrl);
            mDraweeView.setImageURI(data.imageUrl);
            ballnum = data.givePearlQty;
            pri = data.unitPrice;
            bard_number.setText(" 赠送龙珠 " + ballnum);
            price.setText("¥" + pri);
        }
    }
}
