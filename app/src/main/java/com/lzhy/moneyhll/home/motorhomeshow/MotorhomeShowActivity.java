package com.lzhy.moneyhll.home.motorhomeshow;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.model.MotorhomeShowModel;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.MotorhomeShowHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 房车展示
 */
public class MotorhomeShowActivity extends MySwipeBackActivity implements ItemViewListener {
    private int brandId;
    private BaseTitlebar mTitlebar;
    private PullToRefreshListView mPullRefreshListView;
    private List<MotorhomeShowModel> mList;
    private LongAdapter mAdapter;
    private ListView mListView;
    private SimpleDraweeView mDraweeView;
    private TextView name;
    private TextView text;

    private View mHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_motorhome_show);

        addActivityCST(this);
        initView();
        showLoading();
        initTitlebar();
        initAdapter();
        LoadingData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

    private void initView() {
        Intent intent = getIntent();
        brandId = intent.getIntExtra("id", 0);
        mHead = LayoutInflater.from(MotorhomeShowActivity.this).inflate(R.layout.item_motorhome_show_head, null);
        mDraweeView = (SimpleDraweeView) mHead.findViewById(R.id.image);
        name = (TextView) mHead.findViewById(R.id.name);
        text = (TextView) mHead.findViewById(R.id.text);

        mList = new ArrayList<>();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_go_where);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.addHeaderView(mHead);
        //设置点击效果
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        //下拉加载
        mPullRefreshListView.setPullLoadEnabled(true);
        //下拉刷新
        mPullRefreshListView.setPullRefreshEnabled(true);
        mPullRefreshListView.setScrollLoadEnabled(true);
    }

    public void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        mTitlebar.setTitle("房车展示");
        mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initAdapter() {
        mListView.setDividerHeight(0);
        mAdapter = new LongAdapter(MotorhomeShowActivity.this, mList, this);
        mListView.setAdapter(mAdapter);
    }

    private void LoadingData() {
        String displayUrl = UrlAPI.getMotorhomeShowUrl(brandId);
        PrintLog.e("房车展示URL:" + displayUrl);
        OkHttpUtils.get().url(displayUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Toast.makeText(MotorhomeShowActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("房车展示:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                PrintLog.e("房车展示:" + response);
                Type type = new TypeToken<Response<MotorhomeShowModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response<MotorhomeShowModel> resp = gson.fromJson(response, type);
                List<MotorhomeShowModel> data = resp.getData();
                if (data != null) {
                    mList.clear();
                    mList.addAll(data);
                    if (data.get(0).logoUrl != null) {
                        mDraweeView.setImageURI(Uri.parse(data.get(0).logoUrl));
                    }
                    name.setText(data.get(0).manufacturer);
                    text.setText(data.get(0).remark);
                    PrintLog.e("房车展示:" + mList.size());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public View getView(final int id, View itemView, ViewGroup vg, Object data) {
        MotorhomeShowHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(MotorhomeShowActivity.this).inflate(
                    R.layout.item_motorhome_show, null, false);
            helper = new MotorhomeShowHelper(MotorhomeShowActivity.this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (MotorhomeShowHelper) itemView.getTag();
        }
        final MotorhomeShowModel model = (MotorhomeShowModel) data;
        helper.updateView(model);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MotorhomeShowActivity.this, "房车详情", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MotorhomeShowActivity.this, MororhomeShowDetailsActivity.class);
                intent.putExtra("name", model.name);
                intent.putExtra("id", model.id + "");
                startActivity(intent);
            }
        });
        return itemView;
    }
}
