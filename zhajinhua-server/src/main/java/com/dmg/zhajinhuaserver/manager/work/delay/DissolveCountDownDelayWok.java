package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.AnswerDissolveService;
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
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            return;
        }
        AnswerDissolveService answerDissolveService = SpringContextUtil.getBean(AnswerDissolveService.class);
        Map<Integer, Boolean> dissolveMap = room.getDissolveMap();
        if (dissolveMap.isEmpty()) { // 房间解散失败，数据已经清除
            return;
        }
        for (int seat = 1; seat <= room.getTotalPlayer(); seat++) {
            if (dissolveMap.get(seat) == null) {
                Seat data = room.getSeatMap().get(seat);
                answerDissolveService.answerDissolveRoom(data.getPlayer(), true);
            }
        }
    }

}
