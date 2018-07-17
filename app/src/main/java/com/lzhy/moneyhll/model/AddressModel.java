package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public final class AddressModel extends BaseModel{
    /**
     * 用户Id
     */
    public int userId ;
    /**
     * 姓名
     */
    public String name ;
    /**
     * 电话
     */
    public String phone ;
    /**
     * 省
     */
    public String province ;
    /**
     * 市
     */
    public String city  ;
    /**
     * 区
     */
    public String addresss ;
    /**
     * 详细地址
     */
    public String district ;
    /**
     * 是否默认
     */
    public int isDefaultAddress;

}