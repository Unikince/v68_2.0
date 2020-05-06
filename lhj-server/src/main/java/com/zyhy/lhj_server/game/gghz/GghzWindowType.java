/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.lhj_server.game.Window;

/**
 * @author linanjun
 * 5轴9线老虎机视窗信息
 */
public enum GghzWindowType implements Window{

	A1(1, 1),
	A2(1, 2),
	A3(1, 3);
	
	// 轴Id
	private int id;
	// 轴位置
	private int index;
	private GghzWindowType(int id,int index){
		this.id = id;
		this.index = index;
	}
	/**
	 * 枚举转换成class
	 * @return
	 */
	public static List<GghzWindowInfo> infos(){
		List<GghzWindowInfo> ls = new ArrayList<GghzWindowInfo>();
		for(GghzWindowType t : values()) {
			ls.add(new GghzWindowInfo(t));
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
