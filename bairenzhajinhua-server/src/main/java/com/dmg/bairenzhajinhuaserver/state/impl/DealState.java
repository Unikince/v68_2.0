package com.dmg.bairenzhajinhuaserver.state.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dmg.bairenzhajinhuaserver.model.dto.PokerInfo;
import com.dmg.common.core.util.SpringUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenzhajinhuaserver.common.exception.BusinessException;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRoom;
import com.dmg.bairenzhajinhuaserver.common.model.Poker;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.manager.TimerManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.model.RoomStatus;
import com.dmg.bairenzhajinhuaserver.model.Table;
import com.dmg.bairenzhajinhuaserver.model.constants.Combination;
import com.dmg.bairenzhajinhuaserver.model.constants.D;
import com.dmg.bairenzhajinhuaserver.model.dto.DealPokerDTO;
import com.dmg.bairenzhajinhuaserver.model.dto.HandPokerDTO;
import com.dmg.bairenzhajinhuaserver.quarz.RoomActionDelayTask;
import com.dmg.bairenzhajinhuaserver.state.RoomState;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 发牌阶段
 * @author Administrator
 *
 */
@Slf4j
public class DealState implements RoomState {
	private BaseRoom room;
	
	public DealState(BaseRoom room) {
		this.room = room;
	}
	
	@Override
	public State getState() {
		return State.DEAL;
	}

	@Override
	public void action() {
		Room room = (Room) this.room;
        if (room == null){
            throw new BusinessException(ResultEnum.ROOM_NO_EXIST.getCode()+"",ResultEnum.ROOM_NO_EXIST.getMsg());
        }
        room.setRoomStatus(RoomStatus.DEAL);
		Double controlExecuteRate = RoomManager.intance().getControlExecuteRate(room.getLevel());
		// 是否控制
		boolean control = false;
		// false输 true赢
		boolean win = false;
		int random = RandomUtil.randomInt(100);
		// controlExecuteRate>0控制赢 <0控制输
		if (controlExecuteRate>=0){
			win = true;
			if (random<=controlExecuteRate){
				control = true;
			}
		}else {
			if (random<=Math.abs(controlExecuteRate)){
				control = true;
			}
		}

		if (control && (room.isHasPlayerBet() || !room.isSystemBanker())){
			this.dealAlgorithm(win);
		}else {
			this.dealNormal(room);
		}
		StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
		redisTemplate.opsForList().rightPush("bairenzhajinhua_control:"+RoomManager.intance().getGameId()+"_"+room.getLevel(),control+"");
		log.info("场次:{}, 控制率为:{},随机数为:{},当局是否控制:{},控制输赢为:{}",room.getLevel(),controlExecuteRate,random,control,win);
        DealPokerDTO dto = new DealPokerDTO();
        dto.setStartIndex(RandomUtil.randomInt(2, 6));
        BeanUtils.copyProperties(room,dto);
        MessageResult messageResult = new MessageResult(MessageIdConfig.DEAL_POKER_NTC,dto);
        room.broadcast(messageResult);
        try {
            room.setCountdownTime(System.currentTimeMillis() + D.DEAL_TIME);
            room.setNextcountdownTime(room.getCountdownTime() + D.SETTLE_TIME);
            TimerManager.instance().submitDelayWork(RoomActionDelayTask.class, D.DEAL_TIME, room.getRoomId());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        room.changeState();
	}

	/**
	 * @param room
	 * @return void
	 * @description: 正常发牌
	 * @author mice
	 * @date 2019/7/31
	 */
	void dealNormal(Room room) {
		log.info("==>房间:{},开始发牌", room.getRoomId());
		// 洗牌
		Collections.shuffle(room.getPokerList());
		LinkedList<Poker> tablePokerList = room.getPokerList();
		int pokerIndex = 0;
		for (Table table : room.getTableMap().values()) {
			List<Poker> pokers = new ArrayList<>();
			for (int j = 1; j < 4; j++) {
				pokers.add(tablePokerList.get(pokerIndex++));
			}
			table.setPokerList(pokers);
			table.getPokerType();
		}
		boolean outMaxPayout = this.normalVirtualComputing(room);
		if (outMaxPayout){
			log.warn("==> 房间:{},超过最大赔付限额,重新发牌",room.getRoomId());
			this.dealNormal(room);
		}
	}
	
	/**
     * @return void
     * @description: 算法发牌
     * @author mice
     * @date 2019/7/31
     */
    private void dealAlgorithm(boolean win) {
    	Room room = (Room) this.room;
        log.info("==>房间:{},开始发牌", room.getRoomId());
        long start = System.currentTimeMillis();
        LinkedHashMap<Integer, HandPokerDTO> handPokerDTOMap = new LinkedHashMap<>();
        RoomManager manager =  RoomManager.intance();
        LinkedList<Poker> tablePokerList = room.getPokerList();
        Collections.shuffle(tablePokerList);
        int pokerIndex = 0;
        List<Poker> pokers = null;
        int cardType = 0;
        for (int i = 1; i < 6; i++) {
            pokers=new ArrayList<>();
            for (int j = 1; j < 4; j++) {
                pokers.add(tablePokerList.get(pokerIndex++));
            }
            HandPokerDTO handPokerDTO = new HandPokerDTO(cardType, pokers);
            handPokerDTOMap.put(i, handPokerDTO);
        }

        int winCount = room.getWinMap().get(win)==null ? 0:room.getWinMap().get(win);
        room.getWinMap().put(win,winCount+1);
        log.info("==> 房间:{},当局输赢判定为:{},总输赢场次为:{}",room.getRoomId(),win,room.getWinMap());
		// 能赢(输)的牌型 key:全排列key value:PokerInfo
		Map<Integer, PokerInfo> winGoldMap = new HashMap<>();
        Map<Integer,List<Integer>> arrangedMap = manager.getFullyArrangedMap();
        for (Integer key : arrangedMap.keySet()) {
            List<Integer> arrangedList = arrangedMap.get(key);
            int index = 0;
            for (Table table : room.getTableMap().values()) {
                table.setPokerList(handPokerDTOMap.get(arrangedList.get(index++)).getPokerList());
                table.getPokerType();
            }
            this.algVirtualComputing(key, winGoldMap, win);
        }
        List<Integer> arrangedList;
        if (winGoldMap.size() == 0) {
            arrangedList = manager.getFullyArrangedMap().get(RandomUtil.randomInt(1, manager.getFullyArrangedMap().size()));
        } else {
			// 是否闪避通杀或者通赔
			boolean allWinOrLose = RoomManager.intance().isAllWinOrLose(room.getLevel(),win);
			if (allWinOrLose){
				winGoldMap = this.removeAllWinOrLose(winGoldMap);
			}
            Object[] winGoldKeys = winGoldMap.keySet().toArray();
            int index = RandomUtil.randomInt(0, winGoldKeys.length);
            arrangedList = manager.getFullyArrangedMap().get(winGoldKeys[index]);
			if (!win){
				// 如果 系统输钱 超过底线 则判定当局系统必赢 重新计算
				if (winGoldMap.get(winGoldKeys[index]).getGold().intValue()+RoomManager.intance().getMaxPayout(room.getLevel())<=0
						&& RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() < RoomManager.intance().getMaxPayoutReferenceValue(room.getLevel())){
					log.warn("==> 房间{} :系统输钱 超过底线 判定当局系统必赢 重新计算",room.getRoomId());
					this.dealAlgorithm(true);
					return;
				}
			}
        }
        log.info("计算出的牌型组合为:{}", JSONObject.toJSONString(arrangedList));
        // 最后放入计算好的牌型
        int index = 0;
        for (Table table : room.getTableMap().values()) {
            table.setPokerList(handPokerDTOMap.get(arrangedList.get(index++)).getPokerList());
            table.getPokerType();
        }
        log.info("房间{},发牌耗时:{}", room.getRoomId(), System.currentTimeMillis() - start);
    }

    /**
     * @param key
     * @param winGoldMap
     * @param win
     * @return void
     * @description: 虚拟计算系统赢钱的组合
     * @author mice
     * @date 2019/8/5
     */
    private void algVirtualComputing(int key, Map<Integer, PokerInfo> winGoldMap, boolean win) {
    	Room room = (Room) this.room;
    	BigDecimal sysWinGold = new BigDecimal(0);
    	Table bankerTable = room.getTableMap().get(D.ONE);
        int bankerCardType = bankerTable.getCardType();
		// 统计通杀通赔 key: true:赢 false:输
		Map<Boolean,Integer> allWinOrLoseMap = new HashMap<>();
		allWinOrLoseMap.put(true,0);
		allWinOrLoseMap.put(false,0);
        for (Table table : room.getTableMap().values()) {
            if (table.getTableIndex().equals(D.ONE)) continue;
            BigDecimal multiple;
            // 庄家赢
            if (judgeWinner(bankerTable, table)) {
				allWinOrLoseMap.put(true,allWinOrLoseMap.get(true)+1);
            	multiple = RoomManager.intance().getMultiple(room.getLevel(), bankerCardType);
            	if(room.isSystemBanker()) {
            		sysWinGold = sysWinGold.add(table.getPlayerBetChipTotal().multiply(multiple));
            	} else {
            		sysWinGold = sysWinGold.subtract(table.getRobotBetChipTotal().multiply(multiple));
            	}
            } else {
				allWinOrLoseMap.put(false,allWinOrLoseMap.get(false)+1);
            	multiple = RoomManager.intance().getMultiple(room.getLevel(), table.getCardType());
            	if(room.isSystemBanker()) {
            		sysWinGold = sysWinGold.subtract(table.getPlayerBetChipTotal().multiply(multiple));
            	} else {
            		sysWinGold = sysWinGold.add(table.getRobotBetChipTotal().multiply(multiple));
            	}
            }
        }
        if ((win && sysWinGold.compareTo(BigDecimal.ZERO) > 0) 
        		|| (!win && sysWinGold.compareTo(BigDecimal.ZERO) < 0)) {
			if (allWinOrLoseMap.get(win)==4){
				winGoldMap.put(key, new PokerInfo(sysWinGold,true));
			}else {
				winGoldMap.put(key, new PokerInfo(sysWinGold,false));
			}
        }
    }

	/**
	 * @param room
	 * @return void
	 * @description: 正常发牌计算
	 * @author mice
	 * @date 2019/8/5
	 */
	private boolean normalVirtualComputing(Room room) {
		BigDecimal sysWinGold = new BigDecimal(0);
		Table bankerTable = room.getTableMap().get(D.ONE);
		int bankerCardType = bankerTable.getCardType();
		for (Table table : room.getTableMap().values()) {
			if (table.getTableIndex().equals(D.ONE)) continue;
			BigDecimal multiple;
			// 庄家赢
			if (judgeWinner(bankerTable, table)) {
				multiple = RoomManager.intance().getMultiple(room.getLevel(), bankerCardType);
				if(room.isSystemBanker()) {
					sysWinGold = sysWinGold.add(table.getPlayerBetChipTotal().multiply(multiple));
				} else {
					sysWinGold = sysWinGold.subtract(table.getRobotBetChipTotal().multiply(multiple));
				}
			} else {
				multiple = RoomManager.intance().getMultiple(room.getLevel(), table.getCardType());
				if(room.isSystemBanker()) {
					sysWinGold = sysWinGold.subtract(table.getPlayerBetChipTotal().multiply(multiple));
				} else {
					sysWinGold = sysWinGold.add(table.getRobotBetChipTotal().multiply(multiple));
				}
			}
		}
		if (RoomManager.intance().getMaxPayout(room.getLevel())+sysWinGold.intValue()<=0 && RoomManager.intance().getRoomWinGold(room.getLevel()).intValue()< RoomManager.intance().getMaxPayoutReferenceValue(room.getLevel())){
			return true;
		}
		return false;
	}

	/**
	 * @description: 移除通杀 通赔情况 若全是通杀则不作处理
	 * @param winGoldMap
	 * @return void
	 * @author mice
	 * @date 2019/9/28
	 */
	private Map<Integer, PokerInfo> removeAllWinOrLose(Map<Integer, PokerInfo> winGoldMap){
		Map<Integer, PokerInfo> finalMap = new HashMap<>();
		for (Integer key : winGoldMap.keySet()){
			if (!winGoldMap.get(key).isAllWinOrLose()){
				finalMap.put(key,winGoldMap.get(key));
			}
		}
		if (finalMap.size()>0){
			return finalMap;
		}
		return winGoldMap;
	}

    /**
     * @param bankerTable 庄家牌
     * @param table
     * @return boolean
     * @description: 判断PK的玩家谁输谁赢
     * @author mice
     * @date 2019/7/31
     */
    private boolean judgeWinner(Table bankerTable, Table table) {
        List<Poker> bankerPokerList = new ArrayList<>();
        bankerPokerList.addAll(bankerTable.getPokerList());
        Collections.sort(bankerPokerList);
        List<Poker> tablePokerList = new ArrayList<>();
        tablePokerList.addAll(table.getPokerList());
        Collections.sort(tablePokerList);
        if (bankerTable.getCardType() != 0 && table.getCardType() != 6 || bankerTable.getCardType() != 6 && table.getCardType() != 0) {
            //庄家赢
            if (bankerTable.getCardType() > table.getCardType()) {
                return true;
                //牌型相同时进行比较
            } else if (bankerTable.getCardType() == table.getCardType()) {
                return this.operOneWinTheSame(bankerPokerList, tablePokerList, bankerTable.getCardType());
            } else {
                return false;
            }
        } else if (bankerTable.getCardType() == 0 && table.getCardType() == 6) {
            return true;
        } else if (bankerTable.getCardType() == 6 && table.getCardType() == 0) {
            return false;
        }
        return false;

    }
    
    /**
     * @param bankerPokerList
     * @param tablePokerList
     * @param cardsType
     * @return boolean
     * @description: 牌型相同比较
     * @author mice
     * @date 2019/8/2
     */
    private  boolean operOneWinTheSame(List<Poker> bankerPokerList, List<Poker> tablePokerList, int cardsType) {
        int[] pokers = new int[3];
        int[] bePokers = new int[3];
        for (int i = 0; i < bankerPokerList.size(); i++) {
        	int value = bankerPokerList.get(i).getValue() == 1 ? 14 : bankerPokerList.get(i).getValue();
            pokers[i] = value;
        }
        for (int i = 0; i < tablePokerList.size(); i++) {
        	int value = tablePokerList.get(i).getValue() == 1 ? 14 : tablePokerList.get(i).getValue();
            bePokers[i] = value;
        }
        switch (Combination.forValue(cardsType)) {
        case PAIR:
        	if(pokers[1] > bePokers[1]) {
        		return true;
        	} else if(pokers[1] < bePokers[1]){
        		return false;
        	} else {
        		boolean single = singleBiger(pokers);
        		boolean beSingle = singleBiger(bePokers);
        		if(single) {
        			if(beSingle) {
        				if(pokers[0] > bePokers[0]) {
        					return true;
        				} else if(pokers[0] < bePokers[0]) {
        					return false;
        				} else {
        					return pokerTypeBiger(bankerPokerList,single);
        				}
        			} else {
        				return true;
        			}
        		} else {
        			if(beSingle) {
        				return false;
        			} else {
        				if(pokers[0] > bePokers[0]) {
        					return true;
        				} else if(pokers[0] < bePokers[0]) {
        					return false;
        				} else {
        					return pokerTypeBiger(bankerPokerList,single);
        				}
        			}
        		}
        	}
        case LEOPARD:
        	return judgeBiger(pokers, bePokers);
        case PROGRESSION:
        case STRAIGHTFLUSH:
        	//顺子 A 2 3 为最小顺子 A点数需转回为1
        	if(pokers[0] == 14 && pokers[1] == 2) {
        		pokers[0] = 1;
        	}
        	if(bePokers[0] == 14 && bePokers[1] == 2) {
        		bePokers[0] = 1;
        	}
        case FLUSH:
		case HIGHCARD:
			if(Arrays.equals(pokers, bePokers)) {
				if(bankerPokerList.get(0).getType() > tablePokerList.get(0).getType()) {
            		return true;
            	}
				return false;
			} else {
				return judgeBiger(pokers, bePokers);
			}
		default:
			return false;
		}

    }
    /**
     * 花色为4黑桃最大
     * @param pokerList
     * @param single
     * @return
     */
    private boolean pokerTypeBiger(List<Poker> pokerList, boolean single) {
		if(single) {
			if(pokerList.get(1).getType() == 4 || pokerList.get(2).getType() == 4) {
				return true;
			}
		} else {
			if(pokerList.get(0).getType() == 4 || pokerList.get(1).getType() == 4) {
				return true;
			}
		}
		return false;
	}
	/**
     * 对子中单牌点数比对子点数大
     * @param pokers
     * @return
     */
    private boolean singleBiger(int[] pokers) {
		if(pokers[0] != pokers[1]) {
			return true;
		}
		return false;
	}
	/**
     * 依次比较扑克点数大小
     * @param pokers
     * @param bePokers
     * @return
     */
    private boolean judgeBiger(int[] pokers, int[] bePokers) {
		for(int i = 0;i < pokers.length; i ++) {
			if(pokers[i] > bePokers[i]) {
				return true;
			} else if(pokers[i] < bePokers[i]){
				return false;
			}
		}
		return false;
	}
    /**
     * 获取特殊牌型
     * @return
     */
    public List<Poker> getSpecialPoker(){
    	
    	List<Poker> pokers = new ArrayList<>();
    	int random = RandomUtil.randomInt(10);
    	int type = RandomUtil.randomInt(1, 5);
    	Set<Integer> set = new HashSet<>();
    	int value = 0;
    	switch (random) {
    	case 5:
    	case 6:
    	case 7:
    		value = RandomUtil.randomInt(1, 14);
    		if(value > 11) {
    			value = 1;
    		}
    		set.add(value);
    		set.add(value + 1);
    		set.add(value + 2);
    		break;
    	case 8:
		case 9:
			value = RandomUtil.randomInt(1, 14);
			break;
		default:
	    	while(set.size() < 3) {
	    		set.add(RandomUtil.randomInt(1, 14));
	    	}
	    	break;
		}
    	
    	if(set.size() < 3) {//豹子
    		pokers.add(new Poker(value, 1));
    		pokers.add(new Poker(value,2));
    		pokers.add(new Poker(value,RandomUtil.randomInt(3, 5)));
    	} else {//金花、顺金
    		for (int val : set) {
                Poker poker = new Poker(val, type);
                pokers.add(poker);
            }
    	}
		return pokers;
    }
}
