package com.dmg.niuniuserver.manager.work;

import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.service.RoomService;
import com.zyhy.common_server.work.Cron;
import com.zyhy.common_server.work.TimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 自动更新房间状态
 * @Author: Lemon
 * @Date: 2019/8/21 16:59
 * @Version: 1.0
 */
@Cron("0/10 * * * * ? ")
@Slf4j
public class AutoUpdateRoomStatusTimeWork extends TimeWork {

    @Override
    public void init(Object... args) {

    }

    @Override
    public void go() {
        log.debug("定时更新房间状态");
        RoomService roomService = SpringContextUtil.getBean(RoomService.class);
        Map<Integer, GameRoom> roomMap = RoomManager.instance().getRoomMap();
        Map<Integer, Map> roomStatusMap = RoomManager.instance().getRoomStatusMap();
        for (Map.Entry<Integer, GameRoom> roomEntry : roomMap.entrySet()) {
            if (roomEntry.getValue().getSeatMap().size() < 2) {
                continue;
            }
            if (null == roomStatusMap.get(roomEntry.getKey())) {
                Map<String, Object> statusMap = new HashMap();
                statusMap.put("time", System.currentTimeMillis());
                statusMap.put("status", roomEntry.getValue().getRoomStatus());
                roomStatusMap.put(roomEntry.getKey(), statusMap);
            } else {
                Map<String, Object> statusMap = roomStatusMap.get(roomEntry.getKey());
                if (Integer.valueOf(statusMap.get("status").toString()) != roomEntry.getValue().getRoomStatus()) {
                    statusMap.put("time", System.currentTimeMillis());
                    statusMap.put("status", roomEntry.getValue().getRoomStatus());
                    roomStatusMap.put(roomEntry.getKey(), statusMap);
                } else {
                    Boolean isClear = true;
                    for (Seat seat : roomEntry.getValue().getSeatMap().values()) {
                        if (!(seat.getPlayer() instanceof Robot)
                                && (seat.isReady() || (System.currentTimeMillis() - Long.valueOf(seat.getPlayer().getJoinRoomTimeStamp())) <= roomEntry.getValue().getReadyTime())) {
                            isClear = false;
                            break;
                        }
                    }
                    if (!isClear) {
                        return;
                    }
                    log.info("清理房间【{}】,【{}】", roomEntry.getKey(), roomEntry.getValue());
                    long time = System.currentTimeMillis() - Long.valueOf(statusMap.get("time").toString());
                    if (time > 20000) {
                        GameRoom room = roomService.getRoom(roomEntry.getKey());
                        roomService.handleRoundEnd(room);
                    }
                }
            }
        }
        RoomManager.instance().setRoomStatusMap(roomStatusMap);
    }
}
