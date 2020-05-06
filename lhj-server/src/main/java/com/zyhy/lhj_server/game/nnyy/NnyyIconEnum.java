/**
 * 
 */
package com.zyhy.lhj_server.game.nnyy;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum NnyyIconEnum implements Icon{

	WILD(1, "WILD", "鱼", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "年年有余", 0, 1, 5, 10, 100),
	S1(3, "S1", "龙", 2, 10, 50, 500, 10000),
	A1(4, "A1", "狮子", 0, 5, 25, 250, 5000),
	A2(5, "A2", "红包", 0, 5, 20, 150, 1000),
	A3(6, "A3", "果篮", 0, 5, 20, 150, 1000),
	A(7, "A", "包子", 0, 2, 10, 50, 300),
	K(8, "K", "饺子", 0, 2, 10, 50, 300),
	Q(9, "Q", "灯笼", 0, 0, 5, 25, 200),
	J(10, "J", "鞭炮", 0, 0, 5, 25, 200),
	T10(11, "T10", "福", 0, 0, 3, 10, 50),
	T11(12, "T11", "花", 0, 0, 3, 10, 50);
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	NnyyIconEnum(int id, String name, String desc, double... trs){
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

	public static NnyyIconEnum getById(int id) {
		for(NnyyIconEnum v:values()){
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
		if(this == NnyyIconEnum.SCATTER){
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
		return this == NnyyIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
