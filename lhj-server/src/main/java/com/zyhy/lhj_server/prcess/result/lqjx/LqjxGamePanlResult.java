/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.lqjx;

import java.util.List;

import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.lqjx.LqjxScatterInfo;

/**
 * @author linanjun
 * 游戏界面返回
 */
public class LqjxGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<BetLimitInfo> betlimitinfos;
	//视窗信息
	private List<List<Window>> windowinfos;
	//图案信息
	private List<IconInfo> zooIconInfos;
	//免费游戏信息
	private LqjxScatterInfo scatter;
	
	public List<BetLimitInfo> getBetlimitinfos() {
		return betlimitinfos;
	}

	public void setBetlimitinfos(List<BetLimitInfo> betlimitinfos) {
		this.betlimitinfos = betlimitinfos;
	}

//	public List<WindowInfo> getWindowinfos() {
//		return windowinfos;
//	}
//
//	public void setWindowinfos(List<WindowInfo> windowinfos) {
//		this.windowinfos = windowinfos;
//	}

	public void setZooIconInfos(List<IconInfo> infos) {
		this.zooIconInfos = infos;
	}

	public List<List<Window>> getWindowinfos() {
		return windowinfos;
	}

	public void setWindowinfos(List<List<Window>> windowinfos) {
		this.windowinfos = windowinfos;
	}

	public List<IconInfo> getZooIconInfos() {
		return zooIconInfos;
	}

	public LqjxScatterInfo getScatter() {
		return scatter;
	}

	public void setScatter(LqjxScatterInfo scatter) {
		this.scatter = scatter;
	}

	@Override
	public String toString() {
		return "LqjxGamePanlResult [betlimitinfos=" + betlimitinfos
				+ ", windowinfos=" + windowinfos + ", zooIconInfos="
				+ zooIconInfos + ", scatter=" + scatter + "]";
	}
	
}
