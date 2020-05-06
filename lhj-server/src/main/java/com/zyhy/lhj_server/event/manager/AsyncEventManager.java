/**   
* @Title: AsyncEventManager.java
* @Package com.longma.ssss.event
* @Description:
* @author nanjun.li   
* @date 2017-1-6 下午5:33:32
* @version V1.0   
*/


package com.zyhy.lhj_server.event.manager;

import com.zyhy.lhj_server.event.listener.EventListener;
import com.zyhy.lhj_server.event.queue.Event;


/**
 * @ClassName: AsyncEventManager
 * @Description: 异步事件管理器
 * @author nanjun.li
 * @date 2017-1-6 下午5:33:32
 */
public interface AsyncEventManager {

	/**
	 * 
	* @Title: addListener 
	* @Description: 注册事件和处理器
	* @param @param event
	* @param @param listener
	* @return void    返回类型 
	* @throws
	 */
	void addListener(Class<? extends Event> event, EventListener listener);
	
	/**
	 * 
	* @Title: processEvent 
	* @Description: 处理事件
	* @param @param event
	* @return void    返回类型 
	* @throws
	 */
	void processEvent(Event event);
}
