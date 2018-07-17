package com.lzhy.moneyhll.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseFoundFm;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.home.adapter.HomeAdapter;
import com.lzhy.moneyhll.home.banner.BannerDetailActivity;
import com.lzhy.moneyhll.home.makerproject.MakerDetailsActivity;
import com.lzhy.moneyhll.home.snapup.SnapItUpActivity;
import com.lzhy.moneyhll.model.BannerModel;
import com.lzhy.moneyhll.model.HomeListModel;
import com.lzhy.moneyhll.model.ListModel;
import com.lzhy.moneyhll.model.MakerModel;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.lzhy.moneyhll.home.adapter.HomeAdapter.VIEW_TYPE_CAMP;
import static com.lzhy.moneyhll.home.adapter.HomeAdapter.VIEW_TYPE_CAMP_TOP;
import static com.lzhy.moneyhll.home.adapter.HomeAdapter.VIEW_TYPE_MAKER;
import static com.lzhy.moneyhll.home.adapter.HomeAdapter.VIEW_TYPE_MAKER_BOTTON;
import static com.lzhy.moneyhll.home.adapter.HomeAdapter.VIEW_TYPE_MAKER_TOP;
import static com.lzhy.moneyhll.utils.CommonUtil.getBuilder;
import static com.lzhy.moneyhll.utils.UtilCheckMix.networkIsAvailable;

/**
 * 首页
 */
public class HomeFragment extends BaseFoundFm implements AMapLocationListener {

    private Context mContext;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private View banner;

    private HomeAdapter mAdapter;
    //private View view;

    private List<HashMap<Integer, Object>> itemdata;
    private HashMap<Integer, Object> labMap;
    private HashMap<Integer, Object> labMap1;
    //banner图片
    protected List<BannerModel> mList;
    //定位
    private AMapLocationClient mMapLocationClient = null;
    private AMapLocationClientOption option = null;
    private double lat, lon;
    private String bannerUrl;
    private List<MakerModel> mMakerModels;
    private List<HomeListModel> mHomeListModels;


    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/24 16:28
     * 注释:   1 重写banner
     ************************************************************/
    private RollPagerView mLoopViewPager;
    private TestLoopAdapter mLoopAdapter;
    OkHttpClient client = new OkHttpClient();
    private String[] imgs;
    private String[] imgsDetail;
    private String[] nameDetail;
    private IconHintView iconHintView;


    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/24 16:28
     * 注释: 2
     ************************************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_home, null);
        mContext = getActivity();
        initView();
        initBanner();
        initMap();
        initAdapter();
        setListener();
        LoadingMakerData();
        return view;
    }

    private void setListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getBannerData();
                LoadingMakerData();
                if (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    mMapLocationClient.startLocation();
                } else {
                    repuestPermission(Constant.LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
    }

    private void initBanner() {
        bannerUrl = UrlAPI.getHomeBannerUrl();
        banner = LayoutInflater.from(getContext()).inflate(R.layout.banner_view, null);

        mLoopViewPager = (RollPagerView) banner.findViewById(R.id.loop_view_pager);
        mLoopViewPager.setPlayDelay(3000);
        mLoopViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mLoopViewPager));

        ViewGroup.LayoutParams mLayoutParams = mLoopViewPager.getLayoutParams();
        mLayoutParams.height = CommonUtil.getScreenWidthPixels((Activity) mContext) * 41 / 75;
        mLoopViewPager.setLayoutParams(mLayoutParams);
        int screenWidth = CommonUtil.getScreenWidthPixels((Activity) mContext);
        if (screenWidth == 320) {
            iconHintView = new IconHintView(getActivity(), R.mipmap.point_focus, R.mipmap.point_normal, 25);
        } else if (screenWidth == 480) {
            iconHintView = new IconHintView(getActivity(), R.mipmap.point_focus, R.mipmap.point_normal, 30);
        } else if (screenWidth == 540) {
            iconHintView = new IconHintView(getActivity(), R.mipmap.point_focus, R.mipmap.point_normal, 40);
        } else if (screenWidth == 600) {
            iconHintView = new IconHintView(getActivity(), R.mipmap.point_focus, R.mipmap.point_normal, 45);
        } else if (screenWidth == 720) {
            iconHintView = new IconHintView(getActivity(), R.mipmap.point_focus, R.mipmap.point_normal, 50);
        } else if (screenWidth == 1080) {
            iconHintView = new IconHintView(getActivity(), R.mipmap.point_focus, R.mipmap.point_normal);
        }

        mLoopViewPager.setHintView(iconHintView);
        setBannerListener();
        getBannerData();
    }

    private void setBannerListener() {
        mLoopViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(getActivity(),"Item "+position+" clicked"+imgsDetail[position],Toast.LENGTH_SHORT).show();
                if (!networkIsAvailable(getContext())) {
                    Toast.makeText(getContext(), "请检查网络连接", Toast.LENGTH_LONG).show();
                    return;
                }
                //根据 URL 处理不同的banner事件
                if (imgsDetail[position].contains("SnapItUp")) {
                    Intent intent = new Intent(mContext, SnapItUpActivity.class);
                    intent.putExtra("url", imgsDetail[position]);
                    startActivity(intent);
                } else if (imgsDetail[position].contains("maker/detail") || imgsDetail[position].contains("maker/gdetail")) {
                    Intent intent = new Intent(mContext, MakerDetailsActivity.class);
                    Bundle extras = new Bundle();
                    if (imgsDetail[position].contains("maker/detail")) {
                        extras.putInt("type", 103);
                    } else {
                        extras.putInt("type", 104);
                    }
                    String[] split = imgsDetail[position].split("/");

                    int id = Integer.valueOf(split[split.length - 1]);
                    PrintLog.e("创客id：" + id);
                    extras.putInt("id", id);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, BannerDetailActivity.class);
                    intent.putExtra("url", imgsDetail[position]);
                    intent.putExtra("name", nameDetail[position]);
                    startActivity(intent);
                }

            }
        });
    }

    /************************************************************
     * @Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/24 16:34
     * 注释:
     ************************************************************/
    public void getBannerData() {
        Request request = new Request.Builder()
                .url(bannerUrl)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.i("NetImageActivity", "error:" + e.getMessage());

                mPullRefreshListView.onPullDownRefreshComplete();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String content = response.body().string();
                PrintLog.e("首页banner首页banner:" + response);
                Type type = new TypeToken<Response<BannerModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response<BannerModel> resp = gson.fromJson(content, type);
                List<BannerModel> data = resp.getData();
//                Log.i("-----------------------", "raw data:" + data.size());
                /************************************************************
                 *@Author; 龙之游 @ xu 596928539@qq.com
                 * 时间:2016/12/24 17:08
                 * 注释:  获取banner   图片imgs 和  banner 详情 imgsDetail
                 ************************************************************/
                int len = data.size();
                imgs = new String[len];
                imgsDetail = new String[len];
                nameDetail = new String[len];
                for (int i = 0; i < data.size(); i++) {
                    imgs[i] = data.get(i).imageUrl;
                    imgsDetail[i] = data.get(i).linkUrl;
                    nameDetail[i] = data.get(i).title;
//                    Log.i("xxx", "onResponseImgs: " + imgs[i]);
//                    Log.i("xxx", "onResponseDetail: " + imgsDetail[i]);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoopAdapter.setImgs(imgs);
                        if (imgs.length == 1) {
                            mLoopViewPager.pause();
                            mLoopViewPager.setHintView(null);
                        }
//                            Toast.makeText(mContext, "刷新", Toast.LENGTH_SHORT).show();
                    }
                });


                mPullRefreshListView.onPullDownRefreshComplete();
            }

        });
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        String[] imgs = new String[0];

        public void setImgs(String[] imgs) {
            this.imgs = imgs;
            notifyDataSetChanged();
        }


        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
//            Log.i("RollViewPager", "getView:" + imgs[position]);
            ImageView view = new ImageView(container.getContext());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.i("RollViewPager", "onClick");
                }
            });
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(getActivity())
                    .load(imgs[position])
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
//                    .placeholder(R.mipmap.banner_holder)
                    .into(view);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }

    }

    private void initMap() {
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
    }

    private void initView() {
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.itemlist);
        mPullRefreshListView.setPullLoadEnabled(false);
        mPullRefreshListView.setPullRefreshEnabled(true);

        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setSelector(new ColorDrawable(0x00ffffff));
        mList = new ArrayList<>();
    }

    private void initAdapter() {
        mListView.setDividerHeight(0);
        mListView.addHeaderView(banner);

        itemdata = new ArrayList<>();
        labMap = new HashMap<Integer, Object>();
        labMap.put(HomeAdapter.VIEW_TYPE_CATEGOTY, "");
        itemdata.add(labMap);

        labMap1 = new HashMap<Integer, Object>();
        labMap1.put(HomeAdapter.VIEW_TYPE_INTERVAL, "");
        itemdata.add(labMap1);

        mAdapter = new HomeAdapter(mContext, itemdata);
        mListView.setAdapter(mAdapter);
    }

    /************************************************************
     * 创建者;龙之游 @ xu 596928539@qq.com
     * 修改时间:2016/12/24 11:22
     * 注释: 下面的与轮播无关
     ************************************************************/
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    private void LoadingData() {
        String homeListUrl = UrlAPI.getHomeListUrl(lat, lon);
        PrintLog.e("首页列表URL:" + homeListUrl);
        getBuilder(homeListUrl).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // FIXME: 2016/11/17 网络状况不佳
                Toast.makeText(mContext, "网络状况不佳,请切换网络", Toast.LENGTH_SHORT).show();
                return;
            }

            @Override
            public void onResponse(String response, int id) {
                mPullRefreshListView.onRefreshComplete();
                PrintLog.e("首页列表:" + response);
                Type type = new TypeToken<Response<HomeListModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response<HomeListModel> resp = gson.fromJson(response, type);
                mHomeListModels = resp.getData();
                updataView();
            }
        });
    }


    private void LoadingMakerData() {
        String homeListUrl = UrlAPI.getPalyWhatListUrl("1022", "103,104", 0, null, null, null);
        PrintLog.e("首页创客列表URL:" + homeListUrl);
        getBuilder(homeListUrl).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("首页创客列表onError:" + e);
                UtilToast.getInstance().showDragonError("网络状况不佳");
                return;
            }

            @Override
            public void onResponse(String response, int id) {
//                PrintLog.e("首页创客列表onResponse:" + response);
                Type type = new TypeToken<Response1<ListModel<MakerModel>>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<ListModel<MakerModel>> resp = gson.fromJson(response, type);
                mMakerModels = resp.getData().list;
                updataView();

            }
        });
    }


    private void updataView() {
        itemdata.clear();
        itemdata.add(labMap);
        itemdata.add(labMap1);
        //首页创客
        if (mMakerModels != null && mMakerModels.size() != 0) {
            HashMap<Integer, Object> labMap1 = new HashMap<Integer, Object>();
            labMap1.put(VIEW_TYPE_MAKER_TOP, "");
            itemdata.add(labMap1);
            for (int i = 0; i < mMakerModels.size() && i < 2; i++) {
                HashMap<Integer, Object> map2 = new HashMap<Integer, Object>();
                map2.put(VIEW_TYPE_MAKER, mMakerModels.get(i));
                itemdata.add(map2);
            }
            if (mMakerModels.size() >= 2) {
                HashMap<Integer, Object> map3 = new HashMap<Integer, Object>();
                map3.put(VIEW_TYPE_MAKER_BOTTON, "");
                itemdata.add(map3);
            }
        }
        //首页露营地
        if (mHomeListModels != null) {
            HashMap<Integer, Object> labMap1 = new HashMap<Integer, Object>();
            labMap1.put(VIEW_TYPE_CAMP_TOP, "");
            itemdata.add(labMap1);
            for (int i = 0; i < mHomeListModels.size(); i++) {
                HashMap<Integer, Object> labMap2 = new HashMap<Integer, Object>();
                labMap2.put(VIEW_TYPE_CAMP, mHomeListModels.get(i));
                itemdata.add(labMap2);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            lat = aMapLocation.getLatitude();
            lon = aMapLocation.getLongitude();
            if (aMapLocation.getLatitude() <= 0 || aMapLocation.getLongitude() <= 0) {
//                showDragonError("定位失败");//造成启动时无网络闪退
            }
            LoadingData();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.LOCATION_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                mMapLocationClient.startLocation();
            } else {
                UtilToast.getInstance().showDragonInfo("请开启定位权限，有更多精彩内容");

            }
        }
    }
}
