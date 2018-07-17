package com.lzhy.moneyhll.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

@SuppressLint({"NewApi", "HandlerLeak"})
public class StickyListView extends ListView {
    public StickyListView(Context context) {
        this(context, null);
    }

    public StickyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOverScrollMode(OVER_SCROLL_ALWAYS);
    }


    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            // animScrollY();
        }
    }


    public int getFirstViewScrollTop() {
        if (getFirstVisiblePosition() == 0) {
            View firstView = getChildAt(0);
            if (null != firstView) {
                return -firstView.getTop();
            }
        }
        return Integer.MAX_VALUE;
    }

    class AnimUiThread extends Thread {
        private int fromPos, toPos;

        public AnimUiThread(int fromPos, int toPos) {
            this.fromPos = fromPos;
            this.toPos = toPos;
        }

        @Override
        public void run() {
            int num = 10;
            for (int i = 0; i < num; i++) {
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int tempPos = fromPos + (toPos - fromPos) * (i + 1) / num;
                Message msg = uiHandler.obtainMessage();
                msg.what = tempPos;
                msg.sendToTarget();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        uiHandler = null;
        super.onDetachedFromWindow();
    }

    private Handler uiHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int pos = msg.what;
            setSelectionFromTop(0, pos);
        }
    };
}
