package com.lzhy.moneyhll.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.alipay.PayResult;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.motorhome.PayResultActivity;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.lzhy.moneyhll.R.id.price;
import static com.lzhy.moneyhll.api.UrlAPI.URL_PAY;
import static com.lzhy.moneyhll.constant.Constant.GATEWAY_ALIPAY;
import static com.lzhy.moneyhll.constant.Constant.GATEWAY_WX;
import static com.lzhy.moneyhll.constant.Constant.MD5_KEY;
import static com.lzhy.moneyhll.constant.Constant.MERCHANTID_ALIPAY;
import static com.lzhy.moneyhll.constant.Constant.MERCHANTID_WX;
import static com.lzhy.moneyhll.constant.Constant.PAY_ALI_ID;
import static com.lzhy.moneyhll.constant.Constant.PAY_WX_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.finishAllCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilCheckMix.isWeixinAvilible;


public final class WXPayEntryActivity extends MySwipeBackActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI wxApi;
//	private TextView pay_res_tv;

    private BaseTitlebar mTitlebar;
    private TextView alipay_pay;
    private TextView wechat_pay;

    private TextView order_number, total_price;
    private String orderNumber, description;
    private double totalPrice;

    Bundle extras;

    private String platform;

    private static final int SDK_PAY_FLAG = 1;

    private String md5SignAli;
    private String orderId;
    public static String cflag;
    /*
    *@Author xu
    *2016/11/21 10:18
    *annotation:支付宝
    */
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((HashMap<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        setCustomStatisticsKV(WXPayEntryActivity.this,PAY_ALI_ID,"支付宝支付");//统计
                        //跳转到结果页
                        Intent it = new Intent(WXPayEntryActivity.this, PayResultActivity.class);

                        it.putExtra("flag", cflag);
                        startActivity(it);
                        finishAllCST();//用于结果页回退到租房车
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(WXPayEntryActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };
    private int GoodsNum;
    private double perPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivityCST(this);
        setContentView(R.layout.act_choose_pay_type);

        initView();
        initTitlebar();
        initWXPay();
        initData();

        setListenerALI();
        setListenerWX();
        /*
        *@Author xu
        *2016/11/21 10:12
        *annotation:在你需要支付的页面注册微信APP-ID，一般在oncreat里
        */
        addActivityCST(this);
        wxApi = WXAPIFactory.createWXAPI(this, MERCHANTID_WX);
        wxApi.handleIntent(getIntent(), this);


    }

    //---
    private void initData() {
        platform = "android";
        extras = getIntent().getExtras();
        orderNumber = extras.getString("orderNumber");
        totalPrice = Double.valueOf(String.format("%.2f", extras.getDouble("totalPrice")));
        description = extras.getString("description");
        orderId = extras.getString("orderId");
        if (extras.getString("flag") == null) {
            cflag = "";

        } else {
            cflag = extras.getString("flag");//用于支付回退的白标志
        }

        order_number.setText("订单号：" + orderNumber);
        if ("100102".equals(extras.getString("commodityType"))) {
            order_number.setText("订单号：" + CommonUtil.getTime());
        }
        total_price.setText(String.valueOf(totalPrice));
    }

    private void initWXPay() {

        wxApi = WXAPIFactory.createWXAPI(this, MERCHANTID_WX, true);
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        wxApi.registerApp(MERCHANTID_WX);

    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"选择支付方式");
    }

    private void initView() {

        alipay_pay = (TextView) findViewById(R.id.alipay_pay);
        wechat_pay = (TextView) findViewById(R.id.wechat_pay);

        order_number = (TextView) findViewById(R.id.order_number);
        total_price = (TextView) findViewById(price);
    }

    private void setListenerALI() {

        /*
        *@Author xu
        *2016/11/21 9:20
        *annotation:支付宝
        */
        alipay_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wechat_pay.getAlpha()!=1.0f) {
                    Toast.makeText(WXPayEntryActivity.this, "请稍后…", Toast.LENGTH_SHORT).show();
                    return;
                }
                alipay_pay.setAlpha(0.1F);
                setCustomStatisticsKV(WXPayEntryActivity.this,PAY_ALI_ID,"支付宝支付");//统计
                getSign(GATEWAY_ALIPAY);
                final GetBuilder builder = OkHttpUtils.get()
                        .url(URL_PAY)
                        .addParams("?platform", platform) //支付平台
                        .addParams("gateway", GATEWAY_ALIPAY)//平台代码
                        .addParams("merchantId", MERCHANTID_ALIPAY)//商户id
                        .addParams("summary", description)//商品描述
                        .addParams("totalFee", String.valueOf(totalPrice))//总价
                        .addParams("tradeNo", orderNumber)//编号
                        .addParams("sign", md5SignAli);//签名

                if ("100102".equals(extras.getString("commodityType"))) {
                    builder.addParams("commodityType", "100102");
                }

                builder.build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        alipay_pay.setAlpha(1F);
//                        Log.i("xxx", "onError: " + e);
                        Toast.makeText(WXPayEntryActivity.this, "连接服务器失败！！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        alipay_pay.setAlpha(1F);
//                        Log.i("xxx", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getString("errCode").equals("200")) {
                                Toast.makeText(WXPayEntryActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
                                return;
                            }
                            final String data = jsonObject.getString("data").replace("\\", "");
                            Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask(WXPayEntryActivity.this);
                                    // 调用支付接口，获取支付结果
                                    Map<String, String> result = alipay.payV2(data, true);
                                    Message msg = mHandler.obtainMessage();

                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    Bundle bundle = new Bundle();

                                    // TODO_XBB: 2016/12/31   需要传递过来  商品数量信息和单价
                                    bundle.putDouble("totalPrice",totalPrice);//商品总价
                                    bundle.putString("orderNumber",orderNumber);//商品编号
                                    bundle.putInt("GoodsNum",GoodsNum);//商品数量
                                    bundle.putDouble("perPrice",perPrice);//商品单价
//                                    bundle.putInt("source",2);//支付平台
                                    msg.setData(bundle);
                                    mHandler.sendMessage(msg);
                                }
                            };

                            Thread payThread = new Thread(payRunnable);
                            payThread.start();

                        } catch (JSONException e) {
                            PrintLog.e(e.toString());
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    private void setListenerWX() {
          /*
        *@Author xu
        *2016/11/21 9:20
        *annotation:微信
        */
        wechat_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alipay_pay.getAlpha()!=1.0f) {
                    Toast.makeText(WXPayEntryActivity.this, "请稍后…", Toast.LENGTH_SHORT).show();
                    return;
                }
                wechat_pay.setAlpha(0.1F);
                if (!isWeixinAvilible(WXPayEntryActivity.this)) {
                    Toast.makeText(WXPayEntryActivity.this, "请安装微信", Toast.LENGTH_SHORT).show();
                    return;
                }
                //String urlTest = "http://192.168.2.108:8090/lzhyapp/v1/api/pay/gateway";
                Toast.makeText(WXPayEntryActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
                getSign(GATEWAY_WX);
                GetBuilder builder = OkHttpUtils.get()
                        .url(URL_PAY)
                        .addParams("?platform", platform) //支付平台
                        .addParams("gateway", GATEWAY_WX)//平台代码
                        .addParams("merchantId", MERCHANTID_ALIPAY)//商户id
                        .addParams("summary", description)//商品描述
                        .addParams("totalFee", String.valueOf(totalPrice))//总价
                        .addParams("tradeNo", orderNumber)//编号
                        .addParams("sign", md5SignAli);//签名
                if ("100102".equals(extras.getString("commodityType"))) {
                    builder.addParams("commodityType", "100102");
                }

                builder.build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        wechat_pay.setAlpha(1F);
                        Toast.makeText(WXPayEntryActivity.this, "连接服务器失败！！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String content, int id) {
                        wechat_pay.setAlpha(1F);
//                        Log.i("xxx", "initData: " + md5SignAli);
//                        Log.i("xxx", "wxonResponse: " + content);
                        try {

                            JSONObject json = new JSONObject(content);
                            if (!json.getString("errCode").equals("200")) {
                                Toast.makeText(WXPayEntryActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            final String data = json.getString("data");
//                            Log.i("xxx", "onResponse: " + data);
                            /*
                            *@Author xu
                            *2016/11/21 13:56
                            *annotation:解析字符串 data
                         */
                            HashMap<String, String> mapWX = splitData(data);
                            if (null != mapWX) {
                                PayReq req = new PayReq();
                                req.appId = mapWX.get("appid");
                                req.partnerId = mapWX.get("partnerid");
                                req.prepayId = mapWX.get("prepayid");
                                req.nonceStr = mapWX.get("noncestr");
                                req.timeStamp = mapWX.get("timestamp");
                                req.packageValue = mapWX.get("package");
                                req.sign = mapWX.get("sign");
                                req.extData = "app data"; // optional
                                // Toast.makeText(ChoosePayTypeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                boolean res = wxApi.sendReq(req);
                                if(!res) {
                                    Toast.makeText(WXPayEntryActivity.this, "打开微信支付失败!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                                Toast.makeText(WXPayEntryActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
//                            Log.e("PAY_GET", "异常：" + e.getMessage());
                            Toast.makeText(WXPayEntryActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(ChoosePayTypeActivity.this, "微信在线支付", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    private HashMap<String, String> splitData(String data) {
        String[] dataArray = data.split("&");

        HashMap<String, String> mapPay = new HashMap<>();

        for (int i = 0; i < dataArray.length; i++) {
            String[] s = dataArray[i].split("=");
            if (s[0].equals("package")) {
                s[1] = "Sign=WXPay";
            }
            mapPay.put(s[0], s[1]);
//            Log.i("xxx", "splitData: " + s[0] + " = " + s[1]);
//            Log.i("xxx", "splitData: -----------------------------");
        }
        return mapPay;
    }

    private void getSign(String gateway) {

        HashMap<String, String> map = new HashMap<>();
        map.put("gateway", gateway);
        map.put("merchantId", MERCHANTID_ALIPAY);
        map.put("summary", description);
        map.put("totalFee", String.format("%.2f", totalPrice));
        map.put("tradeNo", orderNumber);
        if("100102".equals(extras.getString("commodityType"))){
            map.put("commodityType", "100102");
        }
        String linkString = CommonUtil.createLinkString(map) + MD5_KEY;
//        Log.i("xxx", "initData: 排序" + linkString);

        md5SignAli = Utils.getMD5Value(linkString);

//        Log.i("xxx", "initData: " + md5SignAli);

    }

    //-------------------
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        //finishAllCST();//用于结果页回退到租房车
    }

    /*
    *@Author xu
	*2016/11/21 10:07
	*annotation:得到支付结果
	*/

    @Override
    public void onResp(BaseResp resp) {

//        Log.i("xxx", "onResp: ====================================================");
        wechat_pay.setAlpha(1F);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                setCustomStatisticsKV(WXPayEntryActivity.this,PAY_WX_ID,"微信支付");//统计
                //跳转到结果页
                Intent it = new Intent(this, PayResultActivity.class);

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                it.putExtra("flag", cflag);
                startActivity(it);
                finish();

            }else if (resp.errCode ==-1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_tip);
                builder.setMessage("支付失败！");
//                builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
                builder.show();
                //finish();
                //finishAllCST();//用于结果页回退到租房车
            }else if (resp.errCode ==-2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_tip);
                builder.setMessage("用户取消微信支付！");
                //setCustomStatisticsKV(WXPayEntryActivity.this,PAY_WX_ID,"用户取消微信支付");//统计
//                builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
                builder.show();
                //finish();
                //finishAllCST();//用于结果页回退到租房车
            }else if (resp.errCode ==-3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_tip);
                builder.setMessage("发送失败");
//                builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
                builder.show();
                //finish();
                //finishAllCST();//用于结果页回退到租房车
            }

        }

    }
}