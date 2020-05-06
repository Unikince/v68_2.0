/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.lhj_server.game.Icon;

/**
 * @author ASUS
 *
 */
public enum LllIconEnum implements Icon{

	T1(1, "T1", "黄龙", 0, 0, 88),
	T2(2, "T2", "红龙", 0, 0, 58),
	T3(3, "T3", "绿龙", 0, 0, 28),
	T4(4, "T4", "任意3个BAR", 0, 0, 8),
	NULL(5, "NULL", "空图标", 0, 0, 0),
	;
	private int id;
	private String name; // 名字
	private String desc;//描述
	private double tr1; // 1个赔率
	private double tr2; // 2个赔率
	private double tr3; // 3个赔率
	
	LllIconEnum(int id, String name, String desc, double tr1, double tr2, double tr3){
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.tr1 = tr1;
		this.tr2 = tr2;
		this.tr3 = tr3;
	}

	public static List<LllIconInfo> infos() {
		List<LllIconInfo> ls = new ArrayList<LllIconInfo>();
		for(LllIconEnum i : values()) {
			ls.add(new LllIconInfo(i));
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

	public static LllIconEnum getById(int id) {
		for(LllIconEnum v:values()){
			if(v.getId() == id){
				return v;
			}
		}
		return null;
	}
	
}
