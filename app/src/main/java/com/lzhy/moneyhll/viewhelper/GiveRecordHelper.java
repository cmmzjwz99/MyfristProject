package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.model.GiveRecordModel;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class GiveRecordHelper {
    private Context mContext;
    private View mView;

    private SimpleDraweeView iv_head_portrait;
    private TextView tv_number;
    private TextView tv_name;
    private TextView tv_time;


    public GiveRecordHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        iv_head_portrait = (SimpleDraweeView) mView.findViewById(R.id.iv_head_portrait);
        tv_number = (TextView) mView.findViewById(R.id.tv_number);
        tv_name = (TextView) mView.findViewById(R.id.tv_name);
        tv_time = (TextView) mView.findViewById(R.id.tv_time);

    }

    public void updateView(GiveRecordModel info, int type) {
        tv_time.setText(CommonUtil.getDateTimeFromMillisecond(info.createTime));
        if (type == 1) {
            if (!StringUtils.isEmpty(info.avatar)) {
                iv_head_portrait.setImageURI(info.avatar);
            } else {
                iv_head_portrait.setImageURI("");
            }
            if (info.rownum == 0) {
                //0被赠送者
                if (!StringUtils.isEmpty(info.nickName)) {
                    tv_name.setText(info.nickName + "赠送");
                } else if (!StringUtils.isEmpty(info.account)) {
                    tv_name.setText(info.account + "赠送");
                }
                tv_number.setTextColor(0XFFFDBD44);
                tv_number.setText("+" + info.changeValue + "龙珠");
            } else {
                //赠送者
                if (!StringUtils.isEmpty(info.nickName)) {
                    tv_name.setText("赠送给" + info.nickName);
                } else if (!StringUtils.isEmpty(info.account)) {
                    tv_name.setText("赠送给" + info.account);
                }
                tv_number.setTextColor(0XFFFF3E3E);
                tv_number.setText("-" + info.changeValue + "龙珠");
            }
        }

        if (type == 2) {
            if (info.curveMemberId == UserInfoModel.getInstance().getId()) {
                //赠送人
                if (!StringUtils.isEmpty(info.begiftedavatar)) {
                    iv_head_portrait.setImageURI(info.begiftedavatar);
                } else {
                    iv_head_portrait.setImageURI("");
                }

                if (!StringUtils.isEmpty(info.begiftedname)) {
                    tv_name.setText("赠送给" + info.begiftedname);
                } else if (!StringUtils.isEmpty(info.begiftedTel)) {
                    tv_name.setText("赠送给" + info.begiftedTel);
                }
                tv_number.setTextColor(0XFFFF3E3E);
                tv_number.setText("-" + info.num + "房车劵");
            } else {
                //被赠送人
                if (!StringUtils.isEmpty(info.giftavatar)) {
                    iv_head_portrait.setImageURI(info.giftavatar);
                } else {
                    iv_head_portrait.setImageURI("");
                }

                if (!StringUtils.isEmpty(info.giftname)) {
                    tv_name.setText( info.giftname+"赠送");
                } else if (!StringUtils.isEmpty(info.giftTel)) {
                    tv_name.setText(info.giftTel+"赠送");
                }
                tv_number.setTextColor(0XFFFDBD44);
                tv_number.setText("+" + info.num + "房车劵");
            }
        }
    }
}
