/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum GghzRollerWeightEnum_backup {

	A1(1, 1, GghzIconEnum.T1, 3),
	A2(1, 2, null, 3),
	A3(1, 3, GghzIconEnum.T2, 4),
	A4(1, 4, null, 1),
	A5(1, 5, GghzIconEnum.T3, 5),
	A6(1, 6, null, 2),
	A7(1, 7, GghzIconEnum.T4, 6),
	A8(1, 8, null, 2),
	A9(1, 9, GghzIconEnum.T5, 3),
	A10(1, 10, null, 2),
	A11(1, 11, GghzIconEnum.T6, 4),
	A12(1, 12, null, 2),
	A13(1, 13, GghzIconEnum.T7, 4),
	A14(1, 14, null, 2),
	A15(1, 15, GghzIconEnum.T8, 4),
	A16(1, 16, null, 2),
	A19(1, 19, GghzIconEnum.T5, 4),
	A20(1, 20, null, 2),
	A21(1, 21, GghzIconEnum.T6, 3),
	A22(1, 22, null, 1),
	A23(1, 23, GghzIconEnum.T7, 4),
	A24(1, 24, null, 3),
	A25(1, 25, GghzIconEnum.T8, 5),
	A26(1, 26, null, 1),
	
	B1(2, 1, GghzIconEnum.T1, 4),
	B2(2, 2, null, 1),
	B3(2, 3, GghzIconEnum.T2, 5),
	B4(2, 4, null, 1),
	B5(2, 5, GghzIconEnum.T3, 4),
	B6(2, 6, null, 1),
	B7(2, 7, GghzIconEnum.T4, 6),
	B8(2, 8, null, 2),
	B9(2, 9, GghzIconEnum.T5, 5),
	B10(2, 10, null, 5),
	B11(2, 11, GghzIconEnum.T6, 6),
	B12(2, 12, null, 1),
	B13(2, 13, GghzIconEnum.T7, 5),
	B14(2, 14, null, 4),
	B15(2, 15, GghzIconEnum.T8, 4),
	B16(2, 16, null, 1),
	B19(2, 19, GghzIconEnum.T5, 2),
	B20(2, 20, null, 1),
	B21(2, 21, GghzIconEnum.T6, 2),
	B22(2, 22, null, 1),
	B23(2, 23, GghzIconEnum.T7, 4),
	B24(2, 24, null, 1),
	B25(2, 25, GghzIconEnum.T8, 5),
	B26(2, 26, null, 1),
	
	C1(3, 1, GghzIconEnum.T1, 5),
	C2(3, 2, null, 5),
	C3(3, 3, GghzIconEnum.T2, 5),
	C4(3, 4, null, 4),
	C5(3, 5, GghzIconEnum.T3, 5),
	C6(3, 6, null, 1),
	C7(3, 7, GghzIconEnum.T4, 6),
	C8(3, 8, null, 1),
	C9(3, 9, GghzIconEnum.T5, 6),
	C10(3, 10, null, 1),
	C11(3, 11, GghzIconEnum.T6, 2),
	C12(3, 12, null, 2),
	C13(3, 13, GghzIconEnum.T7, 6),
	C14(3, 14, null, 1),
	C15(3, 15, GghzIconEnum.T8, 3),
	C18(3, 18, null, 1),
	C19(3, 19, GghzIconEnum.T5, 1),
	C20(3, 20, null, 1),
	C21(3, 21, GghzIconEnum.T6, 5),
	C22(3, 22, null, 1),
	C23(3, 23, GghzIconEnum.T7, 2),
	C24(3, 24, null, 1),
	C25(3, 25, GghzIconEnum.T8, 6),
	C26(3, 26, null, 1),
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private GghzIconEnum icon;
	//权重
	private int weight;
	
	private GghzRollerWeightEnum_backup(int id, int index, GghzIconEnum icon, int weight){
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

	public GghzIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static Collection<? extends GghzWindowInfo> windowInfo(int c) {
		List<GghzWindowInfo> ws = new ArrayList<GghzWindowInfo>();
		List<GghzRollerWeightEnum_backup> all = new ArrayList<GghzRollerWeightEnum_backup>();
		for(GghzRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(GghzRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		GghzRollerWeightEnum_backup roller1 = null;
		GghzRollerWeightEnum_backup roller2 = null;
		GghzRollerWeightEnum_backup roller3 = null;
		Iterator<GghzRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			GghzRollerWeightEnum_backup e = it.next();
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
		ws.add(new GghzWindowInfo(c, 1, 
				new GghzRollerInfo(roller1.getId(),roller1.getIndex(),roller1.getIcon())));
		ws.add(new GghzWindowInfo(c, 2, 
				new GghzRollerInfo(roller2.getId(),roller2.getIndex(),roller2.getIcon())));
		ws.add(new GghzWindowInfo(c, 3, 
				new GghzRollerInfo(roller3.getId(),roller3.getIndex(),roller3.getIcon())));
		return ws;
	}
}
