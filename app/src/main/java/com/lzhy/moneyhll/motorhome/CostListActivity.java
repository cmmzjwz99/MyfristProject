package com.lzhy.moneyhll.motorhome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.NoScrollListView;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.model.ServiceCostModel;
import com.lzhy.moneyhll.model.SureMakeOrderModel;
import com.lzhy.moneyhll.model.SureToPayModel;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.AddViewHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 租房车费用清单
 */
public final class CostListActivity extends MySwipeBackActivity implements ItemViewListener {

    private LinearLayout group1, group2;
    private TextView name;
    private TextView time_num;
    private TextView get_address;
    private TextView set_address;
    private TextView ont_price;
    private TextView cost;
    private CheckBox check_1;
    private TextView cost1;
    private SimpleDraweeView image;
    private CheckBox check_2;
    private TextView pay;
    private TextView cost2;
    private TextView to_pay;
    private View mView1, mView2;

    private NoScrollListView mListView1;
    private LongAdapter mAdapter1;
    private NoScrollListView mListView2;
    private LongAdapter mAdapter2;
    private List<ServiceCostModel> mList1 = new ArrayList<>();
    private List<ServiceCostModel> mList2 = new ArrayList<>();
    private List<ServiceCostModel> group11;
    private List<ServiceCostModel> group22;

    private Drawable drawableUp, drawableDown;
    private SureMakeOrderModel model;

    private int day;
    private double price;
    private double pay_cost1;
    private double pay_cost2;
    private double allPay;
    private String begTime;
    private String endTime;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cost_list);

        addActivityCST(this);

        initView();
        initTitlebar();
        initAdapter();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        Intent intent = getIntent();
        extras = intent.getExtras();
        model = (SureMakeOrderModel) extras.getSerializable("SureMakeOrderModel");
        begTime = extras.getString("begTime");
        endTime = extras.getString("endTime");

        day = model.day + 1;
        group1 = (LinearLayout) findViewById(R.id.group1);
        group2 = (LinearLayout) findViewById(R.id.group2);
        name = (TextView) findViewById(R.id.name);
        time_num = (TextView) findViewById(R.id.time_num);
        get_address = (TextView) findViewById(R.id.get_address);
        set_address = (TextView) findViewById(R.id.set_address);
        ont_price = (TextView) findViewById(R.id.ont_price);
        cost = (TextView) findViewById(R.id.cost);
        cost1 = (TextView) findViewById(R.id.cost1);
        cost2 = (TextView) findViewById(R.id.cost2);
        pay = (TextView) findViewById(R.id.pay);
        to_pay = (TextView) findViewById(R.id.to_pay);
        image = (SimpleDraweeView) findViewById(R.id.image);
        check_1 = (CheckBox) findViewById(R.id.check_1);
        check_2 = (CheckBox) findViewById(R.id.check_2);
        mView1 = LayoutInflater.from(CostListActivity.this).inflate(R.layout.add_view_cost, null);
        mView2 = LayoutInflater.from(CostListActivity.this).inflate(R.layout.add_view_cost, null);

        mListView1 = (NoScrollListView) mView1.findViewById(R.id.list);
        mListView2 = (NoScrollListView) mView2.findViewById(R.id.list);

        drawableUp = CostListActivity.this.getResources().getDrawable(R.mipmap.ic_up);
        drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        drawableDown = CostListActivity.this.getResources().getDrawable(R.mipmap.ic_down);
        drawableDown.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());

        group11 = model.serviceCost.Group1;
        group22 = model.serviceCost.Group2;

        for (int i = 0; i < group11.size(); i++) {
            if ("4".equals(group11.get(i).unitName)) {
                int sum = day / 5;
                if (day % 5 != 0) {
                    sum++;
                }
                pay_cost1 += group11.get(i).price * sum;
            } else if ("1".equals(group11.get(i).unitName)) {
                pay_cost1 += group11.get(i).price * 1;
            } else {
                pay_cost1 += group11.get(i).price * day;
            }
        }
        for (int i = 0; i < group22.size(); i++) {
            if ("4".equals(group22.get(i).unitName)) {
                int sum = day / 5;
                if (day % 5 != 0) {
                    sum++;
                }
                pay_cost2 += group22.get(i).price * sum;
            } else if ("1".equals(group11.get(i).unitName)) {
                pay_cost2 += group22.get(i).price * 1;
            } else {
                pay_cost2 += group22.get(i).price * day;
            }
        }

        name.setText(extras.getString("name"));
        image.setImageURI(extras.getString("imageUrl"));
        time_num.setText("使用时间：" + begTime + "至" + endTime);
        get_address.setText("提车地址：" + extras.getString("address"));
        set_address.setText("还车地址：" + extras.getString("address"));
        price = Double.valueOf(extras.getString("price"));
        ont_price.setText("¥ " + price + "×" + day);
        allPay = day * price;
        pay.setText("¥ " + allPay);
        cost.setText("¥ " + allPay);


        cost1.setText("¥ " + CommonUtil.setDouble(pay_cost1));
        cost2.setText("¥ " + CommonUtil.setDouble(pay_cost2));
        pay_cost1 = 0;
        pay_cost2 = 0;
        setOnClick();
    }

    private void initAdapter() {
        mAdapter1 = new LongAdapter(CostListActivity.this, mList1, this);
        mListView1.setAdapter(mAdapter1);
        mAdapter2 = new LongAdapter(CostListActivity.this, mList2, this);
        mListView2.setAdapter(mAdapter2);
        mList1.clear();
        mList1.addAll(group11);
        mAdapter1.notifyDataSetChanged();
        mList2.clear();
        mList2.addAll(group22);
        mAdapter2.notifyDataSetChanged();

    }

    public void setOnClick() {
        View parent = (View) cost1.getParent();
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (group1.getChildCount() == 0) {
                    group1.addView(mView1);
                    cost1.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableUp, null);
                } else {
                    group1.removeAllViews();
                    cost1.setCompoundDrawables(null, null, drawableDown, null);
                }
            }
        });

        View parent1 = (View) cost2.getParent();
        parent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (group2.getChildCount() == 0) {
                    group2.addView(mView2);
                    cost2.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableUp, null);
                } else {
                    group2.removeAllViews();
                    cost2.setCompoundDrawables(null, null, drawableDown, null);
                }
            }
        });
        to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SureToPay();
            }
        });

        check_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    for (int i = 0; i < group11.size(); i++) {
                        if ("4".equals(group11.get(i).unitName)) {
                            int sum = day / 5;
                            if (day % 5 != 0) {
                                sum++;
                            }
                            pay_cost1 += group11.get(i).price * sum;
                        } else if ("1".equals(group11.get(i).unitName)) {
                            pay_cost1 += group11.get(i).price * 1;
                        } else {
                            pay_cost1 += group11.get(i).price * day;
                        }
                    }
                    allPay = allPay + pay_cost1;
                } else {
                    allPay = allPay - pay_cost1;
                    pay_cost1 = 0;
                }
                pay.setText("¥ " + allPay);
            }
        });

        check_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    for (int i = 0; i < group22.size(); i++) {
                        if ("4".equals(group22.get(i).unitName)) {
                            int sum = day / 5;
                            if (day % 5 != 0) {
                                sum++;
                            }
                            pay_cost2 += group22.get(i).price * sum;
                        } else if ("1".equals(group11.get(i).unitName)) {
                            pay_cost2 += group22.get(i).price * 1;
                        } else {
                            pay_cost2 += group22.get(i).price * day;
                        }
                    }
                    allPay = allPay + pay_cost2;
                } else {
                    allPay = allPay - pay_cost2;
                    pay_cost2 = 0;
                }
                pay.setText("¥ " + allPay);
            }
        });
    }

    private void SureToPay() {
        //房车使用费：price，服务费：serviceCharge,用户Id:userId,保证金：margin
        String sureMakeOrderUrl = UrlAPI.getSureToPayUrl(price * day, pay_cost1, UserInfoModel.getInstance().getId(), pay_cost2);
        PrintLog.e("下单房车1URL:" + sureMakeOrderUrl);
        OkHttpUtils.get().url(sureMakeOrderUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("下单房车1:" + e);
                Toast.makeText(CostListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("下单房车1:" + response);
                Type type = new TypeToken<Response1<SureToPayModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<SureToPayModel> data = gson.fromJson(response, type);
                if ("200".equals(data.getErrCode())) {
                    Intent intent = new Intent(CostListActivity.this, PayMotorhomeActivity.class);
                    Bundle extras1 = new Bundle();
                    extras1.putSerializable("SureToPayModel", data.getData());

                    extras1.putString("productId", extras.getString("productId"));
                    extras1.putString("person_name", extras.getString("person_name"));
                    extras1.putString("idCard", extras.getString("idCard"));
                    extras1.putString("licenceNo", extras.getString("licenceNo"));
                    extras1.putString("account", extras.getString("account"));
                    extras1.putString("branchId", extras.getString("branchId"));

                    extras1.putString("serviceCharge", pay_cost1 + "");
                    extras1.putString("margin", pay_cost2 + "");
                    extras1.putString("begTime", begTime);
                    extras1.putString("endTime", endTime);
                    extras1.putString("price", price + "");
                    extras1.putString("day", day + "");

                    extras1.putString("total", data.getData().total + "");
                    intent.putExtras(extras1);
                    startActivity(intent);
                }
            }
        });
    }

    public void initTitlebar() {

        BaseTitlebar mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("费用清单");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        AddViewHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(CostListActivity.this).inflate(
                    R.layout.item_add_view, null, false);
            helper = new AddViewHelper(CostListActivity.this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (AddViewHelper) itemView.getTag();
        }
        helper.updateView((ServiceCostModel) data, day);
        return itemView;
    }
}
