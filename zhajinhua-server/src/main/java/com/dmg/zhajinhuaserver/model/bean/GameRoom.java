package com.dmg.zhajinhuaserver.model.bean;

import com.dmg.server.common.model.dto.UserControlInfoDTO;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.GameRoomConfig;
import com.dmg.zhajinhuaserver.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc 房间数据
 */
@Slf4j
@Data
public class GameRoom extends Room {
    private static final long serialVersionUID = 5764724159197955082L;
    private int playSeat; // 出牌座位
    private int lastPlayerSeat; // 上家出牌座位(断线重连用)
    private boolean hasJoker; // 是否是百变双Q
    private int cost; // 创建房间花费钻石
    private boolean aa; // 是否是AA制
    private Map<Integer, Seat> seatMap = new ConcurrentHashMap<>(); // 座位信息<座位号,数据>
    private Map<Integer, Boolean> dissolveMap = new HashMap<>(); // 记录玩家解散房间投票
    private transient List<Player> watchList = new ArrayList<>(); // 围观列表
    private transient Map<Long, Seat> winScoreMap = new HashMap<>(); // 记录参与游戏的玩家输赢分数
    private transient Map<Integer, List<RoundRecord>> roundRecordMap = new HashMap<>();
    private int dissolveRid; // 发起解散玩家rid
    private long dissolveTime; // 发起解散房间时间
    private int roomStatus; // 房间状态
    private Poker orderPoker; // 第一个抓到此牌的玩家先出牌以及确定座位
    private Segment lastPlayCard; // 刚出的牌
    private transient long dissolveTaskId; // 解散房间任务id
    private transient int lastWinSeat = 0; // 最近一个出完牌的玩家，用于接风判断
    private transient long deadTime; // 玩家都掉线()
    private transient int followSeat; // 接风
    private long totalChips;//房间内总下注
    private long lastBetChips;//上一玩家下注金额
    private boolean haveSeenCards;//是否已经看牌加倍
    public static final int[] discard = new int[]{1, 2, 5, 10, 20, 50, 100};
    private int operTurns; //轮数
    private Integer countTurns;//总轮数
    private int addChipsBet;//当前加注倍数
    private boolean haveThree;//当局是否豹子
    private boolean haveShunJin;//当局是否顺金
    private boolean isReady;//是否准备
    private long bloodId;//血拼玩家id
    private long bloodUpGold;//血拼前房间下注金额
    private boolean bloodUpSeenCards;//血拼前房间是否看牌
    private boolean haveRush;//是否血拼
    private int[] betMul;
    private List<Integer> betList = new ArrayList<>();
    private int bankerSeat; //庄家位置
    private int lastBanker;//上轮庄家位置
    private boolean useRoomCard;//创建房间使用房卡或砖石
    private Integer gameNumber; // 游戏编号
    /**
     * 如果是私人场就会有玩法选择
     */
    private CustomRule customRule;
    private int seeAnnotation; //看牌加注人数
    private int seeFollowUp; //看牌跟注人数
    private int comparisonWin; //比牌赢人数
    private Integer readyTime;//不准备踢出时间
    private Map<Long, UserControlInfoDTO> userControlInfoDTOMap = new HashMap<>();;//玩家点控自控数据

    public GameRoom() {
    }

    public void clear() {
        this.addChipsBet = 0;
        this.lastPlayCard = null;
        this.orderPoker = null;
        this.haveRush = false;
        this.haveSeenCards = false;
        this.followSeat = 0;
        this.totalChips = 0;
        this.roomStatus = Config.RoomStatus.READY;
        this.operTurns = 0;
        this.seeAnnotation = 0;
        this.seeFollowUp = 0;
        this.comparisonWin = 0;
        this.haveThree = false;
        this.isReady = false;
        this.bloodId = 0;
        this.betList.clear();
        this.haveShunJin = false;
        this.userControlInfoDTOMap = new HashMap<>();
    }

    public void clearAll() {
        this.winScoreMap.clear();
        this.seatMap.clear();
        this.playSeat = 0;
        this.addChipsBet = 0;
        this.lastPlayCard = null;
        this.orderPoker = null;
        this.dissolveMap.clear();
        this.haveRush = false;
        this.haveSeenCards = false;
        this.followSeat = 0;
        this.totalChips = 0;
        this.roomStatus = Config.RoomStatus.READY;
        this.operTurns = 0;
        this.seeAnnotation = 0;
        this.seeFollowUp = 0;
        this.comparisonWin = 0;
        this.haveThree = false;
        this.isReady = false;
        this.bloodId = 0;
        this.betList.clear();
        this.bloodUpGold = 0;
        this.bloodUpSeenCards = false;
        this.dissolveRid = 0;
        this.haveShunJin = false;
        this.userControlInfoDTOMap = new HashMap<>();
    }

    public GameRoom createPrivateRoom(int creator, Map<String, Object> maps) {
        this.roomId = (int) maps.get("roomId");
        this.creator = creator;
        round = 0;
        this.rule = (Integer) maps.get(D.GAME_RULE);
        this.extraRule = (Integer) maps.get(D.GAME_EXTRA_RULE);
        roomStatus = Config.RoomStatus.READY;
        createTime = System.currentTimeMillis();
        deadTime = 0;
        followSeat = 0;
        deadTime = 0;
        operTurns = 1;
        seeAnnotation = 0;
        seeFollowUp = 0;
        comparisonWin = 0;
        customRule = new CustomRule(this);
        totalPlayer = GameRoomConfig.zhajinhua.FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT;
        baseScore = 1;
        gameRoomTypeId = D.PRIVATE_FIELD;
        gameTypeId = GameRoomConfig.zhajinhua.FRIDE_GOLDEN_FLOWER_GAME_TYPE;
        return this;
    }


    /**
     * 获取指定座位的准备下注金币
     *
     * @param seatid 座位信息
     * @return
     */
    public long wagerBetChips(int seatid) {
        // 判断房间是否已经看牌下注加倍
        if (isHaveSeenCards()) {
            // 判断这个座位的人是否看牌
            if (seatMap.get(seatid).isHaveSeenCard()) {
                return this.lastBetChips;
            } else {
                return this.lastBetChips / 2;
            }
        } else {
            if (seatMap.get(seatid).isHaveSeenCard()) {
                return this.getLastBetChips() * 2;
            } else {
                return this.getLastBetChips();
            }
        }
    }

    /**
     * 获取指定座位的准备下注金币下标
     *
     * @param seatid 座位信息
     * @return
     */
    public int wagerChips(int seatid) {
        long lastBetChips;
        // 判断房间是否已经看牌下注加倍
        if (isHaveSeenCards()) {
            // 判断这个座位的人是否看牌
            if (seatMap.get(seatid).isHaveSeenCard()) {
                lastBetChips = this.lastBetChips;
            } else {
                lastBetChips = this.lastBetChips / 2;
            }
        } else {
            if (seatMap.get(seatid).isHaveSeenCard()) {
                lastBetChips = this.getLastBetChips() * 2;
            } else {
                lastBetChips = this.getLastBetChips();
            }
        }
        for (int i = 0; i < this.getBetMul().length; i++) {
            int betMul;
            if (seatMap.get(seatid).isHaveSeenCard()) {
                betMul = (this.getBetMul()[i] * this.baseScore) * 2;
            } else {
                betMul = (this.getBetMul()[i] * this.baseScore);
            }
            if (lastBetChips == betMul) {
                return i + 1;
            }
        }
        return 0;
    }
}
