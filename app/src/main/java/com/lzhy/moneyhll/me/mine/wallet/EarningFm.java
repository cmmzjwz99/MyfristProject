package com.lzhy.moneyhll.me.mine.wallet;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseFoundFm;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.StickyListView;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.model.EarningModel;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.EarningHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/1/10 0010.
 * 明细查看
 */

public class EarningFm extends BaseFoundFm implements ItemViewListener {

    private LongAdapter mAdapter;
    private List<EarningModel> itemdata;

    private String payType;
    private int index = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        showLoading();
        mListView.setPullLoadEnabled(true);
        mListView.setScrollLoadEnabled(true);
        mListView.setPullRefreshEnabled(true);

        initView();
        initAdapter();
        LoadingData(true);
        return view;
    }

    private void initView() {
        payType = getArguments().getString("payType");
        PullToRefreshBase.OnRefreshListener<StickyListView> listener = new PullToRefreshBase.OnRefreshListener<StickyListView>() {
            public void onPullDownToRefresh(
                    final PullToRefreshBase<StickyListView> refreshView) {
                index = 1;
                LoadingData(true);
            }

            public void onPullUpToRefresh(final PullToRefreshBase<StickyListView> refreshView) {
                index++;
                LoadingData(false);
            }
        };
        mListView.setOnRefreshListener(listener);
        listView.setDivider(new ColorDrawable(0xffffffff));
    }

    private void initAdapter() {
        itemdata = new ArrayList<>();
        mAdapter = new LongAdapter(getActivity(), itemdata, this);
        listView.setAdapter(mAdapter);
    }

    private void LoadingData(final boolean isrefresh) {

        //收支明细记录
        String myEarningUrl = UrlAPI.getMyEarningUrl(UserInfoModel.getInstance().getId(), index, payType);
        PrintLog.e("收支明细URL:" + myEarningUrl);
        OkHttpUtils.get().url(myEarningUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("收支明细onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mListView.onRefreshComplete();
                PrintLog.e("收支明细onResponse:" + response);
                Type type = new TypeToken<Response1<ListModel<EarningModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<EarningModel>> resp = gson.fromJson(response, type);
                ListModel<EarningModel> data = resp.getData();
                if (data!=null)
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<EarningModel> data) {
        if (isrefresh) {
            itemdata.clear();
        }

        if (itemdata.size() == data.total && itemdata.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            mListView.setHasMoreData(false);
        }
        if (data.items != null && data.items.size() > 0) {
            itemdata.addAll(data.items);
            hideLoading();
        } else {
            if (itemdata.size() == 0) {
                if ("pears".equals(payType)) {
                    dataEmpty(null, R.mipmap.no_dragon_ball, R.mipmap.no_earning_bot);
                } else {
                    dataEmpty(null, R.mipmap.no_dragon_coin, R.mipmap.no_earning_bot);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        EarningHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_earning, null, false);
            helper = new EarningHelper(getContext(), itemView);
            itemView.setTag(helper);
        } else {
            helper = (EarningHelper) itemView.getTag();
        }
        EarningModel model = (EarningModel) data;
        helper.updateView(model, payType);
        return itemView;
    }
}

