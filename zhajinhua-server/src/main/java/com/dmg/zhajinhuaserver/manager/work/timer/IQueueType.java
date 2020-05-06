package com.dmg.zhajinhuaserver.manager.work.timer;

/**
 * @Date: 2015年10月13日 下午4:04:17
 * @Author: zhuqd
 * @Description: 队列标识
 */
public interface IQueueType {

	/**
	 * 获取线程数量
	 * 
	 * @return
	 */
	public int threadSize();

	/**
	 * 是否是分组线程池
	 * 
	 * @return
	 */
	public boolean isGroup();
}
