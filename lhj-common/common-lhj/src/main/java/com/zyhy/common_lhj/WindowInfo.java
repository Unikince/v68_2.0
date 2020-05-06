/**
 * 
 */
package com.zyhy.common_lhj;

import java.io.Serializable;

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
	// 是否中奖
	//private boolean isWin;
	// 中奖个数
	private int num;
	// 中奖图标
	private Icon icon;
	
	public WindowInfo(Window s) {
		this.id = s.getId();
		this.index = s.getIndex();
	}
	
	public WindowInfo(int id, int index){
		this.id = id;
		this.index = index;
	}
	
	public WindowInfo(int id, int index, Icon icon){
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

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	/*public boolean isWin() {
		return isWin;
	}

	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}*/

	@Override
	public String toString() {
		return "WindowInfo [id=" + id + ", index=" + index +  ", num=" + num + ", icon=" + icon
				+ "]";
	}
	
}
