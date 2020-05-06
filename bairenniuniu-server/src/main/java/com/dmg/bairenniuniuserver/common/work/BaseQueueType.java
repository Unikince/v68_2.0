package com.dmg.bairenniuniuserver.common.work;

/**
 * @author zhuqd
 * @Date 2017年7月24日
 * 
 */
public enum BaseQueueType implements IQueueType {
	SERVER(4, false), // 系统
	ROOM(9, true), // 房间消息处理
	SAFE(7, true); //

	private int threadSize;
	private boolean isGroup;

	BaseQueueType(int threadSize, boolean isGroup) {
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