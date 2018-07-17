package com.lzhy.moneyhll.me.loginOrRegister.bean;

import android.graphics.Bitmap;

/**
 * Created by cmm on 2016/11/1.
 */

public class LoginBean {
    /**
     * 用户注册标示id
     */
    private int id;
    /**
     * 用户名
     */
    public String account;
    /**
     * 手机号：mobile
     */
    private String password;
    private String mobile;
    //我的房车劵
    public int status;
    private int coupon;
    private int userId;
    private int cardId;

    public int getRepearl() {
        return repearl;
    }

    public void setRepearl(int repearl) {
        this.repearl = repearl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getRecoin() {
        return recoin;
    }

    public void setRecoin(int recoin) {
        this.recoin = recoin;
    }

    private int repearl;
    private int recoin;

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    /**
     * 验证码：sms
     */
    private String sms;
    /**
     * 登录密码：pwd
     */
    private String pwd;
    /**
     * 真实姓名： name
     */
    private String name;
    /**
     * 手设备唯一标识：driveId
     */

    //头像
    public String avatar;
    //昵称
    public String nickName;
    //龙码
    public String recommendCode;


    private int driverId;

    public String token;


    public long time;

    public String appId;

    private String accessToken;

    private String refreshToken;

    private String sign;

    private String device;

    public String phoneNum;

    //赠送 ：会员账号：account，赠送人账号：giftAccount，龙珠数量：pearlNum，房车券数量：couponNum,支付密码：payPwd
    public String giftAccount;
    public String pearlNum;
    public String couponNum;

    //,"carTicket":"2","coins":150000.0,"pearls":150000,

    //房车卷，龙币，龙珠
    public int carTicket;
    public float coins;
    public float pearls;


    //0是消费者 2是消费商  4是服务商
    public int serviceStatus;
    //判断是否为创客2为创客
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //我的明细收益
    public int index;

    //个人信息修改登录密码
    public String oldPwd;
    public String newPwd;
    public String checkPwd;

    //个人信息修改支付密码
    public String oldPayPwd;
    public String newPayPwd;
    public String checkPayPwd;

    //个人信息
    public int memberId;
    public String licenceNo;
    //    public String nickName;
    public String idCard;
    public String realName;
    public int gender;
    public Bitmap img;
    public String address;
    //用户支付密码
    public String payPwd;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRecommendCode() {
        return recommendCode;
    }

    public void setRecommendCode(String recommendCode) {
        this.recommendCode = recommendCode;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }


    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCheckPayPwd() {
        return checkPayPwd;
    }

    public void setCheckPayPwd(String checkPayPwd) {
        this.checkPayPwd = checkPayPwd;
    }

    public String getOldPayPwd() {
        return oldPayPwd;
    }

    public void setOldPayPwd(String oldPayPwd) {
        this.oldPayPwd = oldPayPwd;
    }

    public String getNewPayPwd() {
        return newPayPwd;
    }

    public void setNewPayPwd(String newPayPwd) {
        this.newPayPwd = newPayPwd;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getCheckPwd() {
        return checkPwd;
    }

    public void setCheckPwd(String checkPwd) {
        this.checkPwd = checkPwd;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGiftAccount() {
        return giftAccount;
    }

    public void setGiftAccount(String giftAccount) {
        this.giftAccount = giftAccount;
    }

    public String getPearlNum() {
        return pearlNum;
    }

    public void setPearlNum(String pearlNum) {
        this.pearlNum = pearlNum;
    }

    public String getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(String couponNum) {
        this.couponNum = couponNum;
    }

    public int getCarTicket() {
        return carTicket;
    }

    public void setCarTicket(int carTicket) {
        this.carTicket = carTicket;
    }

    public float getCoins() {
        return coins;
    }

    public void setCoins(float coins) {
        this.coins = coins;
    }

    public float getPearls() {
        return pearls;
    }

    public void setPearls(float pearls) {
        this.pearls = pearls;
    }


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

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public void copy(LoginBean model) {

        this.id = model.id;
//        Log.i("userInfoq", "copy:1 "+this.id);
        this.account = model.account;
        this.password = model.password;
        this.status = model.status;
        this.serviceStatus = model.serviceStatus;
        this.carTicket = model.carTicket;
        this.coins = model.coins;
        this.pearls = model.pearls;
        this.coupon = model.coupon;
        this.nickName = model.nickName;
        this.realName = model.realName;
        this.token = model.token;
        this.memberId = model.memberId;
        this.userId = model.userId;
        this.cardId = model.cardId;
        this.idCard = model.idCard;
        this.licenceNo = model.licenceNo;
        this.recoin = model.recoin;
        this.repearl = model.repearl;
        this.recommendCode = model.recommendCode;
        this.type = model.type;
    }

    /**
     * 赋值
     *
     * @param model
     */
    public void updateValue(LoginBean model) {
        if (model == null) {
            return;
        }

        this.realName = model.realName;
        this.nickName = model.nickName;
        this.idCard = model.idCard;
        this.licenceNo = model.licenceNo;

        this.gender = model.gender;
        this.address = model.address;
        this.password = model.password;
        this.pearls = model.pearls;
    }
}
