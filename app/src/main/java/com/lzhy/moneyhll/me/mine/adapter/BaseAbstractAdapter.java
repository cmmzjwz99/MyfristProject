package com.lzhy.moneyhll.me.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmm on 2016/11/1.
 */

public abstract class BaseAbstractAdapter<T> extends BaseAdapter {

    protected List<T> list;
    protected LayoutInflater mInflater;
    protected Context context;

    public BaseAbstractAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public BaseAbstractAdapter() {
    }

    public void add(T t) {
        list.add(t);
    }

    public void addList(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position >= 0 && position < list.size()) {
            list.remove(position);
        }
    }

    public void removeAll() {
        if (null != list) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position >= list.size())
            return null;
        T t = list.get(position);
        return null != t ? t : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
