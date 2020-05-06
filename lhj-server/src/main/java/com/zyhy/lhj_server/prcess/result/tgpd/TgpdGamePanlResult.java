/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.tgpd;

import java.util.List;

import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.tgpd.TgpdScatterInfo;

/**
 * 游戏界面返回
 */
public class TgpdGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<BetLimitInfo> betlimitinfos;
	//视窗信息
	private List<List<Window>> windowinfos;
	//图案信息
	private List<IconInfo> zooIconInfos;
	//免费游戏信息
	private TgpdScatterInfo scatter;
	// 游戏数据
	private TgpdGameBetResult result;

	public List<BetLimitInfo> getBetlimitinfos() {
		return betlimitinfos;
	}

	public void setBetlimitinfos(List<BetLimitInfo> betlimitinfos) {
		this.betlimitinfos = betlimitinfos;
	}

	public List<List<Window>> getWindowinfos() {
		return windowinfos;
	}

	public void setWindowinfos(List<List<Window>> list) {
		this.windowinfos = list;
	}

	public void setZooIconInfos(List<IconInfo> infos) {
		this.zooIconInfos = infos;
	}

	public List<IconInfo> getZooIconInfos() {
		return zooIconInfos;
	}

	public TgpdScatterInfo getScatter() {
		return scatter;
	}

	public void setScatter(TgpdScatterInfo scatter) {
		this.scatter = scatter;
	}

	public TgpdGameBetResult getResult() {
		return result;
	}

	public void setResult(TgpdGameBetResult result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "TgpdGamePanlResult [betlimitinfos=" + betlimitinfos + ", windowinfos=" + windowinfos + ", zooIconInfos="
				+ zooIconInfos + ", scatter=" + scatter + ", result=" + result + "]";
	}
	
}
