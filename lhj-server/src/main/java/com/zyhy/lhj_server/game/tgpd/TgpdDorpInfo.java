package com.zyhy.lhj_server.game.tgpd;

import java.util.ArrayList;
import java.util.List;

public class TgpdDorpInfo {
	// 图标窗口信息
	public List<List<WindowInfo>> windowInfo;
	// 掉落图标信息
	public List<List<WindowInfo>> dropWindowInfo;
	// 胜利路线信息
	public List<List<WindowInfo>> winInfos;
	public List<List<WindowInfo>> getWindowInfo() {
		return windowInfo;
	}
	public void setWindowInfo(List<List<WindowInfo>> windowInfo) {
		this.windowInfo = windowInfo;
	}
	public List<List<WindowInfo>> getDropWindowInfo() {
		return dropWindowInfo;
	}
	public void setDropWindowInfo(List<List<WindowInfo>> dropWindowInfo) {
		this.dropWindowInfo = dropWindowInfo;
	}
	public List<List<WindowInfo>> getWinInfos() {
		return winInfos;
	}
	public void setWinInfos(List<List<WindowInfo>> winInfos) {
		this.winInfos = winInfos;
	}
	
	@Override
	public String toString() {
		return "TgpdDorpInfo [windowInfo=" + windowInfo + ", dropWindowInfo=" + dropWindowInfo + ", winInfos="
				+ winInfos + "]";
	}
	
}
