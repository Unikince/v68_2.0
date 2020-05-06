/**
 * 
 */
package com.dmg.bcbm.core.pool;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.config.D;
import com.dmg.common.core.config.RedisRegionConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ASUS
 *
 */
@Slf4j
public class WaterPoolManager {
	private static WaterPoolManager instance = new WaterPoolManager();
	private StringRedisTemplate redisTemplate ;
	private WaterPoolManager() {
	}
	public  static WaterPoolManager instance(){
		return instance;
	}
	public void load(StringRedisTemplate redisTemplate){
		log.info("use bcbmwaterpool");
		this.redisTemplate = redisTemplate;
		init();
	}
	
	public void init() {
		WaterPool pool = getPool();
		if(pool == null){
			log.info("init bcbmwaterpool");
			pool = new WaterPool();
			JSONObject obj = (JSONObject) JSONObject.toJSON(pool);
			for(String key : obj.keySet()){
				obj.put(key, obj.getString(key));
			}
			redisTemplate.opsForHash().putAll(getPoolName(), obj);
		}
	}
	
	private static final String POOLNAME = "REDIS_BCBM_WATERPOOL";

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
	 * 添加到奖池
	 * @param g
	 */
	public strictfp void add(double g){
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		Double increment = ops.increment(getPoolName(), "currentnum", g);
		// 将当前游戏库存值转存到百人场用于统计(目前只用于统计使用)
		redisTemplate.opsForValue().set(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + D.GAME_ID + "_" + 1, String.valueOf(increment));
	}
	
	/**
	 * 更新游戏记录,测试使用
	 */
	/*public void updateRecord(int carId){
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		ops.put(getPoolName(), "gameRecord", String.valueOf(carId));
	}*/
	
	/**
	 * 更新水位,测试使用
	 */
	/*public void updateWater(){
		double currentnum = getPool().getCurrentnum();
		ListOperations<String, String> ops = redisTemplate.opsForList();
		ops.leftPush("TestWater", String.valueOf(currentnum));
	}*/
	
	public String getPoolName() {
		return POOLNAME;
	}

}
