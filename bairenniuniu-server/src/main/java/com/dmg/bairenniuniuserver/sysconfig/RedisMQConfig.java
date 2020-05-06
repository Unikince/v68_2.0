package com.dmg.bairenniuniuserver.sysconfig;

import com.dmg.common.core.config.RedisRegionConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @description:Redis消息处理配置类
 * @return
 * @author mice
 * @date 2019/10/9
*/
@Configuration
public class RedisMQConfig {

	/**
	 * 注入消息监听容器
	 *
	 * @param connectionFactory 连接工厂
	 * @param listenerAdapter   监听处理器1
	 * @param listenerAdapter   监听处理器2 (参数名称需和监听处理器的方法名称一致，因为@Bean注解默认注入的id就是方法名称)
	 * @return
	 */
	@Bean
	RedisMessageListenerContainer container(LettuceConnectionFactory connectionFactory,
											MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		//订阅一个叫mq_01 的信道
		container.addMessageListener(listenerAdapter, new PatternTopic(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL));
		//这个container 可以添加多个 messageListener
		return container;
	}

	/**
	 * 消息监听处理器1
	 *
	 * @param receiver 处理器类
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
		//给messageListenerAdapter 传入一个消息接收的处理器，利用反射的方法调用“receiveMessage”
		return new MessageListenerAdapter(receiver, "updateBairenGameConfig"); //receiveMessage：接收消息的方法名称
	}

}