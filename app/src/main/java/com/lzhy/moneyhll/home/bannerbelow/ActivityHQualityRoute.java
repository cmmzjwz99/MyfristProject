package com.lzhy.moneyhll.home.bannerbelow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshBase;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.home.adapter.HQRouteAdapter;
import com.lzhy.moneyhll.home.beans.LongBean;
import com.lzhy.moneyhll.home.data.AsyncTaskRequesJsontData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.lzhy.moneyhll.api.UrlAPI.APP_H5_HOST;
import static com.lzhy.moneyhll.api.UrlAPI.FeatureJapanShowUrl;
import static com.lzhy.moneyhll.api.UrlAPI.HQualityRouteShowUrl;
import static com.lzhy.moneyhll.home.data.GetURLString.ReadStreamOfJson;
import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 精品路线
 */
/************************************************************
 *@Author; 龙之游 @ xu 596928539@qq.com
 * 时间:2016/12/23 9:25
 * 注释: 特色日本和这个类似   特色日本没有分页  暂不做优化
 ************************************************************/
public class ActivityHQualityRoute extends MySwipeBackActivity implements AsyncTaskRequesJsontData.CallbackRequesJsontData,AbsListView.OnScrollListener {

    private int resId = R.layout.hq_route_item_layout;//布局文件

    private String urlPath;

    private ListView mListView;

    private PullToRefreshListView mPullRefreshListView;
    private BaseTitlebar baseTitlebar;
    private AsyncTaskRequesJsontData asyncTaskRequestData;

    private List<LongBean> longBeanList = new ArrayList<LongBean>();
    private List<LongBean> longBeanListTemp = new ArrayList<LongBean>();
    private int index = 1,totalNum,perPageNum,totalIndex;

    private HQRouteAdapter adapter;
    private final int startPos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hquality_route);

        //通过intent 判断是  精品线路 还是 出境旅游
        addActivityCST(this);

        String tag = getIntent().getStringExtra("hq_tag");
        baseTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);

        if (tag.equals(getString(R.string.tour_abroad))) {
            urlPath = FeatureJapanShowUrl();
            setTitleBarLeftBtn(baseTitlebar,getString(R.string.tour_abroad));
        }else {
            urlPath = HQualityRouteShowUrl();
            setTitleBarLeftBtn(baseTitlebar,getString(R.string.hq_route_detail));
        }
        initView();
        setListener();
        pullUpToRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initView() {


        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_main);
        mPullRefreshListView.setPullRefreshEnabled(true); //下拉刷新
        mPullRefreshListView.setPullLoadEnabled(true);
        mPullRefreshListView.setScrollLoadEnabled(true);

        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setVerticalScrollBarEnabled(false);

    }

    /**
     * 再次执行请求
     */
    private void pullUpToRefresh() {
        longBeanListTemp.clear();
        index = 1;
        asyncTaskRequestData = null;
        asyncTaskRequestData = new AsyncTaskRequesJsontData(this);
        asyncTaskRequestData.execute(urlPath+"?index="+index);
    }

    /**
     * 设置事件监听处理程序
     */
    private void setListener() {
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ActivityHQualityRoute.this, ActivityDetailPage.class);
                intent.putExtra("detail_url",APP_H5_HOST+"/tourism/detail/"+view.getId());
//                intent.putExtra("what","route");
                startActivity(intent);
            }
        });

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullUpToRefresh();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullUpToNextPage();
            }
        });
    }


    private void pullUpToNextPage() {
        index++;
        if (index <= totalIndex) {
            pullToRefeash();
        }else{
            index = startPos;
        }

    }

    /**
     * 上拉加载更多
     */
    private void pullToRefeash() {
        asyncTaskRequestData = null;
        asyncTaskRequestData = new AsyncTaskRequesJsontData(this);
        asyncTaskRequestData.execute(urlPath+"?index="+index);
    }

    /**
     * 解析json数据,
     * 每个类所需的json数据格式可能不一样，
     * 这个需要自行解析，这个方法要自己写
     * @param jsonString 需要传递过来json字符串
     * @return  封装了实体类 LongBean 的集合
     */
    private List<LongBean> getLongBeanListHQ(String jsonString) {

        JSONObject jsonObject;
        LongBean longBean;
        try {
            jsonObject = new JSONObject(jsonString);
            jsonObject = jsonObject.getJSONObject("data");
            totalNum = jsonObject.getInt("total");//获取总条数
            perPageNum = jsonObject.getInt("size");//每页的条数
            totalIndex = (totalNum%perPageNum)==0?totalNum/perPageNum:totalNum/perPageNum+1;
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for(int i = 0; i < jsonArray.length(); i++)
            {
                jsonObject = jsonArray.getJSONObject(i);
                longBean = new LongBean();

                longBean.newsIconUrl = jsonObject.getString("pictureUrl");//图片url
                longBean.newsTitle = jsonObject.getString("title");//图片文字简介
                longBean.newsId = jsonObject.getInt("id");//用于分页

                longBeanListTemp.add(longBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return longBeanListTemp;
    }

    @Override
    public void preExecuteRequesJsontData() {
    }

    @Override
    public List<LongBean> executingRequesJsontData(String  inputStream) {
        String jsonString = ReadStreamOfJson(inputStream);
        return getLongBeanListHQ(jsonString);
    }
    /************************************************************
     *@Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2016/12/22 21:22
     ************************************************************/
    @Override
    public void postExecuteRequesJsontData(List<LongBean> list) {
        if (list == null) {
            Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
            return;
        }else {
            longBeanList.clear();
            longBeanList.addAll(list);
            if (adapter == null) {
                adapter = new HQRouteAdapter(ActivityHQualityRoute.this, longBeanList, mListView,resId,totalNum);
                mListView.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }
        mPullRefreshListView.onRefreshComplete();
    }
    @Override
    public void progressUpdateRequesJsontData(Integer... progresses) {
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount==totalNum+1) {
            mPullRefreshListView.setHasMoreData(false);//滚动到底部
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (longBeanList != null) {
            longBeanList = null;
        }
        if (asyncTaskRequestData != null) {
            asyncTaskRequestData.cancel(true);
            asyncTaskRequestData = null;
        }
        if (mListView != null) {
            mListView = null;
        }
        if (mPullRefreshListView != null) {
            mPullRefreshListView = null;
        }
    }

}
