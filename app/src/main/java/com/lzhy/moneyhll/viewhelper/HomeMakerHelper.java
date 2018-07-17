package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.home.makerproject.MakerDetailsActivity;
import com.lzhy.moneyhll.model.MakerModel;

/**
 * Created by Administrator on 2016/11/25 0025.
 */

public class HomeMakerHelper {
    private Context mContext;
    private View mView;

    private SimpleDraweeView mDraweeView;
    private TextView project_name;
    private TextView project_price;

    public HomeMakerHelper(Context context, View view) {
        mContext = context;
        mView = view;
        initView();
    }

    private void initView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        project_name = (TextView) mView.findViewById(R.id.project_name);
        project_price = (TextView) mView.findViewById(R.id.project_price);
    }

    public void updateView(final MakerModel info) {
        if (info.imageurl != null) {
            mDraweeView.setImageURI(info.imageurl);
        }
        project_name.setText(info.projectname);
        project_price.setText(info.adultprice+"");
        View parent = (View) mDraweeView.getParent();
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MakerDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("id", info.id);
                extras.putInt("type", info.projecttype);
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });

    }
}
