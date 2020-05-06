/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.swk;

import java.util.List;

import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.swk.SwkScatterInfo;

/**
 * @author linanjun
 * 游戏界面返回
 */
public class SwkGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<BetLimitInfo> betlimitinfos;
	//视窗信息
	private List<WindowInfo> windowinfos;
	//图案信息
	private List<IconInfo> zooIconInfos;
	
	//免费游戏信息
	private SwkScatterInfo scatter;

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

	public SwkScatterInfo getScatter() {
		return scatter;
	}

	public void setScatter(SwkScatterInfo scatter) {
		this.scatter = scatter;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SwkGamePanlResult [betlimitinfos=").append(betlimitinfos)
				.append(", windowinfos=").append(windowinfos)
				.append(", zooIconInfos=").append(zooIconInfos)
				.append(", scatter=").append(scatter).append("]");
		return builder.toString();
	}
	
}
