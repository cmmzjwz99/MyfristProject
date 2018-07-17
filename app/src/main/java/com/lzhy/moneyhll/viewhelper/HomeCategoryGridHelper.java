package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.WangDianInfo;
import com.lzhy.moneyhll.model.CategoryModel;
import com.lzhy.moneyhll.model.PlayWhereModel;

/**
 * Created by Administrator on 2016/10/26 0026.
 */

public class HomeCategoryGridHelper {
    private Context mContext;
    private View mView;

    private ImageView mImageView;
    private TextView mTextView;

    public HomeCategoryGridHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mImageView = (ImageView) mView.findViewById(R.id.image);
        mTextView = (TextView) mView.findViewById(R.id.text);
    }

    public void updateView(CategoryModel b) {
        mImageView.setImageResource(b.img);
        mTextView.setText(b.name);
    }

    public void updateView(PlayWhereModel b) {
        mTextView.setText(b.name);
        switch (b.icon) {
            //高尔夫
            case "golf":
                mImageView.setImageResource(R.mipmap.ic_golf);
                break;
            //骑马
            case "riding":
                mImageView.setImageResource(R.mipmap.ic_riding);
                break;
            //攀岩
            case "climbing":
                mImageView.setImageResource(R.mipmap.ic_climbing);
                break;
            //帆船
            case "sailing":
                mImageView.setImageResource(R.mipmap.ic_sailing);
                break;
            //潜水
            case "dive":
                mImageView.setImageResource(R.mipmap.ic_dive);
                break;
            //滑雪
            case "ski":
                mImageView.setImageResource(R.mipmap.ic_ski);
                break;
            //滑翔机
            case "glider":
                mImageView.setImageResource(R.mipmap.ic_glider);
                break;
            //游艇
            case "house-boat":
                mImageView.setImageResource(R.mipmap.ic_house_boat);
                break;
            //温泉
            case "spa":
                mImageView.setImageResource(R.mipmap.ic_spa);
                break;
            //景点
            case "scenic-spots":
                mImageView.setImageResource(R.mipmap.ic_scenic_spots);
                break;
            //餐饮
            case "catering":
                mImageView.setImageResource(R.mipmap.ic_catering);
                break;
            //住宿
            case "accommodation":
                mImageView.setImageResource(R.mipmap.ic_accommodation);
                break;
            //蹦极
            case "bungee":
                mImageView.setImageResource(R.mipmap.ic_bungee);
                break;
            //车
            case "limo":
                mImageView.setImageResource(R.mipmap.ic_limo);
                break;
            //缆车
            case "cable-car":
                mImageView.setImageResource(R.mipmap.ic_cable_car);
                break;
            //直升机
            case "helicopter":
                mImageView.setImageResource(R.mipmap.ic_helicopter);
                break;
            //露营
            case "camping":
                mImageView.setImageResource(R.mipmap.ic_camping);
                break;
            //热气球
            case "fire-balloon":
                mImageView.setImageResource(R.mipmap.ic_fire_balloon);
                break;
            //摩托艇
            case "motor-boat":
                mImageView.setImageResource(R.mipmap.ic_motor_boat);
                break;
            //创客
            case "ck":
                mImageView.setImageResource(R.mipmap.ic_maker);
                break;
            default:
                mImageView.setImageResource(R.mipmap.ic_launcher);
                break;
        }

    }

    public void updateView(WangDianInfo b) {
        mImageView.setImageResource(b.getIcon());
        mTextView.setText(b.getTitle());
        mTextView.setText(b.getCity());
        mTextView.setTextColor(0xff999999);
    }
}
