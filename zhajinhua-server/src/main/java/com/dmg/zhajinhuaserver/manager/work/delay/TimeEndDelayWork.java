package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.dto.OutPlayerDTO;
import com.dmg.zhajinhuaserver.service.QuitRoomService;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TimeEndDelayWork extends DelayTimeWork {

    private int roomId;
    private OutPlayerDTO outPlayerDTO;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.outPlayerDTO = (OutPlayerDTO) args[1];
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        QuitRoomService quitRoomService = SpringContextUtil.getBean(QuitRoomService.class);
        synchronized (room) {
            for (Seat seat : room.getSeatMap().values()) {
                if (!(seat.getPlayer() instanceof Robot)
                        && outPlayerDTO.getSeatId() == seat.getSeatId()
                        && seat.getPlayer().getJoinRoomTimeStamp().equals(outPlayerDTO.getJoinRoomTimeStamp())
                        && !seat.isReady()) {
                    log.info("房间：{}，倒计时结束,玩家:{},未准备[踢出]", roomId, seat.getPlayer().getRoleId());
                    quitRoomService.quitRoom(seat.getPlayer(), false, Config.LeaveReason.LEAVE_NOREADY);
                }
            }
        }
    }
}
