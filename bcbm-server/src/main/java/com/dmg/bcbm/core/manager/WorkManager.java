package com.dmg.bcbm.core.manager;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.abs.def.type.IQueueType;
import com.dmg.bcbm.core.abs.work.Work;
import com.dmg.bcbm.core.thread.ThreadPool;

/**
 * @Date: 2015年10月13日 下午4:01:57
 * @Author: zhuqd
 * @Description:
 */
public class WorkManager {
	private static Logger logger = LoggerFactory.getLogger(WorkManager.class);
	private static WorkManager instance = new WorkManager();
	private Map<IQueueType, ThreadPool> threadPoolMap = new HashMap<>();

	private WorkManager() {

	}

	public static WorkManager instance() {
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param queueType
	 * @throws Exception
	 */
	public void init(IQueueType[] queueType) throws Exception {
		for (IQueueType type : queueType) {
			ThreadPool pool = new ThreadPool(type.threadSize(), type.isGroup());
			threadPoolMap.put(type, pool);
		}
		logger.info("[WORK MANAGER]INITED");
	}

	/**
	 * 提交一个异步任务
	 * 
	 * @param work
	 */
	public void submit(Work work) {
		work.startTime = System.currentTimeMillis();
		//
		IQueueType type = work.queue();
		if (type == null) {
			logger.error("work has no queue type. work delete.", work.getClass().getName());
			return;
		}
		threadPoolMap.get(type).execute(work);
	}

	/**
	 * 提交一个异步任务
	 * 
	 * @param clazz
	 * @param args
	 */
	public void submit(Class<? extends Work> clazz, Object... args) {
		try {
			Work work = clazz.newInstance();
			work.init(args);
			submit(work);
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("submit a work error.{}", e.getMessage());
		}

	}

	public Map<IQueueType, ThreadPool> getThreadPoolMap() {
		return threadPoolMap;
	}

	public void setThreadPoolMap(Map<IQueueType, ThreadPool> threadPoolMap) {
		this.threadPoolMap = threadPoolMap;
	}

}
