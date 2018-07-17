package com.lzhy.moneyhll.utils;

import android.app.Activity;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.loginOrRegister.LoginActivity;
import com.lzhy.moneyhll.me.loginOrRegister.bean.DisparityLoginBean;
import com.lzhy.moneyhll.model.Response1;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.lzhy.moneyhll.LtApplication.driveId;
import static com.lzhy.moneyhll.LtApplication.isOtherDevice;
import static com.lzhy.moneyhll.LtApplication.mContextApp;
import static com.lzhy.moneyhll.LtApplication.whetherCheck;
import static com.lzhy.moneyhll.home.MainActivity.tabMine;
import static com.lzhy.moneyhll.utils.UtilApp.exitLogin;

/**
 * * ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** **
 * created by xu on 2017/1/20.
 * 用途:  判断是否在异地登录
 ***************************************************************************/

public class UtilCheckLogin {
    public static void disparityLogin() {
//        Log.i("login", "disparityLogin: "+"异地登---检查-----------------------------录URL:" + getMillon(System.currentTimeMillis()));
        TelephonyManager TelephonyMgr = (TelephonyManager) mContextApp.getSystemService(TELEPHONY_SERVICE);
        driveId = TelephonyMgr.getDeviceId();
//        Log.i("onClick", "做完异地登录检查线程idname: "+driveId);
        if (whetherCheck) {
            whetherCheck = false;
//            Log.i("onClick", "2---------开始做异地登陆检查: ");
            String getMyMotorHomeRollUrl = UrlAPI.getIsLoginUrl(UserInfoModel.getInstance().getId(), driveId);
            OkHttpUtils.get().url(getMyMotorHomeRollUrl).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    PrintLog.e("异地登录URL:" + e);
                }

                @Override
                public void onResponse(String response, int id) {
//                    Log.i("onClick", "3--------------当前response线程id: "+Thread.currentThread().getId());
                    try {
                        Type type = new TypeToken<Response1<DisparityLoginBean>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response1<DisparityLoginBean> resp = gson.fromJson(response, type);
                        if (resp.getErrCode() == null) {
                            return;
                        }
                        if (resp.getData().sign == 1) {
                            exitLogin(mContextApp);
                            isOtherDevice = true;
                            Log.i("onClick", "做完异地登录检查线程idname: "+driveId);
//                            Log.i("onClick", "做完异地登录检查: "+isOtherDevice);
                            gonaLogin();//此处，如果用户已在别的地方登录  会执行
//                            Log.i("onClick", "当前线程id: "+android.os.Process.myTid());
                        }
                    } catch (Exception e) {

                    }
                }
            });

        }
    }

    public static void gonaLogin() {
        whetherCheck = false;
        Intent intent = new Intent(UtilActivityManager.getInstance().getCurrentActivity(), LoginActivity.class);
        UtilToast.getInstance().showDragonInfo("您的账号在其他地方登录"); ;
        Activity activity = UtilActivityManager.getInstance().getCurrentActivity();
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    /************************************************************
     * 创建者;龙之游 @ xu 596928539@qq.com
     * 修改时间:2017/1/9 20:05
     * 注释: 在我的界面的内容  有点击事件  则先检查用户是否登陆  没有登录则不能操作
     * 是否登录    此功能的实现做不到同步
     ************************************************************/
    public static boolean beforeClickInMine() {
//        Log.i("onClick", "1------------------: "+android.os.Process.myTid());
        whetherCheck = true;
        disparityLogin();
//        Log.i("onClick", "用戶是否在异地登陆: "+isOtherDevice);
        return isOtherDevice;
    }

    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2017/1/9 19:01
     * 注释: 如果是 从“我的”界面 进入其他activity 则mine 为true  ； isOther 为 true
     * 此时 需要用户登录后才能使用
     ************************************************************/
    public static boolean isForceExit() {
        if (isOtherDevice && tabMine.isChecked()) {
            Intent intent = new Intent(UtilActivityManager.getInstance().getCurrentActivity(), LoginActivity.class);
            UtilActivityManager.getInstance().getCurrentActivity().startActivity(intent);
            return true;
        }
        return false;
    }

}
