package com.dmg.bcbm.logic.message.websocket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.abs.def.type.IQueueType;
import com.dmg.bcbm.core.abs.message.NoLoginMessageNet;
import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.annotation.Net;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.core.manager.ServiceManager;
import com.dmg.bcbm.core.manager.StateTimerManager;
import com.dmg.bcbm.logic.def.QueueType;
import com.dmg.bcbm.logic.entity.SimplePlayer;
import com.dmg.bcbm.logic.entity.bcbm.Banker;
import com.dmg.bcbm.logic.entity.bcbm.BmbcWinInfo;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.entity.bcbm.RoomInfoResult;
import com.dmg.bcbm.logic.entity.bcbm.UserBetInfo;
import com.dmg.bcbm.logic.service.UserService;
import com.dmg.gameconfigserverapi.dto.BairenControlConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO.WaterPoolConfigDTO;
import com.google.common.collect.Lists;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * 玩家加入房间
 */
@Net(D.JOINROOM)
public class JoinRoom extends NoLoginMessageNet {
	private static final Logger LOG = LoggerFactory.getLogger(JoinRoom.class);
	private static StateTimerManager stateTimerManager = StateTimerManager.instance();
	private static UserService userService = ServiceManager.instance().get(UserService.class);
	@Override
	public void go(ChannelHandlerContext ctx, JSONObject message) {
		//{"forward":1,"roleid":"805","cmd":"joinRoom","uuid":"1","roomId":"1"}
		String roleid = message.getString("roleid");
		String uuid = message.getString("uuid");
		//int roomId = message.getIntValue("roomId");
		int roomId = 1;
		if (RoomManager.intance().getShutdownServer()) {
			ctx.writeAndFlush(ByteHelper.createFrameMessage
					(JsonUtil.create().cmd("joinRoom").put("roomId", roomId).put("shutdownServer", 6666).toJsonString()));
			return;
		}
		
		// 获取房间
		Room room = RoomManager.intance().getRoomById(roomId);
		// 获取用户信息
		SimplePlayer player = userService.getUserInfo(roleid, uuid,roomId);
		if (player == null) {
			ctx.writeAndFlush(ByteHelper.createFrameMessage
					(JsonUtil.create().cmd("joinRoom").put("roomId", room.getRoomId()).errorCode("玩家信息有误").toJsonString()));
			return;
		}
		
		BairenGameConfigDTO gameConfig = null;
		BairenFileConfigDTO fileConfig = null;
		try {
			gameConfig = room.getGameConfig().get(1); // 获取游戏配置
			fileConfig = gameConfig.getBairenFileConfigDTO(); // 场次配置
		} catch (Exception e) {
			ctx.writeAndFlush(ByteHelper.createFrameMessage
					(JsonUtil.create().cmd("joinRoom").put("roomId", room.getRoomId()).errorCode("游戏还在准备中").toJsonString()));
			return;
		}
		
		// 判断玩家金币是否达到入场要求
		Integer fileLimit = fileConfig.getFileLimit(); // 准入金额
		if (player.getGold().doubleValue() < fileLimit) {
			ctx.writeAndFlush(ByteHelper.createFrameMessage
					(JsonUtil.create().cmd("joinRoom").put("roomId", room.getRoomId()).errorCode("金币达不到入场要求").toJsonString()));
			return;
		} else {
			// 保存玩家通讯信息
			room.addRoleChannel(ctx, roleid);
			// 保存玩家角色信息
			Role role = new Role();
			role.setCtx(ctx);
			role.setActive(true);
			role.setRoleId(roleid);
			role.setNickName(player.getNickname());
			role.setUuid(uuid);
			role.setGold(player.getGold().doubleValue());
			room.addRole(role);
			// 保存玩家下注前金额
			if (!room.getStartBetGoldMap().containsKey(roleid)) {
				room.getStartBetGoldMap().put(roleid, role.getGold());
			}
			LOG.info("room = " + room.getRoomId() + " =====>" + "玩家{}加入房间,当前房间人数为: {}人" , roleid, room.getChannelTotal());
		}
		
		
		// 获取赌注列表
		String betChips = fileConfig.getBetChips();
		String[] split = betChips.split("\\,");
		List<String> betList = Arrays.asList(split);
		// 获取当前在线人数
		//int onlineNum = roleManager.getChannelTotal();
		// 返回界面信息
		RoomInfoResult roomInfoResult = new RoomInfoResult();
		roomInfoResult.setBetList(betList);  // 返回下注列表
		// 返回战绩列表
		List<BmbcWinInfo> gameRecord = room.getGameRecord();
		roomInfoResult.setGameRecord(gameRecord);
		// 返回状态计时器
		boolean wait = false;
		//第一位玩家进入房间为从下注开始
		/*if (onlineNum < 2) {
			stateTimerManager.setTime(0);
			stateTimerManager.setState(1);
			roomInfoResult.getTime().put("state", stateTimerManager.getState());
			roomInfoResult.getTime().put("time", D.GAMESTATE1);
			//房间有人时进入
		} else {*/
			int state = stateTimerManager.getTimer(roomId).getState();
			boolean end = stateTimerManager.getTimer(roomId).isEnd();
			if (state == 1) {
				roomInfoResult.getTime().put("state", stateTimerManager.getTimer(roomId).getState());
				roomInfoResult.getTime().put("time", (D.GAMESTATE1 + 1 )- stateTimerManager.getTimer(roomId).getTime());
				/*for (Integer value : roomInfoResult.getTime().values()) {
					if (value <= 2) {
						wait = true;
					}
				}*/
			} else if (state == 2) {
				roomInfoResult.getTime().put("state", stateTimerManager.getTimer(roomId).getState());
				roomInfoResult.getTime()
					.put("time", D.GAMESTATE2 - ((stateTimerManager.getTimer(roomId).getTime() + 1) - D.GAMESTATE1));
			} else if (state == 3) {
				wait = true;
				roomInfoResult.getTime().put("state", stateTimerManager.getTimer(roomId).getState());
				roomInfoResult.getTime()
					.put("time", D.GAMESTATE3 - ((stateTimerManager.getTimer(roomId).getTime() + 1) - D.GAMESTATE1 - D.GAMESTATE2));
			}
		//}
		// 返回用户信息
		roomInfoResult.setPlayer(player);
		// 返回庄家信息
		Banker banker = room.getBanker();
		List<String> bankerList = room.getBankerList();
		roomInfoResult.setBanker(banker);
		roomInfoResult.setBankerList(bankerList);
		roomInfoResult.setBankerCount(fileConfig.getBankRoundLimit());
		// 返回玩家金币
		roomInfoResult.setGold(player.getGold().doubleValue());
		// 返回当前总投注额度
		double totalBet = room.getTotalBet();
		roomInfoResult.setTotalBet(totalBet);
		// 返回下注信息
		Map<Integer, List<Double>> carBet1 = room.getCarSingleBet();
		Map<Integer, List<Integer>> carBet2 = room.getCarSingleBetForLevel();
		roomInfoResult.setCarBetInfo(carBet2);
		roomInfoResult.setTotalBetInfo(carBet1);
		// 返回上局下注信息
		roomInfoResult.setLastBetInfo(room.getAllPlayerLastBetInfo());
		// 返回当前玩家的下注信息
		roomInfoResult.setCurrentBetInfo(room.getPlayerCurrentBetInfo(roleid));
		// 返回玩家上局下注信息
		roomInfoResult.setLastUserBetInfo(room.getPlayerLastBetInfo(roleid));
		// 是否需要等待
		roomInfoResult.setWait(wait);
		// 是否开奖
		roomInfoResult.setEnd(end);
		// 返回场次配置
		roomInfoResult.setFileConfig(fileConfig);
		// 返回上一局游戏结果
		roomInfoResult.setLastResult(room.getLastGameResult().get(1));
		// 返回车辆投注详细信息
		ctx.writeAndFlush(ByteHelper.createFrameMessage
				(JsonUtil.create().cmd("joinRoom").put("roomId", room.getRoomId()).put("roomInfoResult", roomInfoResult).put("lastBetInfo", room.getPlayerLastBetInfo(roleid)).toJsonString()));
		userService.syncRoomFromJoin(room.getLevel(), room.getRoomId(), Long.parseLong(roleid));
	}
	@Override
	public IQueueType queue() {
		return QueueType.LOGIC;
	}

}
