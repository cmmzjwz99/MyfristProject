package com.lzhy.moneyhll.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseActivity;
import com.lzhy.moneyhll.home.MainActivity;
import com.lzhy.moneyhll.utils.PreferencesUtils;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity{
    private static final int GO_HOME = 100;
    private static final int SPLASH_DELAY_TIME = 500;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (GO_HOME == 100) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        addActivityCST(this);

        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // tv_version = (TextView) findViewById(R.id.tv_version);
        // tv_version.setText(CommonUtil.getVersionCode(this));
        boolean is_first = PreferencesUtils.getBoolean(getApplicationContext(), "is_first");
        if (is_first) {
            //不是第一次进入时，直接跳转到MainActivity
            handler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_TIME);

        } else {
            //第一次启动时，进入引导页 into
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            finish();
        }
    }
}
