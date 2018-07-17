package com.lzhy.moneyhll.me.mine.wallet;

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

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;

/**
 * Created by cmm on 2016/11/3.
 * 明细查看
 */

public class EarningActivity extends MySwipeBackActivity implements ViewPager.OnPageChangeListener {
    private RadioButton dragon_ball;
    private RadioButton dragon_coin;

    private ViewPager mViewPager;
    private MyAdapter mAdapter;
    private List<RadioButton> radioLists;
    private List<MostType> typeList;
    private int type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);

        addActivityCST(this);
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

    private void initView() {
        type = getIntent().getIntExtra("type", 1) ;

        radioLists = new ArrayList<>();
        dragon_ball = (RadioButton) findViewById(R.id.dragon_ball_detail);
        dragon_coin = (RadioButton) findViewById(R.id.dragon_coin_detail);

        mViewPager = (ViewPager) findViewById(R.id.myviewpager);

        radioLists.add(dragon_ball);
        radioLists.add(dragon_coin);
        radioLists.get(type).setChecked(true);
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
        typeList.add(MostType.dragon_ball_detail);
        typeList.add(MostType.dragon_coin_detail);

        mAdapter = new MyAdapter(getSupportFragmentManager(), typeList,
                mViewPager);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(type);
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
        public android.support.v4.app.Fragment getItem(int position) {
            Fragment _fragment = null;
            if (typeList.get(position) == MostType.dragon_ball_detail) {
                //龙珠收支明细
                _fragment = new EarningFm();
                Bundle bundle = new Bundle();
                bundle.putString("payType", "pears");
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.dragon_coin_detail) {
                //龙币收支明细
                _fragment = new EarningFm();
                Bundle bundle = new Bundle();
                bundle.putString("payType", "coins");
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
         * 龙珠收支明细
         */
        dragon_ball_detail,
        /**
         * 龙币收支明细
         */
        dragon_coin_detail,
    }
}
