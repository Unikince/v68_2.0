package com.dmg.doudizhuserver.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * reids配置指定对象模板
 */
@Configuration
public class RedisTemplateConfig {
    @Autowired
    RedisGen redisGen;

    @Bean
    public RedisTemplate<String, Object> objectRedisTemplate(LettuceConnectionFactory factory) {
        return this.redisGen.genRedisTemplate(factory, Object.class);
    }

//    @Bean
//    public RedisTemplate<String, Integer> longRedisTemplate(LettuceConnectionFactory factory) {
//        return this.redisGen.genRedisTemplate(factory, Integer.class);
//    }
}