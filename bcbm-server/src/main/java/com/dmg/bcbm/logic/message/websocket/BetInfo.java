package com.dmg.bcbm.logic.message.websocket;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.dmg.bcbm.logic.entity.bcbm.BaseRobot;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.entity.bcbm.SingleBetInfo;
import com.dmg.bcbm.logic.service.UserService;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * 下注
 */
@Net(D.BETINFO)
public class BetInfo extends NoLoginMessageNet {
	private static final Logger LOG = LoggerFactory.getLogger(BetInfo.class);
    // 当前时间状态
    private  static StateTimerManager stateTimerManager = StateTimerManager.instance();
    // 用户服务
    private static UserService userService = ServiceManager.instance().get(UserService.class);
	@Override
	public void go(ChannelHandlerContext ctx, JSONObject message) {
		//{"forward":1,"roleid":"805","cmd":"betInfo","car": 1 ,"currentGold" :xxx,"bet":50,"uuid":"1","type":1,"roomId":"1"}
		
		//int roomId = message.getIntValue("roomId");
		int roomId = 1;
		// 检查是否下注状态
		int state = stateTimerManager.getTimer(roomId).getState();
		if (state != 1) {
			if (ctx != null) {
				ctx.writeAndFlush(ByteHelper.createFrameMessage
						(JsonUtil.create().cmd("betInfo").put("roomId", roomId).errorCode("现在不是下注时间").toJsonString()));
			}
			return;
		}
		// 下注id
		String roleid = message.getString("roleid");
		String uuid = message.getString("uuid");
		// 下注类型(1:玩家2:机器人)
		String type = message.getString("type");
		// 下注车辆
		int car = message.getIntValue("car");
		// 下注额度
		double bet = message.getDoubleValue("bet");
		// 用户当前金币
		double currentGold = message.getDoubleValue("currentGold");
		// 当前房间
		Room room = RoomManager.intance().getRoomById(roomId);
		
		if (currentGold - bet < 0) {
			if (D.ROBOTBETTYPE.equals(type)){ // 机器人金币不足
				BaseRobot robot = room.getRobot(roleid);
				robot.setPlayRoundLimit(D.ROBOTPLAYROUNDLIMIT);
			} else {
				if (ctx != null) { // 玩家金币不足
					ctx.writeAndFlush(ByteHelper.createFrameMessage
							(JsonUtil.create().cmd("betInfo").put("roomId", room.getRoomId()).errorCode("玩家金币不足").toJsonString()));
				}
			}
			return;
		}
		
		BairenGameConfigDTO gameConfig = room.getGameConfig().get(1); // 获取游戏配置
		BairenFileConfigDTO fileConfig = gameConfig.getBairenFileConfigDTO(); // 场次配置
		
		// 获取赌注列表
		String betChips = fileConfig.getBetChips();
		String[] split = betChips.split("\\,");
		List<Double> betList = new ArrayList<>();
		for (String b : split) {
			betList.add(Double.valueOf(b));
		}
		
		if (!betList.contains(bet)) { // 检查筹码是否正确
			if (D.ROBOTBETTYPE.equals(type)) return;
			if (ctx != null) {
				ctx.writeAndFlush(ByteHelper.createFrameMessage
						(JsonUtil.create().cmd("betInfo").put("roomId", room.getRoomId()).errorCode("筹码不合法").toJsonString()));
			}
			return;
		}
		synchronized (BetInfo.class) {
			// 获取用户信息
			SimplePlayer player = null;
			if (type.equals(D.ROBOTBETTYPE)) {  
				player = room.getRobotList().get(roleid);
			} else {
				Role role = room.getRole(roleid);
				SimplePlayer newPlayer = new SimplePlayer();
				newPlayer.setGold(new BigDecimal(role.getGold()));
				newPlayer.setNickname(role.getNickName());
				newPlayer.setRoleId(role.getRoleId());
				player = newPlayer;
			}
			if (player.getGold().doubleValue() < bet) {
				if (ctx != null) {
					ctx.writeAndFlush(ByteHelper.createFrameMessage
							(JsonUtil.create().cmd("betInfo").put("roomId", room.getRoomId()).errorCode("玩家金币不足").toJsonString()));
				}
				return;
			}
			
			// 庄家不能下注
			if (roleid.equals(room.getBanker().getRoleid())) {
				if (ctx != null) {
					ctx.writeAndFlush(ByteHelper.createFrameMessage
							(JsonUtil.create().cmd("betInfo").put("roomId", room.getRoomId()).errorCode("庄家不可以下注").toJsonString()));
				}
				return;
			}
			
			// 检查本局台红值
			Long redValue = fileConfig.getRedValue(); // 预设台红值
			if (!D.SYSTEMBANKER.equals(room.getBanker().getRoleid())) { // 如果为玩家做庄,台红值为庄家当前金币
				Role role = room.getRole(room.getBanker().getRoleid());
				redValue = (long) role.getGold();
			}
			double currentRedValue = room.getCurrentRedValue(); // 当前台红值
			if ((currentRedValue + bet) > redValue) {
				if (D.ROBOTBETTYPE.equals(type)) return;
				if (ctx != null) {
					ctx.writeAndFlush(ByteHelper.createFrameMessage
							(JsonUtil.create().cmd("betInfo").put("roomId", room.getRoomId()).errorCode("超出当局下注总额度,本次下注失败").toJsonString()));
				}
				return;
			}
			// 检查单人投注总额
			double playerBetTotal = room.getPlayerBetTotal(player.getRoleId());
			double BetLowLimit = fileConfig.getAreaBetLowLimit().doubleValue();  // 玩家最低下注
			double BetUpLimit = fileConfig.getAreaBetUpLimit().doubleValue();  // 玩家最高下注
			if (bet >= BetLowLimit) {
				if (playerBetTotal + bet > BetUpLimit) {
					if (D.ROBOTBETTYPE.equals(type)) return;
					if (ctx != null) {
						ctx.writeAndFlush(ByteHelper.createFrameMessage
								(JsonUtil.create().cmd("betInfo").put("roomId", room.getRoomId()).errorCode("超出个人下注总额度,本次下注失败").toJsonString()));
					}
					return;
				}
			}
			
			// 下注
			room.bet(car, bet, player.getRoleId());
			UserService userService = ServiceManager.instance().get(UserService.class);
			//if (!(player instanceof BaseRobot)) {
			userService.bet(roleid, bet, uuid,roomId);
			//}
			
			// 检查在庄家列表的玩家金币是否足够上庄
			if (room.getBankerList().contains(roleid)) {
				Role role = room.getRole(roleid);
				if (role.getGold() < fileConfig.getApplyBankerLimit()) {
					room.getBankerList().remove(roleid);
					if (ctx != null) {
						ctx.writeAndFlush(ByteHelper.createFrameMessage
								(JsonUtil.create().cmd("betInfo").put("roomId", room.getRoomId()).errorCode("当前玩家金币不足以上庄,移出上庄列表").toJsonString()));
					}
					LOG.info("room = " + room.getRoomId() + " =====>" + "玩家: {} 金币不足以上庄,移出上庄列表!", roleid);
				}
			}
		}
		
		// 添加本局总赌注
		double totalBet = room.getTotalBet();
		totalBet += bet;
		room.setTotalBet(totalBet);
		// 封装本次下注信息
		SingleBetInfo singleBetInfo = new SingleBetInfo(roleid, car, bet);
		singleBetInfo.setBetLevel(betList.indexOf(bet));
		singleBetInfo.setTotalBet(totalBet);
		// 当前车辆总投注详细信息
		Map<Integer, List<Double>> carBet = room.getCarSingleBet();
		double[] carbet2 = {0,0,0,0,0,0,0,0};
		if (carBet.size() > 0) {
			for (int c : carBet.keySet()) {
				List<Double> list = carBet.get(c);
				for (double b : list) {
					carbet2[c-1] += b;
				}
			}
		}
		singleBetInfo.setCarBet(carbet2);
		// 当前投注玩家的投注信息
		Map<Integer, Double> playerBetInfo = room.getPlayerBetInfo(roleid);
		double[] carbet3 = {0,0,0,0,0,0,0,0};
		if (playerBetInfo.size() > 0) {
			for (int c : playerBetInfo.keySet()) {
				carbet3[c-1] = playerBetInfo.get(c);
			}
		}
		singleBetInfo.setPlayerBetInfo(carbet3);
		for (ChannelHandlerContext ctxs : room.getOnlineList().keySet()) { // 广播投注信息给玩家
			JSONObject json = new JSONObject();
			json.put("cmd", "betInfo");
			json.put("roomId", room.getRoomId());
			json.put("singleBetInfo", singleBetInfo);
			ctxs.writeAndFlush(ByteHelper.createFrameMessage(json.toJSONString()));
			//LOG.info("room = " + room.getRoomId() + " =====>" + "推送下注信息给玩家{}!", room.getOnlineList().get(ctxs));
		}
		
		if (D.ROBOTBETTYPE.equals(type)) {
			//LOG.debug("room = " + room.getRoomId() + " =====>" + "robot: {}下注成功,下注类型{},下注额度{}!", roleid, car, bet);
		} else {
			LOG.info("room = " + room.getRoomId() + " =====>" + "玩家: {}下注成功,下注类型{},下注额度{}!", roleid, car, bet);
		}
	}
	@Override
	public IQueueType queue() {
		return QueueType.LOGIC;
	}

}
