/**
 * 
 */
package com.zyhy.common_server.result;

/**
 * @author Administrator 错误信息返回
 */
public class ErrorResponseResult {
	
	private int code;
	private String message;
	public ErrorResponseResult(int code, String message){
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ErrorResponseResult [code=" + code + ", message=" + message
				+ "]";
	}
}
