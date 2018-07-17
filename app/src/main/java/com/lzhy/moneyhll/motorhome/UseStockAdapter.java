package com.lzhy.moneyhll.motorhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.mine.bean.HistoryInfo;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.NowRollHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17 0017.
 * 使用房车劵弹窗适配器
 */

public final class UseStockAdapter extends BaseAdapter {
    List<HistoryInfo> mList;
    Context mContext;
    public List<Integer> mStrings;
    boolean isCheck;

    public UseStockAdapter(List<HistoryInfo> list, Context context) {
        mList = list;
        mContext = context;
        mStrings = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public HistoryInfo getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        isCheck = false;
        NowRollHelper helper;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_now, null, false);
            helper = new NowRollHelper(mContext, view);
            view.setTag(helper);
        } else {
            helper = (NowRollHelper) view.getTag();
        }
        if (HasSelected(mList.get(i).id)) {
            view.setAlpha(0.5f);
            isCheck = true;
        } else {
            view.setAlpha(1f);
            isCheck = false;
        }
        helper.updateView(mList.get(i));
        HasSelected(mList.get(i).id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HasSelected(mList.get(i).id)) {
                    view.setAlpha(0.5f);
                    isCheck = true;
                    mStrings.add(mList.get(i).id);
                    PrintLog.e("添加   " + mList.get(i).id);
                }
            }
        });

        return view;
    }

    private boolean HasSelected(int ticketId) {
        for (int j = 0; j < mStrings.size(); j++) {
            if (ticketId==mStrings.get(j)) {
                return true;
            }
        }
        return false;
    }

    public void clearSelect() {
        mStrings.clear();
    }
}
