package com.lzhy.moneyhll.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by Administrator on 2017/1/5 0005.
 * 文字和左边图片一起居中的RadioButton
 */

public class CenterRadioButton extends RadioButton {
    public CenterRadioButton(Context context) {
        super(context);
    }

    public CenterRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取设置的图片
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            //第一个是left
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                //获取文字的宽度
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                setPadding((int) (getWidth() - bodyWidth) / 2, 0, (int) (getWidth() - bodyWidth) / 2, 0);
                canvas.translate(0, 0);
            }
        }
        super.onDraw(canvas);
    }

}
