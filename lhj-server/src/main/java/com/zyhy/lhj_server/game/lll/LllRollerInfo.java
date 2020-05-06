/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

/**
 * @author ASUS
 *
 */
public class LllRollerInfo {

	// 轴Id
	private int id;
	// 轴位置
	private int index;
	//icon
	private LllIconEnum icon;
	
	public LllRollerInfo(){}
	
	public LllRollerInfo(int id, int index, LllIconEnum icon){
		this.id = id;
		this.index =index;
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
	public LllIconEnum getIcon() {
		return icon;
	}
	public void setIcon(LllIconEnum icon) {
		this.icon = icon;
	}

}
