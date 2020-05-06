package com.zyhy.common_server.constants;

/**
 * @author zhuqd
 * @Date 2017年8月24日
 * @Desc 自定义线程池
 */
public enum QueueType implements IQueueType {
	LOGIC(2, false);

	private int threadSize;
	private boolean isGroup;

	QueueType(int threadSize, boolean isGroup) {
		this.threadSize = threadSize;
		this.isGroup = isGroup;
	}

	@Override
	public int threadSize() {
		return threadSize;
	}

	@Override
	public boolean isGroup() {
		return isGroup;
	}

}
