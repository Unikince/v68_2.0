/**
 * 
 */
package com.zyhy.lhj_server.game.ajxz;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * 5轴中奖线路信息
 */
public enum AjxzWinLineEnum implements Line{
	
	WR1(1, AjxzWindowEnum.A2, AjxzWindowEnum.B2, AjxzWindowEnum.C2, AjxzWindowEnum.D2, AjxzWindowEnum.E2),
	
	WR2(2, AjxzWindowEnum.A1, AjxzWindowEnum.B1, AjxzWindowEnum.C1, AjxzWindowEnum.D1, AjxzWindowEnum.E1),
	
	WR3(3, AjxzWindowEnum.A3, AjxzWindowEnum.B3, AjxzWindowEnum.C3, AjxzWindowEnum.D3, AjxzWindowEnum.E3),
	
	WR4(4, AjxzWindowEnum.A1, AjxzWindowEnum.B2, AjxzWindowEnum.C3, AjxzWindowEnum.D2, AjxzWindowEnum.E1),
	
	WR5(5, AjxzWindowEnum.A3, AjxzWindowEnum.B2, AjxzWindowEnum.C1, AjxzWindowEnum.D2, AjxzWindowEnum.E3),
	
	WR6(6, AjxzWindowEnum.A2, AjxzWindowEnum.B1, AjxzWindowEnum.C1, AjxzWindowEnum.D1, AjxzWindowEnum.E2),
	
	WR7(7, AjxzWindowEnum.A2, AjxzWindowEnum.B3, AjxzWindowEnum.C3, AjxzWindowEnum.D3, AjxzWindowEnum.E2),
	
	WR8(8, AjxzWindowEnum.A1, AjxzWindowEnum.B1, AjxzWindowEnum.C2, AjxzWindowEnum.D3, AjxzWindowEnum.E3),
	
	WR9(9, AjxzWindowEnum.A3, AjxzWindowEnum.B3, AjxzWindowEnum.C2, AjxzWindowEnum.D1, AjxzWindowEnum.E1),
	
	WR10(10, AjxzWindowEnum.A2, AjxzWindowEnum.B3, AjxzWindowEnum.C2, AjxzWindowEnum.D1, AjxzWindowEnum.E2),
	
	WR11(11, AjxzWindowEnum.A2, AjxzWindowEnum.B1, AjxzWindowEnum.C2, AjxzWindowEnum.D3, AjxzWindowEnum.E2),
	
	WR12(12, AjxzWindowEnum.A1, AjxzWindowEnum.B2, AjxzWindowEnum.C2, AjxzWindowEnum.D2, AjxzWindowEnum.E1),
	
	WR13(13, AjxzWindowEnum.A3, AjxzWindowEnum.B2, AjxzWindowEnum.C2, AjxzWindowEnum.D2, AjxzWindowEnum.E3),
	
	WR14(14, AjxzWindowEnum.A1, AjxzWindowEnum.B2, AjxzWindowEnum.C1, AjxzWindowEnum.D2, AjxzWindowEnum.E1),
	
	WR15(15, AjxzWindowEnum.A3, AjxzWindowEnum.B2, AjxzWindowEnum.C3, AjxzWindowEnum.D2, AjxzWindowEnum.E3),
	
	WR16(16, AjxzWindowEnum.A2, AjxzWindowEnum.B2, AjxzWindowEnum.C1, AjxzWindowEnum.D2, AjxzWindowEnum.E2),
	
	WR17(17, AjxzWindowEnum.A2, AjxzWindowEnum.B2, AjxzWindowEnum.C3, AjxzWindowEnum.D2, AjxzWindowEnum.E2),
	
	WR18(18, AjxzWindowEnum.A1, AjxzWindowEnum.B1, AjxzWindowEnum.C3, AjxzWindowEnum.D1, AjxzWindowEnum.E1),
	
	WR19(19, AjxzWindowEnum.A3, AjxzWindowEnum.B3, AjxzWindowEnum.C1, AjxzWindowEnum.D3, AjxzWindowEnum.E3),
	
	WR20(20, AjxzWindowEnum.A1, AjxzWindowEnum.B3, AjxzWindowEnum.C3, AjxzWindowEnum.D3, AjxzWindowEnum.E1),
	
	WR21(21, AjxzWindowEnum.A3, AjxzWindowEnum.B1, AjxzWindowEnum.C1, AjxzWindowEnum.D1, AjxzWindowEnum.E3),
	
	WR22(22, AjxzWindowEnum.A2, AjxzWindowEnum.B3, AjxzWindowEnum.C1, AjxzWindowEnum.D3, AjxzWindowEnum.E2),
	
	WR23(23, AjxzWindowEnum.A2, AjxzWindowEnum.B1, AjxzWindowEnum.C3, AjxzWindowEnum.D1, AjxzWindowEnum.E2),
	
	WR24(24, AjxzWindowEnum.A1, AjxzWindowEnum.B3, AjxzWindowEnum.C1, AjxzWindowEnum.D3, AjxzWindowEnum.E1),
	
	WR25(25, AjxzWindowEnum.A3, AjxzWindowEnum.B1, AjxzWindowEnum.C3, AjxzWindowEnum.D1, AjxzWindowEnum.E3),
	
	WR26(26, AjxzWindowEnum.A1, AjxzWindowEnum.B3, AjxzWindowEnum.C2, AjxzWindowEnum.D1, AjxzWindowEnum.E3),
	
	WR27(27, AjxzWindowEnum.A3, AjxzWindowEnum.B1, AjxzWindowEnum.C2, AjxzWindowEnum.D3, AjxzWindowEnum.E1),
	
	WR28(28, AjxzWindowEnum.A2, AjxzWindowEnum.B1, AjxzWindowEnum.C3, AjxzWindowEnum.D2, AjxzWindowEnum.E3),
	
	WR29(29, AjxzWindowEnum.A1, AjxzWindowEnum.B3, AjxzWindowEnum.C2, AjxzWindowEnum.D3, AjxzWindowEnum.E1),
	
	WR30(30, AjxzWindowEnum.A3, AjxzWindowEnum.B2, AjxzWindowEnum.C1, AjxzWindowEnum.D1, AjxzWindowEnum.E2),
	
	WR31(31, AjxzWindowEnum.A1, AjxzWindowEnum.B2, AjxzWindowEnum.C3, AjxzWindowEnum.D3, AjxzWindowEnum.E2),
	
	WR32(32, AjxzWindowEnum.A2, AjxzWindowEnum.B1, AjxzWindowEnum.C2, AjxzWindowEnum.D1, AjxzWindowEnum.E2),
	
	WR33(33, AjxzWindowEnum.A2, AjxzWindowEnum.B3, AjxzWindowEnum.C2, AjxzWindowEnum.D3, AjxzWindowEnum.E2),
	
	WR34(34, AjxzWindowEnum.A1, AjxzWindowEnum.B2, AjxzWindowEnum.C1, AjxzWindowEnum.D2, AjxzWindowEnum.E3),
	
	WR35(35, AjxzWindowEnum.A3, AjxzWindowEnum.B2, AjxzWindowEnum.C3, AjxzWindowEnum.D1, AjxzWindowEnum.E1),
	
	WR36(36, AjxzWindowEnum.A3, AjxzWindowEnum.B1, AjxzWindowEnum.C1, AjxzWindowEnum.D2, AjxzWindowEnum.E3),
	
	WR37(37, AjxzWindowEnum.A2, AjxzWindowEnum.B3, AjxzWindowEnum.C3, AjxzWindowEnum.D1, AjxzWindowEnum.E1),
	
	WR38(38, AjxzWindowEnum.A1, AjxzWindowEnum.B1, AjxzWindowEnum.C2, AjxzWindowEnum.D2, AjxzWindowEnum.E3),
	
	WR39(39, AjxzWindowEnum.A3, AjxzWindowEnum.B3, AjxzWindowEnum.C1, AjxzWindowEnum.D2, AjxzWindowEnum.E1),
	
	WR40(40, AjxzWindowEnum.A1, AjxzWindowEnum.B1, AjxzWindowEnum.C3, AjxzWindowEnum.D3, AjxzWindowEnum.E3),
	
	WR41(41, AjxzWindowEnum.A3, AjxzWindowEnum.B3, AjxzWindowEnum.C1, AjxzWindowEnum.D1, AjxzWindowEnum.E1),
	
	WR42(42, AjxzWindowEnum.A2, AjxzWindowEnum.B3, AjxzWindowEnum.C1, AjxzWindowEnum.D2, AjxzWindowEnum.E3),
	
	WR43(43, AjxzWindowEnum.A2, AjxzWindowEnum.B1, AjxzWindowEnum.C3, AjxzWindowEnum.D2, AjxzWindowEnum.E1),
	
	WR44(44, AjxzWindowEnum.A1, AjxzWindowEnum.B2, AjxzWindowEnum.C3, AjxzWindowEnum.D2, AjxzWindowEnum.E2),
	
	WR45(45, AjxzWindowEnum.A3, AjxzWindowEnum.B2, AjxzWindowEnum.C1, AjxzWindowEnum.D3, AjxzWindowEnum.E2),
	
	WR46(46, AjxzWindowEnum.A2, AjxzWindowEnum.B3, AjxzWindowEnum.C2, AjxzWindowEnum.D1, AjxzWindowEnum.E1),
	
	WR47(47, AjxzWindowEnum.A2, AjxzWindowEnum.B1, AjxzWindowEnum.C2, AjxzWindowEnum.D3, AjxzWindowEnum.E3),
	
	WR48(48, AjxzWindowEnum.A3, AjxzWindowEnum.B3, AjxzWindowEnum.C2, AjxzWindowEnum.D3, AjxzWindowEnum.E3),
	
	WR49(49, AjxzWindowEnum.A1, AjxzWindowEnum.B1, AjxzWindowEnum.C2, AjxzWindowEnum.D1, AjxzWindowEnum.E1),
	
	WR50(50, AjxzWindowEnum.A3, AjxzWindowEnum.B2, AjxzWindowEnum.C3, AjxzWindowEnum.D1, AjxzWindowEnum.E2)
	;
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private AjxzWinLineEnum(int id, Window... ws){
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
