package com.lzhy.moneyhll.viewhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.api.UrlAPI;
import com.lzhy.moneyhll.custom.dialog.BaseDialog;
import com.lzhy.moneyhll.home.beans.UserInfoModel;
import com.lzhy.moneyhll.me.mine.AddOrChangeAddressActivity;
import com.lzhy.moneyhll.model.AddressModel;
import com.lzhy.moneyhll.model.Response2;
import com.lzhy.moneyhll.utils.Base64;
import com.lzhy.moneyhll.utils.PrintLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/10 0010.
 */

public class AddressHelper {
    private Context mContext;
    private View mView;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_address;
    private TextView tv_cancel;
    private TextView tv_change;

    private LinearLayout linear;
    private CheckBox mCheckBox;
    public int type1;//12设置默认收货地址3删除收货地址

    private BaseDialog dismiss;

    private View.OnClickListener mOnClickListener;

    public void setBackOnClick(View.OnClickListener OnClickListener) {
        mOnClickListener = OnClickListener;
    }

    public AddressHelper(Context context, View view) {
        mContext = context;
        mView = view;
        findView();
    }

    private void findView() {
        tv_name = (TextView) mView.findViewById(R.id.tv_name);
        tv_address = (TextView) mView.findViewById(R.id.tv_address);
        tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
        tv_cancel = (TextView) mView.findViewById(R.id.tv_cancel);
        tv_change = (TextView) mView.findViewById(R.id.tv_change);
        linear = (LinearLayout) mView.findViewById(R.id.linear);
        mCheckBox = (CheckBox) mView.findViewById(R.id.default_address);
    }

    public void updateView(final AddressModel b) {
        final int id = b.id;
        if (b.isDefaultAddress == 1) {
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
        tv_address.setText(b.province + " " + b.city + " " + b.district + " " + b.addresss);
        tv_name.setText(b.name);
        tv_phone.setText(b.phone);

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddOrChangeAddressActivity.class);
                Bundle extras = new Bundle();
                extras.putString("type", "change");
                extras.putSerializable("AddressModel", b);
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    type1 = 1;
                    mOnClickListener.onClick(null);
                }
            }
        };
        linear.setOnClickListener(listener);
        tv_address.setOnClickListener(listener);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setDefaultAddress(id);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dismiss == null) {
                    final BaseDialog.Builder builder = new BaseDialog.Builder(mContext);
                    builder.setMessage("要删除此收货地址？");

                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    deleteAddress(b.id);
                                    dismiss.dismiss();
                                }
                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dismiss.dismiss();
                                }
                            });
                    dismiss = builder.create();
                    dismiss.show();
                } else {
                    dismiss.show();
                }
            }
        });

    }

    /**
     * 删除收货地址
     *
     * @param id
     */
    private void deleteAddress(int id) {
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "user/address/delete")
                .addParams("id", id + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("删除收货地址onError:" + e + call.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                PrintLog.e("删除收货地址onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 response2 = gson.fromJson(response, type);
                if ("200".equals(response2.getErrCode())) {
                    if (mOnClickListener != null) {
                        type1 = 3;
                        mOnClickListener.onClick(null);
                    }
                } else {
                    Toast.makeText(mContext, response2.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 设置默认收货地址
     */
    private void setDefaultAddress(int id) {
        /****************
         *修改者:  ycq
         *修改时间: 2017.01.09
         *修改原因: 接口用户信息加密
         * Describe:param  传递的参数
                    Base64.getBase64(param)  参数加密
                    Base64.getFromBase64(response)  返回数据解密
         ****************/
        JSONObject param = new JSONObject();
        try {
            param.put("id", id );
            param.put("userId",UserInfoModel.getInstance().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.post().url(UrlAPI.HOST_URL + "v1/user/address/updateDefault")
                .addParams("req", Base64.getBase64(param))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                PrintLog.e("设置默认收货地址onError:" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                response=Base64.getFromBase64(response);
                PrintLog.e("设置默认收货地址onResponse:" + response);
                Type type = new TypeToken<Response2>() {
                }.getType();
                Gson gson = new Gson();
                Response2 response2 = gson.fromJson(response, type);
                if ("200".equals(response2.getErrCode())) {
                    if (mOnClickListener != null) {
                        type1 = 2;
                        mOnClickListener.onClick(null);
                    }
                } else {
                    Toast.makeText(mContext, response2.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
