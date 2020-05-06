package com.dmg.common.starter.rocketmq.annotation;

import com.dmg.common.starter.rocketmq.common.enums.ConsumeMode;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.annotation.*;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:50 2019/12/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQMessageListener {

	/**
	 * 分组
	 * 
	 * @return
	 */
	String consumerGroup();

	/**
	 * 主题
	 * 
	 * @return
	 */
	String topic();

	/**
	 * 标签
	 * 
	 * @return
	 */
	String tags() default "*";

	/**
	 * 消费模式, 同时消费、有序消费
	 */
	ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY;

	/**
	 * 消息模式, 集群消费、广播消费
	 */
	MessageModel messageModel() default MessageModel.CLUSTERING;

	/**
	 * 最大消费线程数
	 */
	int consumeThreadMax() default 64;

	/**
	 * 流量控制阈值，默认值为-1（无限制）
	 */
	int pullThresholdForTopic() default -1;

	/**
	 * 限制缓存消息大小，默认值为-1 MIB（无限制）
	 */
	int pullThresholdSizeForTopic() default -1;
}
