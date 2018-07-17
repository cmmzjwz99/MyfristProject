package com.xu.smart.chartline;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/************************************************************
*创建者;龙之游 @ xu 596928539@qq.com
*时间:2016/12/17 11:09
*注释:
************************************************************/

public class MyValueFormatter implements IValueFormatter {

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return StringUtils.double2String(value, 2);
    }
}
