package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.logic.RobotAutoActionLogic;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.PlayerBetChipsService;
import com.dmg.zhajinhuaserver.service.RobotActionService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.impl.RobotActionServiceImpl;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自动比牌
 *
 * @author Administrator
 */
@Slf4j
public class AutoCompareCardsDelayWork extends DelayTimeWork {

    private int roomId;
    private int seatId;
    private int beSeatId;
    private long compareGold;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.seatId = (int) args[1];
        this.beSeatId = (int) args[2];
        this.compareGold = (long) args[3];
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            log.warn("this room is no exist!!");
            return;
        }
        Seat seat = room.getSeatMap().get(seatId);
        if (seat.getPlayer() instanceof Robot) {
            RobotActionService robotActionService = SpringContextUtil.getBean(RobotActionServiceImpl.class);
            //判断是否自动比牌
            if (robotActionService.getRobotAction(room, seat) != Config.PokerOperState.COMPARECARD) {
                return;
            }
        }
        log.info("房间：{}，player:{}，beSeatId:{},【自动比牌】", roomId, seat.getPlayer().getRoleId(), beSeatId);
        PlayerBetChipsService playerBetChipsService = SpringContextUtil.getBean(PlayerBetChipsService.class);
        playerBetChipsService.handleBet(seat.getPlayer(), beSeatId, Config.PokerOperState.COMPARECARD, compareGold);
    }
}
