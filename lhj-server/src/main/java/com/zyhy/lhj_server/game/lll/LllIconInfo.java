/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

import com.zyhy.lhj_server.game.Icon;

/**
 * @author ASUS
 *
 */
public class LllIconInfo {

	private int id;
	
	private String name;
	
	private String desc;
	
	private double tr1;
	
	private double tr2;

	private double tr3;

	public LllIconInfo(Icon icon) {
		this.id = icon.getId();
		this.name = icon.getName();
		this.desc = icon.getDesc();
		this.tr1 = icon.getTr1();
		this.tr2 = icon.getTr2();
		this.tr3 = icon.getTr3();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTr1() {
		return tr1;
	}

	public void setTr1(double tr1) {
		this.tr1 = tr1;
	}

	public double getTr2() {
		return tr2;
	}

	public void setTr2(double tr2) {
		this.tr2 = tr2;
	}

	public double getTr3() {
		return tr3;
	}

	public void setTr3(double tr3) {
		this.tr3 = tr3;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
