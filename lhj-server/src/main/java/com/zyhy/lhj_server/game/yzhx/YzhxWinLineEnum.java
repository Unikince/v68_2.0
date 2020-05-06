/**
 * 
 */
package com.zyhy.lhj_server.game.yzhx;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * 5轴中奖线路信息
 */
public enum YzhxWinLineEnum implements Line{
	
	WR1(1, YzhxWindowEnum.A2, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E2),
	
	WR2(2, YzhxWindowEnum.A1, YzhxWindowEnum.B1, YzhxWindowEnum.C1, YzhxWindowEnum.D1, YzhxWindowEnum.E1),
	
	WR3(3, YzhxWindowEnum.A3, YzhxWindowEnum.B3, YzhxWindowEnum.C3, YzhxWindowEnum.D3, YzhxWindowEnum.E3),
	
	WR4(4, YzhxWindowEnum.A2, YzhxWindowEnum.B1, YzhxWindowEnum.C2, YzhxWindowEnum.D1, YzhxWindowEnum.E2),
	
	WR5(5, YzhxWindowEnum.A2, YzhxWindowEnum.B3, YzhxWindowEnum.C2, YzhxWindowEnum.D3, YzhxWindowEnum.E2),
	
	WR6(6, YzhxWindowEnum.A1, YzhxWindowEnum.B2, YzhxWindowEnum.C3, YzhxWindowEnum.D2, YzhxWindowEnum.E1),
	
	WR7(7, YzhxWindowEnum.A3, YzhxWindowEnum.B2, YzhxWindowEnum.C1, YzhxWindowEnum.D2, YzhxWindowEnum.E3),
	
	WR8(8, YzhxWindowEnum.A1, YzhxWindowEnum.B2, YzhxWindowEnum.C1, YzhxWindowEnum.D2, YzhxWindowEnum.E1),
	
	WR9(9, YzhxWindowEnum.A3, YzhxWindowEnum.B2, YzhxWindowEnum.C3, YzhxWindowEnum.D2, YzhxWindowEnum.E3),
	
	WR10(10, YzhxWindowEnum.A2, YzhxWindowEnum.B1, YzhxWindowEnum.C3, YzhxWindowEnum.D1, YzhxWindowEnum.E2),
	
	WR11(11, YzhxWindowEnum.A2, YzhxWindowEnum.B3, YzhxWindowEnum.C1, YzhxWindowEnum.D3, YzhxWindowEnum.E2),
	
	WR12(12, YzhxWindowEnum.A1, YzhxWindowEnum.B3, YzhxWindowEnum.C1, YzhxWindowEnum.D3, YzhxWindowEnum.E1),
	
	WR13(13, YzhxWindowEnum.A3, YzhxWindowEnum.B1, YzhxWindowEnum.C3, YzhxWindowEnum.D1, YzhxWindowEnum.E3),
	
	WR14(14, YzhxWindowEnum.A1, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E1),
	
	WR15(15, YzhxWindowEnum.A3, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E3),
	
	WR16(16, YzhxWindowEnum.A2, YzhxWindowEnum.B2, YzhxWindowEnum.C1, YzhxWindowEnum.D2, YzhxWindowEnum.E2),
	
	WR17(17, YzhxWindowEnum.A2, YzhxWindowEnum.B2, YzhxWindowEnum.C3, YzhxWindowEnum.D2, YzhxWindowEnum.E2),
	
	WR18(18, YzhxWindowEnum.A1, YzhxWindowEnum.B3, YzhxWindowEnum.C2, YzhxWindowEnum.D3, YzhxWindowEnum.E1),
	
	WR19(19, YzhxWindowEnum.A3, YzhxWindowEnum.B1, YzhxWindowEnum.C2, YzhxWindowEnum.D1, YzhxWindowEnum.E3),
	
	WR20(20, YzhxWindowEnum.A2, YzhxWindowEnum.B1, YzhxWindowEnum.C1, YzhxWindowEnum.D1, YzhxWindowEnum.E2),
	
	WR21(21, YzhxWindowEnum.A2, YzhxWindowEnum.B3, YzhxWindowEnum.C3, YzhxWindowEnum.D3, YzhxWindowEnum.E2),
	
	WR22(22, YzhxWindowEnum.A1, YzhxWindowEnum.B1, YzhxWindowEnum.C3, YzhxWindowEnum.D1, YzhxWindowEnum.E1),
	
	WR23(23, YzhxWindowEnum.A3, YzhxWindowEnum.B3, YzhxWindowEnum.C1, YzhxWindowEnum.D3, YzhxWindowEnum.E3),
	
	WR24(24, YzhxWindowEnum.A1, YzhxWindowEnum.B3, YzhxWindowEnum.C3, YzhxWindowEnum.D3, YzhxWindowEnum.E1),
	
	WR25(25, YzhxWindowEnum.A3, YzhxWindowEnum.B1, YzhxWindowEnum.C1, YzhxWindowEnum.D1, YzhxWindowEnum.E3),
	
	WR26(26, YzhxWindowEnum.A1, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E2),
	
	WR27(27, YzhxWindowEnum.A3, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E2),
	
	WR28(28, YzhxWindowEnum.A1, YzhxWindowEnum.B1, YzhxWindowEnum.C2, YzhxWindowEnum.D3, YzhxWindowEnum.E3),
	
	WR29(29, YzhxWindowEnum.A3, YzhxWindowEnum.B3, YzhxWindowEnum.C2, YzhxWindowEnum.D1, YzhxWindowEnum.E1),
	
	WR30(30, YzhxWindowEnum.A2, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D1, YzhxWindowEnum.E1),
	
	WR31(31, YzhxWindowEnum.A2, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D3, YzhxWindowEnum.E3),
	
	WR32(32, YzhxWindowEnum.A1, YzhxWindowEnum.B1, YzhxWindowEnum.C1, YzhxWindowEnum.D2, YzhxWindowEnum.E3),
	
	WR33(33, YzhxWindowEnum.A3, YzhxWindowEnum.B3, YzhxWindowEnum.C3, YzhxWindowEnum.D2, YzhxWindowEnum.E1),
	
	WR34(34, YzhxWindowEnum.A1, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E3),
	
	WR35(35, YzhxWindowEnum.A3, YzhxWindowEnum.B2, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E1),
	
	WR36(36, YzhxWindowEnum.A2, YzhxWindowEnum.B1, YzhxWindowEnum.C2, YzhxWindowEnum.D3, YzhxWindowEnum.E3),
	
	WR37(37, YzhxWindowEnum.A2, YzhxWindowEnum.B3, YzhxWindowEnum.C2, YzhxWindowEnum.D1, YzhxWindowEnum.E1),
	
	WR38(38, YzhxWindowEnum.A2, YzhxWindowEnum.B1, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E3),
	
	WR39(39, YzhxWindowEnum.A2, YzhxWindowEnum.B3, YzhxWindowEnum.C2, YzhxWindowEnum.D2, YzhxWindowEnum.E1),
	
	WR40(40, YzhxWindowEnum.A1, YzhxWindowEnum.B2, YzhxWindowEnum.C1, YzhxWindowEnum.D2, YzhxWindowEnum.E3),
	;
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private YzhxWinLineEnum(int id, Window... ws){
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
