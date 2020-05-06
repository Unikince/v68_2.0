package com.dmg.redblackwarserver.common.work;


/**
 * @Date: 2015年10月7日 下午3:54:26
 * @Author: zhuqd
 * @Description: 异步任务。
 */
public abstract class Work implements Runnable {

	public long startTime = 0;

	/**
	 * 初始化
	 * 
	 * @param args
	 */
	public abstract void init(Object... args);

	/**
	 * 返回队列标识
	 * 
	 * @return
	 */
	public abstract IQueueType queue();

	@Override
	public void run() {
		go();
	}

	/**
	 * 逻辑处理
	 */
	public abstract void go();

}
