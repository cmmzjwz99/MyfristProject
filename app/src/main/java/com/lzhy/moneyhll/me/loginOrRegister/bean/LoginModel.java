package com.lzhy.moneyhll.me.loginOrRegister.bean;

import com.lzhy.moneyhll.model.BaseModel;

/**
 * Created by cmm on 2016/11/8.
 */

public class LoginModel extends BaseModel {
    //    注册标示id:id,手机号：mobile,验证码： sms,真实姓名： name,登录密码： pwd 设备唯一标识：driveId
    //注册
    private int id;
    private String mobile;
    private String sms;
    private String pwd;
    private String name;
    private String driverId;

    private String accessToken;
    private String refreshToken;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
