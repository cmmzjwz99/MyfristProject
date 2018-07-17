package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.Items;

/**
 * Created by cmm on 2016/11/16.
 */

public class EarningItemHelper {
    private Context mContext;
    private View mView;
    private TextView tvOrderType;
    private TextView tvOrderNumber;
    private TextView tvOrderTime;
    private TextView tvOrderState;


    public EarningItemHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        tvOrderType = (TextView) mView.findViewById(R.id.tv_order_type);
        tvOrderNumber = (TextView) mView.findViewById(R.id.tv_order_number);
        tvOrderTime = (TextView) mView.findViewById(R.id.tv_order_time);
        tvOrderState = (TextView) mView.findViewById(R.id.tv_order_state);

    }

    public void updateView(Items info) {
        tvOrderType.setText(info.orderType);
        tvOrderNumber.setText(info.amount + "");
        tvOrderTime.setText(info.createTime);

        switch (info.status){
            case 1:
                tvOrderState.setText("未提现");
                break;
            case 2:
                tvOrderState.setText("已提现");
                break;
        }
    }
}
