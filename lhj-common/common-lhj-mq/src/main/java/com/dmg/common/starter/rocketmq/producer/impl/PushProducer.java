/**
 * 
 */
package com.dmg.common.starter.rocketmq.producer.impl;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.common.starter.rocketmq.message.MessageBuild;
import com.dmg.common.starter.rocketmq.mqenum.Group;
import com.dmg.common.starter.rocketmq.mqenum.Topics;
import com.dmg.common.starter.rocketmq.producer.MQProducer;

/**
 * @author ASUS
 *
 */
public class PushProducer implements MQProducer{

	/**
	 *  生产者的组名
     */
    private String producerGroup;
    
    /**
     * NameServer 地址
     */
    private String namesrvAddr;
    
    private DefaultMQProducer pro;
    
    private Logger log = LoggerFactory.getLogger(LogProducer.class);
    
    public PushProducer(String nameServer, String producerGroup) throws MQClientException {
    	this.producerGroup = producerGroup;
    	this.namesrvAddr = nameServer;
    	init();
    }
  
    public void init() throws MQClientException {
    	DefaultMQProducer pro = new DefaultMQProducer(producerGroup);
    	pro.setNamesrvAddr(namesrvAddr);
    	pro.start();
    	this.pro = pro;
    }
    
    @Override
    public boolean send(String topic, String tags, String data) {
    	try {
    		Message msg = MessageBuild.build(topic, tags, data);
    		if(log.isDebugEnabled()){
				log.debug(data);
			}
			SendResult result = pro.send(msg);
			if(log.isDebugEnabled()){
				log.debug(result.toString());
			}
		} catch (Exception e) {
			log.error("error", e);
			return false;
		}
    	return true;
    }

    public Object send(String tags, String data) {
    	return send(Topics.PUSH, tags, data);
    }

	@Override
	public String getGroup() {
		return Group.PUSH.getProducerGroup();
	}

}
