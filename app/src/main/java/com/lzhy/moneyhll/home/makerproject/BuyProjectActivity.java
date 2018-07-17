package com.lzhy.moneyhll.home.makerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.loginOrRegister.LoginActivity;
import com.lzhy.moneyhll.me.mine.AddressActivity;
import com.lzhy.moneyhll.model.AddressModel;
import com.lzhy.moneyhll.model.MakerModel;
import com.lzhy.moneyhll.model.MarkerOrderModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.Base64;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.lzhy.moneyhll.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 创客项目下单页
 */
public class BuyProjectActivity extends MySwipeBackActivity {
    private BaseTitlebar mTitlebar;
    private SimpleDraweeView mDraweeView;
    private TextView project_name;
    private TextView project_price;
    private TextView to_choose_address;
    private RelativeLayout rl_choose_address, rl_choose_address1;
    private TextView choose_name, choose_phone, choose_address;

    private TextView all_money;
    private Button to_pay;

    private TextView sign_up;
    private TextView number;
    private TextView sign_dowm;
    private int num = 1;
    private int id;
    private int projecttype;

    private String receiverName = "";
    private String telephone = "";
    private String address = "";
    private MakerModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_buy_project);
        addActivityCST(this);
        initView();
        LoadingData();
        initTitlebar();
        if (projecttype == 104) {
            getDefaultAddress();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

    private void initView() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getInt("id");
        projecttype = extras.getInt("type");
        mDraweeView = (SimpleDraweeView) findViewById(R.id.image);
        project_name = (TextView) findViewById(R.id.project_name);
        project_price = (TextView) findViewById(R.id.project_price);
        all_money = (TextView) findViewById(R.id.all);
        to_pay = (Button) findViewById(R.id.to_pay);
        to_choose_address = (TextView) findViewById(R.id.to_choose_address);
        choose_name = (TextView) findViewById(R.id.choose_name);
        choose_phone = (TextView) findViewById(R.id.choose_phone);
        choose_address = (TextView) findViewById(R.id.choose_address);
        rl_choose_address = (RelativeLayout) findViewById(R.id.rl_choose_address);
        rl_choose_address1 = (RelativeLayout) findViewById(R.id.rl_choose_address1);
        if (projecttype == 104) {
            rl_choose_address.setVisibility(View.VISIBLE);
        } else {
            rl_choose_address.setVisibility(View.GONE);
        }

        sign_up = (TextView) findViewById(R.id.sign_up);
        number = (TextView) findViewById(R.id.number);
        number.setText(num + "");
        sign_dowm = (TextView) findViewById(R.id.sign_dowm);
    }

    private void LoadingData() {
        String makerModelUrl = UrlAPI.getMakerModelUrl(projecttype, id);
        PrintLog.e("创客详情URL:" + makerModelUrl);
        OkHttpUtils.get().url(makerModelUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("创客详情onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("创客详情onResponse:" + response);
                Type type = new TypeToken<Response1<MakerModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<MakerModel> resp = gson.fromJson(response, type);
                if ("200".equals(resp.getErrCode())) {
                    MakerModel data = resp.getData();
                    mModel = data;
                    upDataView();
                }
            }
        });
    }

    private void upDataView() {
        if (mModel.imageurl != null) {
            mDraweeView.setImageURI(mModel.imageurl);
        }
        project_name.setText(mModel.projectname);
        project_price.setText(mModel.adultprice + "");
        all_money.setText(String.format("%.2f", mModel.adultprice * num));
        SetListener();
    }

    private void SetListener() {
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num++;
                number.setText(num + "");
                all_money.setText(String.format("%.2f", mModel.adultprice * num));
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
                    all_money.setText(String.format("%.2f", mModel.adultprice * num));
                }
            }
        });
        to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去支付
                if (!UserInfoModel.getInstance().isLogin()) {
                    startActivity(new Intent(BuyProjectActivity.this, LoginActivity.class));
                    return;
                }
                if (projecttype == 104 && choose_name.getText().length() <= 0) {
                    Toast.makeText(BuyProjectActivity.this, "请选择收货地址：", Toast.LENGTH_SHORT).show();
                    return;
                }
                SureToPay();
            }
        });

        rl_choose_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserInfoModel.getInstance().isLogin()) {
                    Intent intent = new Intent(BuyProjectActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(BuyProjectActivity.this, AddressActivity.class);
                intent.putExtra("type", "choose");
                startActivityForResult(intent, Constant.REQUEST_CODE);
            }
        });
    }

    private void SureToPay() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(UserInfoModel.getInstance().getId()));
        map.put("projectId", String.valueOf(mModel.id));
        map.put("quantity", String.valueOf(num));
        map.put("type", String.valueOf(mModel.projecttype));
        String linkString = CommonUtil.createLinkString(map) + "lzhyapp.hll.html";

//        PrintLog.e("创客url:" + Constant.HOST_URL + "project/submitOrder" + " userId:" + UserInfoModel.getInstance().getId()
//                + " projectId：" + mModel.id + " quantity：" + num + " type：" + mModel.projecttype
//                + " linkString：" + linkString + " key：" + Utils.getMD5Value(linkString)
//                + " receiverName：" + receiverName + " telephone：" + telephone + " address：" + address);

        OkHttpUtils.post().url(UrlAPI.HOST_URL + "project/submitOrder")
                .addParams("userId", UserInfoModel.getInstance().getId() + "")
                .addParams("projectId", mModel.id + "")
                .addParams("quantity", num + "")
                .addParams("type", mModel.projecttype + "")
                .addParams("key", Utils.getMD5Value(linkString))

                .addParams("receiverName", receiverName)
                .addParams("telephone", telephone)
                .addParams("address", address)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("创客onError:" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("创客onResponse:" + response);
                Type type = new TypeToken<Response1<MarkerOrderModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<MarkerOrderModel> response2 = gson.fromJson(response, type);
                if ("200".equals(response2.getErrCode())) {
                    Intent intent = new Intent(BuyProjectActivity.this, WXPayEntryActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("orderNumber", response2.getData().orderCoding);
                    extras.putDouble("totalPrice", response2.getData().totalAmount);
                    extras.putString("description", response2.getData().remark);
                    extras.putString("orderId", response2.getData().id + "");
                    if (projecttype == 104) {
                        extras.putString("flag", "chuangkeGoods");
                    } else {
                        extras.putString("flag", "chuangkeProject");
                    }
                    intent.putExtras(extras);
                    startActivity(intent);

                    // finishAllCST();

                } else {
                    Toast.makeText(BuyProjectActivity.this, response2.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getDefaultAddress() {
        /****************
         *修改者:  ycq
         *修改时间: 2017.01.09
         *修改原因: 接口用户信息加密
         * Describe:param  传递的参数
         Base64.getBase64(param)  参数加密
         Base64.getFromBase64(response)  返回数据解密
         ****************/
        JSONObject param = new JSONObject();
        try {
            param.put("userId", UserInfoModel.getInstance().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String defaultAddreaaUrl = UrlAPI.getDefaultAddreaaUrl(Base64.getBase64(param));
        PrintLog.e("得到默认收货地址URL:" + defaultAddreaaUrl);
        OkHttpUtils.get().url(defaultAddreaaUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Toast.makeText(ActivityDragonBall.this, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("得到默认收货地址URL:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                response = Base64.getFromBase64(response);
                PrintLog.e("得到默认收货地址:" + response);
                Type type = new TypeToken<Response1<AddressModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<AddressModel> resp = gson.fromJson(response, type);
                AddressModel data = resp.getData();
                if (data != null) {
                    rl_choose_address1.setVisibility(View.VISIBLE);
                    to_choose_address.setVisibility(View.GONE);
                    choose_address.setText(data.province + " " + data.city + " " + data.district + " " + data.addresss);
                    choose_name.setText(data.name);
                    choose_phone.setText(data.phone);
                    receiverName = data.name;
                    telephone = data.phone;
                    address = data.province + data.city + data.district + data.addresss;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE && resultCode == Constant.RESULT_CODE) {
            Bundle extras = data.getExtras();
            rl_choose_address1.setVisibility(View.VISIBLE);
            to_choose_address.setVisibility(View.GONE);
            AddressModel moder = (AddressModel) extras.getSerializable("AddressModel");
            choose_address.setText(moder.province + " " + moder.city + " " + moder.district + " " + moder.addresss);
            choose_name.setText(moder.name);
            choose_phone.setText(moder.phone);
            receiverName = moder.name;
            telephone = moder.phone;
            address = moder.province + moder.city + moder.district + moder.addresss;
        }

    }

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("支付");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
