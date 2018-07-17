package smart.xu.update_download.Constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by xu on 2016/11/29.
 */

public class Constant {

    /************************************************************
     *@Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/18 18:50
     * 注释:  应用缓存目录
     ************************************************************/
    public static final String DownloadCachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            +  "Download/";
    public static final String DownloadCachePath21 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + "Download/";
}
