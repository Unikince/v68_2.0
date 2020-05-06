/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.xywjs;

import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author linanjun 猜大小返回
 */
public class XywjsGuessSizeResult extends HttpMessageResult {

	// 结果 0=游戏结束,1=游戏继续
	private int sizeresult;
	// 游戏币
	private double gamegold;
	// 奖池游戏币
	private double poolcoin;
	@Override
	public String toString() {
		return "GuessSizeResult [sizeresult=" + sizeresult + ", gamegold="
				+ gamegold + ", poolcoin=" + poolcoin + "]";
	}
	public double getPoolcoin() {
		return poolcoin;
	}
	public void setPoolcoin(double poolcoin) {
		this.poolcoin = poolcoin;
	}
	public int getSizeresult() {
		return sizeresult;
	}
	public void setSizeresult(int sizeresult) {
		this.sizeresult = sizeresult;
	}
	public double getGamegold() {
		return gamegold;
	}
	public void setGamegold(double gamegold) {
		this.gamegold = gamegold;
	}
}
