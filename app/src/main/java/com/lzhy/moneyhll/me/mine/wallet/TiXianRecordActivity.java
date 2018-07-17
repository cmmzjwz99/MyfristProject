package com.lzhy.moneyhll.me.mine.wallet;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.lzhy.moneyhll.me.mine.bean.RecordItems;
import com.lzhy.moneyhll.me.mine.bean.RecordList;
import com.lzhy.moneyhll.me.mine.bean.TiXianRecordBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.TiXianRecordHelper;
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
 * Created by cmm on 2016/11/16.
 * 提现记录
 */
public class TiXianRecordActivity extends MySwipeBackActivity implements ItemViewListener {
    private List<RecordItems> mList;
    private LongAdapter mAdapter;
    protected ListView mListView;
    private PullToRefreshListView lvRecord;
    private TextView tvSum;
    private TextView tvTotal;
    public int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti_xian_record);

        addActivityCST(this);
        initView();
        initTitlebar();
        initAdapter();
        initData(true);
    }

    private void initView() {
        tvTotal = (TextView) findViewById(R.id.tv_all_data);
        tvSum = (TextView) findViewById(R.id.tv_earning_money);
        lvRecord = (PullToRefreshListView) findViewById(R.id.lv_ti_xian_record);
        mList = new ArrayList<>();
        mListView = lvRecord.getRefreshableView();
        //设置点击效果
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        //下拉加载
        lvRecord.setPullLoadEnabled(true);
        //下拉刷新
        lvRecord.setPullRefreshEnabled(true);
        lvRecord.setScrollLoadEnabled(true);
        lvRecord.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 0;
                initData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                initData(false);
            }
        });
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"提现记录");
    }


    //初始化提现记录
    private void initData(final boolean isrefresh) {
//        int type = 2;
//        String withDrawType = SharePrefenceUtils.getString(TiXianRecordActivity.this, "withDrawType");
//        if (withDrawType.equals("1")) {
//            type = 1;
//        }
//        if (withDrawType.equals("2") || withDrawType.equals("")) {
//            type = 2;
//        }
        String getMyEarningUrl = UrlAPI.getTiXianRecordUrl(UserInfoModel.getInstance().getId(),  index);
        PrintLog.e("提现记录URL:" + getMyEarningUrl);
        OkHttpUtils.get().url(getMyEarningUrl).build().execute(new StringCallback() {
            private List<RecordItems> items;

            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("提现记录URL:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("提现记录-------URL:" + response);
                lvRecord.onRefreshComplete();
                try {
                    Type type = new TypeToken<Response1<TiXianRecordBean>>() {
                    }.getType();
                    Gson gson = new Gson();
                    Response1<TiXianRecordBean> resp = gson.fromJson(response, type);
                    TiXianRecordBean data=resp.getData();
                    if (data!=null)
                    tvTotal.setText("共" + data.recordList.total + "条数据，");
                    tvSum.setText("合计佣金" + data.sumWithDraw);

                    items = data.recordList.items;

                    if (items != null) {
                        updataView( data.recordList,isrefresh);
                    }
                } catch (Exception e) {
                }
            }
        });
    }


    private void updataView(RecordList data, boolean isResfsh) {
        if (isResfsh) {
            mList.clear();
        }
        if (mList.size() == data.total&& mList.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            lvRecord.setHasMoreData(false);
            return;
        }
        if (data.items != null && data.items.size() > 0) {
            mList.addAll(data.items);
        }
        mAdapter.notifyDataSetChanged();
    }
    private void initAdapter() {
        mAdapter = new LongAdapter(this, mList, this);
        mListView.setAdapter(mAdapter);
    }

    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        TiXianRecordHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(this).inflate(
                    R.layout.item_ti_xian_record, null, false);
            helper = new TiXianRecordHelper(this, itemView);
            itemView.setTag(helper);
            PrintLog.e("itemView------data---------:" + itemView);
        } else {
            helper = (TiXianRecordHelper) itemView.getTag();
        }
        helper.updateView((RecordItems) data);
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
