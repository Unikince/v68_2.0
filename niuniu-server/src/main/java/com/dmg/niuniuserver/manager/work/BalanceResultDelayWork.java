package com.dmg.niuniuserver.manager.work;

import static com.dmg.niuniuserver.config.MessageConfig.NTC_SEND_READ_INFO;

import java.util.HashMap;
import java.util.Map;

import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.GameConfig;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;
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
        RoomService roomService = SpringContextUtil.getBean(RoomService.class);
        PushService pushService = SpringContextUtil.getBean(PushService.class);
        GameRoom room = RoomManager.instance().getRoom(this.roomId);
        if (room == null) {
            log.warn("this room is no exist!!");
            return;
        }
        log.info("房间{},重置数据，准备开始下一把", room.getRoomId());
        roomService.handleRoundEnd(room);
        // 停服通知
        if (RoomManager.instance().isShutdownServer()) {
            room.setRoomStatus(RoomStatus.STOP_STATUS);
            for (Map.Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
                Seat seat = entry.getValue();
                if (!(seat.getPlayer() instanceof Robot)) {
                    MessageResult messageResult = new MessageResult(MessageConfig.SHUTDOWN_SERVER_NTC);
                    pushService.push(seat.getPlayer().getUserId(), messageResult);
                    continue;
                }
            }
            return;
        }
        // 通知客户端所有玩家准备
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.isReady()) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("userId", seat.getPlayer().getUserId());
            if (!room.isPrivateRoom()) {
                map.put("countdownTime", System.currentTimeMillis() + room.getReadyTime());
            }
            MessageResult messageResult = new MessageResult(NTC_SEND_READ_INFO, map);
            pushService.push(seat.getPlayer().getUserId(), messageResult);
        }
    }
}
