package com.lzhy.moneyhll.home.dragonball;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.lzhy.moneyhll.custom.LongFlowLayout;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.dialog.PayWadDialog;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.loginOrRegister.LoginActivity;
import com.lzhy.moneyhll.me.mine.AddressActivity;
import com.lzhy.moneyhll.me.mine.userInfo.SetPayPassWordActivity;
import com.lzhy.moneyhll.model.AddressModel;
import com.lzhy.moneyhll.model.DragonBallModel;
import com.lzhy.moneyhll.model.PayGoodsModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.motorhome.PayResultActivity;
import com.lzhy.moneyhll.utils.Base64;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.lzhy.moneyhll.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.PAY_ALL_DRAGON_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.finishAllCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 龙珠商城商品确认下单
 */

public class GoodsSureActivity extends MySwipeBackActivity {
    private BaseTitlebar mTitlebar;
    private SimpleDraweeView mDraweeView;
    private TextView name;
    private TextView describe;
    private TextView goods_price;

    private TextView sign_up;
    private TextView number;
    private TextView sign_dowm;
    private Button payment;

    private LongFlowLayout mysizelist;
    private FilterTypeUI mSizeUI;
    private String type;
    private int typeid = 1;
    private List<String> titles1 = new ArrayList<>();
    private List<DragonBallModel.TypeModel> models;
    private TextView to_choose_address;
    private RelativeLayout rl_choose_address, rl_choose_address1;
    private TextView choose_name, choose_phone, choose_address;

    private int num = 1;

    private int parentId;
    private int quantity;
    private String receiverName;
    private String telephone;
    private String address;
    private String pwd;
    private PayWadDialog dismiss;
    private InputMethodManager imm;
    private DragonBallModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_goods_sure);
        /*
        * @author: ouyangmuyuan
        */
        addActivityCST(this);

        initView();
        initTitlebar();
        LoadingData();
    }

    private void initView() {
        imm = (InputMethodManager) getSystemService(GoodsSureActivity.this.INPUT_METHOD_SERVICE);
        parentId = getIntent().getIntExtra("id", 0);
        mDraweeView = (SimpleDraweeView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        describe = (TextView) findViewById(R.id.describe);
        goods_price = (TextView) findViewById(R.id.goods_price);
        choose_address = (TextView) findViewById(R.id.choose_address);
        mysizelist = (LongFlowLayout) findViewById(R.id.mysizelist);
        payment = (Button) findViewById(R.id.payment);
        to_choose_address = (TextView) findViewById(R.id.to_choose_address);
        choose_name = (TextView) findViewById(R.id.choose_name);
        choose_phone = (TextView) findViewById(R.id.choose_phone);
        choose_address = (TextView) findViewById(R.id.choose_address);
        rl_choose_address = (RelativeLayout) findViewById(R.id.rl_choose_address);
        rl_choose_address1 = (RelativeLayout) findViewById(R.id.rl_choose_address1);

        float space = 15f;
        mysizelist.setMarginRight(CommonUtil.dip2px(GoodsSureActivity.this, space));
        mysizelist.setMarginTop(CommonUtil.dip2px(GoodsSureActivity.this, space));
        mSizeUI = new FilterTypeUI(mysizelist);

        sign_up = (TextView) findViewById(R.id.sign_up);
        number = (TextView) findViewById(R.id.number);
        sign_dowm = (TextView) findViewById(R.id.sign_dowm);

        SetListener();
        if (UserInfoModel.getInstance().isLogin()) {
            getDefaultAddress();
        }
    }

    private void SetListener() {
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num++;
                number.setText(num + "");
                setPrice();
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
                    setPrice();
                }
            }
        });
        rl_choose_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserInfoModel.getInstance().isLogin()) {
                    Intent intent = new Intent(GoodsSureActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(GoodsSureActivity.this, AddressActivity.class);
                intent.putExtra("type", "choose");
                startActivityForResult(intent, Constant.REQUEST_CODE);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.valueOf(number.getText().toString());
                if (models != null) {
                    if ((type == null || type.length() == 0)) {
                        Toast.makeText(GoodsSureActivity.this, "请选择商品类型", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        for (int i = 0; i < models.size(); i++) {
                            if (type.equals(models.get(i).value)) {
                                typeid = models.get(i).id;
                            }
                        }
                    }
                }

                if (choose_address.getText().length() <= 8) {
                    Toast.makeText(GoodsSureActivity.this, "请选择收货地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*****************************************************
                 * 部门 ：龙之游 @ 技术部
                 * @Author 安卓开发工程师 xu cmm
                 * 时间：   2016/12/18 0018 22:12
                 *  如果用户没有登录  去登陆
                 *****************************************************/
                if (!UserInfoModel.getInstance().isLogin()) {
                    startActivity(new Intent(GoodsSureActivity.this, LoginActivity.class));
                    return;
                }
                /*if (UserInfoModel.getInstance().getPayPwd() == null) {
                    startActivity(new Intent(GoodsSureActivity.this, SetPayPassWordActivity.class));
                    return;
                }*/
                /*****************************************************
                 * 部门 ：龙之游 @ 技术部
                 * @Author 安卓开发工程师 xu cmm
                 * 时间：   2016/12/18 0018 22:10
                 * 判断是否有支付密码
                 *****************************************************/

                if (dismiss == null) {
                    final PayWadDialog.Builder builder = new PayWadDialog.Builder(GoodsSureActivity.this);
                    builder.setWatcher(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (editable.length() == 6) {
                                pwd = editable.toString();
                                /*****************************************************
                                 * 部门 ：龙之游 @ 技术部
                                 * @Author 安卓开发工程师 xu cmm
                                 * 时间：   2016/12/18 0018 22:13
                                 *  生成订单
                                 *****************************************************/
                                MakePearlOrder();
                                dismiss.dismiss();
                                editable.clear();
                            }
                        }
                    });
                    dismiss = builder.create();
                    dismiss.show();
                } else {
                    dismiss.show();
                }
                dismiss.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        imm.hideSoftInputFromWindow(((View) payment.getParent()).getWindowToken(), 0);
                    }
                });
                return;

            }

        });
    }

    /**
     * 去支付
     */
    private void MakePearlOrder() {
        pwd = Utils.getMD5Value(pwd);
        quantity = Integer.valueOf(number.getText().toString());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("parentId", String.valueOf(parentId));
        map.put("memberId", String.valueOf(UserInfoModel.getInstance().getId()));
        map.put("quantity", String.valueOf(quantity));
        map.put("receiverName", receiverName);
        map.put("telephone", telephone);
        map.put("address", address);
        map.put("id", String.valueOf(typeid));
        map.put("pwd", pwd);

        String linkString = CommonUtil.createLinkString(map) + "lzhyapp.hll.html";
        //String urlTest = "http://192.168.1.110:8080/lzhyapp/api/";//UserInfoModel.getInstance().getId()

        OkHttpUtils.post().url(UrlAPI.HOST_URL + "pearlmall/payment")
                .addParams("parentId", parentId + "").addParams("memberId", String.valueOf(UserInfoModel.getInstance().getId()))
                .addParams("quantity", quantity + "").addParams("pwd", pwd).addParams("receiverName", receiverName)
                .addParams("telephone", telephone).addParams("address", address).addParams("id", typeid + "")
                .addParams("key", Utils.getMD5Value(linkString))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {

                Type type = new TypeToken<Response1<PayGoodsModel>>() {}.getType();
                Gson gson = new Gson();

                Response1<PayGoodsModel> reaq = gson.fromJson(response, type);
                /*****************************************************
                 * 部门 ：龙之游 @ 技术部
                 * @Author 安卓开发工程师 xu cmm
                 * 时间：   2016/12/18 0018 22:20
                 * 后台(佳荣)返回 支付密码是否正确或是否设置 -1000：未设置；-1001：支付密码错误
                 *****************************************************/
                if (reaq.getMessage().equals("请前往个人中心设置支付密码")) {//去设置支付密码

                    Toast.makeText(GoodsSureActivity.this, reaq.getMessage(), Toast.LENGTH_LONG).show();

                    startActivity(new Intent(GoodsSureActivity.this, SetPayPassWordActivity.class));

                    return;
                }
                if (reaq.getMessage().equals("支付密码不正确")) {

                    Toast.makeText(GoodsSureActivity.this, reaq.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("200".equals(reaq.getErrCode())) {
                    PrintLog.e("+++++++++++++++++++" + reaq.getErrCode());
                    if (reaq.getData().paymentCash != null && reaq.getData().paymentCash > 0) {

                        Intent intent = new Intent(GoodsSureActivity.this, WXPayEntryActivity.class);

                        Bundle extras = new Bundle();

                        extras.putString("orderNumber", reaq.getData().orderNumber);
                        extras.putDouble("totalPrice", reaq.getData().paymentCash);
                        extras.putString("description", reaq.getData().title);
                        extras.putString("orderId", reaq.getData().parentId + "");

                        extras.putString("flag", "longzhushangcheng");

                        intent.putExtras(extras);
                        startActivity(intent);
                    } else {
                        // xu: 2016/11/26  todo跳轉到訂單詳情、
                        setCustomStatisticsKV(GoodsSureActivity.this,PAY_ALL_DRAGON_ID,"龙珠商城的全龙珠支付");//统计
                        Intent intent = new Intent(GoodsSureActivity.this, PayResultActivity.class);
                        intent.putExtra("flag", "longzhushangcheng");
                        startActivity(intent);
                        finishAllCST();//用于回退到租房车
                    }
                } else {
                    dismiss=null;
                    /*****************************************************
                     * 部门 ：龙之游 @ 技术部
                     * @Author 安卓开发工程师 xu cmm
                     * 时间：   2016/12/18 0018 22:17
                     * 支付密码不正确
                     *****************************************************/
                    Toast.makeText(GoodsSureActivity.this, reaq.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void setPrice() {
        if (data == null)
            return;
        String all_price = "龙珠商城价: ";
        if (data.cashPrice > 0) {
            all_price = all_price + data.cashPrice * num + "人民币+";
        }
        if (data.coinPrice > 0) {
            all_price = all_price + data.coinPrice * num + "龙币+";
        }
        if (data.pearlPrice > 0) {
            all_price = all_price + data.pearlPrice * num + "龙珠";
        }
        if (all_price.endsWith("+"))
            all_price=all_price.substring(0,all_price.length()-1);

        if (data.cashPrice <= 0 && data.coinPrice <= 0 && data.pearlPrice <= 0) {
            all_price="";
        }
        goods_price.setText(all_price);
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

    private void LoadingData() {

        String goodsSureUrl = UrlAPI.getGoodsSureUrl(parentId);
        PrintLog.e(" 确认商品URL:" + goodsSureUrl);
        OkHttpUtils.get().url(goodsSureUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e(" 确认商品:" + e);
            }

            @Override
            public void onResponse(String response, int id) {

                PrintLog.e(" 确认商品:" + response);
                Type type = new TypeToken<Response1<DragonBallModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<DragonBallModel> resp = gson.fromJson(response, type);
                data = resp.getData();
                updataView(data);
                setPrice();
            }
        });
    }

    private void updataView(DragonBallModel data) {
        mDraweeView.setImageURI(data.imageUrl);
        name.setText(data.title);
        describe.setText(data.description);
        name.setText(data.title);
        models = data.type;
        if (models != null && models.size() != 0) {
            for (int i = 0; i < models.size(); i++) {
                titles1.add(models.get(i).value);
            }
            setItemLayout(titles1, mysizelist, mSizeUI);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"确认购买");
    }

    public void getDefaultAddress() {
        /****************
         *修改者:  ycq
         *修改时间: 2017.01.09
         *修改原因: 接口用户信息加密
         * Describe:param  传递的参数
                    Base64.getBase64(param)  参数加密
                    Base64.getFromBase64(response)  参数解密
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
                response=Base64.getFromBase64(response);
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

    public class FilterTypeUI {

        private View selectItem;

        private List<String> arr;
        private LongFlowLayout layout;

        public FilterTypeUI(LongFlowLayout _layout) {
            this.layout = _layout;
        }

        public void clear() {
            this.arr = null;
            this.selectItem = null;
        }

        /**
         * 是否需要选择类型
         */
        public boolean needSelect() {
            return arr != null && selectItem == null;
        }

        public Object getSelectValue() {
            if (selectItem == null) {
                return null;
            }
            return selectItem.getTag();
        }

        /**
         * @param name 显示名
         * @param code 标记
         * @return 属性控件
         */
        private View createItem(String name, String code) {

            View v = LayoutInflater.from(layout.getContext()).inflate(R.layout.item_sure_goods_type, null);
            TextView tv = (TextView) v.findViewById(R.id.label_name);
            tv.setText(name);
            v.setTag(code);

            return v;

        }

        public void updateSelectView(View view) {

            if (selectItem != null) {
                TextView tv = (TextView) selectItem.findViewById(R.id.label_name);
                tv.setTextColor(0xff666666);
                tv.setBackgroundResource(R.drawable.bg_00ffffff_1dp_r5dp);
                tv.setPadding(CommonUtil.Dp2Px(view.getContext(), 11f), 0, CommonUtil.Dp2Px(view.getContext(), 11f), 0);
            }

            if (selectItem == view) {
                selectItem = null;
            } else {
                TextView tv = (TextView) view.findViewById(R.id.label_name);
                tv.setTextColor(0xff000000);
                tv.setBackgroundResource(R.drawable.bg_button_rad_r5dp);
                tv.setPadding(CommonUtil.Dp2Px(view.getContext(), 11f), 0, CommonUtil.Dp2Px(view.getContext(), 11f), 0);
                selectItem = view;
            }
        }

        /**
         * 创建一个textView的监听
         *
         * @param v
         */
        private void createListener(View v) {

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    updateSelectView(view);
                    if (mOnClickListener != null)
                        mOnClickListener.onClick(view);
                }
            });
        }

        public View.OnClickListener mOnClickListener;

        /**
         * @param arr 集合
         */
        public void updateItem(List<String> arr) {

            this.arr = arr;
            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i) == null || arr.get(i).length() == 0) {
                    return;
                }
                View v = createItem(arr.get(i), arr.get(i));
                createListener(v);
                layout.addView(v);
            }
        }

    }

    private void setItemLayout(List<String> list, LongFlowLayout itemlayout, FilterTypeUI mFilterTypeUI) {

        itemlayout.removeAllViews();
        mFilterTypeUI.clear();

        if (list == null) {
            ((View) itemlayout.getParent()).setVisibility(View.GONE);
            return;
        }

        int s = list.size();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < s; i++) {
            String str = list.get(i);
            if (str == null || str.length() == 0) {
// TODO_XBB
            } else {
                list2.add(str);
            }
        }

        list.clear();
        list.addAll(list2);

        if (list.size() <= 0) {
            ((View) itemlayout.getParent()).setVisibility(View.GONE);
            return;
        }
        ((View) itemlayout.getParent()).setVisibility(View.VISIBLE);
        mFilterTypeUI.updateItem(list);

        mFilterTypeUI.mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = getStringForType();
                if (type != null) {
                    PrintLog.e(type);
                } else {
                    type = null;
                }
            }
        };
    }

    private String getStringForType() {
        String value = null;
        String str = getSecletTypeValue(mSizeUI, null, value);
        if ("-1".equals(str)) {

        } else {
            value = str;
        }
        return value;
    }

    private String getSecletTypeValue(FilterTypeUI mFilterTypeUI, String msg, String value) {

        if (mFilterTypeUI.needSelect()) {
            if (msg != null)
                Toast.makeText(GoodsSureActivity.this, msg, Toast.LENGTH_SHORT).show();
            return "-1";
        } else if (mFilterTypeUI.getSelectValue() != null) {
            if (value == null) {
                value = (String) mFilterTypeUI.getSelectValue();
            } else {
                value = value + "," + mFilterTypeUI.getSelectValue();
            }
        }
        return value;
    }


}
