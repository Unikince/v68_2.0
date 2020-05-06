/**
 * 
 */
package com.zyhy.common_server.result;



/**
 * @author linanjun
 * 服务器消息
 */
public class HttpMessageResult{
	
	// 结果 1=成功,2=失败,3=重新登陆,4=游戏币不足,5=钻石不足,6=重复登陆,7=游戏失效,8=实名认证
	protected int ret = 1;
	// 错误信息
	protected String msg;
	
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public HttpMessageResult(){}
	public HttpMessageResult(int ret, String msg){
		this.ret = ret;
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "HttpMessageResult [ret=" + ret + ", msg=" + msg + "]";
	}
}
