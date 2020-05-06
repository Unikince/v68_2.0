/**
 * 
 */
package com.zyhy.lhj_server.game.xywjs;

/**
 * @author ASUS
 *
 */
public enum XywjsOddsEnum {
	
	a(1, 0.70),
	b(2, 0.75),
	c(3, 0.80),
	d(4, 0.90),
	e(5, 1.00),
	f(6, 1.10),
	g(7, 1.20),
	;
	
	// 图标id
	private int iconType;
	// 当前胜率
	private double odds;
	
	private XywjsOddsEnum(int iconType, double odds) {
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
		for (XywjsOddsEnum e : values()) {
			if (e.getOdds() == odds) {
				return e.getIconType();
			}
		}
		return 0;
	}
	
}
