package com.lzhy.moneyhll.custom.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzhy.moneyhll.R;

/**
 * Created by Administrator on 2016/11/4 0004.
 * 图片选择弹窗
 */

public class ChoosePicPop extends BasePopupWindow {

    private Context mContext;
    private View.OnClickListener mItemsOnClick;
    private View mMenuView;

    public ChoosePicPop(Context context, View.OnClickListener itemsOnClick) {
        super(context,R.layout.popwindow_choose_pic);
        this.mContext = context;
        this.mItemsOnClick = itemsOnClick;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popwindow_choose_pic, null);
        initView();
    }

    private void initView() {

        setOnClickListener(R.id.photograph,mItemsOnClick);
        setOnClickListener(R.id.photo_gallery,mItemsOnClick);
        setOnClickListener(R.id.cancel,mItemsOnClick);

        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.anim.slide_button);
    }
}

