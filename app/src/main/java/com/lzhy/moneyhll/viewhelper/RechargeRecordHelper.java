package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.RechargeRecordItem;
import com.lzhy.moneyhll.utils.CommonUtil;

/**
 * 龙珠充值记录列表辅助类
 * Created by ycq on 2016/01/12.
 */

public class RechargeRecordHelper {
    private Context mContext;
    private View mView;
    private TextView tvAmount;//龙珠数额
    private TextView tvTime;//充值时间


    public RechargeRecordHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        tvAmount = (TextView) mView.findViewById(R.id.recharge_record_value_tv);
        tvTime = (TextView) mView.findViewById(R.id.recharge_record_time_tv);
    }

    /**
     * 更新界面内容
     * @param info
     */
    public void updateView(RechargeRecordItem info) {
        tvAmount.setText( "+"+info.changeValue );
        tvTime.setText(CommonUtil.getDateTimeFromMillisecond(info.createTime));
    }
}
