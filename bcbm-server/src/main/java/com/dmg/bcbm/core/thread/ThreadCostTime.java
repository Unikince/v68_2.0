package com.dmg.bcbm.core.thread;

/**
 * @author zhuqd
 * @Date 2017年8月16日
 * @Desc
 */
public class ThreadCostTime implements Comparable<ThreadCostTime> {

	private Class<?> clazz;
	private int loop = 0;
	private long cost = 0;
	private long max = 0;
	private long min = 999999999;
	private double avg = 0;

	public ThreadCostTime(Class<?> clazz) {
		this.clazz = clazz;
	}

	//
	public int getLoop() {
		return loop;
	}

	public void setLoop(int loop) {
		this.loop = loop;
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	@Override
	public int compareTo(ThreadCostTime o) {
		if (this.avg > o.avg) {
			return 1;
		}
		if (this.avg < o.avg) {
			return -1;
		}
		return 0;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
