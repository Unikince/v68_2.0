package com.dmg.bcbm.logic.work.timer;

import com.dmg.bcbm.core.abs.work.TimeWork;
import com.dmg.bcbm.core.annotation.Cron;

/**
 * @author zhuqd
 * @Date 2017年9月5日
 * @Desc 定时获取redis实例,防止redis自动断开连接
 */
@Cron("15 ** * * * ?")
public class RedisHeart extends TimeWork {
	@Override
	public void init(Object... args) {
	}

	@Override
	public void go() {
		//StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
		//redisTemplate.opsForValue().set("BcbmRedisHeart", "RedisHeart");
		//redisTemplate.getConnectionFactory().getConnection().close();
		//System.out.println("RedisHeart==================================>" + redisTemplate);
	}

}
