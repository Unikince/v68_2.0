package com.dmg.bcbm.logic.message.websocket;

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
import com.dmg.bcbm.core.manager.StateTimerManager;
import com.dmg.bcbm.core.manager.WorkManager;
import com.dmg.bcbm.logic.def.QueueType;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.entity.bcbm.UserBetInfo;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * 继续下注
 */
@Net(D.CONTINUEBET)
public class ContinueBet extends NoLoginMessageNet {
	private static final Logger LOG = LoggerFactory.getLogger(ContinueBet.class);
	private static StateTimerManager stateTimerManager = StateTimerManager.instance();
	private static WorkManager workManager = WorkManager.instance();
	@Override
	public void go(ChannelHandlerContext ctx, JSONObject message) {
		//{"forward":1,"roleid":"805","cmd":"continueBet","uuid":"2","roomId":"1"}
		// 继续下注id
		String roleid = message.getString("roleid");
		String uuid = message.getString("uuid");
		//int roomId = message.getIntValue("roomId");
		int roomId = 1;
		// 获取房间
		Room room = RoomManager.intance().getRoomById(roomId);
		int state = stateTimerManager.getTimer(roomId).getState();
		if (state != 1) {
			ctx.writeAndFlush(ByteHelper.createFrameMessage
					(JsonUtil.create().cmd("continueBet").put("roomId", room.getRoomId()).errorCode("现在不是下注时间").toJsonString()));
			return;
		}
		
		if (room.getPlayerBetTotal(roleid) > 0d) {
			ctx.writeAndFlush(ByteHelper.createFrameMessage
					(JsonUtil.create().cmd("continueBet").put("roomId", room.getRoomId()).errorCode("当局已下注,不能续投").toJsonString()));
			return;
		}
		
		UserBetInfo lastBetInfo = room.getPlayerLastBetInfo(roleid);
		if (lastBetInfo == null) {
			ctx.writeAndFlush(ByteHelper.createFrameMessage
					(JsonUtil.create().cmd("continueBet").put("roomId", room.getRoomId()).errorCode("没有当前玩家下注信息").toJsonString()));
			return;
		} else { // 进行续投
			Map<Integer, List<Double>> betinfo = lastBetInfo.getBetinfo();
			double gold = room.getRole(roleid).getGold(); // 获取用户当前金币
			for (Integer car : betinfo.keySet()) {
				List<Double> list = betinfo.get(car);
				for (Double bet : list) {
					if (gold < bet) {
						ctx.writeAndFlush(ByteHelper.createFrameMessage
								(JsonUtil.create().cmd("betInfo").put("roomId", room.getRoomId()).errorCode("玩家金币不足").toJsonString()));
						return;
					}
					gold -= bet;
					JSONObject json = new JSONObject();
					json.put("forward", 1);
					json.put("roleid", roleid);
					json.put("cmd", "betInfo");
					json.put("currentGold", gold);
					json.put("type", 1);
					json.put("car", car);
					json.put("bet", bet);
					json.put("uuid", uuid);
					workManager.submit(BetInfo.class, ctx, json);
				}
			}
			LOG.info("room = " + room.getRoomId() + " =====>" + "玩家{}申请继续下注成功!", roleid);
		}
	}
	@Override
	public IQueueType queue() {
		return QueueType.LOGIC;
	}

}
