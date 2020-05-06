/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.alsj;

import java.util.List;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.alsj.AlsjBonusInfo;
import com.zyhy.lhj_server.game.alsj.AlsjScatterInfo;

/**
 * @author linanjun
 * 游戏界面返回
 */
public class AlsjGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<BetLimitInfo> betlimitinfos;
	//视窗信息
	private List<WindowInfo> windowinfos;
	//图案信息
	private List<IconInfo> zooIconInfos;
	//bonus信息
	private AlsjBonusInfo bonusInfo;
	//免费游戏信息
	private AlsjScatterInfo scatterInfo;
	//下注信息
	private BetInfo betInfo;

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

	public AlsjBonusInfo getAlsjBonusInfo() {
		return bonusInfo;
	}

	public void setAlsjBonusInfo(AlsjBonusInfo bonusInfo) {
		this.bonusInfo = bonusInfo;
	}

	public BetInfo getBetInfo() {
		return betInfo;
	}

	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}

	public AlsjBonusInfo getBonusInfo() {
		return bonusInfo;
	}

	public void setBonusInfo(AlsjBonusInfo bonusInfo) {
		this.bonusInfo = bonusInfo;
	}

	public AlsjScatterInfo getScatterInfo() {
		return scatterInfo;
	}

	public void setScatterInfo(AlsjScatterInfo scatterInfo) {
		this.scatterInfo = scatterInfo;
	}

	@Override
	public String toString() {
		return "AlsjGamePanlResult [betlimitinfos=" + betlimitinfos + ", windowinfos=" + windowinfos + ", zooIconInfos="
				+ zooIconInfos + ", bonusInfo=" + bonusInfo + ", scatterInfo=" + scatterInfo + ", betInfo=" + betInfo
				+ "]";
	}

	
}
