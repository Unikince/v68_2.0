/**
 * 
 */
package com.zyhy.lhj_server.game.xywjs.poi.template;

import com.zyhy.lhj_server.game.xywjs.poi.impl.TemplateConfigException;
import com.zyhy.lhj_server.game.xywjs.poi.impl.TemplateObject;

/**
 * @author linanjun
 *
 */
public class FruitOdds extends TemplateObject{

	private static final long serialVersionUID = 1L;

	private String name;
	private int multiple;
	private int odds;
	private int sign;
	private int bet;
	private int type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	public int getOdds() {
		return odds;
	}
	public void setOdds(int odds) {
		this.odds = odds;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
	
	public int getBet() {
		return bet;
	}
	public void setBet(int bet) {
		this.bet = bet;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public void check() throws TemplateConfigException {
		
	}

}
