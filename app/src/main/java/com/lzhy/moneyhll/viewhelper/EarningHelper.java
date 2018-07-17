package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.EarningModel;

/**
 * Created by Administrator on 2016/11/10 0010.
 */

public class EarningHelper {
    private Context mContext;
    private View mView;
    private TextView time;
    private TextView number;
    private TextView income_expenses;

    public EarningHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        time = (TextView) mView.findViewById(R.id.time);
        number = (TextView) mView.findViewById(R.id.number);
        income_expenses = (TextView) mView.findViewById(R.id.income_expenses);
    }

    public void updateView(final EarningModel b, String payType) {
        time.setText(b.createTime);
        if (b.amount >= 0) {
            income_expenses.setText("收入");
            number.setText(String.format("%.2f", b.amount));
            income_expenses.setTextColor(0XFF666666);
            number.setTextColor(0XFF666666);
        } else {
            number.setText(String.format("%.2f", b.amount));
            if ("pears".equals(payType)) {
                income_expenses.setText("支出");
            } else {
                income_expenses.setText("提现成功");
            }
            income_expenses.setTextColor(0XFFff3e3e);
            number.setTextColor(0XFFff3e3e);
        }
    }
}
