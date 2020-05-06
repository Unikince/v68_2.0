package com.dmg.bairenlonghu.service.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.BetSendDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dmg.bairenlonghu.common.enums.BetTableIndexEnum;
import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.common.model.BaseRobot;
import com.dmg.bairenlonghu.common.result.MessageResult;
import com.dmg.bairenlonghu.common.result.ResultEnum;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.model.RoomStatus;
import com.dmg.bairenlonghu.model.Seat;
import com.dmg.bairenlonghu.model.Table;
import com.dmg.bairenlonghu.model.dto.BetResultNTCDTO;
import com.dmg.bairenlonghu.model.dto.CopyBetResultNTCDTO;
import com.dmg.bairenlonghu.service.PushService;
import com.dmg.bairenlonghu.service.cache.PlayerService;
import com.dmg.bairenlonghu.service.cache.RobotCacheService;
import com.dmg.bairenlonghu.service.logic.BetService;
import com.dmg.bairenlonghu.tcp.server.MessageIdConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 下注
 * @Author mice
 * @Date 2019/7/30 14:32
 * @Version V1.0
 **/
@Service
@Slf4j
public class BetServiceImpl implements BetService {
    @Autowired
    private PlayerService playerCacheService;
    @Autowired
    private RobotCacheService robotCacheService;
    @Autowired
    private PushService pushService;

    @Override
    public void playerBet(int userId, String betTableIndex, BigDecimal betChip) {
        BasePlayer basePlayer = this.playerCacheService.getPlayer(userId);
        int roomId = basePlayer.getRoomId();
        Room room = RoomManager.intance().getRoom(roomId);
        if (room == null) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        if (userId == room.getBanker().getPlayer().getUserId() || room.getRoomStatus() != RoomStatus.BET) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.PLAYER_HAS_ACTION_ERROR.getCode());
            return;
        }

        Seat seat = room.getSeatMap().get(basePlayer.getSeatIndex() + "");
        int totalBetChip = seat.getBetChipMap().values().stream().mapToInt(BigDecimal::intValue).sum();
        totalBetChip = totalBetChip + betChip.intValue();

        // 房间下注最低下限
        BigDecimal roomBetLowLimt = RoomManager.intance().getRoomBetLowLimit(room.getLevel());
        if (roomBetLowLimt.compareTo(basePlayer.getGold()) > 0) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.ROOM_BET_LOW_LIMIT.getCode());
            return;
        }
        Table betChipForTable = room.getTableMap().get(betTableIndex);

        BigDecimal tableBetTotal = seat.getBetChipMap().get(betTableIndex);
        BigDecimal betTotal = BigDecimal.ZERO;
        if (tableBetTotal == null) {
            betTotal = betChip;
        } else {
            betTotal = tableBetTotal.add(betChip);
        }
        // 区域下注下限
        long betLowLimit = RoomManager.intance().getBetLowLimit(room.getLevel());
        if (betTotal.compareTo(new BigDecimal(betLowLimit)) < 0) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.AREA_LOW_LIMIT.getCode());
            return;
        }

        // 区域下注上限
        long betUpLimit = RoomManager.intance().getBetUpLimit(room.getLevel());
        if (betTotal.compareTo(new BigDecimal(betUpLimit)) > 0) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.AREA_UP_LIMIT.getCode());
            return;
        }

        int maxMaxMutiple = RoomManager.intance().getMaxMutiple(room.getLevel());
        // 投注总额大于余额赔付 禁止下注
        if (basePlayer.getGold().compareTo(new BigDecimal(totalBetChip * maxMaxMutiple)) < 0) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.BETCHIP_TO_PLAYER_GOLD_MAX_LIMIT.getCode());
            return;
        }

        // 超过房间下注限制
        if (room.getBetChipLimit().compareTo(room.getCurRoundTotalBetChip().add(betChip)) < 0) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.BET_LIMIT.getCode());
            return;
        }

        // 扣除下注金额
        BetSendDto betSendDto = BetSendDto.builder()
                .userId(userId)
                .decGold(betChip.negate())
                .build();
        BetRecvDto decGoldRecvDto = playerCacheService.bet(betSendDto);
        synchronized (room) {
            room.setCurRoundTotalBetChip(room.getCurRoundTotalBetChip().add(betChip));
        }
        synchronized (betChipForTable) {
            betChipForTable.getBetChipList().add(betChip);
            betChipForTable.setPlayerBetChipTotal(betChipForTable.getPlayerBetChipTotal().add(betChip));
            betChipForTable.setBetChipTotal(betChipForTable.getBetChipTotal().add(betChip));
        }
        BigDecimal betChips = seat.getBetChipMap().get(betTableIndex);
        if (betChips == null) {
            betChips = betChip;
            seat.getBetChipRecordMap().put(betTableIndex, new ArrayList<>());
        } else {
            betChips = betChips.add(betChip);
        }
        seat.setReady(true);
        seat.getBetChipMap().put(betTableIndex, betChips);
        seat.getBetChipRecordMap().get(betTableIndex).add(betChip);

        seat.getPlayer().setGold(seat.getPlayer().getGold().subtract(betChip));
        basePlayer.setGold(basePlayer.getGold().subtract(betChip));
        this.playerCacheService.updatePlayer(basePlayer);

        RoomManager.intance().addSystemTurnover(betChip);

        BetResultNTCDTO betResultNTCDTO = new BetResultNTCDTO();
        betResultNTCDTO.setBetChip(betChip);
        betResultNTCDTO.setInfield(false);
        betResultNTCDTO.setBetTableIndex(betTableIndex);
        betResultNTCDTO.setBetChipTotal(betChipForTable.getBetChipTotal());
        betResultNTCDTO.setSeatIndex(seat.getSeatIndex() + "");
        betResultNTCDTO.setBetChipMap(seat.getBetChipMap());
        if (room.getInfieldSeatMap().containsKey(basePlayer.getSeatIndex() + "")) {
            betResultNTCDTO.setInfield(true);
        }
        betResultNTCDTO.setUserGold(basePlayer.getGold());
        MessageResult messageResult = new MessageResult(MessageIdConfig.BET_RESULT_NTC, betResultNTCDTO);
        this.pushService.broadcast(messageResult, room);
    }

    @Override
    public void copyBet(int userId) {
        BasePlayer basePlayer = this.playerCacheService.getPlayer(userId);
        int roomId = basePlayer.getRoomId();
        Room room = RoomManager.intance().getRoom(roomId);
        if (room == null) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        if (userId == room.getBanker().getPlayer().getUserId() || room.getRoomStatus() != RoomStatus.BET) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.PLAYER_HAS_ACTION_ERROR.getCode());
            return;
        }
        Seat seat = room.getSeatMap().get(basePlayer.getSeatIndex() + "");

        if (CollectionUtils.isEmpty(seat.getLastBetChipRecordMap())) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.NO_BET_RECORD.getCode());
            return;
        }
        int lastTotalBetChip = 0;
        Map<String, BigDecimal> betChipMap = new HashMap<>();
        for (String tableIndex : seat.getLastBetChipRecordMap().keySet()) {
            int betChip = seat.getLastBetChipRecordMap().get(tableIndex).stream().mapToInt(BigDecimal::intValue).sum();
            betChipMap.put(tableIndex, new BigDecimal(betChip));
            lastTotalBetChip = lastTotalBetChip + betChip;
        }

        int totalBetChip = seat.getBetChipMap().values().stream().mapToInt(BigDecimal::intValue).sum();
        totalBetChip = totalBetChip + lastTotalBetChip;

        int maxMaxMutiple = RoomManager.intance().getMaxMutiple(room.getLevel());
        // 投注总额大于余额/最大倍数 禁止下注
        if (basePlayer.getGold().compareTo(new BigDecimal(totalBetChip * maxMaxMutiple)) < 0) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.BET_LIMIT.getCode());
            return;
        }
        // 超过房间下注限制
        if (room.getBetChipLimit().compareTo(room.getCurRoundTotalBetChip().add(new BigDecimal(totalBetChip))) < 0) {
            this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.BET_LIMIT.getCode());
            return;
        }
        // 区域下注限制
        for (String tableId : betChipMap.keySet()) {
            BigDecimal tableBetTotal = seat.getBetChipMap().get(tableId);
            BigDecimal betTotal = BigDecimal.ZERO;
            if (tableBetTotal == null) {
                betTotal = betChipMap.get(tableId);
            } else {
                betTotal = tableBetTotal.add(betChipMap.get(tableId));
            }
            // 区域下注上限
            long betUpLimit = RoomManager.intance().getBetUpLimit(room.getLevel());
            if (betTotal.compareTo(new BigDecimal(betUpLimit)) > 0) {
                this.pushService.push(userId, MessageIdConfig.BET, ResultEnum.AREA_UP_LIMIT.getCode());
                return;
            }
        }
        synchronized (room) {
            room.setCurRoundTotalBetChip(room.getCurRoundTotalBetChip().add(new BigDecimal(totalBetChip)));
        }
        seat.getPlayer().setGold(seat.getPlayer().getGold().subtract(new BigDecimal(lastTotalBetChip)));
        basePlayer.setGold(basePlayer.getGold().subtract(new BigDecimal(lastTotalBetChip)));
        this.playerCacheService.updatePlayer(basePlayer);
        List<CopyBetResultNTCDTO> betResultNTCDTOs = new ArrayList<>();
        int betFailCount = 0;
        for (String betTableIndex : betChipMap.keySet()) {
            Table betChipForTable = room.getTableMap().get(betTableIndex);
            BigDecimal betChip = betChipMap.get(betTableIndex);
            // 扣除下注金额
            BetSendDto betSendDto = BetSendDto.builder()
                    .userId(userId)
                    .decGold(betChip.negate())
                    .build();
            BetRecvDto decGoldRecvDto = playerCacheService.bet(betSendDto);
            if (decGoldRecvDto.getCode()>0) {
                betFailCount++;
                continue;
            }
            synchronized (betChipForTable) {
                betChipForTable.getBetChipList().addAll(seat.getLastBetChipRecordMap().get(betTableIndex));
                betChipForTable.setPlayerBetChipTotal(betChipForTable.getPlayerBetChipTotal().add(betChip));
                betChipForTable.setBetChipTotal(betChipForTable.getBetChipTotal().add(betChip));
            }
            BigDecimal betChips = seat.getBetChipMap().get(betTableIndex);
            if (betChips == null) {
                betChips = betChip;
                seat.getBetChipRecordMap().put(betTableIndex, new ArrayList<>());
            } else {
                betChips = betChips.add(betChip);
            }
            seat.setReady(true);
            seat.getBetChipMap().put(betTableIndex, betChips);
            seat.getBetChipRecordMap().get(betTableIndex).addAll(seat.getLastBetChipRecordMap().get(betTableIndex));

            CopyBetResultNTCDTO betResultNTCDTO = new CopyBetResultNTCDTO();
            betResultNTCDTO.setBetChipList(seat.getLastBetChipRecordMap().get(betTableIndex));
            betResultNTCDTO.setInfield(false);
            betResultNTCDTO.setBetTableIndex(betTableIndex);
            betResultNTCDTO.setBetChipTotal(betChipForTable.getBetChipTotal());
            betResultNTCDTO.setSeatIndex(seat.getSeatIndex() + "");
            betResultNTCDTO.setBetChipMap(seat.getBetChipMap());
            if (room.getInfieldSeatMap().containsKey(basePlayer.getSeatIndex() + "")) {
                betResultNTCDTO.setInfield(true);
            }
            betResultNTCDTO.setUserGold(basePlayer.getGold());
            betResultNTCDTOs.add(betResultNTCDTO);
        }
        if (betFailCount==3){
            pushService.push(userId, MessageIdConfig.BET, ResultEnum.ROOM_BET_LOW_LIMIT.getCode());
            return;
        }

        RoomManager.intance().addSystemTurnover(new BigDecimal(lastTotalBetChip));
        MessageResult messageResult = new MessageResult(MessageIdConfig.COPY_BET_RESULT_NTC, betResultNTCDTOs);
        this.pushService.broadcast(messageResult, room);
    }

    @Override
    public void robotBet(int userId, String betTableIndex, BigDecimal betChip) {
        BaseRobot baseRobot = this.robotCacheService.getRobot(userId);
        int roomId = baseRobot.getRoomId();
        Room room = RoomManager.intance().getRoom(roomId);
        if (room == null) {
            return;
        }
        if (room.getRoomStatus() != RoomStatus.BET) {
            return;
        }
        Seat seat = room.getSeatMap().get(baseRobot.getSeatIndex() + "");
        int totalBetChip = seat.getBetChipMap().values().stream().mapToInt(BigDecimal::intValue).sum();
        totalBetChip = totalBetChip + betChip.intValue();

        Table betChipForTable = room.getTableMap().get(betTableIndex);
        BigDecimal tableBetTotal = seat.getBetChipMap().get(betTableIndex);
        BigDecimal betTotal = BigDecimal.ZERO;
        if (tableBetTotal == null) {
            betTotal = betChip;
        } else {
            betTotal = tableBetTotal.add(betChip);
        }
        // 区域下注下限
        long betLowLimit = RoomManager.intance().getBetLowLimit(room.getLevel());
        if (betTotal.compareTo(new BigDecimal(betLowLimit)) < 0) {
            return;
        }

        // 区域下注上限
        long betUpLimit = RoomManager.intance().getBetUpLimit(room.getLevel());
        if (betTotal.compareTo(new BigDecimal(betUpLimit)) > 0) {
            return;
        }

        int maxMaxMutiple = RoomManager.intance().getMaxMutiple(room.getLevel());
        // 投注总额大于余额赔付 禁止下注
        if (baseRobot.getGold().compareTo(new BigDecimal(totalBetChip * maxMaxMutiple)) < 0) {
            return;
        }

        // 超过房间下注限制
        if (room.getBetChipLimit().compareTo(room.getCurRoundTotalBetChip().add(betChip)) < 0) {
            return;
        }

        synchronized (betChipForTable) {
            betChipForTable.getBetChipList().add(betChip);
            betChipForTable.setRobotBetChipTotal(betChipForTable.getRobotBetChipTotal().add(betChip));
            betChipForTable.setBetChipTotal(betChipForTable.getBetChipTotal().add(betChip));
        }
        BigDecimal betChips = seat.getBetChipMap().get(betTableIndex);
        if (betChips == null) {
            betChips = betChip;
        } else {
            betChips = betChips.add(betChip);
        }
        seat.setReady(true);
        seat.getBetChipMap().put(betTableIndex, betChips);
        seat.getPlayer().setGold(seat.getPlayer().getGold().subtract(betChip));
        baseRobot.setGold(baseRobot.getGold().subtract(betChip));
        this.robotCacheService.update(baseRobot);
        BetResultNTCDTO betResultNTCDTO = new BetResultNTCDTO();
        betResultNTCDTO.setBetChip(betChip);
        betResultNTCDTO.setInfield(false);
        betResultNTCDTO.setBetTableIndex(betTableIndex);
        betResultNTCDTO.setBetChipTotal(betChipForTable.getBetChipTotal());

        betResultNTCDTO.setBetChipMap(seat.getBetChipMap());
        if (room.getInfieldSeatMap().containsKey(baseRobot.getSeatIndex() + "")) {
            betResultNTCDTO.setInfield(true);
            betResultNTCDTO.setSeatIndex(seat.getSeatIndex() + "");
            betResultNTCDTO.setUserGold(baseRobot.getGold());
        }
        MessageResult messageResult = new MessageResult(MessageIdConfig.BET_RESULT_NTC, betResultNTCDTO);
        this.pushService.broadcast(messageResult, room);
        // log.info("机器人:{},在房间:{},下注:{}",baseRobot.getNickname(),roomId,betChip);
    }

}