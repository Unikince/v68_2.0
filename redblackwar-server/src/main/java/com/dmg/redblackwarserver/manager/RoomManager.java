package com.dmg.redblackwarserver.manager;

import cn.hutool.core.util.RandomUtil;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.redblackwarserver.common.model.BasePlayer;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.Seat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/29 17:05
 * @Version V1.0
 **/
@Slf4j
public class RoomManager {
    private RoomManager(){}
    private static class RoomManageFactory{
        private static RoomManager roomManager = new RoomManager();
    }

    public static RoomManager intance(){
        return RoomManageFactory.roomManager;
    }
    /** 停服标志 */
    @Setter
    @Getter
    private boolean shutdownServer = false;

    /**
     * 汇率
     */
    @Setter
    @Getter
    private BigDecimal exchangeRate;

    @Setter
    @Getter
    private String gameId;

    private AtomicInteger initRoomId = new AtomicInteger(10000);
    /**
     * 所有房间
     * key:房间id value:房间
    */
    private Map<Integer, Room> roomMap = new HashMap<>();

    /** 牌型倍数配置
     * key:房间等级  key:牌型 value:倍数
     */
    @Getter
    @Setter
    private Map<Integer, Map<String, Integer>> multipleConfigMap = new HashMap<>();

    /** 下注额配置
     * key:房间等级  value:下注额
     */
    private Map<Integer, List<Integer>> betChipsMap = new HashMap<>();

    /** 系统胜率
     * key:房间等级 : 游戏配置
     */
    @Getter
    @Setter
    private Map<Integer, BairenGameConfigDTO> gameConfigMap = new HashMap<>();

    /** 进房间额度限制
     * key:房间等级  value:限制额
     */
    private Map<String, Integer> goldLimitMap = new HashMap<>();

    /**
     * 系统金币
     */
    private BigDecimal systemGold = new BigDecimal(0);

    /** 系统胜率
     * key:赢钱数量 :获胜几率
     */
    private Map<BigDecimal, Integer> systemWinProbabilityMap = new HashMap<>();

    /** 12345 全排列
     * key:Integer : 12345组合数组
     */
    private Map<Integer, List<Integer>> fullyArrangedMap = new HashMap<>();
    
    /** 
     * 台红
     * key:房间等级  value:桌号对应台红值
     */
    @Getter
    private Map<Integer, BigDecimal> taiHongMap = new HashMap<>();

    /**
     * 系统金币 key:roomLevel value:该场次盈利数
     */
    @Getter
    private Map<Integer,BigDecimal> roomGoldMap = new ConcurrentHashMap<>();
    
    /** 
     * 抽水
     * key:房间等级  value:抽水值
     */
    @Setter
    @Getter
    private Map<Integer, BigDecimal> pumpMap = new ConcurrentHashMap<>();
    
    /** 
     * 各个桌号对应下注限制
     * key:房间等级  value:上/下庄值
     */
    @Getter
    private Map<Integer, Map<String,BigDecimal>> areaBetLimitMap = new HashMap<>();
    
    //系统赢钱门槛限制逐额递增
    private int winLimit;
    
    //下轮系统输/赢
    private boolean nextRoundWin = true;

    /**
     * @description: 创建房间
     * @param level
     * @return com.dmg.Room
     * @author mice
     * @date 2019/7/29
    */
    public Room createRoom(int level){
    	Room room = new Room(initRoomId.getAndIncrement(),level,RoomManager.intance().getPumpRate(level));
        Seat bankerSeat = new Seat();
        BasePlayer basePlayer = new BasePlayer();
        basePlayer.setNickname("Banker");
        bankerSeat.setPlayer(basePlayer);
        room.setBanker(bankerSeat);
        room.setMultipleConfigMap(multipleConfigMap.get(room.getLevel()));
        roomMap.put(room.getRoomId(),room);
        return room;
    }

    /**
     * @description: 获取房间
     * @param roomId
     * @return com.dmg.Room
     * @author mice
     * @date 2019/7/29
    */
    public Room getRoom(int roomId){
        return roomMap.get(roomId);
    }

    /**
     * @description: 获取玩家座位信息
     * @param room
     * @param userId
     * @return com.dmg.Seat
     * @author mice
     * @date 2019/7/29
     */
    public Seat getSeat(Room room,int userId){
        for (Seat seat : room.getSeatMap().values()){
            if (seat.getPlayer().getUserId() == userId)
                return seat;
        }
        return null;
    }

    /**
     * @description:
     * @return seatIndex
     * @param userId
     * @author mice
     * @date 2019/7/29
     */
    public int addPlayerGold(int roomId,long userId,BigDecimal addGoldNum){
        Room room = roomMap.get(roomId);
        if (!room.isSystemBanker() && room.getBanker().getPlayer().getUserId() == userId){
            Seat banker = room.getBanker();
            banker.getPlayer().setGold(banker.getPlayer().getGold().add(addGoldNum));
            return 0;
        }
        synchronized (room){
            for (Seat seat : room.getSeatMap().values()){
                if (seat.getPlayer().getUserId()==userId){
                    seat.getPlayer().setGold(seat.getPlayer().getGold().add(addGoldNum));
                    return seat.getSeatIndex();
                }
            }
        }
        return -1;
    }

    /**
     * @description: 获取牌型倍数
     * @param roomLevel
     * @param cardType
     * @return java.math.BigDecimal
     * @author mice
     * @date 2019/7/31
    */
    public BigDecimal getMultiple(int roomLevel,int cardType){
        int multiple = this.getMultipleConfigMap().get(roomLevel).get(cardType + "");
        return new BigDecimal(multiple);
    }

    /**
     * @description: 获取牌型最大倍数
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/10/9
     */
    public int getMaxMutiple(int roomLevel){
        Map<String, Integer> map = this.multipleConfigMap.get(roomLevel);
        return map.values().stream().max(Integer::compareTo).get();
    }

    /**
     * @description: 获取系统输赢
     * @param
     * @return boolean
     * @author mice
     * @date 2019/8/5
    */
    public boolean getSystemWin(){
        if (systemGold.compareTo(BigDecimal.ZERO) < 0){
            return true;
        }
        LinkedList<BigDecimal> keyList = new LinkedList<BigDecimal>();
        keyList.addAll(systemWinProbabilityMap.keySet());
        int winProbability = getSystemWinProbability(keyList);
        int randomInt = RandomUtil.randomInt(100);
        log.info("==> 随机值为:{}",randomInt);
        if (randomInt >= 100 - winProbability){
            return true;
        }
        return nextRoundWin;
    }

    /**
     * @description: 获取系统胜率
     * @param
     * @return int
     * @author mice
     * @date 2019/8/5
     */
    private int getSystemWinProbability(LinkedList<BigDecimal> keyList){
        BigDecimal key = keyList.pop();
        if (key.compareTo(systemGold)>=0 || keyList.size()==0){
            return systemWinProbabilityMap.get(key);
        }else {
            getSystemWinProbability(keyList);
        }

        return 100;
    }

    /**
     * @description: 添加当前场次赢钱数
     * @param roomLevel
     * @param winGold
     * @return void
     * @author mice
     * @date 2019/9/28
     */
    public void addRoomWinGold(int roomLevel,BigDecimal winGold){
        roomGoldMap.put(roomLevel,roomGoldMap.get(roomLevel).add(winGold));
    }

    /**
     * @description: 添加当前场次抽水钱数
     * @param roomLevel
     * @param pumpGold
     * @return void
     * @author mice
     * @date 2019/10/12
     */
    public void addPump(int roomLevel,BigDecimal pumpGold){
        pumpMap.put(roomLevel,pumpMap.get(roomLevel).add(pumpGold));
    }
    
    /**
     * @description: 房间名称
     * @param roomLevel
     * @return BigDecimal
     * @author mice
     * @date 2019/10/9
     */
    public String getName(int roomLevel) {
        return this.getGameConfigMap().get(roomLevel).getBairenFileConfigDTO().getFileName();
    }

    /**
     * @description: 获取抽水率
     * @param roomLevel
     * @return BigDecimal
     * @author mice
     * @date 2019/10/9
     */
    public BigDecimal getPumpRate(int roomLevel){
        return this.getGameConfigMap().get(roomLevel).getBairenFileConfigDTO().getPumpRate().divide(new BigDecimal(100));
    }

    /**
     * @description: 获取控制控制执行率
     * @param
     * @return boolean
     * @author mice
     * @date 2019/8/5
     */
    public Double getControlExecuteRate(int roomLevel){
        List<BairenGameConfigDTO.WaterPoolConfigDTO> waterPoolConfigDTOS = gameConfigMap.get(roomLevel).getWaterPoolConfigDTOS();
        Double controlExecuteRate = new Double("0");
        int roomWinGold = roomGoldMap.get(roomLevel).intValue();
        for (BairenGameConfigDTO.WaterPoolConfigDTO waterPoolConfigDTO : waterPoolConfigDTOS){
            if (waterPoolConfigDTO.getWaterLow() <= roomWinGold && waterPoolConfigDTO.getWaterHigh() > roomWinGold){
                controlExecuteRate = waterPoolConfigDTO.getControlExecuteRate();
                break;
            }else {
                continue;
            }
        }
        return controlExecuteRate;
    }

    /**
     * @description: 获取最大限额
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/9/29
     */
    public int getMaxPayout(int roomLevel){
        return this.gameConfigMap.get(roomLevel).getBairenControlConfigDTO().getMaxPayout();
    }

    /**
     * @description: 获取最大赔付参考值
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/9/29
     */
    public int getMaxPayoutReferenceValue(int roomLevel){
        return this.gameConfigMap.get(roomLevel).getBairenControlConfigDTO().getMaxPayoutReferenceValue();
    }

    /**
     * @description: 获取当前场次赢钱数
     * @param roomLevel
     * @return java.math.BigDecimal
     * @author mice
     * @date 2019/9/28
     */
    public BigDecimal getRoomWinGold(int roomLevel){
        return this.roomGoldMap.get(roomLevel);
    }

    /**
     * @description: 获取区域下注限制(上限)
     * @param roomLevel
     * @return BigDecimal
     * @author mice
     * @date 2019/10/9
     */
    public BigDecimal getAreaBetUpLimit(int roomLevel){
        long areaBetUpLimit = this.gameConfigMap.get(roomLevel).getBairenFileConfigDTO().getAreaBetUpLimit();
        return new BigDecimal(areaBetUpLimit);
    }

    //===================getter setter=================

    public Map<Integer, Room> getRoomMap() {
        return roomMap;
    }

    public void setRoomMap(Map<Integer, Room> roomMap) {
        this.roomMap = roomMap;
    }

    public Map<Integer, List<Integer>> getBetChipsMap() {
        return betChipsMap;
    }
    
    public Map<String,String> getBetChipsMap2() {
        Map<String, String> betChipMap = new HashMap<>();
        for (Integer fileId : this.betChipsMap.keySet()){
            List<Integer> values=betChipsMap.get(fileId);
            String s="";
            for(Integer value:values){
                s+=value+",";
            }
            s=s.substring(0,s.length()-1);
            betChipMap.put(fileId+"",s);
        }
        return betChipMap;
    }

    public void setBetChipsMap(Map<Integer, List<Integer>> betChipsMap) {
        this.betChipsMap = betChipsMap;
    }

    public Map<String, Integer> getGoldLimitMap() {
        return goldLimitMap;
    }

    public void setGoldLimitMap(Map<String, Integer> goldLimitMap) {
        this.goldLimitMap = goldLimitMap;
    }

    public BigDecimal getSystemGold() {
        return systemGold;
    }

    public void setSystemGold(BigDecimal systemGold) {
        this.systemGold = systemGold;
    }

    public Map<BigDecimal, Integer> getSystemWinProbabilityMap() {
        return systemWinProbabilityMap;
    }

    public void setSystemWinProbabilityMap(Map<BigDecimal, Integer> systemWinProbabilityMap) {
        this.systemWinProbabilityMap = systemWinProbabilityMap;
    }

    public Map<Integer, List<Integer>> getFullyArrangedMap() {
        return fullyArrangedMap;
    }

    public void setFullyArrangedMap(Map<Integer, List<Integer>> fullyArrangedMap) {
        this.fullyArrangedMap = fullyArrangedMap;
    }

	public int getWinLimit() {
		return winLimit;
	}

	public void setWinLimit(int winLimit) {
		this.winLimit = winLimit;
	}

	public boolean isNextRoundWin() {
		return nextRoundWin;
	}

	public void setNextRoundWin(boolean nextRoundWin) {
		this.nextRoundWin = nextRoundWin;
	}
	public void setWinLimit() {
		int watershed = systemGold.intValue() / 5;
		winLimit = winLimit > watershed ? winLimit : watershed;
	}
	
}