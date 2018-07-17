package com.lzhy.moneyhll.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2 0002.
 * 当返回结果中的data对应的数据是list时，请使用 Response.class来解析返回结果
 */

public class Response<T> {

    /**
     * 描述
     */
    private String message;

    /**
     * 状态
     */
    private String errCode;
    /**
     * 查询结果
     */
    private List<T> data;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
