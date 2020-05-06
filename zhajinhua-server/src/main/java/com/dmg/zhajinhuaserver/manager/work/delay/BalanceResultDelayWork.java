package com.dmg.zhajinhuaserver.manager.work.delay;


import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BalanceResultDelayWork extends DelayTimeWork {

    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            log.warn("this room is no exist!!");
            return;
        }
        RoomService roomService = SpringContextUtil.getBean(RoomService.class);
        roomService.handleRoundEnd(room, true);
    }

}
