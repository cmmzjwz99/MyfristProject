package com.lzhy.moneyhll.me.maker;

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
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by lzy on 2016/12/12.
 *创客方商品订单
 */
public final class GoodsOrderActivity extends MySwipeBackActivity implements ViewPager.OnPageChangeListener{
    private RadioButton rb_wait_pay;
    private RadioButton rb_wait_shipping;
    private RadioButton rb_wait_delivery;
    private RadioButton rb_has_finished;
    private RadioButton rb_has_end;
    private RadioButton rb_has_failure;

    private ViewPager mViewPager;
    private MyAdapter mAdapter;
    private List<RadioButton> radioLists;
    private List<MostType> typeList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_order);
        addActivityCST(this);
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
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("创客订单——商品");
    }

    private void initView() {
        radioLists = new ArrayList<>();
        rb_wait_pay = (RadioButton) findViewById(R.id.rb_wait_pay);
        rb_wait_shipping = (RadioButton) findViewById(R.id.rb_wait_shipping);
        rb_wait_delivery = (RadioButton) findViewById(R.id.rb_wait_delivery);
        rb_has_finished = (RadioButton) findViewById(R.id.rb_has_finished);
        rb_has_end = (RadioButton) findViewById(R.id.rb_has_end);
        rb_has_failure = (RadioButton) findViewById(R.id.rb_has_failure);

        mViewPager = (ViewPager) findViewById(R.id.myviewpager);

        radioLists.add(rb_wait_pay);
        radioLists.add(rb_wait_shipping);
        radioLists.add(rb_wait_delivery);
        radioLists.add(rb_has_finished);
        radioLists.add(rb_has_end);
        radioLists.add(rb_has_failure);

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
        typeList.add(MostType.wait_pay);
        typeList.add(MostType.wait_shipping);
        typeList.add(MostType.wait_delivery);
        typeList.add(MostType.has_finished);
        typeList.add(MostType.has_end);
        typeList.add(MostType.has_failure);

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
            if (typeList.get(position) == MostType.wait_pay) {
                //待支付
                _fragment = new GoodsOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 1);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.wait_shipping) {
                //待发货
                _fragment = new GoodsOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 2);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.wait_delivery) {
                //已发货
                _fragment = new GoodsOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 3);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.has_finished) {
                //已签收
                _fragment = new GoodsOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 4);
                _fragment.setArguments(bundle);
            }
            else if (typeList.get(position) == MostType.has_end) {
                //已完结
                _fragment = new GoodsOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 8);
                _fragment.setArguments(bundle);
            }else if (typeList.get(position) == MostType.has_failure) {
                //已失效
                _fragment = new GoodsOrderFragment();
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
        wait_pay,
        /**
         * 代发货
         */
        wait_shipping,
        /**
         * 已发货
         */
        wait_delivery,
        /**
         * 已签收
         */
        has_finished,
        /**
         * 已完结
         */
        has_end,
        /**
         * 已失效
         */
        has_failure,
    }
}
