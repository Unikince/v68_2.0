package com.dmg.bairenlonghu.quarz;

import com.dmg.bairenlonghu.common.work.TimeWork;
import com.dmg.bairenlonghu.manager.RoomManager;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
//@Cron("0 0 0 1/1 * ?")
public class CleanSystemGoldTask extends TimeWork {

    @Override
    public void init(Object... args) {

    }

    @Override
    public void go() {
        log.warn("定时清理系统金币:{},系统流水:{}",RoomManager.intance().getRoomGoldMap(),RoomManager.intance().getSystemTurnover());
        RoomManager.intance().getRoomGoldMap().clear();
        RoomManager.intance().setSystemTurnover(BigDecimal.ZERO);
    }

}