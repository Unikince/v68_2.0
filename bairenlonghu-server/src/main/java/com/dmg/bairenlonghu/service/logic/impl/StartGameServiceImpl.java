package com.dmg.bairenlonghu.service.logic.impl;

import java.math.BigDecimal;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dmg.bairenlonghu.common.constant.Constant;
import com.dmg.bairenlonghu.common.exception.BusinessException;
import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.common.model.BaseRobot;
import com.dmg.bairenlonghu.common.result.MessageResult;
import com.dmg.bairenlonghu.common.result.ResultEnum;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.manager.TimerManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.model.RoomStatus;
import com.dmg.bairenlonghu.model.Seat;
import com.dmg.bairenlonghu.model.Table;
import com.dmg.bairenlonghu.model.constants.D;
import com.dmg.bairenlonghu.model.dto.RoomInfoDTO;
import com.dmg.bairenlonghu.quarz.RobotBetDelayTask;
import com.dmg.bairenlonghu.quarz.SettleDelayTask;
import com.dmg.bairenlonghu.service.PushService;
import com.dmg.bairenlonghu.service.cache.PlayerService;
import com.dmg.bairenlonghu.service.cache.RobotCacheService;
import com.dmg.bairenlonghu.service.logic.StartGameService;
import com.dmg.bairenlonghu.sysconfig.RegionConfig;
import com.dmg.bairenlonghu.tcp.server.MessageIdConfig;
import com.dmg.common.core.util.RedisUtil;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/30 14:13
 * @Version V1.0
 **/
@Service
@Slf4j
public class StartGameServiceImpl implements StartGameService {

    @Autowired
    private PlayerService playerCacheService;

    @Autowired
    private RobotCacheService robotCacheService;

    @Autowired
    private PushService pushService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void startGame(int roomId) {
        Room room = RoomManager.intance().getRoom(roomId);
        if (room == null) {
            throw new BusinessException(ResultEnum.ROOM_NO_EXIST.getCode() + "", ResultEnum.ROOM_NO_EXIST.getMsg());
        }
        // 停服通知
        if (RoomManager.intance().isShutdownServer()) {
            MessageResult messageResult = new MessageResult(MessageIdConfig.SHUTDOWN_SERVER_NTC);
            this.pushService.broadcast(messageResult, room);
            return;
        }
        room.setRoomStatus(RoomStatus.BET);
        this.organizeRoomData(room);
        RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
        BeanUtils.copyProperties(room, roomInfoDTO);
        roomInfoDTO.setReportFormMap(this.redisUtil.lGet(RegionConfig.GAME_REPORT_FROM_MAP + ":" + Constant.GAME_ID + "_" + room.getLevel(), 0, -1));
        MessageResult messageResult = new MessageResult(MessageIdConfig.UPDATE_ROOM_NTC, roomInfoDTO);
        synchronized (room) {
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getPlayer() instanceof BaseRobot) {
                    seat.setGameCount(seat.getGameCount() + 1);
                    continue;
                }
                roomInfoDTO.setSelfSeat(seat);
                this.pushService.push(seat.getPlayer().getUserId(), messageResult);
            }
        }
        if (!room.isSystemBanker()) {
            roomInfoDTO.setSelfSeat(room.getBanker());
            pushService.push(room.getBanker().getPlayer().getUserId(), messageResult);
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long betTime = System.currentTimeMillis() + D.BET_TIME;
        messageResult = new MessageResult(MessageIdConfig.BET_NTC, betTime);
        this.pushService.broadcast(messageResult, room);
        try {
            room.setCountdownTime(betTime);
            TimerManager.instance().submitDelayWork(SettleDelayTask.class, D.BET_TIME, roomId);
            synchronized (room) {
                for (Seat seat : room.getSeatMap().values()) {
                    if (!(seat.getPlayer() instanceof BaseRobot)) {
                        continue;
                    }
                    List<Integer> betChips = RoomManager.intance().getBetChipsMap().get(room.getLevel());
                    int random = RandomUtil.randomInt(100);
                    if (random < 20) {
                        continue;
                    }
                    BigDecimal betChip = new BigDecimal(betChips.get(0));
                    if (random > 40) {
                        betChip = new BigDecimal(betChips.get(1));
                    }
                    if (random > 50) {
                        betChip = new BigDecimal(betChips.get(2));
                    }
                    if (random > 60) {
                        betChip = new BigDecimal(betChips.get(3));
                    }
                    if (random > 80) {
                        betChip = new BigDecimal(betChips.get(4));
                    }
                    TimerManager.instance().submitDelayWork(RobotBetDelayTask.class, RandomUtil.randomInt(1000, 3000), roomId, seat.getPlayer().getUserId(), betChip);
                    TimerManager.instance().submitDelayWork(RobotBetDelayTask.class, RandomUtil.randomInt(2000, 5000), roomId, seat.getPlayer().getUserId(), betChip);
                    TimerManager.instance().submitDelayWork(RobotBetDelayTask.class, RandomUtil.randomInt(4000, 7000), roomId, seat.getPlayer().getUserId(), betChip);
                    TimerManager.instance().submitDelayWork(RobotBetDelayTask.class, RandomUtil.randomInt(6000, 9000), roomId, seat.getPlayer().getUserId(), betChip);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        log.info("==>房间:{},进入下注阶段", roomId);
    }

    /**
     * @param room
     * @return void
     * @description: 整理数据
     * @author mice
     * @date 2019/8/1
     */
    private void organizeRoomData(Room room) {
        // 清理房间数据信息
        room.getInfieldSeatMap().clear();
        room.setCurRoundTotalBetChip(new BigDecimal(0));
        room.setBetChipLimit(BigDecimal.ZERO);
        room.setSettleResultDTO(null);
        // 清空牌桌数据
        for (Table table : room.getTableMap().values()) {
            table.getBetChipList().clear();
            table.getPokerList().clear();
            table.setCardType(0);
            table.setBetChipTotal(new BigDecimal(0));
            table.setRobotBetChipTotal(new BigDecimal(0));
            table.setPlayerBetChipTotal(new BigDecimal(0));
        }

        room.getBanker().getWinGoldMap().clear();
        room.getBanker().setCurWinGold(new BigDecimal(0));
        // 设置庄家
        if (room.isSystemBanker()) {
            if (!CollectionUtils.isEmpty(room.getApplyToZhuangPlayerList())) {
                this.setBankerSeat(room);
            }
        } else {
            Seat bankerSeat = room.getBanker();
            BigDecimal bankerLimit = RoomManager.intance().getBankerGoldLowLimit(room.getLevel());
            if (bankerSeat.getBankGameCount() >= RoomManager.intance().getBankRoundLimit(room.getLevel()) || bankerSeat.isLeave() || bankerSeat.getPlayer().getGold().compareTo(bankerLimit) < 0) {
                synchronized (room) {
                    room.getSeatMap().put("999", bankerSeat);
                }
                bankerSeat.setBankGameCount(0);
                this.setBankerSeat(room);
            } else {
                bankerSeat.setBankGameCount(bankerSeat.getBankGameCount() + 1);
            }
        }
        // 设置下注额限制(台红值)
        if (!room.isSystemBanker()
                && room.getBanker().getPlayer().getGold().compareTo(RoomManager.intance().getRedValue(room.getLevel())) < 0) {
            BigDecimal betChipLimit = BigDecimal.ZERO;
            int maxMaxMutiple = RoomManager.intance().getMaxMutiple(room.getLevel());
            betChipLimit = room.getBanker().getPlayer().getGold().divide(new BigDecimal(maxMaxMutiple));
            room.setBetChipLimit(betChipLimit);
        } else {
            room.setBetChipLimit(RoomManager.intance().getRedValue(room.getLevel()));
        }
        if (CollectionUtils.isEmpty(room.getSeatMap())) {
            return;
        }
        // 椅子重排序
        synchronized (room) {
            Seat[] seats = new Seat[room.getSeatMap().size()];
            room.getSeatMap().values().toArray(seats);
            room.getSeatMap().clear();
            // 按金币从大到小排序
            this.quickSort(seats, 0, seats.length - 1);
            int seatIndex = 0;
            for (int i = 0; i < seats.length; i++) {
                Seat seat = seats[i];
                if (seat.isLeave()) {
                    if (!(seat.getPlayer() instanceof BaseRobot)) {
                        this.playerCacheService.syncRoom(0, seat.getPlayer().getUserId(), room.getLevel());
                    }
                    continue;
                }
                if (seat.getPlayer() instanceof BaseRobot) {
                    BaseRobot baseRobot = (BaseRobot) seat.getPlayer();
                    // 机器人局数限制 金币限制 踢出
                    if (seat.getGameCount() > ((BaseRobot) seat.getPlayer()).getPlayRoundLimit() ||
                            seat.getPlayer().getGold().compareTo(new BigDecimal(200)) < 0) {
                        baseRobot.setRoomId(0);
                        baseRobot.setSeatIndex(0);
                        this.robotCacheService.update(baseRobot);
                        continue;
                    }
                } else {
                    BasePlayer basePlayer = this.playerCacheService.getPlayer(seat.getPlayer().getUserId());
                    if (!basePlayer.isOnline()) {
                        basePlayer.setRoomId(0);
                        if (!(basePlayer instanceof BaseRobot)) {
                            this.playerCacheService.syncRoom(0, basePlayer.getUserId(), room.getLevel());
                        }
                        basePlayer.setSeatIndex(0);
                        this.playerCacheService.updatePlayer(basePlayer);
                        continue;
                    }
                }
                // 清空上局座位数据
                seat.getWinGoldMap().clear();
                seat.setReady(false);
                seat.setCurWinGold(new BigDecimal(0));
                seat.getLastBetChipRecordMap().clear();
                seat.getLastBetChipRecordMap().putAll(seat.getBetChipRecordMap());
                seat.getBetChipMap().clear();
                seat.getBetChipRecordMap().clear();

                // 重新坐位
                seat.getPlayer().setSeatIndex(++seatIndex);
                seat.setSeatIndex(seatIndex);
                room.getSeatMap().put(seatIndex + "", seat);
                if (seat.getPlayer() instanceof BaseRobot) {
                    this.robotCacheService.update((BaseRobot) seat.getPlayer());
                } else {
                    this.playerCacheService.updatePlayer(seat.getPlayer());
                }
                // 放入前8个内场玩家
                if (seatIndex <= 6) {
                    room.getInfieldSeatMap().put(seatIndex + "", seat);
                }
            }
            room.setPlayerNumber(room.getSeatMap().size());
            room.setCurRound(room.getCurRound() + 1);
        }
    }

    private void setBankerSeat(Room room) {
        if (!CollectionUtils.isEmpty(room.getApplyToZhuangPlayerList())) {
            BasePlayer basePlayer = room.getApplyToZhuangPlayerList().pop();
            Seat seat = RoomManager.intance().getSeat(room, basePlayer.getUserId());
            if (seat != null && !seat.isLeave()) {
                seat.getWinGoldMap().clear();
                seat.getBetChipMap().clear();
                seat.setReady(false);
                seat.setCurWinGold(new BigDecimal(0));

                seat.setBanker(true);
                seat.setBankGameCount(1);
                seat.setBankGameCountLimit(RoomManager.intance().getBankRoundLimit(room.getLevel()));
                room.setBanker(seat);
                room.setSystemBanker(false);
                synchronized (room) {
                    room.getSeatMap().remove(seat.getSeatIndex() + "");
                    seat.setSeatIndex(0);
                    basePlayer.setSeatIndex(0);
                    this.playerCacheService.updatePlayer(basePlayer);
                }
            } else {
                this.setBankerSeat(room);
            }
        } else {
            Seat bankerSeat = new Seat();
            BasePlayer basePlayer = new BasePlayer();
            basePlayer.setNickname("Banker");
            bankerSeat.setPlayer(basePlayer);

            room.setBanker(bankerSeat);
            room.setSystemBanker(true);
        }
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