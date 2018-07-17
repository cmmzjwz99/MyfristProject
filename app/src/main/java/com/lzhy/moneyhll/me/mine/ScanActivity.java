package com.lzhy.moneyhll.me.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.event.EventBindRoll;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.home.snapup.SnapItUpActivity;
import com.lzhy.moneyhll.me.maker.VerificationResultActivity;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.PrintLog;
import com.tencent.mm.sdk.modelbiz.JumpToBizProfile;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.Fangchequan;
import static com.lzhy.moneyhll.constant.Constant.MERCHANTID_WX;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilWebViewNoAd.setWebView;

/**
 * Created by cmm on 2016/11/7.
 * 扫一扫
 */
public class ScanActivity extends MySwipeBackActivity {

    private static final int REQUEST_CODE_SCAN = 0x0000;
    private String mCode;
    private WebView mWebView;
    private int reqCode;

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasPermission(Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
        } else {
            repuestPermission(Constant.CAMERA_CODE, android.Manifest.permission.CAMERA);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        if (hasPermission(Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
        initView();
        initTitlebar();
        Intent intent = this.getIntent();
        reqCode = intent.getIntExtra("int", 0);
        addActivityCST(this);
        }
    }


    private void initView() {
        mWebView = (WebView) findViewById(R.id.wv_scan);
        toStartActivity();
    }

    private void toStartActivity() {
//        Log.i("sssss", "toStartActivity: ");
        Intent intent = new Intent(ScanActivity.this,
                CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        PrintLog.e("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                mCode = bundle.getString("result");
                int userId = UserInfoModel.getInstance().getId();

                if (!mCode.equals("")) {
                    /************************************************************
                     *@Author; 龙之游 @ xu 596928539@qq.com
                     * 时间:2016/12/27 17:12
                     * 注释: 根据请求来源（从哪个acticity进入SacnActivity）,处理扫描结果  mCode
                    ************************************************************/

                    if (reqCode == Fangchequan) {//从绑定房车券 进入二维码扫描 用于绑定房车券
                        returnCode(mCode);
//                        Log.i("xxx", "onActivityResult: " + requestCode);
                        return;
                    }
                    if (mCode.contains("http")) { // 如果扫描结果是url
                        /************************************************************
                         *创建者;龙之游 @ xu 596928539@qq.com
                         *修改时间:2017/2/9 9:49
                         * url:  http://weixin.qq.com/r/dEmhuTLEhG-7rXba9xxm
                         *注释:  扫描微信公众号的二维码  需求没有提出来 暂时不管
                         *       此功能需要一个 要跳转的公众号原始ID
                        ************************************************************/
                        if (mCode.contains("weixin.qq.com")) {//微信公众号的链接
                            IWXAPI api = WXAPIFactory.createWXAPI(this, MERCHANTID_WX, false);
                            if (api.isWXAppInstalled()) {
                                JumpToBizProfile.Req req = new JumpToBizProfile.Req();
                                req.toUserName = "要跳转的公众号原始ID"; // 公众号原始ID
                                req.extMsg = "";
                                req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE; // 普通公众号
                                api.sendReq(req);
                            }else{
                                Toast.makeText(this, "微信未安装，请先安装微信", Toast.LENGTH_SHORT).show();
                            }
                        }else if (mCode.contains("SnapItUp")) {//判断是否秒杀入口
                                Intent intent = new Intent(ScanActivity.this, SnapItUpActivity.class);
                                intent.putExtra("url", mCode);
                                ScanActivity.this.startActivity(intent);
                        } else {
                            /************************************************************
                             *@Author; 龙之游 @ xu 596928539@qq.com
                             * 时间:2016/12/19 22:19
                             * 注释: 二维码扫描  绑定房车券 用自己的webview
                             ************************************************************/
                            setWebView(mWebView);
                            mWebView.loadUrl(mCode);
                        }
                        return;
                    } else {
                        /**
                         * 扫描项目订单
                         */
                        PrintLog.e("扫描项目订单" + mCode);
                        VerificationResults(mCode);
                    }
                }
            }
        } else {
            returnCode("");
            finish();
        }
    }

    private void VerificationResults(String mCode) {
        //核销
        String verificationUrl = UrlAPI.getVerificationUrl(UserInfoModel.getInstance().getId(), mCode);
        PrintLog.e("核销URL:" + verificationUrl);
        OkHttpUtils.get().url(verificationUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("核销onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                hideLoading();
                PrintLog.e("核销onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 resp = gson.fromJson(response, type);
                Intent intent = new Intent(ScanActivity.this, VerificationResultActivity.class);
                intent.putExtra("ErrCode", resp.getErrCode());
                startActivity(intent);
                finish();
            }
        });
    }
    private void returnCode(String mCode) {
        //解析mCode
        String number;
        String numberCode;

        if (mCode.contains("&")) {
            number = mCode.split("&")[0];
            numberCode = mCode.split("&")[1];
        } else {
            if (mCode.contains("http")) {
                number = "二维码无房车券信息";
            }else {
                number = mCode;
            }
            numberCode = "";
        }
        //发送事件
        EventBus.getDefault().post(new EventBindRoll(number,numberCode));
        this.finish();
    }


    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,getString(com.lzhy.moneyhll.R.string.scan_title));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initView();
                Intent intent = this.getIntent();
                reqCode = intent.getIntExtra("int", 0);
            } else {
                Toast.makeText(ScanActivity.this, "请开启相机权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
