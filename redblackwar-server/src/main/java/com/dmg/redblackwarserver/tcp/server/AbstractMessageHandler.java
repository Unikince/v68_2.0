/**
 * 
 */
package com.dmg.redblackwarserver.tcp.server;

import com.alibaba.fastjson.JSONObject;


/**
 * @author linanjun
 * 消息处理器通用继承接口
 */
public interface AbstractMessageHandler {

	/**
	 * 消息号
	 * @return
	 */
	String getMessageId();

	/**
	 * 消息处理器
	 * @param userId 用户ID
	 * @param params 参数列表
	 * @return
	 */
	void messageHandler(int userId, JSONObject params);
}
