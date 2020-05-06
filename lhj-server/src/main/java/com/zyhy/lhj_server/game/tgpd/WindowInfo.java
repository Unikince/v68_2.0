/**
 * 
 */
package com.zyhy.lhj_server.game.tgpd;

import java.io.Serializable;

import com.zyhy.common_lhj.Window;

/**
 * @author linanjun
 * 视窗信息
 */
public class WindowInfo implements Window , Serializable{
	private static final long serialVersionUID = 1L;
	// 轴Id
	private int id;
	// 轴位置
	private int index;
	// 中奖个数
	private int num;
	// 中奖图标
	private TgpdIconEnum icon;
	// 中奖金额
	private double mul;
	
	public WindowInfo(Window s) {
		this.id = s.getId();
		this.index = s.getIndex();
	}
	
	public WindowInfo(int id, int index){
		this.id = id;
		this.index = index;
	}
	
	public WindowInfo(int id, int index, TgpdIconEnum icon){
		this.id = id;
		this.index = index;
		this.icon = icon;
	}
	
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

	public TgpdIconEnum getIcon() {
		return icon;
	}

	public void setIcon(TgpdIconEnum icon) {
		this.icon = icon;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public double getMul() {
		return mul;
	}

	public void setMul(double mul) {
		this.mul = mul;
	}

	@Override
	public String toString() {
		return "WindowInfo [id=" + id + ", index=" + index + ", num=" + num + ", icon=" + icon + ", mul=" + mul + "]";
	}
}
