package com.lzhy.moneyhll.me.mine;

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
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.RechargeRecordBean;
import com.lzhy.moneyhll.me.mine.bean.RechargeRecordItem;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.lzhy.moneyhll.viewhelper.RechargeRecordHelper;
import com.ta.utdid2.android.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 龙珠充值记录
 * Created by ycq on 2017/1/12.
 */

public class RechargeRecordActivity extends MySwipeBackActivity implements ItemViewListener{
    private PullToRefreshListView listView;
    private LongAdapter adapter;
    private List<RechargeRecordItem> mList;
    public int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_record);
        addActivityCST(this);
        initTitlebar();
        initView();
        initAdapter();
        getRechargeLog(index,true);
    }

    //加载标题栏
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"充值记录");
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.recharge_record_lv);
        mList = new ArrayList<>();
        //上拉加载
        listView.setPullLoadEnabled(true);
        //下拉刷新
        listView.setPullRefreshEnabled(true);
        listView.setScrollLoadEnabled(true);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                getRechargeLog(index,true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                getRechargeLog(index,false);
            }
        });
    }


    /**
     * 获取充值记录信息接口
     * @param index 分页索引
     */
    private void getRechargeLog(int index, final boolean isRefresh){
        String url= UrlAPI.getRechargeRecordUrl(UserInfoModel.getInstance().getId(),index);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("RechargeLogURL:" + e);
                Utils.toast(RechargeRecordActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                listView.onRefreshComplete();
                PrintLog.e("RechargeLogURL:" + response);
                Type type = new TypeToken<Response1<RechargeRecordBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<RechargeRecordBean> resp = gson.fromJson(response, type);
                String errCode = resp.getErrCode();
                if (StringUtils.isEmpty(errCode)) {
                    Utils.toast(RechargeRecordActivity.this, "网络异常");
                    return;
                }
                RechargeRecordBean data=resp.getData();
                if (data!=null)
                updataView(isRefresh,data);
            }
        });
    };

    /**
     * 更新页面显示内容
     * @param isrefresh 是否为刷新
     * @param data 接口请求数据
     */
    private void updataView(boolean isrefresh, RechargeRecordBean data) {
        if (isrefresh) {
            mList.clear();
        }
        if (mList.size() == data.total && mList.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            listView.setHasMoreData(false);
            return;
        }

        if (data.items != null && !data.items.isEmpty()) {
            mList.addAll(data.items);
        } else {
            if (mList.size() == 0) {
                dataEmpty("暂无充值记录",0,0);
            }
        }
        adapter.notifyDataSetChanged();

    }
    //加载适配器
    private void initAdapter() {
        adapter = new LongAdapter(this, mList, this);
        listView.getRefreshableView().setAdapter(adapter);
    }


    /**
     * 列表子项内容加载监听
     * @param id
     * @param itemView 列表子项布局
     * @param vg
     * @param data 列表子项布局数据
     */
    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        RechargeRecordHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(this).inflate(
                    R.layout.item_recharge_record, null, false);
            helper = new RechargeRecordHelper(this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (RechargeRecordHelper) itemView.getTag();
        }
        helper.updateView((RechargeRecordItem) data);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return itemView;

    }


    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

}
