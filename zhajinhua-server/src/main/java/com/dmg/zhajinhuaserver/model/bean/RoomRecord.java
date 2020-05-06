package com.dmg.zhajinhuaserver.model.bean;

public class RoomRecord {
	private String gameObject; // 游戏类型
	private int serverId;
	private long roleId; // 玩家Id
	private String headImgUrl; // 头像
	private int roomId;// 房间号
	private int allTime; // 总时长
	private int status; // 状态
	private long endTime; // 结束时间
	private long createTime; // 创建时间
	private String address; // 游戏地址
	private int round; // 局数

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	private int platform; // 渠道
	private String uniqueSign; // 唯一标识

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getGameObject() {
		return gameObject;
	}

	public void setGameObject(String gameObject) {
		this.gameObject = gameObject;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getAllTime() {
		return allTime;
	}

	public void setAllTime(int allTime) {
		this.allTime = allTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public String getUniqueSign() {
		return uniqueSign;
	}

	public void setUniqueSign(String uniqueSign) {
		this.uniqueSign = uniqueSign;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

}