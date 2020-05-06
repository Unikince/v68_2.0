/**
 * 
 */
package com.zyhy.lhj_server.game.tgpd;

import com.zyhy.common_lhj.Icon;

/**
 * @author ASUS
 *
 */
public enum TgpdIconEnum implements Icon{

	COUNT1(1, "COUNT1", "魔法棒", 0, 0, 0, 0, 0),
	COUNT2(1, "COUNT2", "糖果", 0, 0, 0, 0, 0),
	COUNT3(1, "COUNT3", "五角星", 0, 0, 0, 0, 0),
	FREE(2, "FREE", "巧克力球", 0, 0, 2, 10, 20),
	POOL(3, "POOL", "螺旋糖果球", 10, 100, 1000, 5000),
	
	A1(4, "A1", "豆子", 0, 0, 0, 2, 4, 5, 8, 10, 20, 30, 50, 100, 200, 400),
	A2(5, "A2", "椭圆", 0, 0, 0, 4, 5, 10, 20, 30, 50, 100, 250, 500, 750, 800),
	A3(6, "A3", "方形", 0, 0, 0, 5, 10, 20, 40, 80, 160, 500, 1000, 2000, 5000, 6000),
	A4(7, "A4", "圆形", 0, 0, 0, 10, 30, 50, 60, 100, 750, 1000, 10000, 20000, 50000, 60000),
	A5(8, "A5", "六角星", 0, 0, 0, 20, 50, 100, 500, 1000, 2000, 5000, 20000, 50000, 60000, 80000),
	
	B1(9, "B1", "条纹豆子", 0, 0, 0, 0, 2, 4, 5, 8, 10, 20, 30, 50, 100, 200, 450),
	B2(10, "B2", "条纹椭圆", 0, 0, 0, 0, 4, 5, 10, 20, 30, 50, 100, 250, 500, 750, 1000),
	B3(11, "B3", "条纹方形", 0, 0, 0, 0, 5, 10, 20, 40, 80, 160, 500, 1000, 2000, 5000, 7000),
	B4(12, "B4", "条纹圆形", 0, 0, 0, 0, 10, 30, 50, 60, 100, 750, 1000, 10000, 20000, 50000, 70000),
	B5(13, "B5", "条纹六角星", 0, 0, 0, 0, 20, 50, 100, 500, 1000, 2000, 5000, 20000, 50000, 80000, 100000),
	
	C1(9, "C1", "口袋豆子", 0, 0, 0, 0, 0, 2, 4, 5, 8, 10, 20, 30, 50, 100, 200, 500),
	C2(10, "C2", "口袋椭圆", 0, 0, 0, 0, 0, 4, 5, 10, 20, 30, 50, 100, 250, 500, 750, 1200),
	C3(11, "C3", "口袋方形", 0, 0, 0, 0, 0, 5, 10, 20, 40, 80, 160, 500, 1000, 2000, 5000, 8000),
	C4(12, "C4", "口袋圆形", 0, 0, 0, 0, 0, 10, 30, 50, 60, 100, 750, 1000, 10000, 20000, 50000, 80000),
	C5(13, "C5", "口袋六角星", 0, 0, 0, 0, 0, 20, 50, 100, 500, 1000, 2000, 5000, 20000, 50000, 100000, 100000),
	
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double[] trs;
	
	TgpdIconEnum(int id, String name, String desc, double... trs){
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

	public static TgpdIconEnum getById(int id) {
		for(TgpdIconEnum v:values()){
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
