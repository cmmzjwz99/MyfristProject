package com.lzhy.moneyhll.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 无滚动效果的GridView
 * 
 * @copyright 杭州艺藏文化艺术有限公司
 * 
 * @author zhangjie
 * 
 * @date 2015年3月26日 上午10:03:01
 * 
 */
public class NoScrollListView extends ListView {

	public NoScrollListView(Context context) {
		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
