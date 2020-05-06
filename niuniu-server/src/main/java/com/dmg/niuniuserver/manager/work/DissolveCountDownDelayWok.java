package com.dmg.niuniuserver.manager.work;


import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.service.RoomService;
import com.zyhy.common_server.work.DelayTimeWork;

import java.util.Map;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc 30秒玩家没有投票自动解散
 */
public class DissolveCountDownDelayWok extends DelayTimeWork {

    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
        RoomService roomService = SpringContextUtil.getBean(RoomService.class);
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            return;
        }
        Map<Integer, Boolean> dissolveMap = room.getDissolveMap();
        if (dissolveMap.isEmpty()) { // 房间解散失败，数据已经清除
            return;
        }
        for (int seat = 1; seat <= room.getSeatMap().size(); seat++) {
            if (dissolveMap.get(seat) == null) {
                Seat data = room.getSeatMap().get(seat);
                roomService.answerDissolveRoom(data.getPlayer(), true);
            }
        }
    }
}
