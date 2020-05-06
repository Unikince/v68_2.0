/**
 * 
 */
package com.zyhy.lhj_server.game.bxlm;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum BxlmIconEnum implements Icon{

	WILD(1, "WILD", "WILD", 0, 0, 100, 250, 1500),
	SCATTER(2, "SCATTER", "SCATTER", 0, 1, 2, 20, 200),
	S1(3, "S1", "红衣女", 0, 0, 30, 100, 500),
	A1(4, "A1", "蓝衣男", 0, 0, 30, 100, 450),
	A2(5, "A2", "灰衣男", 0, 0, 20, 80, 400),
	A3(6, "A3", "绿衣女", 0, 0, 20, 80, 350),
	A4(7, "A4", "房子", 0, 0, 15, 60, 300),
	A5(8, "A5", "书房", 0, 0, 15, 60, 250),
	A(9, "A", "A", 0, 0, 10, 25, 150),
	K(10, "K", "K", 0, 0, 10, 25, 150),
	Q(11, "Q", "Q", 0, 0, 7, 20, 125),
	J(12, "J", "J", 0, 0, 7, 20, 125),
	T10(13, "T10", "10", 0, 0, 5, 15, 100),
	T9(14, "T9", "9", 0, 0, 5, 15, 100);
	
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	BxlmIconEnum(int id, String name, String desc, double... trs){
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

	public static BxlmIconEnum getById(int id) {
		for(BxlmIconEnum v:values()){
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
		if(this == BxlmIconEnum.SCATTER){
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
		return this == BxlmIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
