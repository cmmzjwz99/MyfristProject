package com.lzhy.moneyhll.me.mine.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseFragment;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.InComeModel;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.CommonUtil;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.SharePrefenceUtils;
import com.ta.utdid2.android.utils.StringUtils;
import com.xu.smart.chartline.MPChartHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.utils.CommonUtil.getScreenWidthPixels;

/**
 * Created by Administrator on 2017/1/10 0010.
 * 我的收益
 */

public class MyEarningFm extends BaseFragment {
    private Context mContext;
    private SimpleDraweeView head_image;
    private TextView text_type;
    private TextView all_number;
    private TextView can_extract_number;
    private TextView to_extract;
    private RelativeLayout relative, relativelayout;

    //折线图
    private LineChart lineChart;

    private ArrayList<String> xValues;
    private ArrayList<String> yValues;
    private List<String> xAxisValues;
    private List<Float> yAxisValues;

    private String payType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fm_my_earning, container, false);
        initView();
        LoadingData();
        return view;
    }

    private void initView() {
        mContext = getContext();
        payType = getArguments().getString("payType");
        head_image = (SimpleDraweeView) view.findViewById(R.id.head_image);
        text_type = (TextView) view.findViewById(R.id.text_type);
        all_number = (TextView) view.findViewById(R.id.all_number);
        can_extract_number = (TextView) view.findViewById(R.id.can_extract_number);
        to_extract = (TextView) view.findViewById(R.id.to_extract);
        relative = (RelativeLayout) view.findViewById(R.id.relative);
        relativelayout = (RelativeLayout) view.findViewById(R.id.relativelayout);

        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        lineChart.setNoDataText("暂无收益");
        ViewGroup.LayoutParams mLayoutParams = relativelayout.getLayoutParams();
        mLayoutParams.height = getScreenWidthPixels((Activity) mContext) * 37 / 75;
        view.setLayoutParams(mLayoutParams);

        if ("pears".equals(payType)) {
            text_type.setPadding(0, CommonUtil.dip2px(mContext, 15), 0, 0);
            all_number.setPadding(0, CommonUtil.dip2px(mContext, 18), 0, 0);
            all_number.setText("0");
            text_type.setText("总收益（龙珠）");
            relative.setVisibility(View.GONE);
        } else {
            text_type.setPadding(0, CommonUtil.dip2px(mContext, 10), 0, 0);
            all_number.setPadding(0, CommonUtil.dip2px(mContext, 13), 0, 0);
            all_number.setText("0.00");
            text_type.setText("总收益（龙币）");
            relative.setVisibility(View.VISIBLE);
        }
        to_extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TixianActivity.class);
                startActivity(intent);
            }
        });

        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
    }

    private void LoadingData() {
        String myIncomeUrl = UrlAPI.getMyIncomeUrl(UserInfoModel.getInstance().getId(), payType);
        PrintLog.e("我的收益URL:" + myIncomeUrl);
        OkHttpUtils.get().url(myIncomeUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("我的收益onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("我的收益onResponse:" + response);

                Type type = new TypeToken<Response1<InComeModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<InComeModel> resp = gson.fromJson(response, type);
                if ("200".equals(resp.getErrCode()) || "-1000".equals(resp.getErrCode())) {
                    updataView(resp.getData());
                } else {
                    Toast.makeText(mContext, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updataView(InComeModel data) {
        all_number.setText(data.totalRevenue);
        can_extract_number.setText(data.getIncome);
        if (!StringUtils.isEmpty(data.avatar)) {
            head_image.setImageURI(data.avatar);
        }
        if (!"pears".equals(payType)) {
            SharePrefenceUtils.put(mContext, "getInCome", CommonUtil.FloattoInt(Float.valueOf(data.getIncome)));
        }
        if (data.dataList != null && data.dataList.size() > 0) {
            for (int i = 0; i < data.dataList.size(); i++) {
                yValues.add(data.dataList.get(i).amount + "");
                xValues.add(data.dataList.get(i).createTime);
            }
            initChartData();
        }
    }

    private void initChartData() {
        lineChart.setVisibility(View.VISIBLE);

        xAxisValues = new ArrayList<>();
        yAxisValues = new ArrayList<>();

        //获取当前时间的前一天
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date nextDay = getNextDay(new Date(System.currentTimeMillis()));
        String str = formatter.format(nextDay);

        if (xValues != null) {
            if (xValues.size() == 1) {
                xValues.add(str);
                for (int i = 0; i < xValues.size(); i++) {
                    xAxisValues.add(xValues.get(i));
                }
            } else {
                for (int i = 0; i < xValues.size(); i++) {
                    xAxisValues.add(xValues.get(i));
                }
            }
        }


        if (yValues != null) {
            if (yValues.size() == 1) {
                yValues.add("0.0");
                for (int i = 0; i < yValues.size(); i++) {
                    float y = Float.parseFloat(yValues.get(i));
                    yAxisValues.add(y);
                }
            } else {
                for (int i = 0; i < yValues.size(); i++) {
                    float y = Float.parseFloat(yValues.get(i));
                    yAxisValues.add(y);
                }
            }
        }
        if (yAxisValues == null || xAxisValues == null) {
            return;
        }
        MPChartHelper.setLineChart(lineChart, xAxisValues, yAxisValues, "", true);
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }
//
//    //设置可见时加载数据
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            status = getArguments().getInt("status");
//            LoadingData();
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }
}
