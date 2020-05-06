package com.dmg.bjlserver.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.dmg.bjlserver.business.model.game.SeatRecord;

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

    @Bean
    public RedisTemplate<String, Integer> intRedisTemplate(LettuceConnectionFactory factory) {
        return this.redisGen.genRedisTemplate(factory, Integer.class);
    }

    @Bean
    public RedisTemplate<String, Long> longRedisTemplate(LettuceConnectionFactory factory) {
        return this.redisGen.genRedisTemplate(factory, Long.class);
    }

    @Bean
    public RedisTemplate<String, SeatRecord> seatRecordRedisTemplate(LettuceConnectionFactory factory) {
        return this.redisGen.genRedisTemplate(factory, SeatRecord.class);
    }
}