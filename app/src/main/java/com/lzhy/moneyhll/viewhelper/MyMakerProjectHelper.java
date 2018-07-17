package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.model.MyMakerProjectModel;
import com.lzhy.moneyhll.utils.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/16 0016.
 */

public class MyMakerProjectHelper {
    private Context mContext;
    private View mView;

    private TextView create_time;
    private SimpleDraweeView image;
    private TextView project_name;
    private TextView price_symbol;
    private TextView project_price;
    private NoScorllGridView status_grid;
    private List<Map<String, Object>> mList;
    private TextView income;

    private Adapter mAdapter;

    public MyMakerProjectHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        create_time = (TextView) mView.findViewById(R.id.create_time);
        image = (SimpleDraweeView) mView.findViewById(R.id.image);
        project_name = (TextView) mView.findViewById(R.id.project_name);
        price_symbol = (TextView) mView.findViewById(R.id.price_symbol);
        project_price = (TextView) mView.findViewById(R.id.project_price);
        status_grid = (NoScorllGridView) mView.findViewById(R.id.status_grid);
        income = (TextView) mView.findViewById(R.id.income);
        mList = new ArrayList<>();
        mAdapter = new Adapter();
    }

    public void updateView(MyMakerProjectModel b) {
        mList.clear();
        create_time.setText("发布时间：" + CommonUtil.getDateTimeFromMillisecond(b.startTime).split(" ")[0]);
        if (b.projectPic != null) {
            image.setImageURI(b.projectPic);
        } else {
            image.setImageURI("");
        }
        // 6种订单状态
        if (b.makerTotal.abePaid > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", "bePaid");
            map.put("number", b.makerTotal.abePaid);
            mList.add(map);
        }
        if (b.makerTotal.bbeUsed > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", "beUsed");
            map.put("number", b.makerTotal.bbeUsed);
            mList.add(map);
        }
        if (b.makerTotal.cbeShipped > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", "beShipped");
            map.put("number", b.makerTotal.cbeShipped);
            mList.add(map);
        }
        if (b.makerTotal.dbeDeliver > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", "beDeliver");
            map.put("number", b.makerTotal.dbeDeliver);
            mList.add(map);
        }
        if (b.makerTotal.ebeFinished > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", "beFinished");
            map.put("number", b.makerTotal.ebeFinished);
            mList.add(map);
        }
        if (b.makerTotal.fbeFailed > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", "beFailed");
            map.put("number", b.makerTotal.fbeFailed);
            mList.add(map);
        }

        status_grid.setAdapter(mAdapter);
        project_name.setText(b.projectName);
        price_symbol.setText("结算价：¥");
        project_price.setText(b.clearingPrice + "");
        income.setText("收入：" + b.income + "元");
    }

    public class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHelper helper = null;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_my_project_gridview, null);
                helper = new ViewHelper();
                helper.image1 = (ImageView) view.findViewById(R.id.image1);
                helper.stute = (TextView) view.findViewById(R.id.stute);
                helper.number = (TextView) view.findViewById(R.id.number);
                view.setTag(helper);
            } else {
                helper = (ViewHelper) view.getTag();
            }
            Map<String, Object> map = mList.get(i);
            helper.number.setText(map.get("number").toString());
            switch (map.get("key").toString()) {
                case "bePaid":
                    helper.image1.setImageResource(R.mipmap.icon_bepaid);
                    helper.stute.setText("待支付");
                    break;
                case "beUsed":
                    helper.image1.setImageResource(R.mipmap.icon_beused);
                    helper.stute.setText("待确认");
                    break;
                case "beFinished":
                    helper.image1.setImageResource(R.mipmap.icon_befinished);
                    helper.stute.setText("已完结");
                    break;
                case "beFailed":
                    helper.image1.setImageResource(R.mipmap.icon_befailed);
                    helper.stute.setText("已失效");
                    break;
                case "beShipped":
                    helper.image1.setImageResource(R.mipmap.icon_beshipped);
                    helper.stute.setText("待发货");
                    break;
                case "beDeliver":
                    helper.image1.setImageResource(R.mipmap.icon_bedeliver);
                    helper.stute.setText("已发货");
                    break;
            }
            return view;
        }
    }

    class ViewHelper {
        ImageView image1;
        TextView stute;
        TextView number;
    }
}
