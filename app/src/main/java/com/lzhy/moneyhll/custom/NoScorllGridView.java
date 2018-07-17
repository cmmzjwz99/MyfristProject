package com.lzhy.moneyhll.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by DeathWinder on 2016/7/13.
 */
public class NoScorllGridView extends GridView {
    public NoScorllGridView(Context context) {
        super(context);
        init();
    }

    public NoScorllGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoScorllGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;//禁止Gridview进行滑动
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setListViewHeightBasedOnChildren(int col) {
        GridView listView = NoScorllGridView.this;
    // 获取listview的adapter
        BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
    // 固定列宽，有多少列
        int totalHeight = 0;
    // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
    // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
    // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
    // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }
    // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
    // 设置高度
        params.height = totalHeight+100;
    // 设置参数
        listView.setLayoutParams(params);
    }
}
