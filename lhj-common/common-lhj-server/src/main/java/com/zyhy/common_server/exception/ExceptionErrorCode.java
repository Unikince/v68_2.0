/**
 * 
 */
package com.zyhy.common_server.exception;

/**
 * @author Administrator
 * 异常错误对应码
 */
public enum ExceptionErrorCode {

	NOTNULL(40000, "空指针异常"),
	NOTSERVER(40001, "服务未开启"),
	NOTCONFIG(40002, "配置读取异常"),
	ERRORINFO(40003, "错误的请求");
	
	private int ret;
	private String message;
	private ExceptionErrorCode(int ret, String message){
		this.ret = ret;
		this.message = message;
	}
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
