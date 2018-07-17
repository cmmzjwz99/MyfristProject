package com.lzhy.moneyhll.gowhere;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseFragment;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.PlayWhereListModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.BuyStockHelper;
import com.lzhy.moneyhll.viewhelper.GoWhereHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * 去哪玩
 */
public class GoWhereFm extends BaseFragment implements ItemViewListener, AMapLocationListener {
    private BaseTitlebar mTitlebar;
    private PullToRefreshListView mPullRefreshListView;
    private List<PlayWhereListModel> mList;
    private LongAdapter mAdapter;
    private Context mContext;
    protected ListView mListView;
    private int type = 100;
    private int index = 0;

    // private TextView number;
    //定位
    private AMapLocationClient mMapLocationClient = null;
    private AMapLocationClientOption option = null;
    private Double Lat, Lon;

    private EditText go_where_selector;
    private TextView cancel;
    private View head;
    private String projectname = "";
    private int pid = 1000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_go_where_main, null);
        showLoading();
        initView(view);
        initTitlebar(view);
        initAdapter();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        disparityLogin();
    }
    public void initView(View view) {
        mMapLocationClient = new AMapLocationClient(getContext());
        mMapLocationClient.setLocationListener(this);

        option = new AMapLocationClientOption();
        //设置定位模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位请求超时时间，
        option.setHttpTimeOut(30000);
        //设置定位一次
        option.setOnceLocation(true);
        mMapLocationClient.setLocationOption(option);
        if (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMapLocationClient.startLocation();
        } else {
            repuestPermission(Constant.LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
        projectname = BuyStockHelper.serachText1;
        mContext = getActivity();
        mList = new ArrayList<>();
        head = LayoutInflater.from(mContext).inflate(R.layout.item_go_where_head1, null);
        go_where_selector = (EditText) head.findViewById(R.id.go_where_selector);
        cancel = (TextView) head.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        // number = (TextView) head.findViewById(R.id.go_where_number);
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_go_where);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.addHeaderView(head);
        //设置点击效果
        // mListView.setSelector(new ColorDrawable(0x00ffffff));
        //下拉加载
        mPullRefreshListView.setPullLoadEnabled(true);
        mPullRefreshListView.setScrollLoadEnabled(true);
        //下拉刷新
        mPullRefreshListView.setPullRefreshEnabled(true);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    mMapLocationClient.startLocation();
                } else {
                    Toast.makeText(getContext(), "请开启定位权限，有更多精彩内容", Toast.LENGTH_SHORT).show();
                    repuestPermission(Constant.LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                LoadingData(false);
            }
        });
        go_where_selector.setFocusable(false);
        go_where_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_where_selector.setText("");
                startActivityForResult(new Intent(getContext(), SelectorCampActivity.class), Constant.REQUEST_CODE);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!projectname.equals(BuyStockHelper.serachText1)) {
                index = 0;
                LoadingData(true);
            }
        }
    }

    private void initAdapter() {
        mListView.setDividerHeight(CommonUtil.dip2px(getContext(), 8));
        mAdapter = new LongAdapter(getActivity(), mList, this);
        mListView.setAdapter(mAdapter);
    }

    public void initTitlebar(View view) {
        mTitlebar = (BaseTitlebar) view.findViewById(R.id.title_bar);
        mTitlebar.setTitle("去哪玩");
    }

    private void LoadingData(final boolean isrefresh) {
        PrintLog.e("去哪玩URL+++++++++++++index++++++++++++++++:" + index);
        String goWhereListUrl = UrlAPI.getGoWhereListUrl(type, index, BuyStockHelper.serachText1, Lat, Lon, pid);
        PrintLog.e("去哪玩URL+++++++++++++++++++++++++++++:" + goWhereListUrl);
        OkHttpUtils.get()
                .url(goWhereListUrl)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("去哪玩:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                mPullRefreshListView.onRefreshComplete();
                PrintLog.e("去哪玩:" + response);
                Type type = new TypeToken<Response1<ListModel<PlayWhereListModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<PlayWhereListModel>> resp = gson.fromJson(response, type);
                ListModel<PlayWhereListModel> data = resp.getData();
                updataView(isrefresh, data);
//                if (networkIsAvailable(mContext)) {
//                    number.setText("附近有" + data.total + "露营地");
//                } else {
//                    number.setText("请您连接网络获取最新数据");
//                }
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

        go_where_selector.setText(BuyStockHelper.serachText1);
        if (isrefresh) {
            mList.clear();
        }
        if (mList.size() == data.total && mList.size() != 0) {
            index = index > 1 ? index - 1 : 1;
            mPullRefreshListView.setHasMoreData(false);
        }
        if (data.list != null && data.list.size() > 0) {
            mList.addAll(data.list);
            hideLoading();
        } else {
            if (mList.size() == 0) {
                dataEmpty("暂无露营地",0,0);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {

        GoWhereHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_go_where_etape, null, false);
            helper = new GoWhereHelper(mContext, itemView);
            itemView.setTag(helper);
        } else {
            helper = (GoWhereHelper) itemView.getTag();
        }
        final PlayWhereListModel model = (PlayWhereListModel) data;
        helper.updateView(model, id);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CampingDetailsActivity.class);
                intent.putExtra("id", model.id);
                intent.putExtra("image",model.picture2);
                intent.putExtra("name",model.projectname);
                intent.putExtra("text",model.address);
                startActivity(intent);
            }
        });
        return itemView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BuyStockHelper.serachText = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE && resultCode == Constant.RESULT_CODE) {
            BuyStockHelper.serachText1 = data.getStringExtra("text");
//            go_where_selector.setText(BuyStockHelper.serachText1);
            LoadingData(true);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            Lat = aMapLocation.getLatitude();
            Lon = aMapLocation.getLongitude();
            PrintLog.e("定位结果-------------" + Lat + Lon + aMapLocation.getAddress() + aMapLocation.getErrorCode());
            PrintLog.e("定位结果------------------111" + aMapLocation);
            index = 0;
            LoadingData(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.LOCATION_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                mMapLocationClient.startLocation();
            } else {
                Toast.makeText(getContext(), "请开启定位权限，有更多精彩内容", Toast.LENGTH_SHORT).show();
            }
        }
    }


}