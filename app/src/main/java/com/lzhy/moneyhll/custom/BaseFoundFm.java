package com.lzhy.moneyhll.custom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshStickListView;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class BaseFoundFm extends BaseFragment{
    private int onScrollRecordY = 0;
    private int lastProcessStickyTranslateY = 0;

    protected StickyScrollCallBack scrollListener;
    private StickyScrollCallListViewBack scrollListenerlist;

    protected final String TAG = "Artgoer";
    protected PullToRefreshStickListView mListView;
    protected StickyListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_goods_order, container, false);
        findViews();
        return view;
    }


    private void findViews() {
        mListView = (PullToRefreshStickListView) view
                .findViewById(R.id.listview);
        mListView.setPullLoadEnabled(false);
        mListView.setScrollLoadEnabled(true);
        listView =  mListView.getRefreshableView();
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                listView.onScrollStateChanged(view, scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
//				listView.onScroll(view, firstVisibleItem, visibleItemCount,
//						totalItemCount);
            }
        });

        scrollListenerlist = new StickyScrollCallListViewBack() {

            @Override
            public void onScrollChanged(int translateY) {
                onScrollRecordY = translateY;
                if (translateY == Integer.MIN_VALUE
                        || translateY == lastProcessStickyTranslateY) {
                    return;
                }
                lastProcessStickyTranslateY = translateY;
                mListView.setTranslationY(translateY);
                scrollListener.onScrollRecord(onScrollRecordY);
            }
        };
//		listView.setScrollCallBack(scrollListener, scrollListenerlist);
        listView.setDivider(null);
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setSelector(R.color.white);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (listView != null) {// onpasue记录值，onremue 设置值
//				listView.isPause(false);
//				listView.isUser(false);
                if (scrollListener != null)
                    scrollListener.onScrollSetting(this);
            }
        } else {
            // 相当于Fragment的onPause
            if (listView != null) {
//				listView.isPause(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface StickyScrollCallListViewBack {
        public void onScrollChanged(int scrollY);
    }

    @Override
    public void setViewPegeHight(int scrollViewY, int pageHight) {
        super.setViewPegeHight(scrollViewY, pageHight);
        this.onScrollRecordY = scrollViewY;
        this.lastProcessStickyTranslateY = scrollViewY;
        if (mListView != null)
            mListView.setTranslationY(scrollViewY);
    }

    public interface StickyScrollCallBack {

        public void onScrollRecord(int scrollY);

        public void onScrollSetting(BaseFragment fragment);

    }
}
