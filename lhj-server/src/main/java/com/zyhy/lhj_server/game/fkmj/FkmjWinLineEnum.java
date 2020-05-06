/**
 * 
 */
package com.zyhy.lhj_server.game.fkmj;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * 5轴中奖线路信息
 */
public enum FkmjWinLineEnum implements Line{
	
	WR1(1, FkmjWindowEnum.A2, FkmjWindowEnum.B2, FkmjWindowEnum.C2, FkmjWindowEnum.D2, FkmjWindowEnum.E2),
	
	WR2(2, FkmjWindowEnum.A1, FkmjWindowEnum.B1, FkmjWindowEnum.C1, FkmjWindowEnum.D1, FkmjWindowEnum.E1),
	
	WR3(3, FkmjWindowEnum.A3, FkmjWindowEnum.B3, FkmjWindowEnum.C3, FkmjWindowEnum.D3, FkmjWindowEnum.E3),
	
	WR4(4, FkmjWindowEnum.A1, FkmjWindowEnum.B2, FkmjWindowEnum.C3, FkmjWindowEnum.D2, FkmjWindowEnum.E1),
	
	WR5(5, FkmjWindowEnum.A3, FkmjWindowEnum.B2, FkmjWindowEnum.C1, FkmjWindowEnum.D2, FkmjWindowEnum.E3),
	
	WR6(6, FkmjWindowEnum.A1, FkmjWindowEnum.B1, FkmjWindowEnum.C2, FkmjWindowEnum.D3, FkmjWindowEnum.E3),
	
	WR7(7, FkmjWindowEnum.A3, FkmjWindowEnum.B3, FkmjWindowEnum.C2, FkmjWindowEnum.D1, FkmjWindowEnum.E1),
	
	WR8(8, FkmjWindowEnum.A2, FkmjWindowEnum.B1, FkmjWindowEnum.C2, FkmjWindowEnum.D3, FkmjWindowEnum.E2),
	
	WR9(9, FkmjWindowEnum.A2, FkmjWindowEnum.B3, FkmjWindowEnum.C2, FkmjWindowEnum.D1, FkmjWindowEnum.E2),
	
	WR10(10, FkmjWindowEnum.A2, FkmjWindowEnum.B1, FkmjWindowEnum.C1, FkmjWindowEnum.D1, FkmjWindowEnum.E1),
	
	WR11(11, FkmjWindowEnum.A2, FkmjWindowEnum.B3, FkmjWindowEnum.C3, FkmjWindowEnum.D3, FkmjWindowEnum.E3),
	
	WR12(12, FkmjWindowEnum.A1, FkmjWindowEnum.B2, FkmjWindowEnum.C2, FkmjWindowEnum.D2, FkmjWindowEnum.E2),
	
	WR13(13, FkmjWindowEnum.A3, FkmjWindowEnum.B2, FkmjWindowEnum.C2, FkmjWindowEnum.D2, FkmjWindowEnum.E2),
	
	WR14(14, FkmjWindowEnum.A1, FkmjWindowEnum.B2, FkmjWindowEnum.C1, FkmjWindowEnum.D2, FkmjWindowEnum.E1),
	
	WR15(15, FkmjWindowEnum.A3, FkmjWindowEnum.B2, FkmjWindowEnum.C3, FkmjWindowEnum.D2, FkmjWindowEnum.E3),
	
	WR16(16, FkmjWindowEnum.A2, FkmjWindowEnum.B2, FkmjWindowEnum.C1, FkmjWindowEnum.D2, FkmjWindowEnum.E2),
	
	WR17(17, FkmjWindowEnum.A2, FkmjWindowEnum.B2, FkmjWindowEnum.C3, FkmjWindowEnum.D2, FkmjWindowEnum.E2),
	
	WR18(18, FkmjWindowEnum.A1, FkmjWindowEnum.B1, FkmjWindowEnum.C3, FkmjWindowEnum.D1, FkmjWindowEnum.E1),
	
	WR19(19, FkmjWindowEnum.A3, FkmjWindowEnum.B3, FkmjWindowEnum.C1, FkmjWindowEnum.D3, FkmjWindowEnum.E3),
	
	WR20(20, FkmjWindowEnum.A1, FkmjWindowEnum.B3, FkmjWindowEnum.C1, FkmjWindowEnum.D3, FkmjWindowEnum.E1),
	
	WR21(21, FkmjWindowEnum.A3, FkmjWindowEnum.B1, FkmjWindowEnum.C3, FkmjWindowEnum.D1, FkmjWindowEnum.E3),
	
	WR22(22, FkmjWindowEnum.A2, FkmjWindowEnum.B1, FkmjWindowEnum.C3, FkmjWindowEnum.D3, FkmjWindowEnum.E3),
	
	WR23(23, FkmjWindowEnum.A2, FkmjWindowEnum.B3, FkmjWindowEnum.C1, FkmjWindowEnum.D1, FkmjWindowEnum.E1),
	
	WR24(24, FkmjWindowEnum.A1, FkmjWindowEnum.B3, FkmjWindowEnum.C3, FkmjWindowEnum.D3, FkmjWindowEnum.E2),
	
	WR25(25, FkmjWindowEnum.A3, FkmjWindowEnum.B1, FkmjWindowEnum.C1, FkmjWindowEnum.D1, FkmjWindowEnum.E2);
	
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private FkmjWinLineEnum(int id, Window... ws){
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
