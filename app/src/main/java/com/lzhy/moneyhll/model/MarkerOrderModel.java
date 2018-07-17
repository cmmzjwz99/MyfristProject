package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/26 0026.
 */

public class MarkerOrderModel extends BaseModel {
    public String remark;
    public double totalAmount;
    //订单号
    public String orderCoding;
    //份数
    public int quantity;
    //订单状态
    public int status;
    //项目图
    public String picture1;
    //项目名字
    public String projectName;
    //项目价格
    public double adultPrice;
    //下单时间
    public String createTime;
    //商家联系电话
    public String consumerHotline;
    //地址
    public String address;
    //验证码
    public String checkCode;
    //购买人
    public String receiverName;
    //收件人电话
    public String telephone;
    //收件人地址
    public String expressAddress;
    //经纬度
    public double latitude;
    public double longitude;
    public String realName;
    public String account;


    public double payAmount ;
    public String  reserveTime;
}
