package com.zyhy.lhj_server.game.xywjs;

import java.util.List;

import lombok.Data;

@Data
public class PlayerRecord {
	// 游戏类型(1:正常游戏2:猜大小)
	private int gameType;
	// 获胜的图标信息
	private List<WinIconInfo> winIconInfoList;
	// 图标的输赢信息
	private List<WinIconInfo> winLoseInfoList;
}
