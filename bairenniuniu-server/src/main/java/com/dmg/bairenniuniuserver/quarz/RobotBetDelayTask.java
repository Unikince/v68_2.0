package com.dmg.bairenniuniuserver.quarz;


import cn.hutool.core.util.RandomUtil;
import com.dmg.bairenniuniuserver.common.work.DelayTimeWork;
import com.dmg.bairenniuniuserver.service.logic.BetService;
import com.dmg.common.core.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;


@Slf4j
public class RobotBetDelayTask extends DelayTimeWork {
    private int roomId;
    private int userId;
    private BigDecimal betChip;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.userId = (int) args[1];
        this.betChip = (BigDecimal) args[2];
    }

    @Override
    public void go() {
        BetService betService = SpringUtil.getBean(BetService.class);
        betService.robotBet(userId, RandomUtil.randomInt(2,6)+"",betChip);
    }
}
