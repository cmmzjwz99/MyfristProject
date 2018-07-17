package com.lzhy.moneyhll.me.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.me.makerorder.MakerGoodsOrderActivity;
import com.lzhy.moneyhll.me.makerorder.MakerPlayOrderActivity;

import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/10/26.
 * 我的订单
 */
public class MyOrderActivity extends MySwipeBackActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        initView();
        initTitlebar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        LinearLayout shoppingOrder =  (LinearLayout) findViewById(R.id.ll_shopping_order);
        LinearLayout projectOrder = (LinearLayout) findViewById(R.id.ll_project_order);
        LinearLayout makerGoodsOrder = (LinearLayout) findViewById(R.id.ll_maker_goods_order);
        LinearLayout makerPlayOrder = (LinearLayout) findViewById(R.id.ll_maker_play_order);

        shoppingOrder.setOnClickListener(this);
        projectOrder.setOnClickListener(this);
        makerGoodsOrder.setOnClickListener(this);
        makerPlayOrder.setOnClickListener(this);
    }
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"我的订单");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_shopping_order:
                startActivity(new Intent(MyOrderActivity.this,GoodsOrderActivity.class));
                break;
            case R.id.ll_project_order:
                startActivity(new Intent(MyOrderActivity.this,ProjectOrderActivity.class));
                break;
            case R.id.ll_maker_goods_order:
                startActivity(new Intent(MyOrderActivity.this,MakerGoodsOrderActivity.class));
                break;
            case R.id.ll_maker_play_order:
                startActivity(new Intent(MyOrderActivity.this,MakerPlayOrderActivity.class));
                break;

        }
    }
}
