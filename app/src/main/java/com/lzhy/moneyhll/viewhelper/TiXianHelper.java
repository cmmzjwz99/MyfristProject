package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.Items;

/**
 * Created by cmm on 2016/11/16.
 */

public class TiXianHelper {
    private Context mContext;
    private View mView;
    private TextView tvBank;
    private TextView tvNumber;
    private TextView tvCreateTime;
    private TextView tvOrderState;


    public TiXianHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        tvBank = (TextView) mView.findViewById(R.id.tv_bank);
        tvNumber = (TextView) mView.findViewById(R.id.tv_number);
        tvCreateTime = (TextView) mView.findViewById(R.id.tv_create_time);
        tvOrderState = (TextView) mView.findViewById(R.id.tv_order_state);


    }

    public void updateView(Items info) {
        tvBank.setText(info.bankName);
        tvNumber.setText(info.amount + "");
        tvCreateTime.setText(info.createTime);

        switch (info.incomeType){
            case 1:
                tvOrderState.setText("待审核");
                break;
            case 2:
                tvOrderState.setText("待汇款");
                break;
            case 3:
                tvOrderState.setText("已汇款");
                break;
            case 4:
                tvOrderState.setText("完结");
                break;
            case 5:
                tvOrderState.setText("异常");
                break;
            case 6:
                tvOrderState.setText("驳回");
                break;
            default:
                break;
        }

    }
}
