/**
 * 
 */
package com.zyhy.lhj_server.game.ajxz;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum AjxzIconEnum implements Icon{

	WILD(1, "WILD", "金字塔", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "法老", 0, 0, 0, 0, 0),
	A1(3, "A1", "艳后", 0, 3, 25, 200, 1000),
	A2(4, "A2", "狗头", 0, 2, 20, 150, 800),
	A3(5, "A3", "猫头", 0, 2, 20, 125, 750),
	A4(6, "A4", "吊坠", 0, 0, 15, 100, 600),
	//A5(7, "A5", "项链", 0, 0, 12, 90, 500),
	BONUS(7, "BONUS", "项链", 0, 0, 12, 90, 500),
	//A6(8, "A6", "胸针", 0, 0, 12, 80, 400),
	S1(8, "S1", "胸针", 0, 0, 12, 80, 400),
	A(9, "A", "A", 0, 0, 10, 60, 200),
	K(10, "K", "K", 0, 0, 10, 60, 200),
	Q(11, "Q", "Q", 0, 0, 7, 50, 150),
	J(12, "J", "J", 0, 0, 7, 50, 150),
	T10(13, "T10", "10", 0, 0, 7, 50, 150),
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	AjxzIconEnum(int id, String name, String desc, double... trs){
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

	public static AjxzIconEnum getById(int id) {
		for(AjxzIconEnum v:values()){
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
		if(this == AjxzIconEnum.SCATTER){
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
		return this == AjxzIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
