/**   
* @Title: EventListener.java
* @Package com.longma.ssss.com.dmg.clubserver.event
* @Description:
* @author nanjun.li   
* @date 2017-1-6 下午5:23:37
* @version V1.0   
*/


package com.dmg.lobbyserver.event.listener;


import com.dmg.lobbyserver.event.queue.Event;

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
	* @param @param com.dmg.clubserver.event
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
