package com.lzhy.moneyhll.me.order;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.WriterException;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.model.ProjectOrderModel;
import com.zxing.encoding.EncodingHandler;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by cmm on 2016/11/4.
 * 未使用项目订单详情
 */
public class NotUseOrderDetailsActivity extends MySwipeBackActivity {
    private ProjectOrderModel model;

    private SimpleDraweeView mDraweeView;
    private TextView projectName;
    private TextView state;
    private TextView peopleNum;
    private TextView playTime;
    private TextView price;
    private TextView number;
    private TextView allMoney;

    private TextView tv_code_number;
    private TextView tv_use_time;


    private ImageView qrCodeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_use_order_detail);

        initView();
        initTitlebar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        getIntent().getExtras().getSerializable("ProjectOrderModel");
        model = (ProjectOrderModel) getIntent().getExtras().getSerializable("ProjectOrderModel");

        mDraweeView = (SimpleDraweeView) findViewById(R.id.image);
        projectName = (TextView) findViewById(R.id.tv_project_name);
        state = (TextView) findViewById(R.id.tv_state);
        peopleNum = (TextView) findViewById(R.id.tv_men_number);
        playTime = (TextView) findViewById(R.id.tv_play_time);
        price = (TextView) findViewById(R.id.tv_money);
        number = (TextView) findViewById(R.id.tv_number);
        allMoney = (TextView) findViewById(R.id.tv_all_money);

        tv_code_number=(TextView) findViewById(R.id.tv_code_number);
        tv_use_time=(TextView) findViewById(R.id.tv_use_time);


        mDraweeView.setImageURI(Uri.parse(model.imageUrl));
        projectName.setText(model.title);
        state.setText("未使用");
        // peopleNum.setText(info.quantity+"人");
        playTime.setText(model.enjoytime + model.salesUnit);
        price.setText(model.adultPrice + "元");
        number.setText(model.quantity + "");
        allMoney.setText(model.payAmount +"元");
        tv_code_number.setText(model.checkCode);
        /**
        * @Author xu
        * @说明： 生成二维码
        */
        qrCodeImage = (ImageView) findViewById(R.id.ECoder_image);
        String order_num = model.checkCode;

        try {
            Bitmap bm= BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
            Matrix m = new Matrix();

            m.setRotate(0, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap bmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            Bitmap bitmap = EncodingHandler.createQRCode(bmp,order_num,300,300);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        tv_use_time.setText("使用时间："+model.startTime+" 至 "+model.endTime);
    }


    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setLeftTextButton("返回",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.setTitle("订单详情");
    }
}
