package com.lzhy.moneyhll.me.maker;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.wallet.TixianActivity;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.MyMakerProjectModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;
import com.lzhy.moneyhll.viewhelper.MyMakerProjectHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by lzy on 2016/12/12.
 * 创客方我的项目
 */
public class MyProjectActivity extends MySwipeBackActivity implements ItemViewListener, View.OnClickListener {

    private PullToRefreshListView mPullRefreshListView;
    private List<MyMakerProjectModel> mList;
    private LongAdapter mAdapter;
    private ListView mListView;
    private int index = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project);

        addActivityCST(this);
        showLoading();
        initTitlebar();
        initView();
        initAdapter();
        LoadingData(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {

        TextView tvTX = (TextView) findViewById(R.id.tixian);
        tvTX.setOnClickListener(this);

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

    private void LoadingData(final boolean isrefresh) {
        String myProjectListUrl = UrlAPI.getMyProjectListUrl(index, UserInfoModel.getInstance().getId());
        PrintLog.e("我的项目URL:" + myProjectListUrl);
        OkHttpUtils.get().url(myProjectListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("我的项目:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                mPullRefreshListView.onRefreshComplete();
                hideLoading();
                PrintLog.e("我的项目:" + response);
                Type type = new TypeToken<Response1<ListModel<MyMakerProjectModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<MyMakerProjectModel>> resp = gson.fromJson(response, type);
                ListModel<MyMakerProjectModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<MyMakerProjectModel> data) {
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
                dataEmpty("暂无创客项目",0,0);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    private void initAdapter() {
        mList = new ArrayList<>();
        mAdapter = new LongAdapter(MyProjectActivity.this, mList, this);
        mListView.setAdapter(mAdapter);
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("我的项目");
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        MyMakerProjectHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(MyProjectActivity.this).inflate(
                    R.layout.item_my_maker_project, null, false);
            helper = new MyMakerProjectHelper(MyProjectActivity.this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (MyMakerProjectHelper) itemView.getTag();
        }
        final MyMakerProjectModel model = (MyMakerProjectModel) data;
        helper.updateView(model);
        return itemView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //提现
            case R.id.tixian:
                CommonUtil.setViewAlphaAnimation(view);
                SharePrefenceUtils.put(this, "withDrawType", "2");
                startActivity(new Intent(this, TixianActivity.class));
                break;
        }
    }
}
