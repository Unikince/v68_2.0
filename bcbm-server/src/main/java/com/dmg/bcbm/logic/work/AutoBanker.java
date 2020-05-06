package com.dmg.bcbm.logic.work;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.abs.def.type.IQueueType;
import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.abs.work.Work;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.ServiceManager;
import com.dmg.bcbm.logic.def.QueueType;
import com.dmg.bcbm.logic.entity.SimplePlayer;
import com.dmg.bcbm.logic.entity.bcbm.Banker;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.service.UserService;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * @author zhuqd
 * @Date 2017年8月10日
 * @Desc 自动上庄下庄
 */
public class AutoBanker extends Work {
	private static final Logger LOG = LoggerFactory.getLogger(AutoBanker.class);
	private static UserService userService = ServiceManager.instance().get(UserService.class);
	private Room room;
	@Override
	public void init(Object... args) {
		room = (Room) args[0];
	}
	@Override
	public IQueueType queue() {
		return QueueType.LOGIC;
	}
	@Override
	public void go() {
		// 获取当前庄家
		Banker banker = room.getBanker();
		// 获取等候做庄玩家列表
		List<String> bankerList = room.getBankerList();
		Map<String, Role> roleMap = room.getRoleMap();
		
		BairenGameConfigDTO gameConfig = room.getGameConfig().get(1); // 获取游戏配置
		//List<WaterPoolConfigDTO> waterPoolConfigDTOS = gameConfig.getWaterPoolConfigDTOS(); // 库存配置
		BairenFileConfigDTO fileConfig = gameConfig.getBairenFileConfigDTO(); // 场次配置
		//BairenControlConfigDTO bairenControlConfigDTO = gameConfig.getBairenControlConfigDTO(); // 控制配置
		
		// 替换系统庄
		if (D.SYSTEMBANKER.equals(banker.getRoleid())) {
			if (bankerList.size() > 0) {
				// 玩家上庄
				banker.setRoleid(bankerList.remove(0));
				banker.setCount(1);
			}
		} else {
			// 检查当前庄家是否在线
			if (!roleMap.get(banker.getRoleid()).isActive()) {
				if (bankerList.size() > 0) {
					// 玩家上庄
					banker.setRoleid(bankerList.remove(0));
					banker.setCount(1);
				} else {
					// 系统上庄
					banker.setRoleid(D.SYSTEMBANKER);
					banker.setNickName(D.SYSTEMBANKER);
					banker.setCount(0);
					banker.setGold(D.SYSTEMBANKERGOLD);
				}
			} else {
				SimplePlayer player = userService.getUserInfo(banker.getRoleid(),roleMap.get(banker.getRoleid()).getUuid(),room.getRoomId());
				Integer bankRoundLimit = fileConfig.getBankRoundLimit(); // 连续坐庄回合数
				Integer bankerGoldLowLimit = fileConfig.getBankerGoldLowLimit(); // 下庄金额下限
				if (banker.getCount() >= bankRoundLimit ||player.getGold().doubleValue() < bankerGoldLowLimit) {
					if (bankerList.size() > 0) {
						// 玩家上庄
						banker.setRoleid(bankerList.remove(0));
						banker.setCount(1);
					} else {
						// 系统上庄
						banker.setRoleid(D.SYSTEMBANKER);
						banker.setNickName(D.SYSTEMBANKER);
						banker.setCount(0);
						banker.setGold(D.SYSTEMBANKERGOLD);
					}
				} else {
					// 继续上庄
					banker.setCount(banker.getCount() + 1);
				}
			}
		}
		
		// 获取当前banker信息
		if (!D.SYSTEMBANKER.equals(banker.getRoleid())) {
			//SimplePlayer bankerPlayer = userService.getUserInfo(banker.getRoleid(),roleMap.get(banker.getRoleid()).getUuid(),room.getRoomId());
			//banker.setGold(bankerPlayer.getGold());
			Role role = room.getRoleMap().get(banker.getRoleid());
			banker.setGold(role.getGold());
			banker.setNickName(role.getNickName());
			banker.setBankerCount(fileConfig.getBankRoundLimit());
		}
		
		// 通知所有玩家庄家变化
		for (ChannelHandlerContext ctxs : room.getOnlineList().keySet()) {
			ctxs.writeAndFlush(ByteHelper.createFrameMessage(JsonUtil.create().cmd("autoBanker").put("roomId", room.getRoomId()).put("banker", banker).put("bankerList", bankerList).toJsonString()));
		}
		LOG.info("room = " + room.getRoomId() + " =====>" + "庄家: {}, 上庄次数: {} ,金币: {}, 排队上庄的玩家: {} !", banker.getRoleid(), banker.getCount(),banker.getGold(), bankerList);
	}
}
