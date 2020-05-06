/**
 * 
 */
package com.zyhy.common_server.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Administrator
 * Redis自定义序列化
 */
public class ObjectRedisTemplate extends RedisTemplate<String, Object>{

	public ObjectRedisTemplate(){
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(objectMapper);
		
		setKeySerializer(new StringRedisSerializer());
		setValueSerializer(serializer);
		setHashKeySerializer(new StringRedisSerializer());
		setHashValueSerializer(serializer);
	}
	
	public ObjectRedisTemplate(LettuceConnectionFactory connectionFactory, int database) {
		this();
		// 切换缓存数据库
		connectionFactory.setDatabase(database);
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}
	
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}
}
