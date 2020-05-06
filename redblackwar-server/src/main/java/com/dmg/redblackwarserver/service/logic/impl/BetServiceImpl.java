package com.dmg.redblackwarserver.service.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dmg.redblackwarserver.model.constants.D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.redblackwarserver.common.model.BasePlayer;
import com.dmg.redblackwarserver.common.model.BaseRobot;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.common.result.ResultEnum;
import com.dmg.redblackwarserver.manager.RoomManager;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.RoomStatus;
import com.dmg.redblackwarserver.model.Seat;
import com.dmg.redblackwarserver.model.Table;
import com.dmg.redblackwarserver.model.dto.BetResultNTCDTO;
import com.dmg.redblackwarserver.service.cache.PlayerService;
import com.dmg.redblackwarserver.service.cache.RobotCacheService;
import com.dmg.redblackwarserver.service.logic.BetService;
import com.dmg.redblackwarserver.state.impl.StopBetState;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;

import cn.hutool.core.util.RandomUtil;
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

    @Override
    public void playerBet(int userId, boolean copyBet, String betTableIndex, BigDecimal betChip) {
        BasePlayer basePlayer = this.playerCacheService.getPlayer(userId);
        List<String> tableIndexs = new ArrayList<>();
        Map<String, BigDecimal> betMap = new HashMap<>();
        int roomId = basePlayer.getRoomId();
        Room room = RoomManager.intance().getRoom(roomId);
        if (room == null) {
            basePlayer.push(MessageIdConfig.BET, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        if (!(room.getState() instanceof StopBetState)) {
            basePlayer.push(MessageIdConfig.BET);
            return;
        }
        if (RoomManager.intance().getGoldLimitMap().get(room.getLevel() + "") > basePlayer.getGold().doubleValue()) {
            basePlayer.push(MessageIdConfig.BET, ResultEnum.MONEY_ENOUGH_CAN_BET.getCode());
            return;
        }
        Seat seat;
        BigDecimal betTotal = BigDecimal.ZERO;
        synchronized (room) {
            seat = room.getSeatMap().get(basePlayer.getSeatIndex() + "");
            if (seat == null) {
                log.info("player-->{},seat is null", basePlayer.getUserId());
                return;
            }

            BigDecimal maxMutiply = new BigDecimal(RoomManager.intance().getMaxMutiple(room.getLevel()));
            if (copyBet) {
                betMap = seat.getLastBetChipInfo();
                if (betMap.isEmpty()) {
                    basePlayer.push(MessageIdConfig.BET, ResultEnum.NO_BET_RECORD.getCode());
                    return;
                }
                tableIndexs = new ArrayList<>(betMap.keySet());
                for (String tableIndex : betMap.keySet()){
                    if (tableIndex.equals(D.THREE)){
                        // 幸运区*最大倍数
                        betTotal = betTotal.add(betMap.get(tableIndex).multiply(maxMutiply));
                    }else {
                        betTotal = betTotal.add(betMap.get(tableIndex));
                    }
                }
                betChip = new BigDecimal(betMap.values().stream().mapToInt(BigDecimal::intValue).sum());
            } else {
                if (Integer.parseInt(betTableIndex) > 3) {
                    basePlayer.push(MessageIdConfig.BET, ResultEnum.PLAYER_HAS_ACTION_ERROR.getCode());
                    return;
                }
                seat.setCanRenew(false);
                tableIndexs = Arrays.asList(betTableIndex.split(","));
                betMap.put(betTableIndex, betChip);
                if (betTableIndex.equals(D.THREE)){
                    // 幸运区*最大倍数
                    betTotal = betTotal.add(betChip.multiply(maxMutiply));
                }else {
                    betTotal = betTotal.add(betChip);
                }
            }
            // 剩余金额不足
            if (basePlayer.getGold().compareTo(betChip) < 0) {
                basePlayer.push(MessageIdConfig.BET, ResultEnum.PLAYER_HAS_NO_MONEY.getCode());
                return;
            }
            if (!seat.isReady()) {
                seat.getUserBetChipTotal().clear();
            }
            BigDecimal roomTotalBet = room.getCurRoundTotalBetChip();
            roomTotalBet = roomTotalBet == null ? betChip : roomTotalBet.add(betTotal);
            // 下注总额限制 依据当前台红
            if (roomTotalBet.compareTo(room.getTaiHong())>0){
                basePlayer.push(MessageIdConfig.BET, ResultEnum.OVER_ROOM_BET_TOTAL.getCode());
            }
            /*for (Entry<String, BigDecimal> entry : room.getRestCanBetChip().entrySet()) {
                if (betMap.get(entry.getKey() ) != null) {
                    if (betMap.get(entry.getKey()).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(entry.getValue()) > 0 ) {
                        basePlayer.push(MessageIdConfig.BET, ResultEnum.OVER_ROOM_BET_TOTAL.getCode());
                        return;
                    }
                }
            }*/
            // 区域下注限制
            for (Entry<String, BigDecimal> entry : betMap.entrySet()) {
                if (seat.getUserBetChipTotal().get(entry.getKey()) != null) {
                    if (seat.getUserBetChipTotal().get(entry.getKey()).add(entry.getValue()).compareTo(room.getAreBetLimitMap().get(entry.getKey())) > 0) {
                        basePlayer.push(MessageIdConfig.BET, ResultEnum.OVER_SINGLE_BET_TOTAL.getCode());
                        return;
                    }
                } else {
                    BigDecimal betChipOne = entry.getValue();
                    if (betChipOne == null) {
                        continue;
                    }
                    if (betChipOne.compareTo(room.getAreBetLimitMap().get(entry.getKey())) > 0) {
                        basePlayer.push(MessageIdConfig.BET, ResultEnum.OVER_SINGLE_BET_TOTAL.getCode());
                        return;
                    }
                }
            }
            room.setCurRoundTotalBetChip(roomTotalBet);
        }
        this.playerCacheService.bet(userId, betChip.negate());

        // TODO 同步金币
        BigDecimal last = room.getRestCanBetChip();
        room.setRestCanBetChip(last.subtract(betTotal));
        BetResultNTCDTO betResultNTCDTO = new BetResultNTCDTO();
        for (String index : tableIndexs) {
            Table betChipForTable = room.getTableMap().get(index);
            BigDecimal bet = betMap.get(index);
            betChipForTable.setBetChipTotal(betChipForTable.getBetChipTotal().add(bet));
            betChipForTable.getBetChipList().add(bet);
            betChipForTable.setPlayerBetChipTotal(betChipForTable.getPlayerBetChipTotal().add(bet));
            BigDecimal betChips = seat.getUserBetChipTotal().get(index);
            if (betChips == null) {
                betChips = bet;
            } else {
                betChips = betChips.add(bet);
            }
            seat.setReady(true);
            seat.getUserBetChipTotal().put(index, betChips);
            seat.getPlayer().setGold(seat.getPlayer().getGold().subtract(bet));
            basePlayer.setGold(basePlayer.getGold().subtract(bet));
            betResultNTCDTO.getAllBetChipTotal().put(index, betChipForTable.getBetChipTotal());
        }
        betResultNTCDTO.setInfield(false);
        if (room.getInfieldSeatMap().containsKey(basePlayer.getSeatIndex() + "")) {
            betResultNTCDTO.setInfield(true);
        }
        betResultNTCDTO.setUserGold(basePlayer.getGold());
        betResultNTCDTO.setSeatIndex(seat.getSeatIndex() + "");
        betResultNTCDTO.setCanRenew(seat.isCanRenew());
        betResultNTCDTO.setBetChipMap(betMap);
        betResultNTCDTO.setUserBetChipTotal(seat.getUserBetChipTotal());
        this.playerCacheService.updatePlayer(basePlayer);
        
        if(seat.getSeatIndex()==2&&room.getGodOfGamblersFirstBetTable()==null){
            if(copyBet){
                String tBetTableIndex=null;
                BigDecimal tBetChip=null;
                for (String tBetTableIndex2 : betMap.keySet()) {
                    BigDecimal betChip2 = betMap.get(tBetTableIndex2);
                    if(tBetTableIndex==null){
                        tBetTableIndex=tBetTableIndex2;
                        tBetChip=betChip2;
                    }else{
                        if(tBetChip.compareTo(betChip2)>0){
                            tBetTableIndex=tBetTableIndex2;
                            tBetChip=betChip2;
                        }
                    }
                }
                room.setGodOfGamblersFirstBetTable(tBetTableIndex);
            }else{
                room.setGodOfGamblersFirstBetTable(betTableIndex);
            }
        }
        
        MessageResult messageResult = new MessageResult(MessageIdConfig.BET_RESULT_NTC, betResultNTCDTO);
        room.broadcast(messageResult);
    }

    @Override
    public void robotRandomBet(int roomId) {
        Room room = RoomManager.intance().getRoom(roomId);
        if (room.getRoomStatus() != RoomStatus.BET) {
            return;
        }
        List<Integer> list = RoomManager.intance().getBetChipsMap().get(room.getLevel());
        synchronized (room) {
            for (Seat seat : room.getSeatMap().values()) {
                if (!(seat.getPlayer() instanceof BaseRobot)) {
                    continue;
                }
                if (RandomUtil.randomInt(100) > 50) {
                    continue;
                }
                BigDecimal betChip = new BigDecimal(list.get(RandomUtil.randomInt(0, 3)));
                if (room.getLevel() == 1) {
                    if (RandomUtil.randomInt(100) > 85) {
                        betChip = new BigDecimal(list.get(3));
                    }
                }
                if (RandomUtil.randomInt(100) <= 70) {
                    this.robotBet(seat.getPlayer().getUserId(), RandomUtil.randomInt(1, 3) + "", betChip);
                } else {
                    this.robotBet(seat.getPlayer().getUserId(), RandomUtil.randomInt(1, 4) + "", betChip);
                }
            }
        }
    }

    @Override
    public void robotBet(int userId, String betTableIndex, BigDecimal betChip) {
        BaseRobot baseRobot = this.robotCacheService.getRobot(userId);
        int roomId = baseRobot.getRoomId();
        List<String> tableIndexs = new ArrayList<>();
        Map<String, BigDecimal> betMap = new HashMap<>();
        tableIndexs = Arrays.asList(betTableIndex.split(","));
        betMap.put(betTableIndex, betChip);
        Room room = RoomManager.intance().getRoom(roomId);
        if (room == null) {
            return;
        }
        if (!(room.getState() instanceof StopBetState)) {
            return;
        }


        Seat seat = room.getSeatMap().get(baseRobot.getSeatIndex() + "");
//        int totalBetChip = seat.getUserBetChipTotal().values().stream().mapToInt(BigDecimal::intValue).sum();
        // 剩余金额不足
        if (baseRobot.getGold().compareTo(betChip) < 0) {
            return;
        }
        BigDecimal maxMutiply = new BigDecimal(RoomManager.intance().getMaxMutiple(room.getLevel()));

        BigDecimal betTotal = BigDecimal.ZERO;
        if (betTableIndex.equals(D.THREE)){
            // 幸运区*最大倍数
            betTotal = betTotal.add(betChip.multiply(maxMutiply));
        }else {
            betTotal = betTotal.add(betChip);
        }
        BigDecimal roomTotalBet = room.getCurRoundTotalBetChip();
        roomTotalBet = roomTotalBet == null ? betChip : roomTotalBet.add(betTotal);
        // 下注总额限制 依据当前台红/最大牌型倍数
        if (roomTotalBet.compareTo(room.getTaiHong())>0)return;

        for (Entry<String, BigDecimal> entry : betMap.entrySet()) {
            if (seat.getUserBetChipTotal().get(entry.getKey()) != null) {
                if (seat.getUserBetChipTotal().get(entry.getKey()).add(entry.getValue()).compareTo(room.getAreBetLimitMap().get(entry.getKey())) > 0) {
                    return;
                }
            } else {
                if (betChip.compareTo(room.getAreBetLimitMap().get(entry.getKey())) > 0) {
                    return;
                }
            }
        }
        room.setCurRoundTotalBetChip(roomTotalBet);
        // TODO 同步金币
        BetResultNTCDTO betResultNTCDTO = new BetResultNTCDTO();
        BigDecimal last = room.getRestCanBetChip();
        room.setRestCanBetChip(last.subtract(betTotal));
        for (String index : tableIndexs) {
            Table betChipForTable = room.getTableMap().get(index);
            BigDecimal bet = betMap.get(index);
            betChipForTable.setBetChipTotal(betChipForTable.getBetChipTotal().add(bet));
            betChipForTable.getBetChipList().add(bet);
            betChipForTable.setRobotBetChipTotal(betChipForTable.getPlayerBetChipTotal().add(bet));
            BigDecimal betChips = seat.getUserBetChipTotal().get(index);
            if (betChips == null) {
                betChips = bet;
            } else {
                betChips = betChips.add(bet);
            }
            seat.setReady(true);
            seat.getUserBetChipTotal().put(betTableIndex, betChips);
            seat.getPlayer().setGold(seat.getPlayer().getGold().subtract(bet));
            baseRobot.setGold(baseRobot.getGold().subtract(bet));
            betResultNTCDTO.getAllBetChipTotal().put(index, betChipForTable.getBetChipTotal());
        }
        betResultNTCDTO.setInfield(false);
        betResultNTCDTO.setSeatIndex(seat.getSeatIndex() + "");
        if (room.getInfieldSeatMap().containsKey(baseRobot.getSeatIndex() + "")) {
            betResultNTCDTO.setInfield(true);
        }
        betResultNTCDTO.setUserGold(baseRobot.getGold());
        betResultNTCDTO.setBetChipMap(betMap);
        betResultNTCDTO.setUserBetChipTotal(seat.getUserBetChipTotal());
        
        if(seat.getSeatIndex()==2&&room.getGodOfGamblersFirstBetTable()==null){
            room.setGodOfGamblersFirstBetTable(betTableIndex);
        }
        
        this.robotCacheService.update(baseRobot);
        MessageResult messageResult = new MessageResult(MessageIdConfig.BET_RESULT_NTC, betResultNTCDTO);
        room.broadcast(messageResult);
//        log.info("机器人:{},在房间:{},下注:{}",baseRobot.getNickname(),roomId,betChip);
    }

}