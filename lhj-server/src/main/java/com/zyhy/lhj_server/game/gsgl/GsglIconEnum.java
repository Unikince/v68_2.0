/**
 * 
 */
package com.zyhy.lhj_server.game.gsgl;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum GsglIconEnum implements Icon{

	WILD(1, "WILD", "WILD", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "SCATTER", 0, 0, 5, 10, 100),
	BONUS(3, "BONUS", "BONUS", 0, 0, 0, 0, 0),
	S1(4, "S1", "红色卡车", 0, 10, 50, 500, 10000),
	A1(5, "A1", "加油站", 0, 5, 25, 250, 5000),
	A2(6, "A2", "黄色卡车", 0, 5, 20, 150, 1000),
	A3(7, "A3", "绿色卡车", 0, 5, 20, 150, 1000),
	A(8, "A", "骰子", 0, 0, 10, 50, 300),
	K(9, "K", "帽子", 0, 0, 10, 50, 300),
	Q(10, "Q", "打火石", 0, 0, 5, 25, 200),
	J(11, "J", "扳手", 0, 0, 5, 25, 200),
	T10(12, "T10", "轮子", 0, 0, 3, 10, 50),
	T11(13, "T11", "方向盘", 0, 0, 3, 10, 50);
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	GsglIconEnum(int id, String name, String desc, double... trs){
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

	public static GsglIconEnum getById(int id) {
		for(GsglIconEnum v:values()){
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
		return this == GsglIconEnum.SCATTER;
	}

	@Override
	public boolean isBar() {
		return false;
	}

	@Override
	public boolean isWild() {
		return this == GsglIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return this == GsglIconEnum.BONUS;
	}
	
}
