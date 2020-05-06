/**   
* @Title: EventListener.java
* @Package com.longma.ssss.event
* @Description:
* @author nanjun.li   
* @date 2017-1-6 下午5:23:37
* @version V1.0   
*/


package com.zyhy.lhj_server.event.listener;

import com.zyhy.lhj_server.event.queue.Event;


/**
 * @ClassName: EventListener
 * @Description: 事件监听/处理器
 * @author nanjun.li
 * @date 2017-1-6 下午5:23:37
 */

public interface EventListener {

	/**
	 * 
	* @Title: take 
	* @Description:处理器
	* @param @param event
	* @return void    返回类型 
	* @throws
	 */
	void take(Event event);
	
	/**
	 * 
	* @Title: orderLevel 
	* @Description: 处理优先级
	* @param @return
	* @return int    返回类型 
	* @throws
	 */
	int orderLevel();
}
