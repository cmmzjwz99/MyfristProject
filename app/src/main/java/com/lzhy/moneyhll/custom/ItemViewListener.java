package com.lzhy.moneyhll.custom;

import android.view.View;
import android.view.ViewGroup;

/**
 * item
 */
public interface ItemViewListener {
    /**
     * @param id
     * @param itemView
     * @param vg
     * @param data     某一个item对应的数据
     */
    public View getView(int id, View itemView, ViewGroup vg, Object data);
}
