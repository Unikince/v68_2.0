/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.zctz;

import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author ASUS
 *
 */
public class ZctzPoolResult extends HttpMessageResult{

	private double gold;
	
	private double red;
	
	private double blue;
	
	private double green;

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	@Override
	public String toString() {
		return "DragonPool [gold=" + gold + ", red=" + red + ", blue=" + blue
				+ ", green=" + green + "]";
	}
	
}
