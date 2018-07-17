package com.lzhy.moneyhll.me.order;

import android.content.Intent;
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
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.ProjectOrderModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.OrderHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by Administrator on 2016/11/12 0012.
 * 项目订单列表
 */

public class ProjectOrderListFm extends BaseFoundFm implements ItemViewListener {
    private int status;

    private List<ProjectOrderModel> itemdata;
    private LongAdapter mAdapter;

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
        super.onResume();
        disparityLogin();
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
        //项目订单
        String projectOrderListUrl = UrlAPI.getProjectOrderListUrl(UserInfoModel.getInstance().getId(), status, index);
        PrintLog.e("项目订单URL:" + projectOrderListUrl);
        OkHttpUtils.get().url(projectOrderListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("项目订单:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mListView.onRefreshComplete();
                PrintLog.e("项目订单:" + response);
                Type type = new TypeToken<Response1<ListModel<ProjectOrderModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<ProjectOrderModel>> resp = gson.fromJson(response, type);
                ListModel<ProjectOrderModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<ProjectOrderModel> data) {
        if (isrefresh) {
            itemdata.clear();
        }
        if (itemdata.size() == data.total&& itemdata.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            mListView.setHasMoreData(false);
        }
        if (data.items != null && data.items.size() > 0) {
            itemdata.addAll(data.items);
        } else {
            if (itemdata.size() == 0) {
                dataEmpty("还没有项目订单", R.mipmap.no_order,0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        OrderHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_already_used_list, null, false);
            helper = new OrderHelper(getContext(), itemView);
            itemView.setTag(helper);
        } else {
            helper = (OrderHelper) itemView.getTag();
        }
        final ProjectOrderModel model = (ProjectOrderModel) data;
        helper.updateView(model);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putSerializable("ProjectOrderModel", model);
                if (model.status == 2) {
                    Intent intent = new Intent(getContext(), NotUseOrderDetailsActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else if (model.status == 3) {
                    Intent intent = new Intent(getContext(), AlreadyUsedActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
                //Toast.makeText(getContext(),"项目订单详情",Toast.LENGTH_SHORT).show();
            }
        });
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
