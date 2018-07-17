package com.lzhy.moneyhll.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.home.beans.LongBean;
import com.lzhy.moneyhll.imgloader.AsyncImageLoader;
import com.lzhy.moneyhll.utils.CommonUtil;

import java.util.List;

public final class HQRouteAdapter extends BaseAdapter  {
    private List<LongBean> mList;
    private LayoutInflater mInflater;

    private AsyncImageLoader imageLoader;
    public String[] URLS;

    private Context mContext;

    private int resId;

    private int mTotalNum;

    public HQRouteAdapter(Context context, List<LongBean> longBeanList, ListView listView, int id, int totalNum) {
        mList = longBeanList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        resId = id;
        mTotalNum = totalNum;
        URLS = new String[mList.size()];//接收图片url
        for (int i = 0; i < mList.size(); i++) {
            URLS[i] = mList.get(i).newsIconUrl;
        }
        imageLoader = new AsyncImageLoader(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(resId, null);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.id_icon);
            viewHolder.title = (TextView) convertView.findViewById(R.id.id_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.icon.setImageResource(R.mipmap.hqroute);
        String url = mList.get(position).newsIconUrl;

        viewHolder.icon.setTag(url);
        viewHolder.title.setText(mList.get(position).newsTitle);
        CommonUtil.setViewHeight(mContext, viewHolder.icon);


        String imgUrl = url;
        // 给 ImageView 设置一个 tag
        viewHolder.icon.setTag(imgUrl);
        // 预设一个图片
        viewHolder.icon.setImageResource(R.mipmap.hqroute);

        if (!TextUtils.isEmpty(imgUrl)) {
            Bitmap bitmap = imageLoader.loadImage(viewHolder.icon, imgUrl);
            if (bitmap != null) {
                viewHolder.icon.setImageBitmap(bitmap);
            }
        }

        convertView.setId(mList.get(position).newsId);//设置ID
        return convertView;
    }

    class ViewHolder {
        public TextView title;
        public ImageView icon;
    }


}
