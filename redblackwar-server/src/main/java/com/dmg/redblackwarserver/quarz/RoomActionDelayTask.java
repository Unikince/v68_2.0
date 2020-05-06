package com.dmg.redblackwarserver.quarz;

import com.dmg.common.core.util.SpringUtil;
import com.dmg.redblackwarserver.common.work.DelayTimeWork;
import com.dmg.redblackwarserver.service.logic.RoomActionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomActionDelayTask extends DelayTimeWork {
    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
        RoomActionService service = SpringUtil.getBean(RoomActionService.class);
        service.action(roomId);
    }
}
