package com.lzhy.moneyhll.me.loginOrRegister;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseFoundFm;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.custom.NoScrollListView;
import com.lzhy.moneyhll.custom.dialog.BaseDialog;
import com.lzhy.moneyhll.custom.dialog.ChoosePicPop;
import com.lzhy.moneyhll.home.MainActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.home.dragonball.ActivityDragonBall;
import com.lzhy.moneyhll.me.maker.IamMakerActivity;
import com.lzhy.moneyhll.me.mine.AddressActivity;
import com.lzhy.moneyhll.me.mine.ChongzhiActivity;
import com.lzhy.moneyhll.me.mine.MakeDragonBallActivity;
import com.lzhy.moneyhll.me.mine.MotorHomeRollActivity;
import com.lzhy.moneyhll.me.mine.MotorhomeAppointActivity;
import com.lzhy.moneyhll.me.mine.ScanActivity;
import com.lzhy.moneyhll.me.mine.bean.HistoryInfo;
import com.lzhy.moneyhll.me.mine.bean.UesrBean;
import com.lzhy.moneyhll.me.mine.give.GiveActivity;
import com.lzhy.moneyhll.me.mine.userInfo.MineActivity;
import com.lzhy.moneyhll.me.mine.wallet.TixianActivity;
import com.lzhy.moneyhll.me.mine.wallet.WalletActivity;
import com.lzhy.moneyhll.me.order.AppointmentActivity;
import com.lzhy.moneyhll.me.order.MyOrderActivity;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.Base64;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;
import com.lzhy.moneyhll.utils.UtilToast;
import com.lzhy.moneyhll.viewhelper.NowRollHelper;
import com.ta.utdid2.android.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.lzhy.moneyhll.LtApplication.isOtherDevice;
import static com.lzhy.moneyhll.api.UrlAPI.HOST_URL;
import static com.lzhy.moneyhll.constant.Constant.CHOOSE_PICTURE;
import static com.lzhy.moneyhll.constant.Constant.CROP_SMALL_PICTURE;
import static com.lzhy.moneyhll.constant.Constant.TAKE_PICTURE;
import static com.lzhy.moneyhll.home.MainActivity.tabHome;
import static com.lzhy.moneyhll.home.MainActivity.tabMine;
import static com.lzhy.moneyhll.utils.UtilApp.exitLogin;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.gonaLogin;
import static com.lzhy.moneyhll.utils.UtilFile.savePicture;

/**
 * 我的
 */
public final class MineFm extends BaseFoundFm implements AdapterView.OnItemClickListener, ItemViewListener {

    private SimpleDraweeView ivHead;
    private ChoosePicPop mPicPop;

    private NoScorllGridView gvCenter;
    private View view;
    private TextView tvCarTicket;
    private TextView tvPearlNum;
    private TextView tvCouponNum;
    private TextView tvNickName;
    private TextView tvRecommendCode;

    private LinearLayout llCenter;
    private TextView tvLB;
    private TextView tvMR;
    private LinearLayout llLB, ll_fcq;
    private MyAdapter adapter;

    private LongAdapter mAdapter;
    private Context mContext;
    private List<HistoryInfo> mList;

    private BaseDialog dismiss1;
    private List<Map<String, Object>> mMapList1;

    private final static String[] TITLES = new String[]{"房车预约", "我的预约", "我的订单", "我的房车劵",
            "龙珠充值", "个人信息", "龙币提现", "赠送",
            "收货地址", "我要赚龙珠", "我是创客", "扫一扫", "退出登录"};
    private final static int[] ICONS = new int[]{
            R.mipmap.icon_car, R.mipmap.iconyuyue, R.mipmap.icon_dingdan, R.mipmap.icon_juan,
            R.mipmap.icon_chongzhi, R.mipmap.icon_mine, R.mipmap.icon_tixian, R.mipmap.icon_give,
            R.mipmap.icon_id, R.mipmap.icon_yuan, R.mipmap.ic_maker1, R.mipmap.iconfont_scan, R.mipmap.icon_tuichu};

    private final static String[] VIP_TITLES = new String[]{"房车预约", "龙珠商城", "钱包", "个人信息",
            "我的预约", "我的订单", "我的房车劵", "龙珠充值",
            "赠送", "收货地址", "我是创客", "扫一扫", "退出登录"};
    private final static int[] VIP_ICONS = new int[]{
            R.mipmap.icon_car, R.mipmap.iconfont_lzsc, R.mipmap.iconfont_n3, R.mipmap.icon_mine,
            R.mipmap.iconyuyue, R.mipmap.icon_dingdan, R.mipmap.icon_juan, R.mipmap.icon_chongzhi,
            R.mipmap.icon_give, R.mipmap.icon_id, R.mipmap.ic_maker1, R.mipmap.iconfont_scan, R.mipmap.icon_tuichu};

    protected ListView mListView;
    private NoScrollListView llCarTicket;
    private LinearLayout llCT;
    public static boolean is_save;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        initView(view);
        initAdapter();
        return view;
    }

    private void AddData() {
        for (int i = 0; i < TITLES.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", ICONS[i]);
            map.put("text", TITLES[i]);
            mMapList1.add(map);
        }
    }

    private void AddVipData() {
        for (int i = 0; i < VIP_TITLES.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", VIP_ICONS[i]);
            map.put("text", VIP_TITLES[i]);
            mMapList1.add(map);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (is_save) {
            UtilToast.getInstance().showDragonSuccess("个人信息保存成功");

            is_save = false;
        }
        if (!UserInfoModel.getInstance().isLogin()) {
            if (this.isVisible()) {
                Toast.makeText(mContext, "您的账号在其他地方登录!!!", Toast.LENGTH_SHORT).show();
            }
        } else {

            initUserInfo();
            initNowRoll(true);
        }
    }

    //初始化布局 ，设置点击事件
    private void initView(View view) {
        gvCenter = (NoScorllGridView) view.findViewById(R.id.gv_vip_center);
        gvCenter.setOnItemClickListener(this);
        llCenter = (LinearLayout) view.findViewById(R.id.ll_center);
        tvCarTicket = (TextView) view.findViewById(R.id.tv_roll_number);
        tvPearlNum = (TextView) view.findViewById(R.id.tv_ball_num);
        tvCouponNum = (TextView) view.findViewById(R.id.tv_cash_num);
        tvNickName = (TextView) view.findViewById(R.id.tv_nick_name);
        tvRecommendCode = (TextView) view.findViewById(R.id.tv_long_code);

        tvLB = (TextView) view.findViewById(R.id.tv_long_bi);
        tvMR = (TextView) view.findViewById(R.id.tv_motor_roll);
        llLB = (LinearLayout) view.findViewById(R.id.ll_long_bi);
        ll_fcq = (LinearLayout) view.findViewById(R.id.ll_mine_fcq);
        //房车卷
        llCT = (LinearLayout) view.findViewById(R.id.ll_carTicket);
        TextView tvMore = (TextView) view.findViewById(R.id.tv_more);
        mContext = getActivity();
        mList = new ArrayList<>();
        mMapList1 = new ArrayList<>();
        llCarTicket = (NoScrollListView) view.findViewById(R.id.ll_car_ticket);
        ivHead = (SimpleDraweeView) view.findViewById(R.id.iv_head_portrait);
        ivHead.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                View.OnClickListener itemsOnClick = new View.OnClickListener() {

                    public void onClick(View v) {
                        if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            switch (v.getId()) {
                                case R.id.photograph:
                                    if (hasPermission(Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                                        onPhotographClick();
                                    } else {
                                        repuestPermission(Constant.CAMERA_CODE, android.Manifest.permission.CAMERA);
                                        Toast.makeText(getContext(), "请开启相机权限", Toast.LENGTH_SHORT).show();
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
                        } else {
                            repuestPermission(Constant.STORAGE_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            Toast.makeText(getContext(), "请开启读写权限", Toast.LENGTH_SHORT).show();
                        }
                    }

                };

                mPicPop = new ChoosePicPop(getContext(), itemsOnClick);
                mPicPop.showAtBottom((View) gvCenter.getParent());

            }
        });

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MotorHomeRollActivity.class));
            }
        });
    }

    //获取用户信息
    private void initUserInfo() {
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
//        final String HOST_URL = "http://192.168.88.167:8090/lzhyapp/api/user/userInfo"; //tiger
        OkHttpUtils.post().url(HOST_URL + "v1/user/userInfo")//
                .addHeader("accessToken", UserInfoModel.getInstance().token)
                .addParams("req", Base64.getBase64(param))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("个3人信息URL:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                response = Base64.getFromBase64(response);
                Type type = new TypeToken<Response1<UesrBean>>() {
                }.getType();
                Gson gson = new Gson();

                Response1<UesrBean> resp = gson.fromJson(response, type);
                UesrBean data = resp.getData();

                String errCode = resp.getErrCode();
                if (!errCode.equals("200")) {
                    isOtherDevice = true;
                    if (tabMine.isChecked()) {
                        gonaLogin();//此处，如果用户已在别的地方登录  会执行
                    }
                    return;
                }

                mMapList1.clear();
                ivHead.setImageURI(data.user.avatar);
                tvPearlNum.setText(CommonUtil.FloattoInt(data.user.pearls) + "");
                if (!StringUtils.isEmpty(data.user.nickName)) {
                    tvNickName.setText(data.user.nickName);
                } else {
                    tvNickName.setText("");
                }
                /************************************************************
                 *创建者;龙之游 @ xu 596928539@qq.com
                 *修改时间:2017/2/8 10:52
                 *注释: 由于接口返回数据问题 此处需要更新用户信息
                 ************************************************************/
                UserInfoModel.getInstance().setServiceStatus(data.user.serviceStatus);
                UserInfoModel.getInstance().setUserId(data.user.id);
                Log.i("userInfo", "onResponse2222: " + data.user.id);
                if (data.user.serviceStatus == 0) {
//                    tvMR.setText("龙币");//   普通用户下，用服务商的房车权代替龙币
//                    tvMR.setBackgroundResource(R.mipmap.lb);//   普通用户下，用服务商的房车劵代替龙币
                    tvCouponNum.setText(CommonUtil.FloattoInt(data.user.coins));
                    ll_fcq.setVisibility(View.GONE);
                    tvRecommendCode.setVisibility(View.GONE);//龙码
//                    llCenter.setBackgroundColor(0xff92b767);//年后恢復
                    AddData();

                } else {//商户
                    ll_fcq.setVisibility(View.VISIBLE);
//                    tvMR.setText("房车券");
                    tvCarTicket.setText(CommonUtil.FloattoInt(data.user.carTicket) + "");
                    tvRecommendCode.setVisibility(View.VISIBLE);
                    tvRecommendCode.setText("龙码:" + data.user.recommendCode);
                    tvCouponNum.setText(CommonUtil.FloattoInt(data.user.coins));
//                    llCenter.setBackgroundColor(0xfff5a623);//年后恢復
                    AddVipData();
                }
                if (UserInfoModel.getInstance().type == 2) {
                    mMapList1.remove(11);
                } else {
                    mMapList1.remove(10);
                }


                PrintLog.e("龙码-------------------:" + UserInfoModel.getInstance().recommendCode);
                adapter.notifyDataSetChanged();
                if (data.user.carTicket != 0) {
                    llCT.setVisibility(View.VISIBLE);
                } else {
                    llCT.setVisibility(View.GONE);
                }
                UserInfoModel.getInstance().setPayPwd(data.user.paypwd);
                UserInfoModel.getInstance().sync();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMapList1 == null ? 0 : mMapList1.size();
        }

        @Override
        public Object getItem(int position) {
            return mMapList1 == null ? null : mMapList1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.item_center, null);
            }

            Map<String, Object> stringObjectMap = mMapList1.get(position);
            TextView tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title);
            ImageView ivIcon = (ImageView) convertView
                    .findViewById(R.id.iv_img);

            tvTitle.setText(stringObjectMap.get("text").toString());
            ivIcon.setImageResource(Integer.valueOf(stringObjectMap.get("image").toString()));
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Map<String, Object> itemAtPosition = (Map<String, Object>) adapterView.getItemAtPosition(position);
        switch (itemAtPosition.get("text").toString()) {

            case "房车预约"://"房车预约"
                startActivity(new Intent(getContext(), MotorhomeAppointActivity.class));
                break;
            case "我的预约"://我的预约
                startActivity(new Intent(getContext(), AppointmentActivity.class));
                break;
            case "我的订单"://我的订单
                startActivity(new Intent(getContext(), MyOrderActivity.class));
                break;
            case "龙珠商城"://龙珠商城
                startActivity(new Intent(getContext(), ActivityDragonBall.class));
                break;
            case "钱包":// "钱包":
                startActivity(new Intent(getContext(), WalletActivity.class));
                break;
            case "个人信息"://"个人信息":
                startActivity(new Intent(getContext(), MineActivity.class));
                break;
            case "我的房车劵"://"我的房车劵":
                startActivity(new Intent(getContext(), MotorHomeRollActivity.class));
                break;
            case "龙珠充值"://"龙珠充值":
                startActivity(new Intent(getContext(), ChongzhiActivity.class));
                break;
            case "龙币提现"://"龙币提现":
                Intent intent = new Intent(getContext(), TixianActivity.class);
                SharePrefenceUtils.put(getContext(), "withDrawType", "2");
                startActivity(intent);
                break;
            case "赠送"://"赠送":
                startActivity(new Intent(getContext(), GiveActivity.class));
                break;
            case "收货地址"://"收货地址":
                startActivity(new Intent(getContext(), AddressActivity.class));
                break;
            case "我要赚龙珠"://"我要赚龙珠":
                startActivityForResult(new Intent(getContext(), MakeDragonBallActivity.class), 100);
                break;
            case "我是创客"://"我是创客":
                startActivity(new Intent(getContext(), IamMakerActivity.class));
                break;
            case "扫一扫"://"扫一扫":
                startActivity(new Intent(getContext(), ScanActivity.class));
                break;
            case "退出登录"://"退出登录":
                if (dismiss1 == null) {
                    final BaseDialog.Builder builder = new BaseDialog.Builder(mContext);
                    builder.setMessage("要否退出登录？");
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exitLogin(getActivity());
                                    isOtherDevice = false;//用户自己主动退出
                                    tabHome.setClickable(true);
                                    tabHome.performClick();
                                    Log.i("stopTimeTask", "onClick: ");
                                    /************************************************************
                                     *@Author; 龙之游 @ xu 596928539@qq.com
                                     * 时间:2016/12/30 14:15
                                     * 注释: 用户自己主动退出  都要停止检查
                                     ************************************************************/
//                                    stopTimeTask();
                                    dismiss1.dismiss();
                                }
                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dismiss1.dismiss();
                                }
                            });
                    dismiss1 = builder.create();
                    dismiss1.show();
                } else {
                    dismiss1.show();
                }
                dismiss1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dismiss1 = null;
                    }
                });
                break;
        }
    }

    //房车卷
    public void initNowRoll(final boolean isrefresh) {
        String getMyMotorHomeRollUrl = UrlAPI.getMyMotorHomeRollUrl(UserInfoModel.getInstance().getId(), 1);
        PrintLog.e("当前URL:" + getMyMotorHomeRollUrl);
        OkHttpUtils.get().url(getMyMotorHomeRollUrl).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("下一步URL:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("当前URL---------------:" + response);
                Type type = new TypeToken<Response<HistoryInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Response<HistoryInfo> resp = gson.fromJson(response, type);
                List<HistoryInfo> data = resp.getData();

                PrintLog.e("当前URL------data---------:" + data);
                if (data != null) {
                    mList.clear();
                    int size = data.size();
                    size = size >= 3 ? 3 : size;
                    for (int i = 0; i < size; i++) {
                        mList.add(data.get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initAdapter() {
        adapter = new MyAdapter();
        gvCenter.setAdapter(adapter);
        gvCenter.setOnItemClickListener(this);

        mAdapter = new LongAdapter(mContext, mList, this);
        llCarTicket.setAdapter(mAdapter);
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        NowRollHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_now, null, false);
            helper = new NowRollHelper(mContext, itemView);
            itemView.setTag(helper);
        } else {
            helper = (NowRollHelper) itemView.getTag();
        }
        helper.updateView((HistoryInfo) data);//List<RollrBean> data
        return itemView;
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
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");

            File file = savePicture(photo, "haed", "111", true);
            OkHttpUtils.post().url(HOST_URL + "user/updateAvatar")
                    .addParams("id", UserInfoModel.getInstance().getId() + "").addFile("file", "haed.png", file)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    PrintLog.e("上传头像onError:" + e);
                    Toast.makeText(getContext(), "上传头像失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
                    PrintLog.e("上传头像onResponse:" + response);
                    Type type = new TypeToken<Response2>() {
                    }.getType();
                    Gson gson = new Gson();
                    Response2 response2 = gson.fromJson(response, type);
                    if ("200".equals(response2.getErrCode())) {
                        Toast.makeText(getContext(), "上传头像成功", Toast.LENGTH_SHORT).show();
                        ivHead.setImageURI(response2.getData());
                    } else {
                        Toast.makeText(getContext(), response2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPhotographClick();
            } else {
                Toast.makeText(getContext(), "请开启相机权限", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == Constant.STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}
