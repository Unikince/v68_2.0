/**
 * 
 */
package com.zyhy.lhj_server.game.fkmj;

import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Window;

/**
 * @author linanjun
 * 视窗信息
 */
public class FkmjWindowInfo implements Window{
	// 轴Id
	private int id;
	// 轴位置
	private int index;
	// 中奖个数
	private int num;
	// 图标
	private Icon icon;
	// 是否为中奖图标
	private boolean isWinIcon;
	
	public FkmjWindowInfo(Window s) {
		this.id = s.getId();
		this.index = s.getIndex();
	}
	
	public FkmjWindowInfo(int id, int index){
		this.id = id;
		this.index = index;
	}
	
	public FkmjWindowInfo(int id, int index, Icon icon){
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

	public boolean isWinIcon() {
		return isWinIcon;
	}

	public void setWinIcon(boolean isWinIcon) {
		this.isWinIcon = isWinIcon;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WindowInfo [id=").append(id).append(", index=")
				.append(index).append(", num=").append(num).append(", icon=")
				.append(icon).append(", isWinIcon=").append(isWinIcon).append("]");
		return builder.toString();
	}
	
}
