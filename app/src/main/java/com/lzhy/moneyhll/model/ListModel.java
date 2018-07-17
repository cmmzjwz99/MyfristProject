package com.lzhy.moneyhll.model;


import java.util.List;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public class ListModel<T> extends BaseModel {
    /**
     * 总条目
     */
    public int total;
    /**
     * 集合
     */
    public List<T> items;
    /**
     * 集合
     */
    public List<T> list;
}
