/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.gsgl;

import java.util.List;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.gsgl.GsglBonusInfo;

/**
 * @author ASUS
 *
 */
public class GsglBonusCarResult extends HttpMessageResult{

	private List<Integer> group;
	
	private int num;
	
	private GsglBonusInfo bonusInfo;

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

	public GsglBonusInfo getBonusInfo() {
		return bonusInfo;
	}

	public void setBonusInfo(GsglBonusInfo bonusInfo) {
		this.bonusInfo = bonusInfo;
	}

	@Override
	public String toString() {
		return "GsglBonusCarResult [group=" + group + ", num=" + num
				+ ", bonusInfo=" + bonusInfo + "]";
	}
	
}
