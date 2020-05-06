/**
 * 
 */
package com.zyhy.common_lhj;

/**
 * @author ASUS
 *
 */
public class RollerInfo {

	// 轴Id
	private int id;
	// 轴位置
	private int index;
	//icon
	private Icon icon;
	
	public RollerInfo(){}
	
	public RollerInfo(int id, int index, Icon icon){
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
	public Icon getIcon() {
		return icon;
	}
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

}
