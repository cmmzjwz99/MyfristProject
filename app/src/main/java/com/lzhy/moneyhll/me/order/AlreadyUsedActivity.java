package com.lzhy.moneyhll.me.order;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.model.CommentModel;
import com.lzhy.moneyhll.model.ProjectOrderModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;


/**
 * Created by cmm on 2016/11/5.
 * 已使用项目订单详情
 */
public class AlreadyUsedActivity extends MySwipeBackActivity {

    private RelativeLayout rlBK;
    private EditText etSay;
    private TextView tvComment;
    private ProjectOrderModel model;

    private SimpleDraweeView mDraweeView;
    private TextView projectName;
    private TextView state;
    private TextView peopleNum;
    private TextView playTime;
    private TextView price;
    private TextView number;
    private TextView allMoney;

    private TextView text;

    private int pid;
    private int star;


    private ImageButton star1, star2, star3, star4, star5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_project_order_detail);

        initTitlebar();
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {
        model = (ProjectOrderModel) getIntent().getExtras().getSerializable("ProjectOrderModel");
        pid = model.id;

        tvComment = (TextView) findViewById(R.id.tv_show_comment);
        mDraweeView = (SimpleDraweeView) findViewById(R.id.image);
        projectName = (TextView) findViewById(R.id.tv_project_name);
        state = (TextView) findViewById(R.id.tv_state);
        peopleNum = (TextView) findViewById(R.id.tv_men_number);
        playTime = (TextView) findViewById(R.id.tv_play_time);
        price = (TextView) findViewById(R.id.tv_money);
        number = (TextView) findViewById(R.id.tv_number);
        allMoney = (TextView) findViewById(R.id.tv_all_money);
        star1= (ImageButton) findViewById(R.id.star1);
        star2= (ImageButton) findViewById(R.id.star2);
        star3= (ImageButton) findViewById(R.id.star3);
        star4= (ImageButton) findViewById(R.id.star4);
        star5= (ImageButton) findViewById(R.id.star5);

        rlBK = (RelativeLayout) findViewById(R.id.rl_bian_kuang);
        etSay = (EditText) findViewById(R.id.et_whay_you_say);
        text = (TextView) findViewById(R.id.text);
        rlBK.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);

        if (model.projectStatus == 1) {
            rlBK.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            setComment();
        } else {
            rlBK.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            setViewOnClick();
        }


        mDraweeView.setImageURI(Uri.parse(model.imageUrl));
        projectName.setText(model.title);
        state.setText("已使用");
        peopleNum.setText(model.quantity + "人");
        playTime.setText(model.enjoytime + model.salesUnit);
        price.setText(model.adultPrice + "元");
        number.setText(model.quantity + "");
        allMoney.setText(model.payAmount + "元");

    }

    private void setViewOnClick() {
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.mipmap.icon_pingjia);
                star2.setImageResource(R.mipmap.icon_pingjia_normal);
                star3.setImageResource(R.mipmap.icon_pingjia_normal);
                star4.setImageResource(R.mipmap.icon_pingjia_normal);
                star5.setImageResource(R.mipmap.icon_pingjia_normal);
                star = 1;
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.mipmap.icon_pingjia);
                star2.setImageResource(R.mipmap.icon_pingjia);
                star3.setImageResource(R.mipmap.icon_pingjia_normal);
                star4.setImageResource(R.mipmap.icon_pingjia_normal);
                star5.setImageResource(R.mipmap.icon_pingjia_normal);
                star = 2;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.mipmap.icon_pingjia);
                star2.setImageResource(R.mipmap.icon_pingjia);
                star3.setImageResource(R.mipmap.icon_pingjia);
                star4.setImageResource(R.mipmap.icon_pingjia_normal);
                star5.setImageResource(R.mipmap.icon_pingjia_normal);
                star = 3;
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.mipmap.icon_pingjia);
                star2.setImageResource(R.mipmap.icon_pingjia);
                star3.setImageResource(R.mipmap.icon_pingjia);
                star4.setImageResource(R.mipmap.icon_pingjia);
                star5.setImageResource(R.mipmap.icon_pingjia_normal);
                star = 4;
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.mipmap.icon_pingjia);
                star2.setImageResource(R.mipmap.icon_pingjia);
                star3.setImageResource(R.mipmap.icon_pingjia);
                star4.setImageResource(R.mipmap.icon_pingjia);
                star5.setImageResource(R.mipmap.icon_pingjia);
                star = 5;
            }
        });

        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whatSay = etSay.getText().toString();
                if (TextUtils.isEmpty(whatSay)) {
                    Utils.toast(AlreadyUsedActivity.this, "请填写发表内容");
                    return;
                }
                if (star == 0) {
                    Utils.toast(AlreadyUsedActivity.this, "请选择星级");
                    return;
                }
                setComment1();
            }
        });
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

    private void setComment() {
        //项目订单
        String projectCommentUrl = UrlAPI.getProjectCommentUrl(pid, UserInfoModel.getInstance().getId(), star, etSay.getText().toString());

        PrintLog.e("项目订单发表评论URL:" + projectCommentUrl);
        OkHttpUtils.get().url(projectCommentUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                PrintLog.e("项目订单发表评论:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("项目订单发表评论:" + response);
                Type type = new TypeToken<Response1<CommentModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<CommentModel> res = gson.fromJson(response, type);
                if ("200".equals(res.getErrCode())) {
                    text.setText(res.getData().content);
                    if (res.getData().star == 1) {
                        star1.setImageResource(R.mipmap.icon_pingjia);
                        star2.setImageResource(R.mipmap.icon_pingjia_normal);
                        star3.setImageResource(R.mipmap.icon_pingjia_normal);
                        star4.setImageResource(R.mipmap.icon_pingjia_normal);
                        star5.setImageResource(R.mipmap.icon_pingjia_normal);
                    }
                    if (res.getData().star == 2) {
                        star1.setImageResource(R.mipmap.icon_pingjia);
                        star2.setImageResource(R.mipmap.icon_pingjia);
                        star3.setImageResource(R.mipmap.icon_pingjia_normal);
                        star4.setImageResource(R.mipmap.icon_pingjia_normal);
                        star5.setImageResource(R.mipmap.icon_pingjia_normal);
                    }
                    if (res.getData().star == 3) {
                        star1.setImageResource(R.mipmap.icon_pingjia);
                        star2.setImageResource(R.mipmap.icon_pingjia);
                        star3.setImageResource(R.mipmap.icon_pingjia);
                        star4.setImageResource(R.mipmap.icon_pingjia_normal);
                        star5.setImageResource(R.mipmap.icon_pingjia_normal);
                    }
                    if (res.getData().star == 4) {
                        star1.setImageResource(R.mipmap.icon_pingjia);
                        star2.setImageResource(R.mipmap.icon_pingjia);
                        star3.setImageResource(R.mipmap.icon_pingjia);
                        star4.setImageResource(R.mipmap.icon_pingjia);
                        star5.setImageResource(R.mipmap.icon_pingjia_normal);
                    }
                    if (res.getData().star == 5) {
                        star1.setImageResource(R.mipmap.icon_pingjia);
                        star2.setImageResource(R.mipmap.icon_pingjia);
                        star3.setImageResource(R.mipmap.icon_pingjia);
                        star4.setImageResource(R.mipmap.icon_pingjia);
                        star5.setImageResource(R.mipmap.icon_pingjia);
                    }
                }

            }
        });
    }

    private void setComment1() {
        //项目订单
        String projectCommentUrl = UrlAPI.getProjectCommentUrl(pid, UserInfoModel.getInstance().getId(), star, etSay.getText().toString());

        PrintLog.e("项目订单发表评论URL:" + projectCommentUrl);
        OkHttpUtils.get().url(projectCommentUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("项目订单发表评论:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("项目订单发表评论:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 res = gson.fromJson(response, type);
                if ("200".equals(res.getErrCode())) {
                    rlBK.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(etSay.getText().toString());
                    star1.setClickable(false);
                    star2.setClickable(false);
                    star3.setClickable(false);
                    star4.setClickable(false);
                    star5.setClickable(false);
                }
            }
        });
    }
}
