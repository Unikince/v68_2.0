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
	// 游戏回合id
	protected String roundId  ;
	// 用户当前金币
	protected double currentGold; 
	// 准入金额
	protected double InAmount; 
	
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
	
	public String getRoundId() {
		return roundId;
	}
	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}
	
	public double getCurrentGold() {
		return currentGold;
	}
	public void setCurrentGold(double currentGold) {
		this.currentGold = currentGold;
	}
	public double getInAmount() {
		return InAmount;
	}
	public void setInAmount(double inAmount) {
		InAmount = inAmount;
	}
	public HttpMessageResult(){}
	public HttpMessageResult(int ret, String msg){
		this.ret = ret;
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "HttpMessageResult [ret=" + ret + ", msg=" + msg + ", roundId=" + roundId + ", currentGold="
				+ currentGold + ", InAmount=" + InAmount + "]";
	}
	
}
