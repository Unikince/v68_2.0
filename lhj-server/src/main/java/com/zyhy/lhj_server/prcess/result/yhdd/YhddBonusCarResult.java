/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.yhdd;

import java.util.List;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.yhdd.YhddBonusInfo;

/**
 * @author ASUS
 *
 */
public class YhddBonusCarResult extends HttpMessageResult{

	private List<Integer> group;
	
	private int num;
	
	private YhddBonusInfo bonusInfo;

	public List<Integer> getGroup() {
		return group;
	}

	public void setGroup(List<Integer> group) {
		this.group = group;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public YhddBonusInfo getYhddBonusInfo() {
		return bonusInfo;
	}

	public void setYhddBonusInfo(YhddBonusInfo bonusInfo) {
		this.bonusInfo = bonusInfo;
	}

	@Override
	public String toString() {
		return "YhddBonusCarResult [group=" + group + ", num=" + num
				+ ", bonusInfo=" + bonusInfo + "]";
	}
	
}
