/**
 * 
 */
package com.zyhy.lhj_server.game.hjws;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum HjwsIconEnum implements Icon{

	WILD(1, "WILD", "WILD", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "SCATTER", 0, 0, 5, 10, 50),
	S1(3, "S1", "龙", 0, 0, 50, 100, 1000),
	A1(4, "A1", "鱼", 0, 0, 20, 50, 300),
	A2(5, "A2", "扇子", 0, 0, 15, 35, 300),
	A3(6, "A3", "手镯", 0, 0, 30, 100, 800),
	A4(7, "A4", "铜币", 0, 0, 35, 100, 800),
	A(8, "A", "A", 0, 0, 10, 30, 200),
	K(9, "K", "K", 0, 0, 10, 20, 200),
	Q(10, "Q", "Q", 0, 0, 10, 15, 100),
	J(11, "J", "J", 0, 0, 10, 15, 100),
	T10(12, "T10", "10", 0, 0, 5, 15, 100),
	T9(13, "T9", "9", 0, 0, 5, 10, 100);
	
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	HjwsIconEnum(int id, String name, String desc, double... trs){
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

	public static HjwsIconEnum getById(int id) {
		for(HjwsIconEnum v:values()){
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
		if(this == HjwsIconEnum.SCATTER){
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
		return this == HjwsIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
