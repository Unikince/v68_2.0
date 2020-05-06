/**
 * 
 */
package com.zyhy.lhj_server.game.lqjx;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum LqjxWinLineEnum implements Line{
	
	WR1(1, LqjxWindowEnum.A2, LqjxWindowEnum.B2, LqjxWindowEnum.C2, LqjxWindowEnum.D2, LqjxWindowEnum.E2),
	
	WR2(2, LqjxWindowEnum.A1, LqjxWindowEnum.B1, LqjxWindowEnum.C1, LqjxWindowEnum.D1, LqjxWindowEnum.E1),
	
	WR3(3, LqjxWindowEnum.A3, LqjxWindowEnum.B3, LqjxWindowEnum.C3, LqjxWindowEnum.D3, LqjxWindowEnum.E3),
	
	WR4(4, LqjxWindowEnum.A1, LqjxWindowEnum.B2, LqjxWindowEnum.C3, LqjxWindowEnum.D2, LqjxWindowEnum.E1),
	
	WR5(5, LqjxWindowEnum.A3, LqjxWindowEnum.B2, LqjxWindowEnum.C1, LqjxWindowEnum.D2, LqjxWindowEnum.E3),
	
	WR6(6, LqjxWindowEnum.A1, LqjxWindowEnum.B1, LqjxWindowEnum.C2, LqjxWindowEnum.D1, LqjxWindowEnum.E1),
	
	WR7(7, LqjxWindowEnum.A3, LqjxWindowEnum.B3, LqjxWindowEnum.C2, LqjxWindowEnum.D3, LqjxWindowEnum.E3),
	
	WR8(8, LqjxWindowEnum.A2, LqjxWindowEnum.B1, LqjxWindowEnum.C1, LqjxWindowEnum.D1, LqjxWindowEnum.E2),
	
	WR9(9, LqjxWindowEnum.A2, LqjxWindowEnum.B3, LqjxWindowEnum.C3, LqjxWindowEnum.D3, LqjxWindowEnum.E2);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private LqjxWinLineEnum(int id, Window... ws){
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
