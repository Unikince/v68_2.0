/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.xywjs;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author linanjun 开始游戏返回
 */
public class XywjsBeginGameResult extends HttpMessageResult {

	// 转盘Id 8=lucky
	private int zhuanpanid;
	// 胜利分数
	private double winnumber;
	// lucky奖励模式
	private int luckyModel;
	// lucky返回
	private List<XywjsLuckyGameResult> luckyresults = new ArrayList<>();
	// 奖池游戏币
	private double poolcoin;
	// 用户游戏币
	private double gold;
	
	//TODO 测试用字段
	// 当前赔率
	private double odds;
	// 当前使用图标
	private int icon;

	public int getZhuanpanid() {
		return zhuanpanid;
	}

	public void setZhuanpanid(int zhuanpanid) {
		this.zhuanpanid = zhuanpanid;
	}

	public double getWinnumber() {
		return winnumber;
	}

	public void setWinnumber(double winnumber) {
		this.winnumber = winnumber;
	}

	public int getLuckyModel() {
		return luckyModel;
	}

	public void setLuckyModel(int luckyModel) {
		this.luckyModel = luckyModel;
	}

	public List<XywjsLuckyGameResult> getLuckyresults() {
		return luckyresults;
	}

	public void setLuckyresults(List<XywjsLuckyGameResult> luckyresults) {
		this.luckyresults = luckyresults;
	}

	public double getPoolcoin() {
		return poolcoin;
	}

	public void setPoolcoin(double poolcoin) {
		this.poolcoin = poolcoin;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public double getOdds() {
		return odds;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "XywjsBeginGameResult [zhuanpanid=" + zhuanpanid + ", winnumber=" + winnumber + ", luckyModel="
				+ luckyModel + ", luckyresults=" + luckyresults + ", poolcoin=" + poolcoin + ", gold=" + gold
				+ ", odds=" + odds + ", icon=" + icon + "]";
	}

}
