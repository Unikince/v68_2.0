/**
 * 
 */
package com.dmg.bcbm.core.pool;

/**
 * @author ASUS
 *
 */
public class WaterPool {
	// 奖池当前水位
	private double currentnum = 0;
	private String gameRecord;
	
	public double getCurrentnum() {
		return currentnum;
	}
	public void setCurrentnum(double currentnum) {
		this.currentnum = currentnum;
	}
	
	public String getGameRecord() {
		return gameRecord;
	}
	public void setGameRecord(String gameRecord) {
		this.gameRecord = gameRecord;
	}
	@Override
	public String toString() {
		return "WaterPool [currentnum=" + currentnum + ", gameRecord=" + gameRecord + "]";
	}
	
}
