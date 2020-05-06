package com.dmg.niuniuserver.manager.work;


import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.process.AbstractMessageHandler;
import com.dmg.niuniuserver.process.ForceLeaveRoomProcess;
import com.dmg.niuniuserver.service.RoomService;
import com.dmg.niuniuserver.service.action.ReadyService;
import com.dmg.niuniuserver.service.impl.RoomServiceImpl;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

/**
 * 未准备踢出
 */
@Slf4j
public class ReadyPlayerKickOutWork extends DelayTimeWork {

    private int roomId;

    private Long userId;

    //加入房间时间戳
    private String joinRoomTimeStamp;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.userId = (Long) args[1];
        this.joinRoomTimeStamp = String.valueOf(args[2]);
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null || room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY) {
            return;
        }
        AbstractMessageHandler forceLeaveRoom = SpringContextUtil.getBean(ForceLeaveRoomProcess.class);
        for (Seat seat : room.getSeatMap().values()) {
            if (!(seat.getPlayer() instanceof Robot)
                    && seat.getPlayer().getUserId().intValue() == userId.intValue()
                    && seat.getPlayer().getJoinRoomTimeStamp().equals(joinRoomTimeStamp)
                    && !seat.isReady()) {
                // 没有准备的玩家自动踢出
                log.info("房间：{},player:{},倒计时结束未准备【踢出】", roomId, seat.getPlayer().getUserId());
                forceLeaveRoom.messageHandler(seat.getPlayer().getUserId(), null);
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
