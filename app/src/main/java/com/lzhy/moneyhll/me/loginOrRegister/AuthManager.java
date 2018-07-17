package com.lzhy.moneyhll.me.loginOrRegister;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.lzhy.moneyhll.me.loginOrRegister.bean.LoginBean;
import com.lzhy.moneyhll.utils.Utils;

/**
 * Created by cmm on 2016/11/8.
 */

public final class AuthManager {
    private static final String AUTH_DATA = "com.lzhy.moneyhll.utils.AUTH_DATA";
    private static final String USER = "com.lzhy.moneyhll.utils.USER";

    private static AuthManager authManager;
    private SharedPreferences sp;
    private LoginBean authUser;

    private static Context mContext;//建议不用静态变量
    public static AuthManager getAuthManager(Context context) {
        if (null == authManager && null != context) {
            mContext = context;
            authManager = new AuthManager(context);
        }
        return authManager;
    }

    private AuthManager(Context context) {
        sp = context.getSharedPreferences(AUTH_DATA, Context.MODE_PRIVATE);
        autoLogin();
    }

    private void autoLogin() {
        String test = sp.getString(USER, "");
        if (sp.getString(USER, "").equals("")) {
            //未登录
            Toast.makeText(mContext, "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
        } else {
            //登录状态
            authUser = Utils.jsonToClass(sp.getString(USER, ""), LoginBean.class);
        }
    }

    public void updateUESR(LoginBean user) {
        if(authUser == null){
            this.authUser = user;
        }else{
            authUser.setAccessToken(user.getAccessToken());
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER, Utils.getGson().toJson(authUser));
        editor.commit();

    }

    public void logout() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER, "");
        editor.commit();
        authUser = null;
    }

    public String getAuthToken() {
        if(null == authUser){
            return "";
        }
        return authUser.getAccessToken();
    }

    public LoginBean getAuthUser() {
        return authUser;
    }
}
