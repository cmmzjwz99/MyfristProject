package com.lzhy.moneyhll.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseActivity;
import com.lzhy.moneyhll.custom.dialog.UpdataDialog;
import com.lzhy.moneyhll.gowhere.GoWhereFm;
import com.lzhy.moneyhll.home.adapter.FragmentMainTabAdapter;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.manager.ActivityManagerCST;
import com.lzhy.moneyhll.me.loginOrRegister.MineFm;
import com.lzhy.moneyhll.motorhome.MotorhomeFm;
import com.lzhy.moneyhll.playwhat.PlayWhatFm;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.CommonUtil.registerHomeKeyReceiver;
import static com.lzhy.moneyhll.utils.CommonUtil.unregisterHomeKeyReceiver;
import static com.lzhy.moneyhll.utils.UtilApp.getVersionName;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static smart.xu.update_download.Presenter.Download.dowaload;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    public UpdataDialog dismiss;
    private static int tab_hight;
    public static int currentIndex = 0;
    private long firstClickTime = 0;
    /**
     * 首页碎片
     */
    private Fragment mTabHomeFm = null;
    /**
     * 租房车碎片
     */
    public static Fragment mTabLimousineFm = null;
    /**
     * 去哪碎片
     */
    public static Fragment mTabGoWhereFm = null;
    /**
     * 玩什么碎片
     */
    private Fragment mTabPlayWhatFm = null;
    /**
     * 我的碎片
     */
    private Fragment mTabMineFm = null;
    public List<Fragment> fragments = new ArrayList<>();
    private FragmentMainTabAdapter tabAdapter;
    private RadioGroup rgs;
    public static RadioButton tabHome, tabLimousine, tabGoWhere, tabPlayWhat, tabMine;

    public static FragmentMainTabAdapter fragmentMainTabAdapter;
    private String apkName;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerHomeKeyReceiver(this);//监听home键等造成app切换

        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        initView();
        initFragment();
        initAdapter();
        getNewVersion();
    }

    private void initAdapter() {

        tabAdapter = new FragmentMainTabAdapter(MainActivity.this, fragments, R.id.tab_content, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentMainTabAdapter.OnRgsExtraCheckedChangedListener() {
                    @Override
                    public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,int checkedId, int index) {

                        currentIndex = index;
                    }

                    @Override
                    public void OnRgsGoLoginChanged(RadioGroup radioGroup, int checkedId) {
                    }
                });
        fragmentMainTabAdapter = tabAdapter;
    }

    //初始化
    private void initView() {
        rgs = (RadioGroup) findViewById(R.id.tabs_rg1);
        tabHome = (RadioButton) findViewById(R.id.tab_rb_a1);
        tabLimousine = (RadioButton) findViewById(R.id.tab_rb_b2);
        tabGoWhere = (RadioButton) findViewById(R.id.tab_rb_c3);
        tabPlayWhat = (RadioButton) findViewById(R.id.tab_rb_d4);
        tabMine = (RadioButton) findViewById(R.id.tab_rb_e5);
        tabMine.setTag(Constant.need_login);//设置tag的值为 需要登录

        rgs.measure(RadioGroup.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.MATCH_PARENT);
        tab_hight = rgs.getMeasuredHeight();
    }

    /**
     * 初始化fragment界面
     */
    private void initFragment() {
        newHomePage();
        newLimousinePage();
        newGoWherePage();
        newPlayWhatPage();
        newMinePage();

        fragments.add(mTabHomeFm);
        fragments.add(mTabLimousineFm);
        fragments.add(mTabGoWhereFm);
        fragments.add(mTabPlayWhatFm);
       fragments.add(mTabMineFm);
    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  *//************************************************************
         *@Author; 龙之游 @ xu 596928539@qq.com
         * 时间:2017/1/17 13:51
         * 注释:  用户从 我的 进入其他 界面 后 返回 到我的界面
        ************************************************************//*
        if (tabMine.isChecked()&&!UserInfoModel.getInstance().isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            tabMine.setChecked(false);
            tabHome.setChecked(true);
            whetherCheck = false;
        }*/
        disparityLogin();
//        Log.i("HomeReceiver", "onResume: "+ whetherCheck);
    }


    public void newHomePage() {
        mTabHomeFm = new HomeFragment();
    }

    public void newLimousinePage() {
        mTabLimousineFm = new MotorhomeFm();
    }

    public void newGoWherePage() {
        mTabGoWhereFm = new GoWhereFm();
    }

    public void newPlayWhatPage() {
        mTabPlayWhatFm = new PlayWhatFm();
    }

    public void newMinePage() {
        mTabMineFm = new MineFm();
    }


    public void getNewVersion() {
        String updateUrl = Constant.UPDATA_VERSION + "?version="
                + getVersionName(this) + "&platform=android"
                +"&uin="+UserInfoModel.getInstance().getId();
//        Log.i("updateUrl", "getNewVersion: "+updateUrl);
        OkHttpUtils.get()
                .url(updateUrl)
                .build()
                .execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("获取最新版本信息onError" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("获取最新版本信息onResponse:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final int version = jsonObject.getInt("version");
                    url = jsonObject.getString("url");

                    final String description = jsonObject.getString("description");
                    if (version != 0 && url != null && url.length() > 0) {
                        if (dismiss == null) {
                            final UpdataDialog.Builder builder = new UpdataDialog.Builder(MainActivity.this);
                            builder.setMessage(description.replace("\\n","\n"));

                            builder.setPositiveButton("立即更新",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                          /*  Uri uri=Uri.parse(url);
                                            Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                                            MainActivity.this.startActivity(intent);*/
                                            /************************************************************
                                             *修改者;  龙之游 @ xu 596928539@qq.com
                                             *修改时间:2016/12/26 11:12
                                             * 注释代码  会用浏览器下载
                                        ************************************************************/
                                            String[] strarray=url.split("/");
                                            apkName = "";
                                            if (strarray.length>1) {
                                                apkName = strarray[strarray.length-1];
                                            }
                                            if(hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                                dowaload(MainActivity.this,apkName, url);
                                            }else{
                                                repuestPermission(Constant.STORAGE_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                            }
                                            dismiss.dismiss();
                                        }
                                    });
                            builder.setNegativeButton("",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dismiss.dismiss();
                                        }
                                    });
                            dismiss = builder.create();
                            dismiss.show();
                        } else {
                            dismiss.show();
                        }
                    }/*else {
                        Toast.makeText(MainActivity.this, "无需更新", Toast.LENGTH_SHORT).show();
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dowaload(MainActivity.this,apkName, url);
            }else{
                Toast.makeText(this, "请开启读写权限后，再更新！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondClickTime = System.currentTimeMillis();
            if (secondClickTime - firstClickTime > 3000) {
                Utils.toast(this, "再按一次退出程序");
                firstClickTime = System.currentTimeMillis();
                return true;
            } else {
                ActivityManagerCST.AppExit();
                /************************************************************
                 *@Author; 龙之游 @ xu 596928539@qq.com
                 * 时间:2016/12/30 14:15
                 * 注释: 用户自己主动退出  都要停止检查
                 ************************************************************/
//                stopTimeTask();//停止异地登陆检查任务
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterHomeKeyReceiver(this);
    }
}
