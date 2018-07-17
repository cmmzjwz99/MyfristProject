package com.lzhy.moneyhll.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3 0003.
 */

public class DragonBallModel extends BaseModel {
    public int status;
    public int commodityTypeId;
    public int merchantId;
    public int dividendId;

    public double cashPrice;
    public double pearlPrice;
    public double unitPrice;
    public double coinPrice;
    public String description;
    public String imageUrl;
    public String title;
    public List<TypeModel> type;
//    缺货标识，0未缺货，1已缺货',
    public int stockout;

    public class TypeModel extends BaseModel {
        /**
         * 商品类型
         */
        public String value;
    }
}
