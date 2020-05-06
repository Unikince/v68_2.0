package com.dmg.bairenzhajinhuaserver.service.logic.impl;

import org.springframework.stereotype.Component;
import com.dmg.bairenzhajinhuaserver.common.exception.BusinessException;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.service.logic.RoomActionService;
@Component
public class RoomActionServiceImpl implements RoomActionService {

	@Override
	public void action(int roomId) {
		Room room = RoomManager.intance().getRoom(roomId);
        if (room == null){
            throw new BusinessException(ResultEnum.ROOM_NO_EXIST.getCode()+"",ResultEnum.ROOM_NO_EXIST.getMsg());
        }
        room.action();
	}

}
