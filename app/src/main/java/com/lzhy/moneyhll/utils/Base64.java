package com.lzhy.moneyhll.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;



public final class Base64 {
	 // 加密
    public static String getBase64(JSONObject param) {
        byte[] b = null;
        String s = null;
        JSONObject data = new JSONObject();
        JSONArray params = new JSONArray();
        try {
            params.put(param);
            data.put("data",params);
            s=data.toString();
            b = s.getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    // 解密  
    public static String getFromBase64(String s) {
        if (s.contains("\\r")){
            s=s.replace("\\r","");
        }
        if (s.contains("\\n")) {
            s = s.replace("\\n", "");
        }
        if (s!=null&&s.length()>1) {
            s = s.substring(1, s.length() - 1);
        }
        byte[] b = null;
        String result = null;  
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();
            try {  
                b = decoder.decodeBuffer(s);  
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }

        return result;  
    }

}
