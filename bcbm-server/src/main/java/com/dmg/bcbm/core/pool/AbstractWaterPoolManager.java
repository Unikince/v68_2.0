package com.dmg.bcbm.core.pool;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.SpringContextUtil;
public abstract class AbstractWaterPoolManager {
	protected StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
	/**
	 * 获取奖池信息
	 * @param poolName
	 * @return
	 */
	public WaterPool getPool() {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		Map<String, Object> map = ops.entries(getPoolName());
		if(map == null || map.size() == 0){
			return null;
		}
		JSONObject obj = new JSONObject(map);
		return obj.toJavaObject(WaterPool.class);
	}

	/**
	 * 初始化
	 * @return
	 */
	public abstract void init();

	public abstract String getPoolName();
	
	/**
	 * 添加到奖池
	 * @param g
	 */
	public strictfp void add(double g){
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		ops.increment(getPoolName(), "currentnum", g);
	}

}
