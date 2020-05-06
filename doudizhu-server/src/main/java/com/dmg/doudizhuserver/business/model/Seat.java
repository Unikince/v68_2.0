package com.dmg.doudizhuserver.business.model;

import java.util.concurrent.ScheduledFuture;

/**
 * 斗地主位置
 */
public class Seat {
    /** 座位顺序(从0开始),没特殊需求最好不该变它 */
    public final int order;
    /** 所属房间 */
    public Room room;

    /** 玩家id */
    public long playerId;
    /** 玩家昵称 */
    public String nickname;
    /** 头像 */
    public String headImg;
    /** 性别 */
    public int sex;
    /** 是否是机器人 true:是 */
    public boolean robot;
    /** 是否在线 */
    public boolean online;
    /** 心跳掉线 */
    public boolean heartbeatLost;
    /** 托管 */
    public boolean auto;
    /** 做托管 */
    public boolean autoActive;
    /** 自动出牌次数 */
    public int autoNum;
    /** 手牌 */
    public HandCards handCards;
    /** 叫地主分数 */
    public int callScore;
    /** 出牌次数 */
    public int playNum = 0;

    /** 出牌超时定时器 */
    public ScheduledFuture<?> future;
    /** 托管定时Future */
    public ScheduledFuture<?> autoFuture;
    /** 机器人出牌定时器 */
    public ScheduledFuture<?> robotFuture;

    public Seat(int order, Room room) {
        this.order = order;
        this.room = room;
    }

    /**
     * 重置座位数据
     */
    public void reset() {
        this.auto = false;
        this.autoActive = false;
        this.autoNum = 0;
        this.playNum = 0;
        this.callScore = 0;
        this.handCards = null;
        this.closeFuture(true);
    }

    /**
     * 清空座位数据
     */
    public void clear() {
        this.playerId = 0;
        this.nickname = null;
        this.headImg = null;
        this.sex = 0;
        this.online = false;
        this.robot = false;
        this.robot = false;
        this.heartbeatLost = false;
        this.reset();
    }

    public void closeFuture(boolean cnull) {
        if (this.future != null) {
            this.future.cancel(false);
            if (cnull) {
                this.future = null;
            }
        }
        this.closeAutoFuture(cnull);
        if (this.robotFuture != null) {
            this.robotFuture.cancel(false);
            if (cnull) {
                this.robotFuture = null;
            }
        }
    }

    public void closeAutoFuture(boolean cnull) {
        if (this.autoFuture != null) {
            this.autoFuture.cancel(false);
            if (cnull) {
                this.autoFuture = null;
            }
        }
    }

}
