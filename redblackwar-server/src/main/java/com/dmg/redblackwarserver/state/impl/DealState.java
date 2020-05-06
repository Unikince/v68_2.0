package com.dmg.redblackwarserver.state.impl;

import java.util.ArrayList;
import java.util.List;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import com.dmg.redblackwarserver.common.exception.BusinessException;
import com.dmg.redblackwarserver.common.model.BaseRoom;
import com.dmg.redblackwarserver.common.model.Poker;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.common.result.ResultEnum;
import com.dmg.redblackwarserver.manager.TimerManager;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.RoomStatus;
import com.dmg.redblackwarserver.model.Table;
import com.dmg.redblackwarserver.model.constants.D;
import com.dmg.redblackwarserver.model.dto.DealPokerDTO;
import com.dmg.redblackwarserver.quarz.RoomActionDelayTask;
import com.dmg.redblackwarserver.state.RoomState;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 发牌阶段
 * @author Administrator
 *
 */
@Slf4j
public class DealState implements RoomState {
	private BaseRoom room;
	
	public DealState(BaseRoom room) {
		this.room = room;
	}
	
	@Override
	public State getState() {
		return State.DEAL;
	}

	@Override
	public void action() {
		Room room = (Room) this.room;
        if (room == null){
            throw new BusinessException(ResultEnum.ROOM_NO_EXIST.getCode()+"",ResultEnum.ROOM_NO_EXIST.getMsg());
        }
        room.setRoomStatus(RoomStatus.DEAL);
        dealNormal();
        DealPokerDTO dto = new DealPokerDTO();
        dto.setStartIndex(RandomUtil.randomInt(1,3));
        BeanUtils.copyProperties(room,dto);
        MessageResult messageResult = new MessageResult(MessageIdConfig.DEAL_POKER_NTC,dto);
        room.broadcast(messageResult);
        try {
            room.setCountdownTime(System.currentTimeMillis() + D.DEAL_TIME);
            TimerManager.instance().submitDelayWork(RoomActionDelayTask.class, D.DEAL_TIME, room.getRoomId());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        room.changeState();
	}

	/**
     * @return void
     * @description: 正常发牌
     * @author mice
     * @date 2019/7/31
     */
    void dealNormal() {
        log.info("==>房间:{},发牌阶段-->默认牌值", room.getRoomId());
        Room room = (Room) this.room;
        for (Table table : room.getTableMap().values()) {
            List<Poker> pokers = new ArrayList<>();
            for (int j = 1; j < 3; j++) {
                pokers.add(new Poker());
            }
            table.setPokerList(pokers);
        }
    }

   
}
