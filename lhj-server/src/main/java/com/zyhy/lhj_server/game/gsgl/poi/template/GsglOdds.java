/**
 * 
 */
package com.zyhy.lhj_server.game.gsgl.poi.template;

import com.zyhy.lhj_server.game.gsgl.poi.impl.TemplateConfigException;
import com.zyhy.lhj_server.game.gsgl.poi.impl.TemplateObject;

/**
 * @author linanjun
 *
 */
public class GsglOdds extends TemplateObject{

	private static final long serialVersionUID = 1L;
	
	private int col;
	private String name;
	private int type;
	
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
	@Override
	public void check() throws TemplateConfigException {
	}
	@Override
	public String toString() {
		return "YzhxOdds [col=" + col + ", name=" + name + ", type=" + type + "]";
	}
}
