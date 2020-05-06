/**
 * 
 */
package com.zyhy.lhj_server.game.yhdd;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum YhddIconEnum implements Icon{

	WILD(1, "WILD", "WILD", 0, 10, 200, 2000, 10000),
	SCATTER(2, "SCATTER", "SCATTER", 0, 0, 5, 25, 100),
	BONUS(3, "BONUS", "BONUS", 0, 0, 0, 0, 0),
	S1(4, "S1", "玉皇大帝", 0, 3, 30, 150, 600),
	A1(5, "A1", "王母娘娘", 0, 3, 25, 100, 400),
	A2(6, "A2", "二郎神", 0, 2, 20, 75, 300),
	A3(7, "A3", "财神", 0, 2, 15, 50, 200),
	A(8, "A", "绿玉佩", 0, 0, 10, 40, 150),
	K(9, "K", "白玉佩", 0, 0, 10, 30, 100),
	Q(10, "Q", "红玉佩", 0, 0, 8, 25, 75),
	J(11, "J", "蓝玉佩", 0, 0, 8, 20, 50),
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	YhddIconEnum(int id, String name, String desc, double... trs){
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

	public static YhddIconEnum getById(int id) {
		for(YhddIconEnum v:values()){
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
		return this == YhddIconEnum.SCATTER;
	}

	@Override
	public boolean isBar() {
		return false;
	}

	@Override
	public boolean isWild() {
		return this == YhddIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return this == YhddIconEnum.BONUS;
	}
	
}
