package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.gowhere.CampingDetailsActivity;
import com.lzhy.moneyhll.home.makerproject.MakerListActivity;
import com.lzhy.moneyhll.model.HomeListModel;
import com.lzhy.moneyhll.model.PlayWhereModel;
import com.lzhy.moneyhll.playwhat.PlayWhatListActivity;
import com.lzhy.moneyhll.utils.CommonUtil;

import java.util.List;

import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2016/10/26 0026.
 */

public class HomeCampHelper {
    private Context mContext;
    private View mView;

    private SimpleDraweeView mDraweeView;
    private ImageView image1, image2, image3, image4;
    private TextView text1, text2, text3, text4;
    private View parent1, parent2, parent3, parent4;
    private TextView name;
    private View parent;

    private LinearLayout mLayout;

    private ImageView camp_type_icon;
    private TextView camp_type;

    public HomeCampHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.simple_drawee);
        image1 = (ImageView) mView.findViewById(R.id.img1);
        image2 = (ImageView) mView.findViewById(R.id.img2);
        image3 = (ImageView) mView.findViewById(R.id.img3);
        image4 = (ImageView) mView.findViewById(R.id.img4);
        text1 = (TextView) mView.findViewById(R.id.text1);
        parent = (View) text1.getParent();
        text2 = (TextView) mView.findViewById(R.id.text2);
        text3 = (TextView) mView.findViewById(R.id.text3);
        text4 = (TextView) mView.findViewById(R.id.text4);
        name = (TextView) mView.findViewById(R.id.text_name);

        mLayout = (LinearLayout) mView.findViewById(R.id.approve);
        camp_type_icon = (ImageView) mView.findViewById(R.id.camp_type_icon);
        camp_type = (TextView) mView.findViewById(R.id.camp_type);

        parent1 = (View) image1.getParent();
        parent2 = (View) image2.getParent();
        parent3 = (View) image3.getParent();
        parent4 = (View) image4.getParent();

        CommonUtil.setViewHeight(mContext, mDraweeView);
    }

    public void updateView(final HomeListModel b) {
        camp_type_icon.setVisibility(VISIBLE);
        mLayout.setVisibility(View.VISIBLE);
        if (b.authentication == 1) {
            mLayout.setVisibility(View.VISIBLE);
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
        parent.setVisibility(View.VISIBLE);
        parent1.setVisibility(View.VISIBLE);
        parent2.setVisibility(View.VISIBLE);
        parent3.setVisibility(View.VISIBLE);
        parent4.setVisibility(View.VISIBLE);

        name.setText(b.projectname);
        mDraweeView.setImageURI(Uri.parse(b.imageurl));
        mDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CampingDetailsActivity.class);
                intent.putExtra("id", b.id);
                intent.putExtra("image", b.imageurl);
                intent.putExtra("name", b.projectname);
                intent.putExtra("text", b.address);
                mContext.startActivity(intent);
            }
        });

        final List<PlayWhereModel> nearProject = b.nearProject;
        int size = nearProject.size();
        if (size == 0) {
            parent.setVisibility(View.GONE);
        }
        if (size == 1) {
            text1.setText(nearProject.get(0).name);
            setIconImage(nearProject.get(0).icon, image1);
            parent2.setVisibility(View.INVISIBLE);
            parent3.setVisibility(View.INVISIBLE);
            parent4.setVisibility(View.INVISIBLE);
        }
        if (size == 2) {
            text1.setText(nearProject.get(0).name);
            setIconImage(nearProject.get(0).icon, image1);
            text2.setText(nearProject.get(1).name);
            setIconImage(nearProject.get(1).icon, image2);
            parent3.setVisibility(View.INVISIBLE);
            parent4.setVisibility(View.INVISIBLE);
        }
        if (size == 3) {
            text1.setText(nearProject.get(0).name);
            setIconImage(nearProject.get(0).icon, image1);
            text2.setText(nearProject.get(1).name);
            setIconImage(nearProject.get(1).icon, image2);
            text3.setText(nearProject.get(2).name);
            setIconImage(nearProject.get(2).icon, image3);
            parent4.setVisibility(View.INVISIBLE);
        }
        if (size >= 4) {
            text1.setText(nearProject.get(0).name);
            setIconImage(nearProject.get(0).icon, image1);
            text2.setText(nearProject.get(1).name);
            setIconImage(nearProject.get(1).icon, image2);
            text3.setText(nearProject.get(2).name);
            setIconImage(nearProject.get(2).icon, image3);
            text4.setText(nearProject.get(3).name);
            setIconImage(nearProject.get(3).icon, image4);
        }
        parent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if ("1022".equals(nearProject.get(0).id + "")) {
                    intent.setClass(mContext, MakerListActivity.class);
                } else {
                    intent.setClass(mContext, PlayWhatListActivity.class);
                    intent.putExtra("pid", nearProject.get(0).id + "");
                }
                intent.putExtra("lat", b.latitude + "");
                intent.putExtra("lng", b.longitude + "");
                intent.putExtra("scope", "50");
                mContext.startActivity(intent);
            }
        });
        parent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if ("1022".equals(nearProject.get(1).id + "")) {
                    intent.setClass(mContext, MakerListActivity.class);
                } else {
                    intent.setClass(mContext, PlayWhatListActivity.class);
                    intent.putExtra("pid", nearProject.get(1).id + "");
                }
                intent.putExtra("lat", b.latitude + "");
                intent.putExtra("lng", b.longitude + "");
                intent.putExtra("scope", "50");
                mContext.startActivity(intent);
            }
        });
        parent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if ("1022".equals(nearProject.get(2).id + "")) {
                    intent.setClass(mContext, MakerListActivity.class);
                } else {
                    intent.setClass(mContext, PlayWhatListActivity.class);
                    intent.putExtra("pid", nearProject.get(2).id + "");
                }
                intent.putExtra("lat", b.latitude + "");
                intent.putExtra("lng", b.longitude + "");
                intent.putExtra("scope", "50");
                mContext.startActivity(intent);
            }
        });
        parent4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if ("1022".equals(nearProject.get(3).id + "")) {
                    intent.setClass(mContext, MakerListActivity.class);
                } else {
                    intent.setClass(mContext, PlayWhatListActivity.class);
                    intent.putExtra("pid", nearProject.get(3).id + "");
                }
                intent.putExtra("lat", b.latitude + "");
                intent.putExtra("lng", b.longitude + "");
                intent.putExtra("scope", "50");
                mContext.startActivity(intent);
            }
        });
    }

    public void setIconImage(String icon, ImageView image) {
        switch (icon) {
            //高尔夫
            case "golf":
                image.setImageResource(R.mipmap.ic_golf);
                break;
            //骑马
            case "riding":
                image.setImageResource(R.mipmap.ic_riding);
                break;
            //攀岩
            case "climbing":
                image.setImageResource(R.mipmap.ic_climbing);
                break;
            //帆船
            case "sailing":
                image.setImageResource(R.mipmap.ic_sailing);
                break;
            //潜水
            case "dive":
                image.setImageResource(R.mipmap.ic_dive);
                break;
            //滑雪
            case "ski":
                image.setImageResource(R.mipmap.ic_ski);
                break;
            //滑翔机
            case "glider":
                image.setImageResource(R.mipmap.ic_glider);
                break;
            //游艇
            case "house-boat":
                image.setImageResource(R.mipmap.ic_house_boat);
                break;
            //温泉
            case "spa":
                image.setImageResource(R.mipmap.ic_spa);
                break;
            //景点
            case "scenic-spots":
                image.setImageResource(R.mipmap.ic_scenic_spots);
                break;
            //餐饮
            case "catering":
                image.setImageResource(R.mipmap.ic_catering);
                break;
            //住宿
            case "accommodation":
                image.setImageResource(R.mipmap.ic_accommodation);
                break;
            //蹦极
            case "bungee":
                image.setImageResource(R.mipmap.ic_bungee);
                break;
            //车
            case "limo":
                image.setImageResource(R.mipmap.ic_limo);
                break;
            //缆车
            case "cable-car":
                image.setImageResource(R.mipmap.ic_cable_car);
                break;
            //直升机
            case "helicopter":
                image.setImageResource(R.mipmap.ic_helicopter);
                break;
            //露营
            case "camping":
                image.setImageResource(R.mipmap.ic_camping);
                break;
            //热气球
            case "fire-balloon":
                image.setImageResource(R.mipmap.ic_fire_balloon);
                break;
            //摩托艇
            case "motor-boat":
                image.setImageResource(R.mipmap.ic_motor_boat);
                break;
            //创客
            case "ck":
                image.setImageResource(R.mipmap.ic_maker);
                break;
            default:
                image.setImageResource(R.mipmap.ic_launcher);
                break;

        }

    }
}
