package com.lzhy.moneyhll.playwhat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseFragment;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.home.makerproject.MakerListActivity;
import com.lzhy.moneyhll.model.PlayWhereModel;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.HomeCategoryGridHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.PLAY_CLICK_ID;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * 玩什么
 */

public final class PlayWhatFm extends BaseFragment implements ItemViewListener {
    private Context mContext;
    private BaseTitlebar mTitlebar;
    private NoScorllGridView mGridView;
    private int type = 101;
    private List<PlayWhereModel> mList = new ArrayList<>();
    private LongAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_what, null);
        showLoading();
        initView();
        initTitlebar();
        initAdapter();
        LoadingData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        disparityLogin();
    }
    //初始化头
    private void initView() {
        mContext = getContext();
        mGridView = (NoScorllGridView) view.findViewById(R.id.gv_play_what);
    }

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) view.findViewById(R.id.title_bar);
        mTitlebar.setTitle("玩什么");
    }

    private void initAdapter() {

        mAdapter = new LongAdapter(mContext, mList, this);
        mGridView.setAdapter(mAdapter);
    }

    private void LoadingData() {
        String palyWhatUrl = UrlAPI.getPalyWhatUrl(type);
        PrintLog.e("玩什么URL:" + palyWhatUrl);
        OkHttpUtils.get()
                .url(palyWhatUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("玩什么:" + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoading();
                        PrintLog.e("玩什么:" + response);
                        Type type = new TypeToken<Response<PlayWhereModel>>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response<PlayWhereModel> resp = gson.fromJson(response, type);
                        List<PlayWhereModel> data = resp.getData();
                        if (data != null) {
                            mList.clear();
                            mList.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            hideLoading();
						} else {
                            dataEmpty("暂无游玩项目",0,0);
                        }
                    }
                });
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        HomeCategoryGridHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_play_what_grid, null, false);
            helper = new HomeCategoryGridHelper(mContext, itemView);
            itemView.setTag(helper);
        } else {
            helper = (HomeCategoryGridHelper) itemView.getTag();
        }
        final PlayWhereModel modle = (PlayWhereModel) data;
        helper.updateView(modle);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if ("1022".equals(modle.id + "")) {
                    intent.setClass(mContext, MakerListActivity.class);
                } else {
                    intent.setClass(mContext, PlayWhatListActivity.class);
                    intent.putExtra("pid", modle.id + "");
                    intent.putExtra("name", modle.name);
                }

                    /************************************************************
                     *@Author; 龙之游 @ xu 596928539@qq.com
                     * 时间:2016/12/30 12:48
                     * 注释: 玩什么项目被点击的次数
                    ************************************************************/
                    setCustomStatisticsKV(mContext,PLAY_CLICK_ID,modle.name);
                startActivity(intent);
                }

        });
        return itemView;
    }
}
