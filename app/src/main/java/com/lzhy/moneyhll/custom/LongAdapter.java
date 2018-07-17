package com.lzhy.moneyhll.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 实现列表页list
 *
 */
public class LongAdapter extends BaseAdapter {

    private List<?> itemdata;

    private ItemViewListener listener;

    private Context mContext;

    /**
     * @param mContext
     * @param listener 实现view
     */
    public LongAdapter(Context mContext, List<?> itemdata,
                       ItemViewListener listener) {

        this.mContext = mContext;

        this.itemdata = itemdata;

        this.listener = listener;

    }

    @Override
    public int getCount() {

        if (showEmptyView())
            return 1;

        return itemdata.size();

    }

    @Override
    public Object getItem(int position) {
        return itemdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO_XBB Auto-generated method stub

        if (showEmptyView()) {
            return mEmptyView;
        }

        return listener.getView(arg0, arg1, arg2, getItem(arg0));

    }

    private View mEmptyView;

    /**
     * 当没有数据时，显示的提示内容
     *
     * @param view
     */
    public void setEmptyView(View view) {
        mEmptyView = view;
    }

    /**
     * 当没有数据时，显示的提示内容
     *
     */
    public void setEmptyView(int resId) {
        mEmptyView = LayoutInflater.from(mContext).inflate(resId, null);
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    ;

    /**
     * 判断是否显示空数据提示
     */
    private boolean showEmptyView() {
        return mEmptyView != null && itemdata.size() == 0;
    }
}
