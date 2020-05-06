package com.dmg.fish.core.cache;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * reids快速配置封装
 */
@Component
public class RedisGen {
    protected <T> RedisTemplate<String, T> genRedisTemplate(LettuceConnectionFactory factory, Class<T> clazz) {
        return this.genRedisTemplate(factory, clazz, factory.getDatabase());
    }

    protected <T> RedisTemplate<String, T> genRedisTemplate(LettuceConnectionFactory factory, Class<T> clazz, int database) {
        factory.setDatabase(database);
        RedisTemplate<String, T> template = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Object> jacksonSeial = this.genJackSonSeial();
        template.setConnectionFactory(factory);
        template.setValueSerializer(jacksonSeial);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        return template;
    }

    protected Jackson2JsonRedisSerializer<Object> genJackSonSeial() {
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        return jacksonSeial;
    }
}