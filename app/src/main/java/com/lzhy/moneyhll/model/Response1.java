package com.lzhy.moneyhll.model;

/**
 * 对请求结果解析
 *
 * 当返回结果中的data对应的数据不是list时，请使用 Response1.class来解析返回结果
 */
public class Response1<T> {

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
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
