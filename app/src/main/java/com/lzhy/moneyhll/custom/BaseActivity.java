package com.lzhy.moneyhll.custom;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.umeng.analytics.MobclickAgent;
import com.ta.utdid2.android.utils.StringUtils;

public class BaseActivity extends FragmentActivity {
    protected ViewGroup mEmptyContainer;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /************************************************************
     *@Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/30 13:24
     * 注释: 确保在所有的activity中都调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法，
     * 这两个调用将不会阻塞应用程序的主线程，也不会影响应用程序的性能。
    ************************************************************/
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    /**
     * 隐藏键盘
     */
    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示正在加载内容
     */
    protected void showLoading() {
        mEmptyContainer = (ViewGroup) findViewById(R.id.emptyLayout);
        RelativeLayout loadingItem = (RelativeLayout) getLayoutInflater().inflate(
                R.layout.item_loading, null);
        mEmptyContainer.addView(loadingItem);
        mEmptyContainer.setVisibility(View.VISIBLE);

    }

    protected void dataEmpty(String showMessage, int drawIdtop, int drawIdbot) {
        try {
            mEmptyContainer = (ViewGroup) findViewById(R.id.emptyLayout);
            View v = LayoutInflater.from(this).inflate(R.layout.empty_layout_img_top, null);
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

            tv.setText(showMessage);
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

    /**
     * 隐藏正在加载
     */
    protected void hideLoading() {

        if (mEmptyContainer != null) {
            mEmptyContainer.removeAllViews();
            mEmptyContainer.setVisibility(View.GONE);

        }
    }

    /**
     * 为子类提供一个检查权限的方法
     */
    public boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
              return false;
          }
        }
        return true;
    }

    /**
     * 为子类提供一个请求权限的方法
     */
    public void repuestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }
}
