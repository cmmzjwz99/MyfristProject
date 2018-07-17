package com.lzhy.moneyhll.me.mine;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.me.mine.fragment.HistoryFragment;
import com.lzhy.moneyhll.me.mine.fragment.NowFragment;

import java.util.ArrayList;
import java.util.List;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/10/26.
 * 我的房车劵
 */
public class MotorHomeRollActivity extends MySwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_home_roll);

        addActivityCST(this);
        initView();
        initTitlebar();
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"我的房车劵");
        titlebar.setRightButton(R.mipmap.icon_tianjia, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MotorHomeRollActivity.this, BindRollActivity.class));
            }
        });
    }

    List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        RadioGroup rgRoll = (RadioGroup) findViewById(R.id.rg_roll);
        FrameLayout fmMotorRoll = (FrameLayout) findViewById(R.id.fm_motor_roll);

        fragments.add(new NowFragment());
        fragments.add(new HistoryFragment());
        rgRoll.setOnCheckedChangeListener(checkedListener);
        ((RadioButton) rgRoll.getChildAt(0)).setChecked(true);


        getFragmentManager().beginTransaction().replace(R.id.fm_motor_roll, new NowFragment()).commit();
    }

    private RadioGroup.OnCheckedChangeListener checkedListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            View child = group.findViewById(checkedId);
            int index = group.indexOfChild(child);
            int count=group.getChildCount();
            for (int i = 0; i <count ; i++) {
                if(i==index){
                    ((RadioButton) group.getChildAt(i)).setChecked(true);
                }else{
                    ((RadioButton) group.getChildAt(i)).setChecked(false);
                }
            }
            Fragment fragment = fragments.get(index);

            getFragmentManager().beginTransaction().replace(R.id.fm_motor_roll, fragment).commit();
        }
    };
}
