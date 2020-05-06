package com.dmg.bairenlonghu.quarz;


import com.dmg.bairenlonghu.common.work.DelayTimeWork;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.model.RoomStatus;
import com.dmg.bairenlonghu.service.logic.SettleService;
import com.dmg.common.core.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SettleDelayTask extends DelayTimeWork {
    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
        Room room = RoomManager.intance().getRoom(roomId);
        room.setRoomStatus(RoomStatus.DEAL);
        SettleService settleService = SpringUtil.getBean(SettleService.class);
        settleService.settle(roomId);
    }
}
