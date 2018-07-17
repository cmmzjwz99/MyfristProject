package com.lzhy.moneyhll.home.makerproject;

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
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.MakerModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.MakerListHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 创客列表
 */
public class MakerListActivity extends MySwipeBackActivity implements ItemViewListener {
    private BaseTitlebar mTitlebar;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private LongAdapter mAdapter;
    private List<MakerModel> itemdata;

    private int index = 0;
    private String pid = "1022";
    private String type = "103,104";
    private String lat;
    private String lng;
    private String scope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_maker_list);
        addActivityCST(this);
        showLoading();
        initTitlebar();
        initView();
        initAdapter();
        LoadingData(true);
    }

    private void initView() {
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");
        scope = intent.getStringExtra("scope");

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.itemlist);
        mListView = mPullRefreshListView.getRefreshableView();
        //设置点击效果
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        mPullRefreshListView.setPullLoadEnabled(true);
        mPullRefreshListView.setScrollLoadEnabled(true);
        mPullRefreshListView.setPullRefreshEnabled(true);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 0;
                LoadingData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                LoadingData(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("创客项目");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initAdapter() {
        itemdata = new ArrayList<>();
        mAdapter = new LongAdapter(MakerListActivity.this, itemdata, this);
        mListView.setAdapter(mAdapter);
    }

    private void LoadingData(final boolean isrefresh) {
        String homeMakerListUrl = UrlAPI.getPalyWhatListUrl(pid, type, index, lat, lng, scope);
        PrintLog.e("创客列表URL:" + homeMakerListUrl);
        OkHttpUtils.get().url(homeMakerListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("创客列表:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mPullRefreshListView.onRefreshComplete();
                PrintLog.e("创客列表:" + response);
                Type type = new TypeToken<Response1<ListModel<MakerModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<MakerModel>> resp = gson.fromJson(response, type);
                ListModel<MakerModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    /**
     * 更新数据
     *
     * @param isrefresh
     * @param data
     */
    private void updataView(boolean isrefresh, ListModel<MakerModel> data) {
        if (isrefresh) {
            itemdata.clear();
        }
        if (itemdata.size() == data.total && itemdata.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            mPullRefreshListView.setHasMoreData(false);
        }
        if (data.list != null && data.list.size() > 0) {
            itemdata.addAll(data.list);
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        MakerListHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(MakerListActivity.this).inflate(
                    R.layout.item_maker_list, null, false);
            helper = new MakerListHelper(MakerListActivity.this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (MakerListHelper) itemView.getTag();
        }
        final MakerModel model = (MakerModel) data;
        helper.updateView(model);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakerListActivity.this, MakerDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("id", model.id);
                extras.putInt("type", model.projecttype);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        return itemView;
    }
}
