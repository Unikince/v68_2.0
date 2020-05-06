package com.dmg.niuniuserver.manager.work;


import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.service.PlayActionService;
import com.zyhy.common_server.work.DelayTimeWork;

import static com.dmg.niuniuserver.config.MessageConfig.DO_ACTION;

/**
 * @description: 自动开牌任务
 * @return
 * @author mice
 * @date 2019/7/2
*/
public class OpenPokerWork extends DelayTimeWork {

    private int roomId;
    private int seatId;
    @Override
    public void init(Object... args) {
        roomId = (int) args[0];
        seatId = (int) args[1];
    }

    @Override
    public void go() {
        PlayActionService playActionService = SpringContextUtil.getBean(PlayActionService.class);
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room.getRoomStatus() != RoomStatus.STATE_KAIPAI) {
            return;
        }
        if(seatId != 0) {
        	Seat seat = room.getSeatMap().get(seatId);
        	if (room.getOpenCards().get(seat.getSeatId()) == null && seat.getHand() != null && seat.getHand().size() > 0) {
                playActionService.kaiPai(room, seat, DO_ACTION);
            }
        } else {
	        for (Seat seat : room.getSeatMap().values()) {
	            // 如果该玩家没有开牌
	            if (room.getOpenCards().get(seat.getSeatId()) == null && seat.getHand() != null && seat.getHand().size() > 0) {
                    playActionService.kaiPai(room, seat, null);
	            }
	        }
        }
    }
}
