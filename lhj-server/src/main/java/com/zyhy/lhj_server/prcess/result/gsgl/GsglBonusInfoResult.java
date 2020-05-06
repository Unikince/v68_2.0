/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.gsgl;

import java.util.List;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author ASUS
 *
 */
public class GsglBonusInfoResult extends HttpMessageResult{
	
	private BetInfo betInfo;
	
	private List<Window> ws;
	
	private int num;

	public BetInfo getBetInfo() {
		return betInfo;
	}

	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<Window> getWs() {
		return ws;
	}

	public void setWs(List<Window> ws) {
		this.ws = ws;
	}
	
}
