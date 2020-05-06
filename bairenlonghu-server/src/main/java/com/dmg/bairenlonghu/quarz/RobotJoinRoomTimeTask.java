package com.dmg.bairenlonghu.quarz;

import com.dmg.bairenlonghu.common.work.Cron;
import com.dmg.bairenlonghu.common.work.TimeWork;
import com.dmg.bairenlonghu.service.logic.JoinRoomService;
import com.dmg.common.core.util.SpringUtil;

@Cron("*/3 * * * * ?")
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