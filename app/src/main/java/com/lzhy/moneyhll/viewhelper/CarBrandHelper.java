package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.CarBrandModel;

/**
 * Created by Administrator on 2016/11/2 0002.
 */

public class CarBrandHelper {
    private Context mContext;
    private View mView;

    private SimpleDraweeView mDraweeView;
    private TextView name;
    private TextView number;

    public CarBrandHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        name = (TextView) mView.findViewById(R.id.name);
        number = (TextView) mView.findViewById(R.id.number);
    }

    public void updateView(CarBrandModel b) {
        if (b.logoUrl != null) {
            mDraweeView.setImageURI(Uri.parse(b.logoUrl));
        }
        name.setText(b.cnName);
        number.setText(b.count + "");
    }
}
