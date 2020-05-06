package com.dmg.redblackwarserver.quarz;

import com.dmg.common.core.util.SpringUtil;
import com.dmg.redblackwarserver.common.work.Cron;
import com.dmg.redblackwarserver.common.work.TimeWork;
import com.dmg.redblackwarserver.service.logic.JoinRoomService;

@Cron("*/10 * * * * ?")
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