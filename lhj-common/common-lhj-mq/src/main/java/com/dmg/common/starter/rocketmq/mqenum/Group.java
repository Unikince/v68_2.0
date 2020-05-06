/**
 * 
 */
package com.dmg.common.starter.rocketmq.mqenum;

/**
 * @author ASUS
 * 分组信息
 */
public enum Group implements MQGroup{
	
	LOG("LOG_CONSUMER","LOG_PRODUCER"),
	
	PUSH("PUSH_CONSUMER","PUSH_PRODUCER"),
	
	GAME_RECORD("GAME_RECORD_CONSUMER", "GAME_RECORD_PRODUCER"),
	;
	
	private String consumer;
	
	private String producer;
	
	private Group(String consumer, String producer) {
		this.consumer = consumer;
		this.producer = producer;
	}

	public String getConsumerGroup() {
		return consumer;
	}

	public String getProducerGroup() {
		return producer;
	}
	
	
	
}
