/**
 * 
 */
package com.zyhy.lhj_server.userservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.BetSendDto;
import com.dmg.data.common.dto.SettleRecvDto;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.UserRecvDto;
import com.dmg.data.common.dto.UserSendDto;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.context.SpringContext;
import com.zyhy.common_lhj.pool.JackPoolManager;
import com.zyhy.common_server.util.HttpURLUtils;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.constants.Constants;

/**
 * @author ASUS
 *
 */
@Service
public class UserService {
	// 大厅地址
	@Value("${global.account_server_url}")
	private String accountHost;
	@Value("${global.account_server_port}")
	private String accountPort;
	@Autowired
    private NettySend nettySend;
	@Autowired
	private MQProducer mqProducer;
	@Autowired
	private JackPoolManager jackPoolManager;
	private static Map<String,Player> players = new HashMap<String, Player>();
	private Map<Integer,Map<String,Long>> userHeartInfo = new ConcurrentHashMap<>();
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	/**
	 * 获取用户信息
	 * @param roleid
	 * @return
	 */
	public Player getUserInfo(String roleid, String uuid){
		Map<String,String> map = new HashMap<String, String>();
		map.put("roleid", roleid);
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if(SpringContext.isLocal()){
				Player p = players.get(roleid);
				if(p == null){
					p = new Player();
					p.setRoleId(roleid);
					p.setGold(3000000);
					p.setNickname("EBET本地测试" + roleid);
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
				LOG.info("userInfo :" + p);
				return p;
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if(SpringContext.isLocal()){
				Player p = players.get(roleid);
				if(p == null){
					p = new Player();
					p.setRoleId(roleid);
					p.setGold(3000000);
					p.setNickname("V68本地测试" + roleid);
					players.put(roleid, p);
				}
				return p;
			}else{
				UserRecvDto userRecvDto = this.nettySend.getUser(UserSendDto.builder().userId(Long.valueOf(roleid)).build());
		        if (userRecvDto == null) {
		        	LOG.info("获取V68用户{}的信息失败", roleid);
		        	return null;
		        }
		        Player user = new Player();
		        user.setGold(userRecvDto.getGold().doubleValue());
		        user.setNickname(userRecvDto.getNickname());
		        user.setHeadImage(userRecvDto.getHeadImage());
		        user.setSex(userRecvDto.getSex());
		        user.setRoleId(roleid);
		        user.setTourist(0);
		        LOG.info("V68用户{}的信息:{}",roleid,user);
		        return user;
			}
		}
		return null;
	}
	
	/**
	 * 下注
	 * @param roleid
	 * @param betcoin
	 * @param roundId 
	 * @return 
	 * @return
	 * @throws Exception 
	 */
	public boolean bet(String roleid, double betcoin, String roundId, String uuid,int betType,Integer gameId) throws Exception {
		
		if(betcoin < 0){
			throw new Exception("bet cannot < 0");
		}
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("roleid", roleid);
			map.put("roundId", roundId);
			map.put("updategold", Double.toString(-betcoin));
			map.put("type", (10 + betType + ""));
			if(SpringContext.isLocal()){
				Player p = players.get(roleid);
				p.setGold(NumberTool.subtract(p.getGold(), betcoin).doubleValue());
			}else{
				HttpURLUtils.doPost(accountHost + ":" + accountPort + "/slot/UpdatePlayerInfo", map);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if(SpringContext.isLocal()){
				Player p = players.get(roleid);
				p.setGold(NumberTool.subtract(p.getGold(), betcoin).doubleValue());
			}else{
				BetRecvDto bet = this.nettySend.bet(BetSendDto.builder()
		                .decGold(BigDecimal.valueOf(-betcoin))
		                .gameId(gameId)
		                .userId(Long.valueOf(roleid)).build());
				if (bet.getCode() != 0) {
					return false;
				}
				LOG.info("=====> 玩家{},在游戏{}中进行下注,下注额度{}!",roleid,gameId,betcoin);
			}
		}
		return true;
	}
	
	/**
	 * 派彩
	 * @param roleid
	 * @param betcoin
	 * @param roundId 
	 * @return 
	 * @return
	 * @throws Exception 
	 */
	public boolean payout(String roleid, double betcoin, String roundId, String uuid,Integer gameId) throws Exception {
		double payout = new BigDecimal(betcoin).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(payout < 0){
			throw new Exception("reward cannot < 0");
		}
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
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
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if(SpringContext.isLocal()){
				Player p = players.get(roleid);
				p.setGold(NumberTool.add(p.getGold(), payout).doubleValue());
			}else{
				SettleRecvDto settle = this.nettySend.settle(SettleSendDto.builder()
		                .changeGold(BigDecimal.valueOf(betcoin))
		                .userId(Long.valueOf(roleid))
		                .gameId(gameId).build());
				if (settle.getCode() != 0) {
					return false;
				}
				LOG.info("=====> 玩家{},在游戏{}中进行派彩,派彩额度{}!",roleid,gameId,betcoin);
			}
		}
		return true;
	}
	
	
	
	
	/**
	 * 发送日志
	 * @param record
	 * @throws Exception
	 */
	public void sendLog(GameRecordDTO<?> record) throws Exception{
		mqProducer.sendAsync(JSONObject.toJSONString(record,SerializerFeature.DisableCircularReferenceDetect));
		LOG.info("=====> sendLog : " + JSONObject.toJSONString(record,SerializerFeature.DisableCircularReferenceDetect));
	}
	
	/**
	 *  用户心跳信息
	 */
	public void userHeartInfo(String roleid, int gameId){
		if (userHeartInfo.containsKey(gameId)) {
			Map<String, Long> map = userHeartInfo.get(gameId);
			map.put(roleid, System.currentTimeMillis());
		} else {
			Map<String, Long> map = new ConcurrentHashMap<>();
			map.put(roleid, System.currentTimeMillis());
			userHeartInfo.put(gameId, map);
		}
	}
	
	/**
	 * 获取订单id
	 * @return
	 */
	public Long getOrderId(){
		return jackPoolManager.getOrderNum();
	}
	public String getAccountHost() {
		return accountHost;
	}
	public String getAccountPort() {
		return accountPort;
	}
	public Map<Integer, Map<String, Long>> getUserHeartInfo() {
		return userHeartInfo;
	}
}
