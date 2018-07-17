package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/2 0002.
 * 当返回结果中的data对应的数据是list时，请使用 Response1.class来解析返回结果
 */

public class Response2 {

    /**
     * 描述
     */
    private String message;

    /**
     * 状态
     */
    private String errCode;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
