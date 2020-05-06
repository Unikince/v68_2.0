package com.dmg.niuniuserver.model.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhuqd
 * @Date 2017年8月29日
 * @Desc 结算数据
 */
public class Balance {
    private long userId;
    private int round; // 局数
    private BigDecimal totalScore; // 总赢分
    private BigDecimal winscore; // 赢分
    private boolean win; // 是否是赢家
    private int handCardType; //牌型
    private List<Poker> handCards; //手牌
    private int seatId; //座位号
    private String nickname; //玩家名字
    private int bankerFlag; // 是否庄家 1 为庄家
    private BigDecimal pumpMoney;//抽水金额
    private Boolean isRobot;
    private BigDecimal bets;

    public Balance(long userId, int round, String roleName, int seatIndex, int bankerFlag) {
        this.userId = userId;
        this.round = round;
        this.nickname = roleName;
        this.winscore = BigDecimal.ZERO;
        this.seatId = seatIndex;
        this.bankerFlag = bankerFlag;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public List<Poker> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Poker> handCards) {
        this.handCards = handCards;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }


    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public BigDecimal getWinscore() {
        return winscore;
    }

    public void setWinscore(BigDecimal winscore) {
        this.winscore = winscore;
        if (this.winscore.compareTo(BigDecimal.ZERO) > 0) {
            win = true;
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getHandCardType() {
        return handCardType;
    }

    public void setHandCardType(int handCardType) {
        this.handCardType = handCardType;
    }

    public int getBankerFlag() {
        return bankerFlag;
    }

    public void setBankerFlag(int bankerFlag) {
        this.bankerFlag = bankerFlag;
    }

    public BigDecimal getPumpMoney() {
        return pumpMoney;
    }

    public void setPumpMoney(BigDecimal pumpMoney) {
        this.pumpMoney = pumpMoney;
    }

    public Boolean getRobot() {
        return isRobot;
    }

    public void setRobot(Boolean robot) {
        isRobot = robot;
    }

    public BigDecimal getBets() {
        return bets;
    }

    public void setBets(BigDecimal bets) {
        this.bets = bets;
    }
}
