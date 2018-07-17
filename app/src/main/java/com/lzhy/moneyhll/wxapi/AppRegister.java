package com.lzhy.moneyhll.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lzhy.moneyhll.constant.Constant;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public final class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, Constant.MERCHANTID_WX);

		// 将该app注册到微信
		msgApi.registerApp(Constant.MERCHANTID_WX);
	}
}
