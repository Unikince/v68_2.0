package com.dmg.niuniuserver.model.bean;

import com.dmg.niuniuserver.model.constants.Combination;
import com.dmg.server.common.model.dto.UserControlInfoDTO;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc 房间数据
 */
@Data
public class GameRoom extends Room {

    private boolean privateRoom = false;
    private CustomRule customRule;
    private static final long serialVersionUID = 1L;
    private Map<Integer, Seat> seatMap = new ConcurrentHashMap<>(); // 座位信息<座位号,数据>
    private Map<Integer, Boolean> dissolveMap = new HashMap<>(); // 记录玩家解散房间投票
    private transient List<Player> watchList = new ArrayList<>(); // 围观列表
    private transient Map<Long, Seat> winScoreMap = new HashMap<>(); // 记录参与游戏的玩家输赢分数
    private transient Map<Integer, List<RoundRecord>> roundRecordMap = new HashMap<>();
    private long dissolveUserId; // 发起解散玩家userId
    private long dissolveTime; // 发起解散房间时间
    private int roomStatus; // 房间状态
    private transient long dissolveTaskId; // 解散房间任务id
    // 下注倍数配置
    public int[] discard;
    // 庄家
    private Seat bankerSeat;
    // 庄家抢庄的时候的倍数
    private int bankerMultiple;

    public int[] betMultiple;
    // 玩家抢庄列表
    private Map<Integer, Integer> bankerFraction = new ConcurrentHashMap<>();

    // 玩家开牌列表
    private Map<Integer, Seat> openCards = new ConcurrentHashMap<>();
    // 当前抢庄倍数
    private int curMaxRobBet;
    // 阶段倒计时, 每一个阶段都会更新这个倒计时时间
    private long phaseCountdown;

    /**
     * 根据不同的场次,房间初始化不同的牌型倍数
     * 牌型, 倍数
     *
     * @date 2018/5/16 0016 16:02
     */
    private Map<Integer, Integer> mulConfig;

    /**
     * 上一把牛牛牌型最大的玩家
     */
    private Seat maxLastNiouNiou;

    /**
     * 上一把参加游戏的玩家个数
     */
    private int lastPlayerCount;
    /**
     * @description: 游戏编号
     * @author mice
     * @date 2019/7/16
     */
    private int gameNumber;

    /**
     * 记录抢庄操作时候玩家最大的抢庄倍数
     */
    private List<Seat> maxQiangZhuangList = new ArrayList<>();
    private boolean useRoomCard;//创建房间使用房卡或砖石
    private Integer readyTime;//不准备踢出时间
    private Map<Long, UserControlInfoDTO> userControlInfoDTOMap = new HashMap<>();//玩家点控自控数据

    public int[] getDiscardMul(Seat seat) {
        return discard;
    }


    @SuppressWarnings("unchecked")
    private int[] getRulesByList(String rule, Map<String, Object> map) {
        int[] result = null;
        List<Integer> list = (List<Integer>) map.get(rule);
        if (list != null) {
            result = new int[list.size()];
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                result[i] = list.get(i);
            }
        } else {
            System.out.println("规则配置错误!");
        }
        Arrays.sort(result);
        return result;
    }

    public void clear() {
        dissolveMap.clear();
        bankerFraction.clear();
        openCards.clear();
        bankerMultiple = 1;
        roomStatus = 0;
        setPhaseCountdown(0);
        maxQiangZhuangList.clear();
        bankerSeat = null;
        this.curMaxRobBet = 0;
        this.userControlInfoDTOMap = new HashMap<>();
    }

    public void clearAll() {
        this.seatMap.clear();
        this.winScoreMap.clear();
        this.dissolveMap.clear();
        bankerFraction.clear();
        openCards.clear();
        bankerMultiple = 0;
        bankerSeat = null;
        roomStatus = 0;
        setPhaseCountdown(0);
        this.bankerMultiple = 0;
        this.curMaxRobBet = 0;
        this.userControlInfoDTOMap = new HashMap<>();
    }

    public GameRoom() {
        betMultiple = new int[]{0, 1, 2, 3, 4};
        //discard = new int[]{1, 2, 3, 4, 5};
        mulConfig = initMoneyRoom();
    }


    public GameRoom(int roomId, int time, long creator, boolean hasJoker, int cost, boolean aa, int totalRound) {
        this.roomId = roomId;
        this.time = time;
        this.creator = creator;
        this.round = 1;
        this.createTime = System.currentTimeMillis();
        this.totalRound = totalRound;
    }

    public int getMul(int cardType) {
        return mulConfig.get(cardType);
    }

    public boolean isUseRoomCard() {
        return useRoomCard;
    }

    public void setUseRoomCard(boolean useRoomCard) {
        this.useRoomCard = useRoomCard;
    }

    public int getCurMaxRobBet() {
        return curMaxRobBet;
    }

    public void setCurMaxRobBet(int curMaxRobBet) {
        this.curMaxRobBet = curMaxRobBet;
    }

    public Map<Integer, Seat> getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(Map<Integer, Seat> seatMap) {
        this.seatMap = seatMap;
    }

    public Map<Integer, Boolean> getDissolveMap() {
        return dissolveMap;
    }

    public void setDissolveMap(Map<Integer, Boolean> dissolveMap) {
        this.dissolveMap = dissolveMap;
    }

    public int getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(int roomStatus) {
        this.roomStatus = roomStatus;
    }


    public long getDissolveTaskId() {
        return dissolveTaskId;
    }

    public void setDissolveTaskId(long dissolveTaskId) {
        this.dissolveTaskId = dissolveTaskId;
    }

    public long getDissolveTime() {
        return dissolveTime;
    }

    public void setDissolveTime(long dissolveTime) {
        this.dissolveTime = dissolveTime;
    }

    public List<Player> getWatchList() {
        return watchList;
    }

    public void setWatchList(List<Player> watchList) {
        this.watchList = watchList;
    }

    public Map<Long, Seat> getWinScoreMap() {
        return winScoreMap;
    }

    public void setWinScoreMap(Map<Long, Seat> winScoreMap) {
        this.winScoreMap = winScoreMap;
    }

    public Map<Integer, List<RoundRecord>> getRoundRecordMap() {
        return roundRecordMap;
    }

    public void setRoundRecordMap(Map<Integer, List<RoundRecord>> roundRecordMap) {
        this.roundRecordMap = roundRecordMap;
    }

    public Map<Integer, Integer> getBankerFraction() {
        return bankerFraction;
    }

    public void setBankerFraction(Map<Integer, Integer> bankerFraction) {
        this.bankerFraction = bankerFraction;
    }

    public Seat getBankerSeat() {
        return bankerSeat;
    }

    public void setBankerSeat(Seat bankerSeat) {
        this.bankerSeat = bankerSeat;
    }

    public int getBankerMultiple() {
        return bankerMultiple;
    }

    public void setBankerMultiple(int bankerMultiple) {
        this.bankerMultiple = bankerMultiple;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    /**
     * 已经参加过游戏并且全部准备的玩家
     */
    public int getplayerReadyCount() {
        int readyCount = 0;
        for (Seat seat : seatMap.values()) {
            if (seat.isReady() && seat.getGameCount() > 0) {
                readyCount++;
            }
        }
        return readyCount;
    }

    /**
     * 没有参加过游戏并且准备的玩家数量
     */
    public int getNoReadyPlayerCount() {
        int readyCount = 0;
        for (Seat seat : seatMap.values()) {
            if (seat.isReady() && seat.getGameCount() == 0) {
                readyCount++;
            }
        }
        return readyCount;
    }

    /**
     * 所有准备玩家的数量
     */
    public int readyCount() {
        int readyCount = 0;
        for (Seat seat : seatMap.values()) {
            if (seat.isReady()) {
                readyCount++;
            }
        }
        return readyCount;
    }

    public int getBetPlayerCount() {
        int betPlayerCount = 0;
        for (Seat seat : seatMap.values()) {
            if (seat.getBetChips() != 0) {
                betPlayerCount++;
            }
        }
        return betPlayerCount;
    }

    public int getOpenCount() {
        int count = 0;
        for (Seat seat : seatMap.values()) {
            if (seat.isHaveSeenCard()) {
                count++;
            }
        }
        return count;
    }

    public int getNoBankerFraction() {
        int count = 0;
        for (Integer i : bankerFraction.values()) {
            if (i == 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取最大牌型的玩家
     */
    public synchronized Seat getMaxCardsType() {
        Combination max = Combination.UNDEFINE;
        for (Seat seat : seatMap.values()) {
            if (seat.getHand() == null || seat.getHand().size() == 0) {
                continue;
            }
            if (seat.getHandCardsType().getValue() > max.getValue()) {
                max = seat.getHandCardsType();
            }
        }

        List<Seat> seats = new ArrayList<>();
        for (Seat seat : seatMap.values()) {
            if (seat.getHand() == null || seat.getHand().size() == 0) {
                continue;
            }
            if (seat.getHandCardsType().getValue() == max.getValue()) {
                seats.add(seat);
            }
        }
        Seat maxSeat = null;
        if (seats.size() == 1) {
            return seats.get(0);
        }
        to:
        for (Seat seat1 : seats) {
            for (Seat seat2 : seats) {
                if (seat1.getSeatId() == seat2.getSeatId()) {
                    continue;
                }
                for (int i = seat1.getHand().size() - 1; i >= 0; i--) {
                    if (seat1.getHand().get(i).getValue() > seat2.getHand().get(i).getValue()) {
                        maxSeat = seat1;
                        break to;
                    } else {
                        maxSeat = seat2;
                        break to;
                    }
                }
            }
        }
        return maxSeat;
    }


    /**
     * 判断是否固定庄家玩法
     */
    public boolean fixedZhuangJia() {
        return false;
    }

    /**
     * 初始化金币场倍数配置
     */
    private Map<Integer, Integer> initMoneyRoom() {
        Map<Integer, Integer> config = new HashMap<>(34);
        config.put(Combination.HIGH_CARD.getValue(), 1);
        config.put(Combination.FALSE_CATTLE_ONE.getValue(), 1);
        config.put(Combination.CATTLE_ONE.getValue(), 1);
        config.put(Combination.FALSE_CATTLE_TWO.getValue(), 1);
        config.put(Combination.CATTLE_TWO.getValue(), 1);
        config.put(Combination.FALSE_CATTLE_THREE.getValue(), 1);
        config.put(Combination.CATTLE_THREE.getValue(), 1);
        config.put(Combination.FALSE_CATTLE_FOUR.getValue(), 1);
        config.put(Combination.CATTLE_FOUR.getValue(), 1);
        config.put(Combination.FALSE_CATTLE_FIVE.getValue(), 1);
        config.put(Combination.CATTLE_FIVE.getValue(), 1);
        config.put(Combination.FALSE_CATTLE_SIX.getValue(), 1);
        config.put(Combination.CATTLE_SIX.getValue(), 1);
        config.put(Combination.FALSE_CATTLE_SEVEN.getValue(), 2);
        config.put(Combination.CATTLE_SEVEN.getValue(), 2);
        config.put(Combination.FALSE_CATTLE_EIGHT.getValue(), 2);
        config.put(Combination.CATTLE_EIGHT.getValue(), 2);
        config.put(Combination.FALSE_CATTLE_NINE.getValue(), 3);
        config.put(Combination.CATTLE_NINE.getValue(), 3);
        config.put(Combination.FALSE_CATTLE_CATTLE.getValue(), 4);
        config.put(Combination.CATTLE_CATTLE.getValue(), 4);
        config.put(Combination.FALSE_SUEN_ZI_CATTLE.getValue(), 5);
        config.put(Combination.SUEN_ZI_CATTLE.getValue(), 5);
        config.put(Combination.FALSE_TONG_HUA_CATTLE.getValue(), 5);
        config.put(Combination.TONG_HUA_CATTLE.getValue(), 5);
        config.put(Combination.FALSE_WU_HUA_CATTLE.getValue(), 5);
        config.put(Combination.WU_HUA_CATTLE.getValue(), 5);
        //config.put(Combination.FALSE_HU_LU_CATTLE.getValue(), 6);
        //config.put(Combination.HU_LU_CATTLE.getValue(), 6);
        config.put(Combination.FALSE_BOMB_CATTLE.getValue(), 6);
        config.put(Combination.BOMB_CATTLE.getValue(), 6);
        config.put(Combination.FALSE_WU_XIAO_CATTLE.getValue(), 8);
        config.put(Combination.WU_XIAO_CATTLE.getValue(), 8);
        return config;
    }

    /**
     * 初始化私人场倍数配置
     * 1,普通规则
     * 2,高级规则
     * 3,疯狂规则
     */
    private Map<Integer, Integer> initPrivateConfig(int pattern, Map<Integer, Integer> mulConfig) {
        switch (pattern) {
            // 普通规则
            case 1:
                mulConfig.put(Combination.FALSE_CATTLE_SEVEN.getValue(), 1);
                mulConfig.put(Combination.CATTLE_SEVEN.getValue(), 1);
                mulConfig.put(Combination.FALSE_CATTLE_NINE.getValue(), 2);
                mulConfig.put(Combination.CATTLE_NINE.getValue(), 2);
                mulConfig.put(Combination.FALSE_CATTLE_CATTLE.getValue(), 3);
                mulConfig.put(Combination.CATTLE_CATTLE.getValue(), 3);
                break;
            // 高级规则
            case 2:
                // 高级规则为金币场默认倍数规则,不修改啊
                break;
            // 疯狂规则
            case 3:
                mulConfig.put(Combination.FALSE_CATTLE_TWO.getValue(), 2);
                mulConfig.put(Combination.CATTLE_TWO.getValue(), 2);
                mulConfig.put(Combination.FALSE_CATTLE_THREE.getValue(), 3);
                mulConfig.put(Combination.CATTLE_THREE.getValue(), 3);
                mulConfig.put(Combination.FALSE_CATTLE_FOUR.getValue(), 4);
                mulConfig.put(Combination.CATTLE_FOUR.getValue(), 4);
                mulConfig.put(Combination.FALSE_CATTLE_FIVE.getValue(), 5);
                mulConfig.put(Combination.CATTLE_FIVE.getValue(), 5);
                mulConfig.put(Combination.FALSE_CATTLE_SIX.getValue(), 6);
                mulConfig.put(Combination.CATTLE_SIX.getValue(), 6);
                mulConfig.put(Combination.FALSE_CATTLE_SEVEN.getValue(), 7);
                mulConfig.put(Combination.CATTLE_SEVEN.getValue(), 7);
                mulConfig.put(Combination.FALSE_CATTLE_EIGHT.getValue(), 8);
                mulConfig.put(Combination.CATTLE_EIGHT.getValue(), 8);
                mulConfig.put(Combination.FALSE_CATTLE_NINE.getValue(), 9);
                mulConfig.put(Combination.CATTLE_NINE.getValue(), 9);
                mulConfig.put(Combination.FALSE_CATTLE_CATTLE.getValue(), 10);
                mulConfig.put(Combination.CATTLE_CATTLE.getValue(), 10);
                break;
            default:
        }
        return mulConfig;
    }

    public List<Seat> getDaYingJia() {
        List<Seat> list = new ArrayList<>();
        double max = 0;
        for (Seat seat : seatMap.values()) {
            if (seat.getChipsRemain() > max) {
                max = seat.getChipsRemain();
            }
        }
        for (Seat seat : seatMap.values()) {
            if (seat.getChipsRemain() == max) {
                list.add(seat);
            }
        }
        return list;
    }

    public void addLastNiouNiou(Seat seat) {
        if (maxLastNiouNiou == null) {
            maxLastNiouNiou = seat;
        } else {
            if (seat.getHandCardsType().getValue() > maxLastNiouNiou.getHandCardsType().getValue()) {
                maxLastNiouNiou = seat;
            } else if (seat.getHandCardsType().getValue() == maxLastNiouNiou.getHandCardsType().getValue()) {
                for (int i = seat.getHand().size() - 1; i > 0; i--) {
                    if (seat.getHand().get(i).getValue() > maxLastNiouNiou.getHand().get(i).getValue()) {
                        maxLastNiouNiou = seat;
                    }
                }
            }
        }

    }
}
