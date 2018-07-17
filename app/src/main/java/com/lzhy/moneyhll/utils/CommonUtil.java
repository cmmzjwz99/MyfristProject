package com.lzhy.moneyhll.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.widget.Toast;

import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.receiver.HomeWatcherReceiver;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lzhy.moneyhll.constant.Constant.PHONE_CLICK_ID;

/**
 * 基础类
 */
public final class CommonUtil {

    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    public static void registerHomeKeyReceiver(Context context) {
        Log.i("HomeReceiver", "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    public static void unregisterHomeKeyReceiver(Context context) {
        Log.i("HomeReceiver", "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2017/1/9 18:48
     * 注释: 标题栏的标题和左侧按钮的点击事件
     ************************************************************/

    public static void setTitleBarLeftBtn(BaseTitlebar titlebar, String title) {
        titlebar.setTitle(title);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (!isForceExit()) {
                    UtilActivityManager.getInstance().getCurrentActivity().finish();
                }*/
                UtilActivityManager.getInstance().getCurrentActivity().finish();
            }
        });
    }

    private final static int eventNum = 2147483647;


    /**
     * 设置高度为9/16
     */

    public static void setViewHeight(Context context, View view) {
        ViewGroup.LayoutParams mLayoutParams = view
                .getLayoutParams();
        mLayoutParams.height = getScreenWidthPixels((Activity) context) * 9 / 16;
        view.setLayoutParams(mLayoutParams);
    }

    /**
     * 获取当前activity屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getScreenWidthPixels(Activity activity) {
        if (activity == null) {
            return 0;
        }

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * dp转pix
     *
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * @param url
     * @return RequestCall
     */

    public static RequestCall getBuilder(String url) {
        return OkHttpUtils.get()
                .url(url)
                .build()
                .connTimeOut(4000)//4s内未连接  则链接超时
                ;
    }

    /**
     * md5加密排序
     *
     * @param params
     * @return
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i), value = params.get(key);

            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }

            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 采用四舍五入的方式，取两位小数
     */
    public static double setDouble(double allPri) {
        BigDecimal bg = new BigDecimal(allPri);
        double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

    /**
     * 点击动画效果
     *
     * @param view 的透明度变化
     */
    public static void setViewAlphaAnimation(View view) {
        AlphaAnimation alphaAni = new AlphaAnimation(0.05f, 1.0f);
        alphaAni.setDuration(100);                // 设置动画效果时间
        view.startAnimation(alphaAni);        // 添加光效动画到VIew
    }


    public static String getTime() {
        Date dt = new Date();
        Long time = dt.getTime();
        String str = String.valueOf(time);
        return str;
    }


    /**
     * 拨打电话
     *
     * @param mContext
     * @param phoneurl
     */
    public static void doCallPhone(final Context mContext, final String phoneurl) {
//        String replace = phoneurl.replace("-","_");
        PrintLog.e("1111111111" + phoneurl);
        if (phoneurl.length() < 4) {
            Toast.makeText(mContext, "暂无电话信息", Toast.LENGTH_SHORT).show();
            return;
        }
        /************************************************************
         *@Author; 龙之游 @ xu 596928539@qq.com
         * 时间:2016/12/30 13:03
         * 注释: 拨打电话被点击的次数
         ************************************************************/
//        MobclickAgent.onEvent(mContext, "phone_click");
        setCustomStatisticsKV(mContext, PHONE_CLICK_ID, "拨打电话");//统计
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneurl));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /************************************************************
     * @param mContext
     * @param event_id
     * @param infomation
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/30 13:09
     ************************************************************/

    public static void setCustomStatisticsKV(@NonNull Context mContext, @NonNull String event_id, @NonNull String infomation) {

        //多属性事件：统计点击行为各属性被触发的次数
        Map<String, String> mapKeyValue = new HashMap<String, String>();//多值统计 添加你想要统计的信息
        mapKeyValue.put("name", infomation);//项目名称

        //登录的时候如果要传设备信息  把设备信息封装到 map里面
        //
        mapKeyValue.put("设备平台:", "Android");//设备平台
//        mapKeyValue.put("用户手机号:",UserInfoModel.getInstance().getAccount());//设备平台
//        mapKeyValue.put("用户名字:",UserInfoModel.getInstance().getName());//设备平台
//        mapKeyValue.put("用户类型:；",String.valueOf(UserInfoModel.getInstance().getServiceStatus()));//设备平台
//        mapKeyValue.put("手机品牌:",UtilPhone.getInstance((Activity) mContext).getBrand());//手机品牌
//        mapKeyValue.put("手机型号:",UtilPhone.getInstance((Activity) mContext).getModel());//手机型号
        if (Build.VERSION.SDK_INT > 22) {
            //如果您的SDK是>=5.2.2版本，调用以下方法集成数值型事件
            MobclickAgent.onEventValue(mContext, event_id, mapKeyValue, eventNum);
        } else {
            //如果您的SDK是5.2.2版本以前，调用以下方法集成数值型自定义事件
            MobclickAgent.onEvent(mContext, event_id, mapKeyValue);

        }
    }

    public static String FloattoInt(double mfloat) {
        mfloat = Math.floor(mfloat);
        DecimalFormat d = new DecimalFormat("0");
        String mString = d.format(mfloat);
        return mString;
    }
}
