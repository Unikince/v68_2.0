/**
 * 
 */
package com.zyhy.lhj_server.game;

/**
 * @author ASUS
 *
 */
public enum TgpdOddsEnum {
	
	a(1, 0.80),
	b(2, 0.95),
	c(3, 1.00),
	;
	
	// 图标id
	private int iconType;
	// 当前胜率
	private double odds;
	
	private TgpdOddsEnum(int iconType, double odds) {
		this.iconType = iconType;
		this.odds = odds;
	}
	public double getOdds() {
		return odds;
	}
	public void setOdds(double odds) {
		this.odds = odds;
	}
	public int getIconType() {
		return iconType;
	}
	public void setIconType(int iconType) {
		this.iconType = iconType;
	}
	
	public static int getIdByOdds(double odds){
		for (TgpdOddsEnum e : values()) {
			if (e.getOdds() == odds) {
				return e.getIconType();
			}
		}
		return 0;
	}
	
}
