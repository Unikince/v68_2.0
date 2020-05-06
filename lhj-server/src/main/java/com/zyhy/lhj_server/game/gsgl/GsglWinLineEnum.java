/**
 * 
 */
package com.zyhy.lhj_server.game.gsgl;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum GsglWinLineEnum implements Line{
	
	WR1(1, GsglWindowEnum.A2, GsglWindowEnum.B2, GsglWindowEnum.C2, GsglWindowEnum.D2, GsglWindowEnum.E2),
	
	WR2(2, GsglWindowEnum.A1, GsglWindowEnum.B1, GsglWindowEnum.C1, GsglWindowEnum.D1, GsglWindowEnum.E1),
	
	WR3(3, GsglWindowEnum.A3, GsglWindowEnum.B3, GsglWindowEnum.C3, GsglWindowEnum.D3, GsglWindowEnum.E3),
	
	WR4(4, GsglWindowEnum.A1, GsglWindowEnum.B2, GsglWindowEnum.C3, GsglWindowEnum.D2, GsglWindowEnum.E1),
	
	WR5(5, GsglWindowEnum.A3, GsglWindowEnum.B2, GsglWindowEnum.C1, GsglWindowEnum.D2, GsglWindowEnum.E3),
	
	WR6(6, GsglWindowEnum.A2, GsglWindowEnum.B1, GsglWindowEnum.C1, GsglWindowEnum.D1, GsglWindowEnum.E2),
	
	WR7(7, GsglWindowEnum.A2, GsglWindowEnum.B3, GsglWindowEnum.C3, GsglWindowEnum.D3, GsglWindowEnum.E2),
	
	WR8(8, GsglWindowEnum.A1, GsglWindowEnum.B1, GsglWindowEnum.C2, GsglWindowEnum.D3, GsglWindowEnum.E3),
	
	WR9(9, GsglWindowEnum.A3, GsglWindowEnum.B3, GsglWindowEnum.C2, GsglWindowEnum.D1, GsglWindowEnum.E1);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private GsglWinLineEnum(int id, Window... ws){
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
