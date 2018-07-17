package com.lzhy.moneyhll.home.data;

import android.content.Context;
import android.os.AsyncTask;

import com.lzhy.moneyhll.home.beans.LongBean;

import java.util.List;

/**
 * Created by xu on 2016/11/10.
 *
 * 在异步类负责请求接口中的数据
 * 提供一个静态方法，将输入流中数据转换成json字符串
 */

public final class AsyncTaskRequesJsontData extends AsyncTask <String, Void, List<LongBean>> {



    private CallbackRequesJsontData mContextCallback;
    private Context mContext;

    public AsyncTaskRequesJsontData(Context context) {

        mContext = context;
        mContextCallback= (CallbackRequesJsontData) context;//绑定接口
    }
    /*
    *
    * */
    public interface CallbackRequesJsontData{

        void preExecuteRequesJsontData();

        List<LongBean> executingRequesJsontData(String url);

        void progressUpdateRequesJsontData(Integer... progresses);

        void postExecuteRequesJsontData(List<LongBean> longBeen);

    }
    @Override
    protected void onPreExecute() {//onPreExecute方法用于在执行后台任务前做一些UI操作
        super.onPreExecute();

       mContextCallback.preExecuteRequesJsontData();//执行前做准备

    }

    @Override
    protected void onPostExecute(List<LongBean> longBeanList){
        super.onPostExecute(longBeanList);
        /*
        * 接收请求结果并处理
        * */
        mContextCallback.postExecuteRequesJsontData(longBeanList);

    }
    /**
     * 从 URL 中获取数据
     */
    @Override
    protected List<LongBean> doInBackground(String... params) {

        List<LongBean> longBeanList = null;

        longBeanList = mContextCallback.executingRequesJsontData(params[0]);

        return longBeanList;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        mContextCallback.progressUpdateRequesJsontData();//执行更新界面
    }

}
