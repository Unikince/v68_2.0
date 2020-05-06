/**   
* @Title: AsyncEventDispatcher.java
* @Package com.longma.ssss.event
* @Description:
* @author nanjun.li   
* @date 2017-1-6 下午5:07:36
* @version V1.0   
*/


package com.zyhy.lhj_server.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.lhj_server.event.listener.EventListener;
import com.zyhy.lhj_server.event.listener.impl.RedisEventListener;
import com.zyhy.lhj_server.event.manager.AsyncEventManager;
import com.zyhy.lhj_server.event.queue.Event;
import com.zyhy.lhj_server.event.queue.impl.QueueModel;

/**
 * @ClassName: AsyncEventDispatcher
 * @Description: 异步分发事件
 * @author nanjun.li
 * @date 2017-1-6 下午5:07:36
 */
@Service
public class EventDispatcher {

	private static Log log = LogFactory.getLog(EventDispatcher.class);
	
	// 异步执行的线程池
	private ExecutorService executeThread = Executors.newFixedThreadPool(2);
	// 失败重试次数
	private int retrySize = 3;
	
	@Autowired
	private AsyncEventManager asyncEventManager;
	@Autowired
	private RedisEventListener redisEventListener;
	
	/**
	 * 绑定事件和监听器
	* @Title: init 
	* @Description:
	* @param 
	* @return void    返回类型 
	* @throws
	 */
	@PostConstruct
	public void init(){
		addListener(QueueModel.class, redisEventListener);
	}
	
	/**
	 * 
	* @Title: addListener 
	* @Description: 添加事件监听器
	* @param @param eventClass
	* @param @param listener
	* @return void    返回类型 
	* @throws
	 */
	public void addListener(Class<? extends Event> eventClass, EventListener listener){
		asyncEventManager.addListener(eventClass, listener);
		if (log.isInfoEnabled()) {
			log.info(eventClass + ", " + listener);
		}
	}
	
	/**
	 * 
	* @Title: addEvent 
	* @Description: 放入事件任务
	* @param @param event
	* @return void    返回类型 
	* @throws
	 */
	public void addEvent(Event event) {
		if (event == null) {
			return;
		}
		EventExecuteThread thread = new EventExecuteThread(event);
		thread.setDaemon(true);
		thread.setName("AsyncEventThread");
		executeThread.execute(thread);
	}
	
	/**
	 * 事件分发线程。
	 */
	private class EventExecuteThread extends Thread {

		private Event event;
		
		public EventExecuteThread(Event event){
			this.event = event;
		}
		
		public void run() {
			long start = System.currentTimeMillis();
			if (log.isInfoEnabled()) {
				log.info("get event : " + event);
			}
			int retry = 0;
			while (++retry <= retrySize) {
				long now = System.currentTimeMillis();
				try {
					asyncEventManager.processEvent(event);
					break;
				} catch (Throwable e) {
					if (log.isErrorEnabled()) {
						log.error("ADMIN:[" + retry + "] " + event, e);
					}
				} finally {
					long time = System.currentTimeMillis() - now;
					if (time > 3000) {
						log.warn("HANDLE " + event.getClass().getName() + " " + time + " ms.");
					}
				}
			}
			if (log.isInfoEnabled()) {
				log.info("handle event : " + event + " " + (System.currentTimeMillis() - start) + " ms");
			}
		}
	}
	
	/**
	 * 异步发送邮件验证码
	 * @param email
	 * @param code
	 */
	public void sendEmailCode(String email,String code) {
//		executeThread.execute(new MailUtil(email, code));
	}
}
