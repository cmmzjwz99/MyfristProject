package com.lzhy.moneyhll.motorhome;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseFoundFm;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.gowhere.WherePlaySelectProAdapter;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.MotorhomeRentModel;
import com.lzhy.moneyhll.model.ProvinceModel;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.BuyStockHelper;
import com.lzhy.moneyhll.viewhelper.MotorhomeHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.MOTORHOME_ID;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 租房车
 */
public final class MotorhomeFm extends BaseFoundFm implements ItemViewListener {
    private BaseTitlebar mTitlebar;

    private PullToRefreshListView mPullRefreshListView;
    private List<MotorhomeRentModel> mList;
    private LongAdapter mAdapter;
    private Context mContext;
    protected ListView mListView;
    private TextView btnSelectPro;
    private PopupWindow mWhere;
    private Drawable drawableUp, drawableDown;
    private View mLine;
    private View popView;
    private String pro = "";
    private int index = 1;
    private List<String> cities = new ArrayList<>();
    private String[] provinces;

    private WherePlaySelectProAdapter adapter;
    private NoScorllGridView gridView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_motorhome_main, null);
        showLoading();
        initView(view);
        initTitlebar(view);
        initAdapter();
        LoadingProvinceData();
        LoadingData(true);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        disparityLogin();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(!pro.equals(BuyStockHelper.serachText)){
                index = 1;
                LoadingData(true);
             // btnSelectPro.setText(BuyStockHelper.serachText);
//                adapter.clearSelect(-1);
            }
        }
    }

    private void initView(View view) {
        pro= BuyStockHelper.serachText;
        mContext = getActivity();
        mList = new ArrayList<>();
        mLine = view.findViewById(R.id.line);
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_go_where);
        mListView = mPullRefreshListView.getRefreshableView();
        //设置点击效果
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        //下拉加载
        mPullRefreshListView.setPullLoadEnabled(true);
        //下拉刷新
        mPullRefreshListView.setPullRefreshEnabled(true);
        mPullRefreshListView.setScrollLoadEnabled(true);
        drawableUp = mContext.getResources().getDrawable(R.mipmap.ic_up);
        drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        drawableDown = mContext.getResources().getDrawable(R.mipmap.ic_down);
        drawableDown.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        btnSelectPro = (TextView) view.findViewById(R.id.tv_select_pro);
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

    private void initAdapter() {
        mAdapter = new LongAdapter(mContext, mList, this);
        mListView.setAdapter(mAdapter);
    }

    public void initTitlebar(View view) {
        mTitlebar = (BaseTitlebar) view.findViewById(R.id.title_bar);
        mTitlebar.setTitle("租房车");
    }

    //初始化适配器
    private void initPopWindows() {

        btnSelectPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWhere.showAsDropDown(mLine);
                btnSelectPro.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableUp, null);
            }
        });

        cities.clear();
        Collections.addAll(cities, provinces);
        popView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_whereplay, null);
        gridView = (NoScorllGridView) popView.findViewById(R.id.gridview);
        gridView.setNumColumns(4);
        adapter = new WherePlaySelectProAdapter(mContext, cities);
        gridView.setAdapter(adapter);
        adapter.clearSelect(0);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pro = adapter.clearSelect(i);
                BuyStockHelper.serachText=adapter.clearSelect(i);
                btnSelectPro.setText(pro);
                index = 1;
                LoadingData(true);
                mWhere.dismiss();
            }
        });


        mWhere = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mWhere.setBackgroundDrawable(new BitmapDrawable());

        mWhere.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btnSelectPro.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableDown, null);
            }
        });
    }

    private void LoadingProvinceData() {
        String allProvinceUrl = UrlAPI.getAllProvinceUrl();
        PrintLog.e("所有省份URL:" + allProvinceUrl);
        OkHttpUtils.get().url(allProvinceUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("所有省份:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                PrintLog.e("所有省份:" + response);
                Type type = new TypeToken<Response<ProvinceModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response<ProvinceModel> resp = gson.fromJson(response, type);
                List<ProvinceModel> data = resp.getData();
                provinces = new String[data.size()+1];
                provinces[0]="全部";
                for (int i = 0; i < data.size(); i++) {
                    provinces[i+1] = data.get(i).province;
                }
                initPopWindows();
            }
        });
    }

    private void LoadingData(final boolean isrefresh) {
        String motorhomeListUrl = UrlAPI.getMotorhomeListUrl(BuyStockHelper.serachText, index);
        PrintLog.e("租房车URL:" + motorhomeListUrl);
        OkHttpUtils.get().url(motorhomeListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("租房车:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                mPullRefreshListView.onRefreshComplete();
                PrintLog.e("租房车---------------:" + response);
                Type type = new TypeToken<Response1<ListModel<MotorhomeRentModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<MotorhomeRentModel>> resp = gson.fromJson(response, type);
                ListModel<MotorhomeRentModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<MotorhomeRentModel> data) {
        btnSelectPro.setText(BuyStockHelper.serachText);
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
                dataEmpty("暂无可租用的房车",0,0);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        MotorhomeHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_motorhome, null, false);
            helper = new MotorhomeHelper(mContext, itemView);
            itemView.setTag(helper);
        } else {
            helper = (MotorhomeHelper) itemView.getTag();
        }
        final MotorhomeRentModel model = (MotorhomeRentModel) data;
        helper.updateView(model);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "预定", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MotorhomeDetailsActivity.class);
                setCustomStatisticsKV(getActivity(),MOTORHOME_ID, model.name);
                intent.putExtra("id", model.id);
                intent.putExtra("name", model.name);
                startActivity(intent);
                //startActivity(new Intent(mContext, MakeOrderActivity.class));
            }
        });
        return itemView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BuyStockHelper.serachText = null;
    }
}
