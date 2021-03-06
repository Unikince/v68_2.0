/**
 * 
 */
package com.zyhy.common_lhj.logic;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_server.model.LhjBigRewardLog;

/**
 * @author ASUS
 *
 */
public class BaseLogic extends AbstractLogic{

	@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	private MQProducer mqProducer;

	/**
	 * 通杀设置，如满足通杀条件，将重新进行游戏
	 * @param w
	 * @return
	 */
	public boolean kill(WinLineInfo w) {
		double n = w.getIcon().getTrs()[w.getNum() - 1];
		if(n > 100){
			return true;
		}else if(n >= 50 && RandomUtils.nextInt(0, 100)<80){
			return true;
		}else if(n >= 20 && RandomUtils.nextInt(0, 100)<60){
			return true;
		}else if(n >= 5 && RandomUtils.nextInt(0, 100)<30){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 暂定 获得100倍收益为大奖
	 * @return
	 */
	private int bigReward(){
		return 100;
	}
	
	/**
	 * 保存大奖信息
	 * @param gamename
	 * @param bet
	 * @param reward
	 * @param roleid
	 * @throws Exception 
	 */
	public void saveBigReward(String gamename, BetInfo bet, double reward, String roleid) throws Exception {
		if(reward != 0 && reward/bet.getGold() >= bigReward()){
			LhjBigRewardLog log = new LhjBigRewardLog();
			log.setBet(bet.getGold());
			log.setTime(System.currentTimeMillis());
			log.setGamename(gamename);
			log.setLine(bet.getNum());
			log.setReward(reward);
			log.setRoleid(roleid);
			log.setType(LhjBigRewardLog.TYPE_NORMAL);
			mqProducer.sendAsync(JSONObject.toJSONString(log));
		}
	}
}
