/**
 * 
 */
package com.dmg.lobbyserver.result;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author linanjun
 *
 */
@Data
public class MessageResult {

	// 消息结果(1=成功,2=失败,3=前往注册,4=前往登录)
	private int res = 1;
	// 消息错误
	private Object msg="";
	// 系统时间
	private long time;
	// 返回协议号
	private String m;
	public MessageResult(int res, Object msg,String m) {
		this.m = m;
		this.res = res;
		this.msg = msg;
		this.time = System.currentTimeMillis();
	}

	public MessageResult(String m) {
		this.m = m;
		this.setRes(1);
		this.time = System.currentTimeMillis();
	}

	public static String success(String m, Object msg){
		MessageResult messageResult = new MessageResult(1,msg,m);
		messageResult.setTime( System.currentTimeMillis());
		return JSONObject.toJSONString(messageResult);
	}

	public static String error(String m, Object msg){
		MessageResult messageResult = new MessageResult(2,msg,m);
		messageResult.setTime( System.currentTimeMillis());
		return JSONObject.toJSONString(messageResult);
	}

	public static String errorStr(Integer res){
		MessageResult messageResult = new MessageResult(2,"","");
		messageResult.setTime( System.currentTimeMillis());
		return JSONObject.toJSONString(messageResult);
	}

	public static MessageResult error(String errorcode){
		MessageResult messageResult = new MessageResult(2,errorcode,"");
		messageResult.setTime( System.currentTimeMillis());
		return messageResult;
	}

}
