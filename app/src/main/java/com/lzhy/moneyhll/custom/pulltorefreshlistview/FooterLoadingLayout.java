package com.lzhy.moneyhll.custom.pulltorefreshlistview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lzhy.moneyhll.R;

/**
 * 这个类封装了下拉刷新的布局
 */
public class FooterLoadingLayout extends LoadingLayout {
    public static final int STATE_LOADING = 2;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    private TextView mContentView;
    private Context mContext;

    private int mState;
    private TextView text_more;

    private int widthPix;
    private boolean isLoadPull = false;

    /**
     * 构造方法
     *
     *            context
     */
    public FooterLoadingLayout(Context paramContext) {
        super(paramContext);
        init(paramContext);
    }

    /**
     * 构造方法
     *            context
     * @param attrs
     *            attrs
     */
    public FooterLoadingLayout(Context paramContext, AttributeSet attrs) {
        super(paramContext, attrs);
        init(paramContext);
    }


    private FrameLayout localRelativeLayout;

    /**
     * 初始化
     *            context
     */
    @SuppressLint("NewApi")
    private void init(Context paramContext) {
        this.mContext = paramContext;
        localRelativeLayout = (FrameLayout) LayoutInflater.from(this.mContext)
                .inflate(R.layout.xlistview_footer, null);
        addView(localRelativeLayout);
        localRelativeLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        this.mContentView = (TextView) localRelativeLayout
                .findViewById(R.id.xlistview_id_footer);
        this.text_more = ((TextView) localRelativeLayout
                .findViewById(R.id.text_more));
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) paramContext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        widthPix = dm.widthPixels;// 获取设备信息
    }

    @Override
    public void setFooterBackgroundColor(int color) {
        if (localRelativeLayout != null)
            localRelativeLayout.setBackgroundColor(color);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(
                R.layout.xlistview_footer, null);
        return container;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
    }

    @Override
    public int getContentSize() {
        View view = findViewById(R.id.xlistview_id_footer);
        if (null != view) {
            return view.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 40);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {

        super.onStateChanged(curState, oldState);
//        this.text_more.setVisibility(View.GONE);
//        if (curState == State.RESET) {
//            mState = STATE_READY;
//            text_more.setVisibility(View.GONE);
//            this.mContentView.setVisibility(View.VISIBLE);
//            return;
//        } else if (curState != State.REFRESHING) {
//            mState = STATE_READY;
//            text_more.setVisibility(View.GONE);
//            this.mContentView.setVisibility(View.VISIBLE);
//        }else if (curState != State.NO_MORE_DATA) {
//            mState = STATE_READY;
//            text_more.setVisibility(View.VISIBLE);
//            this.mContentView.setVisibility(View.GONE);
//        }

//        if (curState == State.RESET) {
//            mState = STATE_READY;
//            text_more.setVisibility(View.GONE);
//            this.mContentView.setVisibility(View.GONE);
//            return;
//        } else if (curState != State.REFRESHING) {
//            mState = STATE_READY;
//
//        }
    }
    @Override
    protected void onReset() {
        System.out.println("onReset");
        mState = STATE_NORMAL;
        text_more.setVisibility(View.GONE);
        this.mContentView.setVisibility(View.INVISIBLE);
        return;
    }

    @Override
    protected void onPullToRefresh() {
        System.out.println("刷新中onPullToRefresh:");
    }

    @Override
    protected void onReleaseToRefresh() {
        System.out.println("刷新中onReleaseToRefresh:");
    }

    @Override
    protected void onRefreshing() {
        System.out.println("刷新中onRefreshing:");
        this.text_more.setVisibility(View.GONE);
        if (mState == STATE_LOADING) {
            this.mContentView.setVisibility(View.VISIBLE);
            text_more.setVisibility(View.GONE);
        } else {
            mState = STATE_LOADING;
            this.mContentView.setVisibility(View.VISIBLE);
            text_more.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNoMoreData() {
        mState = STATE_NORMAL;
        text_more.setVisibility(View.GONE);
        this.mContentView.setVisibility(View.GONE);
        if(isShowMore) {
            if (!isLoadPull) {
                text_more.setVisibility(View.VISIBLE);
            } else {
                text_more.setVisibility(View.VISIBLE);
            }
        }else{
            text_more.setVisibility(View.GONE);
        }
    }

    // public void onNoShowMoreData() {
    // mState = STATE_NORMAL;
    // this.mContentView.setVisibility(View.GONE);
    // text_more.setVisibility(View.GONE);
    // }

    public void setLoadPull(boolean isLoadPull) {
        this.isLoadPull = isLoadPull;
    }
    @Override
    public void setHanderBackgroundColor(int color) {
        // TODO_XBB Auto-generated method stub

    }

}

