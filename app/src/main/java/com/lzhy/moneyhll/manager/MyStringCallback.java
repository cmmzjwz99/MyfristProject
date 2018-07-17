package com.lzhy.moneyhll.manager;

import android.util.Log;

import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
/************************************************************
 *@Author; 龙之游 @ xu 596928539@qq.com
 * 时间:2017/1/13 10:17
 * 注释: 已成处理类
************************************************************/
public final class MyStringCallback extends StringCallback {
    File file;
    public MyStringCallback(File file) {
        super();
        this.file = file;
    }

    @Override
        public void onBefore(Request request, int id)
        {
            Log.i("file", "onBefore: ");
        }

        @Override
        public void onAfter(int id)
        {
            Log.i("file", "onAfter: ");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
            Log.i("file", "onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.i("file", "onResp------------------------------------------------------------onse: "+id);
            switch (id)
            {
                case 100:
                    Log.i("file", "http: ");
                    break;
                case 101:
                    Log.i("file", "https: ");
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.i("file", "inProgress: "+(int) (100 * progress));
            if (file.exists()) {
                file.delete();
            }
        }

    }