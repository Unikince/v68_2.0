/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

import com.zyhy.lhj_server.game.Window;

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
	
	public LllWindowInfo(Window s) {
		this.id = s.getId();
		this.index = s.getIndex();
	}
	
	public LllWindowInfo(int id, int index){
		this.id = id;
		this.index = index;
	}
	
	public LllWindowInfo(int id, int index, LllRollerInfo r){
		this.id = id;
		this.index = index;
		this.roller = r;
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

	public LllRollerInfo getRoller() {
		return roller;
	}

	public void setRoller(LllRollerInfo roller) {
		this.roller = roller;
	}
	
}
