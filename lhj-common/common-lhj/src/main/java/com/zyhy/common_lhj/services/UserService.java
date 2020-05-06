/**
 * 
 */
package com.zyhy.common_lhj.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.SynthesizedAnnotation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.constants.Lhj;
import com.zyhy.common_lhj.context.SpringContext;
import com.zyhy.common_lhj.pool.JackPoolManager;
import com.zyhy.common_server.util.HttpURLUtils;
import com.zyhy.common_server.util.NumberTool;

/**
 * @author ASUS
 *
 */
@Service
public class UserService {

	@Value("${global.account_server_url}")
	private String accountHost;
	
	@Value("${global.account_server_port}")
	private String accountPort;

	@Autowired
	protected StringRedisTemplate redisTemplate;
	@Autowired
	private JackPoolManager jackPoolManager;
	private static Map<String,Player> players = new HashMap<String, Player>();
	
	/**
	 * 获取用户信息
	 * @param roleid
	 * @return
	 */
	public Player getUserInfo(String roleid){
		Map<String,String> map = new HashMap<String, String>();
		map.put("roleid", roleid);
		if(SpringContext.isLocal()){
			Player p = players.get(roleid);
			if(p == null){
				p = new Player();
				p.setRoleId(roleid);
				p.setGold(300);
				p.setNickname("本地测试" + roleid);
				players.put(roleid, p);
			}
			return p;
		}else{
			String s = HttpURLUtils.doPost(accountHost + ":" + accountPort + "/slot/selectplayerinfo", map);
			if(s == null){
				return null;
			}
			JSONObject obj = JSONObject.parseObject(s);
			Player p = new Player();
			p.setRoleId(obj.getString("roleId"));
			p.setTourist(obj.getIntValue("tourist"));
			p.setGold(new BigDecimal(obj.getDoubleValue("gold")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			p.setNickname(obj.getString("nickname"));
			return p;
		}
	}
	public void getOrderNum(){
	
	}
	/**
	 * 下注
	 * @param roleid
	 * @param betcoin
	 * @param roundId 
	 * @return
	 * @throws Exception 
	 */
	public void bet(String roleid, double betcoin, String roundId) throws Exception {
		
		if(betcoin == 0){
			return;
		}else if(betcoin < 0){
			throw new Exception("bet cannot < 0");
		}
		//奖池
		jackPoolManager.add(betcoin);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("roleid", roleid);
		map.put("roundId", roundId);
		map.put("updategold", Double.toString(-betcoin));
		map.put("type", "1");
		if(SpringContext.isLocal()){
			Player p = players.get(roleid);
			p.setGold(NumberTool.subtract(p.getGold(), betcoin).doubleValue());
		}else{
			HttpURLUtils.doPost(accountHost + ":" + accountPort + "/slot/UpdatePlayerInfo", map);
		}
	}
	
	/**
	 * 派彩
	 * @param roleid
	 * @param betcoin
	 * @param roundId 
	 * @return
	 * @throws Exception 
	 */
	public void payout(String roleid, double betcoin, String roundId) throws Exception {
		double payout = new BigDecimal(betcoin).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(payout > 0){
			addUserLuck(roleid, -1);
		}else if(payout < 0){
			throw new Exception("reward cannot < 0");
		}else{
			addUserLuck(roleid, 1);
		}
		//奖池
		jackPoolManager.add(-payout);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("roleid", roleid);
		map.put("roundId", roundId);
		map.put("updategold", Double.toString(payout));
		map.put("type", "2");
		if(SpringContext.isLocal()){
			Player p = players.get(roleid);
			p.setGold(NumberTool.add(p.getGold(), payout).doubleValue());
		}else{
			HttpURLUtils.doPost(accountHost + ":" + accountPort + "/slot/UpdatePlayerInfo", map);
		}
	}
	
	/**
	 * 校验获得奖励的倍数
	 * @param bet 下注额度
	 * @param reward 奖励
	 * @param currentnum 当前水位
	 * @return 
	 */
	public boolean checkBigReward(double bet, double reward, double currentnum){
		// 小于20倍为普通中奖
		if (reward/bet < 20) {
			return true;
		}
		// 大奖几率
		int big = RandomUtils.nextInt(0, 1000);
		if (currentnum >= 1500000 && reward/bet >= 20 && reward/bet < 30 && big < 2) {
			return true;
		}
		// 超大奖几率
		int superBig = RandomUtils.nextInt(0, 10000);
		if (currentnum >= 2000000 && reward/bet >= 30 && reward/bet < 50 && superBig < 2) {
			return true;
		}
		return false;
	}
	/**
	 * 获取幸运值
	 * @param roleid
	 * @return
	 */
	public int getUserLuck(String roleid){
		String v = redisTemplate.opsForValue().get(Lhj.USER_LUCK + roleid);
		if(v != null){
			return Integer.parseInt(v);
		}
		return 0;
	}
	
	/**
	 * 增加幸运值
	 * @param roleid
	 * @param luck
	 */
	private void addUserLuck(String roleid, int luck){
		//String key = Lhj.USER_LUCK + roleid;
		//redisTemplate.opsForValue().increment(key, luck);
	}
}
