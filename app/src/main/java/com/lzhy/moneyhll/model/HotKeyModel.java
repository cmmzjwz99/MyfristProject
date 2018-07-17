package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class HotKeyModel extends BaseModel{
    public String phrase;
    /**
     * 2表示露营地搜索
     */
    public int module;
    /**
     * 1表示热门搜索词
     */
    public int type;
    /**
     * 搜索次数
     */
    public int frequency;
}
