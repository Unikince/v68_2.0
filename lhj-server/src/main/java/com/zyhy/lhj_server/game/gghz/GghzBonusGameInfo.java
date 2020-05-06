/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

import java.util.List;

/**
 * @author linanjun
 * 小游戏信息
 */
public class GghzBonusGameInfo {

	// 唯一ID
	private String uuid;
	// 下注游戏币
	private int betcoin;
	// 翻牌次数
	private int initnum;
	// 剩余牌
	private List<String> currentnum;

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getBetcoin() {
		return betcoin;
	}
	public void setBetcoin(int betcoin) {
		this.betcoin = betcoin;
	}
	public int getInitnum() {
		return initnum;
	}
	public void setInitnum(int initnum) {
		this.initnum = initnum;
	}
	public List<String> getCurrentnum() {
		return currentnum;
	}
	public void setCurrentnum(List<String> currentnum) {
		this.currentnum = currentnum;
	}
}
