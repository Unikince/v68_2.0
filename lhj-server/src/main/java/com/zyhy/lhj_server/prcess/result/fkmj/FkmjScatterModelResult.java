/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.fkmj;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.fkmj.FkmjBonusInfo;

/**
 * @author ASUS
 *
 */
public class FkmjScatterModelResult extends HttpMessageResult{
	//免费游戏信息
	private FkmjBonusInfo bonus;

	public FkmjBonusInfo getScatter() {
		return bonus;
	}

	public void setScatter(FkmjBonusInfo bonus) {
		this.bonus = bonus;
	}
}
