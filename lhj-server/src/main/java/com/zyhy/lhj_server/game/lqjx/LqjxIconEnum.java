/**
 * 
 */
package com.zyhy.lhj_server.game.lqjx;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum LqjxIconEnum implements Icon{

	WILD(1, "WILD", "WILD", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "SCATTER", 0, 0, 5, 50, 250),
	S1(3, "S1", "红框球员", 0, 0, 0.6, 2, 12),
	A1(4, "A1", "橙框球员", 0, 0, 0.4, 1.6, 6),
	A2(5, "A2", "黄框球员", 0, 0, 0.3, 1.5, 5),
	A3(6, "A3", "绿框球员", 0, 0, 0.24, 1.2, 3),
	A(7, "A", "蓝框球员", 0, 0, 0.2, 1, 2.5),
	K(8, "K", "奖牌", 0, 0, 0.16, 0.6, 2.4),
	Q(9, "Q", "球鞋", 0, 0, 0.16, 0.5, 2),
	J(10, "J", "饮料", 0, 0, 0.1, 0.4, 1.6),
	T10(11, "T10", "球场", 0, 0, 0.04, 0.3, 1.4);
	
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	LqjxIconEnum(int id, String name, String desc, double... trs){
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

	public static LqjxIconEnum getById(int id) {
		for(LqjxIconEnum v:values()){
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
		if(this == LqjxIconEnum.SCATTER){
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
		return this == LqjxIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
