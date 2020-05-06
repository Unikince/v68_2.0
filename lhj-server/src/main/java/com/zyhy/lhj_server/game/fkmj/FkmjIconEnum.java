/**
 * 
 */
package com.zyhy.lhj_server.game.fkmj;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum FkmjIconEnum implements Icon{

	WILD(1, "WILD", "發", 0, 0, 0, 0, 0),
	SCATTER(2, "SCATTER", "疯狂麻将", 0, 1, 5, 20, 100),
	BONUS(3, "BONUS", "色子", 0, 0, 0, 0, 0),
	S1(4, "S1", "东", 0, 0, 100, 250, 5000),
	A1(5, "A1", "南", 0, 0, 75, 100, 300),
	A2(6, "A2", "西", 0, 0, 60, 120, 200),
	A3(7, "A3", "北", 0, 0, 30, 80, 150),
	A4(8, "A4", "雪花", 0, 0, 10, 45, 100),
	A(9, "A", "树枝", 0, 0, 10, 35, 90),
	K(10, "K", "红中", 0, 2, 5, 10, 30),
	Q(11, "Q", "白板", 0, 2, 5, 10, 30),
	J(12, "J", "太阳", 0, 0, 8, 35, 75),
	T10(13, "T10", "树叶", 0, 0, 6, 25, 60),
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	FkmjIconEnum(int id, String name, String desc, double... trs){
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

	public static FkmjIconEnum getById(int id) {
		for(FkmjIconEnum v:values()){
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
		return this == FkmjIconEnum.SCATTER;
	}

	@Override
	public boolean isBar() {
		return false;
	}

	@Override
	public boolean isWild() {
		return this == FkmjIconEnum.WILD;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
