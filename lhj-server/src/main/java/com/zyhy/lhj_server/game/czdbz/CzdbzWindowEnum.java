/**
 * 
 */
package com.zyhy.lhj_server.game.czdbz;

import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Window;

/**
 * @author linanjun
 * 5轴9线老虎机视窗信息
 */
public enum CzdbzWindowEnum implements Window{

	A1(1, 1),
	A2(1, 2),
	A3(1, 3),
	B1(2, 1),
	B2(2, 2),
	B3(2, 3),
	C1(3, 1),
	C2(3, 2),
	C3(3, 3),
	D1(4, 1),
	D2(4, 2),
	D3(4, 3),
	E1(5, 1),
	E2(5, 2),
	E3(5, 3);
	
	// 轴Id
	private int id;
	// 轴位置
	private int index;
	private CzdbzWindowEnum(int id,int index){
		this.id = id;
		this.index = index;
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

	@Override
	public Icon getIcon() {
		return null;
	}

}
