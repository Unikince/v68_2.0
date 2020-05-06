/**
 * 
 */
package com.zyhy.common_server.exception;


/**
 * @author Administrator 
 * 诸葛游戏自定义异常
 * 1.使用之前请确保服务实现了ZgqpGameExceptionHandler的扩展
 * 2.开发者错误码号段规则(李南俊4XXXX,李洋5XXXX)
 */
public class ZgqpGameException extends RuntimeException {

	private static final long serialVersionUID = 4564124491192825748L;
	
	private int ret;

	public ZgqpGameException() {
		super();
	}
	
	public ZgqpGameException(String errmsg) {
		super(errmsg);
	}

	public ZgqpGameException(ExceptionErrorCode code) {
		super(code.getMessage());
		this.setRet(code.getRet());
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}
}
