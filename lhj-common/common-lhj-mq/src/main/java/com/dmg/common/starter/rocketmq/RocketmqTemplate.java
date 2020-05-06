/**
 * 
 */
package com.dmg.common.starter.rocketmq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dmg.common.starter.rocketmq.consumer.DefaultConsumer;
import com.dmg.common.starter.rocketmq.consumer.MQConsumer;
import com.dmg.common.starter.rocketmq.mqenum.Group;
import com.dmg.common.starter.rocketmq.producer.impl.LogProducer;
import com.dmg.common.starter.rocketmq.producer.impl.PushProducer;

/**
 * @author ASUS
 *
 */
public class RocketmqTemplate {
	
	private Logger log = LoggerFactory.getLogger(RocketmqTemplate.class);
	
	@Value("${apache.rocketmq.namesrvAddr}")
	private String nameServer;
	
	/**
	 * 日志
	 */
	@Autowired(required = false)
	private LogProducer logProducer;
	
	/**
	 * 推送
	 */
	@Autowired(required = false)
	private PushProducer pushProducer;
	
	@Autowired(required = false)
	private List<MQConsumer> consumerHandlers;
	
	private Map<String,DefaultConsumer> consumers = new HashMap<String, DefaultConsumer>();
	
	@PostConstruct
	private void postConstruct() throws Exception {
		//初始化并合并
		if(consumerHandlers != null && consumerHandlers.size() > 0) {
			Map<String,String> groups = new HashMap<String, String>();
			for(MQConsumer handler : consumerHandlers) {
				//同一服务器，不能出现group相同，topic不同。合并group相同，且topic相同的handler。
				if(StringUtils.isEmpty(handler.getGroup()) || StringUtils.isEmpty(handler.getTopic())
						|| StringUtils.isEmpty(handler.getTags())) {
					throw new Exception("group || topic || tags can not null");
				}
				String t = groups.get(handler.getGroup());
				if(t != null && !t.equals(handler.getTopic())) {
					throw new Exception("group already exist");
				}
				DefaultConsumer c = consumers.get(handler.getGroup());
				if(c == null) {
					c = new DefaultConsumer(nameServer, handler);
					consumers.put(handler.getGroup(), c);
				}else {
					c.addTagHandler(handler);
				}
				groups.put(handler.getGroup(), handler.getTopic());
			}
			//启动
			for(DefaultConsumer c : consumers.values()) {
				c.start();
			}
		}
	}
	
	/**
	 * 默认topic:log
	 * @param tags
	 * @param obj
	 */
	public void pushLog(String tags, Object obj){
		if(logProducer == null){
			try {
				logProducer = new LogProducer(nameServer, Group.GAME_RECORD.getProducerGroup());
			} catch (MQClientException e) {
				log.error("create LogProducer", e);
			}
		}
		logProducer.send(tags, obj.toString());
	}
	
	/**
	 * 默认topic: push
	 * @param tags
	 * @param obj
	 */
	public void pushMsg(String tags, Object obj){
		if(pushProducer == null){
			try {
				pushProducer = new PushProducer(nameServer, Group.PUSH.getProducerGroup());
			} catch (MQClientException e) {
				e.printStackTrace();
				log.error("create pushProducer", e);
			}
		}
		pushProducer.send(tags, obj.toString());
	}
	
	public void pushMsg(String topic, String tags, Object obj){
		if(pushProducer == null){
			try {
				pushProducer = new PushProducer(nameServer, Group.PUSH.getProducerGroup());
			} catch (MQClientException e) {
				e.printStackTrace();
				log.error("create pushProducer", e);
			}
		}
		pushProducer.send(topic, tags, obj.toString());
	}

	public LogProducer getLogProducer() {
		return logProducer;
	}

	public void setLogProducer(LogProducer logProducer) {
		this.logProducer = logProducer;
	}

	public PushProducer getPushProducer() {
		return pushProducer;
	}

	public void setPushProducer(PushProducer pushProducer) {
		this.pushProducer = pushProducer;
	}

}
