package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.ProjectOrderModel;

/**
 * Created by cmm on 2016/11/4.
 */

public class OrderHelper {
    private Context mContext;
    private View mView;
    private SimpleDraweeView mDraweeView;
    private TextView projectName;
    private TextView state;
    private TextView peopleNum;
    private TextView playTime;
    private TextView price;
    private TextView number;
    private TextView allMoney;

    public OrderHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        projectName = (TextView) mView.findViewById(R.id.tv_project_name);
        state = (TextView) mView.findViewById(R.id.tv_state);
        peopleNum = (TextView) mView.findViewById(R.id.tv_men_number);
        playTime = (TextView) mView.findViewById(R.id.tv_play_time);
        price = (TextView) mView.findViewById(R.id.tv_money);
        number = (TextView) mView.findViewById(R.id.tv_number);
        allMoney = (TextView) mView.findViewById(R.id.tv_all_money);

    }

    public void updateView(ProjectOrderModel info) {
        if (info.imageUrl != null) {
            mDraweeView.setImageURI(Uri.parse(info.imageUrl));
        } else {
            mDraweeView.setImageURI("");
        }
        projectName.setText(info.title);
        if (info.status == 2) {
            state.setText("未使用");
        } else if (info.status == 3) {
            state.setText("已使用");
        }
        playTime.setText(info.enjoytime + "");
        price.setText(info.adultPrice + "元");
        number.setText(info.quantity + "");
        allMoney.setText(info.payAmount + "元");
    }
}
