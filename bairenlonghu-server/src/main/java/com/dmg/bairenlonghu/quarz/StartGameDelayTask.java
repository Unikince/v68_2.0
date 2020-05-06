package com.dmg.bairenlonghu.quarz;

import com.dmg.bairenlonghu.common.work.DelayTimeWork;
import com.dmg.bairenlonghu.service.logic.StartGameService;
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
        startGameService.startGame(roomId);
    }
}
