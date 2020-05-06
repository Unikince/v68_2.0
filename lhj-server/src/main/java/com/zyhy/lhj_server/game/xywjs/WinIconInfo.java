package com.zyhy.lhj_server.game.xywjs;

import lombok.Data;

@Data
public class WinIconInfo {
	// 图标的下注id(模式为猜大小时 1:大0:小)
	private int id;
	// 名称
	private String name;
	// 赔率
	private int lv;
	// 本局下注
	private double bet;
	// 本局输赢
	private double winLoseGold;
}
