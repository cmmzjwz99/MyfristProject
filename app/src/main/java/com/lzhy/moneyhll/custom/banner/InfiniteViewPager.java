package com.lzhy.moneyhll.custom.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * A {@link ViewPager} that allows pseudo-infinite paging with a wrap-around effect. Should be used with an {@link
 */
public class InfiniteViewPager extends ViewPagerEx {

    private boolean isCanScroll = true;

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void scrollTo(int x, int y) {
        if(isCanScroll) {
            super.scrollTo(x, y);
        }
    }
}
