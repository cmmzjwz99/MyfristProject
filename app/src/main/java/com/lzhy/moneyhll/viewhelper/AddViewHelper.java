package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.ServiceCostModel;

/**
 * Created by Administrator on 2016/11/5 0005.
 */

public class AddViewHelper {
    private Context mContext;
    private View mView;

    private TextView checkbox;
    private TextView unit;
    private TextView price;
    private TextView number;

    public AddViewHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        checkbox = (TextView) mView.findViewById(R.id.checkbox);
        unit = (TextView) mView.findViewById(R.id.unit);
        price = (TextView) mView.findViewById(R.id.price);
        number = (TextView) mView.findViewById(R.id.number);
    }

    public void updateView(ServiceCostModel b, int day) {
        checkbox.setText(b.name);
        if ("1".equals(b.unitName)) {
            unit.setText("元/次");
            number.setText("× " + 1);
        } else if ("2".equals(b.unitName)) {
            unit.setText("元/天");
            number.setText("× " + day);
        } else if ("3".equals(b.unitName)) {
            unit.setText("元/人");
            number.setText("× " + day);
        } else if ("4".equals(b.unitName)) {
            unit.setText("元/单元");
            int sum = day / 5;
            if (day % 5 != 0) {
                sum++;
            }
            number.setText("× " + sum);
        } else {
            unit.setText("");
        }
        price.setText("¥ " + b.price);

    }
}
