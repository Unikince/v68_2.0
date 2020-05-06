/**
 * 
 */
package com.zyhy.lhj_server.game;

/**
 * @author ASUS
 *
 */
public enum GameOddsEnum {
	
	/*a(11, 0.70),
	b(12, 0.80),
	c(13, 0.90),
	d(14, 0.95),
	e(15, 1.00),
	f(16, 1.10),
	g(17, 1.20),*/
	
	a(1, 0.70),
	b(2, 0.80),
	c(3, 0.90),
	d(4, 0.95),
	e(5, 1.00),
	f(6, 1.10),
	g(7, 1.20),
	;
	
	// 图标id
	private int iconType;
	// 当前胜率
	private double odds;
	
	private GameOddsEnum(int iconType, double odds) {
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
		for (GameOddsEnum e : values()) {
			if (e.getOdds() == odds) {
				return e.getIconType();
			}
		}
		return 0;
	}
	
}
