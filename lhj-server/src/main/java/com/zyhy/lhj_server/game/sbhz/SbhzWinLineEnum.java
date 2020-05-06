/**
 * 
 */
package com.zyhy.lhj_server.game.sbhz;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum SbhzWinLineEnum implements Line{
	
	WR1(1, SbhzWindowEnum.A2, SbhzWindowEnum.B2, SbhzWindowEnum.C2),
	
	WR2(2, SbhzWindowEnum.A1, SbhzWindowEnum.B1, SbhzWindowEnum.C1),
	
	WR3(3, SbhzWindowEnum.A3, SbhzWindowEnum.B3, SbhzWindowEnum.C3),
	
	WR4(4, SbhzWindowEnum.A1, SbhzWindowEnum.B2, SbhzWindowEnum.C3),
	
	WR5(5, SbhzWindowEnum.A3, SbhzWindowEnum.B2, SbhzWindowEnum.C1);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private SbhzWinLineEnum(int id, Window... ws){
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
