package com.lzhy.moneyhll.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/10/22 0022.
 */

public final class Constant {
    /************************************************************
     *创建者;龙之游 @ xu 596928539@qq.com
     *修改时间:2017/1/6 10:50
     *注释: 友盟统计 事件ID
     ************************************************************/
    public static final String MAP_CLICK_ID         = "map_clicked";
    public static final String SHARE_CLICK_ID       = "share_clicked";
    public static final String PHONE_CLICK_ID       = "phone_clicked";
    public static final String LOGIN_CLICK_ID       = "login_clicked";
    public static final String DETAIL_CLICK_ID      = "play_what_detail";
    public static final String PLAY_CLICK_ID        = "play_what";
    public static final String PAY_ALI_ID            = "pay_alipay";
    public static final String PAY_WX_ID             = "pay_weixin";
    public static final String PAY_ALL_DRAGON_ID    = "all_dragon_ball";
    public static final String MOTORHOME_ID          = "motorhome";
    public static final String BAIDU_ID               = "map_baidu";
    public static final String GAODE_ID               = "map_gaode";
    public static final String LOGOUT_CLICK_ID       = "logout";
    public static final String DRAGON_STORE_CLICK_ID = "Dragon_store_clicked";
    public static final String REGISTER_CLICK_ID     = "register";
    public static final String SACN_CLICK_ID          = "scanTicke";
    public static final String PRESENT_CLICK_ID       = "present";
    /************************************************************
     *@Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/27 17:11
     * 注释:  从  绑定房车券进入ScanActivity时  ，需要传入改值
     ************************************************************/
    public static final int Fangchequan = 9;
    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/19 20:19
     * 注释: js脚本 正则表达式
     ************************************************************/
    public static final String regexStr = "<script\\b[^>]*?src=\"([^\"]*?)\"[^>]*></script>";
    /************************************************************
     * 创建者;龙之游 @ xu 596928539@qq.com
     * 修改时间:2016/12/18 17:48
     * 注释:订单状态  待发货 4  代签收18  已签收20 已完结8 退换货16
     * ****商品订单状态码
     ************************************************************/
    public static final int STATUS_PAY_SUCCESS = 1;//支付成功
    public static final int STATUS_WAIT_PAY = 2;//待支付
    public static final int STATUS_WAIT_DELIVER = 4;//待发货
    public static final int STATUS_DELIVERED = 6;//已发货
    public static final int STATUS_BE_OVER = 8;//已完结
    public static final int STATUS_TERMINATED = 10;//已终止
    public static final int STATUS_DRAWBACK = 14;// 已退款
    public static final int STATUS_BARTER = 16;//待退货
    public static final int STATUS_WAIT_SIGN = 18;//待签收
    public static final int STATUS_SIGNED = 20;//已签收
    public static final int STATUS_CANCLE = 30;//已取消
    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/10/28 18:46
     * 注释:  网络状态
     ************************************************************/
    public static boolean netStateIsOk;

    public static final boolean isDebug = true;

    public static final int REQUEST_CODE = 0X101;
    public static final int RESULT_CODE = 0X102;

    public static final int CHOOSE_PICTURE = 0;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_SMALL_PICTURE = 2;

    public static final String URL_CRASH = "http://ssl.app.lzyhll.com:8080/api/logger/upload"; //crash 日

    //更新
    public static final String UPDATA_VERSION = "https://app.lzyhll.com/api/configure/getversion";

    public static final String DEVICE = "Android";

    /**
     * 获取应用程序的根目录
     */
    public static final String LZhyPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LZhy" + File.separator + "LZYhll" + File.separator;
    /**
     * 获取应用程序的缓存目录
     */
    public static final String Diskcache = LZhyPath + "diskcache";

    /**
     * 用于判断是否需要登录，使用这个值得时候表示需要登录
     */
    public static final String need_login = "need_login";

    public static final String MD5_KEY = "lzhyapp.hll.html";

    //    支付code
    public static final String GATEWAY_ALIPAY = "100400";
    public static final String GATEWAY_WX = "100500";

    //    微信
    public static final String MERCHANTID_ALIPAY = "15120571300000";
    public static final String MERCHANTID_WX = "wx7de8b9ab1b056006";

    /**
     * 权限
     */
    public static final int READ_PHONE_STATE = 0X100;
    public static final int LOCATION_CODE = 0X101;
    public static final int CALL_PHONE_CODE = 0X102;
    public static final int CAMERA_CODE = 0X103;
    public static final int STORAGE_CODE = 0X104;

    //客服电话
    public static final String PHONE_KEFU = "tel:400_0571_893";


}
