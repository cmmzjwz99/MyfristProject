package com.lzhy.moneyhll.me.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.wxapi.WXPayEntryActivity;

import java.util.Date;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by lzy on 2016/12/17.
 * 龙珠充值
 */
public class ChongzhiActivity extends MySwipeBackActivity {
    private BaseTitlebar mTitlebar;

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;

    private EditText edittext;
    private TextView all_money;
    private Button but_chongzhi;

    private String number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chong_zhi);

        addActivityCST(this);
        initTitlebar();
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);

        edittext = (EditText) findViewById(R.id.edittext);
        all_money = (TextView) findViewById(R.id.all_money);
        but_chongzhi = (Button) findViewById(R.id.but_chongzhi);
        setOnClick();
    }

    private void setOnClick() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNoClick();
                edittext.setText("");
                view.setBackgroundResource(R.drawable.bg_fefaea_1sp_ffbf34_r3dp);
                String str = ((TextView) view).getText().toString();
                number = str.substring(0, str.length() - 2);
                all_money.setText("¥" + number);

            }
        };
        text1.setOnClickListener(listener);
        text2.setOnClickListener(listener);
        text3.setOnClickListener(listener);
        text4.setOnClickListener(listener);
        text5.setOnClickListener(listener);
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNoClick();
                all_money.setText("¥0");
                edittext.setFocusable(true);
                edittext.setBackgroundResource(R.drawable.bg_fefaea_1sp_ffbf34_r3dp);
            }
        });
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                number = editable.toString();
                if (number.length() == 0) {
                    all_money.setText("¥0");
                } else {
                    all_money.setText("¥" + number);
                }
            }
        });

        but_chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number == null || "".equals(number)) {
                    Toast.makeText(ChongzhiActivity.this,"请选择充值金额",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.valueOf(number)<50) {
                    Toast.makeText(ChongzhiActivity.this,"至少充值50龙珠",Toast.LENGTH_SHORT).show();
                    return;
                }
                toPay();
            }
        });
    }


    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"龙珠充值");
        /**
         *修改者:  ycq
         *修改时间: 2017.01.12
         *修改原因: 添加龙珠充值记录入口
         */
        TextView rightTv=mTitlebar.getRightTextButton();
        rightTv.setText("记录");
        rightTv.setTextColor(getResources().getColor(R.color.action_bar));
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChongzhiActivity.this, RechargeRecordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setNoClick() {
        text1.setBackgroundResource(R.drawable.bg_fbfbfb_1sp_f6f6f7_r3dp);
        text2.setBackgroundResource(R.drawable.bg_fbfbfb_1sp_f6f6f7_r3dp);
        text3.setBackgroundResource(R.drawable.bg_fbfbfb_1sp_f6f6f7_r3dp);
        text4.setBackgroundResource(R.drawable.bg_fbfbfb_1sp_f6f6f7_r3dp);
        text5.setBackgroundResource(R.drawable.bg_fbfbfb_1sp_f6f6f7_r3dp);
        edittext.setBackgroundResource(R.drawable.bg_fbfbfb_1sp_f6f6f7_r3dp);
    }
    /************************************************************
     *@Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/21 10:41
     * 注释:
    ************************************************************/
    private void toPay(){
        Intent intent = new Intent(ChongzhiActivity.this, WXPayEntryActivity.class);
        Bundle extras = new Bundle();
        extras.putString("commodityType", "100102");
        extras.putString("orderNumber", UserInfoModel.getInstance().getId()+"");
        extras.putDouble("totalPrice", Double.valueOf(number));
        extras.putString("description", "龙珠充值");
        extras.putString("orderId", "");
        extras.putString("flag", "dragon_ball_charge");

        intent.putExtras(extras);
        startActivity(intent);
    }

    public String getTime(){
        Date dt= new Date();
        Long time= dt.getTime();
        String  str=String.valueOf(time);
        return str;
    }
}
