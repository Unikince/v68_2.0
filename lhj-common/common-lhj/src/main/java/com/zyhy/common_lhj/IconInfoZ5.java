/**
 * 
 */
package com.zyhy.common_lhj;

/**
 * @author ASUS
 *
 */
public class IconInfoZ5 extends IconInfo{

	private double tr4;

	private double tr5;
	
	public IconInfoZ5(Icon icon) {
		this.id = icon.getId();
		this.name = icon.getName();
		this.desc = icon.getDesc();
		double[] trs = icon.getTrs();
//		this.tr1 = trs[0];
//		this.tr2 = trs[1];
//		this.tr3 = trs[2];
		this.tr4 = trs[3];
		this.tr5 = trs[4];
	}

	public double getTr4() {
		return tr4;
	}

	public void setTr4(double tr4) {
		this.tr4 = tr4;
	}

	public double getTr5() {
		return tr5;
	}

	public void setTr5(double tr5) {
		this.tr5 = tr5;
	}
	
}
