package com.lzhy.moneyhll.me.makerorder;

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

import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/*
* 创建时间：${date} ${time}
* @author: ouyangmuyuan
*  用途： 创客游玩订单页
*/
public class MakerPlayOrderActivity extends MySwipeBackActivity implements ViewPager.OnPageChangeListener {
    private RadioButton rb_wait_pay;
    private RadioButton rb_wait_used;
    private RadioButton rb_has_finished;
    private RadioButton rb_has_end;
    private RadioButton rb_has_failure;

    private ViewPager mViewPager;
    private MyAdapter mAdapter;
    private List<RadioButton> radioLists;
    private List<MostType> typeList;
    private int type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_maker_play_order);

        initView();
        initTitlebar();
        initListener();
        initAdapter();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"游玩订单");
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        radioLists = new ArrayList<>();
        rb_wait_pay = (RadioButton) findViewById(R.id.rb_wait_pay);
        rb_wait_used = (RadioButton) findViewById(R.id.rb_wait_used);
        rb_has_finished = (RadioButton) findViewById(R.id.rb_has_finished);
        rb_has_end = (RadioButton) findViewById(R.id.rb_has_end);
        rb_has_failure = (RadioButton) findViewById(R.id.rb_has_failure);

        mViewPager = (ViewPager) findViewById(R.id.myviewpager);

        radioLists.add(rb_wait_pay);
        radioLists.add(rb_wait_used);
        radioLists.add(rb_has_finished);
        radioLists.add(rb_has_end);
        radioLists.add(rb_has_failure);
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
        typeList.add(MostType.play_wait_pay);
        typeList.add(MostType.play_wait_used);
        typeList.add(MostType.play_has_finished);
        typeList.add(MostType.has_end);
        typeList.add(MostType.play_has_failure);

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
        public Fragment getItem(int position) {
            Fragment _fragment = null;
            if (typeList.get(position) == MostType.play_wait_pay) {
                //待支付
                _fragment = new MakerPlayOrderFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 1);
                _fragment.setArguments(bundle);
            }  else if (typeList.get(position) == MostType.play_wait_used) {
                //待使用
                _fragment = new MakerPlayOrderFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 2);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.play_has_finished) {
                //已使用
                _fragment = new MakerPlayOrderFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 3);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.has_end) {
                //已完结
                _fragment = new MakerPlayOrderFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 4);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.play_has_failure) {
                //已失效
                _fragment = new MakerPlayOrderFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 5);
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

    public  enum MostType {
        /**
         * 待支付
         */
        play_wait_pay,
        /**
         * 待使用
         */
        play_wait_used,
        /**
         * 已使用
         */
        play_has_finished,
        /**
         * 已完结
         */
        has_end,
        /**
         * 已失效
         */
        play_has_failure,
    }
}
