/**
 * 
 */
package com.zyhy.common_lhj.process;

import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author linanjun
 * 消息处理器接口
 */
public interface IMsgProcess {
	
	/**
	 * 消息号
	 * @return
	 */
	int getMessageId();
	
	/**
	 * 消息处理
	 * @param uuid
	 * @param body
	 * @return
	 */
	HttpMessageResult httpProcess(String uuid, Object body);
}
