/**
 * 
 */
package com.dmg.common.starter.rocketmq.producer;

/**
 * @author ASUS
 *
 */
public interface MQProducer {

	String getGroup();
	
	boolean send(String topic, String tags, String msg) throws Exception;
}
