package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.HistoryInfo;


/**
 * Created by cmm on 2016/11/7.
 */

public class HistoryHelper {
    private Context mContext;
    private View mView;
    private TextView tvCoding;
    private RelativeLayout rlTime;
    private TextView tvRoll;
    private TextView tvStartTime;
    private TextView tvEndTime;

    public HistoryHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        tvCoding = (TextView) mView.findViewById(R.id.tv_coding);
        tvRoll = (TextView) mView.findViewById(R.id.tv_roll);
        rlTime = (RelativeLayout) mView.findViewById(R.id.rl_time);
        tvStartTime = (TextView) mView.findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) mView.findViewById(R.id.tv_end_time);

    }

    public void updateView(HistoryInfo info) {
        tvCoding.setText(info.ticketId);
        tvRoll.setText(info.name);
        tvStartTime.setText(info.startTime + "至"+ info.endTime);
//        tvEndTime.setText(" 至 "+ info.endTime);

    }
}
