package com.lzhy.moneyhll.home.dragonball;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.home.adapter.SelectTypeAdapter;
import com.lzhy.moneyhll.model.DragonBallModel;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.DragonBallHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.DRAGON_STORE_CLICK_ID;
import static com.lzhy.moneyhll.utils.CommonUtil.setCustomStatisticsKV;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.isForceExit;

/**
 * 龙珠商城
 */
public class ActivityDragonBall extends MySwipeBackActivity implements ItemViewListener {
    private PullToRefreshListView mPullRefreshListView;
    private List<DragonBallModel> mList;
    private LongAdapter mAdapter;
    protected ListView mListView;
    private TextView mTextView;
    private PopupWindow mTypePop;
    private View popView;
    private View mView;
    private Drawable drawableUp, drawableDown;
    private RadioGroup mGroup;
    private TextView back;
    private TextView sure;
    private EditText find_key;
    private int index = 1;
    private String title = "";
    private int commodityTypeId = 0;

    private String[] titles = new String[]{
            "全部", "酒水饮料", "零食干货", "生鲜蔬果",
            "服装鞋包", "户外装备", "家用电器", "数码产品",
            "汽车配件", "家居日用", "珠宝饰品", "其他品类"};
    private List<String> titles1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragon_ball);

//        addActivityCST(this);
        showLoading();
        PrintLog.e("ActivityDragonBall" + CommonUtil.getCurProcessName(ActivityDragonBall.this));
        initView();
        initHorizontalScroll();
        initPopWindows();
        initAdapter();
        LoadingData(true);

    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        mView = findViewById(R.id.line);
        mList = new ArrayList<>();
        mGroup = (RadioGroup) findViewById(R.id.radio_group);
        back = (TextView) findViewById(R.id.back);
        sure = (TextView) findViewById(R.id.sure);
        find_key = (EditText) findViewById(R.id.find_key);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isForceExit()) {
                    finish();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 1;
                LoadingData(true);
                dismissKeyboard();
            }
        });
        find_key.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    index = 1;
                    LoadingData(true);
                    dismissKeyboard();
                    return true;
                }
                return false;
            }
        });
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_go_where);
        mListView = mPullRefreshListView.getRefreshableView();
        //设置点击效果
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        //下拉加载
        mPullRefreshListView.setPullLoadEnabled(true);
        //下拉刷新
        mPullRefreshListView.setPullRefreshEnabled(true);
        mPullRefreshListView.setScrollLoadEnabled(true);
        drawableUp = ActivityDragonBall.this.getResources().getDrawable(R.mipmap.ic_up);
        drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        drawableDown = ActivityDragonBall.this.getResources().getDrawable(R.mipmap.ic_down);
        drawableDown.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        mTextView = (TextView) findViewById(R.id.more);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypePop.showAsDropDown(mView);
                mTypePop.setFocusable(true);
                mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableUp, null);
            }
        });
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
    //初始化适配器

    private void initAdapter() {
        mAdapter = new LongAdapter(ActivityDragonBall.this, mList, this);
        mListView.setAdapter(mAdapter);
    }

    //初始化popwindow
    private void initPopWindows() {
        Collections.addAll(titles1, titles);
        popView = LayoutInflater.from(ActivityDragonBall.this).inflate(R.layout.popwindow_whereplay, null);
        NoScorllGridView gridView = (NoScorllGridView) popView.findViewById(R.id.gridview);
        gridView.setNumColumns(4);
        final SelectTypeAdapter adapter = new SelectTypeAdapter(ActivityDragonBall.this, titles1);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTypePop.dismiss();
                // Toast.makeText(ActivityDragonBall.this, adapter.clearSelect(i), Toast.LENGTH_SHORT).show();
                View view1 = mGroup.getTouchables().get(i);
                RadioButton button = (RadioButton) view1;
                button.setChecked(true);

            }
        });

        mTypePop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mTypePop.setBackgroundDrawable(new BitmapDrawable());
        mTypePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTextView.setCompoundDrawables(null, null, drawableDown, null);
            }
        });
    }

    private void initHorizontalScroll() {
        for (int i = 0; i < titles.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_dragon_ball_type1, null);
            RadioButton button = (RadioButton) view.findViewById(R.id.button);
            button.setText(titles[i]);
            button.setId(i);
            button.setPadding(CommonUtil.Dp2Px(ActivityDragonBall.this, 15), 0, CommonUtil.Dp2Px(ActivityDragonBall.this, 15), 0);
            if (i == 0) {
                button.setChecked(true);
            }
            mGroup.addView(button, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                commodityTypeId = checkedId;
                index = 0;
                LoadingData(true);
            }
        });
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        DragonBallHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(ActivityDragonBall.this).inflate(
                    R.layout.item_dragon_ball, null, false);
            helper = new DragonBallHelper(ActivityDragonBall.this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (DragonBallHelper) itemView.getTag();
        }
        final DragonBallModel model = (DragonBallModel) data;
        helper.updateView(model);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDragonBall.this, DragonBallDetailsActivity.class);
                intent.putExtra("id", model.id);
                intent.putExtra("stockout", model.stockout);

                setCustomStatisticsKV(ActivityDragonBall.this, DRAGON_STORE_CLICK_ID,"龙珠商城入口");//统计
                startActivity(intent);
            }
        });
        return itemView;
    }
    
    private void LoadingData(final boolean isrefresh) {
        title = find_key.getText() + "";
        String dragonBallUrl = UrlAPI.getDragonBallUrl(index, title, commodityTypeId);
        PrintLog.e("龙珠全部商城URL:" + dragonBallUrl);
        OkHttpUtils.get().url(dragonBallUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("龙珠全部商城URL:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mPullRefreshListView.onRefreshComplete();
                PrintLog.e("龙珠全部商城:" + response);
                Type type = new TypeToken<Response1<ListModel<DragonBallModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<DragonBallModel>> resp = gson.fromJson(response, type);
                ListModel<DragonBallModel> data = resp.getData();
                updataView(isrefresh, data);
            }
        });
    }

    private void updataView(boolean isrefresh, ListModel<DragonBallModel> data) {
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
                dataEmpty("暂无商品",0,0);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.clear();
        popView = null;
        titles = null;
        titles1.clear();
    }

}
