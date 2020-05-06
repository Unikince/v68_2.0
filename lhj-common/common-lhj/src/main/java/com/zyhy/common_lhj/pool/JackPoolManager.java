/**
 * 
 */
package com.zyhy.common_lhj.pool;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ASUS
 *
 */
public class JackPoolManager extends AbstractJackPoolManager{

	@PostConstruct
	public void load(){
		log.info("use jackpool");
		init();
	}
	
	@Override
	public void init() {
		JackPool pool = getPool();
		if(pool == null){
			log.info("init jackpool");
			pool = new JackPool();
			JSONObject obj = (JSONObject) JSONObject.toJSON(pool);
			for(String key : obj.keySet()){
				obj.put(key, obj.getString(key));
			}
			redisTemplate.opsForHash().putAll(getPoolName(), obj);
		}
	}
	
	private static final String POOLNAME = "REDIS_LHJ_JACKPOOL";

	@Override
	public String getPoolName() {
		return POOLNAME;
	}

}
