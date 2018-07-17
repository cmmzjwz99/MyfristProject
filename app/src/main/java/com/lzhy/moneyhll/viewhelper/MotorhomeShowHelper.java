package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.MotorhomeShowModel;
import com.lzhy.moneyhll.utils.CommonUtil;

/**
 * Created by Administrator on 2016/11/2 0002.
 */

public class MotorhomeShowHelper {
    private Context mContext;
    private View mView;

    private SimpleDraweeView mDraweeView;
    private TextView name;
    private TextView patterm;


    public MotorhomeShowHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        name = (TextView) mView.findViewById(R.id.name);
        patterm = (TextView) mView.findViewById(R.id.patterm);
        CommonUtil.setViewHeight(mContext, mDraweeView);
    }

    public void updateView(MotorhomeShowModel b) {
        if (b.thumbnailsUrl != null) {
            mDraweeView.setImageURI(Uri.parse(b.thumbnailsUrl));
        } else {
            mDraweeView.setImageURI("");
        }
        name.setText(b.name);
        patterm.setText("•"+b.patterm+"•");
    }
}
