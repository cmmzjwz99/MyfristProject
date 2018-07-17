package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.MotorHomeInfo;


/**
 * Created by cmm on 2016/11/10.
 */

public class MotorHomeAppointHelper {
    private Context mContext;
    private View mView;
    private SimpleDraweeView mDraweeView;
    private TextView tvCity;
    private TextView place;
    private TextView tvProvince;


    public MotorHomeAppointHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        tvCity = (TextView) mView.findViewById(R.id.tv_city);
        place = (TextView) mView.findViewById(R.id.text);

    }

    public void updateView(MotorHomeInfo info) {
        if (info.imageUrl!=null) {
            mDraweeView.setImageURI(info.imageUrl);
        }else{
            mDraweeView.setImageURI("");
        }
        place.setText(info.name);
        tvCity.setText(info.city);
    }
}
