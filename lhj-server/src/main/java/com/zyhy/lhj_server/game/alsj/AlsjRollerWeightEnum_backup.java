/**
 * 
 */
package com.zyhy.lhj_server.game.alsj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum AlsjRollerWeightEnum_backup {
	
	//A20(1, 19, AlsjIconEnum.BONUS, 500),
	A1(1, 0, AlsjIconEnum.S1, 10),
	A2(1, 1, AlsjIconEnum.A1, 20),
	A3(1, 2, AlsjIconEnum.A2, 30),
	A4(1, 3, AlsjIconEnum.A3, 40),
	A5(1, 4, AlsjIconEnum.A, 100),
	A6(1, 5, AlsjIconEnum.K, 200),
	A7(1, 6, AlsjIconEnum.Q, 300),
	A10(1, 9, AlsjIconEnum.SCATTER, 50),
	A8(1, 7, AlsjIconEnum.J, 400),
	A9(1, 8, AlsjIconEnum.T10, 500),
	A15(1, 14, AlsjIconEnum.A, 100),
	A16(1, 15, AlsjIconEnum.K, 200),
	A17(1, 16, AlsjIconEnum.Q, 300),
	A18(1, 17, AlsjIconEnum.J, 400),
	A19(1, 18, AlsjIconEnum.T10, 500),
	//A10(1, 9, AlsjIconEnum.SCATTER, 1000),
	A20(1, 19, AlsjIconEnum.BONUS, 50),
	
	
	B1(2, 0, AlsjIconEnum.S1, 100),
	B2(2, 1, AlsjIconEnum.A1, 200),
	B3(2, 2, AlsjIconEnum.A2, 300),
	B4(2, 3, AlsjIconEnum.A3, 400),
	B5(2, 4, AlsjIconEnum.A, 10),
	B6(2, 5, AlsjIconEnum.K, 20),
	B7(2, 6, AlsjIconEnum.Q, 30),
	B10(2, 9, AlsjIconEnum.SCATTER, 50),
	B8(2, 7, AlsjIconEnum.J, 40),
	B9(2, 8, AlsjIconEnum.T10, 50),
	B15(2, 14, AlsjIconEnum.S1, 100),
	B16(2, 15, AlsjIconEnum.A1, 200),
	B17(2, 16, AlsjIconEnum.A2, 300),
	B18(2, 17, AlsjIconEnum.A3, 400),
	//B10(2, 9, AlsjIconEnum.SCATTER, 1000),
	B20(2, 19, AlsjIconEnum.WILD, 50),
	
	
	C1(3, 0, AlsjIconEnum.S1, 10),
	C2(3, 1, AlsjIconEnum.A1, 20),
	C3(3, 2, AlsjIconEnum.A2, 30),
	C4(3, 3, AlsjIconEnum.A3, 40),
	C5(3, 4, AlsjIconEnum.A, 100),
	C6(3, 5, AlsjIconEnum.K, 200),
	C7(3, 6, AlsjIconEnum.Q, 300),
	C8(3, 7, AlsjIconEnum.J, 400),
	C10(3, 9, AlsjIconEnum.SCATTER, 50),
	C9(3, 8, AlsjIconEnum.T10, 500),
	C15(3, 14, AlsjIconEnum.A, 100),
	C16(3, 15, AlsjIconEnum.K, 200),
	C17(3, 16, AlsjIconEnum.Q, 300),
	C18(3, 17, AlsjIconEnum.J, 400),
	C19(3, 18, AlsjIconEnum.T10, 500),
	//C10(3, 9, AlsjIconEnum.SCATTER, 1000),
	C20(3, 19, AlsjIconEnum.WILD, 50),
	
	D1(4, 0, AlsjIconEnum.S1, 100),
	D2(4, 1, AlsjIconEnum.A1, 200),
	D3(4, 2, AlsjIconEnum.A2, 300),
	D4(4, 3, AlsjIconEnum.A3, 400),
	D5(4, 4, AlsjIconEnum.A, 10),
	D6(4, 5, AlsjIconEnum.K, 20),
	D7(4, 6, AlsjIconEnum.Q, 30),
	D8(4, 7, AlsjIconEnum.J, 40),
	D9(4, 8, AlsjIconEnum.T10, 50),
	D15(4, 14, AlsjIconEnum.S1, 100),
	D16(4, 15, AlsjIconEnum.A1, 200),
	D10(4, 9, AlsjIconEnum.SCATTER, 50),
	D17(4, 16, AlsjIconEnum.A2, 300),
	D18(4, 17, AlsjIconEnum.A3, 400),
	//D10(4, 9, AlsjIconEnum.SCATTER, 1000),
	D20(4, 19, AlsjIconEnum.WILD, 50),
	
	//E20(5, 19, AlsjIconEnum.BONUS, 500),
	E1(5, 0, AlsjIconEnum.S1, 100),
	E2(5, 1, AlsjIconEnum.A1, 200),
	E3(5, 2, AlsjIconEnum.A2, 200),
	E4(5, 3, AlsjIconEnum.A3, 200),
	E5(5, 4, AlsjIconEnum.A, 300),
	E6(5, 5, AlsjIconEnum.K, 300),
	E7(5, 6, AlsjIconEnum.Q, 300),
	E8(5, 7, AlsjIconEnum.J, 300),
	E9(5, 8, AlsjIconEnum.T10, 400),
	E10(5, 9, AlsjIconEnum.SCATTER, 50),
	//E10(5, 9, AlsjIconEnum.SCATTER, 1000),
	E15(5, 14, AlsjIconEnum.A, 300),
	E16(5, 15, AlsjIconEnum.K, 300),
	E17(5, 16, AlsjIconEnum.Q, 300),
	E18(5, 17, AlsjIconEnum.J, 300),
	E19(5, 18, AlsjIconEnum.T10, 400),
	E20(5, 19, AlsjIconEnum.BONUS, 50),
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private AlsjIconEnum icon;
	//权重
	private int weight;
	
	private AlsjRollerWeightEnum_backup(int id, int index, AlsjIconEnum icon, int weight){
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

	public AlsjIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static AlsjRollerWeightEnum_backup random(int c) {
		List<AlsjRollerWeightEnum_backup> all = new ArrayList<AlsjRollerWeightEnum_backup>();
		for(AlsjRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(AlsjRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(AlsjRollerWeightEnum_backup e : all){
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
	 * @param j 
	 * @param i
	 * @return
	 */
	public static List<WindowInfo> windowInfo(int c) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<AlsjRollerWeightEnum_backup> all = new ArrayList<AlsjRollerWeightEnum_backup>();
		for(AlsjRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(AlsjRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		AlsjRollerWeightEnum_backup roller = null;
		Iterator<AlsjRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			AlsjRollerWeightEnum_backup e = it.next();
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
	 * 获取
	 * @param i
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<AlsjRollerWeightEnum_backup> all = new ArrayList<AlsjRollerWeightEnum_backup>();
		for(AlsjRollerWeightEnum_backup e : values()){
			if(e.getId() == c+1){
				all.add(e);
			}
		}
		int total = 0;
		for(AlsjRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		AlsjRollerWeightEnum_backup roller = null;
		Iterator<AlsjRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			AlsjRollerWeightEnum_backup e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num <= w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, roller.getIcon()));
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIcon() == roller.getIcon() 
						&& roller.getIcon() == AlsjIconEnum.SCATTER) {
					ws.clear();
					ws.add(windowInfo(c,d,list));
				}else {
					ws.clear();
					ws.add(new WindowInfo(c, d, roller.getIcon()));
				}
			}
		}
		return ws.get(0);
	}
}
