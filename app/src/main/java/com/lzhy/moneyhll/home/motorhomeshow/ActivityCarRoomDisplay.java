package com.lzhy.moneyhll.home.motorhomeshow;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.model.CarBrandModel;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.CarBrandHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 房车品牌展示
 */
public class ActivityCarRoomDisplay extends MySwipeBackActivity implements ItemViewListener {
    private BaseTitlebar mTitlebar;
    private PullToRefreshListView mPullRefreshListView;
    private List<CarBrandModel> mList;
    private LongAdapter mAdapter;
    protected ListView mListView;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_room_display);

        addActivityCST(this);
        initView();
        showLoading();
        initTitlebar();
        initAdapter();
        LoadingData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

    public void initView() {
        mList = new ArrayList<>();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_go_where);
        mListView = mPullRefreshListView.getRefreshableView();
        //设置点击效果
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        //下拉加载
        mPullRefreshListView.setPullLoadEnabled(true);
        //下拉刷新
        mPullRefreshListView.setPullRefreshEnabled(true);
        mPullRefreshListView.setScrollLoadEnabled(true);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                LoadingData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                LoadingData(false);
            }
        });
    }

    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("房车品牌");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initAdapter() {
        mListView.setDividerHeight(0);
        mAdapter = new LongAdapter(ActivityCarRoomDisplay.this, mList, this);
        mListView.setAdapter(mAdapter);
    }

    private void LoadingData(final boolean isrefresh) {
        String displayUrl = UrlAPI.getCarRoomDisplayUrl(index);
        PrintLog.e("房车品牌URL:" + displayUrl);
        OkHttpUtils.get().url(displayUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("房车品牌:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mPullRefreshListView.onRefreshComplete();
                PrintLog.e("房车品牌:" + response);
                Type type = new TypeToken<Response1<ListModel<CarBrandModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<CarBrandModel>> resp = gson.fromJson(response, type);
                ListModel<CarBrandModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<CarBrandModel> data) {
        if (isrefresh) {
            mList.clear();
        }
        if (mList.size() == data.total && mList.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            mPullRefreshListView.setHasMoreData(false);
        }

        if (data.items != null && data.items.size() > 0) {
            mList.addAll(data.items);
            hideLoading();
        } else {
            if (mList.size() == 0) {
                dataEmpty("暂无房车展示", 0, 0);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        CarBrandHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(ActivityCarRoomDisplay.this).inflate(
                    R.layout.item_car_room_display, null, false);
            helper = new CarBrandHelper(ActivityCarRoomDisplay.this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (CarBrandHelper) itemView.getTag();
        }
        final CarBrandModel model = (CarBrandModel) data;
        helper.updateView(model);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCarRoomDisplay.this, MotorhomeShowActivity.class);
                intent.putExtra("id", model.id);
                startActivity(intent);
            }
        });
        return itemView;
    }
}
