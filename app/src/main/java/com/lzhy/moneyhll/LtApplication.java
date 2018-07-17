package com.lzhy.moneyhll;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.utils.UtilActivityManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.LinkedList;
import java.util.List;

import static com.lzhy.moneyhll.constant.Constant.netStateIsOk;
import static com.lzhy.moneyhll.utils.UtilCheckMix.networkIsAvailable;
import static com.lzhy.moneyhll.utils.UtilFile.uploadFile;

public final class LtApplication extends Application {
    private static LtApplication instanceApp;
    /***********龙之游 @ xu 596928539@qq.com*******************************
     *注释: 标识用户的退出行为  false :客户端退出 true ：多端登录，强制下线
     ************************************************************/
    public static boolean isOtherDevice = false;
    public static Context mContextApp;
    private static List<Activity> activitys = null;

    public static String driveId;  //获取设备的唯一标识

    public static boolean whetherCheck = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

//        registerHomeKeyReceiver(mContextApp);
        mContextApp = getApplicationContext();
        //      实现registerActivityLifecycleCallbacks回调，
        activityLifecycleCallbacks();

        instanceApp = this;
        PlatformConfig.setWeixin(Constant.MERCHANTID_WX,"d2647d70dcfa68233dd76d1a807fead9");
        UMShareAPI.get(this);
        Config.DEBUG = true;
        Fresco.initialize(this);
        checkNetSate();
        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
        //MobclickAgent.setDebugMode( true );
        /************************************************************
         *@Author; 龙之游 @ xu 596928539@qq.com
         * 时间:2016/12/13 15:44
         * 注释: 搜集异常处理
        ************************************************************/
      /*  CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());*/

        uploadFile();
    }
    /************************************************************
     *@Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2017/1/13 12:03
     * 注释: 用于获取 栈顶 activity  //// Activity的生命周期事件进行集中处理
     ************************************************************/
    private void activityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                //监听onActivityResumed()方法
                UtilActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * xu
     * app起动时  检查网络状态
     */
    private void checkNetSate() {
        if (networkIsAvailable(this)) {
            netStateIsOk = true;
        } else {
            netStateIsOk = false;
        }
    }

    public static Context getContext() {
        return mContextApp;
    }

    /**
     * 获取ArtAPPlication 单例对象
     *
     * @return
     */
    public static LtApplication getInstance() {
        activitys = new LinkedList<Activity>();
        if (null == instanceApp) {
            instanceApp = new LtApplication();
        }
        return instanceApp;
    }
    /**
     * 获得当前版本信息
     *
     * @return
     */
    public String getVersion() {
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "V " + version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知";
        }
    }

}
