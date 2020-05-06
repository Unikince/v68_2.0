/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.nnyy;

import java.util.List;

import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.pool.DragonGameData;
import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author linanjun
 * 游戏界面返回
 */
public class NnyyGamePanlResult extends HttpMessageResult{

	//投注信息
	private List<BetLimitInfo> betlimitinfos;
	//视窗信息
	private List<WindowInfo> windowinfos;
	//图案信息
	private List<IconInfo> zooIconInfos;
	//奖池游戏信息
	private DragonGameData dragonGameData;

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

	public DragonGameData getDragonGameData() {
		return dragonGameData;
	}

	public void setDragonGameData(DragonGameData dragonGameData) {
		this.dragonGameData = dragonGameData;
	}

	@Override
	public String toString() {
		return "NnyyGamePanlResult [betlimitinfos=" + betlimitinfos
				+ ", windowinfos=" + windowinfos + ", zooIconInfos="
				+ zooIconInfos + ", dragonGameData=" + dragonGameData + "]";
	}
	
}
