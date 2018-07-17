package com.lzhy.moneyhll.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xu on 2016/11/5.
 */

public final class DynamicTextView extends TextView {
    public DynamicTextView(Context context) {
        super(context);
    }

    public DynamicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
