package com.lzhy.moneyhll.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzhy.moneyhll.custom.dialog.SharePop;
import com.ta.utdid2.android.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DeathWinder on 2016/7/7.
 */
public final class Utils {

    public static int getColor(Context context, int id) {
        int color;
        color = ContextCompat.getColor(context, id);
        return color;
    }

    public static HashMap<String, String> getMapByLink(String url) {
        java.util.HashMap<String, String> map = new java.util.HashMap<String, String>(10);

        if (url.indexOf("?") != -1) {
            String param = url.substring(url.indexOf("?") + 1);

            String[] ps = param.split("&");
            for (int i = 0; i < ps.length; i++) {
                String[] kv = ps[i].split("=");
                String val = "";
                if (kv.length > 1) {
                    val = kv[1];
                }
                map.put(kv[0], val);
            }
        }
        return map;
    }

    /**
     * 获取当前时间戳
     */
    public static long getNowTimeTemp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取MD5值
     */
    public static String getMD5(String str) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String mobiles) {
        String telRegex = "[1][3578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    //验证身份证号码
    public static boolean idCardNumber(String number) {
        String rgx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";

        return isCorrect(rgx, number);
    }

    //验证身份证号码
    public boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    //正则验证
    public static boolean isCorrect(String rgx, String res) {
        Pattern p = Pattern.compile(rgx);

        Matcher m = p.matcher(res);

        return m.matches();
    }

    /**
     * 获取bitmap的Base64
     */
    public static String getBase64FromBitmap(Bitmap bitmap) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 设置土司
     */
    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    public static final String hexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return "";
        StringBuffer hs = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(bytes[i] & 0xFF);
            if (s.length() < 2)
                hs.append(0);
            hs.append(s);
        }
        return hs.toString();
    }

    public static final String toSHA1(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(value.getBytes());
            return hexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用md5的算法进行加密
     */
    public static final String getMD5Value(String value) {
        try {
            byte[] b = MessageDigest.getInstance("MD5").digest(value.getBytes("UTF-8"));
            return hexString(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

    /**
     * json转对象
     */
    public static <T extends Object> T jsonToClass(String str, Type type) {
        T result = null;
        if (null != gson) {
            result = gson.fromJson(str, type);
        }
        return result;
    }



    /**
     * 分享
     * @param mContext
     * @param title
     * @param imageUrl
     * @param content
     * @param url
     * @param parentView
     */
    public static void ShareWX(Context mContext,String title, String imageUrl,String content,  String url, View parentView) {
        SharePop mSharePop;
        mSharePop = SharePop.newInstance(mContext);
        if (!StringUtils.isEmpty(title)) {
            mSharePop.setTitle(title);
        }
        if (!StringUtils.isEmpty(content)) {
            mSharePop.setShareContent(content);
        }
        if (!StringUtils.isEmpty(imageUrl)) {
            mSharePop.setSharePic(imageUrl);
        }
        mSharePop.setShareUrl(url);
        mSharePop.showAtBottom(parentView);
    }
}
