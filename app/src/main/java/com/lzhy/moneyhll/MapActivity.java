package com.lzhy.moneyhll;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.dialog.ChooseMapPop;
import com.lzhy.moneyhll.utils.PrintLog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

public final class MapActivity extends MySwipeBackActivity implements AMapLocationListener {
    private MapView mMapView;
    private AMap map;
    private String name;
    //    private String originname;
    private String Titlename;
    //定位
    AMapLocationClient mMapLocationClient;
    AMapLocationClientOption option;
    View.OnClickListener listener;

    private Button navigation;
    private ChooseMapPop mMapPop;
    private PackageManager mPackageManager;
    private List<PackageInfo> mPackageInfos;
    private LatLng origin, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        addActivityCST(this);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initMap();
        initTitlebar();
    }

    private void initMap() {
        mMapLocationClient = new AMapLocationClient(this);
        mMapLocationClient.setLocationListener(this);
        option = new AMapLocationClientOption();
        //设置定位模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位请求超时时间，
        option.setHttpTimeOut(30000);
        option.setNeedAddress(true);

        //设置定位一次
        option.setOnceLocation(true);
        mMapLocationClient.setLocationOption(option);

        if (hasPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMapLocationClient.startLocation();
        } else {
            repuestPermission(Constant.LOCATION_CODE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(!hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            repuestPermission(Constant.STORAGE_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        navigation = (Button) findViewById(R.id.navigation);
        navigation.setVisibility(View.GONE);
        mPackageManager = getPackageManager();
        mPackageInfos = mPackageManager.getInstalledPackages(0);

        map = mMapView.getMap();
        Intent intent = getIntent();
        double lat = Double.valueOf(intent.getStringExtra("lat"));
        double lon = Double.valueOf(intent.getStringExtra("lng"));
        name = intent.getStringExtra("name");
        destination = new LatLng(lat, lon);
        map.getUiSettings().setScaleControlsEnabled(true);

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
        map.moveCamera(CameraUpdateFactory.zoomTo(12));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lon));
        markerOptions.title("目标地");
        markerOptions.visible(true);
        map.addMarker(markerOptions);
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View.OnClickListener itemsOnClick = new View.OnClickListener() {

                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.baidu_map:
                                baiduMap();
                                break;
                            case R.id.autonavi_map:
                                autonaviMap();
                                break;
                            case R.id.cancel:
                                mMapPop.dismiss();
                                break;
                            default:
                                break;
                        }
                        mMapPop.dismiss();
                    }

                };

                mMapPop = new ChooseMapPop(MapActivity.this, itemsOnClick);
                mMapPop.showAtBottom((View) navigation.getParent());
            }
        };
    }

    private void baiduMap() {
        boolean isHaveBaidu = false;
        for (int i = 0; i < mPackageInfos.size(); i++) {
            if ("com.baidu.BaiduMap".equals(mPackageInfos.get(i).packageName)) {
                if (hasPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    mMapLocationClient.startLocation();
                    if (origin!=null&&origin.longitude != 0 && origin.latitude != 0) {
                        startNative_Baidu(origin, destination);
                    } else {
                        Toast.makeText(MapActivity.this, "正在定位", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    repuestPermission(Constant.LOCATION_CODE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION);
                    Toast.makeText(MapActivity.this, "请开启定位权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                isHaveBaidu = true;
            }
        }
        if (!isHaveBaidu) {
            Toast.makeText(this, "还没有安装百度地图", Toast.LENGTH_SHORT).show();
        }
    }

    private void autonaviMap() {
        boolean isHaveAutonavi = false;
        for (int i = 0; i < mPackageInfos.size(); i++) {
            if ("com.autonavi.minimap".equals(mPackageInfos.get(i).packageName)) {
                startNative_Gaode(destination);
                isHaveAutonavi = true;
            }
        }
        if (!isHaveAutonavi) {
            Toast.makeText(this, "还没有安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }

    public void startNative_Baidu(LatLng loc1, LatLng loc2) {
        if (loc1 == null || loc2 == null) {
            return;
        }
        try {
            PrintLog.e(loc1 + "  " + loc2 + "intent://map/direction?origin=latlng:" + loc1.latitude + "," + loc1.longitude + "|name" +
                    "&destination=latlng:" + loc2.latitude + "," + loc2.longitude + "|name" + Titlename +
                    "&mode=driving&src=龙之游|龙之游#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");

            // Intent intent = Intent.parseUri(String.format("baidumap://map/navi?location=%s,%d",loc2.latitude,loc2.longitude),0);
            Intent intent = Intent.parseUri("intent://map/direction?+ origin=latlng:" + loc1.latitude + "," + loc1.longitude +
                    "&destination=latlng:" +
                    loc2.latitude + "," + loc2.longitude
                    + "|name" + Titlename +
                    "&mode=driving&src=龙之游|龙之游#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }

    public void startNative_Gaode(LatLng loc) {
        if (loc == null) {
            return;
        }

        try {
            Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse("androidamap://navi?lat=" + loc.latitude + "&lon=" + loc.longitude + "&dev=1&style=2"));
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        disparityLogin();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapLocationClient.setLocationListener(null);
        mMapLocationClient = null;
    }

    private void initTitlebar() {
        try {
            Titlename = URLDecoder.decode(name, "UTF-8");
            BaseTitlebar mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
            mTitlebar.setTitle(Titlename);
            mTitlebar.setLeftTextButton("返回", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            mTitlebar.setRightText("导航", listener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            origin = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            if (aMapLocation.getLatitude() <= 0 || aMapLocation.getLongitude() <= 0) {
                Toast.makeText(MapActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
            }
            PrintLog.e("定位结果" + aMapLocation.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.LOCATION_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                mMapLocationClient.startLocation();
            } else {
                Toast.makeText(MapActivity.this, "请开启定位权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
