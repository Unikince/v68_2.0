package com.dmg.redblackwarserver.state.impl;

import org.quartz.SchedulerException;

import com.dmg.redblackwarserver.common.model.BaseRoom;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.manager.TimerManager;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.constants.D;
import com.dmg.redblackwarserver.quarz.RoomActionDelayTask;
import com.dmg.redblackwarserver.state.RoomState;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;

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