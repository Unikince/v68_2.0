/**
 * 
 */
package com.zyhy.lhj_server.test;

import java.io.Serializable;

/**
 * @author linanjun
 * 视窗信息
 */
public class WindowInfo {
	// 轴Id
	private int id;
	// 轴位置
	private int index;
	// 是否中奖
	//private boolean isWin;
	// 中奖个数
	private int num;
	// 中奖图标
	private String icon;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Override
	public String toString() {
		return "WindowInfo [id=" + id + ", index=" + index + ", num=" + num + ", icon=" + icon + "]";
	}
	
	
}
