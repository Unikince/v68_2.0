/**
 * 
 */
package com.zyhy.lhj_server.game.alsj;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum AlsjWinLineEnum implements Line{
	
	WR1(1, AlsjWindowEnum.A2, AlsjWindowEnum.B2, AlsjWindowEnum.C2, AlsjWindowEnum.D2, AlsjWindowEnum.E2),
	
	WR2(2, AlsjWindowEnum.A1, AlsjWindowEnum.B1, AlsjWindowEnum.C1, AlsjWindowEnum.D1, AlsjWindowEnum.E1),
	
	WR3(3, AlsjWindowEnum.A3, AlsjWindowEnum.B3, AlsjWindowEnum.C3, AlsjWindowEnum.D3, AlsjWindowEnum.E3),
	
	WR4(4, AlsjWindowEnum.A1, AlsjWindowEnum.B2, AlsjWindowEnum.C3, AlsjWindowEnum.D2, AlsjWindowEnum.E1),
	
	WR5(5, AlsjWindowEnum.A3, AlsjWindowEnum.B2, AlsjWindowEnum.C1, AlsjWindowEnum.D2, AlsjWindowEnum.E3),
	
	WR6(6, AlsjWindowEnum.A2, AlsjWindowEnum.B1, AlsjWindowEnum.C1, AlsjWindowEnum.D1, AlsjWindowEnum.E2),
	
	WR7(7, AlsjWindowEnum.A2, AlsjWindowEnum.B3, AlsjWindowEnum.C3, AlsjWindowEnum.D3, AlsjWindowEnum.E2),
	
	WR8(8, AlsjWindowEnum.A1, AlsjWindowEnum.B1, AlsjWindowEnum.C2, AlsjWindowEnum.D3, AlsjWindowEnum.E3),
	
	WR9(9, AlsjWindowEnum.A3, AlsjWindowEnum.B3, AlsjWindowEnum.C2, AlsjWindowEnum.D1, AlsjWindowEnum.E1);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private AlsjWinLineEnum(int id, Window... ws){
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
