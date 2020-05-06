package com.dmg.common.starter.rocketmq.producer.impl;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.common.starter.rocketmq.message.MessageBuild;
import com.dmg.common.starter.rocketmq.mqenum.Topics;
import com.dmg.common.starter.rocketmq.producer.MQProducer;

public class LogProducer implements MQProducer{

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
    
    public LogProducer(String nameServer, String producerGroup) throws MQClientException {
    	this.namesrvAddr = nameServer;
    	this.producerGroup = producerGroup;
    	init();
    }
    
    private void init() throws MQClientException {
    	if(this.pro == null) {
    		DefaultMQProducer pro = new DefaultMQProducer(producerGroup);
        	pro.setNamesrvAddr(namesrvAddr);
        	pro.start();
        	this.pro = pro;
    	}
    }
    
    @PostConstruct
    public void postConstruct() throws MQClientException {
    	init();
    }
    
    public Object send(String tags, String data) {
    	try {
    		Message msg = MessageBuild.build(Topics.GAME_RECORD, tags, data);
    		if(log.isDebugEnabled()){
				log.debug(data);
			}
			pro.send(msg,new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
						log.info(sendResult.toString());
				}
				@Override
				public void onException(Throwable e) {
					log.error("", e);
				}
			});
		} catch (Exception e) {
			log.error("", e);
			return e.getMessage();
		}
    	return "success";
    }

	@Override
	public String getGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean send(String topic, String tags, String msg) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
