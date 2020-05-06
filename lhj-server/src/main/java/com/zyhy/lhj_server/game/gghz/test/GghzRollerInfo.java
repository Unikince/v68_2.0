/**
 * 
 */
package com.zyhy.lhj_server.game.gghz.test;

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
	private String icon;
	
	public GghzRollerInfo(){}
	

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


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	@Override
	public String toString() {
		return "GghzRollerInfo [id=" + id + ", index=" + index + ", icon=" + icon + "]";
	}

}
