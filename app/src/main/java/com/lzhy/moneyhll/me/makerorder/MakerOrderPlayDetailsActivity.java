package com.lzhy.moneyhll.me.makerorder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.WriterException;
import com.lzhy.moneyhll.MapActivity;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.model.MarkerOrderModel;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.zxing.encoding.EncodingHandler;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by cmm on 2016/11/26.
 * 创客游玩订单详情
 */

public class MakerOrderPlayDetailsActivity extends MySwipeBackActivity implements View.OnClickListener {

    private TextView tvOrderCode;
    private TextView tvOrderNum;
    private SimpleDraweeView ivImage;
    private TextView tvProjectName;
    private TextView tvPayment;
    private TextView price_symbol;
    private TextView tvOrderTime;
    private TextView tvAddress;
    private TextView tvPhoneNum;
    private ImageView ivPhone;
    private TextView tvNumberCode;
    private SimpleDraweeView qrCodeImage;
    private TextView tvFinish;
    private ImageView iv_ke_fu;

    private String phoneurl;
    private MarkerOrderModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker_order_detail);

        initTitlebar();
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        model = (MarkerOrderModel) getIntent().getExtras().getSerializable("MakerProjectModel");
        tvOrderCode = (TextView) findViewById(R.id.tv_order_number);
        tvOrderNum = (TextView) findViewById(R.id.tv_copies);
        ivImage = (SimpleDraweeView) findViewById(R.id.image);
        tvProjectName = (TextView) findViewById(R.id.project_name);
        tvPayment = (TextView) findViewById(R.id.project_price);
        price_symbol = (TextView) findViewById(R.id.price_symbol);

        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvPhoneNum = (TextView) findViewById(R.id.tv_phone);
        ivPhone = (ImageView) findViewById(R.id.iv_phone);
        tvNumberCode = (TextView) findViewById(R.id.number_code);
        qrCodeImage = (SimpleDraweeView) findViewById(R.id.two_dimension_code);
        tvFinish = (TextView) findViewById(R.id.tv_finsh);
        iv_ke_fu = (ImageView) findViewById(R.id.iv_ke_fu);

        iv_ke_fu.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        ivPhone.setOnClickListener(this);
        upDataView();
    }

    private void upDataView() {
        tvOrderCode.setText("订单号：" + model.orderCoding);
        tvOrderNum.setText("份数：" + model.quantity);
        tvProjectName.setText(model.projectName);
        tvPayment.setText(String.format("%.2f", model.totalAmount) + "");
        price_symbol.setText("已付¥");

        tvOrderTime.setText("下单时间：" + model.createTime);
        tvAddress.setText(model.address);
        if (model.consumerHotline == null) {
            ivPhone.setVisibility(View.GONE);
            tvPhoneNum.setVisibility(View.GONE);
        } else {
            ivPhone.setVisibility(View.VISIBLE);
            tvPhoneNum.setVisibility(View.VISIBLE);
            tvPhoneNum.setText("电话：" + model.consumerHotline);
        }

        if (model.picture1 != null) {
            ivImage.setImageURI(model.picture1);
        }
        if (model.status == 2) {
            tvFinish.setText("未使用");
            tvFinish.setBackgroundResource(R.drawable.bg_bottom_rad_r5dp);
        } else if (model.status == 3) {
            tvFinish.setText("已使用");
            tvFinish.setBackgroundResource(R.drawable.bg_bottom_green_r5dp);
        }else if (model.status == 4) {
            tvFinish.setText("已完结");
            tvFinish.setBackgroundResource(R.drawable.bg_bottom_green_r5dp);
        }

        tvNumberCode.setText(model.checkCode);

        /**
         * @Author xu
         * @说明： 生成二维码
         */

        String order_num = model.checkCode;
        try {
            Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
            Matrix m = new Matrix();

            m.setRotate(0, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap bmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            Bitmap bitmap = EncodingHandler.createQRCode(bmp, order_num, 300, 300);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("订单详情");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //地址
            case R.id.tv_address:
                Intent intent = new Intent(MakerOrderPlayDetailsActivity.this, MapActivity.class);
                intent.putExtra("lat", model.latitude + "");
                intent.putExtra("lng", model.longitude + "");
                intent.putExtra("name", model.projectName);

                startActivity(intent);
                break;
            //电话
            case R.id.iv_phone:
                phoneurl = "tel:" + model.consumerHotline;

                    CommonUtil.doCallPhone(MakerOrderPlayDetailsActivity.this, phoneurl);
                break;
            case R.id.iv_ke_fu:
                phoneurl = Constant.PHONE_KEFU;

                    CommonUtil.doCallPhone(MakerOrderPlayDetailsActivity.this, phoneurl);
                break;
        }
    }
}
