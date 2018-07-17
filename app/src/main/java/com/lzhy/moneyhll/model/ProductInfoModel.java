package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class ProductInfoModel extends BaseModel {

    public String permit;
    /**
     * 车名
     */
    public String name;
    /**
     * 单价
     */
    public double price;
    /**
     * 床数
     */
    public int beds;
    /**
     * 图片
     */
    public String imageUrl;
    public String branchId;
    public String address;
    public String branchName;
    //    1可以 2不可以
    public String payCoupon;
    //  1自行式 2拖斗式 3其它
    public int travelType;
    public int nuclearCarrier;
}
