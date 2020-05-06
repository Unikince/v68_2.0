package com.dmg.bairenlonghu.model;

import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.common.model.BaseRoom;
import com.dmg.bairenlonghu.model.dto.SettleResultDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

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
    private TreeMap<String,Seat> infieldSeatMap = new TreeMap<>(Comparator.comparing(Integer::new));
    /**
     * 游戏中所有玩家座位信息
     */
    private TreeMap<String, Seat> seatMap = new TreeMap<>(Comparator.comparing(Integer::new));
    /**
     *  5个牌桌位 key:牌位 value:牌桌
     */
    private Map<String,Table> tableMap = new HashMap<>();
    /**
     * 申请上庄的玩家
     */
    private LinkedList<BasePlayer> applyToZhuangPlayerList = new LinkedList<>();
    /**
     *  总下注额最高限制
     */
    private BigDecimal betChipLimit;
    /**
     *  当局总下注额
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
}