package com.dmg.bairenzhajinhuaserver.quarz;


import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.common.work.DelayTimeWork;
import com.dmg.bairenzhajinhuaserver.service.logic.BetService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RobotBetDelayTask extends DelayTimeWork {
    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
        BetService betService = SpringContextUtil.getBean(BetService.class);
        betService.robotRandomBet(roomId);
    }
}
