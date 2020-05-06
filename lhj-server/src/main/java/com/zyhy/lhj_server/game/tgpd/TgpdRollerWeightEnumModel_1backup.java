/**
 * 
 */
package com.zyhy.lhj_server.game.tgpd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum TgpdRollerWeightEnumModel_1backup {
	
	A1(1, 1, TgpdIconEnum.A1, 3200),
	A2(1, 2, TgpdIconEnum.A2, 2400),
	A3(1, 3, TgpdIconEnum.A3, 1200),
	A4(1, 4, TgpdIconEnum.A4, 960),
	A5(1, 5, TgpdIconEnum.A5, 240),
	A6(1, 6, TgpdIconEnum.COUNT1, 1500),
	A7(1, 7, TgpdIconEnum.FREE, 100),
	
	B1(2, 1, TgpdIconEnum.A1, 3200),
	B2(2, 2, TgpdIconEnum.A2, 2400),
	B3(2, 3, TgpdIconEnum.A3, 1200),
	B4(2, 4, TgpdIconEnum.A4, 960),
	B5(2, 5, TgpdIconEnum.A5, 240),
	B6(2, 6, TgpdIconEnum.COUNT1, 1500),
	B7(2, 7, TgpdIconEnum.FREE, 100),
	
	C1(3, 1, TgpdIconEnum.A1, 3200),
	C2(3, 2, TgpdIconEnum.A2, 2400),
	C3(3, 3, TgpdIconEnum.A3, 1200),
	C4(3, 4, TgpdIconEnum.A4, 960),
	C5(3, 5, TgpdIconEnum.A5, 240),
	C6(3, 6, TgpdIconEnum.COUNT1, 1500),
	C7(3, 7, TgpdIconEnum.FREE, 100),
	
	D1(4, 1, TgpdIconEnum.A1, 3200),
	D2(4, 2, TgpdIconEnum.A2, 2400),
	D3(4, 3, TgpdIconEnum.A3, 1200),
	D4(4, 4, TgpdIconEnum.A4, 960),
	D5(4, 5, TgpdIconEnum.A5, 240),
	D6(4, 6, TgpdIconEnum.COUNT1, 1500),
	D7(4, 7, TgpdIconEnum.FREE, 100),
	
	
	
	
	/*A2(1, 2, TgpdIconEnum.A2, 400),
	A11(1, 11, TgpdIconEnum.FREE, 10),
	A4(1, 4, TgpdIconEnum.A4, 200),
	A1(1, 1, TgpdIconEnum.A1, 500),
	A3(1, 3, TgpdIconEnum.A3, 300),
	A6(1, 6, TgpdIconEnum.A1, 500),
	A7(1, 7, TgpdIconEnum.A2, 400),
	A5(1, 5, TgpdIconEnum.A5, 100),
	A9(1, 9, TgpdIconEnum.A4, 200),
	A8(1, 8, TgpdIconEnum.A3, 300),
	A10(1, 10, TgpdIconEnum.A5, 100),
	//A11(1, 11, TgpdIconEnum.FREE, 300),
	A13(1, 13, TgpdIconEnum.A2, 400),
	A12(1, 12, TgpdIconEnum.A1, 500),
	A14(1, 14, TgpdIconEnum.A3, 300),
	//A15(1, 15, TgpdIconEnum.A4, 300),
	//A16(1, 16, TgpdIconEnum.A5, 300),
	
	B2(2, 2, TgpdIconEnum.A2, 400),
	B3(2, 3, TgpdIconEnum.A3, 300),
	B5(2, 5, TgpdIconEnum.A5, 100),
	B1(2, 1, TgpdIconEnum.A1, 500),
	B4(2, 4, TgpdIconEnum.A4, 200),
	B6(2, 6, TgpdIconEnum.COUNT1, 100),
	B7(2, 7, TgpdIconEnum.A1, 500),
	B9(2, 9, TgpdIconEnum.A3, 300),
	B10(2, 10, TgpdIconEnum.A4, 200),
	B8(2, 8, TgpdIconEnum.A2, 400),
	B13(2, 13, TgpdIconEnum.A2, 400),
	B11(2, 11, TgpdIconEnum.A5, 100),
	B12(2, 12, TgpdIconEnum.A1, 400),
	B14(2, 14, TgpdIconEnum.A3, 300),
	//B15(2, 15, TgpdIconEnum.A4, 300),
	//B16(2, 16, TgpdIconEnum.A5, 300),
	
	B17(2, 6, TgpdIconEnum.COUNT1, 100),
	B18(2, 6, TgpdIconEnum.COUNT1, 200),
	B19(2, 6, TgpdIconEnum.COUNT1, 300),
	
	C12(3, 12, TgpdIconEnum.POOL, 5),
	C3(3, 3, TgpdIconEnum.A3, 300),
	C4(3, 4, TgpdIconEnum.A4, 200),
	C2(3, 2, TgpdIconEnum.A2, 400),
	C5(3, 5, TgpdIconEnum.A5, 100),
	C1(3, 1, TgpdIconEnum.A1, 500),
	C6(3, 6, TgpdIconEnum.COUNT1, 100),
	C7(3, 7, TgpdIconEnum.A1, 500),
	C9(3, 9, TgpdIconEnum.A3, 300),
	C8(3, 8, TgpdIconEnum.A2, 400),
	C11(3, 11, TgpdIconEnum.A5, 100),
	C10(3, 10, TgpdIconEnum.A4, 200),
	C14(3, 14, TgpdIconEnum.A2, 400),
	C13(3, 13, TgpdIconEnum.A1, 500),
	C15(3, 15, TgpdIconEnum.A3, 300),
	//C16(3, 16, TgpdIconEnum.A4, 300),
	//C17(3, 17, TgpdIconEnum.A5, 300),
	
	C18(3, 6, TgpdIconEnum.COUNT1, 300),
	C19(3, 6, TgpdIconEnum.COUNT1, 400),
	C20(3, 6, TgpdIconEnum.COUNT1, 500),
	
	D5(4, 5, TgpdIconEnum.A5, 100),
	D4(4, 4, TgpdIconEnum.A4, 200),
	D3(4, 3, TgpdIconEnum.A3, 300),
	D2(4, 2, TgpdIconEnum.A2, 400),
	D1(4, 1, TgpdIconEnum.A1, 500),
	D6(4, 6, TgpdIconEnum.A1, 500),
	D7(4, 7, TgpdIconEnum.A2, 400),
	D8(4, 8, TgpdIconEnum.A3, 300),
	D9(4, 9, TgpdIconEnum.A4, 200),
	D10(4, 10, TgpdIconEnum.A5, 100),
	D11(4, 11, TgpdIconEnum.POOL, 5),
	D12(4, 1, TgpdIconEnum.A1, 500),
	D13(4, 2, TgpdIconEnum.A2, 400),
	D14(4, 3, TgpdIconEnum.A3, 300),
	//D15(4, 4, TgpdIconEnum.A4, 300),
	//D16(4, 5, TgpdIconEnum.A5, 300),
*/	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private TgpdIconEnum icon;
	//权重
	private int weight;
	
	private TgpdRollerWeightEnumModel_1backup(int id, int index, TgpdIconEnum icon, int weight){
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
	 * 取指定轴一个图标
	 * @param 
	 * @return
	 */
	public static WindowInfo windowInfo(int c) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<TgpdRollerWeightEnumModel_1backup> all = new ArrayList<TgpdRollerWeightEnumModel_1backup>();
		for(TgpdRollerWeightEnumModel_1backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(TgpdRollerWeightEnumModel_1backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		TgpdRollerWeightEnumModel_1backup roller = null;
		Iterator<TgpdRollerWeightEnumModel_1backup> it = all.iterator();
		while(it.hasNext()){
			TgpdRollerWeightEnumModel_1backup e = it.next();
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
		return ws.get(0);
	}
	
	/**
	 * 按指定轴和位置取图标,不重复取指定图标
	 * @param 
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list,TgpdIconEnum icon) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<TgpdRollerWeightEnumModel_1backup> all = new ArrayList<TgpdRollerWeightEnumModel_1backup>();
		for(TgpdRollerWeightEnumModel_1backup e : values()){
			if(e.getId() == c + 1){
				all.add(e);
			}
		}
		int total = 0;
		for(TgpdRollerWeightEnumModel_1backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		TgpdRollerWeightEnumModel_1backup roller = null;
		Iterator<TgpdRollerWeightEnumModel_1backup> it = all.iterator();
		while(roller == null){
			while(it.hasNext()){
				TgpdRollerWeightEnumModel_1backup e = it.next();
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
		List<TgpdRollerWeightEnumModel_1backup> all = new ArrayList<TgpdRollerWeightEnumModel_1backup>();
		for(TgpdRollerWeightEnumModel_1backup e : values()){
			if(e.getId() == c + 1){
				all.add(e);
			}
		}
		int total = 0;
		for(TgpdRollerWeightEnumModel_1backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		TgpdRollerWeightEnumModel_1backup roller = null;
		Iterator<TgpdRollerWeightEnumModel_1backup> it = all.iterator();
		while(roller == null){
			while(it.hasNext()){
				TgpdRollerWeightEnumModel_1backup e = it.next();
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
