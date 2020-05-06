package com.dmg.bairenzhajinhuaserver.quarz;

import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.common.work.Cron;
import com.dmg.bairenzhajinhuaserver.common.work.TimeWork;
import com.dmg.bairenzhajinhuaserver.service.logic.JoinRoomService;

@Cron("*/10 * * * * ?")
public class RobotJoinRoomTimeTask extends TimeWork {

    @Override
    public void init(Object... args) {

    }

    @Override
    public void go() {
        JoinRoomService joinRoomService = SpringContextUtil.getBean(JoinRoomService.class);
        joinRoomService.robotJoinRoom();
    }

}