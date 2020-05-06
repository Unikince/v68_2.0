/**
 * 
 */
package com.zyhy.lhj_server.game.bqtp;

import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;


/**
 * @author linanjun
 * 5轴中奖线路信息
 */
public enum BqtpWinLineEnum implements Line{
	
	WR1(1, BqtpWindowEnum.A2, BqtpWindowEnum.B2, BqtpWindowEnum.C2, BqtpWindowEnum.D2, BqtpWindowEnum.E2),
	
	WR2(2, BqtpWindowEnum.A1, BqtpWindowEnum.B1, BqtpWindowEnum.C1, BqtpWindowEnum.D1, BqtpWindowEnum.E1),
	
	WR3(3, BqtpWindowEnum.A3, BqtpWindowEnum.B3, BqtpWindowEnum.C3, BqtpWindowEnum.D3, BqtpWindowEnum.E3),
	
	WR4(4, BqtpWindowEnum.A1, BqtpWindowEnum.B2, BqtpWindowEnum.C3, BqtpWindowEnum.D2, BqtpWindowEnum.E1),
	
	WR5(5, BqtpWindowEnum.A3, BqtpWindowEnum.B2, BqtpWindowEnum.C1, BqtpWindowEnum.D2, BqtpWindowEnum.E3),
	
	WR6(6, BqtpWindowEnum.A1, BqtpWindowEnum.B1, BqtpWindowEnum.C2, BqtpWindowEnum.D1, BqtpWindowEnum.E1),
	
	WR7(7, BqtpWindowEnum.A3, BqtpWindowEnum.B3, BqtpWindowEnum.C2, BqtpWindowEnum.D3, BqtpWindowEnum.E3),
	
	WR8(8, BqtpWindowEnum.A2, BqtpWindowEnum.B1, BqtpWindowEnum.C1, BqtpWindowEnum.D1, BqtpWindowEnum.E2),
	
	WR9(9, BqtpWindowEnum.A2, BqtpWindowEnum.B3, BqtpWindowEnum.C3, BqtpWindowEnum.D3, BqtpWindowEnum.E2);
	
	private int id;
	/**
	 * 窗口信息
	 */
	private Window[] windows;
	
	private BqtpWinLineEnum(int id, Window... ws){
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
