package com.dmg.zhajinhuaserver.manager.work.delay;

import static com.dmg.zhajinhuaserver.config.MessageConfig.GAME_START_NTC;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.service.*;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import com.dmg.zhajinhuaserver.service.impl.ForceQuitRoomServiceImpl;
import com.dmg.zhajinhuaserver.service.impl.ReadyServiceImpl;
import com.dmg.zhajinhuaserver.service.impl.RoomServiceImpl;
import com.zyhy.common_server.work.DelayTimeWork;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadyTimeEndGameStartDelayWork extends DelayTimeWork {

    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
        GameRoom room = RoomManager.instance().getRoom(this.roomId);
        PlayerService playerCacheService = SpringContextUtil.getBean(PlayerService.class);
        synchronized (room) {
            if (room == null) {
                log.warn("this room is no exist!!");
                return;
            }
            if (room.isReady()) {
                return;
            }
            int readyCount = 0;
            Boolean allRobot = true;
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.isReady()) {
                    if (!(seat.getPlayer() instanceof Robot)) {
                        seat.getPlayer().setRoomId(this.roomId);
                        playerCacheService.update(seat.getPlayer());
                    }
                    readyCount++;
                }
                if (!(seat.getPlayer() instanceof Robot)) {
                    allRobot = false;
                }
            }
            if (allRobot || readyCount < 2) {
                RoomService roomService = SpringContextUtil.getBean(RoomService.class);
                roomService.solveRoom(room.getRoomId());
                return;
            }
            room.setRound(room.getRound() + 1);
            // 扣除底分
            if (!this.deductBaseScore(room)) {
                return;
            }
            room.setReady(true);
            IdGeneratorService idGeneratorService = SpringContextUtil.getBean(IdGeneratorService.class);
            room.setGameNumber(idGeneratorService.getGameNum());
            room.setLastBetChips(room.getBaseScore());
            room.setRoomStatus(Config.RoomStatus.GAME);
            Map<String, Object> tableInfo = new RoomServiceImpl().roomMsg(room, null);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(D.TABLE_INFO, tableInfo);
            MessageResult messageResult = new MessageResult(GAME_START_NTC, resultMap);
            PushService pushService = SpringContextUtil.getBean(PushService.class);
            pushService.broadcast(messageResult, room);
            RoomService roomService = SpringContextUtil.getBean(RoomService.class);
            roomService.distributePoker(room);
        }
    }

    /**
     * @param room
     * @return void
     * @description: 扣除底分
     * @author mice
     * @date 2019/7/13
     */
    private Boolean deductBaseScore(GameRoom room) {
        PlayerService playerCacheService = SpringContextUtil.getBean(PlayerService.class);
        log.info("牌桌{},总分为:{},底分为:{}", room.getRoomId(), room.getBaseScore(), room.getBaseScore());
        for (Seat seat : room.getSeatMap().values()) {
            if (seat == null || !seat.isReady()) {
                seat.setNoReady(seat.getNoReady() + 1);
                continue;
            }
            if (seat.getDecBaseScore() == BigDecimal.ZERO) {
                if (!(seat.getPlayer() instanceof Robot)) {
                    BetRecvDto betRecvDto = playerCacheService.decreaseGold(new BigDecimal(room.getBaseScore()).negate(), seat.getPlayer().getRoleId());
                    if (betRecvDto.getCode() != 0) {
                        seat.setReady(false);
                        ForceQuitRoomService forceQuitRoomService = SpringContextUtil.getBean(ForceQuitRoomServiceImpl.class);
                        forceQuitRoomService.forceQuitRoom(seat.getPlayer(), Config.LeaveReason.LEAVE_NOMONEY);
                        ReadyService readyService = SpringContextUtil.getBean(ReadyServiceImpl.class);
                        room.getSeatMap().remove(seat.getSeatId());
                        readyService.checkAllReady(room);
                        return false;
                    }
                }
                seat.setDecBaseScore(BigDecimal.valueOf(room.getBaseScore()));
                seat.getPlayer().setGold(seat.getPlayer().getGold() - room.getBaseScore());
                playerCacheService.update(seat.getPlayer());
                // 扎金花底分
                seat.setChipsRemain(seat.getChipsRemain() - room.getBaseScore());
                room.getBetList().add(room.getBaseScore());
                seat.setBetChips(room.getBaseScore());
                room.setTotalChips(room.getTotalChips() + room.getBaseScore());
            }
            seat.setState(Config.SeatState.STATE_PLAYING);
            seat.setPlayed(true);
        }
        return true;
    }

}
