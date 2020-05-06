/**
 * 
 */
package com.zyhy.lhj_server.game.lll.test;

/**
 * @author linanjun
 * 视窗信息
 */
public class LllWindowInfo {
	
	// 轴Id
	private int id;
	// 轴位置
	private int index;
	
	private LllRollerInfo roller;
	
	

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



	public LllRollerInfo getRoller() {
		return roller;
	}



	public void setRoller(LllRollerInfo roller) {
		this.roller = roller;
	}



	@Override
	public String toString() {
		return "LllWindowInfo [id=" + id + ", index=" + index + ", roller=" + roller + "]";
	}
	
}
