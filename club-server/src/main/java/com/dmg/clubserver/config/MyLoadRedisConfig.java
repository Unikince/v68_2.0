/**
 * 
 */
package com.dmg.clubserver.config;

import com.zyhy.common_server.redis.ClubRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author Administrator
 * 服务器自定义Redis
 */
@Configuration
@EnableAutoConfiguration
public class MyLoadRedisConfig {

	@Autowired
	private LettuceConnectionFactory factory;
	
	@Bean
	public ClubRedisTemplate redisTemplate(){
		return new ClubRedisTemplate(factory);
	}
}
