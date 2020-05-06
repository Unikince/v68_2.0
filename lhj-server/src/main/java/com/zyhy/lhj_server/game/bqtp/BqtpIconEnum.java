/**
 * 
 */
package com.zyhy.lhj_server.game.bqtp;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum BqtpIconEnum implements Icon{

	WILD(1, "WILD", "WILD", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "SCATTER", 0, 0, 5, 50, 250),
	S1(3, "S1", "9号球员", 0, 0, 0.6, 2.5, 10),
	A1(4, "A1", "19号队员", 0, 0, 0.4, 2, 5),
	A2(5, "A2", "守门员", 0, 0, 0.4, 1.6, 4),
	A3(6, "A3", "裁判", 0, 0, 0.3, 1.5, 3.5),
	A(7, "A", "开球", 0, 0, 0.3, 1.2, 3),
	K(8, "K", "场地", 0, 0, 0.2, 0.6, 2.5),
	Q(9, "Q", "头盔", 0, 0, 0.16, 0.5, 2.4),
	J(10, "J", "鞋子", 0, 0, 0.1, 0.4, 2),
	T10(11, "T10", "洗地机", 0, 0, 0.04, 0.3, 1.6);
	
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	BqtpIconEnum(int id, String name, String desc, double... trs){
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

	public static BqtpIconEnum getById(int id) {
		for(BqtpIconEnum v:values()){
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
		if(this == BqtpIconEnum.SCATTER){
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
		return this == BqtpIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
