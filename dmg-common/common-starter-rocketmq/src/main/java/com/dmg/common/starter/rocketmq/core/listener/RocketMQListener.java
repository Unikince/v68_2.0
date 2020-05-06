package com.dmg.common.starter.rocketmq.core.listener;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:50 2019/12/27
 */
public interface RocketMQListener {
	/**
	 * @Author liubo
	 * @Description //TODO 接收消息
	 * @Date 9:52 2019/12/27
	 **/
	void onMessage(MessageExt msg);
}
