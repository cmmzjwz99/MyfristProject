package com.lzhy.moneyhll.viewhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.home.BuyStockActivity;
import com.lzhy.moneyhll.utils.UtilToast;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.lzhy.moneyhll.utils.CommonUtil.getScreenWidthPixels;

/**
 * Created by Administrator on 2016/11/19 0019.
 */

public class BuyStockHelper {
    private Context mContext;
    private View mView;

    private LinearLayout mImage;

    private ImageView limo, camping;
    private EditText et_seek_motorhome, et_seek_camp;
    private Button seek_motorhome, seek_camp;

    private LinearLayout relative1, relative2;

    private Button tab2, tab3;
    private InputMethodManager imm;

    public static String serachText = "全部";
    public static String serachText1 = "";


    public BuyStockHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        imm=(InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);

        mImage = (LinearLayout) mView.findViewById(R.id.buy_stock);
        relative1 = (LinearLayout) mView.findViewById(R.id.relative1);
        relative2 = (LinearLayout) mView.findViewById(R.id.relative2);
        limo = (ImageView) mView.findViewById(R.id.limo);
        camping = (ImageView) mView.findViewById(R.id.camping);
        et_seek_motorhome = (EditText) mView.findViewById(R.id.et_seek_motorhome);
        et_seek_camp = (EditText) mView.findViewById(R.id.et_seek_camp);
        seek_motorhome = (Button) mView.findViewById(R.id.seek_motorhome);
        seek_camp = (Button) mView.findViewById(R.id.seek_camp);

        tab2 = (Button) ((Activity) mContext).findViewById(R.id.tab_rb_b2);
        tab3 = (Button) ((Activity) mContext).findViewById(R.id.tab_rb_c3);

        setViewHeight(mContext, mImage);
        setViewHeight(mContext, relative1);
        setViewHeight(mContext, relative2);

        limo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到房车
                // serachText = "全部";
                tab2.setClickable(true);
                tab2.performClick();
            }
        });
        camping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到露营地
                tab3.setClickable(true);
                tab3.performClick();
            }
        });
        seek_motorhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_seek_motorhome.getText().length() == 0) {
                    UtilToast.getInstance().showDragonInfo("请输入搜索内容");
                    return;
                } else {
                    //搜索房车
                    BuyStockHelper.serachText = et_seek_motorhome.getText().toString();
                    tab2.setClickable(true);
                    tab2.performClick();
                    imm.hideSoftInputFromWindow(seek_motorhome.getWindowToken(), 0);
                }
            }
        });
        seek_camp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_seek_camp.getText().length() == 0) {
                    UtilToast.getInstance().showDragonInfo("请输入搜索内容");
                    return;
                } else {
                    //搜索露营地
                    BuyStockHelper.serachText1 = et_seek_camp.getText().toString();
                    tab3.setClickable(true);
                    tab3.performClick();
                    imm.hideSoftInputFromWindow(seek_camp.getWindowToken(), 0);
                }
            }
        });
    }

    public void updateView() {

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, BuyStockActivity.class));
            }
        });
    }

    /**
     * 设置高度为1 / 3
     */

    public void setViewHeight(Context context, View view) {
        ViewGroup.LayoutParams mLayoutParams = view
                .getLayoutParams();
        mLayoutParams.height = getScreenWidthPixels((Activity) context) * 1 / 3;
        view.setLayoutParams(mLayoutParams);
    }
}
