/**
 * 
 */
package com.zyhy.lhj_server.game.fkmj;

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
public enum FkmjRollerWeightEnum_backup {
	
	//A22(1, 21, FkmjIconEnum.BONUS, 500),
	A1(1, 0, FkmjIconEnum.S1, 50),
	A2(1, 1, FkmjIconEnum.A1, 60),
	A3(1, 2, FkmjIconEnum.A2, 70),
	A4(1, 3, FkmjIconEnum.A3, 80),
	A5(1, 4, FkmjIconEnum.A4, 290),
	A6(1, 5, FkmjIconEnum.A, 100),
	A7(1, 6, FkmjIconEnum.K, 150),
	A8(1, 7, FkmjIconEnum.Q, 200),
	A9(1, 8, FkmjIconEnum.J, 250),
	A22(1, 21, FkmjIconEnum.BONUS, 100),
	A10(1, 9, FkmjIconEnum.T10, 300),
	A12(1, 11, FkmjIconEnum.A, 350),
	A13(1, 12, FkmjIconEnum.K, 400),
	A14(1, 13, FkmjIconEnum.A, 450),
	A15(1, 14, FkmjIconEnum.Q,500),
	A16(1, 15, FkmjIconEnum.J, 550),
	A11(1, 10, FkmjIconEnum.SCATTER, 250),
	
	B1(2, 0, FkmjIconEnum.S1, 50),
	B2(2, 1, FkmjIconEnum.A1, 60),
	B3(2, 2, FkmjIconEnum.A2, 70),
	B4(2, 3, FkmjIconEnum.A3, 80),
	B5(2, 4, FkmjIconEnum.A4, 290),
	B6(2, 5, FkmjIconEnum.A, 100),
	B7(2, 6, FkmjIconEnum.K, 150),
	B8(2, 7, FkmjIconEnum.Q, 200),
	B9(2, 8, FkmjIconEnum.J, 250),
	B10(2, 9, FkmjIconEnum.T10, 300),
	B12(2, 11, FkmjIconEnum.A, 350),
	B13(2, 12, FkmjIconEnum.K, 400),
	B14(2, 13, FkmjIconEnum.A, 450),
	B15(2, 14, FkmjIconEnum.Q,500),
	B16(2, 15, FkmjIconEnum.J, 550),
	B11(2, 10, FkmjIconEnum.SCATTER, 250),
	B22(2, 21, FkmjIconEnum.WILD, 200),
	
	//C23(3, 22, FkmjIconEnum.BONUS, 500),
	C1(3, 0, FkmjIconEnum.S1, 50),
	C2(3, 1, FkmjIconEnum.A1, 60),
	C3(3, 2, FkmjIconEnum.A2, 70),
	C4(3, 3, FkmjIconEnum.A3, 80),
	C5(3, 4, FkmjIconEnum.A4, 290),
	C6(3, 5, FkmjIconEnum.A, 100),
	C7(3, 6, FkmjIconEnum.K, 150),
	C8(3, 7, FkmjIconEnum.Q, 200),
	C23(3, 22, FkmjIconEnum.BONUS, 100),
	C9(3, 8, FkmjIconEnum.J, 250),
	C10(3, 9, FkmjIconEnum.T10, 300),
	C12(3, 11, FkmjIconEnum.A, 350),
	C13(3, 12, FkmjIconEnum.K, 400),
	C14(3, 13, FkmjIconEnum.A, 450),
	C15(3, 14, FkmjIconEnum.Q,500),
	C16(3, 15, FkmjIconEnum.J, 550),
	C11(3, 10, FkmjIconEnum.SCATTER, 250),
	C22(3, 21, FkmjIconEnum.WILD, 200),
	
	D1(4, 0, FkmjIconEnum.S1, 50),
	D2(4, 1, FkmjIconEnum.A1, 60),
	D3(4, 2, FkmjIconEnum.A2, 70),
	D4(4, 3, FkmjIconEnum.A3, 80),
	D5(4, 4, FkmjIconEnum.A4, 290),
	D6(4, 5, FkmjIconEnum.A, 100),
	D7(4, 6, FkmjIconEnum.K, 150),
	D8(4, 7, FkmjIconEnum.Q, 200),
	D9(4, 8, FkmjIconEnum.J, 250),
	D10(4, 9, FkmjIconEnum.T10, 300),
	D12(4, 11, FkmjIconEnum.A, 350),
	D13(4, 12, FkmjIconEnum.K, 400),
	D14(4, 13, FkmjIconEnum.A, 450),
	D15(4, 14, FkmjIconEnum.Q,500),
	D16(4, 15, FkmjIconEnum.J, 550),
	D11(4, 10, FkmjIconEnum.SCATTER, 250),
	D22(4, 21, FkmjIconEnum.WILD, 200),
	
	//E22(5, 21, FkmjIconEnum.BONUS, 500),
	E1(5, 0, FkmjIconEnum.S1, 50),
	E2(5, 1, FkmjIconEnum.A1, 60),
	E3(5, 2, FkmjIconEnum.A2, 70),
	E4(5, 3, FkmjIconEnum.A3, 80),
	E5(5, 4, FkmjIconEnum.A4, 290),
	E6(5, 5, FkmjIconEnum.A, 100),
	E7(5, 6, FkmjIconEnum.K, 150),
	E8(5, 7, FkmjIconEnum.Q, 200),
	E22(5, 21, FkmjIconEnum.BONUS, 100),
	E9(5, 8, FkmjIconEnum.J, 250),
	E10(5, 9, FkmjIconEnum.T10, 300),
	E12(5, 11, FkmjIconEnum.A, 350),
	E13(5, 12, FkmjIconEnum.K, 400),
	E14(5, 13, FkmjIconEnum.A, 450),
	E15(5, 14, FkmjIconEnum.Q,500),
	E16(5, 15, FkmjIconEnum.J, 550),
	E11(5, 10, FkmjIconEnum.SCATTER, 250),
	
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private FkmjIconEnum icon;
	//权重
	private int weight;
	
	private FkmjRollerWeightEnum_backup(int id, int index, FkmjIconEnum icon, int weight){
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

	public FkmjIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static FkmjRollerWeightEnum_backup random(int c) {
		List<FkmjRollerWeightEnum_backup> all = new ArrayList<FkmjRollerWeightEnum_backup>();
		for(FkmjRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(FkmjRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(FkmjRollerWeightEnum_backup e : all){
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
	 **/
	public static Collection<? extends Window> windowInfo(int c) {
		List<FkmjWindowInfo> ws = new ArrayList<FkmjWindowInfo>();
		List<FkmjRollerWeightEnum_backup> all = new ArrayList<FkmjRollerWeightEnum_backup>();
		for(FkmjRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(FkmjRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		FkmjRollerWeightEnum_backup roller = null;
		Iterator<FkmjRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			FkmjRollerWeightEnum_backup e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num <= w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		ws.add(new FkmjWindowInfo(c, 1, roller.getIcon()));
		int i=2;
		int index = 0;
		for(;i<=3;i++){
			if(it.hasNext()){
				roller = it.next();
			}else{
				roller = all.get(index);
				index++;
			}
			ws.add(new FkmjWindowInfo(c, i, roller.getIcon()));
		}
		return ws;
	}
	
	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static FkmjWindowInfo windowInfo(int c,int d,List<FkmjWindowInfo> list) {
		List<FkmjWindowInfo> ws = new ArrayList<FkmjWindowInfo>();
		List<FkmjRollerWeightEnum_backup> all = new ArrayList<FkmjRollerWeightEnum_backup>();
		for(FkmjRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(FkmjRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		FkmjRollerWeightEnum_backup roller = null;
		Iterator<FkmjRollerWeightEnum_backup> it = all.iterator();
		while(roller == null){
			while(it.hasNext()){
				FkmjRollerWeightEnum_backup e = it.next();
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
			ws.add(new  FkmjWindowInfo(c, d, roller.getIcon()));
		} else {
			int count = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIcon() == FkmjIconEnum.BONUS && roller.getIcon() == FkmjIconEnum.BONUS) {
					count ++;
				} 
			}
			if (count > 0) {
				ws.add(windowInfo(c,d,list));
			}else {
				ws.add(new  FkmjWindowInfo(c, d, roller.getIcon()));
			}
		}
		return ws.get(0);
	}

	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static FkmjWindowInfo windowInfo(int c,int d) {
		List<FkmjWindowInfo> ws = new ArrayList<FkmjWindowInfo>();
		List<FkmjRollerWeightEnum_backup> all = new ArrayList<FkmjRollerWeightEnum_backup>();
		for(FkmjRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(FkmjRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		FkmjRollerWeightEnum_backup roller = null;
		Iterator<FkmjRollerWeightEnum_backup> it = all.iterator();
		while(roller == null){
			while(it.hasNext()){
				FkmjRollerWeightEnum_backup e = it.next();
				int w = v + e.getWeight();
				if(num >= v && num <= w){
					roller = e;
					break;
				}else{
					v = w;
				}
			}
		}
		ws.add(new  FkmjWindowInfo(c, d, roller.getIcon()));
		return ws.get(0);
	}
}
