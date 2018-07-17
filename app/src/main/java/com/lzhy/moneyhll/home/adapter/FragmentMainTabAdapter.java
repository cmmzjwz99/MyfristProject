package com.lzhy.moneyhll.home.adapter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.loginOrRegister.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentMainTabAdapter implements
        RadioGroup.OnCheckedChangeListener {
    /**
     * 一个tab页面对应一个Fragment
     */
    private List<Fragment> fragments;
    /**
     * 用于切换tab
     */
    private RadioGroup rgs;
    /**
     * Fragment所属的Activity
     */
    private FragmentActivity fragmentActivity;
    /**
     * Activity中所要被替换的区域的id
     */
    private int fragmentContentId;
    /**
     * 当前Tab页面索引
     */
    private int currentTab;
//    private RadioButton tabHome, tabLimousine, tabGoWhere, tabPlayWhat, tabMine;
    private ArrayList<View> touchables;

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    /**
     * @param fragmentActivity  主界面
     * @param fragments         fragments管理集合
     * @param fragmentContentId 替换的view的id
     * @param rgs               RadioGroup 的信息
     */
    public FragmentMainTabAdapter(final FragmentActivity fragmentActivity,
                                  List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        if (!fragments.get(4).isAdded()) {
            FragmentTransaction ft = fragmentActivity
                    .getSupportFragmentManager().beginTransaction();
            ft.add(fragmentContentId, fragments.get(0));
            ft.commit();
        }
        touchables = rgs.getTouchables();
        rgs.setOnCheckedChangeListener(this);
    }


    /**
     * 是否需要登录，true表示需要登录
     *
     * @param radioGroup
     * @param checkedId
     * @return
     */
    private boolean isNeedLogin(RadioGroup radioGroup, int checkedId) {
        RadioButton radio = (RadioButton) this.fragmentActivity
                .findViewById(radioGroup.getCheckedRadioButtonId());

        String str = (String) radio.getTag();

        if (UserInfoModel.getInstance().isLogin()) {
            return false;
        } else if (radio.getText().equals("动态")) {
            return true;
        } else if (Constant.need_login.equals(str)) {
            return true;
        }

        return false;

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (isNeedLogin(radioGroup, checkedId)) {
            Intent intent = new Intent(fragmentActivity, LoginActivity.class);
            Log.i("onCheckedChanged", "onCheckedChanged: ");
            fragmentActivity.startActivity(intent);
            ((RadioButton) touchables.get(currentTab)).setChecked(true);

        } else {
            for (int i = 0; i < rgs.getChildCount(); i++) {
                if (rgs.getChildAt(i).getId() == checkedId) {

                    if (fragments.size() > 0 && fragments.size() > i) {
                        Fragment fragment = fragments.get(i);
                        FragmentTransaction ft = obtainFragmentTransaction(i);
                        getCurrentFragment().onPause(); // 暂停当前tab
                        // getCurrentFragment().onStop(); // 暂停当前tab
                        if (fragment.isAdded()) {
                            // fragment.onStart(); // 启动目标tab的onStart()
                            fragment.onResume(); // 启动目标tab的onResume()
                        } else {
                            ft.add(fragmentContentId, fragment);
                        }
                        showTab(i); // 显示目标tab
                        ft.commitAllowingStateLoss();
                        // 如果设置了切换tab额外功能功能接口
                        if (null != onRgsExtraCheckedChangedListener) {
                            onRgsExtraCheckedChangedListener
                                    .OnRgsExtraCheckedChanged(radioGroup,
                                            checkedId, i);
                        }
                    }
                }
            }
        }
    }


    /**
     * 切换tab
     *  tab点击事件
     * @param idx
     */
    public  void showTab(int idx) {
        Log.i("showTab", "showTab: ");

        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commitAllowingStateLoss();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        // 设置切换动画
        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.slide_right_in,
                    R.anim.slide_right_out);
        }
        return ft;
    }


    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(
            OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     * 切换tab额外功能功能接口
     */
    public static class OnRgsExtraCheckedChangedListener {

        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
                                             int checkedId, int index) {

        }

        public void OnRgsGoLoginChanged(RadioGroup radioGroup, int checkedId) {

        }
    }
}
