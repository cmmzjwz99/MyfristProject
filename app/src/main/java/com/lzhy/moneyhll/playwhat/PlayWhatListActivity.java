package com.lzhy.moneyhll.playwhat;

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
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.PlayWhereListModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.MotorhomeListHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.DETAIL_CLICK_ID;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 高端休旅项目列表
 */

public final class PlayWhatListActivity extends MySwipeBackActivity implements ItemViewListener {
    private BaseTitlebar mTitlebar;
    private PullToRefreshListView mPullRefreshListView;
    private List<PlayWhereListModel> itemdata;
    private ListView mListView;
    private LongAdapter mAdapter;
    private int index = 0;
    private String pid = "0";
    private String name;
    private String type ;
    private String lat;
    private String lng;
    private String scope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_play_what_list);

        addActivityCST(this);
        showLoading();
        initView();
        initTitlebar();
        initAdapter();
        LoadingData(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        name = intent.getStringExtra("name");

        type = intent.getStringExtra("type");
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");
        scope=intent.getStringExtra("scope");


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

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        if (name == null) {
            mTitlebar.setTitle("游玩项目");
        } else {
            mTitlebar.setTitle(name + "服务");
        }
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initAdapter() {
        itemdata = new ArrayList<>();
        mListView.setDividerHeight(0);
        mAdapter = new LongAdapter(PlayWhatListActivity.this, itemdata, this);
        mListView.setAdapter(mAdapter);
    }

    private void LoadingData(final boolean isrefresh) {
        String palyWhatListUrl = UrlAPI.getPalyWhatListUrl(pid, type, index, lat, lng,scope);
        PrintLog.e("高端休旅服务URL:" + palyWhatListUrl);
        OkHttpUtils.get().url(palyWhatListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("高端休旅服务:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mPullRefreshListView.onRefreshComplete();
                PrintLog.e("高端休旅服务:" + response);
                Type type = new TypeToken<Response1<ListModel<PlayWhereListModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<PlayWhereListModel>> resp = gson.fromJson(response, type);
                ListModel<PlayWhereListModel> data = resp.getData();
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
    private void updataView(boolean isrefresh, ListModel<PlayWhereListModel> data) {
        if (isrefresh) {
            itemdata.clear();
        }
        if (itemdata.size() == data.total&& itemdata.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            mPullRefreshListView.setHasMoreData(false);
        }
        if (data.list != null && data.list.size() > 0) {
            itemdata.addAll(data.list);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, final Object data) {
        MotorhomeListHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(PlayWhatListActivity.this).inflate(
                    R.layout.item_play_what_list, null, false);
            helper = new MotorhomeListHelper(PlayWhatListActivity.this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (MotorhomeListHelper) itemView.getTag();
        }
        final PlayWhereListModel model = (PlayWhereListModel) data;
        helper.updateView(model);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayWhatListActivity.this, ProjcetDetailsActivity.class);
                intent.putExtra("id", model.id);
                intent.putExtra("image",model.picture2);
                intent.putExtra("name",model.projectname);
                intent.putExtra("text",model.address);
                intent.putExtra("consumerHotline", model.consumerHotline);

                /************************************************************
                 *@Author; 龙之游 @ xu 596928539@qq.com
                 * 时间:2016/12/30 12:57
                 * 注释: 玩什么项目详情页某一列表项被点击的次数
                ************************************************************/
                setCustomStatisticsKV(PlayWhatListActivity.this,DETAIL_CLICK_ID,model.projectname);
                startActivity(intent);
            }
        });
        return itemView;
    }
}
