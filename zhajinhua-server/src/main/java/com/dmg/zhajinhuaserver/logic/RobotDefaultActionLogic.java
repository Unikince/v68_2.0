/**
 *
 */
package com.dmg.zhajinhuaserver.logic;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.RoomService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class RobotDefaultActionLogic {

    /**
     * 机器人默认行为操作
     * @param roomService
     * @param room
     * @param seat
     * @param wager
     * @param number
     */
    /*public static void defaultAutoAction(RoomService roomService, GameRoom room, Seat seat, long wager, int number) {
        switch (Config.Combination.forValue(seat.getHandCardsType())) {
            case HIGHCARD: {
                int count = 0;
                List<Integer> list = new ArrayList<>();
                for (Seat se : room.getSeatMap().values()) {
                    if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                        if (!se.isPlayed()) {
                            continue;
                        }
                    }
                    if (se.isReady() && !se.isPass()) {
                        if (se.getSeatId() != seat.getSeatId()) {
                            list.add(se.getSeatId());
                        }
                        count++;
                    }
                }
                if (count == 2 && room.getOperTurns() >= 3) {
                    int beid = (int) (Math.random() * list.size());
                    roomService.handleBet(seat.getPlayer(), list.get(beid), Config.PokerOperState.COMPARECARD, wager);
                } else if ((count == 2 && room.getOperTurns() < 3) || !seat.isHaveSeenCard()) {
                    roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.FOLLOWCHIPS, wager);
                } else {
                    roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.DISCARD, 0);
                }
                break;
            }
            case PAIR: {
                if (room.getOperTurns() >= 3) {
                    if (number == 0 && seat.isHaveSeenCard()) {
                        roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.DISCARD, 0);
                    } else if (number == 1) {
                        roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.FOLLOWCHIPS, wager);
                    } else {
                        List<Integer> list = new ArrayList<>();
                        for (Seat se : room.getSeatMap().values()) {
                            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                                if (!se.isPlayed()) {
                                    continue;
                                }
                            }
                            if (se.isReady() && !se.isPass()) {
                                if (se.getSeatId() != seat.getSeatId()) {
                                    list.add(se.getSeatId());
                                }
                            }
                        }
                        int beid = (int) (Math.random() * list.size());
                        roomService.handleBet(seat.getPlayer(), list.get(beid), Config.PokerOperState.COMPARECARD, wager);
                    }
                } else {
                    roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.FOLLOWCHIPS, wager);
                }
                break;
            }
            case PROGRESSION: {
                if (room.getOperTurns() >= 5) {
                    if (number == 0) {
                        roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.FOLLOWCHIPS, wager);
                    } else {
                        List<Integer> list = new ArrayList<>();
                        for (Seat se : room.getSeatMap().values()) {
                            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                                if (!se.isPlayed()) {
                                    continue;
                                }
                            }
                            if (se.isReady() && !se.isPass()) {
                                if (se.getSeatId() != seat.getSeatId()) {
                                    list.add(se.getSeatId());
                                }
                            }
                        }
                        int beid = (int) (Math.random() * list.size());
                        roomService.handleBet(seat.getPlayer(), list.get(beid), Config.PokerOperState.COMPARECARD, wager);
                    }
                } else {
                    roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.FOLLOWCHIPS, wager);
                }
                break;
            }
            case FLUSH:
            case STRAIGHTFLUSH:
            case LEOPARD:
                if (room.getOperTurns() >= 10) {
                    List<Integer> list = new ArrayList<>();
                    for (Seat se : room.getSeatMap().values()) {
                        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                            if (!se.isPlayed()) {
                                continue;
                            }
                        }
                        if (se.isReady() && !se.isPass()) {
                            if (se.getSeatId() != seat.getSeatId()) {
                                list.add(se.getSeatId());
                            }
                        }
                    }
                    int beid = (int) (Math.random() * list.size());
                    roomService.handleBet(seat.getPlayer(), list.get(beid), Config.PokerOperState.COMPARECARD, wager);
                } else {
                    if (seat.getOperNotify().canAddChips) {
                        roomService.handleBet(seat.getPlayer(), 255
                                , Config.PokerOperState.ADDCHIPS, room.getAddChipsBet() + 1);
                    } else {
                        int i = (int) (Math.random() * 100);
                        if (room.getOperTurns() > 3 && !room.isHaveRush() && i < 10) {
                            roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.RUSH, 0);
                        } else {
                            roomService.handleBet(seat.getPlayer(), 255, Config.PokerOperState.FOLLOWCHIPS, wager);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }*/
}
