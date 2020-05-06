package com.dmg.niuniuserver.model.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Robot extends Player implements Serializable {
	private static final long serialVersionUID = 5471648531675394414L;

	/****************机器人特性********************/

	/**
	 * 更新机器人或者私人场游戏币,承接大厅数据同步到游戏服务器
	 * @param newgold
	 */
	public void updateRobotAndPrivateField(double newgold){
		this.gold = newgold < 0 ? 0 : newgold;
	}

	private boolean win;
	
}
