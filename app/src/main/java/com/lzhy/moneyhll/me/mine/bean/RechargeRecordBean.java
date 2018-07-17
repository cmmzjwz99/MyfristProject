package com.lzhy.moneyhll.me.mine.bean;

import java.util.List;

/**
 * 龙珠充值实体类
 * Created by ycq on 2017/1/12.
 */

public class RechargeRecordBean {

    /**
     * size : 10
     * index : 1  分页索引
     * total : 10 记录总条数
     * count : 1
     * items : [] 充值信息列表
     */

    public int size;
    public int index;
    public int total;
    public int count;
    public List<RechargeRecordItem> items;


}
