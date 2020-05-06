package com.dmg.bairenzhajinhuaserver.state.impl;

import com.dmg.bairenzhajinhuaserver.common.exception.BusinessException;
import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRobot;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRoom;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.manager.TimerManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.model.RoomStatus;
import com.dmg.bairenzhajinhuaserver.model.Seat;
import com.dmg.bairenzhajinhuaserver.model.dto.RoomInfoDTO;
import com.dmg.bairenzhajinhuaserver.quarz.RoomActionDelayTask;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.service.cache.RobotCacheService;
import com.dmg.bairenzhajinhuaserver.state.RoomState;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

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
                    if (!seat.isLeave()){
                        seat.getPlayer().push(messageResult);
                    }
                }
            });
            return;
        }

        room.setRoomStatus(RoomStatus.START);
        room.setGodOfGamblersFirstBetTable(null);
//          QuitRoomService service = SpringUtil.getBean(QuitRoomService.class);
//          List<Integer> kickList = new ArrayList<>();
//          for(Seat seat : room.getSeatMap().values()) {
//          	if(seat.getNoBetCount() >= 5) {
//          		kickList.add(seat.getPlayer().getUserId());
//          	}
//          }
//          kickList.forEach(userId -> {
//          	service.quitRoom(userId, true);
//          });
        synchronized (room) {
            this.organizeRoomData();
        }
        RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
        BeanUtils.copyProperties(room, roomInfoDTO);
        roomInfoDTO.setRoomLevel(room.getLevel());
        MessageResult messageResult = new MessageResult(MessageIdConfig.UPDATE_ROOM_NTC, roomInfoDTO);

        if (room.getCurRound() >= Integer.MAX_VALUE) {
            room.setCurRound(1);
        } else {
            room.setCurRound(room.getCurRound() + 1);
        }
        if (!room.isSystemBanker()) {
            roomInfoDTO.setSelfSeat(room.getBanker());
            room.getBanker().getPlayer().push(messageResult);
        }
        room.getSeatMap().values().forEach(seat -> {
            if (seat.getPlayer() instanceof BaseRobot) {
                seat.setGameCount(seat.getGameCount() + 1);
            } else {
                roomInfoDTO.setSelfSeat(seat);
                if (!seat.isLeave()){
                    seat.getPlayer().push(messageResult);
                }
            }
        });

        room.changeState();
        if (room.isBankerChanged()) {
            try {
                room.setCountdownTime(System.currentTimeMillis() + 1000);
                TimerManager.instance().submitDelayWork(RoomActionDelayTask.class, 1000, room.getRoomId());
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        } else {
            room.action();
        }
    }

    /**
     * @return void
     * @description: 整理数据
     * @author mice
     * @date 2019/8/1
     */
    private void organizeRoomData() {
        Room room = (Room) this.room;
        RobotCacheService robotCacheService = SpringContextUtil.getBean(RobotCacheService.class);
        PlayerService playerCacheService = SpringContextUtil.getBean(PlayerService.class);
        RoomManager manager = RoomManager.intance();
        room.setBankerChanged(false);
        // 清理房间数据信息
        room.getInfieldSeatMap().clear();
        room.setCurRoundTotalBetChip(new BigDecimal(0));
        // 清空牌桌数据
        room.getTableMap().values().forEach(table -> {
            table.clear();
        });
        room.setHasPlayerBet(false);

        // 设置庄家
        if (room.isSystemBanker()) {
            this.setBankerSeat();
        } else {
            int round = manager.getGameConfigMap().get(room.getLevel()).getBairenFileConfigDTO().getBankRoundLimit();
            BigDecimal down = new BigDecimal(manager.getGameConfigMap().get(room.getLevel()).getBairenFileConfigDTO().getApplyBankerLimit());
            Seat bankerSeat = room.getBanker();
            BasePlayer bankerPlayer = playerCacheService.getPlayer(bankerSeat.getPlayer().getUserId());
            if (bankerSeat.getBankGameCount() >= round || !bankerPlayer.isOnline() || bankerSeat.getPlayer().getGold().compareTo(down) < 0) {
                room.getSeatMap().put("-1", bankerSeat);
                bankerSeat.setBankGameCount(0);
                bankerSeat.setBanker(false);
                this.setBankerSeat();
            } else {
                bankerSeat.setBankGameCount(bankerSeat.getBankGameCount() + 1);
            }
        }
        room.getBanker().clear();
        BigDecimal taiHong = manager.getRedValue(room.getLevel());
        if (!room.isSystemBanker()) {
            BigDecimal bankerGold = room.getBanker().getPlayer().getGold();
            taiHong = taiHong.compareTo(bankerGold) > 0 ? bankerGold : taiHong;
        }
        room.setTaiHong(taiHong);
        room.setRestCanBetChip(taiHong);
        if (CollectionUtils.isEmpty(room.getSeatMap())) {
            return;
        }
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
                if (!(seat.getPlayer() instanceof BaseRobot)) {
                    playerCacheService.syncRoom(0, seat.getPlayer().getUserId(),room.getLevel());
                }
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
                        playerCacheService.syncRoom(0, basePlayer.getUserId(),room.getLevel());
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
            // 放入前8个内场玩家
            if (seatIndex <= 8) {
                room.getInfieldSeatMap().put(seatIndex + "", seat);
            }
        }
        room.setPlayerNumber(room.getSeatMap().size());
    }

    /**
     * 设置庄家
     *
     * @param
     */
    private void setBankerSeat() {
        Room room = (Room) this.room;
        if (!CollectionUtils.isEmpty(room.getApplyToZhuangPlayerList())) {
            PlayerService playerCacheService = SpringContextUtil.getBean(PlayerService.class);
            BasePlayer basePlayer = room.getApplyToZhuangPlayerList().pop();
            Seat seat = RoomManager.intance().getSeat(room, basePlayer.getUserId());
            basePlayer = playerCacheService.getPlayer(basePlayer.getUserId());
            if (seat != null && basePlayer.isOnline()) {
                seat.setApplyBanker(false);
                seat.clear();
                seat.setBanker(true);
                seat.setNoBetCount(0);
                seat.setBankGameCount(1);
                seat.setBankGameCountLimit(RoomManager.intance().getBankRoundLimit(room.getLevel()));
                room.setBanker(seat);
                room.setSystemBanker(false);
                room.getSeatMap().remove(seat.getSeatIndex() + "");
                seat.setSeatIndex(-1);
                room.setBankerChanged(true);
            } else {
                if (!basePlayer.isOnline() && seat != null) {
                    seat.setApplyBanker(false);
                }
                this.setBankerSeat();
            }
        } else {
            if (!room.isSystemBanker()) {
                room.setBankerChanged(true);
            }
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
     * @param left 数组的前针
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
