package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.DragonBallModel;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class DragonBallHelper {
    private Context mContext;
    private View mView;

    private SimpleDraweeView mDraweeView;
    private TextView name;
    private TextView introduce;
    private TextView price;
    private TextView no_goods;

    public DragonBallHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        mDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image);
        name = (TextView) mView.findViewById(R.id.name);
        introduce = (TextView) mView.findViewById(R.id.introduce);
        price = (TextView) mView.findViewById(R.id.price);
        no_goods = (TextView) mView.findViewById(R.id.no_goods);
    }

    public void updateView(DragonBallModel b) {
        if (b.imageUrl != null) {
            mDraweeView.setImageURI(Uri.parse(b.imageUrl));
        } else {
            //mDraweeView.setImageURI(Uri.parse("http://img.hb.aicdn.com/9538d45fac365f65a4acf8f22bb361409dd2b82d16f0-j4LkMG_fw580"));
            mDraweeView.setImageURI("");
        }
        name.setText(b.title);
        introduce.setText(b.description);
        String all_price = "龙珠商城价: ";
        if (b.cashPrice > 0) {
            all_price = all_price + b.cashPrice + "人民币+";
        }
        if (b.coinPrice > 0) {
            all_price = all_price + b.coinPrice + "龙币+";
        }
        if (b.pearlPrice > 0) {
            all_price = all_price + b.pearlPrice + " 龙珠";
        }
        if (all_price.endsWith("+"))
            all_price=all_price.substring(0,all_price.length()-1);

        if (b.cashPrice <= 0 && b.coinPrice <= 0 && b.pearlPrice <= 0) {
            all_price="";
        }
        price.setText(all_price);
        if (b.stockout == 1) {
            no_goods.setVisibility(View.VISIBLE);
        } else {
            no_goods.setVisibility(View.GONE);
        }
    }
}
