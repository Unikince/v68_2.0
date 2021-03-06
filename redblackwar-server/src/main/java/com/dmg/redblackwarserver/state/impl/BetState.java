package com.dmg.redblackwarserver.state.impl;

import org.quartz.SchedulerException;
import com.dmg.redblackwarserver.common.model.BaseRoom;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.manager.TimerManager;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.RoomStatus;
import com.dmg.redblackwarserver.model.constants.D;
import com.dmg.redblackwarserver.quarz.RobotBetDelayTask;
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
public class BetState implements RoomState{
	
	private BaseRoom room;
	
	public BetState(BaseRoom room) {
		this.room = room;
	}
	
	@Override
	public State getState() {
		return State.BET;
	}

	@Override
	public void action() {
		Room room = ((Room)this.room);
		room.setRoomStatus(RoomStatus.BET);
		long betTime = System.currentTimeMillis()+ D.BET_TIME;
		MessageResult messageResult = new MessageResult(MessageIdConfig.BET_NTC,betTime);
		room.broadcast(messageResult);
		room.setCountdownTime(betTime);
        startDelayWork(room.getRoomId());
        room.changeState();
        log.info("==>房间:{},进入下注阶段",room.getRoomId());
	}

	/**
	 * 开启延时
	 * @param roomId
	 */
	private void startDelayWork(int roomId) {
        try {
            TimerManager.instance().submitDelayWork(RoomActionDelayTask.class, D.BET_TIME, roomId);
            for(int i = 1;i < D.BET_TIME / 1000;i++) {
            	TimerManager.instance().submitDelayWork(RobotBetDelayTask.class, RandomUtil.randomInt(i * 1000,(i + 1) * 1000), roomId);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
	}
	
}