package com.lzhy.moneyhll.motorhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.dialog.UseStockDialog;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.HistoryInfo;
import com.lzhy.moneyhll.model.PayMotorhomeModel;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.model.SureToPayModel;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.lzhy.moneyhll.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.MD5_KEY;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.finishAllCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 下单支付界面
 */
public final class PayMotorhomeActivity extends MySwipeBackActivity {
    private BaseTitlebar mTitlebar;
    private TextView pricetv;
    private TextView day;
    private TextView stock;
    private TextView money;
    private Button pay;
    private Bundle extras;
    private UseStockDialog dismiss;
    //private String[] couponNum;
    private String couponNum1 = "";
    private String couponNum2 = "";
    private List<HistoryInfo> mList;
    private UseStockAdapter adapter;

    private String begTime;
    private String endTime;
    private String serviceCharge;
    private String margin;
    private String price;
    private String productId;
    private String name;
    private String idCard;
    private String licenceNo;
    private String account;
    private String branchId;
    private SureToPayModel sureToPayModel;
    private String daynum;
    private List<HistoryInfo> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pay_motorhome);

        addActivityCST(this);

        initView();
        initTitlebar();
        initNowRoll();

    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

    private void initView() {
        Intent intent = getIntent();
        extras = intent.getExtras();

        begTime = extras.getString("begTime");
        endTime = extras.getString("endTime");
        serviceCharge = extras.getString("serviceCharge");
        margin = extras.getString("margin");
        price = extras.getString("price");
        productId = extras.getString("productId");
        name = extras.getString("person_name");
        idCard = extras.getString("idCard");
        licenceNo = extras.getString("licenceNo");
        account = extras.getString("account");
        branchId = extras.getString("branchId");
        daynum = extras.getString("day");

        pricetv = (TextView) findViewById(R.id.price);
        day = (TextView) findViewById(R.id.day);
        stock = (TextView) findViewById(R.id.stock);
        money = (TextView) findViewById(R.id.money);
        pay = (Button) findViewById(R.id.pay);
        mList = new ArrayList<>();
        pricetv.setText(extras.getString("price") + "元/天");
        day.setText(extras.getString("day") + " 天");
        sureToPayModel = (SureToPayModel) extras.getSerializable("SureToPayModel");
        money.setText("¥ " + sureToPayModel.total);
        View parent = (View) stock.getParent();
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mList.size() == 0) {
                    Toast.makeText(PayMotorhomeActivity.this, "没有可使用的房车劵", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dismiss == null) {
                    final UseStockDialog.Builder builder = new UseStockDialog.Builder(PayMotorhomeActivity.this);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            List<Integer> strings = adapter.mStrings;
                            //房车劵使用数量不得大于租用天数，否则清除选中信息重新选择
                            couponNum1 = "";
                            couponNum2 = "";
                            if (strings.size() > Integer.valueOf(daynum)) {
                                Toast.makeText(PayMotorhomeActivity.this, "您最多可使用" + daynum + "张房车劵", Toast.LENGTH_SHORT).show();
                                adapter.clearSelect();
                                adapter.notifyDataSetChanged();
                                return;
                            } else {
                                stock.setText("已使用" + strings.size() + "张房车券");
                                SureChoose(strings);
                                adapter.clearSelect();
                                adapter.notifyDataSetChanged();
                                dismiss.dismiss();
                            }

                        }
                    });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    adapter.clearSelect();//清除选中房车劵信息
                                    couponNum1 = "";
                                    couponNum2 = "";
                                    dismiss.dismiss();
                                    money.setText("¥ " + sureToPayModel.total);//取消时显示原价
                                    if (data!=null&&!data.isEmpty()){
                                        stock.setText("使用房车抵用券（可用" + data.size() + "张）");
                                    }else {
                                        stock.setText("使用房车抵用券（可用0张）");
                                    }
                                }
                            });
                    dismiss = builder.create();
                    ListView listView = builder.getListView();
                    adapter = new UseStockAdapter(mList, PayMotorhomeActivity.this);
                    listView.setAdapter(adapter);
                    dismiss.show();
                } else {
                    dismiss.show();
                }
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("xxx", "onClick: ----------------------------------------------");
                view.setAlpha(0.5f);
                ToChoosePayType(view);
            }
        });
    }

    private void SureChoose(List<Integer> strings) {
        if (strings.size() != 0) {
            for (int j = 0; j < strings.size(); j++) {
                couponNum1 = couponNum1 + strings.get(j);
                couponNum2 = couponNum2 + strings.get(j) + "&";
            }
            couponNum2 = couponNum2.substring(0, couponNum2.length() - 1);
            if (sureToPayModel.total - (Double.valueOf(price) * strings.size()) >= 0) {
                money.setText("¥ " + (sureToPayModel.total - (Double.valueOf(price) * strings.size())));
            } else {
                money.setText("¥ " + (sureToPayModel.total - (Double.valueOf(price) * Integer.valueOf(daynum))));
            }

        }
    }

    private void ToChoosePayType(View view) {

        view.setAlpha(1.0f);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(UserInfoModel.getInstance().getId()));
        map.put("startDate", begTime);
        map.put("endDate", endTime);
        map.put("serviceCharge", String.valueOf(serviceCharge));
        map.put("price", String.valueOf(price));
        map.put("productId", String.valueOf(productId));
        map.put("margin", String.valueOf(margin));

        map.put("name", name);
        map.put("idCard", idCard);
        map.put("licenceNo", licenceNo);
        map.put("account", account);
        map.put("branchId", String.valueOf(branchId));
        if (couponNum1 != null && couponNum1.length() > 0)
            map.put("coupon", couponNum1);
        String linkString = CommonUtil.createLinkString(map) + MD5_KEY;

//        开始时间：startDate，结束时间：endDate，服务费：serviceCharge,保证金：margin，房车使用费：price， 用户ID:userId,
//       产品ID:productId,提车人姓名：name,身份证号：idCard,档案编号：licenceNo，手机号：account,,网点id branchId 使用房车券数量：coupon[]
        PrintLog.e(begTime + "   " + endTime + "   " + serviceCharge + "   " + margin + "   " + price + "   " + productId + "   "
                + name + "   " + idCard + "   " + licenceNo + "   " + account + "   " + branchId + "   " + couponNum1 + "   "
                + linkString + Utils.getMD5Value(linkString));

        OkHttpUtils.post().url(UrlAPI.HOST_URL + "product/payment")
                .addParams("startDate", begTime).addParams("endDate", endTime).addParams("serviceCharge", serviceCharge)
                .addParams("margin", margin).addParams("price", price).addParams("userId", UserInfoModel.getInstance().getId() + "")
                .addParams("productId", productId).addParams("name", name).addParams("idCard", idCard).addParams("licenceNo", licenceNo)
                .addParams("account", account).addParams("branchId", branchId).addParams("coupon", couponNum2)
                .addParams("key", Utils.getMD5Value(linkString))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(PayMotorhomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                //// TODO_XBB: 2016/12/5 这个地方的逻辑需要简单的改一下
                PrintLog.e("下单onResponse:" + response);
                Type type = new TypeToken<Response1<PayMotorhomeModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<PayMotorhomeModel> data = gson.fromJson(response, type);
                if ("200".equals(data.getErrCode())) {
                    final Intent intent = new Intent(PayMotorhomeActivity.this, WXPayEntryActivity.class);
                    final Bundle extras = new Bundle();

                    /************************************************************
                     *修改者;  龙之游 @ xu 596928539@qq.com
                     *修改时间:2016/12/17 18:27
                     *bug: 无   extras.putString("description", data.getData().description);
                     *修复:  代码风格调整，结构优化
                     ************************************************************/
                    final double totalPrice = data.getData().totalPrice;
                    final String orderNumber = data.getData().orderNumber;
                    final String description = data.getData().orderNumber;
                    final String orderId = data.getData().orderId;

                    extras.putDouble("totalPrice", totalPrice);
                    extras.putString("orderNumber", orderNumber);
                    extras.putString("description", description);
                    extras.putString("orderId", orderId);

                    extras.putString("flag", "zufangche");//将被 下一个界面/"选择支付界面接受"/结果页接收，回退标志

                    // Log.i("xxx", "onResponse: " + response);

                    if (totalPrice <= 0.0) {//房车卷支付成功,跳轉到結果頁
                        Intent it = new Intent(PayMotorhomeActivity.this, PayResultActivity.class);
                        it.putExtra("flag", "zufangche");
                        startActivity(it);
                        finishAllCST();//用于回退到租房车
                    } else {
                        //跳转到现金支付
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(PayMotorhomeActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initNowRoll() {
        String getMyMotorHomeRollUrl = UrlAPI.getMyMotorHomeRollUrl(UserInfoModel.getInstance().getId(), 1);
        PrintLog.e("当前URL:" + getMyMotorHomeRollUrl);
        OkHttpUtils.get().url(getMyMotorHomeRollUrl).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("房车劵onError" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("房车劵onResponse" + response);

                Type type = new TypeToken<Response<HistoryInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Response<HistoryInfo> resp = gson.fromJson(response, type);
                data = resp.getData();

                if (data != null) {
                    mList.clear();
                    mList.addAll(data);
                    stock.setText("使用房车抵用券（可用" + data.size() + "张）");
                }
            }
        });
    }

    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("支付");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
