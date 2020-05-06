package com.dmg.bjlserver.business.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.config.dic.BjlTableDic;
import com.dmg.bjlserver.core.work.Worker;
import com.dmg.gameconfigserverapi.bjl.feign.BjlConfigRedis;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableCaching
public class ConfigInit {
    private ConcurrentHashSet<String> channels = new ConcurrentHashSet<>();
    @Autowired
    private Worker worker;
    @Autowired
    private BjlTableDic bjlTableDic;

    /** 添加监听器 */
    private void addMessageListener(MessageListenerAdapter listenerAdapter, RedisMessageListenerContainer container) {
        container.addMessageListener(listenerAdapter, new PatternTopic(BjlConfigRedis.BJL_TABLE));
    }

    private void updateConfig() {
        for (String channel : this.channels) {
            try {
                switch (channel) {
                    case BjlConfigRedis.BJL_TABLE: {
                        ConfigInit.this.bjlTableDic.load();
                        break;
                    }
                }
                this.channels.remove(channel);
            } catch (Exception e) {
                log.error("配置更新错误", e);
            }
        }
    }

    @PostConstruct
    public void init() {
        this.worker.scheduleWithFixedDelay(() -> {// 做定时器处理错误
            this.updateConfig();
        }, 0, 30, TimeUnit.SECONDS);
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        this.addMessageListener(listenerAdapter, container);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisReceiver receiver) {
        return new MessageListenerAdapter(receiver, "onMessage");
    }

    @Component
    class RedisReceiver implements MessageListener {
        @Override
        public void onMessage(Message message, byte[] pattern) {
            String chinnel = new String(message.getChannel());
            ConfigInit.this.channels.add(chinnel);
            ConfigInit.this.updateConfig();// 收到消息后立即执行一次
        }
    }
}
