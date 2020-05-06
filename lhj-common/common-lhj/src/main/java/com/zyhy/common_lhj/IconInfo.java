/**
 * 
 */
package com.zyhy.common_lhj;

import java.util.Arrays;

/**
 * @author ASUS
 *
 */
public class IconInfo {

	protected int id;
	
	protected String name;
	
	protected String desc;
	
	protected double[] trs;

	public IconInfo() {}
	
	public IconInfo(Icon icon) {
		this.id = icon.getId();
		this.name = icon.getName();
		this.desc = icon.getDesc();
		this.trs = icon.getTrs();
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double[] getTrs() {
		return trs;
	}

	public void setTrs(double[] trs) {
		this.trs = trs;
	}

	@Override
	public String toString() {
		return "IconInfo [id=" + id + ", name=" + name + ", desc=" + desc
				+ ", trs=" + Arrays.toString(trs) + "]";
	}
	
}
