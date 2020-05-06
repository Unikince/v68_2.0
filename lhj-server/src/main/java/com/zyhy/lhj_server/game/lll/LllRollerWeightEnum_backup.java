/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum LllRollerWeightEnum_backup {

	A1(1, 1, LllIconEnum.T1, 4),
	A2(1, 2, null, 8),
	A3(1, 3, LllIconEnum.T2, 5),
	A4(1, 4, null, 8),
	A5(1, 5, LllIconEnum.T3, 6),
	A6(1, 6, null, 8),
	A7(1, 7, LllIconEnum.T1, 4),
	A8(1, 8, null, 8),
	A9(1, 9, LllIconEnum.T2, 5),
	A10(1, 10, null, 10),
	A11(1, 11, LllIconEnum.T3, 6),
	
	B1(2, 1, LllIconEnum.T1, 4),
	B2(2, 2, null, 8),
	B3(2, 3, LllIconEnum.T2, 5),
	B4(2, 4, null, 8),
	B5(2, 5, LllIconEnum.T3, 6),
	B6(2, 6, null, 8),
	B7(2, 7, LllIconEnum.T1, 4),
	B8(2, 8, null, 8),
	B9(2, 9, LllIconEnum.T2, 5),
	B10(2, 10, null, 10),
	B11(2, 11, LllIconEnum.T3, 6),

	C1(3, 1, LllIconEnum.T1, 4),
	C2(3, 2, null, 8),
	C3(3, 3, LllIconEnum.T2, 5),
	C4(3, 4, null, 8),
	C5(3, 5, LllIconEnum.T3, 6),
	C6(3, 6, null, 8),
	C7(3, 7, LllIconEnum.T1, 5),
	C8(3, 8, null, 8),
	C9(3, 9, LllIconEnum.T2, 5),
	C10(3, 10, null, 9),
	C11(3, 11, LllIconEnum.T3, 6),
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private LllIconEnum icon;
	//权重
	private int weight;
	
	private LllRollerWeightEnum_backup(int id, int index, LllIconEnum icon, int weight){
		this.id = id;
		this.index = index;
		this.icon = icon;
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public LllIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static LllRollerWeightEnum_backup random(int c) {
		List<LllRollerWeightEnum_backup> all = new ArrayList<LllRollerWeightEnum_backup>();
		for(LllRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(LllRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(LllRollerWeightEnum_backup e : all){
			int w = v + e.getWeight();
			if(num >= v && num < w){
				return e;
			}else{
				v = w;
			}
		}
		return null;
	}

	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static Collection<? extends LllWindowInfo> windowInfo(int c) {
		List<LllWindowInfo> ws = new ArrayList<LllWindowInfo>();
		List<LllRollerWeightEnum_backup> all = new ArrayList<LllRollerWeightEnum_backup>();
		for(LllRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(LllRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		LllRollerWeightEnum_backup roller1 = null;
		LllRollerWeightEnum_backup roller2 = null;
		LllRollerWeightEnum_backup roller3 = null;
		Iterator<LllRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			LllRollerWeightEnum_backup e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num <= w){
				roller2 = e;
				break;
			}else{
				roller1 = e;
				v = w;
			}
		}
		if(roller1 == null){
			roller1 = all.get(all.size() - 1);
		}
		if(it.hasNext()){
			roller3 = it.next();
		}else{
			roller3 = all.get(0);
		}
		ws.add(new LllWindowInfo(c, 1, 
				new LllRollerInfo(roller1.getId(),roller1.getIndex(),roller1.getIcon())));
		ws.add(new LllWindowInfo(c, 2, 
				new LllRollerInfo(roller2.getId(),roller2.getIndex(),roller2.getIcon())));
		ws.add(new LllWindowInfo(c, 3, 
				new LllRollerInfo(roller3.getId(),roller3.getIndex(),roller3.getIcon())));
		return ws;
	}
}
