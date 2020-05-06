/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

/**
 * @author ASUS
 *
 */
public class GghzRollerInfo {

	// 轴Id
	private int id;
	// 轴位置
	private int index;
	//icon
	private GghzIconEnum icon;
	
	public GghzRollerInfo(){}
	
	public GghzRollerInfo(int id, int index, GghzIconEnum icon){
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
	public GghzIconEnum getIcon() {
		return icon;
	}
	public void setIcon(GghzIconEnum icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "GghzRollerInfo [id=" + id + ", index=" + index + ", icon=" + icon + "]";
	}

}
