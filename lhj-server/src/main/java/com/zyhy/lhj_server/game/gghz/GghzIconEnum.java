/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.lhj_server.game.Icon;

/**
 * @author ASUS
 *
 */
public enum GghzIconEnum implements Icon{

	T1(1, "T1", "手鼓", 800, 1600, 2400),
	T2(2, "T2", "椰树", 400, 800, 1200),
	T3(3, "T3", "房子", 200, 400, 600),
	T4(4, "T4", "眼镜", 100, 200, 300),
	T5(5, "T5", "香蕉", 80, 160, 240),
	T6(6, "T6", "3BAR", 60, 120, 180),
	T7(7, "T7", "2BAR", 40, 80, 120),
	T8(8, "T8", "1BAR", 20, 40, 60),
	T9(9, "T9", "任意3个BAR", 10, 20, 30),
	NULL(10, "NULL", "空图标", 0, 0, 0),
	
	/*T1(1, "BAR", "手鼓", 800, 1600, 2400),
	T2(2, "777", "椰树", 400, 800, 1200),
	T3(3, "Ace", "房子", 200, 400, 600),
	T4(4, "King", "眼镜", 100, 200, 300),
	T5(5, "Queen", "香蕉", 80, 160, 240),
	T6(6, "Jack", "3BAR", 60, 120, 180),
	T7(7, "10", "2BAR", 40, 80, 120),
	T8(8, "SmallPrize", "1BAR", 20, 40, 60),
	T9(9, "SmallPrize-10-Jack", "任意3个BAR", 10, 20, 30),*/
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double tr1; // 1个赔率
	private double tr2; // 2个赔率
	private double tr3; // 3个赔率
	
	GghzIconEnum(int id, String name, String desc, double tr1, double tr2, double tr3){
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.tr1 = tr1;
		this.tr2 = tr2;
		this.tr3 = tr3;
	}

	public static List<GghzIconInfo> infos() {
		List<GghzIconInfo> ls = new ArrayList<GghzIconInfo>();
		for(GghzIconEnum i : values()) {
			ls.add(new GghzIconInfo(i));
		}
		return ls;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getTr1() {
		return tr1;
	}

	@Override
	public double getTr2() {
		return tr2;
	}

	@Override
	public double getTr3() {
		return tr3;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	public int getId() {
		return id;
	}

	public static GghzIconEnum getById(int id) {
		for(GghzIconEnum v:values()){
			if(v.getId() == id){
				return v;
			}
		}
		return null;
	}
	
}
