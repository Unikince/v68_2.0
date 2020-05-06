/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.xywjs;


/**
 * @author linanjun
 * 辛运返回
 */
public class XywjsLuckyGameResult {
	// 转盘Id 8=lucky
	private int zhuanpanid;
	// 胜利分数
	private int winnumber;
	// 奖池游戏币
	private long poolcoin;
	@Override
	public String toString() {
		return "LuckyGameResult [zhuanpanid=" + zhuanpanid + ", winnumber="
				+ winnumber + ", poolcoin=" + poolcoin + "]";
	}
	public int getZhuanpanid() {
		return zhuanpanid;
	}
	public void setZhuanpanid(int zhuanpanid) {
		this.zhuanpanid = zhuanpanid;
	}
	public int getWinnumber() {
		return winnumber;
	}
	public void setWinnumber(int winnumber) {
		this.winnumber = winnumber;
	}
	public long getPoolcoin() {
		return poolcoin;
	}
	public void setPoolcoin(long poolcoin) {
		this.poolcoin = poolcoin;
	}
}
