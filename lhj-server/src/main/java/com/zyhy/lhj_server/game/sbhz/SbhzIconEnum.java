/**
 * 
 */
package com.zyhy.lhj_server.game.sbhz;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum SbhzIconEnum implements Icon{

	Monkey(1, "Monkey", "猴子", 0, 0, 300),
	Sun(2, "Sun", "太阳", 0, 0, 200),
	S1(3, "S1", "椰树", 0, 0, 100),
	A1(4, "A1", "椰果", 0, 0, 50),
	A2(5, "A2", "香蕉", 0, 0, 25),
	A3(6, "A3", "3BAR", 0, 0, 15),
	A4(7, "A4", "2BAR", 0, 0, 10),
	A5(8, "A5", "1BAR", 0, 0, 5),
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	SbhzIconEnum(int id, String name, String desc, double... trs){
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

	public static SbhzIconEnum getById(int id) {
		for(SbhzIconEnum v:values()){
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
		return false;
	}

	@Override
	public boolean isBar() {
		return false;
	}

	@Override
	public boolean isWild() {
		return false;
	}

	@Override
	public boolean IsBonus() {
		return false;
	}

}
