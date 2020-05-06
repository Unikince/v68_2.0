/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.bqtp;

import java.util.List;

import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.bqtp.BqtpScatterInfo;

/**
 * @author linanjun
 * 游戏界面返回
 */
public class BqtpGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<BetLimitInfo> betlimitinfos;
	//视窗信息
	private List<List<Window>> windowinfos;
	//图案信息
	private List<IconInfo> zooIconInfos;
	//免费游戏信息
	private BqtpScatterInfo scatter;
	
	public List<BetLimitInfo> getBetlimitinfos() {
		return betlimitinfos;
	}

	public void setBetlimitinfos(List<BetLimitInfo> betlimitinfos) {
		this.betlimitinfos = betlimitinfos;
	}

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

	public BqtpScatterInfo getScatter() {
		return scatter;
	}

	public void setScatter(BqtpScatterInfo scatter) {
		this.scatter = scatter;
	}

	@Override
	public String toString() {
		return "BqtpGamePanlResult [betlimitinfos=" + betlimitinfos + ", windowinfos=" + windowinfos + ", zooIconInfos="
				+ zooIconInfos + ", scatter=" + scatter + "]";
	}
	
}
