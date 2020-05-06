package com.dmg.zhajinhuaserver.model.bean;

import com.dmg.zhajinhuaserver.config.Config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuqd
 * @Date 2017年8月29日
 * @Desc
 */
public class Seat {

	private int seatId;
	private Player player;
	private transient List<Poker> hand = new ArrayList<>(); // 手牌
	private int handSize; // 手牌数量
	private boolean pass; // 是否弃牌
	private boolean ready; // 是否准备好
	private boolean winOrder; // 是否赢家
	private int handCardsType; //手牌牌型
	private int score; // 房间总赢分
	private transient List<Balance> balanceList = new ArrayList<>(); // log信息
	private List<Segment> tip = new ArrayList<>(); // 提示
	private boolean canPass;
	private boolean offline; // true--离线，false-在线
	private boolean haveSeenCard;//是否已经看牌
	private double chipsRemain;//玩家椅子剩余筹码
	private double betChips;//玩家椅子下注筹码
	private OperNotify operNotify;//玩家基本操作
	private int actionSate;//玩家行动状态
	private boolean lostPk;//玩家椅子是否输掉PK
	private boolean hasFollowEnd;//跟到底
	private long actionEndTime;//行动结束时间
	private int state;//椅子状态
	/** 一轮中是否操作次数 */
	private boolean operedInOneTurn;//当前
	private List<Integer> actionOper;//玩家可行动列表
	private boolean isPlayed;//是否玩过游戏
	private boolean isTrust;//托管状态
	private Map<Integer, Integer> resultInfo = new HashMap<>();//结算牌型数据
	private int noReady;
	//扣底分
	private BigDecimal decBaseScore;
	public Seat() {
		
	}
	public Seat(int seatId, Player player) {
		this.seatId = seatId;
		this.player = player;
		this.pass = false;
		this.ready = false;
		this.winOrder = false;
		this.score = 0;
		this.offline = false;
		this.handCardsType = Config.Combination.NONE.getValue();
		this.operNotify = new OperNotify();
		this.chipsRemain = player.getGold();
		this.actionOper = new ArrayList<>();
		this.decBaseScore = BigDecimal.ZERO;
	}
	public void clear(boolean bool) {
		if(bool) {
			this.seatId = 0;
			this.player = null;
			this.chipsRemain = 0;
			this.offline = false;
		}
		this.actionEndTime = 0;
		this.hand.clear();
		this.pass = false;
		this.ready = false;
		this.score = 0;
		this.handCardsType = Config.Combination.NONE.getValue();
		this.operNotify = new OperNotify();
		this.actionSate = 0;
		this.betChips = 0;
		this.hand = new ArrayList<Poker>();
		this.handSize = 0;
		this.haveSeenCard = false;
		this.lostPk = false;
		this.operedInOneTurn = false;
		this.hasFollowEnd = false;
		this.actionOper = new ArrayList<>();
		this.isTrust = false;
		this.decBaseScore = BigDecimal.ZERO;
	}
	
	public Map<Integer, Integer> getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(Map<Integer, Integer> resultInfo) {
		this.resultInfo = resultInfo;
	}
	public void addResultInfo(Integer info) {
        if (info == null || info == 0) {
            return;
        }
        if (resultInfo.get(info) == null) {
        	resultInfo.put(info, 0);
        }
        resultInfo.put(info, resultInfo.get(info) + 1);
    }
	public boolean isPlayed() {
		return isPlayed;
	}
	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
	}
	public int getState() {
		return state;
	}

	public boolean isTrust() {
		return isTrust;
	}
	public void setTrust(boolean isTrust) {
		this.isTrust = isTrust;
	}
	public void setState(int state) {
		this.state = state;
	}

	public List<Integer> getActionOper() {
		return actionOper;
	}

	public void setActionOper(List<Integer> actionOper) {
		this.actionOper = actionOper;
	}

	public long getActionEndTime() {
		return actionEndTime;
	}

	public void setActionEndTime(long actionEndTime) {
		this.actionEndTime = actionEndTime;
	}

	public boolean isHasFollowEnd() {
		return hasFollowEnd;
	}

	public void setHasFollowEnd(boolean hasFollowEnd) {
		this.hasFollowEnd = hasFollowEnd;
	}

	public boolean isOperedInOneTurn() {
		return operedInOneTurn;
	}

	public void setOperedInOneTurn(boolean operedInOneTurn) {
		this.operedInOneTurn = operedInOneTurn;
	}

	public boolean isLostPk() {
		return lostPk;
	}

	public void setLostPk(boolean lostPk) {
		this.lostPk = lostPk;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public int getActionSate() {
		return actionSate;
	}

	public void setActionSate(int actionSate) {
		this.actionSate = actionSate;
	}

	
	public OperNotify getOperNotify() {
		return operNotify;
	}

	public void setOperNotify(OperNotify operNotify) {
		this.operNotify = operNotify;
	}

	public double getChipsRemain() {
		return chipsRemain;
	}

	public void setChipsRemain(double chipsRemain) {
		this.chipsRemain = chipsRemain;
	}

	public double getBetChips() {
		return betChips;
	}

	public void setBetChips(double betChips) {
		this.betChips = betChips;
	}

	public boolean isHaveSeenCard() {
		return haveSeenCard;
	}

	public void setHaveSeenCard(boolean haveSeenCard) {
		this.haveSeenCard = haveSeenCard;
	}

	public int getHandCardsType() {
		return handCardsType;
	}

	public void setHandCardsType(int handCardsType) {
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
	public List<Poker> getBackHand() {
		List<Poker> list = new ArrayList<>();
		Poker poker1 = new Poker(14,4);
		Poker poker2 = new Poker(14,4);
		Poker poker3 = new Poker(14,4);
		list.add(poker1);
		list.add(poker2);
		list.add(poker3);
		return list;
	}
	public List<Poker> getHand() {
		return hand;
	}

	public void setHand(List<Poker> hand) {
		this.hand = hand;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
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

	public void setWinOrder(boolean winOrder) {
		this.winOrder = winOrder;
	}

	public List<Balance> getBalanceList() {
		return balanceList;
	}

	public void setBalanceList(List<Balance> balanceList) {
		this.balanceList = balanceList;
	}

	public List<Segment> getTip() {
		return tip;
	}

	public void setTip(List<Segment> tip) {
		this.tip = tip;
	}

	public boolean isCanPass() {
		return canPass;
	}

	public void setCanPass(boolean canPass) {
		this.canPass = canPass;
	}

	public int getHandSize() {
		return handSize;
	}

	public void setHandSize(int handSize) {
		this.handSize = handSize;
	}

	public boolean isOffline() {
		return offline;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	public int getNoReady() {
		return noReady;
	}

	public void setNoReady(int noReady) {
		this.noReady = noReady;
	}

	@Override
	public String toString() {
		return "Seat [seatId=" + seatId + ", player=" + player + ", handSize=" + handSize + ", pass=" + pass
				+ ", ready=" + ready + ", winOrder=" + winOrder + ", handCardsType=" + handCardsType + ", score="
				+ score + ", tip=" + tip + ", canPass=" + canPass + ", offline=" + offline + ", haveSeenCard="
				+ haveSeenCard + ", chipsRemain=" + chipsRemain + ", betChips=" + betChips + ", operNotify="
				+ operNotify + ", actionSate=" + actionSate + ", lostPk=" + lostPk + ", hasFollowEnd=" + hasFollowEnd
				+ ", actionEndTime=" + actionEndTime + ", state=" + state + ", operedInOneTurn=" + operedInOneTurn
				+ ", actionOper=" + actionOper + ", isPlayed=" + isPlayed + ", resultInfo=" + resultInfo + "]";
	}

	public BigDecimal getDecBaseScore() {
		return decBaseScore;
	}

	public void setDecBaseScore(BigDecimal decBaseScore) {
		this.decBaseScore = decBaseScore;
	}

}
