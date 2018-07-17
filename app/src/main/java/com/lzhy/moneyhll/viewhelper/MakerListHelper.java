package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.MakerModel;
import com.lzhy.moneyhll.utils.CommonUtil;

/**
 * Created by Administrator on 2016/11/25 0025.
 */

public class MakerListHelper {
    private Context mContext;
    private View mView;

    private SimpleDraweeView mDraweeView;
    private TextView project_name;
    private TextView project_price;
    private TextView project_day;

    public MakerListHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        project_name = (TextView) mView.findViewById(R.id.project_name);
        project_price = (TextView) mView.findViewById(R.id.project_price);
        project_day= (TextView) mView.findViewById(R.id.project_day);
    }

    public void updateView(MakerModel info) {
        if(info.imageurl!=null){
            mDraweeView.setImageURI(info.imageurl);
        }
        project_name.setText(info.projectname);
        project_price.setText(info.adultprice+"");
        String startTime = CommonUtil.getDateTimeFromMillisecond(info.startTime);
        String endTime = CommonUtil.getDateTimeFromMillisecond(info.endTime);
        project_day.setText(startTime.split(" ")[0]
                +"至"+endTime.split(" ")[0]);
    }
}
