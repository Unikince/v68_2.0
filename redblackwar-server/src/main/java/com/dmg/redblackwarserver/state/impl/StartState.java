package com.dmg.redblackwarserver.state.impl;

import java.math.BigDecimal;

import com.dmg.common.core.util.SpringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.dmg.redblackwarserver.common.exception.BusinessException;
import com.dmg.redblackwarserver.common.model.BasePlayer;
import com.dmg.redblackwarserver.common.model.BaseRobot;
import com.dmg.redblackwarserver.common.model.BaseRoom;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.common.result.ResultEnum;
import com.dmg.redblackwarserver.manager.RoomManager;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.RoomStatus;
import com.dmg.redblackwarserver.model.Seat;
import com.dmg.redblackwarserver.model.dto.RoomInfoDTO;
import com.dmg.redblackwarserver.service.cache.PlayerService;
import com.dmg.redblackwarserver.service.cache.RobotCacheService;
import com.dmg.redblackwarserver.state.RoomState;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 开始阶段
 *
 * @author Administrator
 */
@Slf4j
public class StartState implements RoomState {

    private BaseRoom room;

    public StartState(BaseRoom room) {
        this.room = room;
    }

    @Override
    public State getState() {
        return State.START;
    }

    @Override
    public void action() {
        Room room = (Room) this.room;
        if (room == null) {
            throw new BusinessException(ResultEnum.ROOM_NO_EXIST.getCode() + "", ResultEnum.ROOM_NO_EXIST.getMsg());
        }
        // 停服通知
        if (RoomManager.intance().isShutdownServer()) {
            MessageResult messageResult = new MessageResult(MessageIdConfig.SHUTDOWN_SERVER_NTC);
            room.getSeatMap().values().forEach(seat -> {
                if (!(seat.getPlayer() instanceof BaseRobot)) {
                    if (!seat.isLeave()) {
                        seat.getPlayer().push(messageResult);
                    }
                }
            });
            return;
        }
        log.info("房间:{},开始新一局", room.getRoomId());
        room.setRoomStatus(RoomStatus.START);
        synchronized (room) {
            this.organizeRoomData();
        }
        RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
        BeanUtils.copyProperties(room, roomInfoDTO);
        roomInfoDTO.setRoomLevel(room.getLevel());
        MessageResult messageResult = new MessageResult(MessageIdConfig.UPDATE_ROOM_NTC, roomInfoDTO);
        room.getSeatMap().values().forEach(seat -> {
            if (seat.getPlayer() instanceof BaseRobot) {
                seat.setGameCount(seat.getGameCount() + 1);
            } else {
                roomInfoDTO.setSelfSeat(seat);
                (seat.getPlayer()).push(messageResult);
            }
        });

        if (room.getCurRound() >= Integer.MAX_VALUE) {
            room.setCurRound(1);
        } else {
            room.setCurRound(room.getCurRound() + 1);
        }
        room.changeState();
        room.action();
    }

    /**
     * @return void
     * @description: 整理数据
     * @author mice
     * @date 2019/8/1
     */
    private void organizeRoomData() {
        Room room = (Room) this.room;
        RobotCacheService robotCacheService = SpringUtil.getBean(RobotCacheService.class);
        PlayerService playerCacheService = SpringUtil.getBean(PlayerService.class);
        RoomManager manager = RoomManager.intance();
        // 清理房间数据信息
        room.getInfieldSeatMap().clear();
        room.setCurRoundTotalBetChip(new BigDecimal(0));
        // 清空牌桌数据
        room.getTableMap().values().forEach(table -> table.clear());
        BigDecimal taihong = manager.getTaiHongMap().get(room.getLevel());

       /* for (Entry<Integer, Double> entry : manager.getTaiHongMap().get(room.getLevel()).entrySet()) {
            room.getTaiHongMap().put(entry.getKey(), entry.getValue());
            room.getRestCanBetChip().put(entry.getKey(), entry.getValue());
        }*/
        room.setRestCanBetChip(taihong);
        room.setGodOfGamblersFirstBetTable(null);
        room.setAreBetLimitMap(manager.getAreaBetLimitMap().get(room.getLevel()));
        if (CollectionUtils.isEmpty(room.getSeatMap())) {
            return;
        }
        room.getBanker().clear();
        // 椅子重排序
        Seat[] seats = new Seat[room.getSeatMap().size()];
        room.getSeatMap().values().toArray(seats);
        room.getSeatMap().clear();
        // 按金币从大到小排序
        this.quickSort(seats, 0, seats.length - 1);
        int seatIndex = 0;
        for (int i = 0; i < seats.length; i++) {
            Seat seat = seats[i];
            if (seat.isLeave()) {
                continue;
            }
            if (seat.getPlayer() instanceof BaseRobot) {
                BaseRobot baseRobot = (BaseRobot) seat.getPlayer();
                // 机器人局数金币 限制 踢出
                if (seat.getGameCount() > ((BaseRobot) seat.getPlayer()).getPlayRoundLimit() || seat.getPlayer().getGold().compareTo(new BigDecimal(200)) < 0) {
                    baseRobot.setRoomId(0);
                    baseRobot.setSeatIndex(0);
                    robotCacheService.update(baseRobot);
                    continue;
                }
            } else {
                BasePlayer basePlayer = playerCacheService.getPlayer(seat.getPlayer().getUserId());
                if (!basePlayer.isOnline()) {
                    basePlayer.setRoomId(0);
                    if (!(basePlayer instanceof BaseRobot)) {
                        playerCacheService.syncRoom(0, 0, basePlayer.getUserId());
                    }
                    basePlayer.setSeatIndex(0);
                    playerCacheService.updatePlayer(basePlayer);
                    continue;
                }
            }
            // 清空上局座位数据
            seat.clear();
            // 重新坐位
            seat.getPlayer().setSeatIndex(++seatIndex);
            seat.setSeatIndex(seatIndex);
            room.getSeatMap().put(seatIndex + "", seat);
            if (seat.getPlayer() instanceof BaseRobot) {
                robotCacheService.update((BaseRobot) seat.getPlayer());
            } else {
                playerCacheService.updatePlayer(seat.getPlayer());
            }
            // 放入前6个内场玩家
            if (seatIndex <= 6) {
                room.getInfieldSeatMap().put(seatIndex + "", seat);
            }
        }
        room.setPlayerNumber(room.getSeatMap().size());
    }

    /**
     * @param seats 排序的数组
     * @param left  数组的前针
     * @param right 数组后针
     * @return void
     * @description: 快速排序 从大到小
     * @author mice
     * @date 2019/8/1
     */
    private void quickSort(Seat[] seats, int left, int right) {
        // 如果left等于right，即数组只有一个元素，直接返回
        if (left >= right) {
            return;
        }
        // 设置最左边的元素为基准值
        Seat key = seats[left];
        // 数组中比key大的放在左边，比key小的放在右边，key值下标为i
        int i = left;
        int j = right;
        while (i < j) {
            // j向左移，直到遇到比key大的值
            while (seats[j].getPlayer().getGold().compareTo(key.getPlayer().getGold()) <= 0 && i < j) {
                j--;
            }
            // i向右移，直到遇到比key小的值
            while (seats[i].getPlayer().getGold().compareTo(key.getPlayer().getGold()) >= 0 && i < j) {
                i++;
            }
            // i和j指向的元素交换
            if (i < j) {
                Seat temp = seats[i];
                seats[i] = seats[j];
                seats[j] = temp;
            }
        }
        seats[left] = seats[i];
        seats[i] = key;
        this.quickSort(seats, left, i - 1);
        this.quickSort(seats, i + 1, right);
    }
}
