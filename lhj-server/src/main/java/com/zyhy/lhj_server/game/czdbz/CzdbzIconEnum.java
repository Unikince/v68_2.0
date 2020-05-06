/**
 * 
 */
package com.zyhy.lhj_server.game.czdbz;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum CzdbzIconEnum implements Icon{

	WILD(1, "WILD", "WILD", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "SCATTER", 0, 1, 5, 10, 100),
	S1(3, "S1", "双刀", 2, 10, 100, 500, 5000),
	A1(4, "A1", "地图", 0, 5, 50, 250, 2500),
	A2(5, "A2", "船锚", 0, 3, 20, 100, 1000),
	A3(6, "A3", "船舵", 0, 3, 20, 100, 1000),
	A(7, "A", "A", 0, 0, 10, 30, 500),
	K(8, "K", "K", 0, 0, 5, 25, 300),
	Q(9, "Q", "Q", 0, 0, 5, 20, 200),
	J(10, "J", "J", 0, 0, 5, 20, 200),
	T10(11, "T10", "10", 0, 0, 5, 15, 100),
	T9(12, "T9", "9", 0, 0, 5, 15, 100);
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	CzdbzIconEnum(int id, String name, String desc, double... trs){
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

	public static CzdbzIconEnum getById(int id) {
		for(CzdbzIconEnum v:values()){
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
		return this == CzdbzIconEnum.SCATTER;
	}

	@Override
	public boolean isBar() {
		return false;
	}

	@Override
	public boolean isWild() {
		return this == CzdbzIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
