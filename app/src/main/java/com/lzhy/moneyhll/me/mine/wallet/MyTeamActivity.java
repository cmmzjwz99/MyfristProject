package com.lzhy.moneyhll.me.mine.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.BaseTitlebar;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.NoScrollListView;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.bean.Friend;
import com.lzhy.moneyhll.me.mine.bean.OwnInfo;
import com.lzhy.moneyhll.me.mine.bean.PoInfo;
import com.lzhy.moneyhll.me.mine.bean.TeamInfo;
import com.lzhy.moneyhll.viewhelper.MyTeamHelper;
import com.lzhy.moneyhll.model.Response1;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.CommonUtil.setTitleBarLeftBtn;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * Created by cmm on 2016/11/3.
 * 我的团队
 */
public class MyTeamActivity extends MySwipeBackActivity implements ItemViewListener {

    private SimpleDraweeView ivHead;
    private TextView tvMyName;
    private TextView tvOwnNum;
    private SimpleDraweeView image;
    private TextView tvYJN;
    private TextView tvYJTime;
    private TextView tvPhone;
    private NoScrollListView lvRecList;
    private LongAdapter mAdapter;
    private List<Friend> mList;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        addActivityCST(this);
        initView();
        initTitlebar();
        initAdapter();
        initTeam(true);
    }

    private void initView() {
        ivHead = (SimpleDraweeView) findViewById(R.id.iv_head_portrait);
        tvMyName = (TextView) findViewById(R.id.tv_name);
        tvOwnNum = (TextView) findViewById(R.id.tv_yingjian_number);

        image = (SimpleDraweeView) findViewById(R.id.image);
        tvYJN = (TextView) findViewById(R.id.tv_yin_jian_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvYJTime = (TextView) findViewById(R.id.tv_yin_jian_time);

        mList = new ArrayList<>();
        lvRecList = (NoScrollListView) findViewById(R.id.lv_me_yin_jian);
    }

    private void initTitlebar() {
        BaseTitlebar titlebar = (BaseTitlebar) findViewById(R.id.title_bar);
        setTitleBarLeftBtn(titlebar,"我的团队");
    }


    //初始化我的团队
    private void initTeam(boolean isrefresh) {

        String getTeamUrl = UrlAPI.getTeamUrl(UserInfoModel.getInstance().getId());//14881
        PrintLog.e("我的团队URL:" + getTeamUrl);
        OkHttpUtils.get().url(getTeamUrl).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("我的团队URL:" + e);
                Utils.toast(MyTeamActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {

                response = response.replace(",\"PoInfo\":\"\"","");

                PrintLog.e("我的团队.....................URL:" + response);
                Type type = new TypeToken<Response1<TeamInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Response1<TeamInfo> resp = gson.fromJson(response, type);

                TeamInfo info = resp.getData();

                OwnInfo ownInfo = info.ownInfo;

                ivHead.setImageURI(ownInfo.inviteImg);
                tvMyName.setText(ownInfo.inviteName);
                PrintLog.e("我的团队........ownInfo.inviteName.............URL:" + ownInfo.inviteName);

                tvOwnNum.setText("引荐了" + ownInfo.ownNum + "位小伙伴");

                if (info.PoInfo != null) {
                    PoInfo poInfo = info.PoInfo;
                    PrintLog.e("poInfo.....................URL:" + poInfo);
                    image.setImageURI(poInfo.recImg);
                    tvYJN.setText(poInfo.recName);
                    tvPhone.setText(poInfo.recPhone);
                    tvYJTime.setText(poInfo.recTime);
                }

                List<Friend> recList;
                recList = resp.getData().recList;
                if (recList != null) {
                    mList.clear();
                    mList.addAll(recList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initAdapter() {
        mAdapter = new LongAdapter(this, mList, this);
        lvRecList.setAdapter(mAdapter);
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        MyTeamHelper helper;
        if (itemView == null) {
            itemView = LayoutInflater.from(this).inflate(
                    R.layout.item_my_team, null, false);
            helper = new MyTeamHelper(this, itemView);
            itemView.setTag(helper);
        } else {
            helper = (MyTeamHelper) itemView.getTag();
        }
        final Friend model = (Friend) data;

        helper.updateView(model);
        return itemView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
}
