/**
 * 
 */
package com.zyhy.lhj_server.game.bxlm;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum BxlmWinLineEnum implements Line{
	
	WR1(1, BxlmWindowEnum.A2, BxlmWindowEnum.B2, BxlmWindowEnum.C2, BxlmWindowEnum.D2, BxlmWindowEnum.E2),
	
	WR2(2, BxlmWindowEnum.A1, BxlmWindowEnum.B1, BxlmWindowEnum.C1, BxlmWindowEnum.D1, BxlmWindowEnum.E1),
	
	WR3(3, BxlmWindowEnum.A3, BxlmWindowEnum.B3, BxlmWindowEnum.C3, BxlmWindowEnum.D3, BxlmWindowEnum.E3),
	
	WR4(4, BxlmWindowEnum.A1, BxlmWindowEnum.B2, BxlmWindowEnum.C3, BxlmWindowEnum.D2, BxlmWindowEnum.E1),
	
	WR5(5, BxlmWindowEnum.A3, BxlmWindowEnum.B2, BxlmWindowEnum.C1, BxlmWindowEnum.D2, BxlmWindowEnum.E3),
	
	WR6(6, BxlmWindowEnum.A1, BxlmWindowEnum.B1, BxlmWindowEnum.C2, BxlmWindowEnum.D1, BxlmWindowEnum.E1),
	
	WR7(7, BxlmWindowEnum.A3, BxlmWindowEnum.B3, BxlmWindowEnum.C2, BxlmWindowEnum.D3, BxlmWindowEnum.E3),
	
	WR8(8, BxlmWindowEnum.A2, BxlmWindowEnum.B1, BxlmWindowEnum.C1, BxlmWindowEnum.D1, BxlmWindowEnum.E2),
	
	WR9(9, BxlmWindowEnum.A2, BxlmWindowEnum.B3, BxlmWindowEnum.C3, BxlmWindowEnum.D3, BxlmWindowEnum.E2);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private BxlmWinLineEnum(int id, Window... ws){
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
