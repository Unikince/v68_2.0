/**
 * 
 */
package com.zyhy.lhj_server.service.nnyy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WeightUtils;
import com.zyhy.common_lhj.pool.DragonGameData;
import com.zyhy.common_lhj.pool.DragonItem;
import com.zyhy.common_lhj.pool.DragonPool;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.manager.pool.PoolManager;
import com.zyhy.lhj_server.prcess.result.nnyy.NnyyDragonGameResult;
import com.zyhy.lhj_server.prcess.result.nnyy.NnyyGameBetResult;

/**
 * @author ASUS
 *
 */
@Component
public class NnyyDragonGameService {
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private PoolManager poolManager;

	/**
	 * 是否拥有资格
	 * @param roleid
	 * @return
	 */
	public boolean check(String roleid, String uuid) {
		return redisTemplate.hasKey(Constants.NNYY_REDIS_DRAGON_GAME + roleid + "|" + uuid);
	}

	/**
	 * 添加资格
	 * @param roleid
	 */
	public void add(String roleid, Player userinfo, String uuid) {
		//redisTemplate.opsForValue().set(Constants.NNYY_REDIS_DRAGON_GAME + roleid, "1");
		redisTemplate.delete(Constants.NNYY_REDIS_DRAGON_GAME_DATA + roleid + "|" + uuid);
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.NNYY_REDIS_DRAGON_GAME + roleid + "|" + uuid, "1",
						Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.NNYY_REDIS_DRAGON_GAME + roleid + "|" + uuid, "1",
						Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.NNYY_REDIS_DRAGON_GAME + roleid + "|" + uuid, "1",
						Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.NNYY_REDIS_DRAGON_GAME + roleid + "|" + uuid, "1",
						Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
		
	}
	
	/**
	 * 删除资格
	 * @param roleid
	 */
	public void delete(String roleid, String uuid) {
		redisTemplate.delete(Constants.NNYY_REDIS_DRAGON_GAME + roleid + "|" + uuid);
		redisTemplate.delete(Constants.NNYY_REDIS_DRAGON_GAME_DATA + roleid + "|" + uuid);
	}
	
	/**
	 * 获取奖池游戏数据
	 * @param roleid
	 * @param uuid
	 * @return
	 */
	public DragonGameData getDragonGameData(String roleid, String uuid) {
		String key = Constants.NNYY_REDIS_DRAGON_GAME_DATA + roleid + "|" + uuid;
		//游戏数据
		String str = redisTemplate.opsForValue().get(key);
		DragonGameData data = null;
		if(str == null){
			data = new DragonGameData();
		}else{
			data = JSONObject.parseObject(str, DragonGameData.class);
		}
		return data;
	}

	/**
	 * 游戏逻辑
	 * @param roleid
	 * @param userinfo 
	 * @return
	 */
	public NnyyDragonGameResult doGame(String roleid, Player userinfo, String uuid) {
		String key = Constants.NNYY_REDIS_DRAGON_GAME_DATA + roleid + "|" + uuid;
		//游戏数据
		DragonGameData data = getDragonGameData(roleid, uuid);
		//进行游戏
		DragonItem is1 = new DragonItem(DragonPool.GRAND,100);
		DragonItem is2 = new DragonItem(DragonPool.MAJOR,1000);
		DragonItem is3 = new DragonItem(DragonPool.MINOR,10000);
		DragonItem is4 = new DragonItem(DragonPool.MINI,100000);
		List<DragonItem> ws = new ArrayList<>();
		ws.add(is1);
		ws.add(is2);
		ws.add(is3);
		ws.add(is4);
		DragonItem d = WeightUtils.random(ws);
		data.add(d);
		//返回
		NnyyDragonGameResult res = new NnyyDragonGameResult();
		res.setDragonGameData(data);
		res.setName(d.getName());
		if(data.gameOver()){
			delete(roleid, uuid);
			double reward = poolManager.getAndReset(data);
			res.setRewardcoin(new BigDecimal(reward).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
			res.setStatus(2);
		}else{
			//redisTemplate.opsForValue().set(key, JSON.toJSONString(data));
			// 添加奖池数据
			if (Constants.PLATFORM_EBET.equals(uuid)) {
				if (userinfo.getTourist() == -1) {
					redisTemplate.opsForValue().set(key, JSON.toJSONString(data),
							Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
				}else {
					redisTemplate.opsForValue().set(key, JSON.toJSONString(data),
							Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
				}
			} else if (Constants.PLATFORM_V68.equals(uuid)) {
				if (userinfo.getTourist() == 1) {
					redisTemplate.opsForValue().set(key, JSON.toJSONString(data),
							Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
				}else {
					redisTemplate.opsForValue().set(key, JSON.toJSONString(data),
							Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
				}
			}
			
		}
		return res;
	}

	/**
	 * 随机触发游戏
	 * @param result
	 * @param roleid
	 * @param betInfo
	 */
	public void random(NnyyGameBetResult result, String roleid, BetInfo betInfo, Player userinfo, String uuid) {
		Map<String, Double> checkPoolGame = bgManagementServiceImp.checkPoolGame(2);
		if(checkPoolGame.size() > 0){
			result.setPoolGame(1);
			add(roleid, userinfo, uuid);
		}
	}
}
