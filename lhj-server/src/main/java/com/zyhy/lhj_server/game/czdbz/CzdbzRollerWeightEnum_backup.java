/**
 * 
 */
package com.zyhy.lhj_server.game.czdbz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum CzdbzRollerWeightEnum_backup {
	
	A1(1, 1, CzdbzIconEnum.SCATTER, 0),
	A3(1, 3, CzdbzIconEnum.A2, 0),
	A4(1, 4, CzdbzIconEnum.A, 60),
	A5(1, 5, CzdbzIconEnum.K, 80),
	A6(1, 6, CzdbzIconEnum.A1, 0),
	A7(1, 7, CzdbzIconEnum.Q, 100),
	A8(1, 8, CzdbzIconEnum.J, 200),
	A9(1, 9, CzdbzIconEnum.T10, 250),
	A10(1, 10, CzdbzIconEnum.T9, 300),
	A11(1, 11, CzdbzIconEnum.A3, 0),
	A12(1, 12, CzdbzIconEnum.Q, 100),
	A13(1, 13, CzdbzIconEnum.S1, 0),
	A14(1, 14, CzdbzIconEnum.J, 200),
	A15(1, 15, CzdbzIconEnum.T10, 250),
	A16(1, 16, CzdbzIconEnum.T9, 300),
	A17(1, 17, CzdbzIconEnum.Q, 100),
	A18(1, 18, CzdbzIconEnum.J, 200),
	A19(1, 19, CzdbzIconEnum.T10, 250),
	A20(1, 20, CzdbzIconEnum.T9, 300),
	
	B1(2, 1, CzdbzIconEnum.SCATTER, 0),
	B2(2, 0, CzdbzIconEnum.WILD, 1),
	B3(2, 4, CzdbzIconEnum.A2, 0),
	B4(2, 3, CzdbzIconEnum.A1, 0),
	B5(2, 8, CzdbzIconEnum.Q, 100),
	B6(2, 2, CzdbzIconEnum.S1, 0),
	B7(2, 9, CzdbzIconEnum.J, 200),
	B8(2, 10, CzdbzIconEnum.T10, 250),
	B9(2, 11, CzdbzIconEnum.T9, 300),
	B10(2, 12, CzdbzIconEnum.A, 60),
	B11(2, 5, CzdbzIconEnum.A3, 0),
	B12(2, 13, CzdbzIconEnum.K, 80),
	B13(2, 14, CzdbzIconEnum.Q, 100),
	B14(2, 15, CzdbzIconEnum.J, 200),
	B15(2, 16, CzdbzIconEnum.T10, 250),
	B16(2, 17, CzdbzIconEnum.T9, 300),
	B17(2, 14, CzdbzIconEnum.Q, 100),
	B18(2, 15, CzdbzIconEnum.J, 200),
	B19(2, 16, CzdbzIconEnum.T10, 250),
	B20(2, 17, CzdbzIconEnum.T9, 300),
	
	C2(3, 1, CzdbzIconEnum.SCATTER, 0),
	C1(3, 0, CzdbzIconEnum.WILD, 1),
	C5(3, 4, CzdbzIconEnum.A2, 0),
	C3(3, 2, CzdbzIconEnum.S1, 0),
	C4(3, 3, CzdbzIconEnum.A1, 0),
	C9(3, 8, CzdbzIconEnum.Q, 100),
	C10(3, 9, CzdbzIconEnum.J, 200),
	C11(3, 10, CzdbzIconEnum.T10, 250),
	C12(3, 11, CzdbzIconEnum.T9, 300),
	C13(3, 12, CzdbzIconEnum.A, 60),
	C6(3, 5, CzdbzIconEnum.A3, 0),
	C14(3, 13, CzdbzIconEnum.K, 80),
	C15(3, 14, CzdbzIconEnum.Q, 100),
	C16(3, 15, CzdbzIconEnum.J, 200),
	C17(3, 16, CzdbzIconEnum.T10, 250),
	C18(3, 17, CzdbzIconEnum.T9, 300),
	C19(3, 14, CzdbzIconEnum.Q, 100),
	C20(3, 15, CzdbzIconEnum.J, 200),
	C21(3, 16, CzdbzIconEnum.T10, 250),
	C22(3, 17, CzdbzIconEnum.T9, 300),
	
	D2(4, 1, CzdbzIconEnum.SCATTER, 0),
	D1(4, 0, CzdbzIconEnum.WILD, 1),
	D5(4, 4, CzdbzIconEnum.A2, 0),
	D4(4, 3, CzdbzIconEnum.A1, 0),
	D9(4, 8, CzdbzIconEnum.Q, 100),
	D10(4, 9, CzdbzIconEnum.J, 200),
	D11(4, 10, CzdbzIconEnum.T10, 250),
	D12(4, 11, CzdbzIconEnum.T9, 300),
	D13(4, 12, CzdbzIconEnum.A, 60),
	D6(4, 5, CzdbzIconEnum.A3, 0),
	D14(4, 13, CzdbzIconEnum.K, 80),
	D15(4, 14, CzdbzIconEnum.Q, 100),
	D16(4, 15, CzdbzIconEnum.J, 200),
	D17(4, 16, CzdbzIconEnum.T10, 250),
	D18(4, 17, CzdbzIconEnum.T9, 300),
	D3(4, 2, CzdbzIconEnum.S1, 0),
	D19(4, 14, CzdbzIconEnum.Q, 100),
	D20(4, 15, CzdbzIconEnum.J, 200),
	D21(4, 16, CzdbzIconEnum.T10, 250),
	D22(4, 17, CzdbzIconEnum.T9, 300),
	
	E2(5, 1, CzdbzIconEnum.SCATTER, 0),
	E5(5, 4, CzdbzIconEnum.A2, 0),
	E4(5, 3, CzdbzIconEnum.A1, 0),
	E9(5, 8, CzdbzIconEnum.Q, 100),
	E10(5, 9, CzdbzIconEnum.J, 200),
	E11(5, 10, CzdbzIconEnum.T10, 250),
	E12(5, 11, CzdbzIconEnum.T9, 300),
	E13(5, 12, CzdbzIconEnum.A, 60),
	E6(5, 5, CzdbzIconEnum.A3, 0),
	E14(5, 13, CzdbzIconEnum.K, 80),
	E15(5, 14, CzdbzIconEnum.Q, 100),
	E3(5, 2, CzdbzIconEnum.S1, 0),
	E16(5, 15, CzdbzIconEnum.J, 200),
	E17(5, 16, CzdbzIconEnum.T10, 250),
	E18(5, 17, CzdbzIconEnum.T9, 300),
	E19(5, 14, CzdbzIconEnum.Q, 100),
	E20(5, 15, CzdbzIconEnum.J, 200),
	E21(5, 16, CzdbzIconEnum.T10, 250),
	E22(5, 17, CzdbzIconEnum.T9, 300),
	;

/*	A1(1, 0, CzdbzIconEnum.S1, 20),
	A2(1, 1, CzdbzIconEnum.A1,30),
	A3(1, 2, CzdbzIconEnum.A2, 40),
	A4(1, 3, CzdbzIconEnum.A3, 50),
	A5(1, 4, CzdbzIconEnum.A, 300),
	A6(1, 5, CzdbzIconEnum.K, 300),
	A7(1, 6, CzdbzIconEnum.Q, 300),
	A8(1, 7, CzdbzIconEnum.J, 300),
	A9(1, 8, CzdbzIconEnum.T10, 400),
	A10(1, 9, CzdbzIconEnum.T9, 400),
	A11(1, 10, CzdbzIconEnum.SCATTER, 20),
	A12(1, 11, CzdbzIconEnum.S1, 100),
	A13(1, 12, CzdbzIconEnum.A1, 200),
	A14(1, 13, CzdbzIconEnum.A2, 200),
	A15(1, 14, CzdbzIconEnum.A3, 200),
	A16(1, 15, CzdbzIconEnum.A, 300),
	A17(1, 16, CzdbzIconEnum.K, 300),
	A18(1, 17, CzdbzIconEnum.Q, 300),
	A19(1, 18, CzdbzIconEnum.J, 300),
	A20(1, 19, CzdbzIconEnum.T10, 400),
	A21(1, 20, CzdbzIconEnum.T9, 400),
	
	B1(2, 0, CzdbzIconEnum.S1, 100),
	B2(2, 1, CzdbzIconEnum.A1, 200),
	B3(2, 2, CzdbzIconEnum.A2, 200),
	B4(2, 3, CzdbzIconEnum.A3, 200),
	B5(2, 4, CzdbzIconEnum.A, 300),
	B6(2, 5, CzdbzIconEnum.K, 300),
	B7(2, 6, CzdbzIconEnum.Q, 300),
	B8(2, 7, CzdbzIconEnum.J, 300),
	B9(2, 8, CzdbzIconEnum.T10, 400),
	B10(2, 9, CzdbzIconEnum.T9, 400),
	B11(2, 10, CzdbzIconEnum.SCATTER, 20),
	B12(2, 11, CzdbzIconEnum.S1, 100),
	B13(2, 12, CzdbzIconEnum.A1, 200),
	B14(2, 13, CzdbzIconEnum.A2, 200),
	B15(2, 14, CzdbzIconEnum.A3, 200),
	B16(2, 15, CzdbzIconEnum.A, 300),
	B17(2, 16, CzdbzIconEnum.K, 300),
	B18(2, 17, CzdbzIconEnum.Q, 300),
	B19(2, 18, CzdbzIconEnum.J, 300),
	B20(2, 19, CzdbzIconEnum.T10, 400),
	B21(2, 20, CzdbzIconEnum.T9, 400),
	B22(2, 21, CzdbzIconEnum.WILD, 50),
	
	C1(3, 0, CzdbzIconEnum.S1, 100),
	C2(3, 1, CzdbzIconEnum.A1, 200),
	C3(3, 2, CzdbzIconEnum.A2, 200),
	C4(3, 3, CzdbzIconEnum.A3, 200),
	C5(3, 4, CzdbzIconEnum.A, 300),
	C6(3, 5, CzdbzIconEnum.K, 300),
	C7(3, 6, CzdbzIconEnum.Q, 300),
	C8(3, 7, CzdbzIconEnum.J, 300),
	C9(3, 8, CzdbzIconEnum.T10, 400),
	C10(3, 9, CzdbzIconEnum.T9, 400),
	C11(3, 10, CzdbzIconEnum.SCATTER, 20),
	C12(3, 11, CzdbzIconEnum.S1, 100),
	C13(3, 12, CzdbzIconEnum.A1, 200),
	C14(3, 13, CzdbzIconEnum.A2, 200),
	C15(3, 14, CzdbzIconEnum.A3, 200),
	C16(3, 15, CzdbzIconEnum.A, 300),
	C17(3, 16, CzdbzIconEnum.K, 300),
	C18(3, 17, CzdbzIconEnum.Q, 300),
	C19(3, 18, CzdbzIconEnum.J, 300),
	C20(3, 19, CzdbzIconEnum.T10, 400),
	C21(3, 20, CzdbzIconEnum.T9, 400),
	C22(3, 21, CzdbzIconEnum.WILD, 50),
	
	D1(4, 0, CzdbzIconEnum.S1, 100),
	D2(4, 1, CzdbzIconEnum.A1, 200),
	D3(4, 2, CzdbzIconEnum.A2, 200),
	D4(4, 3, CzdbzIconEnum.A3, 200),
	D5(4, 4, CzdbzIconEnum.A, 300),
	D6(4, 5, CzdbzIconEnum.K, 300),
	D7(4, 6, CzdbzIconEnum.Q, 300),
	D8(4, 7, CzdbzIconEnum.J, 300),
	D9(4, 8, CzdbzIconEnum.T10, 400),
	D10(4, 9, CzdbzIconEnum.T9, 400),
	D11(4, 10, CzdbzIconEnum.SCATTER, 20),
	D12(4, 11, CzdbzIconEnum.S1, 100),
	D13(4, 12, CzdbzIconEnum.A1, 200),
	D14(4, 13, CzdbzIconEnum.A2, 200),
	D15(4, 14, CzdbzIconEnum.A3, 200),
	D16(4, 15, CzdbzIconEnum.A, 300),
	D17(4, 16, CzdbzIconEnum.K, 300),
	D18(4, 17, CzdbzIconEnum.Q, 300),
	D19(4, 18, CzdbzIconEnum.J, 300),
	D20(4, 19, CzdbzIconEnum.T10, 400),
	D21(4, 20, CzdbzIconEnum.T9, 400),
	D22(4, 21, CzdbzIconEnum.WILD, 50),
	
	E1(5, 0, CzdbzIconEnum.S1, 100),
	E2(5, 1, CzdbzIconEnum.A1, 200),
	E3(5, 2, CzdbzIconEnum.A2, 200),
	E4(5, 3, CzdbzIconEnum.A3, 200),
	E5(5, 4, CzdbzIconEnum.A, 300),
	E6(5, 5, CzdbzIconEnum.K, 300),
	E7(5, 6, CzdbzIconEnum.Q, 300),
	E8(5, 7, CzdbzIconEnum.J, 300),
	E9(5, 8, CzdbzIconEnum.T10, 400),
	E10(5, 9, CzdbzIconEnum.T9, 400),
	E11(5, 10, CzdbzIconEnum.SCATTER, 20),
	E12(5, 11, CzdbzIconEnum.S1, 100),
	E13(5, 12, CzdbzIconEnum.A1, 200),
	E14(5, 13, CzdbzIconEnum.A2, 200),
	E15(5, 14, CzdbzIconEnum.A3, 200),
	E16(5, 15, CzdbzIconEnum.A, 300),
	E17(5, 16, CzdbzIconEnum.K, 300),
	E18(5, 17, CzdbzIconEnum.Q, 300),
	E19(5, 18, CzdbzIconEnum.J, 300),
	E20(5, 19, CzdbzIconEnum.T10, 400),
	E21(5, 20, CzdbzIconEnum.T9, 400),
	;*/
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private CzdbzIconEnum icon;
	//权重
	private int weight;
	
	private CzdbzRollerWeightEnum_backup(int id, int index, CzdbzIconEnum icon, int weight){
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

	public CzdbzIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static CzdbzRollerWeightEnum_backup random(int c) {
		List<CzdbzRollerWeightEnum_backup> all = new ArrayList<CzdbzRollerWeightEnum_backup>();
		for(CzdbzRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(CzdbzRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(CzdbzRollerWeightEnum_backup e : all){
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
	public static Collection<? extends Window> windowInfo(int c) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<CzdbzRollerWeightEnum_backup> all = new ArrayList<CzdbzRollerWeightEnum_backup>();
		for(CzdbzRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(CzdbzRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		CzdbzRollerWeightEnum_backup roller = null;
		Iterator<CzdbzRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			CzdbzRollerWeightEnum_backup e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num < w){
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
}
