package com.lzhy.moneyhll.me.maker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.me.mine.ScanActivity;
import com.lzhy.moneyhll.me.mine.wallet.TixianActivity;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 创建者;xu
 * 时间:2016/12/12 11:15        我是创客
 * 注释:我是创客
 */
public class IamMakerActivity extends MySwipeBackActivity implements View.OnClickListener {

    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iam_maker_activtiy);

        addActivityCST(this);

        linearLayout1 = (LinearLayout) findViewById(R.id.maker_mine1);
        linearLayout2 = (LinearLayout) findViewById(R.id.maker_mine2);
        linearLayout3 = (LinearLayout) findViewById(R.id.maker_mine3);
        linearLayout4 = (LinearLayout) findViewById(R.id.maker_mine4);
        linearLayout5 = (LinearLayout) findViewById(R.id.maker_mine5);
        linearLayout6 = (LinearLayout) findViewById(R.id.maker_mine6);

        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
        linearLayout4.setOnClickListener(this);
        linearLayout5.setOnClickListener(this);
        linearLayout6.setOnClickListener(this);

        initTitlebar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    @Override
    public void onClick(View view) {
        AlphaAnimation alphaAni = new AlphaAnimation(0.3f, 1.0f);
        alphaAni.setDuration(100);                // 设置动画效果时间
        view.startAnimation(alphaAni);        // 添加光效动画到VIew
        int id = view.getId();
        if (id == R.id.maker_mine1) {//我的项目
            startActivity(new Intent(this, MyProjectActivity.class));
        } else if (id == R.id.maker_mine2) {//商品类订单
            startActivity(new Intent(this, GoodsOrderActivity.class));
        } else if (id == R.id.maker_mine3) {//游玩类订单
            startActivity(new Intent(this, PlayOrderActivity.class));
        } else if (id == R.id.maker_mine4) {//数字码核销
            startActivity(new Intent(this, NumberVerificationActivity.class));
        } else if (id == R.id.maker_mine5) {//扫一扫核销
            startActivity(new Intent(this, ScanActivity.class));
        } else if (id == R.id.maker_mine6) {//提现
            Intent intent = new Intent(this, TixianActivity.class);
            SharePrefenceUtils.put(this, "withDrawType", "2");
            startActivity(intent);
        }
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar, "我是创客");
    }
}
