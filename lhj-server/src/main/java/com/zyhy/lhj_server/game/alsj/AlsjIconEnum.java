/**
 * 
 */
package com.zyhy.lhj_server.game.alsj;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum AlsjIconEnum implements Icon{

	WILD(1, "WILD", "WILD", 0, 0, 60, 100, 500),
	SCATTER(2, "SCATTER", "SCATTER", 0, 0, 5, 10, 100),
	BONUS(3, "BONUS", "BONUS", 0, 0, 50, 80, 400),
	S1(4, "S1", "奖杯", 0, 0, 30, 60, 350),
	A1(5, "A1", "一条鱼", 0, 0, 20, 50, 300),
	A2(6, "A2", "两条鱼", 0, 0, 15, 40, 200),
	A3(7, "A3", "大棕熊", 0, 0, 7, 20, 100),
	A(8, "A", "老鹰", 0, 0, 7, 20, 100),
	K(9, "K", "飞机", 0, 0, 5, 15, 80),
	Q(10, "Q", "垂钓台", 0, 0, 5, 15, 80),
	J(11, "J", "转轴", 0, 0, 3, 10, 60),
	T10(12, "T10", "鱼钩", 0, 0, 3, 10, 60),
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	AlsjIconEnum(int id, String name, String desc, double... trs){
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

	public static AlsjIconEnum getById(int id) {
		for(AlsjIconEnum v:values()){
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
		return this == AlsjIconEnum.SCATTER;
	}

	@Override
	public boolean isBar() {
		return false;
	}

	@Override
	public boolean isWild() {
		return this == AlsjIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return this == AlsjIconEnum.BONUS;
	}
	
}
