package com.lzhy.moneyhll.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.model.HomeListModel;
import com.lzhy.moneyhll.model.MakerModel;
import com.lzhy.moneyhll.viewhelper.BuyStockHelper;
import com.lzhy.moneyhll.viewhelper.HomeCampHelper;
import com.lzhy.moneyhll.viewhelper.HomeCategoryHelper;
import com.lzhy.moneyhll.viewhelper.HomeMakerHelper;
import com.lzhy.moneyhll.viewhelper.MakerBottonHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Artist on 2016/7/5.
 */
public final class HomeAdapter extends BaseAdapter {

    private Context mContext;
    protected List<HashMap<Integer, Object>> mList;

    public static final int VIEW_TYPE_CATEGOTY = 0;//类别
    public static final int VIEW_TYPE_INTERVAL = 1;//购买房车劵
    public static final int VIEW_TYPE_CAMP = 2;//露营地
    public static final int VIEW_TYPE_MAKER = 3;//创客
    public static final int VIEW_TYPE_MAKER_TOP = 4;//创客头部
    public static final int VIEW_TYPE_MAKER_BOTTON = 5;//创客底部
    public static final int VIEW_TYPE_CAMP_TOP = 6;//露营地头


    private static int ITEM_TYPE[] = {VIEW_TYPE_CATEGOTY, VIEW_TYPE_INTERVAL
            , VIEW_TYPE_CAMP,VIEW_TYPE_MAKER
            ,VIEW_TYPE_MAKER_TOP,VIEW_TYPE_MAKER_BOTTON
            ,VIEW_TYPE_CAMP_TOP};

    public HomeAdapter(Context context, List<HashMap<Integer, Object>> itemdata) {
        mContext = context;
        mList = itemdata;
    }

    @Override
    public int getItemViewType(int position) {
        int key_type = ITEM_TYPE[0];
        if (!mList.get(position).keySet().isEmpty()) {
            Iterator iter = mList.get(position).entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                key_type = (Integer) entry.getKey();
            }
        }
        return key_type;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE.length;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int id, View view, ViewGroup parent) {
        HomeCategoryHelper homeCategoryHelper = null;
        HomeCampHelper campHelper = null;
        BuyStockHelper buyStockHelper = null;
        HomeMakerHelper makerHelper=null;
        MakerBottonHelper makerBottonHelper=null;

        View itemView = null;
        if (view == null) {
            switch (getItemViewType(id)) {
                case VIEW_TYPE_CATEGOTY:
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_category, null, false);
                    homeCategoryHelper = new HomeCategoryHelper(mContext, itemView);
                    itemView.setTag(homeCategoryHelper);
                    break;
                case VIEW_TYPE_INTERVAL:
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_interval, null, false);
                    buyStockHelper = new BuyStockHelper(mContext, itemView);
                    itemView.setTag(buyStockHelper);
                    break;
                case VIEW_TYPE_CAMP:
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_camp, null, false);
                    campHelper = new HomeCampHelper(mContext, itemView);
                    itemView.setTag(campHelper);
                    break;
                case VIEW_TYPE_MAKER:
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_maker, null, false);
                    makerHelper = new HomeMakerHelper(mContext, itemView);
                    itemView.setTag(makerHelper);
                    break;
                case VIEW_TYPE_MAKER_TOP:
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_maker_top, null, false);
                    break;
                case VIEW_TYPE_MAKER_BOTTON:
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_maker_button, null, false);
                    makerBottonHelper=new MakerBottonHelper(mContext, itemView);
                    itemView.setTag(makerBottonHelper);
                    break;
                case VIEW_TYPE_CAMP_TOP:
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_camp_top, null, false);
                    break;
            }
        } else {
            itemView = view;
            switch (getItemViewType(id)) {
                case VIEW_TYPE_CATEGOTY:
                    homeCategoryHelper = (HomeCategoryHelper) itemView.getTag();
                    break;
                case VIEW_TYPE_INTERVAL:
                    buyStockHelper = (BuyStockHelper) itemView.getTag();
                    break;
                case VIEW_TYPE_CAMP:
                    campHelper = (HomeCampHelper) itemView.getTag();
                    break;
                case VIEW_TYPE_MAKER:
                    makerHelper = (HomeMakerHelper) itemView.getTag();
                    break;
                case VIEW_TYPE_MAKER_BOTTON:
                    makerBottonHelper = (MakerBottonHelper) itemView.getTag();
                    break;
            }
        }
        switch (getItemViewType(id)) {
            case VIEW_TYPE_CATEGOTY:
                homeCategoryHelper.updateView();
                break;
            case VIEW_TYPE_INTERVAL:
                buyStockHelper.updateView();
                break;
            case VIEW_TYPE_CAMP:
                campHelper.updateView((HomeListModel) mList.get(id).get(VIEW_TYPE_CAMP));
                break;
            case VIEW_TYPE_MAKER:
                makerHelper.updateView((MakerModel) mList.get(id).get(VIEW_TYPE_MAKER));
                break;
            case VIEW_TYPE_MAKER_BOTTON:
                break;
        }
        return itemView;
    }
}
