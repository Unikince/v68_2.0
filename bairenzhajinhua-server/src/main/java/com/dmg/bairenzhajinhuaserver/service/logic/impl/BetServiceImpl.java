package com.dmg.bairenzhajinhuaserver.service.logic.impl;

import cn.hutool.core.util.RandomUtil;
import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRobot;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.model.RoomStatus;
import com.dmg.bairenzhajinhuaserver.model.Seat;
import com.dmg.bairenzhajinhuaserver.model.Table;
import com.dmg.bairenzhajinhuaserver.model.constants.Combination;
import com.dmg.bairenzhajinhuaserver.model.dto.BetResultNTCDTO;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.service.cache.RobotCacheService;
import com.dmg.bairenzhajinhuaserver.service.logic.BetService;
import com.dmg.bairenzhajinhuaserver.state.impl.StopBetState;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.BetSendDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
        BasePlayer basePlayer = playerCacheService.getPlayer(userId);
        List<String> tableIndexs = new ArrayList<String>();
        Map<String,BigDecimal> betMap = new HashMap<>();
        int roomId = basePlayer.getRoomId();
        Room room = RoomManager.intance().getRoom(roomId);
        if (room == null){
        	basePlayer.push(MessageIdConfig.BET, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
		BigDecimal maxMaxMutiple = RoomManager.intance().getMultiple(room.getLevel(), Combination.LEOPARD.getValue());
        if (!(room.getState() instanceof StopBetState)){
        	basePlayer.push(MessageIdConfig.BET);
//        	basePlayer.push(MessageIdConfig.BET, ResultEnum.PLAYER_HAS_ACTION_ERROR.getCode());
            return;
        }
        if(RoomManager.intance().getBetLowLimit(room.getLevel()) > basePlayer.getGold().doubleValue()) {
            basePlayer.push(MessageIdConfig.BET, ResultEnum.MONEY_ENOUGH_CAN_BET.getCode());
        	return;
        }
        Seat seat;
        synchronized (room){
        	seat = room.getSeatMap().get(basePlayer.getSeatIndex()+"");
	        if(seat == null ) {
	        	log.info("player-->{},seat is null",basePlayer.getUserId());
	        	return;
	        }
	        if(room.getBanker().getPlayer().getUserId() == basePlayer.getUserId()) {
	//        	basePlayer.push(MessageIdConfig.BET, ResultEnum.PLAYER_HAS_ACTION_ERROR.getCode());
	        	basePlayer.push(MessageIdConfig.BET);
	            return;
	        }
	        if (copyBet){
	        	betMap = seat.getLastBetChipInfo();
	            if (betMap.isEmpty()){
	            	basePlayer.push(MessageIdConfig.BET, ResultEnum.NO_BET_RECORD.getCode());
	                return;
	            }

	            // 区域下注上限限制
				for (String tableId :betMap.keySet()){
                    int tableTotalBet = betMap.get(tableId).intValue();
				    if (seat.getUserBetChipTotal().get(tableId)!=null){
                        tableTotalBet = seat.getUserBetChipTotal().get(tableId).intValue()+betMap.get(tableId).intValue();
                    }
					if (tableTotalBet > RoomManager.intance().getBetUpLimit(room.getLevel())){
						basePlayer.push(MessageIdConfig.BET, ResultEnum.AREA_UP_LIMIT.getCode());
						return;
					}
				}
	            tableIndexs = new ArrayList<>(betMap.keySet());
	            betChip =new BigDecimal(betMap.values().stream().mapToInt(BigDecimal::intValue).sum());

	        } else {
				BigDecimal tableBetTotal = seat.getUserBetChipTotal().get(betTableIndex);
				BigDecimal betTotal = BigDecimal.ZERO;
				if (tableBetTotal ==null){
					betTotal = betChip;
				}else {
					betTotal = tableBetTotal.add(betChip);
				}
				// 区域下注下限
				long betLowLimit = RoomManager.intance().getBetLowLimit(room.getLevel());
				if (betTotal.compareTo(new BigDecimal(betLowLimit))<0){
					basePlayer.push(MessageIdConfig.BET, ResultEnum.AREA_LOW_LIMIT.getCode());
					return;
				}
				// 区域下注上限
				long betUpLimit = RoomManager.intance().getBetUpLimit(room.getLevel());
				if (betTotal.compareTo(new BigDecimal(betUpLimit))>0){
					basePlayer.push(MessageIdConfig.BET, ResultEnum.AREA_UP_LIMIT.getCode());
					return;
				}

	        	seat.setCanRenew(false);
	        	tableIndexs = Arrays.asList(betTableIndex.split(","));
	        	betMap.put(betTableIndex, betChip);
	        }
	        int totalBetChip = seat.getUserBetChipTotal().values().stream().mapToInt(BigDecimal::intValue).sum();
	        //剩余金额不足
	        if (basePlayer.getGold().compareTo((new BigDecimal(totalBetChip).add(betChip)).multiply(maxMaxMutiple))<0){
	        	basePlayer.push(MessageIdConfig.BET, ResultEnum.OVER_BET_LIMIT.getCode());
	            return;
	        }
	        if (!seat.isReady()){
	            seat.getUserBetChipTotal().clear();
	        }
	    	BigDecimal roomTotalBet = room.getCurRoundTotalBetChip();
	    	roomTotalBet = roomTotalBet == null ? betChip : roomTotalBet.add(betChip);
	    	//下注总额限制 依据当前台红/最大牌型倍数
	    	if(room.getRestCanBetChip().compareTo(betChip) < 0) {
	    		basePlayer.push(MessageIdConfig.BET, ResultEnum.OVER_ROOM_BET_TOTAL.getCode());
	    		return;
	    	}
        	room.setRestCanBetChip(room.getRestCanBetChip().subtract(betChip));
        	room.setCurRoundTotalBetChip(roomTotalBet);
        }
        // TODO 同步金币
    	BetResultNTCDTO betResultNTCDTO = new BetResultNTCDTO();
        int betFailCount = 0;
    	for(String index : tableIndexs) {
        	Table betChipForTable = room.getTableMap().get(index);
        	BigDecimal bet = betMap.get(index);
			// 扣除下注金额
			BetSendDto betSendDto = BetSendDto.builder()
					.userId(userId)
					.decGold(bet.negate())
					.build();
			BetRecvDto decGoldRecvDto = playerCacheService.bet(betSendDto);
			if (decGoldRecvDto.getCode()>0) {
				betFailCount++;
				continue;
			}
        	betChipForTable.setBetChipTotal(betChipForTable.getBetChipTotal().add(bet));
        	betChipForTable.getBetChipList().add(bet);
        	betChipForTable.setPlayerBetChipTotal(betChipForTable.getPlayerBetChipTotal().add(bet));
        	BigDecimal betChips = seat.getUserBetChipTotal().get(index);
        	if (betChips == null){
        		betChips = bet;
        	} else {
        		betChips = betChips.add(bet);
        	}
        	// 增加系统流水
			RoomManager.intance().addSystemTurnover(bet);
        	seat.setReady(true);
        	seat.getUserBetChipTotal().put(index,betChips);
			basePlayer.setGold(basePlayer.getGold().subtract(bet));
			seat.getPlayer().setGold(basePlayer.getGold());
			betResultNTCDTO.getAllBetChipTotal().put(index, betChipForTable.getBetChipTotal());
        }
    	if (betFailCount==tableIndexs.size()){
			basePlayer.push(MessageIdConfig.BET, ResultEnum.OVER_BET_LIMIT.getCode());
			return;
		}
    	if (!room.isHasPlayerBet()){
    		room.setHasPlayerBet(true);
		}
    	betResultNTCDTO.setInfield(false);
    	if (room.getInfieldSeatMap().containsKey(basePlayer.getSeatIndex()+"")){
    		betResultNTCDTO.setInfield(true);
    	}
    	betResultNTCDTO.setUserGold(basePlayer.getGold());
    	betResultNTCDTO.setSeatIndex(seat.getSeatIndex() + "");
    	betResultNTCDTO.setCanRenew(seat.isCanRenew());
    	betResultNTCDTO.setBetChipMap(betMap);
    	betResultNTCDTO.setUserBetChipTotal(seat.getUserBetChipTotal());
        playerCacheService.updatePlayer(basePlayer);
        
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
        if (room.getRoomStatus() != RoomStatus.BET)return;
        List<Integer> list = RoomManager.intance().getBetChipsMap().get(room.getLevel());
        synchronized (room){
	        for (Seat seat : room.getSeatMap().values()){
	            if (!(seat.getPlayer() instanceof BaseRobot)) continue;
	            if (RandomUtil.randomInt(100) > 50) continue;
	            BigDecimal betChip = new BigDecimal(list.get(RandomUtil.randomInt(0,3)));
	            if (room.getLevel() == 1){
	                if (RandomUtil.randomInt(100) > 85){
	                    betChip = new BigDecimal(list.get(3));
	                }
	            }
	            this.robotBet(seat.getPlayer().getUserId(),RandomUtil.randomInt(2,6)+"",betChip);
	        }
        }
    }

    @Override
    public void robotBet(int userId, String betTableIndex, BigDecimal betChip) {
        BaseRobot baseRobot = robotCacheService.getRobot(userId);
        int roomId = baseRobot.getRoomId();
        List<String> tableIndexs = new ArrayList<String>();
        Map<String,BigDecimal> betMap = new HashMap<>();
        tableIndexs = Arrays.asList(betTableIndex.split(","));
    	betMap.put(betTableIndex, betChip);
        Room room = RoomManager.intance().getRoom(roomId);
        if (room == null){
            return;
        }
        if (room.getRoomStatus()!= RoomStatus.BET){
            return;
        }
        Seat seat = room.getSeatMap().get(baseRobot.getSeatIndex() + "");
        int totalBetChip = seat.getUserBetChipTotal().values().stream().mapToInt(BigDecimal::intValue).sum();
        //剩余金额不足
        if (baseRobot.getGold().compareTo((new BigDecimal(totalBetChip).add(betChip)).multiply(RoomManager.intance().getMultiple(room.getLevel(), Combination.LEOPARD.getValue())))<0){
            return;
        }
		BigDecimal tableBetTotal = seat.getUserBetChipTotal().get(betTableIndex);
		BigDecimal betTotal = BigDecimal.ZERO;
		if (tableBetTotal ==null){
			betTotal = betChip;
		}else {
			betTotal = tableBetTotal.add(betChip);
		}
		// 区域下注下限
		long betLowLimit = RoomManager.intance().getBetLowLimit(room.getLevel());
		if (betTotal.compareTo(new BigDecimal(betLowLimit))<0){
			return;
		}
		// 区域下注上限
		long betUpLimit = RoomManager.intance().getBetUpLimit(room.getLevel());
		if (betTotal.compareTo(new BigDecimal(betUpLimit))>0){
			return;
		}

    	BigDecimal roomTotalBet = room.getCurRoundTotalBetChip();
    	roomTotalBet = roomTotalBet == null ? betChip : roomTotalBet.add(betChip);
    	if(room.getRestCanBetChip().compareTo(roomTotalBet) < 0) {
    		return;
    	}


    	room.setRestCanBetChip(room.getRestCanBetChip().subtract(betChip));
    	room.setCurRoundTotalBetChip(roomTotalBet);
        // TODO 同步金币
    	BetResultNTCDTO betResultNTCDTO = new BetResultNTCDTO();
    	for(String index : tableIndexs) {
        	Table betChipForTable = room.getTableMap().get(index);
        	BigDecimal bet = betMap.get(index);
        	betChipForTable.setBetChipTotal(betChipForTable.getBetChipTotal().add(bet));
        	betChipForTable.getBetChipList().add(bet);
        	betChipForTable.setRobotBetChipTotal(betChipForTable.getRobotBetChipTotal().add(betChip));
        	BigDecimal betChips = seat.getUserBetChipTotal().get(index);
        	if (betChips == null){
        		betChips = bet;
        	} else {
        		betChips = betChips.add(bet);
        	}
        	seat.setReady(true);
        	seat.getUserBetChipTotal().put(betTableIndex,betChips);
        	seat.getPlayer().setGold(seat.getPlayer().getGold().subtract(bet));
        	baseRobot.setGold(baseRobot.getGold().subtract(bet));
        	betResultNTCDTO.getAllBetChipTotal().put(index, betChipForTable.getBetChipTotal());
        }
    	betResultNTCDTO.setInfield(false);
    	betResultNTCDTO.setSeatIndex(seat.getSeatIndex()+"");
    	if (room.getInfieldSeatMap().containsKey(baseRobot.getSeatIndex()+"")){
    		betResultNTCDTO.setInfield(true);
    	}
    	betResultNTCDTO.setUserGold(baseRobot.getGold());
    	betResultNTCDTO.setBetChipMap(betMap);
    	betResultNTCDTO.setUserBetChipTotal(seat.getUserBetChipTotal());
    	

        if(seat.getSeatIndex()==2&&room.getGodOfGamblersFirstBetTable()==null){
            room.setGodOfGamblersFirstBetTable(betTableIndex);
        }
        
        robotCacheService.update(baseRobot);
        MessageResult messageResult = new MessageResult(MessageIdConfig.BET_RESULT_NTC, betResultNTCDTO);
        room.broadcast(messageResult);
//        log.info("机器人:{},在房间:{},下注:{}",baseRobot.getNickname(),roomId,betChip);
    }

}