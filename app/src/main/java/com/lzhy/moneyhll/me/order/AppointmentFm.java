package com.lzhy.moneyhll.me.order;

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
import com.lzhy.moneyhll.model.AppointmentModel;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.AppointmentHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by Administrator on 2016/11/15 0015.
 * 我的预约
 */

public class
AppointmentFm extends BaseFoundFm implements ItemViewListener {
    private LongAdapter mAdapter;
    private List<AppointmentModel> itemdata;

    private int status;
    private int index = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        showLoading();
        initView();
        initAdapter();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        mListView.setPullLoadEnabled(true);
        mListView.setScrollLoadEnabled(true);
        mListView.setPullRefreshEnabled(true);
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
        //我的预约
        String appointmentListUrl = UrlAPI.getAppointmentListUrl(UserInfoModel.getInstance().getId(), status, index);
        PrintLog.e("我的预约URL:" + appointmentListUrl);
        OkHttpUtils.get().url(appointmentListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("我的预约:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mListView.onRefreshComplete();
                PrintLog.e("我的预约:" + response);
                Type type = new TypeToken<Response1<ListModel<AppointmentModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<AppointmentModel>> resp = gson.fromJson(response, type);
                ListModel<AppointmentModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<AppointmentModel> data) {
        if (isrefresh) {
            itemdata.clear();
        }
        if (itemdata.size() == data.total&&itemdata.size()!=0) {
            index = index > 1 ? index - 1 : 1;
            mListView.setHasMoreData(false);
        }
        if (data.items != null && data.items.size() > 0) {
            itemdata.addAll(data.items);
        } else {
            if (itemdata.size() == 0) {
                dataEmpty("还没有预约", R.mipmap.no_order,0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View getView(final int id, View itemView, ViewGroup vg, final Object data) {
        AppointmentHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_appointment, null, false);
            helper = new AppointmentHelper(getContext(), itemView, status);
            itemView.setTag(helper);
        } else {
            helper = (AppointmentHelper) itemView.getTag();
        }
        final AppointmentModel model = (AppointmentModel) data;
        helper.updateView(model);
//        final AppointmentHelper finalHelper = helper;
//        helper.setOnUpdate(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (finalHelper.type1 == 1) {
//                    itemdata.remove(id);
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//        });
        return itemView;
    }

    //设置可见时加载数据
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            status = getArguments().getInt("status");
            LoadingData(true);
//            PrintLog.log_d("setUserVisibleHint 第   " + type + "  页订单  ");
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
