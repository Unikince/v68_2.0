/**
 * 
 */
package com.zyhy.lhj_server.game.swk;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum SwkWinLineEnum implements Line{
	
	WR1(1, SwkWindowEnum.A2, SwkWindowEnum.B2, SwkWindowEnum.C2, SwkWindowEnum.D2, SwkWindowEnum.E2),
	
	WR2(2, SwkWindowEnum.A1, SwkWindowEnum.B1, SwkWindowEnum.C1, SwkWindowEnum.D1, SwkWindowEnum.E1),
	
	WR3(3, SwkWindowEnum.A3, SwkWindowEnum.B3, SwkWindowEnum.C3, SwkWindowEnum.D3, SwkWindowEnum.E3),
	
	WR4(4, SwkWindowEnum.A1, SwkWindowEnum.B2, SwkWindowEnum.C3, SwkWindowEnum.D2, SwkWindowEnum.E1),
	
	WR5(5, SwkWindowEnum.A3, SwkWindowEnum.B2, SwkWindowEnum.C1, SwkWindowEnum.D2, SwkWindowEnum.E3),
	
	WR6(6, SwkWindowEnum.A2, SwkWindowEnum.B1, SwkWindowEnum.C1, SwkWindowEnum.D1, SwkWindowEnum.E2),
	
	WR7(7, SwkWindowEnum.A2, SwkWindowEnum.B3, SwkWindowEnum.C3, SwkWindowEnum.D3, SwkWindowEnum.E2),
	
	WR8(8, SwkWindowEnum.A1, SwkWindowEnum.B1, SwkWindowEnum.C2, SwkWindowEnum.D3, SwkWindowEnum.E3),
	
	WR9(9, SwkWindowEnum.A3, SwkWindowEnum.B3, SwkWindowEnum.C2, SwkWindowEnum.D1, SwkWindowEnum.E1),
	
	WR10(10, SwkWindowEnum.A2, SwkWindowEnum.B3, SwkWindowEnum.C2, SwkWindowEnum.D1, SwkWindowEnum.E2),
	
	WR11(11, SwkWindowEnum.A2, SwkWindowEnum.B1, SwkWindowEnum.C2, SwkWindowEnum.D3, SwkWindowEnum.E2),
	
	WR12(12, SwkWindowEnum.A1, SwkWindowEnum.B2, SwkWindowEnum.C2, SwkWindowEnum.D2, SwkWindowEnum.E1),
	
	WR13(13, SwkWindowEnum.A3, SwkWindowEnum.B2, SwkWindowEnum.C2, SwkWindowEnum.D2, SwkWindowEnum.E3),
	
	WR14(14, SwkWindowEnum.A1, SwkWindowEnum.B2, SwkWindowEnum.C1, SwkWindowEnum.D2, SwkWindowEnum.E1),
	
	WR15(15, SwkWindowEnum.A3, SwkWindowEnum.B2, SwkWindowEnum.C3, SwkWindowEnum.D2, SwkWindowEnum.E3);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private SwkWinLineEnum(int id, Window... ws){
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
