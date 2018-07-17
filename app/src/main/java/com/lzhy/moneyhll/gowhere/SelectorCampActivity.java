package com.lzhy.moneyhll.gowhere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.constant.Constant;
import com.lzhy.moneyhll.custom.ItemViewListener;
import com.lzhy.moneyhll.custom.LongAdapter;
import com.lzhy.moneyhll.custom.MySwipeBackActivity;
import com.lzhy.moneyhll.custom.NoScorllGridView;
import com.lzhy.moneyhll.model.HotKeyModel;
import com.lzhy.moneyhll.model.Response;
import com.lzhy.moneyhll.utils.PrintLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

import static com.lzhy.moneyhll.manager.ActivityManagerCST.addActivityCST;
import static com.lzhy.moneyhll.utils.UtilCheckLogin.disparityLogin;

/**
 * 搜索露营地
 */
public class SelectorCampActivity extends MySwipeBackActivity implements ItemViewListener {
    private TextView cancel;
    private EditText go_where_selector;
    private LongAdapter mAdapter;
    private NoScorllGridView gridView;
    private List<String> cities = new ArrayList<>();
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_selector_camp);

        addActivityCST(this);
        initView();
        initAdapter();
        LoadingProvinceData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        disparityLogin();
    }
    private void initAdapter() {
        mAdapter = new LongAdapter(this, cities, this);
        gridView.setAdapter(mAdapter);
    }

    private void initView() {
        cancel = (TextView) findViewById(R.id.cancel);
        go_where_selector = (EditText) findViewById(R.id.go_where_selector);
        go_where_selector.setFocusable(true);
        go_where_selector.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        showKeyboard();

        gridView = (NoScorllGridView) findViewById(R.id.grid_view);
        setClick();
    }

    /************************************************************
     *@Author; 龙之游 @ xu 596928539@qq.com
     * 时间:2017/1/4 12:39
     * 注释: 自动弹出软键盘
     ************************************************************/
    private void showKeyboard() {
        timer.schedule(new TimerTask(){
            public void run(){
                InputMethodManager inputManager =(InputMethodManager)go_where_selector.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(go_where_selector, 0);
            }
        },500);

    }


    private void setClick() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Constant.RESULT_CODE, (new Intent().putExtra("text", "")));
                finish();
            }
        });

        go_where_selector.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (go_where_selector.getText().toString().length() <= 0) {
                        Toast.makeText(SelectorCampActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                    } else {
                        setResult(Constant.RESULT_CODE, (new Intent().putExtra("text", go_where_selector.getText().toString())));
                        finish();
                    }
                    return true;
                }
                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setResult(Constant.RESULT_CODE, (new Intent().putExtra("text", cities.get(i))));
                finish();
            }
        });
    }

    private void LoadingProvinceData() {
        String allHotKeyUrl = UrlAPI.getAllHotKeyUrl();
        PrintLog.e("获取热门词URL:" + allHotKeyUrl);
        OkHttpUtils.get().url(allHotKeyUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Toast.makeText(mContext, "请求错误", Toast.LENGTH_SHORT).show();
                PrintLog.e("获取热门词onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("获取热门词onResponse:" + response);
                Type type = new TypeToken<Response<HotKeyModel>>() {
                }.getType();
                Gson gson = new Gson();
                Response<HotKeyModel> resp = gson.fromJson(response, type);
                List<HotKeyModel> data = resp.getData();
                if (data != null) {
                    cities.clear();
                    for (int i = 0; i < data.size(); i++) {
                        cities.add(data.get(i).phrase);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View getView(int id, View itemView, ViewGroup vg, Object data) {
        TextView textView;
        if (itemView == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_whereplay_key, null);
            textView = (TextView) view;
            itemView = textView;
            itemView.setTag(textView);
        } else {
            textView = (TextView) itemView.getTag();
        }
        textView.setText(cities.get(id));

        return itemView;
    }
}
