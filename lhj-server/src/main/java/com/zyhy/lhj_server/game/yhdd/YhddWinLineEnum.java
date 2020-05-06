/**
 * 
 */
package com.zyhy.lhj_server.game.yhdd;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum YhddWinLineEnum implements Line{
	
	WR1(1, YhddWindowEnum.A2, YhddWindowEnum.B2, YhddWindowEnum.C2, YhddWindowEnum.D2, YhddWindowEnum.E2),
	
	WR2(2, YhddWindowEnum.A1, YhddWindowEnum.B1, YhddWindowEnum.C1, YhddWindowEnum.D1, YhddWindowEnum.E1),
	
	WR3(3, YhddWindowEnum.A3, YhddWindowEnum.B3, YhddWindowEnum.C3, YhddWindowEnum.D3, YhddWindowEnum.E3),
	
	WR4(4, YhddWindowEnum.A1, YhddWindowEnum.B2, YhddWindowEnum.C3, YhddWindowEnum.D2, YhddWindowEnum.E1),
	
	WR5(5, YhddWindowEnum.A3, YhddWindowEnum.B2, YhddWindowEnum.C1, YhddWindowEnum.D2, YhddWindowEnum.E3),
	
	WR6(6, YhddWindowEnum.A2, YhddWindowEnum.B1, YhddWindowEnum.C1, YhddWindowEnum.D1, YhddWindowEnum.E2),
	
	WR7(7, YhddWindowEnum.A2, YhddWindowEnum.B3, YhddWindowEnum.C3, YhddWindowEnum.D3, YhddWindowEnum.E2),
	
	WR8(8, YhddWindowEnum.A1, YhddWindowEnum.B1, YhddWindowEnum.C2, YhddWindowEnum.D3, YhddWindowEnum.E3),
	
	WR9(9, YhddWindowEnum.A3, YhddWindowEnum.B3, YhddWindowEnum.C2, YhddWindowEnum.D1, YhddWindowEnum.E1),
	
	WR10(10, YhddWindowEnum.A2, YhddWindowEnum.B3, YhddWindowEnum.C2, YhddWindowEnum.D1, YhddWindowEnum.E2),
	
	WR11(11, YhddWindowEnum.A2, YhddWindowEnum.B1, YhddWindowEnum.C2, YhddWindowEnum.D3, YhddWindowEnum.E2),
	
	WR12(12, YhddWindowEnum.A1, YhddWindowEnum.B2, YhddWindowEnum.C2, YhddWindowEnum.D2, YhddWindowEnum.E1),
	
	WR13(13, YhddWindowEnum.A3, YhddWindowEnum.B2, YhddWindowEnum.C2, YhddWindowEnum.D2, YhddWindowEnum.E3),
	
	WR14(14, YhddWindowEnum.A1, YhddWindowEnum.B2, YhddWindowEnum.C1, YhddWindowEnum.D2, YhddWindowEnum.E1),
	
	WR15(15, YhddWindowEnum.A3, YhddWindowEnum.B2, YhddWindowEnum.C3, YhddWindowEnum.D2, YhddWindowEnum.E3),
	
	WR16(16, YhddWindowEnum.A2, YhddWindowEnum.B2, YhddWindowEnum.C1, YhddWindowEnum.D2, YhddWindowEnum.E2),
	
	WR17(17, YhddWindowEnum.A2, YhddWindowEnum.B2, YhddWindowEnum.C3, YhddWindowEnum.D2, YhddWindowEnum.E2),
	
	WR18(18, YhddWindowEnum.A1, YhddWindowEnum.B1, YhddWindowEnum.C3, YhddWindowEnum.D1, YhddWindowEnum.E1),
	
	WR19(19, YhddWindowEnum.A3, YhddWindowEnum.B3, YhddWindowEnum.C1, YhddWindowEnum.D3, YhddWindowEnum.E3),
	
	WR20(20, YhddWindowEnum.A3, YhddWindowEnum.B1, YhddWindowEnum.C1, YhddWindowEnum.D1, YhddWindowEnum.E3),
	;
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private YhddWinLineEnum(int id, Window... ws){
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
