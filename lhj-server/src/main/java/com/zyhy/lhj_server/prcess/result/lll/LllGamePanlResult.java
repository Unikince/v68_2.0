/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.lll;

import java.util.List;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.lll.LllBetLimitInfo;
import com.zyhy.lhj_server.game.lll.LllIconInfo;
import com.zyhy.lhj_server.game.lll.LllWindowInfo;

/**
 * @author linanjun
 * 游戏界面返回
 */
public class LllGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<LllBetLimitInfo> betlimitinfos;
	//视窗信息
	private List<LllWindowInfo> windowinfos;
	//图案信息
	private List<LllIconInfo> zooLllIconInfos;

	public List<LllBetLimitInfo> getBetlimitinfos() {
		return betlimitinfos;
	}

	public void setBetlimitinfos(List<LllBetLimitInfo> betlimitinfos) {
		this.betlimitinfos = betlimitinfos;
	}

	public List<LllWindowInfo> getWindowinfos() {
		return windowinfos;
	}

	public void setWindowinfos(List<LllWindowInfo> windowinfos) {
		this.windowinfos = windowinfos;
	}

	public void setZooLllIconInfos(List<LllIconInfo> infos) {
		this.zooLllIconInfos = infos;
	}

	public List<LllIconInfo> getZooLllIconInfos() {
		return zooLllIconInfos;
	}
	
}
