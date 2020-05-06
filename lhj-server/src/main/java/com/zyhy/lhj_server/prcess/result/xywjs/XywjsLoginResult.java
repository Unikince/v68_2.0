/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.xywjs;

import java.util.Map;

import com.zyhy.common_lhj.Player;
import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author linanjun 登录返回信息
 */
public class XywjsLoginResult extends HttpMessageResult {

	// 奖池游戏币
	private double poolcoin;
	private String uuid;
	// sid
	private int sid;
	private double gold;
	private double exchangeRate; // 汇率
	private Map<Integer, Integer> oddsMap;
	private Player player;

	public double getPoolcoin() {
		return poolcoin;
	}

	public void setPoolcoin(double poolcoin) {
		this.poolcoin = poolcoin;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public double getGold() {
		return gold;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Map<Integer, Integer> getOddsMap() {
		return oddsMap;
	}

	public void setOddsMap(Map<Integer, Integer> oddsMap) {
		this.oddsMap = oddsMap;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "XywjsLoginResult [poolcoin=" + poolcoin + ", uuid=" + uuid + ", sid=" + sid + ", gold=" + gold
				+ ", exchangeRate=" + exchangeRate + ", oddsMap=" + oddsMap + ", player=" + player + "]";
	}




}
