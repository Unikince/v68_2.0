/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

/**
 * @author ASUS
 *
 */
public enum GghzDifficultyEnum {

	d1(GghzIconEnum.T1, 0, 10 ,30),
	d2(GghzIconEnum.T2, 0, 10 ,20),
	d3(GghzIconEnum.T3, 0, 10 ,20),
	d4(GghzIconEnum.T4, 0, 10 ,10),
	d5(GghzIconEnum.T5, 0, 10 ,10),
	d6(GghzIconEnum.T6, 0, 0 ,0),
	d7(GghzIconEnum.T7, 0, 0 ,0),
	d8(GghzIconEnum.T8, 0, 0 ,0),
	;
	private GghzIconEnum icon; // 图案
	private int k1; // 1个杀率
	private int k2; // 2个杀率
	private int k3; // 3个杀率
	
	private GghzDifficultyEnum(GghzIconEnum icon, int k1, int k2, int k3){
		this.icon = icon;
		this.k1 = k1;
		this.k2 = k2;
		this.k3 = k3;
	}

	public GghzIconEnum getIcon() {
		return icon;
	}

	public int getK1() {
		return k1;
	}

	public int getK2() {
		return k2;
	}

	public int getK3() {
		return k3;
	}
	
	
}
