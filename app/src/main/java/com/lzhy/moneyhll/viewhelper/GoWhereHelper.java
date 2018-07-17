package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.PlayWhereListModel;
import com.lzhy.moneyhll.utils.CommonUtil;

import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2016/10/22 0022.
 */

public class GoWhereHelper {
    private Context mContext;
    private View mView;
    private TextView name;
    private SimpleDraweeView mImageView;
    private TextView address;
    private LinearLayout mLayout;

    private ImageView camp_type_icon;
    private TextView camp_type;
    private TextView distance;

    public GoWhereHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        name = (TextView) mView.findViewById(R.id.go_where_site);
        mImageView = (SimpleDraweeView) mView.findViewById(R.id.go_where_etape_img);
        address = (TextView) mView.findViewById(R.id.go_where_distance);
        mLayout = (LinearLayout) mView.findViewById(R.id.approve);
        distance = (TextView) mView.findViewById(R.id.distance);
        CommonUtil.setViewHeight(mContext, mImageView);

        camp_type_icon = (ImageView) mView.findViewById(R.id.camp_type_icon);
        camp_type = (TextView) mView.findViewById(R.id.camp_type);
    }

    public void updateView(PlayWhereListModel b, int id) {
        mLayout.setVisibility(VISIBLE);
        name.setText(b.projectname);
        camp_type_icon.setVisibility(VISIBLE);
        if (b.authentication == 1) {
            mLayout.setVisibility(VISIBLE);
        } else {
            mLayout.setVisibility(View.GONE);
        }
        if (b.campType == 0) {
            camp_type_icon.setVisibility(View.GONE);
            camp_type.setText("");
        } else if (b.campType == 1) {
            camp_type_icon.setImageResource(R.mipmap.leisurely_holidays);
            camp_type.setText("营位型营地");
        } else if (b.campType == 2) {
            camp_type_icon.setImageResource(R.mipmap.post_house);
            camp_type.setText("帐篷型营地");
        } else {
            camp_type_icon.setVisibility(View.GONE);
            camp_type.setText("");
        }
        mImageView.setImageURI(Uri.parse(b.imageurl != null ? b.imageurl : ""));
        distance.setVisibility(View.GONE);
//        if (b.distance != null && b.distance > 0) {
//            //取两位小数
//            BigDecimal bg = new BigDecimal(b.distance);
//            double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//            distance.setText("约" + value + "公里");
//        }else{
//            distance.setText("");
//        }
    }
}
