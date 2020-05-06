package com.dmg.bairenzhajinhuaserver.quarz;

import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.common.work.DelayTimeWork;
import com.dmg.bairenzhajinhuaserver.service.logic.RoomActionService;
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
        RoomActionService service = SpringContextUtil.getBean(RoomActionService.class);
        service.action(roomId);
    }
}
