/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.yzhx;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.yzhx.YzhxFreeInfo;

/**
 * @author ASUS
 *
 */
public class YzhxScatterModelResult extends HttpMessageResult{
	//免费游戏信息
	private YzhxFreeInfo bonus;

	public YzhxFreeInfo getScatter() {
		return bonus;
	}

	public void setScatter(YzhxFreeInfo bonus) {
		this.bonus = bonus;
	}
}
