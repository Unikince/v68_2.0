package com.dmg.bjlserver.business.model.game;

import java.io.Serializable;

import com.dmg.common.pb.java.Bjl;

/**
 * 玩家战绩
 */
public class SeatRecord implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /**
     * 结束时间 当前时间戳/1000
     */
    public int gameOverTime;
    /**
     * 房间类型
     */
    public String roomType;
    /**
     * 结算后余额
     */
    public long gold;
    /**
     * 投注前余额
     */
    public long betStartGold;
    /**
     * 输赢金额
     */
    public long winLoseGold;

    /**
     * 庄
     */
    public int[] bankerCards;

    /**
     * 闲
     */
    public int[] playerCards;

    /**
     * 下注类型分数
     */
    public long[] areaBets = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];

    /**
     * 每个区域的赢
     */
    public long[] areaWinGold = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];

    /**
     * 是否是庄家
     */
    public boolean isBanker;
}
