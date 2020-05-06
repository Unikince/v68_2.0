/**
 * 
 */
package com.zyhy.lhj_server.game.zctz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.lhj_server.game.zctz.poi.template.ZctzOdds;

/**
 * @author ASUS
 *
 */
public enum ZctzIconEnum implements Icon{

	WILD(1, "WILD", "狮", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "招财童子", 0, 1, 5, 10, 100),
	S1(4, "S1", "蝶", 2, 10, 50, 500, 10000),
	A1(5, "A1", "元宝", 0, 5, 25, 250, 5000),
	A2(6, "A2", "鱼", 0, 5, 20, 170, 1200),
	A3(7, "A3", "桃", 0, 5, 20, 125, 750),
	A(8, "A", "菠萝", 0, 2, 10, 50, 350),
	K(9, "K", "福", 0, 2, 10, 50, 250),
	Q(10, "Q", "铜板", 0, 0, 5, 25, 200),
	J(11, "J", "红包", 0, 0, 5, 25, 200),
	T10(12, "T10", "灯笼", 0, 0, 3, 10, 50),
	T11(12, "T11", "鞭炮", 0, 0, 3, 10, 50);
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	ZctzIconEnum(int id, String name, String desc, double... trs){
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.trs = trs;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	public int getId() {
		return id;
	}

	public static ZctzIconEnum getById(int id) {
		for(ZctzIconEnum v:values()){
			if(v.getId() == id){
				return v;
			}
		}
		return null;
	}

	@Override
	public double[] getTrs() {
		return trs;
	}

	@Override
	public boolean isScatter() {
		if(this == ZctzIconEnum.SCATTER){
			return true;
		}
		return false;
	}

	@Override
	public boolean isBar() {
		return false;
	}

	@Override
	public boolean isWild() {
		return this == ZctzIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}
	
}
