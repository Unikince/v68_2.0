/**
 * 
 */
package com.zyhy.common_lhj.logic;

import java.util.List;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;

/**
 * @author ASUS
 *
 */
public interface Logic {

	/**
	 * 得到线上的格子信息
	 * @param ws
	 * @param line
	 * @return
	 */
	public List<Window> getLineWindows(List<Window> ws, Line line);
	
	/**
	 * 计算奖励
	 * @param wl
	 * @param betinfo
	 */
	public void reward(WinLineInfo wl, BetInfo betinfo);

	/**
	 * 获取获胜线路信息
	 * @param lineWindows 线路信息
	 * @return
	 */
	public WinLineInfo getWinLineInfo(List<Window> lineWindows);
	
	/**
	 * Scatter奖励
	 * @param wl
	 * @param betinfo
	 */
	public void rewardScatter(WinLineInfo wl, BetInfo betinfo);
	
	/**
	 * Wail奖励
	 * @param wl
	 * @param betinfo
	 */
	public void rewardWail(WinLineInfo wl, BetInfo betinfo);
}
