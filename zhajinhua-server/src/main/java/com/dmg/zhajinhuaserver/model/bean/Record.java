package com.dmg.zhajinhuaserver.model.bean;

/**
 * @author zhuqd
 * @Date 2017年9月5日
 * @Desc
 */
public class Record {

	private String game;
	private long roleId;
	private int roomId;
	private int score;
	private String headImgUrl;
	private String nickname;
	private int platform;
	private int roundTime;
	private String date;
	private long uniqueSign;

	//
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getRoundTime() {
		return roundTime;
	}

	public void setRoundTime(int roundTime) {
		this.roundTime = roundTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getUniqueSign() {
		return uniqueSign;
	}

	public void setUniqueSign(long uniqueSign) {
		this.uniqueSign = uniqueSign;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

}
