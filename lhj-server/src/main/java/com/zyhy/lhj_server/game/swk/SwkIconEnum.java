/**
 * 
 */
package com.zyhy.lhj_server.game.swk;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum SwkIconEnum implements Icon{

	WILD(1, "WILD", "孙悟空", 0, 10, 200, 2000, 10000),
	SCATTER(2, "SCATTER", "SCATTER", 0, 2, 5, 25, 100),
	S1(3, "S1", "师父", 0, 2, 30, 200, 600),
	A1(4, "A1", "八戒", 0, 2, 30, 200, 600),
	A2(5, "A2", "沙僧", 0, 0, 20, 100, 400),
	A3(6, "A3", "白龙马", 0, 0, 15, 50, 250),
	A4(7, "A4", "蟠桃", 0, 0, 15, 50, 250),
	A(8, "A", "A", 0, 0, 10, 30, 150),
	K(9, "K", "K", 0, 0, 10, 30, 150),
	Q(10, "Q", "Q", 0, 0, 8, 25, 125),
	J(11, "J", "J", 0, 0, 7, 20, 100),
	T10(12, "T10", "10", 0, 0, 7, 20, 100),
	T9(13, "T9", "9", 0, 0, 5, 15, 50),
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	SwkIconEnum(int id, String name, String desc, double... trs){
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

	public static SwkIconEnum getById(int id) {
		for(SwkIconEnum v:values()){
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
		if(this == SwkIconEnum.SCATTER){
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
		return this == SwkIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
