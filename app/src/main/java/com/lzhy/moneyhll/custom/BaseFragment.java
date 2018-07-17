package com.lzhy.moneyhll.custom;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.umeng.analytics.MobclickAgent;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class BaseFragment extends Fragment {
    protected ViewGroup mEmptyContainer;
    public FragmentActivity mFragmentActivity;
    protected Handler handler = new Handler();
    /**
     * 当前显示的界面
     */
    protected View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentActivity = (FragmentActivity) super.getActivity();
    }

    /**
     * 隐藏键盘
     */
    public void dismissKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mFragmentActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mFragmentActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (mFragmentActivity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(mFragmentActivity
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示正在加载内容
     */
    protected void showLoading() {
        if (mEmptyContainer == null)
          mEmptyContainer = (ViewGroup) view.findViewById(R.id.emptyLayout);
        RelativeLayout loadingItem = (RelativeLayout)  LayoutInflater.from(mFragmentActivity)
                .inflate(R.layout.item_loading, null);
        mEmptyContainer.setVisibility(View.VISIBLE);
        mEmptyContainer.addView(loadingItem);

        mEmptyContainer.setBackgroundColor(0x00ffffff);

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
    /**
     * 隐藏正在加载
     */
    protected void hideLoading() {

        if (mEmptyContainer != null) {
            mEmptyContainer.removeAllViews();
            mEmptyContainer.setVisibility(View.GONE);

        }
    }

    protected void dataEmpty(String showMessage, int drawIdtop, int drawIdbot) {
        try {
            mEmptyContainer = (ViewGroup) view.findViewById(R.id.emptyLayout);
            View v = LayoutInflater.from(mFragmentActivity).inflate(R.layout.empty_layout_img_top, null);
            TextView tv = (TextView) v.findViewById(R.id.empty_msg);
            ImageView ivtop = (ImageView) v.findViewById(R.id.empty_imgtop);
            ImageView ivbot = (ImageView) v.findViewById(R.id.empty_imgbot);

            if (drawIdtop > 0) {
                ivtop.setImageResource(drawIdtop);
                ivtop.setVisibility(View.VISIBLE);
            } else {
                ivtop.setVisibility(View.INVISIBLE);
            }
            if (drawIdbot > 0) {
                if(drawIdbot==R.mipmap.no_earning_bot){
                    ivbot.setOnClickListener(new View.OnClickListener() {
                @Override
                        public void onClick(View view) {
                            CommonUtil.doCallPhone(mFragmentActivity, Constant.PHONE_KEFU);
                }
            });
        }
                ivbot.setImageResource(drawIdbot);
                ivbot.setVisibility(View.VISIBLE);
            } else {
                ivbot.setVisibility(View.INVISIBLE);
    }
            if (!StringUtils.isEmpty(showMessage)) {
                tv.setVisibility(View.VISIBLE);
            tv.setText(showMessage);
            } else {
                tv.setVisibility(View.GONE);
            }
            mEmptyContainer.addView(v);
            mEmptyContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mEmptyContainer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setViewPegeHight(int scrollViewY, int pageHight) {
    }

    /**
     * 为子类提供一个检查权限的方法
     */
    public boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mFragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 为子类提供一个检查请求的方法
     */
    public void repuestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(mFragmentActivity, permissions, code);
    }

}
