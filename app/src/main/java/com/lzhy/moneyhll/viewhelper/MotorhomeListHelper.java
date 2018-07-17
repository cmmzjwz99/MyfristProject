package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.PlayWhereListModel;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by Administrator on 2016/11/1 0001.
 */

public class MotorhomeListHelper {
    private Context mContext;
    private View mView;

    private SimpleDraweeView mDraweeView;
    private TextView name;
    private TextView address;
//    private TextView unit_type;
//    private TextView time_num;
//    private TextView price;

    public MotorhomeListHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        name = (TextView) mView.findViewById(R.id.name);
        address = (TextView) mView.findViewById(R.id.address);
//        unit_type = (TextView) mView.findViewById(unit_type);
//        time_num = (TextView) mView.findViewById(time_num);
//        price = (TextView) mView.findViewById(price);
    }

    public void updateView(PlayWhereListModel b) {
        if (b.picture2 != null) {
            mDraweeView.setImageURI(Uri.parse(b.picture2));
        } else {
            mDraweeView.setImageURI("");
        }
        name.setText(b.projectname);
        if (!StringUtils.isEmpty(b.address)) {
            address.setText("地址：" + b.address);
        } else {
            address.setText("地址：空" );
        }
//        address.setText("地址："+b.address);
//        unit_type.setText(b.salesunit);
//        time_num.setText("约" + b.enjoytime + "小时");
//        price.setText(b.adultprice + "");
    }
}
