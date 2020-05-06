package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.QuitRoomService;
import com.dmg.zhajinhuaserver.service.ReadyService;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ReadyPlayerDelayWork extends DelayTimeWork {

    private int roomId;
    private List<Integer> outList;

    @SuppressWarnings("unchecked")
    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.outList = (List<Integer>) args[1];
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            log.warn("this room is no exist!!");
            return;
        }
        ReadyService readyService = SpringContextUtil.getBean(ReadyService.class);
        for (Seat seat : room.getSeatMap().values()) {
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                if (seat.isReady()) {
                    continue;
                }
                if (seat.isPlayed()) {
                    readyService.ready(seat.getPlayer(), true);
                }
            } else {
                if (seat.getPlayer().isActive() && !outList.contains(seat.getSeatId())) {
                    readyService.ready(seat.getPlayer(), true);
                } else {
                    QuitRoomService quitRoomService = SpringContextUtil.getBean(QuitRoomService.class);
                    quitRoomService.quitRoom(seat.getPlayer(), false, Config.LeaveReason.LEAVE_NORMAL);
                }
            }
        }
    }
}
