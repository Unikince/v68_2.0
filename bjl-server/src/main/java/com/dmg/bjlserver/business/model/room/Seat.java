package com.dmg.bjlserver.business.model.room;

import org.apache.commons.lang3.StringUtils;

import com.dmg.bjlserver.business.model.game.SeatRecord;
import com.dmg.common.pb.java.Bjl;

/**
 * 座位
 */
public class Seat {
    /** 所属场次 */
    public final Table table;
    /** 座位顺序(从0开始),没特殊需求最好不该变它 */
    public final int order;
    /** 玩家id */
    public long playerId;
    /** 玩家昵称 */
    public String nickName = "";
    /** 是否在线 */
    public boolean online;
    /** 0:男,非0:女 */
    public int sex;
    /** 头像 */
    public String icon = "";
    /** 是否是机器人 true:是 */
    public boolean robot;
    /** 机器人是否申请退出 */
    public boolean robotExit;
    /** 是否续压过 */
    public boolean continueBeted;

    /** 区域下注 */
    public long[] areaBets = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
    /** 每个区域的输赢数量 */
    public long[] winGolds = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
    /** 每次税收 */
    public long tax;
    /** 金币,玩家金币的缓存 */
    public long gold;
    /** 离线局数计数器 */
    public int offlineRound;
    /** 没有下注次数 */
    public int notBetRound;
    /** 当前局战绩 */
    public SeatRecord seatRecord = null;

    public Seat(int order, Table table) {
        this.order = order;
        this.table = table;
    }

    public Seat(int order, Table table, long playerId, String nickName) {
        this(order, table);
        this.playerId = playerId;
        this.nickName = nickName;
    }

    /**
     * 座位信息
     */
    public Bjl.SeatInfo seatInfo() {
        Bjl.SeatInfo.Builder seatInfo = Bjl.SeatInfo.newBuilder();
        seatInfo.setPlayerId(this.playerId);
        seatInfo.setNickName(this.nickName);
        seatInfo.setGold(this.gold);
        if (StringUtils.isNotBlank(this.icon)) {
            seatInfo.setIcon(this.icon);
        }
        seatInfo.setSex(this.sex);
        return seatInfo.build();
    }

    /**
     * 合计下注金币
     *
     * @return
     */
    public long totalBet() {
        long totalBet = 0;
        for (int i = 0; i < this.areaBets.length; i++) {
            totalBet += this.areaBets[i];
        }

        return totalBet;
    }

    /**
     * 合计输赢金币
     *
     * @return
     */
    public long totalWinGold() {
        long totalWinGold = 0;
        for (int i = 0; i < this.winGolds.length; i++) {
            totalWinGold += this.winGolds[i];
        }
        return totalWinGold;
    }

    /**
     * 合计赢的区域 下注金币
     */
    public long totalWinBet() {
        long totalWinBet = 0;
        for (int i = 0; i < this.winGolds.length; i++) {
            if (this.winGolds[i] > 0) {
                totalWinBet += this.areaBets[i];
            }
        }
        return totalWinBet;
    }

    /**
     * 重置座位数据
     */
    public void reset() {
        this.tax = 0;
        this.winGolds = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
        this.areaBets = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
        this.continueBeted = false;
        this.seatRecord = null;
    }

    /**
     * 清空座位数据
     */
    public void clear() {
        this.playerId = 0;
        this.nickName = "";
        this.online = false;
        this.sex = 0;
        this.icon = "";
        this.robot = false;
        this.robotExit = false;

        this.reset();
        this.offlineRound = 0;
        this.notBetRound = 0;
        this.seatRecord = null;
        this.gold = 0;
    }
}
