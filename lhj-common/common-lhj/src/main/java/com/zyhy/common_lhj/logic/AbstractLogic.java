/**
 * 
 */
package com.zyhy.common_lhj.logic;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.util.NumberTool;

/**
 * @author ASUS
 *
 */
public abstract class AbstractLogic implements Logic{

	/**
	 * 获取线路对应的格子信息
	 */
	public List<Window> getLineWindows(List<Window> ws, Line line) {
		Window[] lws = line.getWindows();
		int lsize = lws.length;
		List<Window> rs = new ArrayList<Window>();
		int i = 0;
		for(Window w : ws){
			Window lw = lws[i];
			if(w.getId() == lw.getId() && w.getIndex() == lw.getIndex()){
				rs.add(w);
				i++;
				if(i>=lsize){
					break;
				}
			}
		}
		return rs;
	}
	
	/**
	 * 获取线路奖励信息
	 */
	@Override
	public WinLineInfo getWinLineInfo(List<Window> lineWindows) {
		int wail = 0;
		int num = 0;
		//是否scatter触发免费游戏
		Icon lineIcon = null;
		int size = lineWindows.size();
		for(int i=0;i<size;i++){
			Icon icon = lineWindows.get(i).getIcon();
			if(i > 0 && i != wail && !icon.isWild() && lineIcon != icon){
				break;
			}
			if(icon.isScatter()){
				lineIcon = icon;
			}else if(icon.isWild()){
				wail++;
			}else if(icon.isBar()){
				lineIcon = icon;
			}else{
				lineIcon = icon;
			}
			num++;
		}
		if(num > 0){
			if(lineIcon == null){
				lineIcon = lineWindows.get(0).getIcon();
			}
			double v = lineIcon.getTrs()[num-1];
			if(v > 0){
				WinLineInfo winfo = new WinLineInfo();
				winfo.setReward(v);
				winfo.setNum(num);
				winfo.setIcon(lineIcon);
				return winfo;
			}
		}
		return null;
	}
	
	/**
	 * 计算线路奖励
	 */
	@Override
	public void reward(WinLineInfo wl, BetInfo betinfo) {
		if(wl != null){
			rewardWail(wl, betinfo);
			rewardScatter(wl, betinfo);
			
			wl.setReward(NumberTool.multiply(wl.getReward(), betinfo.getGold()).doubleValue());
		}
	}
	
	/**
	 * 计算wail奖励
	 */
	@Override
	public void rewardWail(WinLineInfo wl, BetInfo betinfo) {
		if(wl.validWailNum() > 0){
			wl.setReward(NumberTool.multiply(wl.getReward(), 2).doubleValue());
		}
	}
	
	/**
	 * 计算scatter奖励
	 */
	@Override
	public void rewardScatter(WinLineInfo wl, BetInfo betinfo) {
		if(wl.getIcon().isScatter()){
			wl.setReward(NumberTool.multiply(wl.getReward(), betinfo.getNum()).doubleValue());
		}
	}
}
