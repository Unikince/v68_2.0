/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.hjws;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.hjws.HjwsScatterInfo;

/**
 * @author ASUS
 *
 */
public class HjwsScatterModelResult extends HttpMessageResult{
	//免费游戏信息
	private HjwsScatterInfo scatter;

	public HjwsScatterInfo getScatter() {
		return scatter;
	}

	public void setScatter(HjwsScatterInfo scatter) {
		this.scatter = scatter;
	}
}
