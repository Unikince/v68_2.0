package com.dmg.bcbm.core.server.http.handler;

import java.util.Map;

/**
 * @Date: 2015年11月19日 下午5:06:01
 * @Author: zhuqd
 * @Description:
 */
public abstract class HttpReader {

	/**
	 * 消息读取
	 *
	 * @param path
	 * @param paramMap
	 * @param httpSender
	 */
	public abstract void readMessage(Map<String, String> paramMap, HttpSender httpSender);
	
}
