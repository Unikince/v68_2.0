package com.dmg.bcbm.core.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuqd
 * @Date 2017年7月18日
 */
public class ThreadWorkCounter {

	private static ThreadWorkCounter instance = new ThreadWorkCounter();

	/** 统计任务花费时间 */
	private Map<Class<?>, ThreadCostTime> costTimeCountMap = new ConcurrentHashMap<>();

	private boolean count = true;

	private ThreadWorkCounter() {

	}

	public static ThreadWorkCounter instance() {
		return instance;
	}

	public void init(boolean count) {
		this.count = count;
	}

	/**
	 * 统计每次消息处理时长
	 * 
	 * @param clazz
	 * @param time
	 */
	public void addCount(Class<?> clazz, long time) {
		if (!this.count) {
			return;
		}
		if (costTimeCountMap.get(clazz) == null) {
			// 第一次不统计，耗时较长
			costTimeCountMap.put(clazz, new ThreadCostTime(clazz));
			return;
		}
		//
		ThreadCostTime cost = costTimeCountMap.get(clazz);
		synchronized (cost) {
			cost.setCost(cost.getCost() + time);
			cost.setLoop(cost.getLoop() + 1);
			if (cost.getMax() < time) {
				cost.setMax(time);
			}
			if (cost.getMin() > time) {
				cost.setMin(time);
			}
			//
		}
	}

	public Map<Class<?>, ThreadCostTime> getCostTimeCountMap() {
		return costTimeCountMap;
	}

	public void setCostTimeCountMap(Map<Class<?>, ThreadCostTime> costTimeCountMap) {
		this.costTimeCountMap = costTimeCountMap;
	}

}
