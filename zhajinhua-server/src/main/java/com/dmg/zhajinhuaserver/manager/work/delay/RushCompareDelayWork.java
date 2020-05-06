package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.model.bean.*;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.service.ALGService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_PLAY;
import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_PLAY_RESULT;

@Slf4j
public class RushCompareDelayWork extends DelayTimeWork {

    private int roomId;
    private Player player;
    private int beSeatId;
    private long wager;

    @SuppressWarnings("unchecked")
    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.player = (Player) args[1];
        this.beSeatId = (int) args[2];
        this.wager = (long) args[3];
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        RoomService roomService = SpringContextUtil.getBean(RoomService.class);
        PushService pushService = SpringContextUtil.getBean(PushService.class);
        ALGService algService = SpringContextUtil.getBean(ALGService.class);
        int seatId = roomService.getPlaySeat(room, player);
        Seat seat = room.getSeatMap().get(seatId);
        // 数据同步
        synchronized (room) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map1 = new HashMap<>();
            map.put(D.PLAYER_RID, player.getRoleId());
            map.put(D.TABLE_ACTION_SEAT_INDEX, seatId);
            map.put(ZhaJinHuaD.ACTION_TYPE, Config.PokerOperState.COMPARECARD);
            map.put(D.TABLE_CUR_TURN_NUM, room.getOperTurns());
            map1.put(ZhaJinHuaD.ACTION_TYPE, Config.PokerOperState.COMPARECARD);
            boolean win = true;
            int canActionseat = roomService.getCanActionCout(room);
            List<Integer> seatList = new ArrayList<>();
            if (beSeatId == 0) {
                for (Seat c : room.getSeatMap().values()) {
                    if (c.getSeatId() == seatId || c.isPass()
                            || c.isLostPk() || !c.isReady()) {
                        continue;
                    }
                    seatList.add(c.getSeatId());
                }
            }
            Seat pkseat = null;
            if (beSeatId == 0) {
                if (seatList.isEmpty()) {
                    return;
                } else {
                    pkseat = room.getSeatMap().get(seatList.get(0));
                }
            } else {
                pkseat = room.getSeatMap().get(beSeatId);
            }
            Player pp = pkseat.getPlayer();

            List<Poker> cardList = seat.getHand();
            List<Poker> becardList = pkseat.getHand();
            // ---------比较两家的牌-----PK---
            win = algService.operOneWin(room, cardList, becardList);
            if (win) {
                pkseat.setPass(true);
                pkseat.setLostPk(true);
                pkseat.setState(Config.SeatState.STATE_COMPARE_FAIL);
            } else {
                seat.setPass(true);
                seat.setLostPk(true);
                seat.setState(Config.SeatState.STATE_COMPARE_FAIL);
            }
            map.put(ZhaJinHuaD.LOSER_RID, seat.isLostPk() ? seat.getPlayer().getRoleId() : pp.getRoleId());
            map.put(ZhaJinHuaD.TARGET_RID, pp.getRoleId());
            map.put(ZhaJinHuaD.CHIPS, wager);

            map.put(ZhaJinHuaD.IS_RUSH, room.isHaveRush());

            map.put(D.TABLE_ALL_BETS, room.getTotalChips());
            map1.put(D.SEAT_STATE, seat.getState());
            map.put(D.SEAT_STATE, seat.getState());
            map.put(ZhaJinHuaD.TOTALSCORE, seat.getChipsRemain());

            MessageResult messageResult = new MessageResult(PLAYER_PLAY, map1);
            pushService.push(player.getRoleId(), messageResult);
            messageResult = new MessageResult(PLAYER_PLAY_RESULT, map);
            pushService.broadcast(messageResult, room);
            if (canActionseat == 2) {
                if (win) {
                    seat.setWinOrder(true);
                    room.setBankerSeat(seat.getSeatId());
                    room.getSeatMap().get(beSeatId).setWinOrder(false);
                } else {
                    room.getSeatMap().get(beSeatId).setWinOrder(true);
                    room.setBankerSeat(beSeatId);
                    seat.setWinOrder(false);
                }
                room.setHaveRush(false);
                try {
                    TimerManager.instance().submitDelayWork(BalanceResultDelayWork.class, 3500, room.getRoomId());
                } catch (SchedulerException e) {
                    log.error("submit BalanceResultDelayWork error:{}", e);
                }
                return;
            }
            if (win) {
                int beSeatId = roomService.getNextSeat(room);
                try {
                    TimerManager.instance().submitDelayWork(RushCompareDelayWork.class, 4000, room.getRoomId(), player, beSeatId, wager);
                } catch (SchedulerException e) {
                    log.error("submit CompareResultDelayWork error:{}", e);
                }
            } else {
                // 用于重连确定行动椅子,设置下个椅子轮次
                room.setHaveRush(false);
                room.setBloodId(0);
                if (seat.getActionSate() == 1) {
                    room.setPlaySeat(seatId);
                } else {
                    roomService.setNextPlaySeat(room);
                }
                roomService.turnsOverMax(room);
                if (room.getOperTurns() == room.getCountTurns()) {
                    return;
                }
                try {
                    TimerManager.instance().submitDelayWork(CompareResultDelayWork.class, 4000, room.getRoomId());
                } catch (SchedulerException e) {
                    log.error("submit CompareResultDelayWork error:{}", e);
                }
            }
        }
    }
}
