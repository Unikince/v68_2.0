/**
 * 
 */
package com.dmg.bairenniuniuserver.tcp.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author linanjun
 * 消息处理接口
 */
@Service
public class MainProcess {

	@Autowired
	private List<AbstractMessageHandler> processList;
	
	private Map<String, AbstractMessageHandler> map;
	
	// 消息处理器
	@PostConstruct
	private void postConstruct() {
		map = new HashMap<String, AbstractMessageHandler>();
		for (AbstractMessageHandler handler : processList) {
			map.put(handler.getMessageId(), handler);
		}
	}
	
	/**
	 * 根据消息ID查询消息处理器
	 * @param messageId
	 * @return
	 */
	public AbstractMessageHandler getHandler(String messageId) {
		if (map.containsKey(messageId)) {
			return map.get(messageId);
		}
		return null;
	}
}
