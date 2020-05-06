package com.dmg.bcbm.logic.message.websocket;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.abs.def.type.IQueueType;
import com.dmg.bcbm.core.abs.message.NoLoginMessageNet;
import com.dmg.bcbm.core.annotation.Net;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.core.manager.WorkManager;
import com.dmg.bcbm.logic.def.QueueType;
import com.dmg.bcbm.logic.entity.bcbm.BaseRobot;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.zyhy.common_server.util.RandomUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * 机器人下注
 */
@Net(D.ROBOTBET)
public class RobotBet extends NoLoginMessageNet {
	@Override
	public void go(ChannelHandlerContext ctx, JSONObject message) {
		int roomId = 1;
		Room room = RoomManager.intance().getRoomById(roomId);
		
		BairenGameConfigDTO gameConfig = room.getGameConfig().get(1); // 获取游戏配置
		BairenFileConfigDTO bairenFileConfigDTO = gameConfig.getBairenFileConfigDTO(); // 场次配置
		// 获取赌注列表
		String betChips = bairenFileConfigDTO.getBetChips();
		String[] split = betChips.split("\\,");
		List<String> betList = Arrays.asList(split);
		if (betList.size() > 0) {
			for (BaseRobot robot : room.getRobotList().values()) {
				// 随机下注次数
				int betCount = RandomUtil.getRandom(1, 3);
				for (int i = 0; i < betCount; i++) {
					// 随机下注车辆
					int car = RandomUtil.getRandom(1, 8);
					// 随机赌注
					double carBet = Double.valueOf(betList.get(RandomUtil.getRandom(0, 3)));
					String roleId = robot.getRoleId();
					JSONObject json = new JSONObject();
					json.put("forward", 1);
					json.put("roleid", roleId);
					json.put("cmd", "betInfo");
					json.put("currentGold", robot.getGold());
					json.put("type", D.ROBOTBETTYPE);
					json.put("car", car);
					json.put("bet", carBet);
					json.put("uuid", D.UUID);
					try {
						Thread.sleep(120L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					WorkManager.instance().submit(BetInfo.class, null, json);
				}
			}
		}
	}
	@Override
	public IQueueType queue() {
		return QueueType.LOGIC;
	}

}
