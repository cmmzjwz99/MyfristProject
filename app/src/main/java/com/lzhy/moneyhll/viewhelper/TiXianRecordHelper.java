package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.RecordItems;

/**
 * Created by cmm on 2016/11/16.
 */

public class TiXianRecordHelper {
    private Context mContext;
    private View mView;
    private TextView tvBankType;
    private TextView tvAmount;
    private TextView tvCreateTime;
    private TextView tvStatus;


    public TiXianRecordHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        tvBankType = (TextView) mView.findViewById(R.id.tv_bank_type);
        tvAmount = (TextView) mView.findViewById(R.id.tv_amount_number);

        tvCreateTime = (TextView) mView.findViewById(R.id.tv_create_time);
        tvStatus = (TextView) mView.findViewById(R.id.tv_status);


    }

    public void updateView(RecordItems info) {
        tvBankType.setText(info.bankName);
        tvAmount.setText(info.amount + "");
        tvCreateTime.setText(info.createTime);
        switch (info.status){
            case 1:
                tvStatus.setText("待审核");
                break;
            case 2:
                tvStatus.setText("待汇款");
                break;
            case 3:
                tvStatus.setText("已汇款");
                break;
            case 4:
                tvStatus.setText("完结");
                break;
            case 5:
                tvStatus.setText("异常");
                break;
            case 6:
                tvStatus.setText("驳回");
                break;
            default:
                break;
        }


    }
}
