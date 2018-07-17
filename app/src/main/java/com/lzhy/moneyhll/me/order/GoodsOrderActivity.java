package com.lzhy.moneyhll.me.order;

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
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

public class GoodsOrderActivity extends MySwipeBackActivity implements ViewPager.OnPageChangeListener {
    private BaseTitlebar mTitlebar;
    private RadioButton wait_shipments;
    private RadioButton wait_sign;
    private RadioButton after_sign;
    private RadioButton after_end;
    private RadioButton after_sales;
    private RadioButton all_order;

    private ViewPager mViewPager;
    private MyAdapter mAdapter;
    private List<MostType> typeList;

    private List<RadioButton> radioLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_goods_order);
        addActivityCST(this);

        findViews();
        initTitleBar();
        initListener();
        initAdapter();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    /**
     * 初始化组件
     */
    private void findViews() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        radioLists = new ArrayList<>();
        all_order = (RadioButton) findViewById(R.id.all_order);
        wait_shipments = (RadioButton) findViewById(R.id.wait_shipments);
        wait_sign = (RadioButton) findViewById(R.id.wait_sign);
        after_sign = (RadioButton) findViewById(R.id.after_sign);
        after_end = (RadioButton) findViewById(R.id.after_end);
        after_sales = (RadioButton) findViewById(R.id.after_sales);
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        radioLists.add(all_order);
        radioLists.add(wait_shipments);
        radioLists.add(wait_sign);
        radioLists.add(after_sign);
        radioLists.add(after_end);
        radioLists.add(after_sales);
    }

    private void initTitleBar() {
        setTitleBarLeftBtn(mTitlebar,"全部订单");
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
        typeList.add(MostType.All_order);
        typeList.add(MostType.Wait_shipments);
        typeList.add(MostType.Wait_sign);
        typeList.add(MostType.After_sign);
        typeList.add(MostType.After_end);
        typeList.add(MostType.After_sales);

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
        switch (position) {
            case 0:
                mTitlebar.setTitle("全部订单");
                break;
            case 1:
                mTitlebar.setTitle("待发货订单");
                break;
            case 2:
                mTitlebar.setTitle("待签收订单");
                break;
            case 3:
                mTitlebar.setTitle("已签收订单");
                break;
            case 4:
                mTitlebar.setTitle("已完结订单");
                break;
            case 5:
                mTitlebar.setTitle("退换货订单");
                break;

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
            if (typeList.get(position) == MostType.All_order) {
                _fragment = new GoodsOrderListFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 0);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.Wait_shipments) {
                _fragment = new GoodsOrderListFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 4);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.Wait_sign) {
                _fragment = new GoodsOrderListFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 18);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.After_sign) {
                _fragment = new GoodsOrderListFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 20);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.After_end) {
                _fragment = new GoodsOrderListFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 8);
                _fragment.setArguments(bundle);
            } else if (typeList.get(position) == MostType.After_sales) {
                _fragment = new GoodsOrderListFm();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 16);
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
         * 待发货
         */
        All_order,
        /**
         * 待发货
         */
        Wait_shipments,
        /**
         * 待签收
         */
        Wait_sign,
        /**
         * 已签收
         */
        After_sign,
        /**
         * 已完结
         */
        After_end,
        /**
         * 退换货
         */
        After_sales,
    }
}

