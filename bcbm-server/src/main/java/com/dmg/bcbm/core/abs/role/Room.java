package com.dmg.bcbm.core.abs.role;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author zhuqd
 * @Date 2017年8月30日
 * @Desc 房间基类，游戏中所以房间继承该类。并将房间放入RoomManager中进行管理
 */
public class Room implements Serializable {
	
	private static Logger logger = LoggerFactory.getLogger(Room.class);
    private static final long serialVersionUID = 7151967532994977574L;

    protected int roomId; // 房间id
    protected int time; // 房间总时长(毫秒)
    protected int round; // 游戏当前局数
    protected int totalRound; // 总局数
    protected long playTime; // 开始游戏时间(毫秒)
    protected long createTime; // 房间创建时间(毫秒)
    protected int playerNumber; // 房间中座位上的人数
    protected int totalPlayer; // 房间总座位数
    protected String creator; // 房间创建者id
    protected long overTime; // 房间结束时间
    protected String gameName; // 游戏名字
    protected int grade;// 当前房间等级(初,中,高...)
    protected int baseScore; //房间底分
    protected int lowerLimit; // 当前房间最低携带金额
    protected int upperLimit; // 当前房间最高携带金额
    protected int gameTypeId; // 游戏ID(炸金花,牛牛)
    protected int totalBet; // 上限,当前房间最高上限下注金额
    protected int gameRoomTypeId;// 游戏类型ID(自由场,比赛场)
    protected int rule;//房间规则
    protected int extraRule;//房间额外规则
    // 创建房间消耗的房卡数量
    protected int consumptionRoomCardQuantity;
    protected boolean noWashCard;//斗地主不洗牌

    /**
     * 该方法创建私人场的时候调用
     *
     * @author CharlesLee
     * @date 2018/5/7 0007 10:01
     */

    public boolean isNoWashCard() {
		return noWashCard;
	}

	public void setNoWashCard(boolean noWashCard) {
		this.noWashCard = noWashCard;
	}

	protected transient int serverId;

    public int getRule() {
		return rule;
	}

	public void setRule(int rule) {
		this.rule = rule;
	}

	public int getExtraRule() {
		return extraRule;
	}

	public void setExtraRule(int extraRule) {
		this.extraRule = extraRule;
	}

	public int getTotalBet() {
        return totalBet;
    }

    public void setTotalBet(int totalBet) {
        this.totalBet = totalBet;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(int baseScore) {
        this.baseScore = baseScore;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getTotalRound() {
        return totalRound;
    }

    public void setTotalRound(int totalRound) {
        this.totalRound = totalRound;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
    	if (this.playerNumber < 0) {
			this.playerNumber = 0;
		}else {
			this.playerNumber = playerNumber;
		}
        logger.info("room player nums:" + this.playerNumber);
    }


    public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public long getOverTime() {
        return overTime;
    }

    public void setOverTime(long overTime) {
        this.overTime = overTime;
    }

    public int getTotalPlayer() {
        return totalPlayer;
    }

    public void setTotalPlayer(int totalPlayer) {
        this.totalPlayer = totalPlayer;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getGameTypeId() {
        return gameTypeId;
    }

    public void setGameTypeId(int gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public int getGameRoomTypeId() {
        return gameRoomTypeId;
    }

    public void setGameRoomTypeId(int gameRoomTypeId) {
        this.gameRoomTypeId = gameRoomTypeId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public int getConsumptionRoomCardQuantity() {
        return consumptionRoomCardQuantity;
    }

    public void setConsumptionRoomCardQuantity(int consumptionRoomCardQuantity) {
        this.consumptionRoomCardQuantity = consumptionRoomCardQuantity;
    }

	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", time=" + time + ", round=" + round
				+ ", totalRound=" + totalRound + ", playTime=" + playTime
				+ ", createTime=" + createTime + ", playerNumber="
				+ playerNumber + ", totalPlayer=" + totalPlayer + ", creator="
				+ creator + ", overTime=" + overTime + ", gameName=" + gameName
				+ ", grade=" + grade + ", baseScore=" + baseScore
				+ ", lowerLimit=" + lowerLimit + ", upperLimit=" + upperLimit
				+ ", gameTypeId=" + gameTypeId + ", totalBet=" + totalBet
				+ ", gameRoomTypeId=" + gameRoomTypeId + ", rule=" + rule
				+ ", extraRule=" + extraRule + ", consumptionRoomCardQuantity="
				+ consumptionRoomCardQuantity + ", noWashCard=" + noWashCard
				+ "]";
	}
}
