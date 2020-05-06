/**
 * 
 */
package com.zyhy.lhj_server.game.tgpd.poi.template;

import com.zyhy.lhj_server.game.tgpd.poi.impl.TemplateConfigException;
import com.zyhy.lhj_server.game.tgpd.poi.impl.TemplateObject;

/**
 * @author linanjun
 *
 */
public class TgpdOdds extends TemplateObject{

	private static final long serialVersionUID = 1L;
	
	private int col;
	private String name;
	private int type;
	private int weight;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	@Override
	public void check() throws TemplateConfigException {
	}
	@Override
	public String toString() {
		return "GghzOdds [col=" + col + ", name=" + name + ", type=" + type + ", weight=" + weight + "]";
	}
}
