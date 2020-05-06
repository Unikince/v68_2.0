/**
 * 
 */
package com.dmg.common.starter.rocketmq.consumer;

/**
 * @author ASUS
 *
 */
public interface MQConsumer {
	// 获取分组
	String getGroup();
	// 获取主题
	String getTopic();
	// 获取标签
	String getTags();
	
	void handler(String msg) throws Exception;
}
