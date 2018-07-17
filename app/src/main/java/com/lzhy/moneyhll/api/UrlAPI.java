package com.lzhy.moneyhll.api;

import static com.lzhy.moneyhll.R.string.account;

/**
 * Created by Administrator on 2016/11/2 0002.
 */

public final class UrlAPI {
    private static StringBuilder url;
    /************************************************************
     *创建者;龙之游 @ xu 596928539@qq.com
     *修改时间:2017/1/9 10:28
     *注释: 公司简介
     ************************************************************/
//    public static final String urlCompany = "https://appsvr.lzyhll.com/v1/tourism/Introduction";
    public static final String urlCompany = "https://appsvr.lzyhll.com/v1/tourism/Introduction";
    /**
     * 测试   222222
     */
//    public static final String HOST_SONG = "http://192.168.1.110:8080/lzhyapp/api/"; //宋佳融
//   public static final String HOST_URL = "http://192.168.88.167:8090/lzhyapp/api/"; //姜鹏
//
//    public static final String HOST_URL = "http://120.55.120.167:8080/lzhyapp/api/"; //数据库接口
//    public static final String APP_H5_HOST = "http://devwp.lzyhll.com/v1/"; //h5
//    public static final String URL_PAY = "http://120.55.120.167:8080/lzhyapp/v1/api/pay/gateway"; //支付


    /**
     * 正式
     */
    public static final String HOST_URL = "http://ssl.app.lzyhll.com:8080/api/"; //数据库接口
    public static final String APP_H5_HOST = "https://appsvr.lzyhll.com/v1"; //h5
    public static final String URL_PAY = "http://ssl.app.lzyhll.com:8080/v1/api/pay/gateway"; //支付

    /**
     * 首页banner url
     *
     * @return
     */
    public static String getHomeBannerUrl() {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append("https://xiangqi.qzyunfengkong.com/api/");
        url.append("home/get_brands");
        return url.toString();
    }

    /**
     * 首页列表url
     *
     * @return
     */
    public static String getHomeListUrl(double lat, double lng) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("project/index");
        url.append("?lat=" + lat);
        url.append("&lng=" + lng);
        return url.toString();
    }


    /**
     * 房车品牌
     *
     * @return
     */
    public static String getCarRoomDisplayUrl(int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("brands/list");
        url.append("?index=" + index);
        return url.toString();
    }

    /**
     * 房车品牌
     *
     * @return
     */
    public static String getMotorhomeShowUrl(int id) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("brandsDetail/" + id);
        return url.toString();
    }

    /**
     * 龙珠全部商城 url
     *
     * @return
     */
    public static String getDragonBallUrl(int index, String title, int commodityTypeId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("pearl");
        url.append("?index=" + index);
        if (title != null && title != "") {
            url.append("&title=" + title);
        }
        if (commodityTypeId != 0) {
            url.append("&commodityTypeId=" + commodityTypeId);
        }
        return url.toString();
    }

    /**
     * 确认商品 url
     *
     * @return
     */
    public static String getGoodsSureUrl(int id) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("commodityconfirmationList");
        url.append("/" + id);
        return url.toString();
    }

    /**
     * 租房车列表url
     *
     * @return
     */
    public static String getMotorhomeListUrl(String address, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("product/list");
        url.append("?index=" + index);
        if (!"全部".equals(address)) {
            url.append("&address=" + address);
        }
        return url.toString();
    }

    /**
     * 预定房车url
     *
     * @return
     */
    public static String getMakeOrderUrl(String id, String account) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("product/reservation");

        url.append("?productId=" + id);

        url.append("&account=" + account);
        return url.toString();
    }

    /**
     * 下单预定房车url
     * 用户ID：userId，房车ID：productId，提车时间：startDate，还车时间：endDate
     *
     * @return
     */
    public static String getSureMakeOrderUrl(int userId, String productId, String startDate, String endDate) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("product/order");

        url.append("?userId=" + userId);
        url.append("&account=" + account);
        url.append("&productId=" + productId);
        url.append("&startDate=" + startDate);
        url.append("&endDate=" + endDate);
        return url.toString();
    }

    /**
     * 下单去支付房车url
     * 房车使用费：price，服务费：serviceCharge,用户Id:userId,保证金：margin
     *
     * @return
     */

    public static String getSureToPayUrl(double price, double serviceCharge, int userId, double margin) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("product/confirmPayment");

        url.append("?price=" + price);
        url.append("&serviceCharge=" + serviceCharge);
        url.append("&userId=" + userId);
        url.append("&margin=" + margin);
        return url.toString();
    }

    /**
     * 下单去支付房车url
     * 开始时间：startDate，结束时间：endDate，服务费：serviceCharge,保证金：margin，房车使用费：price，
     * 用户ID:userId,产品ID:productId,提车人姓名：name,身份证号：idCard,档案编号：licenceNo，手机号：account,网点id branchId
     *
     * @return
     */
    public static String getToChoosePayTypeUrl(String startDate, String endDate, String serviceCharge, String margin, String price
            , int userId, String productId, String name, String idCard, String licenceNo, String account, String branchId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("product/payment");

        url.append("?startDate=" + startDate);
        url.append("&endDate=" + endDate);
        url.append("&serviceCharge=" + serviceCharge);
        url.append("&margin=" + margin);
        url.append("&price=" + price);
        url.append("&userId=" + userId);
        url.append("&productId=" + productId);
        url.append("&name=" + name);
        url.append("&idCard=" + idCard);
        url.append("&licenceNo=" + licenceNo);
        url.append("&account=" + account);
        url.append("&branchId=" + branchId);
        return url.toString();
    }

    /**
     * 获取所有省份url
     *
     * @return
     */
    public static String getAllProvinceUrl() {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("product/allProvince");
        return url.toString();
    }

    /**
     * 去哪玩列表url
     *
     * @return
     */
    public static String getGoWhereListUrl(int type, int index, String projectname, Double lat, Double lng, int pid) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("project/list");
        url.append("?type=" + type);
        url.append("&index=" + index);
        if (projectname != null && projectname.length() > 0) {
            url.append("&projectname=" + projectname);
        }
        if (lat > 0) {
            url.append("&lat=" + lat);
        }
        if (lng > 0) {
            url.append("&lng=" + lng);
        }
        url.append("&pid=" + pid);
        return url.toString();
    }

    /**
     * 玩什么分类列表url
     *
     * @return
     */
    public static String getPalyWhatUrl(int type) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("nearByGroup");
        url.append("?type=" + type);
        return url.toString();
    }

    /**
     * 玩什么项目列表url
     *
     * @return
     */
    public static String getPalyWhatListUrl(String pid, String type, int index, String lat, String lng, String scope) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("project/list");
        url.append("?index=" + index);
        if (type != null) {
            url.append("&type=" + type);
        } else {
            url.append("&type=" + 101);
        }
        if (pid != null) {
            url.append("&pid=" + pid);
        } else {
            url.append("&pid=" + 0);
        }
        if (lat != null) {
            url.append("&lat=" + lat);
        }
        if (lng != null) {
            url.append("&lng=" + lng);
        }
        if (scope != null) {
            url.append("&scope=" + scope);
        }
        return url.toString();
    }

    /**
     * 获取热门词
     *
     * @return
     */
    public static String getAllHotKeyUrl() {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("search/list");
        return url.toString();
    }

    /*
    * 精品线路
    *
    * */
    public static String HQualityRouteShowUrl() {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("news");
        return url.toString();
    }

    /*
    * 特色日本
    *
    *
    */
    public static String FeatureJapanShowUrl() {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("japanTrip");
        return url.toString();
    }

    /*
    * 自驾保险
    * */
    public static String DriveInsuranceShowUrl() {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("safe");
        return url.toString();
    }

    /*
    * 旅游签证
    * */
    public static String TouristVisaShowUrl() {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("visa");
        return url.toString();
    }

    /*
    * 机票预订
    * */
    public static String TicketBookShowUrl() {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("flight");
        return url.toString();
    }


    /*
         短信验证码(注册)
  */
    public static String getCodeUrl(String mobile) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/doRegist");
        url.append("?mobile=" + mobile);
        return url.toString();
    }

    /*
       短信验证码(手机快捷登陆)
*/
    public static String getPhoneCodeUrl(String phoneNum) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/sendSms");
        url.append("?phoneNum=" + phoneNum);
        return url.toString();
    }

    /*
    短信验证码(找回密码)
*/
    public static String getFindCodeUrl(String account) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/sendEmsForResetPwd");
        url.append("?account=" + account);
        return url.toString();
    }


 /*   *//*
         完成注册
  *//*
    public static String getRegisterUrl(String mobile, String sms, String name, String pwd, int driveId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/Regist");
        url.append("?mobile=" + mobile);
        url.append("&sms=" + sms);
        url.append("&name=" + name);
        url.append("&pwd=" + pwd);
        url.append("&driveId=" + driveId);
        return url.toString();
    }*/

    /*
        登录
  */
    public static String getLoginUrl(String account, String pwd) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/login");
        url.append("?account=" + account);
        url.append("&pwd=" + pwd);

        return url.toString();
    }

 /*   *//*
       手机登录
 *//*
    public static String getPhoneLoginUrl(String phoneNum, String sms, String driveId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/loginForPhone");
        url.append("?phoneNum=" + phoneNum);
        url.append("&sms=" + sms);
        url.append("&driveId=" + driveId);
        return url.toString();
    }*/


    /**
     * 找回密码(下一步)
     */
    public static String getFindNextUrl(String account, String sms) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/doResetlogPwd");
        url.append("?account=" + account);
        url.append("&sms=" + sms);
        return url.toString();
    }

    /**
     * 找回密码(下一步/确认)
     */
    public static String getResetUrl(String mobile, String pwd) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/ResetlogPwd");
        url.append("?mobile=" + mobile);
        url.append("&pwd=" + pwd);
        return url.toString();
    }

    /**
     * 获取所有省份url(房车预约)
     *
     * @return
     */
    public static String getProvinceUrl(String province, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("product/getBranchList");
        url.append("?province=" + province);
        url.append("&index=" + index);
        return url.toString();
    }

    /**
     * 获取所有网点信息
     */
    public static String getAllBranchListUrl(String province, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("product/getBranchList");
        url.append("?province=" + province);
        url.append("&index=" + index);
        return url.toString();
    }

    /**
     * 赠送
     */
    public static String getGiveUrl(String account, String giftAccount, String pearlNum, String couponNum, String payPwd) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/give");
        url.append("?account=" + account);
        url.append("&giftAccount=" + giftAccount);
        url.append("&pearlNum=" + pearlNum);
        url.append("&couponNum=" + couponNum);
        url.append("&payPwd=" + payPwd);
        return url.toString();
    }

    /**
     * 赠送(下一步)
     */
    public static String getGiveNextUrl(String account, int userId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/dogive");
        url.append("?account=" + account);
        url.append("&userId=" + userId);
        return url.toString();
    }

    /**
     * 赠送记录url
     *
     * @return
     */
    public static String getGiveRecordUrl(int memberId, int type, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/GiftLog");
        url.append("?userId=" + memberId);
        url.append("&type=" + type);
        url.append("&index=" + index);
        return url.toString();
    }

    /**
     * 绑定房车卷
     */
    public static String getBindRollUrl(String couponCoding, String activeCode, String account) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/bindCoupon");
        url.append("?couponCoding=" + couponCoding);
        url.append("&activeCode=" + activeCode);
        url.append("&account=" + account);
        return url.toString();
    }

    /**
     * 商品订单url
     *
     * @return
     */
    public static String getGoodsOrderListUrl(int memberId, int status, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("mall/orderList");
        url.append("?memberId=" + memberId);
        if (status != 0) {
            url.append("&status=" + status);
        }
        url.append("&index=" + index);
        return url.toString();
    }

    /**
     * 创客订单url
     *
     * @return
     */
    public static String getMakerOrderListUrl(int memberId, int status, int index, int type) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("creativeOrder");
        url.append("?userId=" + memberId);
        if (status != 0) {
            url.append("&status=" + status);
        }
        url.append("&index=" + index);
        url.append("&type=" + type);
        return url.toString();
    }

    /**
     * 我是创客创客订单url
     *
     * @return
     */
    public static String getMyProjectListUrl(int index, int userId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("marker/projectList");
        url.append("?index=" + index);
        url.append("&userId=" + userId);
        return url.toString();
    }

    /**
     * 我是创客创客订单url
     *
     * @return
     */
    public static String getIImMakerOrderListUrl(int memberId, int status, int index, int projectType) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("project/makerorder");
        url.append("?userId=" + memberId);
        if (status != 0) {
            url.append("&status=" + status);
        }
        url.append("&index=" + index);
        url.append("&projectType=" + projectType);
        return url.toString();
    }

    /**
     * 核销url
     *
     * @return
     */
    public static String getVerificationUrl(int userId, String checkCode) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("updateWriteoff");
        url.append("?checkCode=" + checkCode);
        url.append("&userId=" + userId);
        return url.toString();
    }

    /**
     * 创客商品确认收货url
     *
     * @return
     */
    public static String getSureReceiveUrl(int orderId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("updatestatus/");
        url.append(orderId);
        return url.toString();
    }

    /**
     * 创客商品确认fa货url
     *
     * @return
     */
    public static String getSureSendUrl(int orderId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("updateShipStatus/");
        url.append(orderId);
        return url.toString();
    }

    /**
     * 我的预约url
     *
     * @return
     */
    public static String getAppointmentListUrl(int userId, int status, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("branch/orderList");
        url.append("?userId=" + userId);
        url.append("&status=" + status);
        url.append("&index=" + index);
        return url.toString();
    }

    /**
     * 取消预约url
     *
     * @return
     */
    public static String getcancelOrderUrl(String orderId, int status) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("branch/cancelOrder");
        url.append("?orderId=" + orderId);
        url.append("&status=" + status);
        return url.toString();
    }

    /**
     * 项目订单url
     *
     * @return
     */
    public static String getProjectOrderListUrl(int memberId, int status, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("project/orderList");
        url.append("?memberId=" + memberId);
        url.append("&status=" + status);
        url.append("&index=" + index);
        return url.toString();
    }

    /**
     * 项目订单发表评论url
     *
     * @return
     */
    public static String getProjectCommentUrl(int pid, int memberId, int star, String content) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("project/insert");
        url.append("?pid=" + pid);
        url.append("&memberId=" + memberId);
        if (content == null) {
            url.append("&star=" + star);
            url.append("&content=" + content);
        }
        return url.toString();
    }

    /**
     * 退货url
     *
     * @return
     */
    public static String getReturnGoodsUrl(String orderId, int status, String remark) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("mall/return");
        url.append("?orderId=" + orderId);
        url.append("&status=" + status);
        url.append("&remark=" + remark);
        return url.toString();
    }

    /**
     * 确认收货状态更改url
     *
     * @return
     */
    public static String getSureReceiveGoodsUrl(String orderId, int status) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("mall/confirm");
        url.append("?orderId=" + orderId);
        url.append("&status=" + status);
        return url.toString();
    }

    /**
     * 得到收货地址url
     *
     * @return
     */
    public static String getAllAddreaaUrl(String req) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("v1/addressList");
        url.append("?req=" + req);
        return url.toString();
    }

    /**
     * 得到默认收货地址url
     *
     * @return
     */
    public static String getDefaultAddreaaUrl(String req) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("v1/user/address/getDefault");
        url.append("?req=" + req);
        return url.toString();
    }

    /**
     * 我要赚龙珠获取用户信息url
     *
     * @return
     */
    public static String getUserMakeInfoUrl(String account) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/doEarnPearls");
        url.append("?account=" + account);
        return url.toString();
    }

    /**
     * 我要赚龙珠url
     *
     * @return
     */
    public static String getMakeDragonBallUrl(String mobile, String name,
                                              String IdCard, String recommendCode) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/earnPearls");
        url.append("?mobile=" + mobile);
        url.append("&name=" + name);
        url.append("&IdCard=" + IdCard);
        url.append("&recommendCode=" + recommendCode);
        return url.toString();
    }

    /**
     * 个人信息url(保存)
     *
     * @return
     */
    public static String getUserSaveInfoUrl(int memberId, String licenceNo, String address,
                                            String nickName, String idCard, String realName, int gender) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/updateUser");
        url.append("?memberId=" + memberId);
        url.append("&licenceNo=" + licenceNo);
        url.append("&address=" + address);
        url.append("&nickName=" + nickName);
        url.append("&idCard=" + idCard);
        url.append("&realName=" + realName);
        url.append("&gender=" + gender);
        return url.toString();
    }

    /**
     * 个人信息url
     *
     * @return
     */
    public static String getUserInfoUrl(int memberId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/userInfo");
        url.append("?memberId=" + memberId);
        return url.toString();
    }


    /**
     * 个人信息设置支付密码url
     *
     * @return
     */
    public static String getPayPwdUrl(int memberId, String payPwd, String checkPayPwd) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/setPayPwd");
        url.append("?memberId=" + memberId);
        url.append("&payPwd=" + payPwd);
        url.append("&checkPayPwd=" + checkPayPwd);
        return url.toString();
    }


    /**
     * 个人信息修改支付密码url
     *
     * @return
     */
    public static String getUserPayPwdUrl(int memberId, String oldPayPwd, String newPayPwd, String checkPayPwd) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/editPayPwd");
        url.append("?memberId=" + memberId);
        url.append("&oldPayPwd=" + oldPayPwd);
        url.append("&newPayPwd=" + newPayPwd);
        url.append("&checkPayPwd=" + checkPayPwd);
        return url.toString();
    }

    /**
     * 个人信息重置支付密码url
     *
     * @return
     */
    public static String getSettingPayPwdUrl(String phoneNum, String sms, String newPayPwd) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/resetPayPwd");
        url.append("?phoneNum=" + phoneNum);
        url.append("&sms=" + sms);
        url.append("&newPayPwd=" + newPayPwd);
        return url.toString();
    }

    /**
     * 个人信息重置支付密码获取验证码url
     *
     * @return
     */
    public static String getSettingPayPwdCodeUrl(String mobile) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/doResetPayPwd");
        url.append("?mobile=" + mobile);
        return url.toString();
    }


    /**
     * 个人信息设置登录密码  getMyMotorHomeRollUrl
     */
    public static String getUserLoginPwdUrl(int memberId, String oldPwd, String newPwd, String checkPwd) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/editPwd");
        url.append("?memberId=" + memberId);
        url.append("&oldPwd=" + oldPwd);
        url.append("&newPwd=" + newPwd);
        url.append("&checkPwd=" + checkPwd);
        return url.toString();
    }


    /**
     * 我的房车劵  getMyMotorHomeRollUrl
     */
    public static String getMyMotorHomeRollUrl(int memberId, int status) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/getTicket");
        url.append("?memberId=" + memberId);
        url.append("&status=" + status);
        return url.toString();
    }

    /**
     * 我的团队
     */
    public static String getTeamUrl(int memberId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/team");
        url.append("?memberId=" + memberId);
        return url.toString();
    }

    /**
     * 我的预约(根据状态获取我的预约中的列表)
     */
    public static String getMyAppointListUrl(int userId, int status, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("branch/orderList");
        url.append("?userId=" + userId);
        url.append("&status=" + status);
        url.append("?index=" + index);
        return url.toString();
    }


    /**
     * 我的预约(订单中代缴清 待提车 取消订单)
     * 根据订单编号更改订单状态
     */
    public static String getMyOrderListUrl(int orderId, int status) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("branch/cancelOrder");
        url.append("?orderId=" + orderId);
        url.append("&status=" + status);
        return url.toString();
    }

    /**
     * 我的收益
     * （用于获取我的收益）
     */
    public static String getMyIncomeUrl(int memberId, String payType) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/service/dataList");
        url.append("?memberId=" + memberId);//105267 测试库数据
        url.append("&payType=" + payType);
        return url.toString();
    }

    /**
     * 我的收益明细
     * （用于获取我的收益明细）
     */
    public static String getMyEarningUrl(int memberId, int index, String payType) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/service/list");
        url.append("?memberId=" + memberId);
        url.append("&payType=" + payType);
        url.append("&index=" + index);
        return url.toString();
    }

    /**
     * 我的收益明细
     * （折线图接口）
     */
    public static String getZheXianTuUrl(int memberId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/service/dataList");
        url.append("?memberId=" + memberId);
        return url.toString();
    }


    /**
     * 龙币提现
     */
    public static String getWithDrawUrl(int memberId, int type, String bankName, String bankBranch,
                                        String name, String bankNumber, double amount) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/withDraw");
        url.append("?memberId=" + memberId);
        url.append("&type=" + 1);
        url.append("&bankName=" + bankName);
        url.append("&bankBranch=" + bankBranch);
        url.append("&name=" + name);
        url.append("&bankNumber=" + bankNumber);
        url.append("&amount=" + amount);
        return url.toString();
    }

    /**
     * 现金提现
     */
    public static String getCashUrl(int memberId, int type, String bankName, String bankBranch,
                                    String name, String bankNumber, double cash) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/withDraw");
        url.append("?memberId=" + memberId);
        url.append("&type=" + 1);
        url.append("&bankName=" + bankName);
        url.append("&bankBranch=" + bankBranch);
        url.append("&name=" + name);
        url.append("&bankNumber=" + bankNumber);
        url.append("&amount=" + cash);
        return url.toString();
    }

    /**
     * 提现记录
     */
    public static String getTiXianRecordUrl(int memberId, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/getRecord");
        url.append("?memberId=" + memberId);
        url.append("&withDrawType=" + 1);
        url.append("&index=" + index);
        return url.toString();
    }

    /**
     * 确认是否登录
     */
    public static String getIsLoginUrl(int memberId, String driveId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/singleSign");
        url.append("?memberId=" + memberId);
        url.append("&driveId=" + driveId);
        url.append("&platform=android");
        return url.toString();
    }

    /**
     * 龙珠充值记录
     *
     * @param userId 用户ID
     * @param index  分页索引
     * @return
     */
    public static String getRechargeRecordUrl(int userId, int index) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("user/RechargeLog");
        url.append("?userId=" + userId);
        url.append("&index=" + index);
        return url.toString();
    }
    /**
     * 创客详情
     *
     * @param projectId 用户创客id
     * @return
     */
    public static String getMakerModelUrl(int type,int projectId) {
        if (url == null) {
            url = new StringBuilder();
        } else {
            url.delete(0, url.length());
        }
        url.append(HOST_URL);
        url.append("maker/project/view");
        url.append("?type=" + type);
        url.append("&projectId=" + projectId);
        return url.toString();
    }
}
