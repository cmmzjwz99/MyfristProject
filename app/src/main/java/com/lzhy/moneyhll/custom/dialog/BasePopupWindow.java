package com.lzhy.moneyhll.custom.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.lzhy.moneyhll.R;

import static com.lzhy.moneyhll.constant.Constant.BAIDU_ID;
import static com.lzhy.moneyhll.constant.Constant.GAODE_ID;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;

/**
 * @date 2016年11月5日 上午10:15:17
 */
public class BasePopupWindow {

    private boolean isShowBack = false;
    protected Activity mActivity;
    protected PopupWindow popupWindow;

    protected View view;

    @SuppressWarnings("deprecation")
    public BasePopupWindow(Context _context, int resId) {
        this.mActivity = (Activity) _context;

        view = mActivity.getLayoutInflater().inflate(resId, null, false);
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new OnDismissListener() {
            // 在dismiss中恢复透明度
            public void onDismiss() {
                if(!isShowBack) {
                    setBackgroundAlpha(1f);
                }
                closePager();
            }
        });
    }

    /**
     * 设置背景透明度
     *
     * @param f
     */
    protected void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = f;
        mActivity.getWindow().setAttributes(lp);
        mActivity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    public View getView() {
        return view;
    }

    // 隐藏菜单
    public void dismiss() {
        popupWindow.dismiss();
    }
    public void setDismissClick(OnDismissListener dismissClick) {
        popupWindow.setOnDismissListener(dismissClick);
    }

    /**
     * 对指定的组件添加点击事件
     *
     * @param id
     * @param listener
     *   setOnClickListener(R.id.baidu_map, mItemsOnClick);
    setOnClickListener(R.id.autonavi_map, mItemsOnClick);
     */
    public void setOnClickListener(int id, OnClickListener listener) {
        View v = view.findViewById(id);
        if (v == null)
            return;
        if (id == R.id.baidu_map) {
            setCustomStatisticsKV(mActivity,BAIDU_ID, "百度地图");
        }
        if (id == R.id.autonavi_map) {
            setCustomStatisticsKV(mActivity,GAODE_ID, "高德地图");
        }
        v.setOnClickListener(listener);
    }

    /**
     *
     */
    public void showAtBottom(View parent) {
        if(!isShowBack) {
            setBackgroundAlpha(0.8f);
        }
        popupWindow.showAtLocation(parent, Gravity.CENTER_HORIZONTAL
                | Gravity.BOTTOM, 0, 0);

        update();
    }

    public void showAtTop(View parent) {
        setBackgroundAlpha(0.8f);

        // popupWindow.showAtLocation(parent, Gravity.CENTER_HORIZONTAL
        // | Gravity.TOP, 0, 0);

        popupWindow.showAsDropDown(parent, 0, -135);

        update();
    }

    /**
     * 根据组件定位
     *
     * @param anchor
     */
    public void showAsDropDown(View anchor) {
        setBackgroundAlpha(0.8f);
        popupWindow.showAsDropDown(anchor);
        update();
    }

    /**
     * 根据组件定位
     *
     * @param anchor
     */
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        setBackgroundAlpha(0.8f);
        popupWindow.showAsDropDown(anchor, xoff, yoff);
        update();
    }

    public void update() {

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 刷新状态
        popupWindow.update();
    }

    public void update(Context _context) {
        this.mActivity = (Activity) _context;
    }

    /**
     *
     */
    public void showAtCenter(View parent) {
        setBackgroundAlpha(0.8f);

        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        update();
    }

    /**
     * 对指定的组件添加文本内容
     *
     * @param id
     * @param txt
     */
    public void setText(int id, String txt) {
        View v = view.findViewById(id);
        if (v == null)
            return;
        if (v instanceof Button)
            ((Button) v).setText(txt);
        else if (v instanceof TextView)
            ((TextView) v).setText(txt);
        else if (v instanceof EditText)
            ((EditText) v).setText(txt);
    }

    /**
     * 对指定的组件添加文本内容
     *
     * @param id
     * @param resId
     */
    public void setText(int id, int resId) {
        View v = view.findViewById(id);
        if (v == null)
            return;
        if (v instanceof Button)
            ((Button) v).setText(resId);
        else if (v instanceof TextView)
            ((TextView) v).setText(resId);
        else if (v instanceof EditText)
            ((EditText) v).setText(resId);
    }

    private OnClickListener mOnClickListener;

    /**
     * 设置页面关闭的监听
     *
     * @param l
     */
    public void setOnClosePagerListener(OnClickListener l) {
        mOnClickListener = l;
    }

    /**
     * 设置页面关闭时相应的事件
     */
    private void closePager() {

        if (mOnClickListener != null && isValid) {
            mOnClickListener.onClick(view);
        } else {
            isValid = true;
        }
    }

    /**
     * 关闭事件是否有效
     */
    private boolean isValid = true;

    /**
     * 设置关闭的相关事件是否有效 ，默认是有效的,设置只有第一次有效，之后会变成默认值，<br>
     * 默认值是 true
     *
     * @param valid
     */
    public void setOnClosePagerListener(boolean valid) {
        isValid = valid;
    }

    public void setIsShowBack(boolean isShowBack) {
        this.isShowBack = isShowBack;
    }
}
