package com.lzhy.moneyhll.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Hashtable;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class LongFlowLayout extends LinearLayout{
    int mLeft, mRight, mTop, mBottom;
    Hashtable<View, Position> map = new Hashtable<View, Position>();

    private int maxLine =100;
    public LongFlowLayout(Context context) {
        super(context);
    }

    private int marRight = 0;

    public LongFlowLayout(Context context, int horizontalSpacing,
                         int verticalSpacing) {
        super(context);
    }

    public void setMaxLine(int maxLine){
        this.maxLine = maxLine;
    }
    public LongFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int mCount = getChildCount();
        int mX = 0;
        int mY = 0;
        mLeft = 0;
        mRight = 0;
        mTop = 5;
        mBottom = 0;

        int j = 0;

        View lastview = null;
        int currentTop = 0;
        int currentLine = 0;
        int showBootom = 0;
        for (int i = 0; i < mCount; i++) {
            final View child = getChildAt(i);

            child.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int childw = child.getMeasuredWidth();
            int childh = child.getMeasuredHeight();
            mX += childw;
            if (i > 0)
                mX += mMarginRight;

            Position position = new Position();
            mLeft = getPosition(i - j, i);
            mRight = mLeft + child.getMeasuredWidth();
            if (mX >= mWidth) {
                mX = childw;
                mY += childh;
                j = i;
                mLeft = 0;
                mRight = mLeft + child.getMeasuredWidth();
                mTop = mY + mMarginTop;
            }
            mBottom = mTop + child.getMeasuredHeight();
            if(currentLine>=maxLine){
                showBootom = currentTop;
            }else{
                if(currentTop!=mBottom){
                    currentLine++;
                }
                currentTop = mBottom;
                showBootom = mBottom;
            }
            mY = mTop;
            position.left = mLeft;
//			position.top = mTop + 3;
            position.top = mTop;
            position.right = mRight;
            position.bottom = mBottom;
            map.put(child, position);
        }
        setMeasuredDimension(mWidth, showBootom);
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LinearLayout.LayoutParams(1, 1); // default of 1px spacing
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO_XBB Auto-generated method stub

        int count = getChildCount();
        int currentLine = 0;
        int currentTop = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            Position pos = map.get(child);
            if (pos != null) {
                if(currentTop!=pos.top){
                    currentLine++;
                }
                currentTop = pos.top;
                if(currentLine<=maxLine) {
                    child.layout(pos.left, pos.top, pos.right, pos.bottom);
                }
            } else {
                Log.i("MyLayout", "error");
            }
        }
    }

    public void setmLeftRight(int left, int right) {
        this.mLeft = left;
        this.marRight = right;
    }


    private class Position {
        int left, top, right, bottom;
    }

    /** 外编剧，默认值8 */
    private int mMarginRight = 10;
    private int mMarginTop = 10;

    /**
     * 设置左边的外边据
     *
     *            单位px
     */
    public void setMarginRight(int right) {
        mMarginRight = right;
    }

    /**
     * 设置左边的外边据
     *
     * @param top
     *            单位px
     */
    public void setMarginTop(int top) {
        mMarginTop = top;
    }

    public int getPosition(int IndexInRow, int childIndex) {
        if (IndexInRow > 0) {
            return getPosition(IndexInRow - 1, childIndex - 1)
                    + getChildAt(childIndex - 1).getMeasuredWidth()
                    + mMarginRight;
        }
        return getPaddingLeft();
    }
}
