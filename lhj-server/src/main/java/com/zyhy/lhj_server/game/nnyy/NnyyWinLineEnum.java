/**
 * 
 */
package com.zyhy.lhj_server.game.nnyy;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum NnyyWinLineEnum implements Line{
	
	WR1(1, NnyyWindowEnum.A2, NnyyWindowEnum.B2, NnyyWindowEnum.C2, NnyyWindowEnum.D2, NnyyWindowEnum.E2),
	
	WR2(2, NnyyWindowEnum.A1, NnyyWindowEnum.B1, NnyyWindowEnum.C1, NnyyWindowEnum.D1, NnyyWindowEnum.E1),
	
	WR3(3, NnyyWindowEnum.A3, NnyyWindowEnum.B3, NnyyWindowEnum.C3, NnyyWindowEnum.D3, NnyyWindowEnum.E3),
	
	WR4(4, NnyyWindowEnum.A1, NnyyWindowEnum.B2, NnyyWindowEnum.C3, NnyyWindowEnum.D2, NnyyWindowEnum.E1),
	
	WR5(5, NnyyWindowEnum.A3, NnyyWindowEnum.B2, NnyyWindowEnum.C1, NnyyWindowEnum.D2, NnyyWindowEnum.E3),
	
	WR6(6, NnyyWindowEnum.A1, NnyyWindowEnum.B1, NnyyWindowEnum.C2, NnyyWindowEnum.D1, NnyyWindowEnum.E1),
	
	WR7(7, NnyyWindowEnum.A3, NnyyWindowEnum.B3, NnyyWindowEnum.C2, NnyyWindowEnum.D3, NnyyWindowEnum.E3),
	
	WR8(8, NnyyWindowEnum.A2, NnyyWindowEnum.B1, NnyyWindowEnum.C1, NnyyWindowEnum.D1, NnyyWindowEnum.E2),
	
	WR9(9, NnyyWindowEnum.A2, NnyyWindowEnum.B3, NnyyWindowEnum.C3, NnyyWindowEnum.D3, NnyyWindowEnum.E2);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private NnyyWinLineEnum(int id, Window... ws){
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
