/**
 * 
 */
package com.dmg.common.starter.rocketmq.consumer.impl;

import com.dmg.common.starter.rocketmq.consumer.MQConsumer;
import com.dmg.common.starter.rocketmq.mqenum.Group;
import com.dmg.common.starter.rocketmq.mqenum.Topics;

/**
 * @author ASUS
 * 日志消费者
 */
public abstract class PushConsumer implements MQConsumer{

	@Override
	public String getGroup() {
		return Group.PUSH.getConsumerGroup();
	}

	@Override
	public String getTopic() {
		return Topics.PUSH;
	}

	@Override
	public abstract String getTags();

	@Override
	public abstract void handler(String msg) throws Exception;

}
