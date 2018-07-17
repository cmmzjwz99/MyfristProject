package com.lzhy.moneyhll.me.order;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import static com.lzhy.moneyhll.me.order.AppointmentActivity.MostType.After_end;
import static com.lzhy.moneyhll.me.order.AppointmentActivity.MostType.Travelling;
import static com.lzhy.moneyhll.me.order.AppointmentActivity.MostType.Wait_account;
import static com.lzhy.moneyhll.me.order.AppointmentActivity.MostType.Wait_get_car;
import static com.lzhy.moneyhll.me.order.AppointmentActivity.MostType.Wait_pay;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/10/26.
 * 我的预约
 */
public class AppointmentActivity extends MySwipeBackActivity implements ViewPager.OnPageChangeListener {

    RadioButton wait_pay;
    RadioButton wait_get_car;
    RadioButton travelling;
    RadioButton wait_account;
    RadioButton after_end;

    private ViewPager mViewPager;
    private MyAdapter mAdapter;
    private List<MostType> typeList;
    private List<RadioButton> radioLists = null;

    private String finishZFC = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointent);

        initTitlebar();
        initView();
        initListener();
        initAdapter();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        radioLists = new ArrayList<>();
        wait_pay = (RadioButton) findViewById(R.id.rb_a1);
        wait_get_car = (RadioButton) findViewById(R.id.rb_a2);
        travelling = (RadioButton) findViewById(R.id.rb_a3);
        wait_account = (RadioButton) findViewById(R.id.rb_a4);
        after_end = (RadioButton) findViewById(R.id.rb_a5);

        mViewPager = (ViewPager) findViewById(R.id.myviewpager);

        Intent intent = getIntent();
        finishZFC = intent.getStringExtra("zufangche");
        radioLists.add(wait_pay);
        radioLists.add(wait_get_car);
        radioLists.add(travelling);
        radioLists.add(wait_account);
        radioLists.add(after_end);
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        for (int i = 0; i < radioLists.size(); i++) {
            final int finalI = i;
            radioLists.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(finalI);
                }
            });
        }
    }

    private void initAdapter() {
        typeList = new ArrayList<>();
        typeList.add(Wait_pay);
        typeList.add(Wait_get_car);
        typeList.add(Travelling);
        typeList.add(Wait_account);
        typeList.add(After_end);

        mAdapter = new MyAdapter(getSupportFragmentManager(), typeList,
                mViewPager);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < radioLists.size(); i++) {
            if (i == position) {
                radioLists.get(i).setChecked(true);
            } else {
                radioLists.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 适配器
     */
    protected class MyAdapter extends FragmentStatePagerAdapter {
        private List<MostType> typeList;
        private ViewPager mPager;

        public MyAdapter(FragmentManager fm, List<MostType> typeList, ViewPager pager) {
            super(fm);
            this.typeList = typeList;
            mPager = pager;
        }

        @Override
        public int getCount() {
            return typeList == null ? 0 : typeList.size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment _fragment = null;
            if (typeList.get(position) == MostType.Wait_pay) {
                _fragment = new AppointmentFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 9);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.Wait_get_car) {
                _fragment = new AppointmentFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 3);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.Travelling) {
                _fragment = new AppointmentFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 4);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.Wait_account) {
                _fragment = new AppointmentFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 5);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.After_end) {
                _fragment = new AppointmentFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 6);
                _fragment.setArguments(bundle);
            }
            return _fragment;
        }

        // 初始化每个页卡选项
        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {

            return super.instantiateItem(arg0, arg1);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    //初始化头
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"我的预约");
    }

    public  enum MostType {
        /**
         * 代缴清
         */
        Wait_pay,
        /**
         * 待提车
         */
        Wait_get_car,
        /**
         * 旅途中
         */
        Travelling,
        /**
         * 待结算
         */
        Wait_account,
        /**
         * 已完结
         */
        After_end,
    }
}
