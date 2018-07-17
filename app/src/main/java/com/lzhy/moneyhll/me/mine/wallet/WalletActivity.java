package com.lzhy.moneyhll.me.mine.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/11/3.
 * 钱包
 */
public class WalletActivity extends MySwipeBackActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

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
        LinearLayout myEarnings = (LinearLayout) findViewById(R.id.ll_my_earnings);
        LinearLayout cashTX = (LinearLayout) findViewById(R.id.ll_cash_ti_xian);
        LinearLayout myTeam = (LinearLayout) findViewById(R.id.ll_my_team);

        myEarnings.setOnClickListener(this);
        cashTX.setOnClickListener(this);
        myTeam.setOnClickListener(this);
    }


    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"钱包");
    }

    @Override
    public void onClick(View view) {
//        beforeClickInMine();
        switch (view.getId()) {
            case R.id.ll_my_earnings:
                SharePrefenceUtils.put(this, "withDrawType", "1");
                startActivity(new Intent(this, MyEarningActivity.class));
                break;
            case R.id.ll_cash_ti_xian:
                SharePrefenceUtils.put(this, "withDrawType", "2");
                startActivity(new Intent(this, TixianActivity.class));

                break;
            case R.id.ll_my_team:
                startActivity(new Intent(this, MyTeamActivity.class));

                break;
        }
    }
}
