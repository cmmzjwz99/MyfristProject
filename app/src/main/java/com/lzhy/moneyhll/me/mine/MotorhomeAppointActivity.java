package com.lzhy.moneyhll.me.mine;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.custom.pulltorefreshgridView.GridViewWithHeaderAndFooter;
import com.lzhy.moneyhll.custom.pulltorefreshgridView.PullToRefreshGridViewWithHeaderAndFooter;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.gowhere.WherePlaySelectProAdapter;
import com.lzhy.moneyhll.me.mine.bean.MotorHomeInfo;
import com.lzhy.moneyhll.me.mine.bean.MotorhomeAllModel;
import com.lzhy.moneyhll.model.ProvinceModel;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.lzhy.moneyhll.viewhelper.MotorHomeAppointHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/11/3.
 * 我的房车预约
 */
public class MotorhomeAppointActivity extends MySwipeBackActivity implements ItemViewListener {

    private BaseTitlebar mTitlebar;

    private List<MotorHomeInfo> mList;
    private LongAdapter mAdapter;

    private TextView btnSelectPro;
    private PopupWindow mWhere;
    private Drawable drawableUp, drawableDown;
    private View mLine;
    private View popView;

    private String pro = "";
    private List<String> cities = new ArrayList<>();

    protected PullToRefreshGridViewWithHeaderAndFooter mPullToRefreshGridView;
    protected GridViewWithHeaderAndFooter mGridView;
    private View top;

    private String[] provinces;

    private int index = 1;
    private TextView tvProvince;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_home_appoint);

//        addActivityCST(this);
        initView();
        showLoading();
        initTitlebar();
        initAdapter();
        LoadingProvinceData();
        LoadingData(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        top = LayoutInflater.from(MotorhomeAppointActivity.this).inflate(R.layout.item_motorhome_appoint_top, null);
        tvProvince = (TextView) findViewById(R.id.tv_select_pro);
        mPullToRefreshGridView = (PullToRefreshGridViewWithHeaderAndFooter) findViewById(R.id.gv_wang_dian);
        mGridView = mPullToRefreshGridView.getRefreshableView();
        mPullToRefreshGridView.setPullRefreshEnabled(true);
        mPullToRefreshGridView.setScrollLoadEnabled(true);
        mGridView.setNumColumns(4);
        mGridView.addHeaderView(top);
        //为GridView设置滑动监听
        PullToRefreshBase.OnRefreshListener<GridViewWithHeaderAndFooter> listener = new PullToRefreshBase.OnRefreshListener<GridViewWithHeaderAndFooter>() {

            public void onPullDownToRefresh(
                    final PullToRefreshBase<GridViewWithHeaderAndFooter> refreshView) {
                mPullToRefreshGridView.setHasMoreData(true);
                index = 1;
                LoadingData(true);
            }

            public void onPullUpToRefresh(
                    final PullToRefreshBase<GridViewWithHeaderAndFooter> refreshView) {
                index++;
                LoadingData(false);

            }
        };
        mPullToRefreshGridView.setOnRefreshListener(listener);

        btnSelectPro = (TextView) findViewById(R.id.tv_select_pro);
        mLine = findViewById(R.id.line);
        tvProvince.setText("浙江省");

        mList = new ArrayList<>();
        drawableUp = getResources().getDrawable(R.mipmap.ic_up);
        drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        drawableDown = getResources().getDrawable(R.mipmap.ic_down);
        drawableDown.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
    }


    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"房车预约");
    }


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
        popView = LayoutInflater.from(MotorhomeAppointActivity.this).inflate(R.layout.popwindow_whereplay, null);
        NoScorllGridView gridView = (NoScorllGridView) popView.findViewById(R.id.gridview);
        gridView.setNumColumns(4);
        final WherePlaySelectProAdapter adapter = new WherePlaySelectProAdapter(MotorhomeAppointActivity.this, cities);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pro = adapter.clearSelect(i);
                tvProvince.setText(pro);
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

    /**
     * 获取所有省份
     */
    private void LoadingProvinceData() {
        String allProvinceUrl = UrlAPI.getAllProvinceUrl();
        PrintLog.e("所有省份URL:" + allProvinceUrl);
        OkHttpUtils.get().url(allProvinceUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("所有省份:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("所有省份:" + response);
                Type type = new TypeToken<Response<ProvinceModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response<ProvinceModel> resp = gson.fromJson(response, type);
                List<ProvinceModel> data = resp.getData();
                provinces = new String[data.size()];
                for (int i = 0; i < data.size(); i++) {
                    provinces[i] = data.get(i).province;
                }
                initPopWindows();
            }
        });
    }


    /**
     * 网点信息
     *
     * @param
     */
    private void LoadingData(final boolean isResfsh) {

        String motorhomeListUrl = UrlAPI.getAllBranchListUrl(tvProvince.getText().toString(), index);
        PrintLog.e("房车预约:" + motorhomeListUrl);
        OkHttpUtils.get().url(motorhomeListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Utils.toast(MotorhomeAppointActivity.this, "网络异常");
                PrintLog.e("房车预约:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mPullToRefreshGridView.onRefreshComplete();
                PrintLog.e("房车预约:" + response);
                Type type = new TypeToken<Response1<MotorhomeAllModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<MotorhomeAllModel> resp = gson.fromJson(response, type);
                if (resp.getErrCode() == null) {
                    Utils.toast(MotorhomeAppointActivity.this, "暂无适合服务");
                    return;
                }
                if (!resp.getErrCode().equals("200")) {
                    Utils.toast(MotorhomeAppointActivity.this, "获取网点失败");
                    return;
                }
                Utils.toast(MotorhomeAppointActivity.this, "获取网点成功");
                MotorhomeAllModel data = resp.getData();
//
                updataView(data, isResfsh);
            }
        });

    }


    private void updataView(MotorhomeAllModel data, boolean isResfsh) {
        if (isResfsh) {
            PrintLog.log_d("清空数据");
            mList.clear();
        }
        if (mList.size() == data.total&& mList.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            mPullToRefreshGridView.setHasMoreData(false);
        }
        if (data.items != null && data.items.size() > 0) {
            mList.addAll(data.items);
        }
        mAdapter.notifyDataSetChanged();
    }


    private void initAdapter() {
        mAdapter = new LongAdapter(MotorhomeAppointActivity.this, mList, this);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        MotorHomeAppointHelper helper = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(this).inflate(
                    R.layout.item_motor_home_appoint_grid, null, false);
            helper = new MotorHomeAppointHelper(this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (MotorHomeAppointHelper) itemView.getTag();
        }
        final MotorHomeInfo info = (MotorHomeInfo) data;

        helper.updateView(info);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MotorhomeAppointActivity.this, MotorhomeAppointDetailsActivity.class);
                intent.putExtra("id", info.id);
                startActivity(intent);
            }
        });
        return itemView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cities.clear();
        provinces = null;
    }
}
