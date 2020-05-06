package com.dmg.redblackwarserver.service.logic.impl;

import org.springframework.stereotype.Component;
import com.dmg.redblackwarserver.common.exception.BusinessException;
import com.dmg.redblackwarserver.common.result.ResultEnum;
import com.dmg.redblackwarserver.manager.RoomManager;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.service.logic.RoomActionService;
@Component
public class RoomActionServiceImpl implements RoomActionService{

	@Override
	public void action(int roomId) {
		Room room = RoomManager.intance().getRoom(roomId);
        if (room == null){
            throw new BusinessException(ResultEnum.ROOM_NO_EXIST.getCode()+"",ResultEnum.ROOM_NO_EXIST.getMsg());
        }
        room.action();
	}

}
