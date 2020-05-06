/**
 * 
 */
package com.dmg.common.starter.rocketmq.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 默认消费者
 * @author ASUS
 */
public class DefaultConsumer {

    /**
     * NameServer地址
     */
    private String namesrvAddr;
    
    private MQConsumer rocketmqConsumer;
    
    private Map<String, MQConsumer> taghandlers = new HashMap<String, MQConsumer>();
    
    private Logger log = LoggerFactory.getLogger(DefaultConsumer.class);

    private String tags;
    
    public DefaultConsumer(String nameServer, MQConsumer rocketmqConsumer) throws Exception {
    	this.namesrvAddr = nameServer;
    	this.rocketmqConsumer = rocketmqConsumer;
    	addTagHandler(rocketmqConsumer);
    }
    
    public void start() throws MQClientException {
    	setTags();
    	log.info("create consumer :  group = " + rocketmqConsumer.getGroup() + 
    			", topic = " + rocketmqConsumer.getTopic() + ", tags = " + tags + ", namesrvAddr = " + namesrvAddr);

        //消费者的组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketmqConsumer.getGroup());

        //指定NameServer地址，多个地址以 ; 隔开
        consumer.setNamesrvAddr(namesrvAddr);
        //订阅PushTopic下Tag为push的消息
        consumer.subscribe(rocketmqConsumer.getTopic(), tags);
        //广播消息模式
//            consumer.setMessageModel(MessageModel.BROADCASTING);
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        //如果非第一次启动，那么按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                try {
                    for (MessageExt messageExt : list) {
                    	if(log.isDebugEnabled()) {
                    		log.debug(messageExt.toString());
                    	}
                        String messageBody = new String(messageExt.getBody(), "utf-8");
                        taghandlers.get(messageExt.getTags()).handler(messageBody);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("ConsumeResult : {}", ConsumeConcurrentlyStatus.RECONSUME_LATER.toString());
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
                }
                log.info("ConsumeResult : {}", ConsumeConcurrentlyStatus.CONSUME_SUCCESS.toString());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            }


        });
        consumer.start();
    }
    
    private void setTags() {
    	StringBuilder tags = new StringBuilder();
    	int size = taghandlers.size();
    	int i = 0;
    	for(String t : taghandlers.keySet()) {
    		i++;
    		if(i < size) {
    			tags.append(t).append("||");
    		}else {
    			tags.append(t);
    		}
    	}
    	this.tags = tags.toString();
	}

	public void addTagHandler(MQConsumer handler) throws Exception {
		String[] ts = handler.getTags().split("\\|\\|");
		for(String t : ts) {
			String tag = t.trim();
			if(taghandlers.get(tag) != null) {
				throw new Exception("tag exists");
			}
	    	taghandlers.put(tag, handler);
		}
    }
	
}
