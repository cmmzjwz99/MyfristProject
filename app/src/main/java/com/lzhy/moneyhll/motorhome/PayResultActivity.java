package com.lzhy.moneyhll.motorhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.snapup.SnapItUpBuyActivity;
import com.lzhy.moneyhll.home.snapup.SnapItUpDetailsActivity;
import com.lzhy.moneyhll.me.makerorder.MakerGoodsOrderActivity;
import com.lzhy.moneyhll.me.makerorder.MakerPlayOrderActivity;
import com.lzhy.moneyhll.me.mine.MotorHomeRollActivity;
import com.lzhy.moneyhll.me.order.AppointmentActivity;
import com.lzhy.moneyhll.me.order.GoodsOrderActivity;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.finishAllCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.wxapi.WXPayEntryActivity.cflag;

public final class PayResultActivity extends MySwipeBackActivity implements View.OnClickListener {

    private Button leftbtn;
    private Button rightbtn;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        this.rightbtn = (Button) findViewById(R.id.right_btn);
        this.leftbtn = (Button) findViewById(R.id.left_btn);
        this.leftbtn.setOnClickListener(this);
        this.rightbtn.setOnClickListener(this);
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.left_btn) {
            if(flag.equals("snapup")){
                SnapItUpBuyActivity.context.finish();
                SnapItUpDetailsActivity.context.finish();
            }
            finish();//干掉自己，回退到入口，前提是，从入口，

        } else {
            //addActivityCST(this);
            Intent it = null;
            if (flag.equals("longzhushangcheng")||flag.equals("snapup")) {

                it = new Intent(this, GoodsOrderActivity.class);
                startActivity(it);
                finish();//干掉自己，去查看订单详百年
//                Log.i("xxx", "onClick: ----------1--------------------");
                finishAllCST();

            }
            if (flag.equals("chuangkeGoods")) {//跳轉到创客商品订单

                it = new Intent(this, MakerGoodsOrderActivity.class);
                it.putExtra("type",1);
                startActivity(it);
//                Log.i("xxx", "onClick: --------2--------------------");
                finish();//干掉自己，去查看订单详百年
                finishAllCST();

            }
            if (flag.equals("chuangkeProject")) {//跳轉到创客项目订单

                it = new Intent(this, MakerPlayOrderActivity.class);
                it.putExtra("type",1);
                startActivity(it);
//                Log.i("xxx", "onClick: --------2--------------------");
                finish();//干掉自己，去查看订单详百年
                finishAllCST();

            }
            if (flag.equals("zufangche")) {//我的预约

                it = new Intent(this, AppointmentActivity.class);
//                Log.i("xxx", "onClick: --------3-------------------");
                startActivity(it);
                finish();//干掉自己，去查看订单详百年
                finishAllCST();
            }
            if (flag.equals("makerorder")) {//创客订单

                finish();//干掉自己，去查看订单详百年
            }
            if (flag.equals("appointmentorder")) {//我的预约订单

                finish();//干掉自己，去查看订单详百年
            }
            if (flag.equals("buystock")) {
                it = new Intent(this, MotorHomeRollActivity.class);
//                Log.i("xxx", "onClick: --------3-------------------");
                startActivity(it);
                finish();//干掉自己，去查看订单详百年
                finishAllCST();
            }
        }
        cflag = null;
    }
}
