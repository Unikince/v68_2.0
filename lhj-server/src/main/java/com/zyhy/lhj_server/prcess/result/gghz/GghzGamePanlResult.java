/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.gghz;

import java.util.List;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.gghz.GghzBetLimitInfo;
import com.zyhy.lhj_server.game.gghz.GghzIconInfo;
import com.zyhy.lhj_server.game.gghz.GghzWindowInfo;

/**
 * @author linanjun
 * 游戏界面返回
 */
public class GghzGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<GghzBetLimitInfo> betlimitinfos;
	//视窗信息
	private List<GghzWindowInfo> windowinfos;
	//图案信息
	private List<GghzIconInfo> zooIconInfos;

	public List<GghzBetLimitInfo> getBetlimitinfos() {
		return betlimitinfos;
	}

	public void setBetlimitinfos(List<GghzBetLimitInfo> betlimitinfos) {
		this.betlimitinfos = betlimitinfos;
	}

	public List<GghzWindowInfo> getWindowinfos() {
		return windowinfos;
	}

	public void setWindowinfos(List<GghzWindowInfo> windowinfos) {
		this.windowinfos = windowinfos;
	}

	public void setZooIconInfos(List<GghzIconInfo> infos) {
		this.zooIconInfos = infos;
	}

	public List<GghzIconInfo> getZooIconInfos() {
		return zooIconInfos;
	}
	
}
