/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

/**
 * @author ASUS
 *
 */
public enum LllDifficultyEnum {

	d1(LllIconEnum.T1, 0, 10 ,30),
	d2(LllIconEnum.T2, 0, 10 ,20),
	d3(LllIconEnum.T3, 0, 10 ,20),
	;
	private LllIconEnum icon; // 图案
	private int k1; // 1个杀率
	private int k2; // 2个杀率
	private int k3; // 3个杀率
	
	private LllDifficultyEnum(LllIconEnum icon, int k1, int k2, int k3){
		this.icon = icon;
		this.k1 = k1;
		this.k2 = k2;
		this.k3 = k3;
	}

	public LllIconEnum getIcon() {
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
