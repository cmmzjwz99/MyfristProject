package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/10 0010.
 */

public class GoodsOrderModel extends BaseModel {
    /**
     * 订单状态（待发货4  代签收18  已签收20 已完结8 退换货16
     */
    public int status ;
    /**
     * 订单号
     */
    public String orderCoding ;
    /**
     * 商品名
     */
    public String title ;
    /**
     * 商品图片
     */
    public String imageUrl ;
    /**
     * 购买数量
     */
    public int quantity;
    /**
     *支付现金
     */
    public double cashPrice;
    /**
     * 支付龙币
     */
    public double coinPrice;
    /**
     * 支付龙珠
     */
    public double pearlPrice ;
    /**
     * 秒杀龙珠价
     */
    public double promotionPrice ;
}
