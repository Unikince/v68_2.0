package com.dmg.fish.business.config;

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

import com.dmg.fish.business.config.dic.FishCtrlReturnRateDic;
import com.dmg.fish.business.config.dic.FishCtrlStockDic;
import com.dmg.fish.business.config.dic.FishDic;
import com.dmg.fish.business.config.dic.FishRoomDic;
import com.dmg.fish.business.config.dic.FishRouteDic;
import com.dmg.fish.business.config.dic.FishScenceDic;
import com.dmg.fish.business.config.dic.FishStrategyDic;
import com.dmg.fish.core.work.Worker;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigRedis;

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
    private FishDic fishDic;
    @Autowired
    private FishRoomDic fishRoomDic;
    @Autowired
    private FishCtrlReturnRateDic fishCtrlReturnRateDic;
    @Autowired
    private FishCtrlStockDic fishCtrlStockDic;
    @Autowired
    private FishRouteDic fishRouteDic;
    @Autowired
    private FishScenceDic fishScenceDic;
    @Autowired
    private FishStrategyDic fishStrategyDic;

    /** 添加监听器 */
    private void addMessageListener(MessageListenerAdapter listenerAdapter, RedisMessageListenerContainer container) {
        container.addMessageListener(listenerAdapter, new PatternTopic(FishConfigRedis.FISH));
        container.addMessageListener(listenerAdapter, new PatternTopic(FishConfigRedis.FISH_ROOM));
        container.addMessageListener(listenerAdapter, new PatternTopic(FishConfigRedis.FISH_CTRL_RETURN_RATE));
        container.addMessageListener(listenerAdapter, new PatternTopic(FishConfigRedis.FISH_CTRL_STOCK));
        container.addMessageListener(listenerAdapter, new PatternTopic(FishConfigRedis.FISH_ROUTE));
        container.addMessageListener(listenerAdapter, new PatternTopic(FishConfigRedis.FISH_SCENCE));
        container.addMessageListener(listenerAdapter, new PatternTopic(FishConfigRedis.FISH_STRATEGY));
    }

    private void updateConfig() {
        for (String channel : this.channels) {
            try {
                switch (channel) {
                    case FishConfigRedis.FISH: {
                        ConfigInit.this.fishDic.load();
                        break;
                    }
                    case FishConfigRedis.FISH_ROOM: {
                        ConfigInit.this.fishRoomDic.load();
                        break;
                    }
                    case FishConfigRedis.FISH_CTRL_RETURN_RATE: {
                        ConfigInit.this.fishCtrlReturnRateDic.load();
                        break;
                    }
                    case FishConfigRedis.FISH_CTRL_STOCK: {
                        ConfigInit.this.fishCtrlStockDic.load();
                        break;
                    }
                    case FishConfigRedis.FISH_ROUTE: {
                        ConfigInit.this.fishRouteDic.load();
                        break;
                    }
                    case FishConfigRedis.FISH_SCENCE: {
                        ConfigInit.this.fishScenceDic.load();
                        break;
                    }
                    case FishConfigRedis.FISH_STRATEGY: {
                        ConfigInit.this.fishStrategyDic.load();
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
