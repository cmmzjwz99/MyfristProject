package com.lzhy.moneyhll.home.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xu on 2016/11/11.
 *
 * Restore preferences
 */

public final class SharedPreferencesURL {
    private Context mContext;
    private String mFileName;

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    public SharedPreferencesURL(Context context,String fileName) {
        mContext = context;
        mFileName = fileName;
        /*
        * 只能被本应用读取
       */
        sharedPreferences = mContext.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        clearAll();//
    }

    public void saveURL(String imgUrlKey,String imgUrlValue){

        editor.putString(imgUrlKey,imgUrlValue);

    }

    /**
     * @param imgUrlKey
     * @param length
     *
     */
    public void saveInt(String imgUrlKey,int length){

        editor.putInt(imgUrlKey,length);

    }

    public void commitURL(){

        editor.commit();
    }

    public String getURL(String keyName){
        if (sharedPreferences.contains(keyName)) {
            return sharedPreferences.getString(keyName,"qqqqq");
        }else {
            return null;
        }

    }
    public int getInt(String keyName){
        return sharedPreferences.getInt(keyName,0);
    }
    public void clearAll(){

        editor.clear();//清楚所有数据
    }

}
