package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.dto.OutPlayerDTO;
import com.dmg.zhajinhuaserver.service.QuitRoomService;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class QuitRoomDelayWork extends DelayTimeWork {

    private int roomId;
    private List<OutPlayerDTO> outList;

    @SuppressWarnings("unchecked")
    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.outList = (List<OutPlayerDTO>) args[1];
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            log.warn("this room is no exist!!");
            return;
        }
        QuitRoomService quitRoomService = SpringContextUtil.getBean(QuitRoomService.class);
        for (Seat seat : room.getSeatMap().values()) {
            outList.forEach(outPlayer -> {
                if (outPlayer.getSeatId() == seat.getSeatId()) {
                    quitRoomService.quitRoom(seat.getPlayer(), false, outPlayer.getLeaveReason());
                }
            });
        }
    }
}
