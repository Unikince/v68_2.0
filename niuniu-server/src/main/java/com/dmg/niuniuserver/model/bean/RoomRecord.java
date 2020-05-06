package com.dmg.niuniuserver.model.bean;

import lombok.Data;

@Data
public class RoomRecord {
	private String gameObject; // 游戏类型
	private int serverId;
	private long userId; // 玩家Id
	private String headImgUrl; // 头像
	private int roomId;// 房间号
	private int allTime; // 总时长
	private int status; // 状态
	private long endTime; // 结束时间
	private long createTime; // 创建时间
	private String address; // 游戏地址
	private int round; // 局数
	private int platform; // 渠道
	private String uniqueSign; // 唯一标识

}