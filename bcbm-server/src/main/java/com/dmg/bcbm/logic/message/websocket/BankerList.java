package com.dmg.bcbm.logic.message.websocket;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.abs.def.type.IQueueType;
import com.dmg.bcbm.core.abs.message.NoLoginMessageNet;
import com.dmg.bcbm.core.annotation.Net;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.logic.def.QueueType;
import com.dmg.bcbm.logic.entity.bcbm.Banker;
import com.dmg.bcbm.logic.entity.bcbm.BankerListInfo;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * 等待上庄列表
 */
@Net(D.BANKERLIST)
public class BankerList extends NoLoginMessageNet {
	@Override
	public void go(ChannelHandlerContext ctx, JSONObject message) {
		//{"forward":1,"roleid":"805","cmd":"bankerList","uuid":"1","roomId":"1"}
		// 查询id
		String roleid = message.getString("roleid");
		//int roomId = message.getIntValue("roomId");
		int roomId = 1;
		// 获取房间
		Room room = RoomManager.intance().getRoomById(roomId);
		BairenGameConfigDTO gameConfig = room.getGameConfig().get(1); // 获取游戏配置
		BairenFileConfigDTO fileConfig = gameConfig.getBairenFileConfigDTO(); // 场次配置
		// 获取当前庄家
		Banker banker = room.getBanker();
		// 获取等候做庄玩家列表
		List<String> bankerList = room.getBankerList();
		
		BankerListInfo bankerListInfo = new BankerListInfo();
		bankerListInfo.setRoleId(banker.getRoleid());
		
		if(banker.getRoleid().equals(roleid)){
			bankerListInfo.setBanker(1);
		}
		if(bankerList.contains(roleid)){
			bankerListInfo.setQueue(1);
		}
		
		if(bankerListInfo.getQueue() > 0){
			int indexOf = bankerList.indexOf(roleid);
			bankerListInfo.setNum(indexOf + 1);
		}
		
		bankerListInfo.setBankerList(bankerList);
		bankerListInfo.setBankerCount(fileConfig.getBankRoundLimit());
		ctx.writeAndFlush(ByteHelper.createFrameMessage(JsonUtil.create().cmd("bankerList").put("roomId", room.getRoomId()).put("bankerListInfo", bankerListInfo).toJsonString()));
	}
	@Override
	public IQueueType queue() {
		return QueueType.LOGIC;
	}

}
