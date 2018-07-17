package com.lzhy.moneyhll.me.mine.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.HistoryInfo;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.lzhy.moneyhll.viewhelper.HistoryHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by cmm on 2016/11/7.
 */
public class HistoryFragment extends android.app.Fragment implements ItemViewListener {
    private PullToRefreshListView mPullRefreshListView;
    private List<HistoryInfo> mList;
    private LongAdapter mAdapter;
    private Context mContext;
    protected ListView mListView;
    private int index = 0;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, null);
        initView(view);
        initAdapter();

        initHistoryRoll(true);
        return view;
    }


    private void initView(View view) {
        mContext = getActivity();
        mList = new ArrayList<>();
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_history);
        mListView = mPullRefreshListView.getRefreshableView();
        //设置点击效果
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        //下拉加载
        mPullRefreshListView.setPullLoadEnabled(true);
        //下拉刷新
        mPullRefreshListView.setPullRefreshEnabled(true);
//        mPullRefreshListView.setScrollLoadEnabled(true);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 0;
                initHistoryRoll(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                initHistoryRoll(false);
            }
        });
    }


    public void initHistoryRoll( boolean isrefresh){

        String getMyMotorHomeRollUrl = UrlAPI.getMyMotorHomeRollUrl(UserInfoModel.getInstance().getId(),2);
        PrintLog.e("下一步URL:" + getMyMotorHomeRollUrl);
        OkHttpUtils.get().url(getMyMotorHomeRollUrl).build().execute(new StringCallback() {

            private List<HistoryInfo> data;

            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("下一步URL:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("下一步URL:" + response);
                mPullRefreshListView.onRefreshComplete();
                Type type = new TypeToken<Response<HistoryInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Response<HistoryInfo> resp = gson.fromJson(response, type);
                data = resp.getData();

                PrintLog.e("当前URL------data---------:" + data);
                if (data != null){
                    mList.clear();
                    mList.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void initAdapter(){
        mAdapter=new LongAdapter(mContext,mList,this);
        mListView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetInvalidated();
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        HistoryHelper helper;
        if (itemView == null) {

            itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_history, null, false);
            helper = new HistoryHelper(mContext, itemView);
            itemView.setTag(helper);
        } else {
            helper = (HistoryHelper) itemView.getTag();
        }
        helper.updateView((HistoryInfo) data);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                beforeClickInMine();
                Utils.toast(mContext,"已使用");
            }
        });
        return itemView;
    }
}
