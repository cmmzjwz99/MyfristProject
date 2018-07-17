package com.lzhy.moneyhll.utils;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.launcher.SplashActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 获得屏幕相关的辅助类
 * 
 */
public final class ScreenUtils{
	private ScreenUtils(){
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	/**
     * 返回添加到桌面快捷方式的Intent：   
     * 1.给Intent指定action="com.android.launcher.INSTALL_SHORTCUT" 
     * 2.给定义为Intent.EXTRA_SHORTCUT_INENT的Intent设置与安装时一致的action(必须要有)
     * 3.添加权限:com.android.launcher.permission.INSTALL_SHORTCUT
    */
    public static Intent getShortcutToDesktopIntent(Context context) {
        Intent intent = new Intent();   
        intent.setClass(context, SplashActivity.class);
       /*以下两句是为了在卸载应用的时候同时删除桌面快捷方式*/  
        intent.setAction("android.intent.action.MAIN");    
        intent.addCategory("android.intent.category.LAUNCHER");    
         
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");  
        // 不允许重建  
        shortcut.putExtra("duplicate", false);  
        // 设置名字  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,context.getString(R.string.app_name));  
        // 设置图标  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
        // 设置意图和快捷方式关联程序  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);
        
        return shortcut;
    } 
    /** 
     * 删除快捷方式 
     * */  
    public static void deleteShortCut(Context context){
       Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");    
       //快捷方式的名称  
       shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,context.getString(R.string.app_name));
       /**删除和创建需要对应才能找到快捷方式并成功删除**/
       Intent intent = new Intent();
       intent.setClass(context, context.getClass());
       intent.setAction("android.intent.action.MAIN");
       intent.addCategory("android.intent.category.LAUNCHER");
       shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);    
       context.sendBroadcast(shortcut);            
    } 
    /** 
     * 判断是否已添加快捷方式：   
     * 暂时没有方法能够准确的判断到快捷方式，原因是， 
       1、不同厂商的机型他的快捷方式uri不同，我遇到过HTC的他的URI是content://com.htc.launcher.settings/favorites?notify=true 
       2、桌面不只是android自带的，可能是第三方的桌面，他们的快捷方式 uri 都不同 提供一个解决办法，创建快捷方式的时候保存到preference，或者建个文件在SD卡上，下次加载的时候判断不存在就先发删除广播，再重新创建 
     * 添加权限:<uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
     */  
    public static boolean hasInstallShortcut(Context context) {  
        boolean hasInstall = false;
        String AUTHORITY = "com.android.launcher.settings";  
        int systemversion = Build.VERSION.SDK_INT;  
//        Log.i("Build.VERSION.SDK==========>", systemversion + "");
        /*大于8的时候在com.android.launcher2.settings 里查询（未测试）*/  
        if(systemversion >= 8){   
            AUTHORITY = "com.android.launcher2.settings";   
        }   
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY  + "/favorites?notify=true");
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[] { "title" }, "title=?",new String[] { context.getString(R.string.app_name) }, null);
        if (cursor != null && cursor.getCount() > 0) {  
            hasInstall = true;  
        } 
        return hasInstall;  
    }
	
	
	
	
	
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }
	/**
	 * 获得屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context){
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context){
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context){

		int statusHeight = -1;
		try
		{
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity){
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity){
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;
	}
	/**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * @param context
     * @param pxValue
     * @return
     */
     public static int px2dip(Context context, float pxValue) {
             final float scale = context.getResources().getDisplayMetrics().density;  
             return (int) (pxValue / scale + 0.5f);
     }
     /**
      * 将dip或dp值转换为px值，保证尺寸大小不变
      * @param dipValue
      * @param scale
      * @return
      */
     public static int dip2px(Context context, float dipValue) {
             final float scale = context.getResources().getDisplayMetrics().density;  
             return (int) (dipValue * scale + 0.5f);
     }
     /**
      * 将px值转换为sp值，保证文字大小不变
      * @param pxValue
      * @param
      * @return
      */
     public static int px2sp(Context context, float pxValue) {
             final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
             return (int) (pxValue / fontScale + 0.5f);
     }
     /**
      * 将px值转换为sp值，保证文字大小不变
      * @param spValue
      * @param
      * @return
      */
     public static int sp2px(Context context, float spValue) {
             final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
             return (int) (spValue * fontScale + 0.5f);
     }

}
