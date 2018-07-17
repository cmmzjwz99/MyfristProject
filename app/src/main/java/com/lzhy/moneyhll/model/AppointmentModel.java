package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/15 0015.
 */

public final class AppointmentModel extends BaseModel {
    /**
     * 订单id
     */
    public String orderId;
    /**
     * 用户id
     */
    public int userId;
    public int payDecide;
    /**
     * 订单状态
     * 9代缴清 3待提车 4旅途中 5待结算 6已完结
     */
    public int status;
    /**
     * 图片
     */
    public String imageUrl;
    /**
     * 所属网点
     */
    public String area;
    /**
     * 车牌号码
     */
    public String carNum;
    /**
     * 提车地点
     */
    public String areaAdd;
    /**
     * 联系电话
     */
    public String areaTel;
    /**
     * 提车人
     */
    public String mentionCar;
    /**
     * 还车人
     */
    public String returnCar;
    /**
     * 开始时间
     */
    public String startTime;
    /**
     * 结束时间
     */
    public String endTime;
    /**
     * 房车使用费
     */
    public double usePrice;
    /**
     *车辆及保险违约金
     */
    public double margin;
    /**
     * 车型
     */
    public String carName;
    /**
     * 车辆及保险违约金实付
     */
    public double marginPay;
    /**
     * 服务费及相关附加费
     */
    public double serve;
    /**
     * 服务费及相关附加费实付
     */
    public double servePay;
    /**
     * 需付
     */
    public double needPay;

}
