package com.dmg.niuniuserver.model.bean;



import com.dmg.niuniuserver.model.constants.Combination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuqd
 */
public class Seat {

    private int seatId;
    private Player player;
    private transient List<Poker> hand = new ArrayList<>(); // 手牌
    // 玩家散牌列表
    private transient List<Poker> scatteredCards = new ArrayList<>();
    // 玩家拼整数牌型列表
    private transient List<Poker> WholeTenCards = new ArrayList<>();
    private boolean ready; // 是否准备好
    private boolean winOrder; // 是否赢家
    private Combination handCardsType; //手牌牌型
    private int score; // 房间总赢分
    private transient List<Balance> balanceList = new ArrayList<>(); // 结算信息
    private boolean offline; // true--离线，false-在线
    private boolean haveSeenCard;//是否已经开牌
    private double chipsRemain;//玩家椅子剩余筹码
    private long betChips;//玩家椅子下注筹码
    private long actionEndTime;//行动结束时间
    private int status;//椅子状态
    // 该玩家已经在此房间游戏的局数
    private int gameCount;

    private Balance balance;

    private Map<Integer, Map<Integer, List<Poker>>> cacheGameInfo = new HashMap<>();


    public void clear(boolean bool, GameRoom room) {
        if (bool) {
            this.seatId = 0;
            this.player = null;
            this.chipsRemain = 0;
            this.offline = false;
        }
        this.actionEndTime = 0;
        this.hand.clear();
        this.score = 0;
        this.betChips = 0;
        this.hand = new ArrayList<>();
        this.haveSeenCard = false;
        this.scatteredCards = new ArrayList<>();
        this.WholeTenCards = new ArrayList<>();
        status = 0;
        balance = null;

    }

    public List<Poker> send(int count) {
        if (count == 1) {
            List<Poker> copyList = new ArrayList<>();
            for (int i = 0; i < hand.size() - 1; i++) {
                copyList.add(hand.get(i));
            }
            return copyList;
        } else {
            return hand;
        }

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public long getActionEndTime() {
        return actionEndTime;
    }

    public void setActionEndTime(long actionEndTime) {
        this.actionEndTime = actionEndTime;
    }


    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }


    public Seat(int seatId, Player player) {
        this.seatId = seatId;
        this.player = player;
        this.ready = false;
        this.winOrder = false;
        this.score = 0;
        this.offline = false;
        this.chipsRemain = player.getGold();
    }

    public List<Poker> getScatteredCards() {
        return scatteredCards;
    }

    public void setScatteredCards(List<Poker> scatteredCards) {
        this.scatteredCards = scatteredCards;
    }

    public List<Poker> getWholeTenCards() {
        return WholeTenCards;
    }

    public void setWholeTenCards(List<Poker> wholeTenCards) {
        WholeTenCards = wholeTenCards;
    }

    public double getChipsRemain() {
        return chipsRemain;
    }

    public void setChipsRemain(double chipsRemain) {
        this.chipsRemain = chipsRemain;
    }

    public long getBetChips() {
        return betChips;
    }

    public void setBetChips(long betChips) {
        this.betChips = betChips;
    }

    public boolean isHaveSeenCard() {
        return haveSeenCard;
    }

    public void setHaveSeenCard(boolean haveSeenCard) {
        this.haveSeenCard = haveSeenCard;
    }

    public Combination getHandCardsType() {
        return handCardsType;
    }

    public void setHandCardsType(Combination handCardsType) {
        this.handCardsType = handCardsType;
    }

    public int getSeatId() {
        return seatId;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Poker> getHand() {
        return hand;
    }

    public void setHand(List<Poker> hand) {
        this.hand = hand;
    }


    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isWinOrder() {
        return winOrder;
    }

    public List<Balance> getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(List<Balance> balanceList) {
        this.balanceList = balanceList;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public int getGameCount() {
        return gameCount;
    }

    public void setGameCount(int gameCount) {
        this.gameCount = gameCount;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Seat seat = (Seat) o;

        return seatId == seat.seatId;
    }

    @Override
    public int hashCode() {
        return seatId;
    }

    public Map<Integer, Map<Integer, List<Poker>>> getCacheGameInfo() {
        return cacheGameInfo;
    }

    public void setCacheGameInfo(Map<Integer, Map<Integer, List<Poker>>> cacheGameInfo) {
        this.cacheGameInfo = cacheGameInfo;
    }

    public void addCacheGameInfo(List<Poker> hand, Combination type, int count) {
        cacheGameInfo.put(count, new HashMap<Integer, List<Poker>>());
        cacheGameInfo.get(count).put(type.getValue(), hand);
    }

	@Override
	public String toString() {
		return "Seat [seatId=" + seatId + ", player=" + player + ", ready="
				+ ready + ", winOrder=" + winOrder + ", handCardsType="
				+ handCardsType + ", score=" + score + ", offline=" + offline
				+ ", haveSeenCard=" + haveSeenCard + ", chipsRemain="
				+ chipsRemain + ", betChips=" + betChips + ", actionEndTime="
				+ actionEndTime + ", status=" + status + ", gameCount="
				+ gameCount + ", balance=" + balance + ", cacheGameInfo="
				+ cacheGameInfo + "]";
	}
}
