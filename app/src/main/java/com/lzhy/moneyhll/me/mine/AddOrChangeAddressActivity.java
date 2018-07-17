package com.lzhy.moneyhll.me.mine;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.dialog.AddAdderssPop;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.model.AddressModel;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilCheckMix.editTextCheck;

/**
 * Created by cmm on 2016/11/1.
 * 添加或修改收货地址
 */
public class AddOrChangeAddressActivity extends MySwipeBackActivity {
    private BaseTitlebar titlebar;
    protected String mProviceName = "";
    protected String mCityName = "";
    protected String mDistrictName = "";
    private InputMethodManager imm;
    private AddAdderssPop mAdderss;
    private String type;

    private EditText people;
    private EditText phone;
    private TextView choose_area;
    private EditText address;
    private Button sure;
    private Button cancel;
    private CheckBox default_address;
    private int id = 0;
    private String isDefaultAddress = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);

        addActivityCST(this);
        initView();
        initTitlebar();
        setOnClick();
    }

    private void initTitlebar() {
        titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("编辑收货地址");
    }
    @Override
    protected void onResume() {
        disparityLogin();
        super.onResume();
    }
    private void initView() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        type = extras.getString("type");

        people = (EditText) findViewById(R.id.people);
        phone = (EditText) findViewById(R.id.phone);
        choose_area = (TextView) findViewById(R.id.choose_area);
        address = (EditText) findViewById(R.id.address);
        sure = (Button) findViewById(R.id.sure);
        cancel = (Button) findViewById(R.id.cancel);
        default_address = (CheckBox) findViewById(R.id.default_address);

        if ("add".equals(type)) {
            cancel.setText("取 消");
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else if ("change".equals(type)) {
            AddressModel model = (AddressModel) extras.getSerializable("AddressModel");
            people.setText(model.name);
            phone.setText(model.phone);
            choose_area.setText(model.province + " " + model.city + " " + model.district);
            address.setText(model.addresss);
            if (model.isDefaultAddress == 1) {
                default_address.setChecked(true);
            }
            mProviceName = model.province;
            mCityName = model.city;
            mDistrictName = model.district;
            id = model.id;

            cancel.setText("删 除");
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteAddress(id);
                }
            });
        }
        imm = (InputMethodManager) getSystemService(AddOrChangeAddressActivity.this.INPUT_METHOD_SERVICE);
        mAdderss = new AddAdderssPop(AddOrChangeAddressActivity.this);

        mAdderss.setOnlistener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProviceName = mAdderss.mCurrentProviceName;
                mCityName = mAdderss.mCurrentCityName;
                mDistrictName = mAdderss.mCurrentDistrictName;
                choose_area.setText(mProviceName + " " + mCityName + " " + mDistrictName);
                mAdderss.dismiss();
            }
        });
    }
    private void setOnClick() {
        ((View) choose_area.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = (View) cancel.getParent();
                mAdderss.showAtBottom((View) cancel.getParent());
                imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
            }
        });
        default_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isDefaultAddress = "1";
                } else {
                    isDefaultAddress = "0";
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (people.getText().length() == 0) {
                    Toast.makeText(AddOrChangeAddressActivity.this, "请填写收件人", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!editTextCheck(people.getText().toString(), AddOrChangeAddressActivity.this,"收件人不能输入非法字符")) {
                    return;
                }
                if (phone.getText().length() != 11) {
                    UtilToast.getInstance().showDragonError( "请填写正确的联系人电话") ;
                    return;
                }
                if (choose_area.getText().length() == 0) {
                    UtilToast.getInstance().showDragonInfo( "请选择区域"); ;
                    return;
                }
                if (address.getText().length() == 0) {
                    UtilToast.getInstance().showDragonInfo( "请填写详细地址"); ;
                    return;
                }
                if (!editTextCheck(address.getText().toString(), AddOrChangeAddressActivity.this,"地址不能输入非法字符")) {
                    return;
                }
                if ("add".equals(type)) {
                    AddAddress();
                } else if ("change".equals(type)) {
                    ChangeAddress();
                }
            }
        });
    }

    /**
     * 删除收货地址
     *
     * @param id
     */
    private void deleteAddress(int id) {
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/address/delete")
                .addParams("id", id + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("删除收货地址onError:" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("删除收货地址onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 response2 = gson.fromJson(response, type);
                if ("200".equals(response2.getErrCode())) {
                    Toast.makeText(AddOrChangeAddressActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddOrChangeAddressActivity.this, response2.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 添加收货地址
     */
    private void AddAddress() {
        PrintLog.e("添加收货地址" + isDefaultAddress);
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/address/save")
                .addParams("userId", UserInfoModel.getInstance().getId() + "").addParams("name", people.getText().toString())
                .addParams("phone", phone.getText().toString()).addParams("province", mProviceName)
                .addParams("city", mCityName).addParams("district", mDistrictName)
                .addParams("addresss", address.getText().toString()).addParams("isDefaultAddress", isDefaultAddress)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("添加收货地址onError:" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("添加收货地址onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 response2 = gson.fromJson(response, type);
                if ("200".equals(response2.getErrCode())) {
                    Toast.makeText(AddOrChangeAddressActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddOrChangeAddressActivity.this, response2.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 改变收货地址
     */
    private void ChangeAddress() {
        PrintLog.e("改变收货地址" + isDefaultAddress);
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/address/save").addParams("userId", UserInfoModel.getInstance().getId() + "")
                .addParams("id", id + "").addParams("name", people.getText().toString())
                .addParams("phone", phone.getText().toString()).addParams("province", mProviceName)
                .addParams("city", mCityName).addParams("district", mDistrictName)
                .addParams("addresss", address.getText().toString()).addParams("isDefaultAddress", isDefaultAddress)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("编辑收货地址onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("编辑收货地址onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 response2 = gson.fromJson(response, type);
                if ("200".equals(response2.getErrCode())) {
                    Toast.makeText(AddOrChangeAddressActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddOrChangeAddressActivity.this, response2.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
