/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.swk;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.swk.SwkScatterInfo;

/**
 * @author ASUS
 *
 */
public class SwkScatterModelResult extends HttpMessageResult{
	//免费游戏信息
	private SwkScatterInfo scatter;

	public SwkScatterInfo getScatter() {
		return scatter;
	}

	public void setScatter(SwkScatterInfo scatter) {
		this.scatter = scatter;
	}
}
