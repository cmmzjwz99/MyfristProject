package com.lzhy.moneyhll.me.mine.give;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.CenterRadioButton;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * 赠送记录 龙珠赠送和房车券赠送
 */

public class GiveRecordActivity extends MySwipeBackActivity implements ViewPager.OnPageChangeListener {
    private CenterRadioButton rb_dragon_ball;
    private CenterRadioButton rb_motorhomes_stock;

    private ViewPager mViewPager;
    private MyAdapter mAdapter;
    private List<CenterRadioButton> radioLists;
    private List<MostType> typeList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_give_record);

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
        titlebar.setTitle("记录");
    }
    @Override
    protected void onResume() {
        disparityLogin();
        super.onResume();
    }
    private void initView() {

        radioLists = new ArrayList<>();
        rb_dragon_ball = (CenterRadioButton) findViewById(R.id.rb_dragon_ball);
        rb_motorhomes_stock = (CenterRadioButton) findViewById(R.id.rb_motorhomes_stock);

        mViewPager = (ViewPager) findViewById(R.id.myviewpager);

        radioLists.add(rb_dragon_ball);
        radioLists.add(rb_motorhomes_stock);
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
        typeList.add(MostType.dragon_ball);
        typeList.add(MostType.motorhomes_stock);

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
            if (typeList.get(position) == MostType.dragon_ball) {
                //龙珠
                _fragment = new GiveRecordFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 1);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.motorhomes_stock) {
                //房车劵
                _fragment = new GiveRecordFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 2);
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
         * 龙珠
         */
        dragon_ball,
        /**
         * 房车劵
         */
        motorhomes_stock,
    }
}
