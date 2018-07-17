package com.lzhy.moneyhll.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class PasswordView extends EditText {

    private Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF strokeRect = new RectF();

    private int mCurSize;

    public PasswordView(Context context) {
        super(context);
        init(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundColor(Color.TRANSPARENT);
        setSingleLine();
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        // 内容区
        dividerPaint.setStyle(Paint.Style.STROKE);
        dividerPaint.setColor(0xff999999);
        dividerPaint.setStrokeWidth(4);
        strokeRect.set(0, 0, width, height);
        canvas.drawRoundRect(strokeRect, 4, 4, dividerPaint);

        // 分割线
        dividerPaint.setStrokeWidth(2);
        for (int i = 1; i < 6; i++) {
            float x = width * i / 6;
            canvas.drawLine(x, 0, x, height, dividerPaint);
        }

        //密码
        dividerPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mCurSize; i++) {
            float cx = (2 * i + 1) * width / (2 * 6);
            canvas.drawCircle(cx, height / 2, 10, dividerPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        mCurSize = text.length();
        invalidate();
    }
}
