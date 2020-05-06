package com.dmg.redblackwarserver.common.work;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Date: 2015年10月7日 下午5:48:49
 * @Author: zhuqd
 * @Description: 定时任务。任务会一直按确定的时间间隔执行
 */
public abstract class TimeWork implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Object[] args = (Object[]) arg0.getJobDetail().getJobDataMap().get("args");
		init(args);
		go();
	}

	/**
	 * 使用的的qutarz,不需要自己的线程池
	 * 
	 * @return
	 */
	public IQueueType queue() {
		return null;
	}

	/**
	 * 初始化参数
	 * 
	 * @param args
	 */
	public abstract void init(Object... args);

	/**
	 * 任务运行
	 */
	public abstract void go();

}
