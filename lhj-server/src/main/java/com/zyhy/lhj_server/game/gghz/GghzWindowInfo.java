/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

import com.zyhy.lhj_server.game.Window;

/**
 * @author linanjun
 * 视窗信息
 */
public class GghzWindowInfo {
	
	// 轴Id
	private int id;
	// 轴位置
	private int index;
	
	private GghzRollerInfo roller;
	
	public GghzWindowInfo(Window s) {
		this.id = s.getId();
		this.index = s.getIndex();
	}
	
	public GghzWindowInfo(int id, int index){
		this.id = id;
		this.index = index;
	}
	
	public GghzWindowInfo(int id, int index, GghzRollerInfo r){
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

	public GghzRollerInfo getRoller() {
		return roller;
	}

	public void setRoller(GghzRollerInfo roller) {
		this.roller = roller;
	}

	@Override
	public String toString() {
		return "GghzWindowInfo [id=" + id + ", index=" + index + ", roller=" + roller + "]";
	}
	
	
}
