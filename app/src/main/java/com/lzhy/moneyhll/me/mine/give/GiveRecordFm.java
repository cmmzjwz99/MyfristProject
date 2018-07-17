package com.lzhy.moneyhll.me.mine.give;

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
import com.lzhy.moneyhll.model.GiveRecordModel;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.GiveRecordHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by Administrator on 2016/12/13 0013.
 * 赠送记录
 * 龙珠记录和房车券记录
 */

public class GiveRecordFm extends BaseFoundFm implements ItemViewListener {

    private LongAdapter mAdapter;
    private List<GiveRecordModel> itemdata;

    private int status;
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

        return view;
    }
    @Override
    public void onResume() {
        disparityLogin();
        super.onResume();
    }
    private void initView() {

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
        //赠送记录
        String giveRecordUrl = UrlAPI.getGiveRecordUrl(UserInfoModel.getInstance().getId(), status, index);
        PrintLog.e("赠送记录URL:" + giveRecordUrl);
        OkHttpUtils.get().url(giveRecordUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("赠送记录onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mListView.onRefreshComplete();
                PrintLog.e("赠送记录onResponse:"+status+"  " + response);
                Type type = new TypeToken<Response1<ListModel<GiveRecordModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<GiveRecordModel>> resp = gson.fromJson(response, type);
                ListModel<GiveRecordModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<GiveRecordModel> data) {
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
                dataEmpty("还没有赠送记录",0,0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        GiveRecordHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_give_record, null, false);
            helper = new GiveRecordHelper(getContext(), itemView);
            itemView.setTag(helper);
        } else {
            helper = (GiveRecordHelper) itemView.getTag();
        }
        GiveRecordModel model = (GiveRecordModel) data;
        helper.updateView(model,status);
        return itemView;
    }

    //设置可见时加载数据
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            status = getArguments().getInt("status");
            LoadingData(true);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
