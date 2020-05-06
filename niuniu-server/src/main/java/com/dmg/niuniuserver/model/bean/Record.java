package com.dmg.niuniuserver.model.bean;

import lombok.Data;

/**
 * @author zhuqd
 * @Date 2017年9月5日
 * @Desc
 */
@Data
public class Record {

	private String game;
	private long userId;
	private int roomId;
	private int score;
	private String headImgUrl;
	private String nickname;
	private int platform;
	private int roundTime;
	private String date;
	private long uniqueSign;
}
