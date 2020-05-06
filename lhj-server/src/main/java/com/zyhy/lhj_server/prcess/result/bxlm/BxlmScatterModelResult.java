/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.bxlm;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.bxlm.BxlmScatterInfo;

/**
 * @author ASUS
 *
 */
public class BxlmScatterModelResult extends HttpMessageResult{
	//免费游戏信息
	private BxlmScatterInfo scatter;
	//免费游戏次数
	private int ScatterNum;
	public BxlmScatterInfo getScatter() {
		return scatter;
	}
	public void setScatter(BxlmScatterInfo scatter) {
		this.scatter = scatter;
	}
	public int getScatterNum() {
		return ScatterNum;
	}
	public void setScatterNum(int scatterNum) {
		ScatterNum = scatterNum;
	}
	
}
