/**
 * 
 */
package com.zyhy.lhj_server.game.yzhx;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum YzhxIconEnum implements Icon{

	WILD1(1, "WILD1", "美女", 0, 0, 0, 0, 0),
	WILD2(1, "WILD2", "美女", 0, 0, 0, 0, 0),
	WILD3(1, "WILD3", "美女", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "龙头", 0, 0, 2, 10, 20),
	A1(3, "A1", "士兵", 0, 5, 50, 150, 300),
	A2(4, "A2", "头盔", 0, 2, 20, 75, 150),
	A3(5, "A3", "武器", 0, 2, 20, 75, 150),
	A4(6, "A4", "盾牌", 0, 2, 20, 75, 150),
	A(7, "A", "A", 0, 0, 10, 30, 125),
	K(8, "K", "K", 0, 0, 10, 30, 125),
	Q(9, "Q", "Q", 0, 0, 10, 30, 125),
	J(10, "J", "J", 0, 0, 5, 20, 100),
	T10(11, "T10", "10", 0, 0, 5, 20, 100),
	//T9(12, "T9", "9", 0, 0, 5, 20, 100),
	S1(12, "S1", "9", 0, 0, 5, 20, 100),
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	YzhxIconEnum(int id, String name, String desc, double... trs){
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

	public static YzhxIconEnum getById(int id) {
		for(YzhxIconEnum v:values()){
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
		if(this == YzhxIconEnum.SCATTER){
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
		if (this == YzhxIconEnum.WILD1 || this == YzhxIconEnum.WILD2 || this == YzhxIconEnum.WILD3) {
			return true;
		}
		return false ;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
