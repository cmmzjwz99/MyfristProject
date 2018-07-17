package com.lzhy.moneyhll.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15 0015.
 */

public class SureMakeOrderModel extends BaseModel {
    public ServiceModel serviceCost;
    /**
     * 使用天数（比实际使用天数少1）
     */
    public int day;

    public class ServiceModel extends BaseModel {
        /**
         * 服务以及相关附加费
         */
        public List<ServiceCostModel> Group1;
        /**
         * 车辆及违章保险金
         */
        public List<ServiceCostModel> Group2;
    }
}
