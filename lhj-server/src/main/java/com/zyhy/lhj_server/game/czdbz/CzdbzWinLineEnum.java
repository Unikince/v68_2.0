/**
 * 
 */
package com.zyhy.lhj_server.game.czdbz;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum CzdbzWinLineEnum implements Line{
	
	WR1(1, CzdbzWindowEnum.A2, CzdbzWindowEnum.B2, CzdbzWindowEnum.C2, CzdbzWindowEnum.D2, CzdbzWindowEnum.E2),
	
	WR2(2, CzdbzWindowEnum.A1, CzdbzWindowEnum.B1, CzdbzWindowEnum.C1, CzdbzWindowEnum.D1, CzdbzWindowEnum.E1),
	
	WR3(3, CzdbzWindowEnum.A3, CzdbzWindowEnum.B3, CzdbzWindowEnum.C3, CzdbzWindowEnum.D3, CzdbzWindowEnum.E3),
	
	WR4(4, CzdbzWindowEnum.A1, CzdbzWindowEnum.B2, CzdbzWindowEnum.C3, CzdbzWindowEnum.D2, CzdbzWindowEnum.E1),
	
	WR5(5, CzdbzWindowEnum.A3, CzdbzWindowEnum.B2, CzdbzWindowEnum.C1, CzdbzWindowEnum.D2, CzdbzWindowEnum.E3),
	
	WR6(6, CzdbzWindowEnum.A1, CzdbzWindowEnum.B1, CzdbzWindowEnum.C2, CzdbzWindowEnum.D1, CzdbzWindowEnum.E1),
	
	WR7(7, CzdbzWindowEnum.A3, CzdbzWindowEnum.B3, CzdbzWindowEnum.C2, CzdbzWindowEnum.D3, CzdbzWindowEnum.E3),
	
	WR8(8, CzdbzWindowEnum.A2, CzdbzWindowEnum.B1, CzdbzWindowEnum.C1, CzdbzWindowEnum.D1, CzdbzWindowEnum.E2),
	
	WR9(9, CzdbzWindowEnum.A2, CzdbzWindowEnum.B3, CzdbzWindowEnum.C3, CzdbzWindowEnum.D3, CzdbzWindowEnum.E2);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private CzdbzWinLineEnum(int id, Window... ws){
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
