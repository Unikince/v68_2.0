/**
 * 
 */
package com.zyhy.common_lhj.pool;

/**
 * @author ASUS
 *
 */
public class DragonGameData {

	private int goldNum;
	private int redNum;
	private int blueNum;
	private int greenNum;
	public int getGoldNum() {
		return goldNum;
	}
	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}
	public int getRedNum() {
		return redNum;
	}
	public void setRedNum(int redNum) {
		this.redNum = redNum;
	}
	public int getBlueNum() {
		return blueNum;
	}
	public void setBlueNum(int blueNum) {
		this.blueNum = blueNum;
	}
	public int getGreenNum() {
		return greenNum;
	}
	public void setGreenNum(int greenNum) {
		this.greenNum = greenNum;
	}
	
	public void add(DragonItem d){
		switch (d.getName()) {
		case DragonPool.GRAND:
			goldNum++;
			break;
		case DragonPool.MAJOR:
			redNum++;
			break;
		case DragonPool.MINOR:
			blueNum++;
			break;
		case DragonPool.MINI:
			greenNum++;
			break;
		}
	}
	
	public boolean gameOver(){
		if(goldNum >= 3 || redNum >=3 || blueNum >=3 || greenNum >= 3){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "DragonGameData [goldNum=" + goldNum + ", redNum=" + redNum
				+ ", blueNum=" + blueNum + ", greenNum=" + greenNum + "]";
	}
	
}
