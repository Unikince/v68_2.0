package com.dmg.bairenniuniuserver.manager;

import cn.hutool.core.util.RandomUtil;
import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.bairenniuniuserver.model.Room;
import com.dmg.bairenniuniuserver.model.RoomStatus;
import com.dmg.bairenniuniuserver.model.Seat;
import com.dmg.bairenniuniuserver.model.Table;
import com.dmg.bairenniuniuserver.model.constants.D;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.SpringUtil;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private boolean shutdownServer = false;

    private int initRoomId = 10000;

    @Setter
    @Getter
    private String gameId;
    /**
     * 汇率
     */
    @Setter
    @Getter
    private BigDecimal exchangeRate;

    @Getter
    private Map<String ,Integer> failCountMap = new HashMap<>();

    /**
     * 所有房间
     * key:房间id value:房间
    */
    private Map<Integer, Room> roomMap = new HashMap<>();

    /** 牌型倍数配置
     * key:房间等级  key:牌型 value:倍数
     */
    private Map<Integer, Map<Integer, Integer>> multipleConfigMap = new HashMap<>();

    /** 下注额配置
     * key:房间等级  value:下注额
     */
    private Map<Integer, List<Integer>> betChipsMap = new HashMap<>();

    /**
     * 系统金币 key:roomLevel value:该场次盈利数
     *//*
    @Getter
    private Map<Integer,BigDecimal> roomGoldMap = new ConcurrentHashMap<>();*/

    /**
     * 抽水 key:roomLevel value:该场次抽水数
     */
    @Getter
    private Map<Integer,BigDecimal> pumpMap = new ConcurrentHashMap<>();

    /**
     * 系统流水
     */
    private BigDecimal systemTurnover = new BigDecimal(0);

    /** 系统胜率
     * key:房间等级 : 游戏配置
     */
    @Getter
    @Setter
    private Map<Integer, BairenGameConfigDTO> gameConfigMap = new HashMap<>();

    /** 12345 全排列
     * key:Integer : 12345组合数组
     */
    private Map<Integer, List<Integer>> fullyArrangedMap = new HashMap<>();

    /**
     * @description: 获取当前场次赢钱数
     * @param roomLevel
     * @return java.math.BigDecimal
     * @author mice
     * @date 2019/9/28
    */
    public BigDecimal getRoomWinGold(int roomLevel){
        StringRedisTemplate  redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        String winGoldStr = redisTemplate.opsForValue().get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + roomLevel);
        if (StringUtils.isEmpty(winGoldStr)){
            redisTemplate.opsForValue().set(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + roomLevel,"0");
            return BigDecimal.ZERO;
        }
        return new BigDecimal(winGoldStr);
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
        StringRedisTemplate  redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        redisTemplate.opsForValue().increment(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + roomLevel,winGold.doubleValue());
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
     * @description: 创建房间
     * @param level
     * @return com.dmg.bairenniuniuserver.model.Room
     * @author mice
     * @date 2019/7/29
    */
    public Room createRoom(Integer level){
        Seat bankerSeat = new Seat();
        BasePlayer basePlayer = new BasePlayer();
        basePlayer.setNickname("Banker");
        bankerSeat.setPlayer(basePlayer);

        Room room = new Room();
        room.setRoomId(initRoomId++);
        room.setCreateTime(System.currentTimeMillis());
        room.setLevel(level);
        room.setFileName(RoomManager.intance().getGameConfigMap().get(level).getBairenFileConfigDTO().getFileName());
        room.setRoomStatus(RoomStatus.BET);
        room.setCountdownTime(System.currentTimeMillis()+D.BET_TIME);
        room.setTotalPlayer(100);
        room.setBetChipLimit(new BigDecimal(0));
        room.setBanker(bankerSeat);
        room.setSystemBanker(true);
        for (int i = 1; i < 6; i++) {
            Table table = new Table();
            table.setTableIndex(i+"");
            room.getTableMap().put(i+"",table);
        }
        roomMap.put(room.getRoomId(),room);
        return room;
    }

    /**
     * @description: 获取房间
     * @param roomId
     * @return com.dmg.bairenniuniuserver.model.Room
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
     * @return com.dmg.bairenniuniuserver.model.Seat
     * @author mice
     * @date 2019/7/29
     */
    public Seat getSeat(Room room,int userId){
        synchronized (room){
            for (Seat seat : room.getSeatMap().values()){
                if (seat.getPlayer().getUserId()==userId)
                    return seat;
            }
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
        int multiple = this.getMultipleConfigMap().get(roomLevel).get(cardType);
        return new BigDecimal(multiple);
    }

    /**
     * @description: 获取台红值
     * @param roomLevel
     * @return java.math.BigDecimal
     * @author mice
     * @date 2019/7/31
     */
    public BigDecimal getRedValue(int roomLevel){
        long redValue = this.getGameConfigMap().get(roomLevel).getBairenFileConfigDTO().getRedValue();
        return new BigDecimal(redValue);
    }

    /**
     * @description: 获取自动下装下限
     * @param roomLevel
     * @return java.math.BigDecimal
     * @author mice
     * @date 2019/7/31
     */
    public BigDecimal getBankerGoldLowLimit(int roomLevel){
        int bankerGoldLowLimit = this.getGameConfigMap().get(roomLevel).getBairenFileConfigDTO().getBankerGoldLowLimit();
        return new BigDecimal(bankerGoldLowLimit);
    }

    /**
     * @description: 获取自动下装下限
     * @param roomLevel
     * @return java.math.BigDecimal
     * @author mice
     * @date 2019/7/31
     */
    public BigDecimal getRoomBetLowLimit(int roomLevel){
        int roomBetLowLimt = this.getGameConfigMap().get(roomLevel).getBairenFileConfigDTO().getRoomBetLowLimit();
        return new BigDecimal(roomBetLowLimt);
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
        int roomWinGold = this.getRoomWinGold(roomLevel).intValue();
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
     * @description: 是否闪避 通杀 或 通赔
     * @param roomLevel
     * @param win true通杀 false通赔
     * @return boolean true闪避 false不闪避
     * @author mice
     * @date 2019/9/28
    */
    public boolean isAllWinOrLose(int roomLevel,boolean win){
        Double rate;
        if (win){
            rate = this.getGameConfigMap().get(roomLevel).getBairenControlConfigDTO().getAllWinDodgeRate();
        }else {
            rate = this.getGameConfigMap().get(roomLevel).getBairenControlConfigDTO().getAllLoseDodgeRate();
        }
        int random = RandomUtil.randomInt(100);
        if (random <=rate){
            log.warn("==>是否闪避 通杀 或 通赔:{},随机值:{},闪避概率:{}",true,random,rate);
            return true;
        }else {
            log.warn("==>是否闪避 通杀 或 通赔:{},随机值:{},闪避概率:{}",false,random,rate);
            return false;
        }
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
    public void addSystemTurnover(BigDecimal turnover){
        synchronized (RoomManager.class){
            RoomManager.intance().systemTurnover = RoomManager.intance().systemTurnover.add(turnover);
        }
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
     * @description: 获取场次限制额
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/10/9
    */
    public int getFileLimit(int roomLevel){
        return this.getGameConfigMap().get(roomLevel).getBairenFileConfigDTO().getFileLimit();
    }

    public Map<String, Integer> getFileLimitMap(){
        Map<String, Integer> goldLimitMap = new HashMap<>();
        for (Integer fileId : this.gameConfigMap.keySet()){
            goldLimitMap.put(fileId+"",this.gameConfigMap.get(fileId).getBairenFileConfigDTO().getFileLimit());
        }
        return goldLimitMap;
    }

    public Map<String, Integer> getBankerLimitMap() {
        Map<String, Integer> bankerLimitMap = new HashMap<>();
        for (Integer fileId : this.gameConfigMap.keySet()){
            bankerLimitMap.put(fileId+"",this.gameConfigMap.get(fileId).getBairenFileConfigDTO().getApplyBankerLimit());
        }
        return bankerLimitMap;
    }

    public Map<String, String> getBetChipMap() {
        Map<String, String> betChipMap = new HashMap<>();
        for (Integer fileId : this.gameConfigMap.keySet()){
            betChipMap.put(fileId+"",this.gameConfigMap.get(fileId).getBairenFileConfigDTO().getBetChips());
        }
        return betChipMap;
    }

    /**
     * @description: 获取抽水率
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/10/9
    */
    public BigDecimal getPumpRate(int roomLevel){
        return this.getGameConfigMap().get(roomLevel).getBairenFileConfigDTO().getPumpRate();
    }

    /**
     * @description: 获取上庄轮数限制
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/10/9
     */
    public int getBankRoundLimit(int roomLevel){
        return this.getGameConfigMap().get(roomLevel).getBairenFileConfigDTO().getBankRoundLimit();
    }

    /**
     * @description: 获取牌型最大倍数
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/10/9
    */
    public int getMaxMutiple(int roomLevel){
        Map<Integer, Integer> map = this.multipleConfigMap.get(roomLevel);
        return map.values().stream().max(Integer::compareTo).get();
    }

    /**
     * @description: 获取区域下注限制(下限)
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/10/9
     */
    public long getBetLowLimit(int roomLevel){
        return this.gameConfigMap.get(roomLevel).getBairenFileConfigDTO().getAreaBetLowLimit();
    }

    /**
     * @description: 获取区域下注限制(上限)
     * @param roomLevel
     * @return int
     * @author mice
     * @date 2019/10/9
     */
    public long getBetUpLimit(int roomLevel){
        return this.gameConfigMap.get(roomLevel).getBairenFileConfigDTO().getAreaBetUpLimit();
    }

    //===================getter setter=================

    public Map<Integer, Room> getRoomMap() {
        return roomMap;
    }

    public void setRoomMap(Map<Integer, Room> roomMap) {
        this.roomMap = roomMap;
    }

    public Map<Integer, Map<Integer, Integer>> getMultipleConfigMap() {
        return multipleConfigMap;
    }

    public void setMultipleConfigMap(Map<Integer, Map<Integer, Integer>> multipleConfigMap) {
        this.multipleConfigMap = multipleConfigMap;
    }

    public Map<Integer, List<Integer>> getBetChipsMap() {
        return betChipsMap;
    }

    public void setBetChipsMap(Map<Integer, List<Integer>> betChipsMap) {
        this.betChipsMap = betChipsMap;
    }

    public Map<Integer, List<Integer>> getFullyArrangedMap() {
        return fullyArrangedMap;
    }

    public void setFullyArrangedMap(Map<Integer, List<Integer>> fullyArrangedMap) {
        this.fullyArrangedMap = fullyArrangedMap;
    }

    public BigDecimal getSystemTurnover() {
        return systemTurnover;
    }

    public void setSystemTurnover(BigDecimal systemTurnover) {
        this.systemTurnover = systemTurnover;
    }


    public boolean isShutdownServer() {
        return shutdownServer;
    }

    public void setShutdownServer(boolean shutdownServer) {
        this.shutdownServer = shutdownServer;
    }
}