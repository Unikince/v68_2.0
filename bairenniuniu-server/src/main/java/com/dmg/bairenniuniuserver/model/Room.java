package com.dmg.bairenniuniuserver.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.bairenniuniuserver.common.model.BaseRoom;
import com.dmg.bairenniuniuserver.model.dto.SettleResultDTO;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/29 15:21
 * @Version V1.0
 **/
@Data
public class Room extends BaseRoom {
    /**
     * 庄家
     */
    private Seat banker;
    /**
     * 是否为系统庄家
     */
    private boolean systemBanker;
    /**
     * 内场玩家
     */
    private TreeMap<String, Seat> infieldSeatMap = new TreeMap<>(Comparator.comparing(Integer::new));
    /**
     * 游戏中所有玩家座位信息
     */
    private Map<String, Seat> seatMap = new ConcurrentHashMap<>();
    /**
     * 5个牌桌位 key:牌位 value:牌桌
     */
    private Map<String, Table> tableMap = new HashMap<>();
    /**
     * 输赢统计 key:牌位 value:输赢
     */
    private Map<String, LinkedList<Boolean>> reportFormMap = new HashMap<>();
    /**
     * 申请上庄的玩家
     */
    private LinkedList<BasePlayer> applyToZhuangPlayerList = new LinkedList<>();
    /**
     * 总下注额最高限制
     */
    private BigDecimal betChipLimit;
    /**
     * 当局总下注额
     */
    private BigDecimal curRoundTotalBetChip;

    /**
     * 房间输赢统计 key:赢true 输false value:输赢场次
     */
    private Map<Boolean, Integer> winCountMap = new HashMap<>();

    /**
     * 房间结算信息
     */
    private SettleResultDTO settleResultDTO;
    /**
     * 是否有玩家下注
     */
    private boolean hasPlayerBet = false;
    /**
     * 下注数统计
     */
    private AtomicInteger betCount = new AtomicInteger(0);

    /**
     * 赌神第一次下注位置
     */
    private String godOfGamblersFirstBetTable =null;
}