package com.dmg.zhajinhuaserver.manager.work.delay;


import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.PlayerBetChipsService;
import com.dmg.zhajinhuaserver.service.RobotActionService;
import com.dmg.zhajinhuaserver.service.impl.RobotActionServiceImpl;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Slf4j
public class AutoCallDelayWork extends DelayTimeWork {

    private int roomId;
    private int seatId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.seatId = (int) args[1];
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            log.warn("this room is no exist!!");
            return;
        }
        PlayerBetChipsService playerBetChipsService = SpringContextUtil.getBean(PlayerBetChipsService.class);
        Seat seat = room.getSeatMap().get(seatId);
        if (seat == null) {
            return;
        }
        if (seat.getActionEndTime() == 0 || (seat.getActionEndTime() > System.currentTimeMillis() && !seat.isTrust())) {
            return;
        }
        if (seat.getState() == Config.SeatState.STATE_RUSH) {
            return;
        }
        //机器人操作
        if (seat.getPlayer() instanceof Robot) {
            RobotActionService robotActionService = SpringContextUtil.getBean(RobotActionServiceImpl.class);
            Integer actionId = robotActionService.getRobotAction(room, seat);
            if (Config.PokerOperState.FOLLOWCHIPS == actionId || Config.PokerOperState.ADDCHIPS == actionId) {
                Integer count = 0;
                for (Map.Entry<Integer, Seat> m : room.getSeatMap().entrySet()) {
                    Seat i = m.getValue();
                    if (i.isReady() && !i.isPass() && !i.isLostPk()) {
                        count += 1;
                    }
                }
                //若机器人身上金币数≤（场上剩余人数）*最大下注，强行每轮比牌
                if (seat.getPlayer().getGold() <= (count) * (room.getBetMul()[room.getBetMul().length - 1] * room.getBaseScore()) * 2) {
                    actionId = Config.PokerOperState.COMPARECARD;
                }
            }
            log.info("==================房间：{},player:{}{},操作：{}", roomId, seat.getPlayer().getRoleId(), seat.getPlayer().getNickname(), actionId);
            if (Config.PokerOperState.COMPARECARD == actionId) {
                //比牌
                for (Map.Entry<Integer, Seat> m : room.getSeatMap().entrySet()) {
                    Seat i = m.getValue();
                    if (i.getSeatId() != seat.getSeatId() && i.isReady() && !i.isPass() && !i.isLostPk() && i.isHaveSeenCard()) {
                        playerBetChipsService.handleBet(seat.getPlayer(), i.getSeatId(), actionId, room.wagerChips(seatId));
                        return;
                    }
                }
                for (Map.Entry<Integer, Seat> m : room.getSeatMap().entrySet()) {
                    Seat i = m.getValue();
                    if (i.getSeatId() != seat.getSeatId() && i.isReady() && !i.isPass() && !i.isLostPk()) {
                        playerBetChipsService.handleBet(seat.getPlayer(), i.getSeatId(), actionId, room.wagerChips(seatId));
                        return;
                    }
                }
            }
            int wager = room.wagerChips(seatId);
            playerBetChipsService.handleBet(seat.getPlayer(), 255, actionId, Config.PokerOperState.ADDCHIPS == actionId ? (wager + 1) : wager);
        } else {
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                if (room.getCustomRule().getMenRule() == 0) {
                    if (room.getOperTurns() <= 1) {
                        playerBetChipsService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.FOLLOWCHIPS, room.getLastBetChips());
                        return;
                    }
                }
                if (room.getCustomRule().getMenRule() == 1) {
                    if (room.getOperTurns() <= 1) {
                        if (room.getPlaySeat() == room.getBankerSeat()) {
                            playerBetChipsService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.FOLLOWCHIPS, room.getLastBetChips());
                            return;
                        }
                    }
                }
                playerBetChipsService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.DISCARD, 0);
            } else {
                playerBetChipsService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.DISCARD, 0);
            }
        }
    }
}
