package com.dmg.redblackwarserver.quarz;


import com.dmg.common.core.util.SpringUtil;
import com.dmg.redblackwarserver.common.work.DelayTimeWork;
import com.dmg.redblackwarserver.service.logic.BetService;
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
        BetService betService = SpringUtil.getBean(BetService.class);
        betService.robotRandomBet(roomId);
    }
}
