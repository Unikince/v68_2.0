package com.dmg.bairenniuniuserver.quarz;

import com.dmg.bairenniuniuserver.common.work.Cron;
import com.dmg.bairenniuniuserver.common.work.TimeWork;
import com.dmg.bairenniuniuserver.service.logic.JoinRoomService;
import com.dmg.common.core.util.SpringUtil;

@Cron("*/9 * * * * ?")
public class RobotJoinRoomTimeTask extends TimeWork {

    @Override
    public void init(Object... args) {

    }

    @Override
    public void go() {
        JoinRoomService joinRoomService = SpringUtil.getBean(JoinRoomService.class);
        joinRoomService.robotJoinRoom();
    }

}