/**
 * 
 */
package com.zyhy.lhj_server.service.gsgl;

import java.util.ArrayList;
import java.util.Collections;
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
import com.zyhy.lhj_server.game.gsgl.GsglBonusConfig;
import com.zyhy.lhj_server.game.gsgl.GsglBonusInfo;
import com.zyhy.lhj_server.prcess.result.gsgl.GsglBonusCarResult;

@Order
@Component
public class GsglBonusService {
	
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
	public void save(GsglBonusInfo bi, String roleid, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.GSGL_REDIS_BONUS + roleid, 
				JSONObject.toJSONString(bi));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.GSGL_REDIS_BONUS + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.GSGL_REDIS_BONUS + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.GSGL_REDIS_BONUS + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.GSGL_REDIS_BONUS + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
		
		
	}
	
	/**
	 * 获取免费游戏数据
	 * @param roleid
	 * @return
	 */
	public GsglBonusInfo getData(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(Constants.GSGL_REDIS_BONUS + roleid + "|" + uuid);
		if(str != null){
			GsglBonusInfo bi = JSONObject.parseObject(str, GsglBonusInfo.class);
			return bi;
		}
		return null;
	}
	
	/**
	 * 删除游戏数据
	 * @param bi
	 * @param roleid
	 */
	public void del(String roleid, String uuid) {
		redisTemplate.delete(Constants.GSGL_REDIS_BONUS + roleid + "|" + uuid);
	}

	/**
	 * 进行游戏，获得倍数和免费次数
	 * @param roleid
	 * @param car
	 * @param line
	 */
	public void doGame(String roleid, int car, int line, String uuid) {
		GsglBonusInfo bi = getData(roleid, uuid);
		if(bi != null && bi.getMul() == 0){
			List<Integer> cars = new ArrayList<Integer>(3);
			cars.add(1);
			cars.add(2);
			cars.add(3);
			List<Integer> lines  = new ArrayList<Integer>(car);
			Collections.shuffle(cars);
			Collections.shuffle(lines);
			
		}
	}

	/**
	 * 选择汽车游戏
	 * @param roleid
	 * @param car
	 * @param bi 
	 */
	public GsglBonusCarResult doGameCar(String roleid, int car, GsglBonusInfo bi) {
//		//测试代码 新建BonusInfo
//		bi = new GsglBonusInfo();
//		bi.setBetInfo(new BetInfo(GsglBetEnum.h.getBetcoin(), 9));
		
		GsglBonusCarResult result = new GsglBonusCarResult();
		if(bi != null && bi.getNum() == 0 && bi.getMul() == 0){
			List<Integer> cars = new ArrayList<Integer>(3);
			GsglBonusConfig bonusConfig = new GsglBonusConfig();
			cars.add(bonusConfig.getNum());
			cars.add(bonusConfig.getNum());
			cars.add(bonusConfig.getNum());
			result.setGroup(cars);
			int free = cars.get(car-1);
			result.setNum(free);
			bi.setNum(free);
			result.setBonusInfo(bi);
		}else{
			result.setRet(2);
			result.setMsg("无效的操作");
		}
		return result;
	}

	public GsglBonusCarResult doGameLine(String roleid, int line, GsglBonusInfo bi) {
		GsglBonusCarResult result = new GsglBonusCarResult();
		if(bi != null && bi.getNum() > 0 && bi.getMul() == 0){
			List<Integer> cars = new ArrayList<Integer>(3);
			GsglBonusConfig bonusConfig = new GsglBonusConfig();
			cars.add(bonusConfig.getLv());
			cars.add(bonusConfig.getLv());
			cars.add(bonusConfig.getLv());
			result.setGroup(cars);
			int free = cars.get(line-1);
			result.setNum(free);
			bi.setMul(free);
			result.setBonusInfo(bi);
		}else{
			result.setRet(2);
			result.setMsg("无效的操作");
		}
		return result;
	}
}
