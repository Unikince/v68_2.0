/**
 * 
 */
package com.zyhy.lhj_server.manager.pool;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.pool.DragonPool;
import com.zyhy.lhj_server.bgmanagement.entity.GamePoolConfig;
import com.zyhy.lhj_server.bgmanagement.manager.CacheManager;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.nnyy.AbstractDragonPoolManager;

/**
 * @author ASUS
 *
 */
@Component
public class PoolManager extends AbstractDragonPoolManager{
	
	private static PoolManager instance = new PoolManager();
	public void postconstruct(){
		Map<String, GamePoolConfig> initAmount = CacheManager.instance().getPoolConfig(2);
		log.info("use DragonPool");
		init(initAmount);
	}
	private PoolManager() {
	}

	public static PoolManager instance() {
		return instance;
	}
	@Override
	public DragonPool init(Map<String, GamePoolConfig> initAmount) {
		HashOperations<String, String, String> ops = redisTemplate.opsForHash();
		if(!redisTemplate.hasKey(Constants.NNYY_REDIS_LHJ_NNYY_DRAGON_POOL)){
			log.info("DragonPool init");
			DragonPool p = new DragonPool();
			p.setGrand(initAmount.get(DragonPool.GRAND).getInitAmount());
			p.setMajor(initAmount.get(DragonPool.MAJOR).getInitAmount());
			p.setMinor(initAmount.get(DragonPool.MINOR).getInitAmount());
			p.setMini(initAmount.get(DragonPool.MINI).getInitAmount());
			Map<String,String> obj = new HashMap<String, String>();
			obj.put(DragonPool.GRAND, p.getGrand()+"");
			obj.put(DragonPool.MAJOR, p.getMajor()+"");
			obj.put(DragonPool.MINOR, p.getMinor()+"");
			obj.put(DragonPool.MINI, p.getMini()+"");
			ops.putAll(Constants.NNYY_REDIS_LHJ_NNYY_DRAGON_POOL, obj);
			return p;
		}
		return getPool();
	}

	@Override
	public String getPoolName() {
		return Constants.NNYY_REDIS_LHJ_NNYY_DRAGON_POOL;
	}

	@Override
	public String getInitValue(String name) {
		Map<String, GamePoolConfig> initAmount = CacheManager.instance().getPoolConfig(2);
		switch (name) {
		case DragonPool.GRAND:
			return initAmount.get(DragonPool.GRAND).getInitAmount() + "";
		case DragonPool.MAJOR:
			return initAmount.get(DragonPool.MAJOR).getInitAmount() + "";
		case DragonPool.MINOR:
			return initAmount.get(DragonPool.MINOR).getInitAmount() + "";
		case DragonPool.MINI:
			return initAmount.get(DragonPool.MINI).getInitAmount() + "";
		}
		return initAmount.get(DragonPool.MINI).getInitAmount() + "";
	}

}
