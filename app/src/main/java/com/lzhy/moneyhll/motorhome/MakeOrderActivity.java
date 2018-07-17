package com.lzhy.moneyhll.motorhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import com.lzhy.moneyhll.model.MakeOrderModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.model.SureMakeOrderModel;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckIDCard.IDCardValidate;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilCheckMix.editTextCheck;
import static com.lzhy.moneyhll.utils.UtilCheckMix.isMobileNO;

/**
 * 下单
 */
public final class MakeOrderActivity extends MySwipeBackActivity {
    private BaseTitlebar mTitlebar;

    private SimpleDraweeView image;
    private TextView name;
    private TextView driving_type;
    private TextView website;
    private TextView car_stock;
    private TextView price;
    private TextView get_time;
    private RadioGroup group1;
    private TextView get_address;
    private TextView set_time;
    private RadioGroup group2;
    private TextView set_address;
    private TextView type;

    private EditText edit_name;
    private EditText edit_id;
    private EditText edit_phone;
    private EditText edit_driving;
    private Button sure;

    private String productId;
    private String begTime;
    private String endTime;

    public String branchId;
    private MakeOrderModel.ProductInfoModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_make_order);

        addActivityCST(this);

        initTitlebar();
        initView();
        LoadingData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("id");
        begTime = intent.getStringExtra("begTime");
        endTime = intent.getStringExtra("endTime");

        image = (SimpleDraweeView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        driving_type = (TextView) findViewById(R.id.driving_type);
        website = (TextView) findViewById(R.id.website);
        car_stock = (TextView) findViewById(R.id.car_stock);
        price = (TextView) findViewById(R.id.price);
        get_time = (TextView) findViewById(R.id.get_time);
        get_time.setText(begTime);
        group1 = (RadioGroup) findViewById(R.id.group1);
        get_address = (TextView) findViewById(R.id.get_address);
        set_time = (TextView) findViewById(R.id.set_time);
        set_time.setText(endTime);
        group2 = (RadioGroup) findViewById(R.id.group2);
        set_address = (TextView) findViewById(R.id.set_address);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_id = (EditText) findViewById(R.id.edit_id);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_driving = (EditText) findViewById(R.id.edit_driving);
        sure = (Button) findViewById(R.id.sure);
        type = (TextView) findViewById(R.id.type);
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio1:
                        Toast.makeText(MakeOrderActivity.this, "送车上门", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.get_website:
                        Toast.makeText(MakeOrderActivity.this, "网点自提", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio2:
                        Toast.makeText(MakeOrderActivity.this, "上门收车", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.set_website:
                        Toast.makeText(MakeOrderActivity.this, "直送网点", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextCheck(edit_driving.getText().toString(), MakeOrderActivity.this,"驾照档案编号不能输入非法字符")) return;
                if (!editTextCheck(edit_name.getText().toString(), MakeOrderActivity.this,"姓名不能输入非法字符"))             return;
                if (!editTextCheck(edit_phone.getText().toString(), MakeOrderActivity.this,"电话不能输入非法字符"))            return;

                if (edit_name.getText().length() == 0) {
                    UtilToast.getInstance().showDragonInfo("请输入真实名字");
                    return;
                }
                if (!isMobileNO(edit_phone.getText().toString())) {
                    UtilToast.getInstance().showDragonInfo("请核对手机号码");
                    return;
                }
                String idCard  = edit_id.getText().toString();
                String tempIdCard = "";
                if (idCard.length() != 0) {
                    if (idCard.substring(idCard.length() - 1, idCard.length()).equals("X")) {
                        //tempIdCard = idCard.substring(0,idCard.length()-1)+"x";
                        tempIdCard = idCard.toLowerCase();
                    } else {
                        tempIdCard = idCard;
                    }
                    if (!IDCardValidate(tempIdCard)) {
                        return;
                    }
                }else {
                    UtilToast.getInstance().showDragonInfo("请填写身份证号码");
                    return;
                }

                if (edit_driving.getText().length() != 12) {
                    UtilToast.getInstance().showDragonInfo("请输入正确的驾照档案编号");
                    return;
                }
                MakeOrder();
            }
        });
    }

    //* 用户ID：userId，驾照档案编号：licenceNo，提车人手机号：account，姓名：name,身份证号：idCard,
    //* 房车ID：productId，提车时间：startDate，还车时间：endDate,网点ID:branchId

    private void MakeOrder() {
        String sureMakeOrderUrl = UrlAPI.getSureMakeOrderUrl(UserInfoModel.getInstance().getId(),
                productId, begTime, endTime);
        PrintLog.e("下单房车1URL:" + sureMakeOrderUrl);
        OkHttpUtils.get().url(sureMakeOrderUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("下单房车1:" + e);
            }

            @Override
            public void onResponse(String response, int id) {

                PrintLog.e("下单房车1:" + response);
                Type type = new TypeToken<Response1<SureMakeOrderModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<SureMakeOrderModel> data = gson.fromJson(response, type);
                if ("200".equals(data.getErrCode())) {
                    Intent intent = new Intent(MakeOrderActivity.this, CostListActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable("SureMakeOrderModel", data.getData());
                    extras.putString("name", model.name);
                    extras.putString("imageUrl", model.imageUrl);
                    extras.putString("begTime", begTime);
                    extras.putString("endTime", endTime);
                    extras.putString("address", model.address);
                    extras.putString("price", model.price + "");

                    extras.putString("productId", productId);
                    extras.putString("person_name", edit_name.getText().toString());
                    extras.putString("idCard", edit_id.getText().toString());
                    extras.putString("licenceNo", edit_driving.getText().toString());
                    extras.putString("account", edit_phone.getText().toString());
                    extras.putString("branchId", model.branchId);

                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });
    }

    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"下单");
    }

    private void LoadingData() {
        String makeOrderUrl = UrlAPI.getMakeOrderUrl(productId, UserInfoModel.getInstance().account);
        PrintLog.e("下单房车URL:" + makeOrderUrl);
        OkHttpUtils.get().url(makeOrderUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("下单房车:" + e);
            }

            @Override
            public void onResponse(String response, int id) {

                PrintLog.e("下单房车:" + response);
                Type type = new TypeToken<Response1<MakeOrderModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<MakeOrderModel> resp = gson.fromJson(response, type);
                MakeOrderModel data = resp.getData();
                updataView(data);
            }
        });
    }

    private void updataView(MakeOrderModel data) {
        MakeOrderModel.MemberModel member = data.member;
        model = data.productInfo;
        image.setImageURI(model.imageUrl);
        branchId = model.branchId;
        name.setText(model.name);

        StringBuffer buffer = new StringBuffer();
        buffer.append("准驾类型：" + model.permit);
        buffer.append(" | 床数：" + model.beds + "张");
        buffer.append(" | 额载：" + model.nuclearCarrier + "人");

        driving_type.setText(buffer);

        website.setText(model.branchName);
        if (model.travelType == 1) {
            type.setText("自行式");
        } else if (model.travelType == 2) {
            type.setText("拖斗式");
        } else {
            type.setText("其它");
        }
        if ("1".equals(model.payCoupon)) {
            car_stock.setText("可使用房车劵");
        } else {
            car_stock.setText("不可使用房车劵");
        }
        price.setText("¥" + model.price + "");
        get_address.setText(model.address);
        set_address.setText(model.address);
        edit_name.setText(member.realName);
        edit_phone.setText(member.account);
        if (member.idCard != null && member.idCard.length() >= 15) {
            edit_id.setText(member.idCard);
        }
        if (member.licenceNo != null && member.licenceNo.length() >= 10) {
            edit_driving.setText(member.licenceNo);
        }
    }

}
