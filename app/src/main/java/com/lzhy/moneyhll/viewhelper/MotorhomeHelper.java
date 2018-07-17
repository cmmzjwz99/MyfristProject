package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.MotorhomeRentModel;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class MotorhomeHelper {
    private Context mContext;
    private View mView;
    private SimpleDraweeView mDraweeView;
    private TextView name;
    private TextView driving_type;
    private TextView bed_number;
    private TextView bear_number;
    private TextView address;
    private TextView price;

    public MotorhomeHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        name = (TextView) mView.findViewById(R.id.name);
        driving_type = (TextView) mView.findViewById(R.id.driving_type);
        bed_number = (TextView) mView.findViewById(R.id.bed_number);
        bear_number = (TextView) mView.findViewById(R.id.bear_number);
        address = (TextView) mView.findViewById(R.id.address);
        price = (TextView) mView.findViewById(R.id.price);
    }

    public void updateView(MotorhomeRentModel b) {
        if (b.imageUrl != null) {
            mDraweeView.setImageURI(Uri.parse(b.imageUrl));
        }else{
            mDraweeView.setImageURI("");
        }
        if (b.name != null) {
            name.setText(b.name);
        }
        if (b.permit != null) {
            driving_type.setText("准驾类型：" + b.permit);
        }
        if (b.beds != 0) {
            bed_number.setText("床数：" + b.beds);
        }
        if (b.nuclearCarrier != 0) {
            bear_number.setText("核载人数：" + b.nuclearCarrier);
        }
        if (b.address != null) {
            address.setText(b.address);
        }
        if (b.price != 0) {
            price.setText(b.price + "");
        }
//        name.setText(b.name);
//        driving_type.setText("准驾类型：" + b.permit);
//        bed_number.setText("床数：" + b.beds);
//        bear_number.setText("核载人数：" + b.nuclearCarrier);
//        address.setText(b.address);
//        price.setText(b.price + "");
    }
}
