/**
 * 
 */
package com.zyhy.lhj_server.game.yhdd;

import java.util.List;

import com.zyhy.common_lhj.BetInfo;

/**
 * @author ASUS
 *
 */
public class YhddBonusInfo {

	//下注信息
	private BetInfo betInfo;
	// 下注档位
	private int betLine;
	//免费次数
	private int num;
	//倍数
	private int mul;
	//总奖金
	private double gold;
	// 选择的次数
	private List<Integer> numGroup;
	// 选择的倍数
	private List<Integer> mulGroup;
	public YhddBonusInfo(){}
	
	public YhddBonusInfo(BetInfo betInfo){
		this.betInfo = betInfo;
	}

	public BetInfo getBetInfo() {
		return betInfo;
	}

	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getMul() {
		return mul;
	}

	public void setMul(int mul) {
		this.mul = mul;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public int getBetLine() {
		return betLine;
	}

	public void setBetLine(int betLine) {
		this.betLine = betLine;
	}

	public List<Integer> getNumGroup() {
		return numGroup;
	}

	public void setNumGroup(List<Integer> numGroup) {
		this.numGroup = numGroup;
	}

	public List<Integer> getMulGroup() {
		return mulGroup;
	}

	public void setMulGroup(List<Integer> mulGroup) {
		this.mulGroup = mulGroup;
	}

	@Override
	public String toString() {
		return "YhddBonusInfo [betInfo=" + betInfo + ", betLine=" + betLine + ", num=" + num + ", mul=" + mul
				+ ", gold=" + gold + ", numGroup=" + numGroup + ", mulGroup=" + mulGroup + "]";
	}


	
}
