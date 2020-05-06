/**
 * 
 */
package com.dmg.zhajinhuaserver.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author linanjun
 * 服务管理器默认实现
 */
@Service
public abstract class AbstractManager {

	@Autowired
	protected StringRedisTemplate redisTemplate;
}
