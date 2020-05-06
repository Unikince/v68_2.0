package com.dmg.bcbm.logic.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.SpringContextUtil;
import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.annotation.Service;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.DefFactory;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.core.pool.WaterPoolManager;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.ServerDef;
import com.dmg.bcbm.logic.entity.SimplePlayer;
import com.dmg.bcbm.logic.entity.UserDTO;
import com.dmg.bcbm.logic.entity.bcbm.BaseRobot;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.service.UserService;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.BetSendDto;
import com.dmg.data.common.dto.SettleRecvDto;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.SyncRoomSendDto;
import com.dmg.data.common.dto.UserRecvDto;
import com.dmg.data.common.dto.UserSendDto;
import com.zyhy.common_server.util.HttpURLUtils;
import com.zyhy.common_server.util.StringUtils;

@Service
public class UserServiceImp implements UserService {
    private NettySend nettySend;
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImp.class);
	private static ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
	
	@Override
	public void init(NettySend nettySend) {
		this.nettySend = nettySend;
	}
	
	@Override
	public SimplePlayer getUserInfo(String roleid, String uuid,int roomId) {
		// 本地测试
		if (SpringContextUtil.isLocal()) {
			Room room = RoomManager.intance().getRoomById(roomId);
			Map<String, Role> roleMap = room.getRoleMap();
			Role role = new Role();
			if (roleMap.containsKey(roleid)) {
				role = roleMap.get(roleid);
			} else {
				role.setRoleId(roleid);
				role.setGold(1000000);
				role.setNickName("牛B轮" + roleid);
				roleMap.put(roleid, role);
			}
			SimplePlayer simplePlayer = new SimplePlayer();
			Role role2 = roleMap.get(roleid);
			simplePlayer.setRoleId(role2.getRoleId());
			simplePlayer.setGold(new BigDecimal(role2.getGold()));
			simplePlayer.setNickname(role2.getNickName());
			return simplePlayer;
		}
		
		if (D.PLATFORM_EBET.equals(uuid)) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("roleid", roleid);
			String s = HttpURLUtils.doPost("http://" + def.getV68_lobby_host() + ":" + def.getV68_lobby_port() + "/slot/selectplayerinfo", map);
			//LOG.info("Ebt用户{}的信息:{}",roleid,s);
			if(s == null){
				LOG.error("获取Ebt用户{}的信息失败", roleid);
				return null;
			}
			JSONObject obj = JSONObject.parseObject(s);
			SimplePlayer p = new SimplePlayer();
			p.setRoleId(obj.getString("roleId"));
			p.setTourist(obj.getIntValue("tourist"));
			p.setGold(new BigDecimal(obj.getDoubleValue("gold")).setScale(2, BigDecimal.ROUND_HALF_UP));
			p.setNickname(obj.getString("nickname"));
			return p;
		} else if (D.PLATFORM_V68.equals(uuid)) {
			UserRecvDto userRecvDto = this.nettySend.getUser(UserSendDto.builder().userId(Long.valueOf(roleid)).build());
	        if (userRecvDto == null) {
	        	LOG.info("获取V68用户{}的信息失败", roleid);
	        	return null;
	        }
	        SimplePlayer user = new SimplePlayer();
	        user.setGold(userRecvDto.getGold());
	        user.setNickname(userRecvDto.getNickname());
	        user.setRoleId(roleid);
	        user.setHeadImage(userRecvDto.getHeadImage());
	        user.setSex(userRecvDto.getSex());
	        user.setTourist(0);
	        LOG.info("V68用户{}的信息:{}",roleid,user);
	        return user;
		}
		return null;
	}
	@Override
	public void bet(String roleid, double betcoin, String uuid,int roomId) {
		if(betcoin == 0){
			return;
		}else if(betcoin < 0){
			return;
		}
		
		// 更新缓存
		Room room = RoomManager.intance().getRoomById(roomId);
		Map<String, Role> roleMap = room.getRoleMap();
		Role role = roleMap.get(roleid);
		syncGoldToCache(roleid,-betcoin,roomId);
		if (SpringContextUtil.isLocal() || role == null) {  // 本地测试或机器人直接返回
			return;
		}
		
		//更新水位
		WaterPoolManager waterPoolManager =WaterPoolManager.instance();
		waterPoolManager.add(betcoin);
		
		// 远程更新
		if (D.PLATFORM_EBET.equals(uuid)) {
			/*Map<String,String> map = new HashMap<String, String>();
			map.put("roleid", roleid);
			map.put("roundId", roundId);
			map.put("updategold", Double.toString(-betcoin));
			map.put("type", "1");
				HttpURLUtils.doPost("http://" + def.getV68_lobby_host() + ":" + def.getV68_lobby_port() + "/slot/UpdatePlayerInfo", map);*/
		} else if (D.PLATFORM_V68.equals(uuid)) {
			nettySend.bet(BetSendDto.builder()
	                .decGold(BigDecimal.valueOf(-betcoin))
	                .gameId(Integer.parseInt(D.GAME_ID))
	                .userId(Long.valueOf(roleid)).build());
		}
	}
	
	@Override
	public void payout(String roleid, double betcoin, String roundId, String uuid,int roomId)  {
		double payout = new BigDecimal(betcoin).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(payout < 0){
			return;
		}
		
		// 更新缓存
		Room room = RoomManager.intance().getRoomById(roomId);
		Map<String, Role> roleMap = room.getRoleMap();
		Role role = roleMap.get(roleid);
		syncGoldToCache(roleid,payout,roomId);
		if (SpringContextUtil.isLocal() || role == null) {  // 本地测试或机器人直接返回
			return;
		}
		
		//更新水位
		WaterPoolManager waterPoolManager = WaterPoolManager.instance();
		waterPoolManager.add(-payout);
		
		// 远程更新
		if (D.PLATFORM_EBET.equals(uuid)) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("roleid", roleid);
			map.put("roundId", roundId);
			map.put("updategold", Double.toString(payout));
			map.put("type", "2");
			HttpURLUtils.doPost("http://" + def.getV68_lobby_host() + ":" + def.getV68_lobby_port() + "/slot/UpdatePlayerInfo", map);
		} else if (D.PLATFORM_V68.equals(uuid)) {
			nettySend.settle(SettleSendDto.builder()
	                .changeGold(BigDecimal.valueOf(betcoin))
	                .userId(Long.valueOf(roleid))
	                .gameId(Integer.parseInt(D.GAME_ID)).build());
		}
	}
	
	/**
	 * 同步金币到缓存
	 * @param userId
	 * @param gameType
	 * @param changeType
	 * @param gold
	 */
	private void syncGoldToCache(String userId,double gold,int roomId) {
		// 更新本地缓存
		Room room = RoomManager.intance().getRoomById(roomId);
		Map<String, Role> roleMap = room.getRoleMap();
		Role role = roleMap.get(userId);
		if (role != null) { // 真人
			role.setGold(role.getGold() + gold);
		} else { //robot
			BaseRobot robot = room.getRobot(userId);
			if (robot != null) {
				robot.setGold(robot.getGold().add(BigDecimal.valueOf(gold)));
			}
		}
    }
	
	 /**
	  * 
	 * @param roomLevel
	 * @param roomId
	 * @param userId
	 */
	@Override
	public void syncRoomFromJoin(int roomLevel, int roomId, long userId) {
		 SyncRoomSendDto sendDto = SyncRoomSendDto.builder().build();
	        sendDto.setGameId(Integer.parseInt(D.GAME_ID));
	        sendDto.setRoomLevel(roomLevel);
	        sendDto.setRoomId(roomId);
	        sendDto.setUserId(userId);
	        this.nettySend.syncRoomAsync(sendDto);
	}

	@Override
	public void syncRoomFromExit(long userId) {
		SyncRoomSendDto sendDto = SyncRoomSendDto.builder().build();
        sendDto.setGameId(Integer.parseInt(D.GAME_ID));
        sendDto.setRoomLevel(0);
        sendDto.setRoomId(0);
        sendDto.setUserId(userId);
        this.nettySend.syncRoomAsync(sendDto);
	}

}
