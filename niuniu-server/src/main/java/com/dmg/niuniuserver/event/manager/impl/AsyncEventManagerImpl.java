/**   
* @Title: AsyncEventManagerImpl.java
* @Package com.longma.ssss.com.dmg.clubserver.event.com.dmg.clubserver.manager
* @Description:
* @author nanjun.li   
* @date 2017-1-6 下午5:38:06
* @version V1.0   
*/


package com.dmg.niuniuserver.event.manager.impl;

import com.dmg.niuniuserver.event.manager.AsyncEventManager;
import com.dmg.niuniuserver.event.queue.Event;
import com.dmg.niuniuserver.event.listener.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @ClassName: AsyncEventManagerImpl
 * @Description: 异步事件管理器
 * @author nanjun.li
 * @date 2017-1-6 下午5:38:06
 */
@Service
public class AsyncEventManagerImpl implements AsyncEventManager {

	private Map<Class<? extends Event>, List<EventListener>> map = new HashMap<Class<? extends Event>, List<EventListener>>();
	
	private static Comparator<EventListener> eventSort = new EventListenerComparator();
	
	@Override
	public void addListener(Class<? extends Event> event, EventListener listener) {
		List<EventListener> list = map.get(event);
		if (list == null) {
			list = new ArrayList<EventListener>();
			map.put(event, list);
		}
		list.add(listener);
		Collections.sort(list, eventSort);
	}

	@Override
	public void processEvent(Event event) {
		List<EventListener> list = map.get(event.getClass());
		for (EventListener eventListener : list) {
			eventListener.take(event);
		}
	}
	
	/**
	 * 
	* @ClassName: EventListenerComparator
	* @Description: 排序器
	* @author nanjun.li
	* @date 2017-1-6 下午6:27:24
	 */
	public static class EventListenerComparator implements Comparator<EventListener>{
		@Override
		public int compare(EventListener o1, EventListener o2) {
			return o1.orderLevel() - o2.orderLevel();
		}
	}

}
