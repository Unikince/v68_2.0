package com.dmg.bcbm.logic.entity.bcbm;

import lombok.Data;

@Data
public class PlayerRecordDTO {
	// 类型
	private int car;
	// 名称
	private String name;
	// 赔率
	private int lv;
	// 本局下注
	private double bet;
	// 本局输赢
	private double winLoseGold;
	// 是否为中奖车辆 0不是,1是
	private int winCar;
	// 本局庄家名字
	private String bankerName;
}
