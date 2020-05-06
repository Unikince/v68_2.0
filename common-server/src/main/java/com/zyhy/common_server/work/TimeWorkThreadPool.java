package com.zyhy.common_server.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Date: 2015年10月7日 下午3:03:40
 * @Author: zhuqd
 * @Description:
 */
public class TimeWorkThreadPool {

	private static Logger logger = LoggerFactory.getLogger(TimeWorkThreadPool.class);
	private Map<Integer, ExecutorService> executorMap = new HashMap<>();
	private ExecutorService executor = null;
	private boolean isCount = true; //

	private ConcurrentHashMap<Class<?>, Integer> timeoutCount = new ConcurrentHashMap<>(); // 超时work记录
	private ConcurrentHashMap<Class<?>, Long> useTimeCount = new ConcurrentHashMap<>(); // 正常work时间统计
	private ConcurrentHashMap<Class<?>, Integer> executTimesCount = new ConcurrentHashMap<>(); // 正常执行次数统计

	public TimeWorkThreadPool() {
		this(Runtime.getRuntime().availableProcessors() * 2, false);
	}

	/**
	 *
	 * @param threadNum
	 */
	public TimeWorkThreadPool(int threadNum, boolean isGroup) {
		if (threadNum > 50) {
			throw new RuntimeException("you create too mutch thread .... ");
		}
		if (isGroup) {
			for (int i = 1; i <= threadNum; i++) {
				ExecutorService executor1 = Executors.newSingleThreadExecutor();
				executorMap.put(i, executor1);
			}
		} else if (threadNum <= 1) {
			this.executor = Executors.newSingleThreadExecutor();
		} else {
			this.executor = Executors.newFixedThreadPool(threadNum);
		}

	}

	/**
	 * 提交一个任务
	 * 
	 * @param work
	 */
	public void execute(Work work) {
		if (work instanceof GroupWork) {
			if (executorMap.isEmpty()) {
				logger.error("提交的work不应该在这个线程池处理");
			}
			int groupId = ((GroupWork) work).getGroupId();
			int poolId = groupId % executorMap.size() + 1;
			executorMap.get(poolId).execute(work);
		} else {
			if (executor == null) {
				logger.error("提交的work不应该在这个线程池处理");
			}
			executor.execute(work);

		}
	}

	// /**
	// * 提交一个callable任务
	// *
	// * @param callable
	// */
	// public void sumbit(Callable<?> callable) {
	// executor.submit(callable);
	// }

	/**
	 * 清空统计数据
	 */
	public void clearCountMap() {
		timeoutCount.clear();
		useTimeCount.clear();
		executTimesCount.clear();
	}

	public boolean isCount() {
		return isCount;
	}

	public void setCount(boolean isCount) {
		this.isCount = isCount;
	}

	public ConcurrentHashMap<Class<?>, Integer> getTimeoutCount() {
		return timeoutCount;
	}

	public void setTimeoutCount(ConcurrentHashMap<Class<?>, Integer> timeoutCount) {
		this.timeoutCount = timeoutCount;
	}

	public ConcurrentHashMap<Class<?>, Long> getUseTimeCount() {
		return useTimeCount;
	}

	public void setUseTimeCount(ConcurrentHashMap<Class<?>, Long> useTimeCount) {
		this.useTimeCount = useTimeCount;
	}

	public ConcurrentHashMap<Class<?>, Integer> getExecutTimesCount() {
		return executTimesCount;
	}

	public void setExecutTimesCount(ConcurrentHashMap<Class<?>, Integer> executTimesCount) {
		this.executTimesCount = executTimesCount;
	}
}
