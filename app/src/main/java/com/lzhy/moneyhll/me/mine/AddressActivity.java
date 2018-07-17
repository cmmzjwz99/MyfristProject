package com.lzhy.moneyhll.me.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.pulltorefreshlistview.PullToRefreshListView;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.model.AddressModel;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.utils.Base64;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.viewhelper.AddressHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/10/26.
 * 我的收货地址
 */
public class AddressActivity extends MySwipeBackActivity implements ItemViewListener {
    private BaseTitlebar mTitlebar;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private List<AddressModel> mList;

    private LongAdapter mAdapter;
    private View foot;
    private TextView add_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_adress);

        addActivityCST(this);
        initView();
        initTitlebar();
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingData();
        disparityLogin();
    }
    private void initAdapter() {
        mList = new ArrayList<>();
        mAdapter = new LongAdapter(AddressActivity.this, mList, this);
        mListView.setAdapter(mAdapter);

    }

    private void initView() {
        foot = LayoutInflater.from(AddressActivity.this).inflate(R.layout.item_add_address_foot, null);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.itemlist);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.addFooterView(foot);
        add_address = (TextView) foot.findViewById(R.id.add_address);
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, AddOrChangeAddressActivity.class);
                Bundle extras = new Bundle();
                extras.putString("type", "add");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        mPullRefreshListView.setPullLoadEnabled(false);
        mPullRefreshListView.setPullRefreshEnabled(false);
    }

    private void initTitlebar() {
        mTitlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(mTitlebar,"我的收货地址");
    }

    private void LoadingData() {
        /****************
         *修改者:  ycq
         *修改时间: 2017.01.09
         *修改原因: 接口用户信息加密
         * Describe:param  传递的参数
                    Base64.getBase64(
         )  参数加密
                    Base64.getFromBase64(response)  返回数据解密
         ****************/
        JSONObject param = new JSONObject();
        try {
            param.put("userId", UserInfoModel.getInstance().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String allAddreaaUrl = UrlAPI.getAllAddreaaUrl(Base64.getBase64(param));
        PrintLog.e("我的收货地址URL:" + allAddreaaUrl);
        OkHttpUtils.get().url(allAddreaaUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("我的收货地址:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                response=Base64.getFromBase64(response);
                mList.clear();
                mPullRefreshListView.onRefreshComplete();
                PrintLog.e("我的收货地址:" + response);
                Type type = new TypeToken<Response<AddressModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response<AddressModel> resp = gson.fromJson(response, type);
                List<AddressModel> data = resp.getData();
                if (data != null) {
                    mList.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public View getView(final int id, View itemView, ViewGroup vg, final Object data) {
        final AddressHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(AddressActivity.this).inflate(
                    R.layout.item_add_address, null, false);
            helper = new AddressHelper(AddressActivity.this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (AddressHelper) itemView.getTag();
        }
        final AddressModel moder = (AddressModel) data;
        helper.updateView(moder);
        final String type = getIntent().getStringExtra("type");
        helper.setBackOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (helper.type1 == 1 && "choose".equals(type)) {
                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putSerializable("AddressModel",moder);
                    intent.putExtras(extras);
                    setResult(Constant.RESULT_CODE, intent);
                    finish();
                } else if (helper.type1 == 2) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i) == moder) {
                            mList.get(i).isDefaultAddress = 1;
                        } else {
                            mList.get(i).isDefaultAddress = 0;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }else if(helper.type1 == 3){
                    mList.remove(id);
                    mAdapter.notifyDataSetChanged();
                }else if (helper.type1==1&&"snap".equals(type)){//返回秒杀界面
                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putSerializable("AddressModel",moder);
                    intent.putExtras(extras);
                    setResult(10010, intent);
                    finish();
                }
            }
        });
        return itemView;
    }
}
