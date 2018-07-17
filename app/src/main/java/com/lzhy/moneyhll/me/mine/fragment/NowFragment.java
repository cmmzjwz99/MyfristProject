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
import com.lzhy.moneyhll.viewhelper.NowRollHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by cmm on 2016/11/7.
 */
public class NowFragment extends android.app.Fragment implements ItemViewListener{
    private PullToRefreshListView mPullRefreshListView;
    private List<HistoryInfo> mList;
    private LongAdapter mAdapter;
    private Context mContext;
    protected ListView mListView;
    private View view;
    private int index = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_now, null);
        initView(view);
        initAdapter();
        initNowRoll(true);
        return view;
    }

    private void initView(View view) {
        mContext = getActivity();
        mList = new ArrayList<>();
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_now);
        mListView = mPullRefreshListView.getRefreshableView();
        //设置点击效果
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        //下拉加载
        mPullRefreshListView.setPullLoadEnabled(true);
        //下拉刷新
        mPullRefreshListView.setPullRefreshEnabled(true);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 0;
                initNowRoll(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                initNowRoll(false);
            }
        });

    }


    public void initNowRoll(final boolean isrefresh){
        String getMyMotorHomeRollUrl = UrlAPI.getMyMotorHomeRollUrl(UserInfoModel.getInstance().getId(),1);
        PrintLog.e("当前URL:" + getMyMotorHomeRollUrl);
        OkHttpUtils.get().url(getMyMotorHomeRollUrl).build().execute(new StringCallback() {

            private List<HistoryInfo> data;

            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("下一步URL:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("当前URL---------------:" + response);
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
//                updataView(isrefresh, data);
            }
        });
    }

//    private void updataView(boolean isrefresh, UserList list) {
//        if (isrefresh) {
//            mList.clear();
//        }
//        if (mList.size() == list.total) {
//            index = index > 1 ? index - 1 : 1;
//            lvEarning.setHasMoreData(false);
//        }
//        if (list.items != null && list.items.size() > 0) {
//            mList.addAll(list.items);
//        }
//        mAdapter.notifyDataSetChanged();
//    }



    private void initAdapter(){
        mAdapter=new LongAdapter(mContext,mList,this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        NowRollHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_now, null, false);
            helper = new NowRollHelper(mContext, itemView);
            itemView.setTag(helper);
            PrintLog.e("itemView------data---------:" + itemView);
        } else {
            helper = (NowRollHelper) itemView.getTag();
        }
        helper.updateView((HistoryInfo) data);//List<RollrBean> data
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                beforeClickInMine();
                Utils.toast(mContext,"未使用");
            }
        });
        return itemView;//知道了

    }

}
