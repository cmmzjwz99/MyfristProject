package com.lzhy.moneyhll.me.maker;

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
import com.lzhy.moneyhll.model.MarkerOrderModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.GoodsOrderFmHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by Administrator on 2016/12/13 0013.
 * 创客方商品订单
 */

public class GoodsOrderFragment extends BaseFoundFm implements ItemViewListener {

    private LongAdapter mAdapter;
    private List<MarkerOrderModel> itemdata;

    //  待支付1 ；待发货2；待收货3；已签收4；已失效5；已完结8；
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
        //创客商品订单
        String makerOrderListUrl = UrlAPI.getIImMakerOrderListUrl(UserInfoModel.getInstance().getId(), status, index, 104);
        PrintLog.e("创客商品订单URL:" + makerOrderListUrl);
        OkHttpUtils.get().url(makerOrderListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("创客订单:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mListView.onRefreshComplete();
                PrintLog.e("创客商品订单:" + response);
                Type type = new TypeToken<Response1<ListModel<MarkerOrderModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<MarkerOrderModel>> resp = gson.fromJson(response, type);
                ListModel<MarkerOrderModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<MarkerOrderModel> data) {
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
                dataEmpty("还没有创客订单", R.mipmap.no_order,0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        GoodsOrderFmHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_is_maker_order, null, false);
            helper = new GoodsOrderFmHelper(getContext(), itemView);
            itemView.setTag(helper);
        } else {
            helper = (GoodsOrderFmHelper) itemView.getTag();
        }
        final MarkerOrderModel model = (MarkerOrderModel) data;
        helper.updateViewGoods(model);
        if (model.status == 2 || model.status == 3 || model.status == 4||model.status == 8) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), GoodsOrderDetailsActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable("MakerProjectModel", model);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        } else {
            itemView.setOnClickListener(null);
        }
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
