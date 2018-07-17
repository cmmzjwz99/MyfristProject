package com.lzhy.moneyhll.me.mine.wallet;


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

/**
 * Created by cmm on 2016/11/7.
 * 我的收益
 */

public class MyEarningActivity extends MySwipeBackActivity implements ViewPager.OnPageChangeListener {

    private RadioButton dragon_ball_earning;
    private RadioButton dragon_coin_earning;

    private ViewPager mViewPager;
    private MyAdapter mAdapter;
    private List<RadioButton> radioLists;
    private List<MostType> typeList;
    private int type = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earning);

        initView();
        initTitlebar();
        initListener();
        initAdapter();
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("我的收益");
        titlebar.setRightText("查看明细", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyEarningActivity.this, EarningActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    private void initView() {

        radioLists = new ArrayList<>();
        dragon_ball_earning = (RadioButton) findViewById(R.id.dragon_ball_earning);
        dragon_coin_earning = (RadioButton) findViewById(R.id.dragon_coin_earning);

        mViewPager = (ViewPager) findViewById(R.id.myviewpager);

        radioLists.add(dragon_ball_earning);
        radioLists.add(dragon_coin_earning);
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
        typeList.add(MostType.dragon_ball_earning);
        typeList.add(MostType.dragon_coin_earning);

        mAdapter = new MyAdapter(getSupportFragmentManager(), typeList,
                mViewPager);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < radioLists.size(); i++) {
            if (i == position) {
                radioLists.get(i).setChecked(true);
                type=i;
            } else {
                radioLists.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {
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
            if (typeList.get(position) == MostType.dragon_ball_earning) {
                //龙珠收益
                _fragment = new MyEarningFm();
                Bundle bundle = new Bundle();
                bundle.putString("payType", "pears");
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.dragon_coin_earning) {
                //龙币收益
                _fragment = new MyEarningFm();
                Bundle bundle = new Bundle();
                bundle.putString("payType","coins");
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

    public enum MostType {
        /**
         * 龙珠收益
         */
        dragon_ball_earning,
        /**
         * 龙币收益
         */
        dragon_coin_earning,
    }
}
