package com.dmg.zhajinhuaserver.manager.work.timer;

import com.dmg.common.core.util.SpringUtil;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.PlayerBetChipsService;
import com.dmg.zhajinhuaserver.service.RobotActionService;
import com.dmg.zhajinhuaserver.service.impl.RobotActionServiceImpl;
import com.zyhy.common_server.work.Cron;
import com.zyhy.common_server.work.TimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhuqd
 * @Date 2017年12月11日
 * @Desc 机器人自动看牌
 */
@Slf4j
@Cron("*/1 * * * * ?")
public class RequestRobotSeeCards extends TimeWork {

    @Override
    public void init(Object... args) {
    }

    @Override
    public void go() {
        Map<Integer, GameRoom> roomMap = RoomManager.instance().getRoomMap();
        for (int roomId : roomMap.keySet()) {
            GameRoom room = RoomManager.instance().getRoom(roomId);
            PlayerBetChipsService playerBetChipsService = SpringUtil.getBean(PlayerBetChipsService.class);
            if (room.getGameRoomTypeId() == D.FREE_FIELD && room.isReady()) {
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.isReady() && !seat.isHaveSeenCard() && seat.getPlayer() instanceof Robot) {
                        RobotActionService robotActionService = SpringContextUtil.getBean(RobotActionServiceImpl.class);
                        //判断是否自动看牌
                        if (robotActionService.getRobotActionIsSee(room, seat)) {
                            log.info("房间：{}，player:{},【看牌】", room.getRoomId(), seat.getPlayer().getRoleId());
                            playerBetChipsService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.SEECARDS, 0);
                            break;
                        }
                    }
                }
            }
        }
    }

}
