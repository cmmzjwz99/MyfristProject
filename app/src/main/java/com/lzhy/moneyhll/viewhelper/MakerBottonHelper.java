package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.home.makerproject.MakerListActivity;

/**
 * Created by Administrator on 2016/11/25 0025.
 */

public class MakerBottonHelper {
    private Context mContext;
    private View mView;

    private RelativeLayout button;

    public MakerBottonHelper(Context context, View view) {
        mContext = context;
        mView = view;
        button = (RelativeLayout) mView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MakerListActivity.class);
                mContext.startActivity(intent);
            }
        });
    }
}
