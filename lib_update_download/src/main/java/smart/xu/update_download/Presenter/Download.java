package smart.xu.update_download.Presenter;

import android.content.Context;

import smart.xu.update_download.DownloadeManager;

/**
 * Created by xu on 2016/11/26.
 */

public class Download {

    public static void dowaload(Context context,String apkName ,String downloadUrl){
        DownloadeManager downloadeManager = new DownloadeManager(context, apkName, downloadUrl);
    }
}
