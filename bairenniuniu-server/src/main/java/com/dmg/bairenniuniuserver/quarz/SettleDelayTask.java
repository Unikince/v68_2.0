package com.dmg.bairenniuniuserver.quarz;


import com.dmg.bairenniuniuserver.common.work.DelayTimeWork;
import com.dmg.bairenniuniuserver.manager.RoomManager;
import com.dmg.bairenniuniuserver.model.Room;
import com.dmg.bairenniuniuserver.model.RoomStatus;
import com.dmg.bairenniuniuserver.service.logic.SettleService;
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
