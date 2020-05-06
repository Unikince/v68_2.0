package com.dmg.niuniuserver.manager.work;


import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.process.AbstractMessageHandler;
import com.dmg.niuniuserver.process.ForceLeaveRoomProcess;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;
import com.dmg.niuniuserver.service.action.ReadyService;
import com.dmg.niuniuserver.service.impl.RoomServiceImpl;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadyPlayerDelayWork extends DelayTimeWork {

    private int roomId;
    private int seatId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.seatId = (int) args[1];
    }

    @Override
    public void go() {
        ReadyService readyService = SpringContextUtil.getBean(ReadyService.class);
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null || room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY) {
            return;
        }
        if (seatId != 0) {
            Seat seat = room.getSeatMap().get(seatId);
            if (seat == null) {
                return;
            }
            if (!seat.isReady() && roomId == seat.getPlayer().getRoomId() && (seat.getPlayer() instanceof Robot)) {
                //判断当前房间号是否一致
                readyService.ready(seat.getPlayer(), true, false);
            }
            if (roomId != seat.getPlayer().getRoomId()) {
                room.getSeatMap().remove(seatId);
            }
        } else {
            if (room.getSeatMap().size() < 2) {
                return;
            }
            AbstractMessageHandler forceLeaveRoom = SpringContextUtil.getBean(ForceLeaveRoomProcess.class);
            for (Seat seat : room.getSeatMap().values()) {
                if (!seat.isReady()) {
                    if (seat.getPlayer() instanceof Robot) {
                        readyService.ready(seat.getPlayer(), true, true);
                    } else {
                        log.info("房间：{},player:{},倒计时结束未准备【踢出】", roomId, seat.getPlayer().getUserId());
                        // 没有准备的玩家自动踢出
                        forceLeaveRoom.messageHandler(seat.getPlayer().getUserId(), null);
                    }
                }
            }
            // 房间是否全是机器人
            boolean allrobot = true;
            for (Seat checkSeat : RoomManager.instance().getRoom(roomId).getSeatMap().values()) {
                if (checkSeat.getPlayer() != null && !(checkSeat.getPlayer() instanceof Robot)) {
                    allrobot = false;
                    break;
                }
            }
            if (allrobot) {
                RoomService roomService = SpringContextUtil.getBean(RoomService.class);
                // 全是机器人,解散房间
                roomService.solveRoom(roomId);
            }
            // 检查玩家准备状态, 然后判断是否进入下一步操作
            RoomService roomService = SpringContextUtil.getBean(RoomServiceImpl.class);
            roomService.canEnterDeal(RoomManager.instance().getRoom(roomId));
        }
    }
}
