package com.zyhy.common_lhj.pool;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractJackPoolManager {
	
	protected Logger log = LoggerFactory.getLogger(AbstractJackPoolManager.class);
	
	@Autowired
	protected StringRedisTemplate redisTemplate;
	
	/**
	 * 获取奖池信息
	 * @param poolName
	 * @return
	 */
	public JackPool getPool() {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		Map<String, Object> map = ops.entries(getPoolName());
		if(map == null || map.size() == 0){
			return null;
		}
		JSONObject obj = new JSONObject(map);
		return obj.toJavaObject(JackPool.class);
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
	
	/**
	 * 获取订单号
	 * @param num
	 * @return 
	 */
	public strictfp Long getOrderNum(){
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		Long increment = ops.increment(getPoolName(), "orderNum", 1);
		// 订单号到达10亿后初始化
		if (increment == 999999999) {
			ops.put(getPoolName(), "orderNum", "100000000");
		}
		/*// TODO 临时调整订单号使用
		if (Integer.parseInt(orderNum) < 100100000) {
			ops.put(getPoolName(), "orderNum", "100100000");
			orderNum = (String)ops.get(getPoolName(), "orderNum");
		}*/
		return increment;
	}

}
