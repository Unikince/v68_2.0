package com.zyhy.lhj_server.game.nnyy;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.pool.DragonGameData;
import com.zyhy.common_lhj.pool.DragonPool;
import com.zyhy.lhj_server.bgmanagement.entity.GamePoolConfig;

public abstract class AbstractDragonPoolManager {
	
	protected Logger log = LoggerFactory.getLogger(AbstractDragonPoolManager.class);
	
	@Autowired
	protected StringRedisTemplate redisTemplate;
	
	/**
	 * 获取奖池信息
	 * @param poolName
	 * @return
	 */
	public DragonPool getPool() {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		Map<String, Object> map = ops.entries(getPoolName());
		JSONObject obj = new JSONObject(map);
		return obj.toJavaObject(DragonPool.class);
	}

	/**
	 * 初始化
	 * @return
	 */
	public abstract DragonPool init(Map<String, GamePoolConfig> initAmount);
	

	/**
	 * 初始化
	 * @param name
	 */
	public void init(String name){
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		ops.put(getPoolName(), name, getInitValue(name));
	}
	
	public abstract String getInitValue(String name);

	public abstract String getPoolName();
	
	/**
	 * 添加到奖池
	 * @param addBet
	 */
	public strictfp void add(Map<String, Double> addBet){
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		ops.increment(getPoolName(), DragonPool.GRAND, addBet.get(DragonPool.GRAND));
		ops.increment(getPoolName(), DragonPool.MAJOR, addBet.get(DragonPool.MAJOR));
		ops.increment(getPoolName(), DragonPool.MINOR, addBet.get(DragonPool.MINOR));
		ops.increment(getPoolName(), DragonPool.MINI, addBet.get(DragonPool.MINI));
	}
	
	public double getAndReset(DragonGameData data) {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		String name = null;
		if(data.getGreenNum() >= 3){
			name = DragonPool.MINI;
		}else if(data.getBlueNum() >= 3){
			name = DragonPool.MINOR;
		}else if(data.getRedNum() >= 3){
			name = DragonPool.MAJOR;
		}else if(data.getGoldNum() >= 3){
			name = DragonPool.GRAND;
		}
		String v = (String) ops.get(getPoolName(), name);
		init(name);
		return Double.parseDouble(v);
	}


}
