package com.lzhy.moneyhll.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lzhy.moneyhll.home.beans.UserInfoModel;

import static com.lzhy.moneyhll.constant.Constant.LOGOUT_CLICK_ID;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/****************************************************************************
 * Created by xu on 2017/1/13.
 * Function:
 ***************************************************************************/

public final class UtilApp {
    /************************************************************
     *修改者;  龙之游 @ xu 596928539@qq.com
     *修改时间:2016/12/26 9:53
     *functon:获取当前程序的版本号
     *bug:  方法名和返回值不匹配
     *修复:
     ************************************************************/

    public static PackageInfo getPackageInfo(Context context){
        //获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }
    //退出登录
    public static void exitLogin(Context context) {
        UserInfoModel.getInstance().clear();
        UserInfoModel.getInstance().sync();
        setCustomStatisticsKV(context, LOGOUT_CLICK_ID,"用户退出");//统计
    }
}
