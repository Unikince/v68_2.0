package com.dmg.zhajinhuaserver.model.bean;

/**
 * @author zhuqd
 * @Date 2017年12月13日
 * @Desc
 */
public class RoundRecord {
	private int roleId;
	private long winScore;
	private String nickname;
	private int qType;
	private int round;

	//
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


	public long getWinScore() {
		return winScore;
	}

	public void setWinScore(long winScore) {
		this.winScore = winScore;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getqType() {
		return qType;
	}

	public void setqType(int qType) {
		this.qType = qType;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

}
