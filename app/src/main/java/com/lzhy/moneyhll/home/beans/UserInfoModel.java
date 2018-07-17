package com.lzhy.moneyhll.home.beans;

import android.content.SharedPreferences;
import android.util.Log;

import com.lzhy.moneyhll.LtApplication;
import com.lzhy.moneyhll.me.loginOrRegister.bean.LoginBean;

/**
 * Created by Administrator on 2016/11/12 0012.
 * * 登录用户的信息基本类
 */

public class UserInfoModel extends LoginBean {
    /**
     * 获取单例模式
     *
     * @return
     */

    private static UserInfoModel instance;


    public static synchronized UserInfoModel getInstance() {
        if (instance == null) {
            instance = new UserInfoModel();
        }
        return instance;
    }

    /**
     * 构造函数,获取本地保存的用户数据
     * //房车卷，龙币，龙珠
     * //头像
     * //昵称
     * //龙码
     */
    private UserInfoModel() {
        SharedPreferences sp = LtApplication.getInstance()
                .getSharedPreferences("Login", LtApplication.MODE_PRIVATE);

        setId(sp.getInt("id", 0));
        setAccount(sp.getString("account", null));
        setPassword(sp.getString("password", null));
        setStatus(sp.getInt("status", 0));
        setServiceStatus(sp.getInt("serviceStatus", 0));
        setCarTicket(sp.getInt("carTicket", 0));
        setCoins(sp.getFloat("coins", 0));
        setPearls(sp.getFloat("pearls", 0));
        setCoupon(sp.getInt("coupon", 0));
        setNickName(sp.getString("nickName", null));
        setRealName(sp.getString("realName", null));
        setToken(sp.getString("token", null));
        setMemberId(sp.getInt("memberId", 0));
        setUserId(sp.getInt("userId", 0));
        setIdCard(sp.getString("idCard", null));
        setLicenceNo(sp.getString("licenceNo", null));
        setRecoin(sp.getInt("recoin", 0));
        setRepearl(sp.getInt("repearl", 0));
        setGender(sp.getInt("gender", 0));
        setAddress(sp.getString("address", null));
        setPayPwd(sp.getString("payPwd", null));
        setType(sp.getInt("type", 0));

        setRecommendCode(sp.getString("recommendCode", null));

    }

    /**
     * 同步数据到本地
     */
    public void sync() {
        SharedPreferences sp = LtApplication.getInstance()
                .getSharedPreferences("Login", LtApplication.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        Log.i("userInfoq", "copy:2 "+getId());
        editor.putInt("id", getId());
        editor.putString("account", getAccount());
        editor.putString("password", getPassword());
        editor.putInt("status", getStatus());
        editor.putInt("serviceStatus", getServiceStatus());
        editor.putInt("carTicket", getCarTicket());

        editor.putFloat("coins", getCoins());
        editor.putFloat("pearls", getPearls());
        editor.putInt("coupon", getCoupon());
        editor.putString("nickName", getNickName());
        editor.putString("realName", getRealName());
        editor.putString("token", getToken());
        editor.putInt("memberId", getMemberId());
        editor.putInt("userId", getUserId());
        editor.putString("cardId", getIdCard());
        editor.putString("licenceNo", getLicenceNo());
        editor.putInt("recoin", getRecoin());
        editor.putInt("repearl", getRepearl());
        editor.putInt("gender", getGender());
        editor.putString("address", getAddress());
        editor.putString("payPwd", getPayPwd());
        editor.putInt("type", getType());
        editor.putString("recommendCode", getRecommendCode());

        editor.commit();
    }

    /**
     * 清空本地保存数据 <br>
     * 清空当前登录用户的登录信息
     */
    public void clear() {
        SharedPreferences sp = LtApplication.getInstance()
                .getSharedPreferences("Login", LtApplication.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();

        setId(0);
        setAccount(null);
        setPassword(null);
        setStatus(0);
        setServiceStatus(-1);
        setCarTicket(0);
        setCoins(0);
        setPearls(0);
        setCoupon(0);
        setNickName(null);
        setRealName(null);
        setToken(null);
        setMemberId(0);
        setUserId(0);
        setIdCard(null);
        setLicenceNo(null);
        setRecoin(0);
        setRepearl(0);
        setGender(0);
        setAddress(null);
        setPayPwd(null);
        setRecommendCode(null);
        setType(0);
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return getId() > 0;
    }

}
