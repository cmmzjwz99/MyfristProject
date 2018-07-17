package com.lzhy.moneyhll.me.mine.userInfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.dialog.ChoosePicPop;
import com.lzhy.moneyhll.home.MainActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.loginOrRegister.bean.LoginBean;
import com.lzhy.moneyhll.me.mine.bean.UesrBean;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.Base64;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;
import com.lzhy.moneyhll.utils.UtilToast;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.constant.Constant.CHOOSE_PICTURE;
import static com.lzhy.moneyhll.constant.Constant.CROP_SMALL_PICTURE;
import static com.lzhy.moneyhll.constant.Constant.TAKE_PICTURE;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.me.loginOrRegister.MineFm.is_save;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckIDCard.IDCardValidate;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;
import static com.lzhy.moneyhll.utils.UtilCheckMix.editTextCheck;
import static com.lzhy.moneyhll.utils.UtilFile.savePicture;

/**
 * Created by cmm on 2016/10/26.
 * 个人信息界面
 */
public class MineActivity extends MySwipeBackActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_tittle;
    private TextView tv_finsh;
    private SimpleDraweeView ivHead;
    private Bitmap mBitmap;
    protected static Uri tempUri;
    private ChoosePicPop mPicPop;
    private String realName;
    private String nickName;
    private String idCard;
    private String licenceNo;
    private String address;
    private TextView tvDefault;

    private int gender = 1;
    private LinearLayout llGender;
    private RelativeLayout payPwd;
    private TextView etSave;
    private EditText etName;
    private EditText etNickName;
    private EditText etIdCard;
    private EditText etLicenceNo;
    private RelativeLayout etGender;
    private EditText etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        addActivityCST(this);
        initInfo();
        initTitlebar();
        initView();
        initLodingDate();
    }

    @Override
    protected void onResume() {
        disparityLogin();
        super.onResume();
    }

    //获取个人信息
    private void initLodingDate() {
        /****************
         *修改者:  ycq
         *修改时间: 2017.01.09
         *修改原因: 接口用户信息加密
         * Describe:param  传递的参数
                    Base64.getBase64(param)  参数加密
                    Base64.getFromBase64(response)  返回数据解密
         ****************/
        JSONObject param = new JSONObject();
        try {
            param.put("memberId", UserInfoModel.getInstance().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "v1/user/userInfo")
                .addHeader("accessToken", UserInfoModel.getInstance().token)
                .addParams("req", Base64.getBase64(param))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("6个人信息URL:" + e);
                Utils.toast(MineActivity.this, "网络异常，个人信息加载失败");
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("修改登+++++11111111111111111++++++录密码:" + UserInfoModel.getInstance().account + ":::::" + UserInfoModel.getInstance().token);
                response = Base64.getFromBase64(response);
                PrintLog.e( "获取个人信息URL:" + response);
                Type type = new TypeToken<Response1<UesrBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<UesrBean> resp = gson.fromJson(response, type);
                UesrBean data = resp.getData();
                String errCode = resp.getErrCode();

                if (errCode == null) {
                    return;
                }
                if (!errCode.equals("200")) {
                    Utils.toast(MineActivity.this, resp.getMessage());
                    return;
                }

                ivHead.setImageURI(data.user.avatar);
                etName.setText(data.user.realName);
                etAddress.setText(data.user.address);
                etNickName.setText(data.user.nickName);
                etIdCard.setText(data.user.idCard);
                etLicenceNo.setText(data.user.licenceNo);

                if (data.user.gender == 1) {
                    tvDefault.setText("男");
                } else {
                    tvDefault.setText("女");
                }


                SharePrefenceUtils.put(MineActivity.this, "payPwd", data.user.paypwd);

                try {
                    if (resp.getData().noPayPwd.equals("")) {
                        SharePrefenceUtils.put(MineActivity.this, "noPayPwd", "noPayPwd");
                    }
                } catch (NullPointerException e) {
                    SharePrefenceUtils.put(MineActivity.this, "noPayPwd", "");
                }

                PrintLog.e("resp.getData().noPayPwd-----------:" + resp.getData().noPayPwd);
//                Utils.toast(MineActivity.this, resp.getMessage());
            }
        });
    }

    //初始化布局
    private void initView() {

        tv_finsh = (TextView) findViewById(R.id.tv_finsh);
        payPwd = (RelativeLayout) findViewById(R.id.rl_setting_pay_password);
        RelativeLayout rlSetLoginPwd = (RelativeLayout) findViewById(R.id.rl_setting_login_password);
        etName = (EditText) findViewById(R.id.et_name);
        etNickName = (EditText) findViewById(R.id.et_nickname);
        etIdCard = (EditText) findViewById(R.id.et_id_card);
        etLicenceNo = (EditText) findViewById(R.id.et_code);
        etGender = (RelativeLayout) findViewById(R.id.rl_gender);
        tvDefault = (TextView) findViewById(R.id.tv_default);
        etAddress = (EditText) findViewById(R.id.et_addressa);
        etSave = (TextView) findViewById(R.id.tv_save);

        etGender.setOnClickListener(this);
        etSave.setOnClickListener(this);
        payPwd.setOnClickListener(this);
        rlSetLoginPwd.setOnClickListener(this);

    }

    //初始化标题
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"个人信息");
    }

    //初始化头像
    private void initInfo() {
        ivHead = (SimpleDraweeView) findViewById(R.id.iv_head_portrait);
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View.OnClickListener itemsOnClick = new View.OnClickListener() {

                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.photograph:
                                if (hasPermission(Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                                    onPhotographClick();
                                } else {
                                    repuestPermission(Constant.CAMERA_CODE, android.Manifest.permission.CAMERA);
                                    UtilToast.getInstance().showDragonInfo("请开启相机权限"); ;
                                    mPicPop.dismiss();
                                }
                                break;
                            case R.id.photo_gallery:
                                onPhotoGalleryClick();
                                break;
                            case R.id.cancel:
                                mPicPop.dismiss();
                                break;
                            default:
                                break;
                        }
                        mPicPop.dismiss();
                    }

                };

                mPicPop = new ChoosePicPop(MineActivity.this, itemsOnClick);
                mPicPop.showAtBottom((View) etSave.getParent());
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //设置登录密码
            case R.id.rl_setting_login_password:
                startActivity(new Intent(this, SettingLoginPswActivity.class));
                break;

            //性别选择
            case R.id.rl_gender:
                genderChoose(gender);
                break;
            //保存
            case R.id.tv_save:
                saveInfo();
                break;
            //点击男
            case R.id.tv_man:
                tvDefault.setText("男");
                gender = 1;
                llGender.setVisibility(View.GONE);
                break;
            //点击女
            case R.id.tv_woman:
                tvDefault.setText("女");
                gender = 2;
                llGender.setVisibility(View.GONE);
                break;
            //设置支付密码
            case R.id.rl_setting_pay_password:
                startActivity(new Intent(this, SetPayPassWordActivity.class));
                break;
        }
    }

    private int genderChoose(int gender) {
        llGender = (LinearLayout) findViewById(R.id.ll_gender);
        TextView tvMan = (TextView) findViewById(R.id.tv_man);
        TextView tvMoman = (TextView) findViewById(R.id.tv_woman);
        llGender.setVisibility(View.VISIBLE);
        tvMan.setOnClickListener(this);
        tvMoman.setOnClickListener(this);
        return gender;
    }
    //保存
    private void saveInfo() {
        /************************************************************
         *@Author; 龙之游 @ xu 596928539@qq.com
         * 时间:2017/1/9 19:45
         * 注释: 如果用户处在该页面时，被挤掉，则不允许做保存操作
        ************************************************************/
//        Log.i("onClick", "被挤掉，则不允许做保存操: "+isOtherDevice);
        String tempIdCard = "";
        realName = etName.getText().toString().trim();
        nickName = etNickName.getText().toString().trim();
        idCard = etIdCard.getText().toString().trim();

        /************************************************************
         *@Author; 龙之游 @ xu 596928539@qq.com
         * 时间:2016/12/21 16:03
         * 注释: 姓名和昵称不允许输入非法字符  地址不能输入非法字符
         ************************************************************/
        licenceNo = etLicenceNo.getText().toString().trim();
        address = etAddress.getText().toString().trim();

        if (!editTextCheck(realName,this,"真实姓名不能输入非法字符"))     return;
        if (!editTextCheck(nickName,this,"昵称不能输入非法字符"))         return;
        if (!editTextCheck(address,this,"地址不能输入非法字符"))          return;
        if (!editTextCheck(licenceNo,this,"驾驶证编号不能输入非法字符")) return;

        if (licenceNo.length() != 0) {
            if (licenceNo.length() != 12) {
                UtilToast.getInstance().showDragonError("请输入正确12位驾驶证编号") ;
                return;
            }
        }

//        if (idCard.length() != 0){
//            if (!Utils.idCardNumber(idCard)) {
//                Utils.toast(this, "身份证号码不对，请重输");
//                return;
//            }
//
//        }

        /*****************************************************
         * 修改者 ：龙之游@xu cmm 596928539@qq.com
         * 时间   ：   2016/12/18 0018 14:42
         * 说明   ：最后一位如果是大写 转换为小写后去核对
         *****************************************************/

        if (idCard.length() != 0) {
            if (idCard.substring(idCard.length() - 1, idCard.length()).equals("X")) {
                //tempIdCard = idCard.substring(0,idCard.length()-1)+"x";
                tempIdCard = idCard.toLowerCase();
            } else {
                tempIdCard = idCard;
            }
            if (tempIdCard.length() != 0) {
                if (!IDCardValidate(tempIdCard)) {
                    return;
                }
            }
        }

        /****************
         *修改者:  ycq
         *修改时间: 2017.01.09
         *修改原因: 接口用户信息加密
         * Describe:param  传递的参数
                    Base64.getBase64(param)  参数加密
                    Base64.getFromBase64(response)  返回数据解密
         ****************/
        JSONObject param = new JSONObject();
        try {
            param.put("memberId", UserInfoModel.getInstance().getId());
            param.put("licenceNo", licenceNo);
            param.put("address", address);
            param.put("nickName", nickName);
            param.put("idCard", idCard );
            param.put("realName", realName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "v1/user/updateUser")
                .addParams("req", Base64.getBase64(param))
                .addParams("gender",gender+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("个人信息保存URL:" + e);
                UtilToast.getInstance().showDragonError("网络异常，个人信息保存失败") ;
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("个人信息保存:" + response);
                response = Base64.getFromBase64(response);
                Type type = new TypeToken<Response1<LoginBean>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<LoginBean> resp = gson.fromJson(response, type);
                String errCode = resp.getErrCode();
                if (errCode == null) {
                    return;
                }

                if (!errCode.equals("200")) {
                    Utils.toast(MineActivity.this, resp.getMessage());
                    return;
                }
//                Utils.toast(MineActivity.this, resp.getMessage());
                UserInfoModel.getInstance().updateValue(resp.getData());
                UserInfoModel.getInstance().sync();
                is_save = true;
                finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:// 直接从相册获取
                    try {
                        startPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        e.printStackTrace();// 用户点击取消操作
                    }
                    break;
                case TAKE_PICTURE:// 调用相机拍照
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + "temp_image.jpg");
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                case CROP_SMALL_PICTURE:// 取得裁剪后的图片
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
            }
        }
    }

    //照相
    public void onPhotographClick() {

        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_image.jpg")));
        startActivityForResult(takeIntent, TAKE_PICTURE);
    }

    //图库
    public void onPhotoGalleryClick() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, CHOOSE_PICTURE);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            // Drawable drawable = new BitmapDrawable(null, photo);
//            ivHead.setImageBitmap(photo);
            if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                File file = savePicture(photo, "haed", "111", true);
                OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/updateAvatar")
                        .addParams("id", UserInfoModel.getInstance().getId() + "").addFile("file", "haed.png", file)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        PrintLog.e("上传头像onError:" + e);
                        UtilToast.getInstance().showDragonError("上传头像失败") ;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PrintLog.e("上传头像onResponse:" + response);
                        Type type = new TypeToken<Response2>() {
                        }.getType();
                        Gson gson = new Gson();
                        Response2 response2 = gson.fromJson(response, type);
                        if ("200".equals(response2.getErrCode())) {
                            UtilToast.getInstance().showDragonSuccess("上传头像成功");
                            ivHead.setImageURI(response2.getData());
                        } else {
                            UtilToast.getInstance().showDragonError("response2.getMessage()"); ;
                        }
                    }
                });
            } else {
                repuestPermission(Constant.STORAGE_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPhotographClick();
            } else {
                UtilToast.getInstance().showDragonInfo("请开启相机权限"); ;
            }
        }
        if (requestCode == Constant.STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}
