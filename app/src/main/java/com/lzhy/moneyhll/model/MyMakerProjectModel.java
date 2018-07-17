package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/12/16 0016.
 */

public class MyMakerProjectModel extends BaseModel {
    public int type;
    public String projectName;
    public String projectPic;
    public double clearingPrice;
    public double income;
    public long startTime;

    public MakerTotal makerTotal;

    public class MakerTotal {
        /**
         * 待支付
         */
        public int abePaid;
        /**
         * 待使用
         */
        public int bbeUsed;
        /**
         * 已完结
         */
        public int ebeFinished;
        /**
         * 已失效
         */
        public int fbeFailed;
        /**
         * 待发货
         */
        public int cbeShipped;
        /**
         * 已发货
         */
        public int dbeDeliver;
    }
}
