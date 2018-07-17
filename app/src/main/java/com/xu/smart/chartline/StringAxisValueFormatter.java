package com.xu.smart.chartline;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/************************************************************
*创建者;龙之游 @ xu 596928539@qq.com
*时间:2016/12/17 11:09
*注释: 对字符串类型的坐标轴标记进行格式化
************************************************************/
public class StringAxisValueFormatter implements IAxisValueFormatter {

    //区域值
    private List<String> mStrs;

    /**
     * 对字符串类型的坐标轴标记进行格式化
     * @param strs
     */
    public StringAxisValueFormatter(List<String> strs){
        this.mStrs =strs;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        return mStrs.get((int)v);
    }
}
