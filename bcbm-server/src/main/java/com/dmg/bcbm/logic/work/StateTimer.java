package com.dmg.bcbm.logic.work;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.dmg.bcbm.SpringContextUtil;
import com.dmg.bcbm.core.abs.def.type.IQueueType;
import com.dmg.bcbm.core.abs.work.Work;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.core.manager.ServiceManager;
import com.dmg.bcbm.core.manager.WorkManager;
import com.dmg.bcbm.logic.def.QueueType;
import com.dmg.bcbm.logic.entity.SimplePlayer;
import com.dmg.bcbm.logic.entity.bcbm.BaseRobot;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.entity.bcbm.UserBetInfo;
import com.dmg.bcbm.logic.message.websocket.RobotBet;
import com.dmg.bcbm.logic.service.GameService;
import com.dmg.common.core.config.RedisRegionConfig;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * 计时器
 */
public class StateTimer extends Work {
	private static final Logger LOG = LoggerFactory.getLogger(StateTimer.class);
	private Room room;
	// 开奖是否结束
	private boolean isEnd;
	// 游戏状态(1为下注,2为开奖,3为休息)
	private int state = 1;
	private int time = 0;
	
	public StateTimer(Room room) {
		this.room = room;
	}
	@Override
	public void init(Object... args) {
	}
	@Override
	public IQueueType queue() {
		return QueueType.LOGIC;
	}
	
	@Override
	public void go() {
		Boolean flag = true;
		while (flag) {
			try {
				Thread.sleep(1000);
				time ++;
				 for (ChannelHandlerContext ctxs : room.getOnlineList().keySet()) {
						ctxs.writeAndFlush(ByteHelper.createFrameMessage
								(JsonUtil.create().put("currentTime", time).toJsonString()));
				}
				if (time == 35) {
					time = 0;
					state = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("room = " + room.getRoomId() + " =====>" + "定时器出错!");
			}
				
			// 下注
			if (time== 0) {
				Map<String, Integer> temp = new HashMap<>();
				temp.put("state", 1);
				temp.put("time", 15);
				 for (ChannelHandlerContext ctxs : room.getOnlineList().keySet()) {
					 String roleid = room.getOnlineList().get(ctxs);
						ctxs.writeAndFlush(ByteHelper.createFrameMessage
								(JsonUtil.create().cmd("stateTimer").put("time", temp).put("lastBetInfo", room.getPlayerLastBetInfo(roleid)).toJsonString()));
				}
				 WorkManager.instance().submit(new RobotBet());
				 LOG.info("room = " + room.getRoomId() + " =====>" + "当前计时器为 {} , 状态为 {} , 房间人数为 {}人,机器人为{}", time , state, room.getChannelTotal(),room.getRobotNum());
			}
			// 开彩
			if (time== 15) {
				state = 2;
				Map<String, Integer> temp = new HashMap<>();
				temp.put("state", 2);
				temp.put("time", 15);
				 for (ChannelHandlerContext ctxs : room.getOnlineList().keySet()) {
						ctxs.writeAndFlush(ByteHelper.createFrameMessage
								(JsonUtil.create().cmd("stateTimer").put("time", temp).toJsonString()));
				}
				 GameService gameService = ServiceManager.instance().get(GameService.class);
				 try {
					gameService.mainGameProcess(room.getRoomId());
				} catch (Exception e) {
					e.printStackTrace();
					LOG.info("room = " + room.getRoomId() + " =====>" + "mainGameProcess error!");
				}
				 isEnd = true; // 已经开奖
				 LOG.info("room = " + room.getRoomId() + " =====>" + "当前计时器为 {} , 状态为 {} , 房间人数为 {}人,机器人为{}", time , state, room.getChannelTotal(),room.getRobotNum());
			}
			// 休息
			if (time== 30) {
				state = 3;
				isEnd = false;// 未开奖
				Map<String, Integer> temp = new HashMap<>();
				temp.put("state", 3);
				temp.put("time", 5);
				// 自动换庄
				WorkManager.instance().submit(AutoBanker.class,room);
				// 清除上一局游戏结果
				room.getLastGameResult().remove(1);
				// 清空赌注信息
				room.cleanBetInfo();
				// 保存上一局赌注信息
				room.saveBetInfo();
				 for (ChannelHandlerContext ctxs : room.getOnlineList().keySet()) {
						ctxs.writeAndFlush(ByteHelper.createFrameMessage
								(JsonUtil.create().cmd("stateTimer").put("time", temp).toJsonString()));
				}
				 LOG.info("room = " + room.getRoomId() + " =====>" + "当前计时器为 {} , 状态为 {} , 房间人数为 {}人,机器人为{}", time , state, room.getChannelTotal(),room.getRobotNum());
				 
				 // 添加或删除机器人
				 for (String robotId : room.getRobotList().keySet()) { // 删除金币不足的机器人
					if (room.getRobotList().get(robotId).getPlayRoundLimit() == D.ROBOTPLAYROUNDLIMIT) {
						room.getRobotList().remove(robotId);
					}
				 }
				 if (room.getTotalNum() > D.LOWNUM) {
					 room.delRobot(room.getTotalNum() - D.LOWNUM);
				 } else {
					 if (room.getGameConfig().size() > 0) {
						 room.addRobot(D.LOWNUM - room.getTotalNum());
					}
				}
				 // 更新游戏配置
				 room.updateGameconfig();
				 // 保存玩家下注前金币
				 room.savePlayerBeforeBetinfo();
				 // 更新在线人数
				 StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
				 redisTemplate.opsForValue().set(RedisRegionConfig.ONLINE_NUM_KEY + ":" + D.GAME_ID + "_" + 1,String.valueOf(room.getChannelTotal()));
				 LOG.info("room = " + room.getRoomId() + " =====>" + "更新当前在线人数,当前在线人数为: {}",room.getChannelTotal());
				 // 停服
				 if (RoomManager.intance().getShutdownServer()) {
					for (ChannelHandlerContext ctx : room.getOnlineList().keySet()) {
						if (ctx == null) continue;
						ctx.writeAndFlush(ByteHelper.createFrameMessage
								(JsonUtil.create().cmd("stateTimer").put("roomId", room.getRoomId()).put("shutdownServer", 6666).toJsonString()));
						//ctx.channel().close();
					}
					 flag = false;
				}
			}
		}
	}
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public boolean isEnd() {
		return isEnd;
	}
	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	
}
