package com.dmg.niuniuserver.manager.work;


import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.LeaveReason;
import com.dmg.niuniuserver.service.RoomService;
import com.zyhy.common_server.work.Cron;
import com.zyhy.common_server.work.TimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.dmg.niuniuserver.model.constants.RoomStatus.STATE_WAIT_ALL_READY;


/**
 * @description: 清除房间
 * @param
 * @return
 * @author mice
 * @date 2019/7/9
*/
//@Cron("0 0/3 * * * ? ")
@Slf4j
public class CleanRoomTimeWork extends TimeWork {

    @Override
    public void init(Object... args) {

    }

    @Override
    public void go() {
        log.info("==>定时清理房间");
        RoomService roomService = SpringContextUtil.getBean(RoomService.class);
        Map<Integer, GameRoom> roomMap = RoomManager.instance().getRoomMap();
        Map<Integer,Integer> roomRoundMap = RoomManager.instance().getRoomRoundMap();
        for (int roomId : roomMap.keySet()) {
            GameRoom room = RoomManager.instance().getRoom(roomId);
            if (roomRoundMap.get(roomId)==null){
                roomRoundMap.put(roomId,room.getRound());
            }else if (roomRoundMap.get(roomId)==room.getRound()&&room.getRoomStatus()>STATE_WAIT_ALL_READY){
                // 踢掉所有玩家
                for (Seat seat : room.getSeatMap().values()) {
                    seat.getPlayer().setLeaveReason(LeaveReason.LEAVE_NOMONEY);
                    roomService.quitRoom(seat.getPlayer(), false);
                }
            }else {
                roomRoundMap.put(roomId,room.getRound());
            }
        }
    }

}
