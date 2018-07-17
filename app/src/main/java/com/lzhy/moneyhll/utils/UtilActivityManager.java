package com.lzhy.moneyhll.utils;

import android.app.Activity;

import java.lang.ref.WeakReference;

/************************************************************
 *@Author; 龙之游 @ xu 596928539@qq.com
 * 时间:2016/12/20 15:59
 * 注释:
************************************************************/

public final class UtilActivityManager {

    private static UtilActivityManager sInstance = new UtilActivityManager();
	  private WeakReference<Activity> sCurrentActivityWeakRef;
	 
	 
	  private UtilActivityManager() {
	 
	  }
	 
	  public static UtilActivityManager getInstance() {
	    return sInstance;
	  }
	 
	  public Activity getCurrentActivity() {
	    Activity currentActivity = null;
	    if (sCurrentActivityWeakRef != null) {
	      currentActivity = sCurrentActivityWeakRef.get();
	    }
	    return currentActivity;
	  }
	 
	  public void setCurrentActivity(Activity activity) {
	    sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
	  }
}