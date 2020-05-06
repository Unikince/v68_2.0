/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.lhj_server.game.Window;

/**
 * @author linanjun
 * 5轴9线老虎机视窗信息
 */
public enum LllWindowType implements Window{

	A1(1, 1),
	A2(1, 2),
	A3(1, 3);
	
	// 轴Id
	private int id;
	// 轴位置
	private int index;
	private LllWindowType(int id,int index){
		this.id = id;
		this.index = index;
	}
	/**
	 * 枚举转换成class
	 * @return
	 */
	public static List<LllWindowInfo> infos(){
		List<LllWindowInfo> ls = new ArrayList<LllWindowInfo>();
		for(LllWindowType t : values()) {
			ls.add(new LllWindowInfo(t));
		}
		return ls;
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
}
