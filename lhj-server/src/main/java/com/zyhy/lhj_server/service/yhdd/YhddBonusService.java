/**
 * 
 */
package com.zyhy.lhj_server.service.yhdd;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.Window;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.yhdd.YhddBonusConfig;
import com.zyhy.lhj_server.game.yhdd.YhddBonusInfo;
import com.zyhy.lhj_server.prcess.result.yhdd.YhddBonusCarResult;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class YhddBonusService {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	/**
	 * 获得bonus中奖信息
	 * @param ws
	 * @return
	 */
	public List<Window> getBonusWindows(List<Window> ws){
		Window start = null;
		Window end = null;
		for(Window w : ws){
			if(w.getId() == 1){
				if(w.getIcon().IsBonus()){
					start = w;
				}
			}
			if(w.getId() == 5){
				if(w.getIcon().IsBonus()){
					end = w;
				}
			}
		}
		if(start != null && end != null){
			List<Window> r = new ArrayList<Window>(2);
			r.add(start);
			r.add(end);
			return r;
		}
		return null;
	}

	/**
	 * 保存游戏数据
	 * @param bi
	 * @param roleid
	 */
	public void save(YhddBonusInfo bi, String roleid, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.YHDD_REDIS_BONUS + roleid, 
				JSONObject.toJSONString(bi));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.YHDD_REDIS_BONUS + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.YHDD_REDIS_BONUS + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.YHDD_REDIS_BONUS + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.YHDD_REDIS_BONUS + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
		
		
	}
	
	/**
	 * 删除游戏数据
	 * @param roleid
	 */
	public void delete(String roleid, String uuid) {
		redisTemplate.delete(Constants.YHDD_REDIS_BONUS + roleid + "|" + uuid);
	}

	/**
	 * 获取免费游戏信息
	 * @param roleid
	 * @param car
	 * @param line
	 */

	public YhddBonusInfo getData(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(Constants.YHDD_REDIS_BONUS + roleid + "|" + uuid);
		if(str != null){
			YhddBonusInfo bi = JSONObject.parseObject(str, YhddBonusInfo.class);
			return bi;
		}
		return null;
	}

	/**
	 * 选择游戏
	 * @param roleid
	 * @param car
	 * @param bi 
	 */
	public YhddBonusCarResult doGameLantern(String roleid, int car, YhddBonusInfo bi) {
		
		YhddBonusCarResult result = new YhddBonusCarResult();
		if(bi != null && bi.getNum() == 0){
			List<Integer> cars = new ArrayList<Integer>(6);
			YhddBonusConfig yhddBonusConfig = new YhddBonusConfig();
			cars.add(yhddBonusConfig.getNum());
			cars.add(yhddBonusConfig.getNum());
			cars.add(yhddBonusConfig.getNum());
			cars.add(yhddBonusConfig.getNum());
			cars.add(yhddBonusConfig.getNum());
			cars.add(yhddBonusConfig.getNum());
			result.setGroup(cars);
			int free = cars.get(car-1);
			result.setNum(free);
			bi.setNum(free);
			bi.setNumGroup(cars);
			result.setYhddBonusInfo(bi);
		}else{
			result.setRet(2);
			result.setMsg("无效的操作");
		}
		return result;
	}

	public YhddBonusCarResult doGameBox(String roleid, int line, YhddBonusInfo bi) {
		YhddBonusCarResult result = new YhddBonusCarResult();
		if(bi != null && bi.getMul() == 0){
			List<Integer> cars = new ArrayList<Integer>(6);
			YhddBonusConfig yhddBonusConfig = new YhddBonusConfig();
			cars.add(yhddBonusConfig.getLv());
			cars.add(yhddBonusConfig.getLv());
			cars.add(yhddBonusConfig.getLv());
			cars.add(yhddBonusConfig.getLv());
			cars.add(yhddBonusConfig.getLv());
			cars.add(yhddBonusConfig.getLv());
			result.setGroup(cars);
			int free = cars.get(line-1);
			result.setNum(free);
			bi.setMul(free);
			bi.setMulGroup(cars);
			result.setYhddBonusInfo(bi);
		}else{
			result.setRet(2);
			result.setMsg("无效的操作");
		}
		return result;
	}
}
