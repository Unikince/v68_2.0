package com.zyhy.lhj_server.bgmanagement.work;

/**
 * @author zhuqd
 * @Date 2017年7月24日
 * @Desc 服务器启动时根据{@link QueueTye}来初始化线程池。<br>
 *       配置有2个参数QueueType(threadNum,isGroup)。<br>
 *       threadNum：设置线程池的数量。一般建议不超过cpu*4的数量。线程数设置过多并不能提高性能和响应速度，
 *       反而会因为线程上下文之间的更加频繁的切换和导致更激烈的锁竞争降低性能。<br>
 *       isGroup：false表示开启任务分组来保证在多线程的环境下的线程安全，即是将会出现线程安全的逻辑(如房
 *       间中玩家的操作)都放入同一个线程处理。<b>这个异步任务需要实现{@link GroupWork}并返回groupId(如
 *       房间id)来进行分组<b>
 */
public enum QueueType implements IQueueType {
	//SERVER(10, false), // 系统
	//LOGIC(40, false), // 一般游戏逻辑处理线程
	SQL(40, false), // 数据库处理线程，只要处理数据更新，插入
	//GAME(40, false), // 处理游戏服务器之间的通信
	//BROADCAST(10, false), // 广播线程
	//PAY(30, false)// 处理支付
	; 
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