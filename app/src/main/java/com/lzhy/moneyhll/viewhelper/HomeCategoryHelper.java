package com.lzhy.moneyhll.viewhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.home.bannerbelow.ActivityComProfile;
import com.lzhy.moneyhll.home.bannerbelow.ActivityHQualityRoute;
import com.lzhy.moneyhll.home.bannerbelow.ActivityTicketBook;
import com.lzhy.moneyhll.home.dragonball.ActivityDragonBall;
import com.lzhy.moneyhll.home.motorhomeshow.ActivityCarRoomDisplay;
import com.lzhy.moneyhll.model.CategoryModel;
import com.lzhy.moneyhll.utils.UtilToast;

import java.util.List;

import static com.lzhy.moneyhll.constant.Constant.netStateIsOk;
import static com.lzhy.moneyhll.utils.UtilCheckMix.networkIsAvailable;

/**
 * Created by Administrator on 2016/10/26 0026.
 */

public class HomeCategoryHelper implements ItemViewListener {
    // private final MyReceiver myReceiver;
    private Context mContext;
    private View mView;
    private List<CategoryModel> mList;

    private LongAdapter mAdapter;
    private NoScorllGridView mGridView;

    public HomeCategoryHelper(Context context, View view) {
        mContext = context;
        mView = view;
        mList = new java.util.ArrayList<>();
        findView();
    }

    private void findView() {
        mGridView = (NoScorllGridView) mView.findViewById(R.id.home_gridview);
        mGridView.setNumColumns(4);
        ViewGroup.LayoutParams mLayoutParams = mGridView
                .getLayoutParams();
      /*  mLayoutParams.height = CommonUtil.getScreenWidthPixels((Activity) mContext) * 41 / 75;
        mGridView.setLayoutParams(mLayoutParams);*/
        mAdapter = new com.lzhy.moneyhll.custom.LongAdapter(mContext, mList, this);
        mGridView.setAdapter(mAdapter);
    }

    public void updateView() {
        mList.clear();
        mList.add(new CategoryModel(R.mipmap.fczs, mContext.getString(R.string.fangche_desplay)));
        mList.add(new CategoryModel(R.mipmap.jpxl, mContext.getString(R.string.hq_route)));
        mList.add(new CategoryModel(R.mipmap.lzsc, mContext.getString(R.string.dragon_market)));
        mList.add(new CategoryModel(R.mipmap.lzy,  mContext.getString(R.string.personal_center)));
        mList.add(new CategoryModel(R.mipmap.zjbx, mContext.getString(R.string.drive_insurance)));
        mList.add(new CategoryModel(R.mipmap.tsrb, mContext.getString(R.string.tour_abroad)));
        mList.add(new CategoryModel(R.mipmap.lyqz, mContext.getString(R.string.tour_visa)));
        mList.add(new CategoryModel(R.mipmap.jpyd, mContext.getString(R.string.ticket_book)));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public android.view.View getView(int id, android.view.View itemView, android.view.ViewGroup vg, final Object data) {
        com.lzhy.moneyhll.viewhelper.HomeCategoryGridHelper helper = null;

        if (itemView == null) {
            itemView = android.view.LayoutInflater.from(mContext).inflate(
                    com.lzhy.moneyhll.R.layout.item_home_grid, null, false);
            helper = new com.lzhy.moneyhll.viewhelper.HomeCategoryGridHelper(mContext, itemView);
            itemView.setTag(helper);
        } else {
            helper = (com.lzhy.moneyhll.viewhelper.HomeCategoryGridHelper) itemView.getTag();
        }
        helper.updateView((CategoryModel) data);
        final android.view.View v = itemView;
        itemView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                final int ANITIME = 100;
                // 透明度变化
                android.view.animation.AlphaAnimation alphaAni = new android.view.animation.AlphaAnimation(0.05f, 1.0f);
                alphaAni.setDuration(ANITIME);                // 设置动画效果时间
                v.startAnimation(alphaAni);        // 添加光效动画到VIew

                alphaAni.setAnimationListener(new android.view.animation.Animation.AnimationListener() {

                    String onsOfEight = ((CategoryModel) data).name;

                    Activity activity = (Activity) mContext;

                    @Override
                    public void onAnimationStart(android.view.animation.Animation animation) {
                        //调转前的准备工作
                        //Toast.makeText(activity, "正在跳转到"+onsOfEight, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationEnd(android.view.animation.Animation animation) {
                        Intent intent = null;
                       relyOnNet(onsOfEight,activity,intent);
                    }
                    @Override
                    public void onAnimationRepeat(android.view.animation.Animation animation) {

                    }
                });
            }
        });
        return itemView;
    }
    private void relyOnNet(String onsOfEight,Activity activity,Intent intent) {
        if (networkIsAvailable(activity)) {
            if (onsOfEight.equals(mContext.getString(R.string.fangche_desplay))) {//"房车展示"
                intent = new Intent(activity, ActivityCarRoomDisplay.class);
            } else if (onsOfEight.equals(mContext.getString(R.string.hq_route))) {//"精品线路"
                intent = new Intent(activity, ActivityHQualityRoute.class);
                intent.putExtra("hq_tag",mContext.getString(R.string.hq_route));
            } else if (onsOfEight.equals(mContext.getString(R.string.dragon_market))) {//"龙珠商城"
                intent = new Intent(activity, ActivityDragonBall.class);
            } else if (onsOfEight.equals(mContext.getString(R.string.personal_center))) {//"个人中心"
                intent = new Intent(activity, ActivityComProfile.class);
            } else if (onsOfEight.equals(mContext.getString(R.string.tour_abroad))) {//"特色日本"
                intent = new Intent(activity, ActivityHQualityRoute.class);
                intent.putExtra("hq_tag",mContext.getString(R.string.tour_abroad));
            }  else if (onsOfEight.equals(mContext.getString(R.string.drive_insurance))) {//"自驾保险"
                intent = new Intent(activity, ActivityTicketBook.class);
                intent.putExtra("wv_tag",mContext.getString(R.string.drive_insurance));
            }else if (onsOfEight.equals(mContext.getString(R.string.tour_visa))) {//"旅游签证"
                intent = new Intent(activity, ActivityTicketBook.class);
                intent.putExtra("wv_tag",mContext.getString(R.string.tour_visa));
            } else if (onsOfEight.equals(mContext.getString(R.string.ticket_book))) {//"机票预订"
                intent = new Intent(activity, ActivityTicketBook.class);
                intent.putExtra("wv_tag",mContext.getString(R.string.ticket_book));
            }
            activity.startActivity(intent);
        }else{
            UtilToast.getInstance().showDragonSuccess("请您先连接网络！");
            netStateIsOk = false;
            return;
        }
    }


}
