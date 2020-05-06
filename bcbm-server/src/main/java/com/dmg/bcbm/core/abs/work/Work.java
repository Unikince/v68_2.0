package com.dmg.bcbm.core.abs.work;

import com.dmg.bcbm.core.abs.def.type.IQueueType;
import com.dmg.bcbm.core.thread.ThreadWorkCounter;

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
		long costTime = System.currentTimeMillis() - startTime;
		ThreadWorkCounter.instance().addCount(this.getClass(), costTime);
	}

	/**
	 * 逻辑处理
	 */
	public abstract void go();

}
