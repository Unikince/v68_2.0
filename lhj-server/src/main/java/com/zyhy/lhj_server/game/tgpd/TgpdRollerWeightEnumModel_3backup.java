/**
 * 
 */
package com.zyhy.lhj_server.game.tgpd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum TgpdRollerWeightEnumModel_3backup {

	A2(1, 2, TgpdIconEnum.C2, 400),
	A1(1, 1, TgpdIconEnum.C1, 500),
	A11(1, 11, TgpdIconEnum.FREE, 10),
	A17(1, 17, TgpdIconEnum.POOL, 5),
	A4(1, 4, TgpdIconEnum.C4, 200),
	A5(1, 5, TgpdIconEnum.C5, 100),
	A3(1, 3, TgpdIconEnum.C3, 300),
	A6(1, 6, TgpdIconEnum.C1, 500),
	A8(1, 8, TgpdIconEnum.C3, 300),
	A7(1, 7, TgpdIconEnum.C2, 400),
	A10(1, 10, TgpdIconEnum.C5, 100),
	A9(1, 9, TgpdIconEnum.C4, 200),
	//A11(1, 11, TgpdIconEnum.FREE, 300),
	A13(1, 13, TgpdIconEnum.C2, 400),
	A14(1, 14, TgpdIconEnum.C3, 300),
	A12(1, 12, TgpdIconEnum.C1, 500),
	A16(1, 16, TgpdIconEnum.C5, 100),
	A15(1, 15, TgpdIconEnum.C4, 200),
	
	B1(2, 1, TgpdIconEnum.C1, 500),
	B2(2, 2, TgpdIconEnum.C2, 400),
	B16(2, 16, TgpdIconEnum.POOL, 5),
	B3(2, 3, TgpdIconEnum.C3, 300),
	B4(2, 4, TgpdIconEnum.C4, 200),
	B6(2, 6, TgpdIconEnum.C1, 500),
	B7(2, 7, TgpdIconEnum.C2, 400),
	B9(2, 9, TgpdIconEnum.C4, 200),
	B5(2, 5, TgpdIconEnum.C5, 100),
	B8(2, 8, TgpdIconEnum.C3, 300),
	B11(2, 11, TgpdIconEnum.C1, 500),
	B10(2, 10, TgpdIconEnum.C5, 100),
	B14(2, 14, TgpdIconEnum.C4, 200),
	B12(2, 12, TgpdIconEnum.C2, 400),
	B13(2, 13, TgpdIconEnum.C3, 300),
	B15(2, 15, TgpdIconEnum.C5, 100),
	
	C1(3, 1, TgpdIconEnum.C1, 500),
	C3(3, 3, TgpdIconEnum.C3, 300),
	C4(3, 4, TgpdIconEnum.C4, 200),
	C2(3, 2, TgpdIconEnum.C2, 400),
	C5(3, 5, TgpdIconEnum.C5, 100),
	C6(3, 6, TgpdIconEnum.C1, 500),
	C8(3, 8, TgpdIconEnum.C3, 300),
	C7(3, 7, TgpdIconEnum.C2, 400),
	C10(3, 10, TgpdIconEnum.C5, 100),
	C11(3, 11, TgpdIconEnum.COUNT3, 100),
	C9(3, 9, TgpdIconEnum.C4, 200),
	C13(3, 13, TgpdIconEnum.C2, 400),
	C12(3, 12, TgpdIconEnum.C1, 500),
	C15(3, 15, TgpdIconEnum.C4, 200),
	C14(3, 14, TgpdIconEnum.C3, 300),
	C16(3, 16, TgpdIconEnum.C5, 100),
	
	/*C17(3, 11, TgpdIconEnum.COUNT3, 100),
	C18(3, 11, TgpdIconEnum.COUNT3, 100),
	C19(3, 11, TgpdIconEnum.COUNT3, 100),
	C20(3, 11, TgpdIconEnum.COUNT3, 100),*/
	
	D1(4, 1, TgpdIconEnum.C1, 500),
	D3(4, 3, TgpdIconEnum.C3, 300),
	D4(4, 4, TgpdIconEnum.C4, 200),
	D2(4, 2, TgpdIconEnum.C2, 400),
	D7(4, 7, TgpdIconEnum.C2, 400),
	D5(4, 5, TgpdIconEnum.C5, 100),
	D6(4, 6, TgpdIconEnum.C1, 500),
	D8(4, 8, TgpdIconEnum.C3, 300),
	D10(4, 10, TgpdIconEnum.C5, 100),
	D11(4, 11, TgpdIconEnum.C1, 500),
	D9(4, 9, TgpdIconEnum.C4, 200),
	D13(4, 13, TgpdIconEnum.C3, 300),
	D12(4, 12, TgpdIconEnum.C2, 400),
	D15(4, 15, TgpdIconEnum.C5, 100),
	D14(4, 14, TgpdIconEnum.C4, 200),
	
	E1(5, 1, TgpdIconEnum.C1, 500),
	E2(5, 2, TgpdIconEnum.C2, 400),
	E11(5, 11, TgpdIconEnum.POOL, 5),
	E3(5, 3, TgpdIconEnum.C3, 300),
	E5(5, 5, TgpdIconEnum.C5, 100),
	E6(5, 6, TgpdIconEnum.C1, 500),
	E4(5, 4, TgpdIconEnum.C4, 200),
	E8(5, 8, TgpdIconEnum.C3, 300),
	E7(5, 7, TgpdIconEnum.C2, 400),
	E9(5, 9, TgpdIconEnum.C4, 200),
	E12(5, 12, TgpdIconEnum.C1, 500),
	E10(5, 10, TgpdIconEnum.C5, 100),
	E13(5, 13, TgpdIconEnum.C2, 400),
	E14(5, 14, TgpdIconEnum.C3, 300),
	E16(5, 16, TgpdIconEnum.C5, 100),
	E15(5, 15, TgpdIconEnum.C4, 200),
	
	/*E17(5, 11, TgpdIconEnum.POOL, 300),
	E18(5, 11, TgpdIconEnum.POOL, 400),
	E19(5, 11, TgpdIconEnum.POOL, 500),*/
	
	F2(6, 2, TgpdIconEnum.C2, 400),
	F3(6, 3, TgpdIconEnum.C3, 300),
	F5(6, 5, TgpdIconEnum.C5, 100),
	F1(6, 1, TgpdIconEnum.C1, 500),
	F4(6, 4, TgpdIconEnum.C4, 200),
	F7(6, 7, TgpdIconEnum.C2, 400),
	F6(6, 6, TgpdIconEnum.C1, 500),
	F9(6, 9, TgpdIconEnum.C4, 200),
	F8(6, 8, TgpdIconEnum.C3, 300),
	F10(6, 10, TgpdIconEnum.C5, 100),
	F11(6, 11, TgpdIconEnum.COUNT3, 100),
	F12(6, 12, TgpdIconEnum.C1, 500),
	F15(6, 15, TgpdIconEnum.C4, 200),
	F13(6, 13, TgpdIconEnum.C2, 400),
	F16(6, 16, TgpdIconEnum.C5, 100),
	F14(6, 14, TgpdIconEnum.C3, 300),
	F17(6, 17, TgpdIconEnum.POOL, 5),
	
	/*F18(6, 11, TgpdIconEnum.COUNT3, 100),
	F19(6, 11, TgpdIconEnum.COUNT3, 200),
	F20(6, 11, TgpdIconEnum.COUNT3, 300),
	F21(6, 11, TgpdIconEnum.COUNT3, 400),*/
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private TgpdIconEnum icon;
	//权重
	private int weight;
	
	private TgpdRollerWeightEnumModel_3backup(int id, int index, TgpdIconEnum icon, int weight){
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

	public TgpdIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * 按指定轴取图标
	 * @param 
	 * @return
	 */
	public static Collection<? extends Window> windowInfo(int c) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<TgpdRollerWeightEnumModel_3backup> all = new ArrayList<TgpdRollerWeightEnumModel_3backup>();
		for(TgpdRollerWeightEnumModel_3backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(TgpdRollerWeightEnumModel_3backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		TgpdRollerWeightEnumModel_3backup roller = null;
		Iterator<TgpdRollerWeightEnumModel_3backup> it = all.iterator();
		while(it.hasNext()){
			TgpdRollerWeightEnumModel_3backup e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num <= w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		ws.add(new WindowInfo(c, 1, roller.getIcon()));
		int i=2;
		int index = 0;
		for(;i<=3;i++){
			if(it.hasNext()){
				roller = it.next();
			}else{
				roller = all.get(index);
				index++;
			}
			ws.add(new WindowInfo(c, i, roller.getIcon()));
		}
		return ws;
	}
	
	/**
	 * 按指定轴和位置取图标,不重复取指定图标
	 * @param 
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list,TgpdIconEnum icon) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<TgpdRollerWeightEnumModel_3backup> all = new ArrayList<TgpdRollerWeightEnumModel_3backup>();
		for(TgpdRollerWeightEnumModel_3backup e : values()){
			if(e.getId() == c + 1){
				all.add(e);
			}
		}
		int total = 0;
		for(TgpdRollerWeightEnumModel_3backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		TgpdRollerWeightEnumModel_3backup roller = null;
		Iterator<TgpdRollerWeightEnumModel_3backup> it = all.iterator();
		while(roller == null){
			while(it.hasNext()){
				TgpdRollerWeightEnumModel_3backup e = it.next();
				int w = v + e.getWeight();
				if(num >= v && num <= w){
					roller = e;
					break;
				}else{
					v = w;
				}
			}
		}
		
		if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, roller.getIcon()));
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIcon() == roller.getIcon() 
						&& roller.getIcon() == icon) {
					ws.clear();
					ws.add(windowInfo(c,d,list,icon));
				}else {
					ws.clear();
					ws.add(new WindowInfo(c, d, roller.getIcon()));
				}
			}
		}
		return ws.get(0);
	}
	
	/**
	 * 按指定轴和位置取图标,排除指定图标
	 * @param 
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,TgpdIconEnum icon) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<TgpdRollerWeightEnumModel_3backup> all = new ArrayList<TgpdRollerWeightEnumModel_3backup>();
		for(TgpdRollerWeightEnumModel_3backup e : values()){
			if(e.getId() == c + 1){
				all.add(e);
			}
		}
		int total = 0;
		for(TgpdRollerWeightEnumModel_3backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		TgpdRollerWeightEnumModel_3backup roller = null;
		Iterator<TgpdRollerWeightEnumModel_3backup> it = all.iterator();
		while(roller == null){
			while(it.hasNext()){
				TgpdRollerWeightEnumModel_3backup e = it.next();
				int w = v + e.getWeight();
				if(num >= v && num <= w){
					roller = e;
					break;
				}else{
					v = w;
				}
			}
		}
		if (roller.getIcon() != icon) {
			ws.add(new WindowInfo(c, d, roller.getIcon()));
		} else {
			ws.add(windowInfo(c , d , icon));
		}
		return ws.get(0);
	}
}
