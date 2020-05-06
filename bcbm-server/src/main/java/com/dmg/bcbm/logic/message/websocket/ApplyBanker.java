package com.dmg.bcbm.logic.message.websocket;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.abs.def.type.IQueueType;
import com.dmg.bcbm.core.abs.message.NoLoginMessageNet;
import com.dmg.bcbm.core.annotation.Net;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.core.manager.ServiceManager;
import com.dmg.bcbm.core.manager.StateTimerManager;
import com.dmg.bcbm.logic.def.QueueType;
import com.dmg.bcbm.logic.entity.SimplePlayer;
import com.dmg.bcbm.logic.entity.bcbm.Banker;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.service.UserService;
import com.dmg.gameconfigserverapi.dto.BairenControlConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO.WaterPoolConfigDTO;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * 申请上庄下庄
 */
@Net(D.APPLYBANKER)
public class ApplyBanker extends NoLoginMessageNet {
	private static final Logger LOG = LoggerFactory.getLogger(ApplyBanker.class);
	@Override
	public void go(ChannelHandlerContext ctx, JSONObject message) {
		//{"forward":1,"roleid":"805","cmd":"applyBanker","type":"1","uuid":"1","roomId":"1"}
		// 申请id
		String roleid = message.getString("roleid");
		// 申请类型(1为上庄,2为下庄)
		String type = message.getString("type");
		String uuid = message.getString("uuid");
		//int roomId = message.getIntValue("roomId");
		int roomId = 1;
		// 获取房间
		Room room = RoomManager.intance().getRoomById(roomId);
		
		BairenGameConfigDTO gameConfig = room.getGameConfig().get(1); // 获取游戏配置
		//List<WaterPoolConfigDTO> waterPoolConfigDTOS = gameConfig.getWaterPoolConfigDTOS(); // 库存配置
		BairenFileConfigDTO fileConfig = gameConfig.getBairenFileConfigDTO(); // 场次配置
		//BairenControlConfigDTO bairenControlConfigDTO = gameConfig.getBairenControlConfigDTO(); // 控制配置
		
		// 获取当前庄家
		Banker banker = room.getBanker();
		// 获取等候做庄玩家列表
		List<String> bankerList = room.getBankerList();
		
		// 下庄
		/*if (D.BANKERTYPE2.equals(type)) {
			
		}	*/
		// 获取用户信息
		UserService userService = ServiceManager.instance().get(UserService.class);
		SimplePlayer player = userService.getUserInfo(roleid, uuid,roomId);
		Integer applyBankerLimit = fileConfig.getApplyBankerLimit(); // 上庄最低金币限制
		if (D.BANKERTYPE1.equals(type)) {
			if (roleid.equals(banker.getRoleid())) {
				ctx.writeAndFlush(ByteHelper.createFrameMessage(JsonUtil.create().cmd("applyBanker").put("roomId", room.getRoomId()).errorCode("已经是庄家").toJsonString()));
				return;
			} else if (bankerList.contains(roleid)) {
				ctx.writeAndFlush(ByteHelper.createFrameMessage(JsonUtil.create().cmd("applyBanker").put("roomId", room.getRoomId()).errorCode("已经在排队中").toJsonString()));
				return;
			} else if (player.getGold().doubleValue() < applyBankerLimit) {
				ctx.writeAndFlush(ByteHelper.createFrameMessage(JsonUtil.create().cmd("applyBanker").put("roomId", room.getRoomId()).errorCode("金币不足").toJsonString()));
				return;
			} else if (applyBankerLimit <= 0) {
				ctx.writeAndFlush(ByteHelper.createFrameMessage(JsonUtil.create().cmd("applyBanker").put("roomId", room.getRoomId()).errorCode("配置错误,暂时不能上庄!").toJsonString()));
				return;
			}
			
			// 如果排队为0,又为系统做庄,不在开奖与休息状态,没有下注,可直接上庄
			int state = StateTimerManager.instance().getTimer(roomId).getState();
			if (bankerList.size() == 0 && D.SYSTEMBANKER.equals(banker.getRoleid()) 
					&& state == 1 && room.getPlayerBetTotal(roleid) == 0d) {
				banker.setRoleid(roleid);
				banker.setNickName(player.getNickname());
				banker.setCount(1);
				banker.setGold(player.getGold().doubleValue());
				banker.setBankerCount(fileConfig.getBankRoundLimit());
				LOG.info("room = " + room.getRoomId() + " =====>" + "玩家{}成为庄家!", roleid);
				// 否则排队
			} else {
				bankerList.add(roleid);
				LOG.info("room = " + room.getRoomId() + " =====>" + "玩家{}进入庄家排队列表!", roleid);
			}
		}
		
		// 通知所有玩家庄家变化
		for (ChannelHandlerContext ctxs : room.getOnlineList().keySet()) {
			ctxs.writeAndFlush(ByteHelper.createFrameMessage
					(JsonUtil.create().cmd("applyBanker").put("roomId", room.getRoomId()).put("banker", banker).put("bankerList", bankerList).toJsonString()));
		}
		LOG.info("room = " + room.getRoomId() + " =====>" + "当前庄家 {}, 庄家金币 {}, 上庄次数 {}, 排队玩家 {}!", banker.getRoleid(), banker.getGold(),banker.getCount(),bankerList);
	}
	@Override
	public IQueueType queue() {
		return QueueType.LOGIC;
	}

}
