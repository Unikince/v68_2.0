package com.dmg.bairenniuniuserver.quarz;

import com.dmg.common.core.util.SpringUtil;
import com.dmg.bairenniuniuserver.common.work.Cron;
import com.dmg.bairenniuniuserver.common.work.TimeWork;
import com.dmg.bairenniuniuserver.manager.RoomManager;
import com.dmg.bairenniuniuserver.model.Room;
import com.dmg.bairenniuniuserver.service.logic.StartGameService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
//@Cron("0 0/2 * * * ?")
public class CheckRoomTask extends TimeWork {

    @Override
    public void init(Object... args) {

    }

    @Override
    public void go() {
        log.warn("定时清理房间");
        long now = System.currentTimeMillis();
        long outTime = 1000*60*2;
        for (Room room : RoomManager.intance().getRoomMap().values()){
            if (now - room.getCountdownTime()<outTime){
                continue;
            }
            log.warn("房间:{},重置开始游戏",room.getRoomId());
            StartGameService startGameService = SpringUtil.getBean(StartGameService.class);
            startGameService.startGame(room.getRoomId());
        }

    }
}