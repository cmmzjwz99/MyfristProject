package com.lzhy.moneyhll.launcher;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseActivity;
import com.lzhy.moneyhll.home.MainActivity;
import com.lzhy.moneyhll.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity {

    private ViewPager viewPager;
    private List<View> views;

    private LinearLayout layout;
    private LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_guide);
        addActivityCST(this);
        //申请权限
        if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE)) {

            repuestPermission(Constant.LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE);
        }

        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //创建快捷方式
        if (!ScreenUtils.hasInstallShortcut(this)) {
            sendBroadcast(ScreenUtils.getShortcutToDesktopIntent(this));
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        layout = (LinearLayout) findViewById(R.id.bottom_id);

        float density = getResources().getDisplayMetrics().density;

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) (10 * density), 0, (int) (10 * density), 0);

        //引导页页面
        views = new ArrayList<>();
        views.add(getLayoutInflater().inflate(R.layout.view_pager1, null));
        views.add(getLayoutInflater().inflate(R.layout.view_pager2, null));
        addDotView();


        views.get(views.size() - 1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new android.content.Intent(GuideActivity.this, MainActivity.class));
                com.lzhy.moneyhll.utils.PreferencesUtils.putBoolean(getApplicationContext(), "is_first", true);
                finish();
            }
        });
        viewPager.setAdapter(new MyPagerAdapter());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO_XBB Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO_XBB Auto-generated method stub

            }
        });


    }
    @Override
    protected void onResume() {
        disparityLogin();
        super.onResume();
    }

    private void addDotView() {
        ImageView dotImageView = null;

        for (int i = 0; i < views.size(); i++) {
            dotImageView = new ImageView(getApplicationContext());
            dotImageView.setTag(i);
            dotImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewPager.setCurrentItem((Integer) v.getTag());
                }
            });

            layout.addView(dotImageView, params);
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO_XBB Auto-generated method stub
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO_XBB Auto-generated method stub
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO_XBB Auto-generated method stub
            return view == object;
        }
    }
}
