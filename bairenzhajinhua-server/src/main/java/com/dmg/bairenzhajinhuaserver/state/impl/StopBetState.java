package com.dmg.bairenzhajinhuaserver.state.impl;

import org.quartz.SchedulerException;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRoom;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.manager.TimerManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.model.RoomStatus;
import com.dmg.bairenzhajinhuaserver.model.constants.D;
import com.dmg.bairenzhajinhuaserver.quarz.RobotBetDelayTask;
import com.dmg.bairenzhajinhuaserver.quarz.RoomActionDelayTask;
import com.dmg.bairenzhajinhuaserver.state.RoomState;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 下注阶段
 * @author Administrator
 *
 */
@Slf4j
public class StopBetState implements RoomState{
	
	private BaseRoom room;
	
	public StopBetState(BaseRoom room) {
		this.room = room;
	}
	
	@Override
	public State getState() {
		return State.BET;
	}

	@Override
	public void action() {
		Room room = ((Room)this.room);
		long betTime = System.currentTimeMillis()+ D.STOP_BET_TIME;
		MessageResult messageResult = new MessageResult(MessageIdConfig.STOP_BET_NTC,betTime);
		room.broadcast(messageResult);
		room.setCountdownTime(betTime);
		room.changeState();
		try {
			TimerManager.instance().submitDelayWork(RoomActionDelayTask.class, D.STOP_BET_TIME, room.getRoomId());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
        log.info("==>房间:{},进入下注阶段",room.getRoomId());
	}

}