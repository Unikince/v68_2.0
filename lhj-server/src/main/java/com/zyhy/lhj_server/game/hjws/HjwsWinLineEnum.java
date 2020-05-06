/**
 * 
 */
package com.zyhy.lhj_server.game.hjws;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum HjwsWinLineEnum implements Line{
	
	WR1(1, HjwsWindowEnum.A2, HjwsWindowEnum.B2, HjwsWindowEnum.C2, HjwsWindowEnum.D2, HjwsWindowEnum.E2),
	
	WR2(2, HjwsWindowEnum.A1, HjwsWindowEnum.B1, HjwsWindowEnum.C1, HjwsWindowEnum.D1, HjwsWindowEnum.E1),
	
	WR3(3, HjwsWindowEnum.A3, HjwsWindowEnum.B3, HjwsWindowEnum.C3, HjwsWindowEnum.D3, HjwsWindowEnum.E3),
	
	WR4(4, HjwsWindowEnum.A1, HjwsWindowEnum.B2, HjwsWindowEnum.C3, HjwsWindowEnum.D2, HjwsWindowEnum.E1),
	
	WR5(5, HjwsWindowEnum.A3, HjwsWindowEnum.B2, HjwsWindowEnum.C1, HjwsWindowEnum.D2, HjwsWindowEnum.E3),
	
	WR6(6, HjwsWindowEnum.A1, HjwsWindowEnum.B1, HjwsWindowEnum.C2, HjwsWindowEnum.D1, HjwsWindowEnum.E1),
	
	WR7(7, HjwsWindowEnum.A3, HjwsWindowEnum.B3, HjwsWindowEnum.C2, HjwsWindowEnum.D3, HjwsWindowEnum.E3),
	
	WR8(8, HjwsWindowEnum.A2, HjwsWindowEnum.B1, HjwsWindowEnum.C1, HjwsWindowEnum.D1, HjwsWindowEnum.E2),
	
	WR9(9, HjwsWindowEnum.A2, HjwsWindowEnum.B3, HjwsWindowEnum.C3, HjwsWindowEnum.D3, HjwsWindowEnum.E2);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private HjwsWinLineEnum(int id, Window... ws){
		this.id = id;
		this.windows = ws;
	}
	
	public int getId() {
		return id;
	}
	
	public Window[] getWindows() {
		return windows;
	}
}
