package com.lzhy.moneyhll.me.maker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.me.mine.ScanActivity;
import com.lzhy.moneyhll.utils.CommonUtil;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * 扫描核销结果
 */
public class VerificationResultActivity extends MySwipeBackActivity {
    private ImageView image;
    private TextView tv_result;
    private TextView tv_text;
    private Button left_btn;
    private Button right_btn;
    private ImageView iv_ke_fu;
    private String ErrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_verification_result);
        initTitlebar();
        initView();
        upDataView();
        setClick();
    }

    private void initView() {
        ErrCode = getIntent().getStringExtra("ErrCode");
        image = (ImageView) findViewById(R.id.image);
        tv_result = (TextView) findViewById(R.id.tv_result);
        tv_text = (TextView) findViewById(R.id.tv_text);
        left_btn = (Button) findViewById(R.id.left_btn);
        right_btn = (Button) findViewById(R.id.right_btn);
        iv_ke_fu = (ImageView) findViewById(R.id.iv_ke_fu);
    }

    private void upDataView() {
        if ("200".equals(ErrCode)) {
            image.setImageResource(R.mipmap.success_result);
            tv_result.setText("订单确认成功！");
            tv_result.setTextColor(0xff00ca81);
            tv_text.setText("噢耶！又成功一笔订单");
            left_btn.setText("查看订单");
            right_btn.setVisibility(View.GONE);
            iv_ke_fu.setVisibility(View.GONE);
            left_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(VerificationResultActivity.this, PlayOrderActivity.class);
                    intent.putExtra("type",2);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            right_btn.setVisibility(View.VISIBLE);
            iv_ke_fu.setVisibility(View.VISIBLE);
            image.setImageResource(R.mipmap.failure_result);
            tv_result.setText("扫码失败");
            tv_result.setTextColor(0xffff606a);
            tv_text.setText("检查网络是否通畅，如有问题请联系客服~");
            left_btn.setText("返回");
            right_btn.setText("在扫一次");
            left_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(VerificationResultActivity.this, ScanActivity.class));
                    finish();
                }
            });
        }
    }

    private void setClick() {
        iv_ke_fu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.doCallPhone(VerificationResultActivity.this, Constant.PHONE_KEFU);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        titlebar.setTitle("温馨提示");
    }
}
