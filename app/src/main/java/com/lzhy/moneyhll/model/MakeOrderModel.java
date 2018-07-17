package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class MakeOrderModel extends BaseModel{
    public MemberModel member;
    public ProductInfoModel productInfo;

    public class MemberModel extends BaseModel {
        public String account;
        public int status ;
        public int serviceStatus ;
        public int pearls ;
        public int coupon ;
        public String realName ;
        public String memberId ;
        public String cardId ;
        public String licenceNo;
        public String idCard;
    }

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
}
