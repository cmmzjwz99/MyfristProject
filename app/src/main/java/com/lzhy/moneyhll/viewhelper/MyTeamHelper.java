package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.Friend;
import com.lzhy.moneyhll.model.BaseModel;

/**
 * Created by cmm on 2016/11/15.
 */

public class MyTeamHelper extends BaseModel {
    private Context mContext;
    private View mView;
    private SimpleDraweeView mDraweeView;
    private TextView tvPhone;
    private TextView tvYJT;
    private TextView tvYJF;
    private TextView tvYJN;


    public MyTeamHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView= (SimpleDraweeView) mView.findViewById(R.id.image);
        tvPhone = (TextView) mView.findViewById(R.id.tv_phone);
        tvYJT = (TextView) mView.findViewById(R.id.tv_yin_jian_time);
        tvYJF = (TextView) mView.findViewById(R.id.tv_friend);
        tvYJN = (TextView) mView.findViewById(R.id.tv_yin_jian_name);
    }

    public void updateView(Friend info) {
        mDraweeView.setImageURI(info.recImg);
        tvPhone.setText(info.recPhone);
        tvYJT.setText(info.recTime);
        tvYJF.setText(info.recNum +" 位小伙伴");
        tvYJN.setText(info.recName);
    }


}
