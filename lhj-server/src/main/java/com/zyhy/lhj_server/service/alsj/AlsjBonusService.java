/**
 * 
 */
package com.zyhy.lhj_server.service.alsj;

import java.math.BigDecimal;
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
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.alsj.AlsjBonusConfig;
import com.zyhy.lhj_server.game.alsj.AlsjBonusInfo;
import com.zyhy.lhj_server.prcess.result.alsj.AlsjBonusRedResult;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class AlsjBonusService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 保存游戏数据
	 * 
	 * @param bi
	 * @param roleid
	 */
	public void save(AlsjBonusInfo bi, String roleid, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.ALSJREDIS_BONUS + roleid,
				JSONObject.toJSONString(bi));*/
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.ALSJREDIS_BONUS + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.ALSJREDIS_BONUS + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.ALSJREDIS_BONUS + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.ALSJREDIS_BONUS + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
		
		
	}
	/**
	 * 获取游戏数据
	 * @param roleid
	 * @return
	 */
	public AlsjBonusInfo getData(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(
				Constants.ALSJREDIS_BONUS + roleid + "|" + uuid);
		if (str != null) {
			AlsjBonusInfo bi = JSONObject.parseObject(str, AlsjBonusInfo.class);
			return bi;
		}
		return null;
	}
	
	/**
	 * 删除游戏数据
	 * 
	 * @param number
	 * @param roleid
	 */
	public void deleteBonus(String roleid, String uuid) {
		redisTemplate.delete(Constants.ALSJREDIS_BONUS + roleid + "|" + uuid);
	}


	/**
	 * 选择红利游戏
	 * 
	 * @param roleid
	 * @param bi 
	 * @param userinfo 
	 */
	public AlsjBonusRedResult doGameRed(String roleid, AlsjBonusInfo bi, Player userinfo, String uuid) {

		AlsjBonusRedResult result = new AlsjBonusRedResult();

		if (bi != null && bi.getNum() > 0) {
			List<Double> all = new ArrayList<Double>();
			List<Integer> num = new ArrayList<Integer>();
			int count = bi.getNum();
			for (int i = 0; i < count; i++) {
				bi.setNum(bi.getNum() - 1);
				// 随机奖励几倍
				AlsjBonusConfig config = new AlsjBonusConfig();
				int ran = config.getLv();
				double bet = bi.getBetInfo().getTotalBet();
				double money = new BigDecimal(ran * bet).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				bi.setGold(NumberTool.add(bi.getGold(), money).doubleValue());
				save(bi, roleid, userinfo, uuid);
				all.add(money);
				num.add(ran);
				result.setRewardAllCoin(NumberTool.add(
						result.getRewardAllCoin(), money).doubleValue());
			}
			result.setRewardcoin(all);
			result.setNum(num);
		} else {
			result.setRet(2);
			result.setMsg("无效的操作");
		}
		return result;
	}
}
