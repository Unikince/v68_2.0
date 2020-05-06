package com.dmg.niuniuserver.model.bean;

import lombok.Data;

/**
 * @author zhuqd
 * @Date 2017年12月13日
 * @Desc
 */
@Data
public class RoundRecord {
	private int roleId;
	private long winScore;
	private String nickname;
	private int qType;
	private int round;

}
