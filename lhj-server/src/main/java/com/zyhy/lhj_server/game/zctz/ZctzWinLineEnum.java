/**
 * 
 */
package com.zyhy.lhj_server.game.zctz;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum ZctzWinLineEnum implements Line{
	
	WR1(1, ZctzWindowEnum.A2, ZctzWindowEnum.B2, ZctzWindowEnum.C2, ZctzWindowEnum.D2, ZctzWindowEnum.E2),
	
	WR2(2, ZctzWindowEnum.A1, ZctzWindowEnum.B1, ZctzWindowEnum.C1, ZctzWindowEnum.D1, ZctzWindowEnum.E1),
	
	WR3(3, ZctzWindowEnum.A3, ZctzWindowEnum.B3, ZctzWindowEnum.C3, ZctzWindowEnum.D3, ZctzWindowEnum.E3),
	
	WR4(4, ZctzWindowEnum.A1, ZctzWindowEnum.B2, ZctzWindowEnum.C3, ZctzWindowEnum.D2, ZctzWindowEnum.E1),
	
	WR5(5, ZctzWindowEnum.A3, ZctzWindowEnum.B2, ZctzWindowEnum.C1, ZctzWindowEnum.D2, ZctzWindowEnum.E3),
	
	WR6(6, ZctzWindowEnum.A1, ZctzWindowEnum.B1, ZctzWindowEnum.C2, ZctzWindowEnum.D1, ZctzWindowEnum.E1),
	
	WR7(7, ZctzWindowEnum.A3, ZctzWindowEnum.B3, ZctzWindowEnum.C2, ZctzWindowEnum.D3, ZctzWindowEnum.E3),
	
	WR8(8, ZctzWindowEnum.A2, ZctzWindowEnum.B1, ZctzWindowEnum.C1, ZctzWindowEnum.D1, ZctzWindowEnum.E2),
	
	WR9(9, ZctzWindowEnum.A2, ZctzWindowEnum.B3, ZctzWindowEnum.C3, ZctzWindowEnum.D3, ZctzWindowEnum.E2);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private ZctzWinLineEnum(int id, Window... ws){
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
