package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.logic.RobotAutoActionLogic;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.manager.work.delay.BalanceResultDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.CompareResultDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.RushCompareDelayWork;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.bean.ZhaJinHuaD;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.ALGService;
import com.dmg.zhajinhuaserver.service.PlayerBetChipsService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_PLAY;
import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_PLAY_RESULT;

/**
 * @Description 下注
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
@Slf4j
@Service
public class PlayerBetChipsServiceImpl implements PlayerBetChipsService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PushService pushService;

    @Override
    public void handleBet(Player player, int beSeatId, int oper, long wager) {
        try {
            GameRoom room = roomService.getRoom(player.getRoomId());
            if (room == null) {
                pushService.push(player.getRoleId(), PLAYER_PLAY, ResultEnum.ROOM_NO_EXIST.getCode());
                return;
            }
            int seatId = roomService.getPlaySeat(room, player);
            if (seatId == 0) {
                pushService.push(player.getRoleId(), PLAYER_PLAY, ResultEnum.PLAYER_HAS_NO_SEAT.getCode());
                return;
            }
            Seat seat = room.getSeatMap().get(seatId);
            if (seat.getSeatId() != room.getPlaySeat() && oper != Config.PokerOperState.SEECARDS && oper != Config.PokerOperState.AUTOCALL) {
                pushService.push(player.getRoleId(), PLAYER_PLAY, ResultEnum.PLAYER_HAS_NO_SEAT.getCode());
                return;
            }
            if ((seat.isHaveSeenCard() && oper == Config.PokerOperState.SEECARDS)
                    || (!seat.isHaveSeenCard() && oper == Config.PokerOperState.SEECARDS && room.getOperTurns() <= 1)) {
                pushService.push(player.getRoleId(), PLAYER_PLAY, ResultEnum.PLAYER_HAS_ACTION_ERROR.getCode());
                return;
            }
            if (seat.getHand().isEmpty()) {
                pushService.push(player.getRoleId(), PLAYER_PLAY, ResultEnum.PLAYER_HAS_NO_CARDS.getCode());
                return;
            }
            // 数据同步
            synchronized (room) {
                // 剩下的个人底金不足桌子最低要求则弃牌
                if (room.getGameRoomTypeId() == D.FREE_FIELD) {
                    oper = roomService.noMoneyDiscard(oper, room, seat, wager);
                }
                long seatWager = 0;
                if (oper == Config.PokerOperState.FOLLOWCHIPS || oper == Config.PokerOperState.COMPARECARD) {
                    seatWager = room.wagerBetChips(seatId);
                } else if (oper == Config.PokerOperState.RUSH) {
                    player.setBloodNum(player.getBloodNum() + 1);
                    room.setBloodId((int) player.getRoleId());
                    room.setBloodUpGold(room.getLastBetChips());
                    room.setBloodUpSeenCards(room.isHaveSeenCards());
                    room.setHaveRush(true);
                    seatWager = room.getLastBetChips() * room.getOperTurns();
                } else if (oper == Config.PokerOperState.ADDCHIPS) {
                    if (wager > room.getBetMul().length && room.getAddChipsBet() < room.getBetMul().length) {
                        wager = room.getAddChipsBet() + 1;
                    }
                    if (wager > room.getBetMul().length) {
                        wager = room.getBetMul().length;
                    }
                    room.setAddChipsBet((int) wager);
                    if (seat.isHaveSeenCard()) {
                        seatWager = room.getBaseScore() * room.getBetMul()[room.getAddChipsBet() - 1] * 2;
                    } else {
                        seatWager = room.getBaseScore() * room.getBetMul()[room.getAddChipsBet() - 1];
                    }

                }
                if (oper != Config.PokerOperState.SEECARDS && oper != Config.PokerOperState.AUTOCALL) {
                    if (seat.getActionEndTime() == 0
                            && room.getOperTurns() != room.getCountTurns()) {
                        return;
                    }
                    seat.setOperedInOneTurn(true);
                    seat.setActionEndTime(0);
                    seat.setActionSate(2);
                } else {
                    seat.setActionSate(1);
                }
                roomService.addTurns(room);
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> map1 = new HashMap<>();
                map.put(D.PLAYER_RID, player.getRoleId());
                map.put(D.TABLE_ACTION_SEAT_INDEX, seatId);
                map.put(ZhaJinHuaD.ACTION_TYPE, oper);
                map.put(D.TABLE_CUR_TURN_NUM, room.getOperTurns());
                map1.put(ZhaJinHuaD.ACTION_TYPE, oper);
                boolean win = true;
                int canActionseat = roomService.getCanActionCout(room);
                if (oper == Config.PokerOperState.SEECARDS) {
                    this.seeCards(room, player, seatId, seat, map1);
                } else if (oper == Config.PokerOperState.AUTOCALL) {
                    this.autoCall(player, seat, map1);
                    return;
                } else if (oper == Config.PokerOperState.FOLLOWCHIPS
                        || oper == Config.PokerOperState.ADDCHIPS
                        || oper == Config.PokerOperState.RUSH) {
                    // 下注
                    if (oper == Config.PokerOperState.FOLLOWCHIPS) {
                        seat.setState(Config.SeatState.STATE_CALL);
                        if (seat.isHaveSeenCard()) {
                            //看牌下注人数加1
                            room.setSeeFollowUp(room.getSeeFollowUp() + 1);
                        }
                    }
                    if (oper == Config.PokerOperState.ADDCHIPS) {
                        seat.setState(Config.SeatState.STATE_RAISE);
                        if (seat.isHaveSeenCard()) {
                            //看牌加注人数加1
                            room.setSeeAnnotation(room.getSeeAnnotation() + 1);
                        }
                    }
                    if (oper == Config.PokerOperState.RUSH) {
                        seat.setState(Config.SeatState.STATE_RUSH);
                    }
                    roomService.playerBetChips(room, seat, seatWager, oper);
                    map.put(ZhaJinHuaD.CHIPS, seatWager);
                } else if (oper == Config.PokerOperState.DISCARD) {
                    // 弃牌
                    seat.setState(Config.SeatState.STATE_FOLD);
                    seat.setPass(true);
                } else if (oper == Config.PokerOperState.COMPARECARD) {
                    List<Integer> seatList = new ArrayList<>();
                    if (beSeatId == 0) {
                        for (Seat c : room.getSeatMap().values()) {
                            if (c.getSeatId() == seatId || c.isPass()
                                    || c.isLostPk() || !c.isReady()) {
                                continue;
                            }
                            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                                if (!c.isPlayed()) {
                                    continue;
                                }
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
                    int flag = roomService.playerCompareCards(room, seat, pkseat, seatWager, win, oper);
                    if (flag == 2) {
                        return;
                    }
                    win = flag == 1 ? true : false;
                    //比牌赢人数加1
                    room.setComparisonWin(room.getComparisonWin() + 1);
                    map.put(ZhaJinHuaD.LOSER_RID, seat.isLostPk() ? seat.getPlayer().getRoleId() : pp.getRoleId());
                    map.put(ZhaJinHuaD.TARGET_RID, pp.getRoleId());
                    map.put(ZhaJinHuaD.CHIPS, seatWager);
                }
                map.put(D.TABLE_ALL_BETS, room.getTotalChips());
                map1.put(D.SEAT_STATE, seat.getState());
                map.put(D.SEAT_STATE, seat.getState());
                map.put(ZhaJinHuaD.TOTALSCORE, seat.getChipsRemain());
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    map1.put("isfirstman", false);
                }
                MessageResult messageResult = new MessageResult(PLAYER_PLAY, map1);
                pushService.push(player.getRoleId(), messageResult);
                messageResult = new MessageResult(PLAYER_PLAY_RESULT, map);
                pushService.broadcast(messageResult, room);
                if (oper == Config.PokerOperState.SEECARDS) {
                    return;
                }
                if (oper == Config.PokerOperState.COMPARECARD) {
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
                } else {
                    if (roomService.resultByOnlyOne(room)) {
                        return;
                    }
                }
                if (oper == Config.PokerOperState.RUSH) {
                    try {
                        beSeatId = roomService.getNextSeat(room);
                        TimerManager.instance().submitDelayWork(RushCompareDelayWork.class, 1000, room.getRoomId(), player, beSeatId, seatWager);
                    } catch (SchedulerException e) {
                        log.error("submit CompareResultDelayWork error:{}", e);
                    }
                    return;
                }
                // 用于重连确定行动椅子,设置下个椅子轮次
                if (seat.getActionSate() == 1) {
                    room.setPlaySeat(seatId);
                } else {
                    roomService.setNextPlaySeat(room);
                }
                roomService.turnsOverMax(room);
                log.info(String.format(" actionSeatId:%d actionSeat:%d  nickname:%s betChips:%d", seatId, room.getPlaySeat(),
                        player.getNickname(), seatWager));
                if (room.getOperTurns() == room.getCountTurns()) {
                    return;
                }
                if (oper == Config.PokerOperState.COMPARECARD) {
                    try {
                        TimerManager.instance().submitDelayWork(CompareResultDelayWork.class, 4000, room.getRoomId());
                    } catch (SchedulerException e) {
                        log.error("submit CompareResultDelayWork error:{}", e);
                    }
                } else {
                    roomService.sendPlayerBaseAction(room, room.getPlaySeat());
                }
            }
        } catch (Exception e) {
            log.error("操作出现异常{}", e);
        }
    }

    /**
     * @param room
     * @param player
     * @param seatId
     * @param seat
     * @param map
     * @return void
     * @description: 看牌
     * @author mice
     * @date 2019/7/15
     */
    private void seeCards(GameRoom room, Player player, Integer seatId, Seat seat, Map<String, Object> map) {
        List<Integer> list = roomService.seeOwenrCards(room, seatId, seat);
        roomService.getRushMsg(player, room, map);
        map.put(D.PLAYER_BASE_ACTION_OPER, seat.getActionOper());
        map.put(ZhaJinHuaD.ADD_CHIPS, list);
        map.put(D.SEAT_HAND_CARDS, seat.getHand());
        map.put(D.SEAT_FOLLOW_CHIPS, room.wagerBetChips(seatId));
        map.put(ZhaJinHuaD.CARD_TYPE, seat.getHandCardsType());
    }

    /**
     * @param
     * @return void
     * @description: 自动跟注
     * @author mice
     * @date 2019/7/15
     */
    private void autoCall(Player player, Seat seat, Map<String, Object> map) {
        if (seat.isHasFollowEnd()) {
            seat.setHasFollowEnd(false);
        } else {
            seat.setHasFollowEnd(true);
        }
        map.put(D.SEAT_IS_AUTOCALL, seat.isHasFollowEnd());
        MessageResult messageResult = new MessageResult(PLAYER_PLAY, map);
        pushService.push(player.getRoleId(), messageResult);
    }
}
