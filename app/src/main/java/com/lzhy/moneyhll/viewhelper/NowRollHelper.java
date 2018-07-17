package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.HistoryInfo;


/**
 * Created by cmm on 2016/11/7.
 */

public class NowRollHelper {
    private Context mContext;
    private View mView;
    private TextView tvCoding;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tv_name;

    public NowRollHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        tvCoding = (TextView) mView.findViewById(R.id.tv_coding);
        tv_name = (TextView) mView.findViewById(R.id.tv_name);
        tvStartTime = (TextView) mView.findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) mView.findViewById(R.id.tv_end_time);

    }

    public void updateView(HistoryInfo info) {
        tvCoding.setText(info.ticketId);
        tv_name.setText(info.name);
        tvStartTime.setText(info.startTime + "至"+info.endTime);
//        tvEndTime.setText(info.endTime);
    }
}
