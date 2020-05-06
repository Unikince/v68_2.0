/**
 * 
 */
package com.zyhy.lhj_server.game.lqjx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum LqjxRollerWeightEnum_backup {
	
	//A10(1, 9, LqjxIconEnum.SCATTER, 300),
	A1(1, 0, LqjxIconEnum.S1, 10),
	A2(1, 1, LqjxIconEnum.A1, 15),
	A3(1, 2, LqjxIconEnum.A2, 20),
	A4(1, 3, LqjxIconEnum.A3, 25),
	A5(1, 4, LqjxIconEnum.A, 100),
	A6(1, 5, LqjxIconEnum.K, 150),
	A7(1, 6, LqjxIconEnum.Q, 200),
	A8(1, 7, LqjxIconEnum.J, 250),
	A9(1, 8, LqjxIconEnum.T10, 300),
	A11(1, 10, LqjxIconEnum.A, 100),
	A12(1, 11, LqjxIconEnum.K, 150),
	A13(1, 12, LqjxIconEnum.Q, 200),
	A14(1, 13, LqjxIconEnum.J, 250),
	A19(1, 18, LqjxIconEnum.T10, 300),
	A10(1, 9, LqjxIconEnum.SCATTER, 100),
	
	//B10(2, 9, LqjxIconEnum.SCATTER, 300),
	B1(2, 0, LqjxIconEnum.S1, 100),
	B2(2, 1, LqjxIconEnum.A1, 150),
	B3(2, 2, LqjxIconEnum.A2, 200),
	B4(2, 3, LqjxIconEnum.A3, 250),
	B5(2, 4, LqjxIconEnum.A, 50),
	B6(2, 5, LqjxIconEnum.K, 60),
	B7(2, 6, LqjxIconEnum.Q, 70),
	B8(2, 7, LqjxIconEnum.J, 80),
	B9(2, 8, LqjxIconEnum.T10, 90),
	B10(2, 9, LqjxIconEnum.SCATTER, 100),
	B11(2, 10, LqjxIconEnum.S1, 100),
	B12(2, 11, LqjxIconEnum.A1, 150),
	B13(2, 12, LqjxIconEnum.A2, 200),
	B14(2, 13, LqjxIconEnum.A3, 250),
	
	//C10(3, 9, LqjxIconEnum.SCATTER, 300),
	C1(3, 0, LqjxIconEnum.S1, 20),
	C2(3, 1, LqjxIconEnum.A1, 30),
	C3(3, 2, LqjxIconEnum.A2, 40),
	C4(3, 3, LqjxIconEnum.A3, 50),
	C5(3, 4, LqjxIconEnum.A, 100),
	C6(3, 5, LqjxIconEnum.K, 150),
	C7(3, 6, LqjxIconEnum.Q, 200),
	C8(3, 7, LqjxIconEnum.J, 250),
	C9(3, 8, LqjxIconEnum.T10, 300),
	C10(3, 9, LqjxIconEnum.SCATTER, 100),
	C11(3, 10, LqjxIconEnum.A, 100),
	C12(3, 11, LqjxIconEnum.K, 150),
	C13(3, 12, LqjxIconEnum.Q, 200),
	C14(3, 13, LqjxIconEnum.J, 250),
	C19(3, 18, LqjxIconEnum.T10, 300),
	C20(3, 19, LqjxIconEnum.WILD, 50),
	
	//D10(4, 9, LqjxIconEnum.SCATTER, 300),
	D1(4, 0, LqjxIconEnum.S1, 10),
	D2(4, 1, LqjxIconEnum.A1, 15),
	D3(4, 2, LqjxIconEnum.A2, 20),
	D4(4, 3, LqjxIconEnum.A3, 25),
	D5(4, 4, LqjxIconEnum.A, 100),
	D6(4, 5, LqjxIconEnum.K, 150),
	D7(4, 6, LqjxIconEnum.Q, 200),
	D8(4, 7, LqjxIconEnum.J, 250),
	D9(4, 8, LqjxIconEnum.T10, 300),
	D10(4, 9, LqjxIconEnum.SCATTER, 100),
	D11(4, 10, LqjxIconEnum.A, 100),
	D12(4, 11, LqjxIconEnum.K, 150),
	D13(4, 12, LqjxIconEnum.Q, 200),
	D14(4, 13, LqjxIconEnum.J, 250),
	D15(4, 10, LqjxIconEnum.A, 100),
	D16(4, 11, LqjxIconEnum.K, 150),
	D17(4, 12, LqjxIconEnum.Q, 200),
	D18(4, 13, LqjxIconEnum.J, 250),
	D19(4, 18, LqjxIconEnum.T10, 300),
	D20(4, 19, LqjxIconEnum.WILD, 50),
	
	//E10(5, 9, LqjxIconEnum.SCATTER, 300),
	E1(5, 0, LqjxIconEnum.S1, 10),
	E2(5, 1, LqjxIconEnum.A1, 15),
	E3(5, 2, LqjxIconEnum.A2, 20),
	E4(5, 3, LqjxIconEnum.A3, 25),
	E5(5, 4, LqjxIconEnum.A, 100),
	E6(5, 5, LqjxIconEnum.K, 150),
	E7(5, 6, LqjxIconEnum.Q, 200),
	E8(5, 7, LqjxIconEnum.J, 250),
	E9(5, 8, LqjxIconEnum.T10, 300),
	E10(5, 9, LqjxIconEnum.SCATTER, 100),
	E11(5, 10, LqjxIconEnum.A, 100),
	E12(5, 11, LqjxIconEnum.K, 150),
	E13(5, 12, LqjxIconEnum.Q, 200),
	E14(5, 13, LqjxIconEnum.J, 250),
	E15(5, 14, LqjxIconEnum.A, 300),
	E16(5, 15, LqjxIconEnum.K, 300),
	E17(5, 16, LqjxIconEnum.Q, 300),
	E18(5, 17, LqjxIconEnum.J, 300),
	E19(5, 18, LqjxIconEnum.T10, 400),
	E20(5, 19, LqjxIconEnum.WILD, 50),
	
	/*A1(1, 0, LqjxIconEnum.S1, 100),
	A2(1, 1, LqjxIconEnum.A1, 200),
	A3(1, 2, LqjxIconEnum.A2, 200),
	A4(1, 3, LqjxIconEnum.A3, 200),
	A5(1, 4, LqjxIconEnum.A, 300),
	A6(1, 5, LqjxIconEnum.K, 300),
	A7(1, 6, LqjxIconEnum.Q, 300),
	A8(1, 7, LqjxIconEnum.J, 300),
	A9(1, 8, LqjxIconEnum.T10, 400),
	A10(1, 9, LqjxIconEnum.SCATTER, 50),
	//A10(1, 9, LqjxIconEnum.SCATTER, 1000),
	A11(1, 10, LqjxIconEnum.S1, 100),
	A12(1, 11, LqjxIconEnum.A1, 200),
	A13(1, 12, LqjxIconEnum.A2, 200),
	A14(1, 13, LqjxIconEnum.A3, 200),
	A15(1, 14, LqjxIconEnum.A, 300),
	A16(1, 15, LqjxIconEnum.K, 300),
	A17(1, 16, LqjxIconEnum.Q, 300),
	A18(1, 17, LqjxIconEnum.J, 300),
	A19(1, 18, LqjxIconEnum.T10, 400),
	
	
	B1(2, 0, LqjxIconEnum.S1, 100),
	B2(2, 1, LqjxIconEnum.A1, 200),
	B3(2, 2, LqjxIconEnum.A2, 200),
	B4(2, 3, LqjxIconEnum.A3, 200),
	B5(2, 4, LqjxIconEnum.A, 300),
	B6(2, 5, LqjxIconEnum.K, 300),
	B7(2, 6, LqjxIconEnum.Q, 300),
	B8(2, 7, LqjxIconEnum.J, 300),
	B9(2, 8, LqjxIconEnum.T10, 400),
	B10(2, 9, LqjxIconEnum.SCATTER, 50),
	//B10(2, 9, LqjxIconEnum.SCATTER, 1000),
	B11(2, 10, LqjxIconEnum.S1, 100),
	B12(2, 11, LqjxIconEnum.A1, 200),
	B13(2, 12, LqjxIconEnum.A2, 200),
	B14(2, 13, LqjxIconEnum.A3, 200),
	B15(2, 14, LqjxIconEnum.A, 300),
	B16(2, 15, LqjxIconEnum.K, 300),
	B17(2, 16, LqjxIconEnum.Q, 300),
	B18(2, 17, LqjxIconEnum.J, 300),
	B19(2, 18, LqjxIconEnum.T10, 100),
	
	
	C1(3, 0, LqjxIconEnum.S1, 100),
	C2(3, 1, LqjxIconEnum.A1, 200),
	C3(3, 2, LqjxIconEnum.A2, 200),
	C4(3, 3, LqjxIconEnum.A3, 200),
	C5(3, 4, LqjxIconEnum.A, 300),
	C6(3, 5, LqjxIconEnum.K, 300),
	C7(3, 6, LqjxIconEnum.Q, 300),
	C8(3, 7, LqjxIconEnum.J, 300),
	C9(3, 8, LqjxIconEnum.T10, 400),
	C10(3, 9, LqjxIconEnum.SCATTER, 50),
	//C10(3, 9, LqjxIconEnum.SCATTER, 1000),
	C11(3, 10, LqjxIconEnum.S1, 100),
	C12(3, 11, LqjxIconEnum.A1, 200),
	C13(3, 12, LqjxIconEnum.A2, 200),
	C14(3, 13, LqjxIconEnum.A3, 200),
	C15(3, 14, LqjxIconEnum.A, 300),
	C16(3, 15, LqjxIconEnum.K, 300),
	C17(3, 16, LqjxIconEnum.Q, 300),
	C18(3, 17, LqjxIconEnum.J, 300),
	C19(3, 18, LqjxIconEnum.T10, 400),
	C20(3, 19, LqjxIconEnum.WILD, 100),
	
	D1(4, 0, LqjxIconEnum.S1, 100),
	D2(4, 1, LqjxIconEnum.A1, 200),
	D3(4, 2, LqjxIconEnum.A2, 200),
	D4(4, 3, LqjxIconEnum.A3, 200),
	D5(4, 4, LqjxIconEnum.A, 300),
	D6(4, 5, LqjxIconEnum.K, 300),
	D7(4, 6, LqjxIconEnum.Q, 300),
	D8(4, 7, LqjxIconEnum.J, 300),
	D9(4, 8, LqjxIconEnum.T10, 400),
	D10(4, 9, LqjxIconEnum.SCATTER, 50),
	//D10(4, 9, LqjxIconEnum.SCATTER, 1000),
	D11(4, 10, LqjxIconEnum.S1, 100),
	D12(4, 11, LqjxIconEnum.A1, 200),
	D13(4, 12, LqjxIconEnum.A2, 200),
	D14(4, 13, LqjxIconEnum.A3, 200),
	D15(4, 14, LqjxIconEnum.A, 300),
	D16(4, 15, LqjxIconEnum.K, 300),
	D17(4, 16, LqjxIconEnum.Q, 300),
	D18(4, 17, LqjxIconEnum.J, 300),
	D19(4, 18, LqjxIconEnum.T10, 400),
	D20(4, 19, LqjxIconEnum.WILD, 100),
	
	E1(5, 0, LqjxIconEnum.S1, 100),
	E2(5, 1, LqjxIconEnum.A1, 200),
	E3(5, 2, LqjxIconEnum.A2, 200),
	E4(5, 3, LqjxIconEnum.A3, 200),
	E5(5, 4, LqjxIconEnum.A, 300),
	E6(5, 5, LqjxIconEnum.K, 300),
	E7(5, 6, LqjxIconEnum.Q, 300),
	E8(5, 7, LqjxIconEnum.J, 300),
	E9(5, 8, LqjxIconEnum.T10, 400),
	E10(5, 9, LqjxIconEnum.SCATTER, 50),
	//E10(5, 9, LqjxIconEnum.SCATTER, 1000),
	E11(5, 10, LqjxIconEnum.S1, 100),
	E12(5, 11, LqjxIconEnum.A1, 200),
	E13(5, 12, LqjxIconEnum.A2, 200),
	E14(5, 13, LqjxIconEnum.A3, 200),
	E15(5, 14, LqjxIconEnum.A, 300),
	E16(5, 15, LqjxIconEnum.K, 300),
	E17(5, 16, LqjxIconEnum.Q, 300),
	E18(5, 17, LqjxIconEnum.J, 300),
	E19(5, 18, LqjxIconEnum.T10, 400),
	E20(5, 19, LqjxIconEnum.WILD, 100),*/

	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private LqjxIconEnum icon;
	//权重
	private int weight;
	
	private LqjxRollerWeightEnum_backup(int id, int index, LqjxIconEnum icon, int weight){
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

	public LqjxIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static LqjxRollerWeightEnum_backup random(int c) {
		List<LqjxRollerWeightEnum_backup> all = new ArrayList<LqjxRollerWeightEnum_backup>();
		for(LqjxRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(LqjxRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(LqjxRollerWeightEnum_backup e : all){
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
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<LqjxRollerWeightEnum_backup> all = new ArrayList<LqjxRollerWeightEnum_backup>();
		for(LqjxRollerWeightEnum_backup e : values()){
			if(e.getId() == c+1){
				all.add(e);
			}
		}
		int total = 0;
		for(LqjxRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		LqjxRollerWeightEnum_backup roller = null;
		Iterator<LqjxRollerWeightEnum_backup> it = all.iterator();
		while(roller == null){
			while(it.hasNext()){
				LqjxRollerWeightEnum_backup e = it.next();
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
						&& roller.getIcon() == LqjxIconEnum.SCATTER) {
					ws.clear();
					ws.add(windowInfo(c,d,list));
				}else {
					ws.clear();
					ws.add(new WindowInfo(c, d, roller.getIcon()));
				}
			}
		}
		
		/*if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, roller.getIcon()));
		} else {
			for (WindowInfo wi : list) {
				if (wi.getIcon() == roller.getIcon() 
						&& roller.getIcon() == LqjxIconEnum.SCATTER) {
					ws.add(windowInfo(c,d,list));
				} else {
					ws.add(new WindowInfo(c, d, roller.getIcon()));
				}
			}
		}*/
		return ws.get(0);
	}
}
