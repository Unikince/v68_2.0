/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.yzhx;

import java.util.List;

import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.yzhx.YzhxFreeInfo;

/**
 * 游戏界面返回
 */
public class YzhxGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<BetLimitInfo> betlimitinfos;
	//视窗信息
	private List<WindowInfo> windowinfos;
	//图案信息
	private List<IconInfo> zooIconInfos;
	//BONUS游戏信息
	private YzhxFreeInfo bonus;

	public List<BetLimitInfo> getBetlimitinfos() {
		return betlimitinfos;
	}

	public void setBetlimitinfos(List<BetLimitInfo> betlimitinfos) {
		this.betlimitinfos = betlimitinfos;
	}

	public List<WindowInfo> getWindowinfos() {
		return windowinfos;
	}

	public void setWindowinfos(List<WindowInfo> windowinfos) {
		this.windowinfos = windowinfos;
	}

	public void setZooIconInfos(List<IconInfo> infos) {
		this.zooIconInfos = infos;
	}

	public List<IconInfo> getZooIconInfos() {
		return zooIconInfos;
	}

	public YzhxFreeInfo getBonus() {
		return bonus;
	}

	public void setBonus(YzhxFreeInfo bonus) {
		this.bonus = bonus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GamePanlResult [betlimitinfos=").append(betlimitinfos)
				.append(", windowinfos=").append(windowinfos)
				.append(", zooIconInfos=").append(zooIconInfos)
				.append(", bonus=").append(bonus).append("]");
		return builder.toString();
	}

	
}
