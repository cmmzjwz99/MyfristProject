package com.lzhy.moneyhll.me.loginOrRegister;

import java.util.Map;

/**
 * Created by cmm on 2016/11/2.
 */

public final class Result<T>{
    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";

    private Map<String,Object> mapMeta;

    private T data;

    public boolean isSuccess(){
        if(null == mapMeta){
            return false;
        }
        return (boolean) mapMeta.get(SUCCESS);
    }

    public String getMessage(){
        if(null == mapMeta){
            return "";
        }
        return (String) mapMeta.get(MESSAGE);
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}