package com.dmg.bairenniuniuserver.quarz;

import com.dmg.bairenniuniuserver.common.work.DelayTimeWork;
import com.dmg.bairenniuniuserver.manager.RoomManager;
import com.dmg.bairenniuniuserver.model.Room;
import com.dmg.bairenniuniuserver.model.RoomStatus;
import com.dmg.bairenniuniuserver.service.logic.SettleService;
import com.dmg.bairenniuniuserver.service.logic.StartGameService;
import com.dmg.common.core.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartGameDelayTask extends DelayTimeWork {
    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
        StartGameService startGameService = SpringUtil.getBean(StartGameService.class);
        Room room = RoomManager.intance().getRoom(roomId);
        room.setRoomStatus(RoomStatus.BET);
        startGameService.startGame(roomId);
    }
}
