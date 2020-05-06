package com.dmg.doudizhuserver.business.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.dmg.common.core.util.SpringUtil;
import com.dmg.doudizhuserver.business.config.local.Constant;
import com.dmg.doudizhuserver.business.config.local.RoomConfig;
import com.dmg.doudizhuserver.business.util.GenerateSequenceUtil;

/**
 * 斗地主房间
 */
public class Room {
    /** 房间配置 */
    public RoomConfig config;
    /** 房间唯一id */
    public int uniquId = 0;

    /** 一副牌 */
    public List<Card> cards;
    /** 牌桌的座位 */
    public List<Seat> seats;
    /** 地主牌，3张 */
    public List<Card> hiddenCards;
    /** 游戏阶段 */
    public Stage stage;
    /** 炸弹数量 */
    public int bombNum;
    /** 出牌轮数 */
    public int playRound;
    /** 是否是春天 */
    public boolean spring;

    /** 地主 */
    public Seat landlord;
    /** 下一个应该叫地主的玩家 */
    public Seat nextCallSeat;
    /** 下一个应该出牌的玩家 */
    public Seat nextPlaySeat;
    /** 上一次出牌的玩家 */
    public Seat prePlaySeat;
    /** 上一次玩家出的牌 */
    public PlayCards prePlayCards;

    /** 计时器：加入房间、抢地主 */
    public ScheduledFuture<?> future;
    /** 计时器：机器人加入房间 */
    public ScheduledFuture<?> robotFuture;
    /** 保险定时器，用于卡死 */
    public ScheduledFuture<?> future00;

    public Room(RoomConfig config) {
        this.config = config;
        GenerateSequenceUtil generateSequenceUtil = SpringUtil.getBean(GenerateSequenceUtil.class);
        this.uniquId = generateSequenceUtil.generateUniquId();
        this.cards = Arrays.asList(Card.values());
        Collections.shuffle(this.cards);

        this.seats = new ArrayList<>(Constant.SEAT_NUM);
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            this.seats.add(new Seat(i, this));
        }
        this.stage = Stage.WAIT;
    }

    /** 下一个位置 */
    public Seat nextSeat(Seat seat) {
        return this.seats.get((seat.order + 1) % Constant.SEAT_NUM);
    }

    /** 上一个位置 */
    public Seat preSeat(Seat seat) {
        return this.seats.get((seat.order + 2) % Constant.SEAT_NUM);
    }

    /** 是否房间已经坐满 */
    public boolean isSeatFull() {
        for (Seat s : this.seats) {
            if (s.playerId == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否房间已经走空
     */
    public boolean isSeatEnpty() {
        for (Seat s : this.seats) {
            if (s.playerId != 0 && !s.robot) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否所有人都没有叫地主
     */
    public boolean allNotCallLandlord() {
        for (Seat s : this.seats) {
            if (s.callScore != -1) {
                return false;
            }
        }
        return true;
    }

    /** 初始化地主牌 */
    public void initHiddenCards() {
        List<Card> cards = new ArrayList<>();
        cards.addAll(this.cards);
        for (Seat seat : this.seats) {
            cards.removeAll(seat.handCards.cards);
        }
        this.hiddenCards = cards;
    }

    /** 真人数量 */
    public int humanNum() {
        int humanNum = 0;
        for (Seat seat : this.seats) {
            if (seat.playerId > 0 && !seat.robot) {
                humanNum++;
            }
        }
        return humanNum;
    }

    public void closeFuture(boolean cnull) {
        if (this.future != null) {
            this.future.cancel(false);
            if (cnull) {
                this.future = null;
            }
        }
    }

    public void closeRobotFuture() {
        if (this.robotFuture != null) {
            this.robotFuture.cancel(false);
        }
        this.robotFuture = null;
    }

    public void closeFuture0() {
        if (this.future00 != null) {
            this.future00.cancel(false);
        }
    }
}
