package com.lzhy.moneyhll.me.mine.bean;

import com.lzhy.moneyhll.model.BaseModel;

import java.util.List;

/**
 * Created by cmm on 2016/11/15.
 */

public class InComeModel extends BaseModel {
    public String getIncome;
    public String totalRevenue;
    public String avatar;
    public List<InCome> dataList;

    public class InCome extends BaseModel {
        public int serviceId;
        public int incomeType;
        public double amount;
        public int count;
        public String createTime;
        public int withdrawsMoney;
        public int status;
    }
}
