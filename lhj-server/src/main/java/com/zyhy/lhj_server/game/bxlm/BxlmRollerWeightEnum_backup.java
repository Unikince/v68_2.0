/**
 * 
 */
package com.zyhy.lhj_server.game.bxlm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum BxlmRollerWeightEnum_backup {

	A1(1, 0, BxlmIconEnum.S1, 100),
	A2(1, 1, BxlmIconEnum.A1, 200),
	A3(1, 2, BxlmIconEnum.A2, 200),
	A4(1, 3, BxlmIconEnum.A3, 200),
	A5(1, 4, BxlmIconEnum.A4, 200),
	A6(1, 5, BxlmIconEnum.A5, 200),
	/*A7(1, 6, BxlmIconEnum.A, 300),
	A8(1, 7, BxlmIconEnum.K, 300),
	A9(1, 8, BxlmIconEnum.Q, 300),
	A10(1, 9, BxlmIconEnum.J, 300),
	A11(1, 10, BxlmIconEnum.T10, 400),
	A12(1, 11, BxlmIconEnum.T9, 400),*/
	A13(1, 12, BxlmIconEnum.SCATTER, 150),
	A14(1, 13, BxlmIconEnum.S1, 100),
	A15(1, 14, BxlmIconEnum.A1, 200),
	A16(1, 15, BxlmIconEnum.A2, 200),
	A17(1, 16, BxlmIconEnum.A3, 200),
	A18(1, 17, BxlmIconEnum.A4, 200),
	A19(1, 18, BxlmIconEnum.A5, 200),
	A20(1, 19, BxlmIconEnum.A, 300),
	A21(1, 20, BxlmIconEnum.K, 300),
	A22(1, 21, BxlmIconEnum.Q, 300),
	A23(1, 22, BxlmIconEnum.J, 300),
	A24(1, 23, BxlmIconEnum.T10, 400),
	A25(1, 24, BxlmIconEnum.T9, 400),
	
	/*A26(1, 12, BxlmIconEnum.SCATTER, 100),
	A27(1, 12, BxlmIconEnum.SCATTER, 100),
	A28(1, 12, BxlmIconEnum.SCATTER, 100),
	A29(1, 12, BxlmIconEnum.SCATTER, 100),
	A30(1, 12, BxlmIconEnum.SCATTER, 100),*/
	
	B1(2, 0, BxlmIconEnum.S1, 100),
	B2(2, 1, BxlmIconEnum.A1, 200),
	B3(2, 2, BxlmIconEnum.A2, 200),
	B4(2, 3, BxlmIconEnum.A3, 200),
	B5(2, 4, BxlmIconEnum.A4, 200),
	B6(2, 5, BxlmIconEnum.A5, 200),
	/*B7(2, 6, BxlmIconEnum.A, 300),
	B8(2, 7, BxlmIconEnum.K, 300),
	B9(2, 8, BxlmIconEnum.Q, 300),
	B10(2, 9, BxlmIconEnum.J, 300),
	B11(2, 10, BxlmIconEnum.T10, 400),
	B12(2, 11, BxlmIconEnum.T9, 400),*/
	B13(2, 12, BxlmIconEnum.SCATTER, 150),
	B14(2, 13, BxlmIconEnum.S1, 100),
	B15(2, 14, BxlmIconEnum.A1, 200),
	B16(2, 15, BxlmIconEnum.A2, 200),
	B17(2, 16, BxlmIconEnum.A3, 200),
	B18(2, 17, BxlmIconEnum.A4, 200),
	B19(2, 18, BxlmIconEnum.A5, 200),
	B20(2, 19, BxlmIconEnum.A, 300),
	B21(2, 20, BxlmIconEnum.K, 300),
	B22(2, 21, BxlmIconEnum.Q, 300),
	B23(2, 22, BxlmIconEnum.J, 300),
	B24(2, 23, BxlmIconEnum.T10, 400),
	B25(2, 24, BxlmIconEnum.T9, 400),
	B26(2, 24, BxlmIconEnum.WILD, 100),
	
	/*B27(2, 12, BxlmIconEnum.SCATTER, 100),
	B28(2, 12, BxlmIconEnum.SCATTER, 100),
	B29(2, 12, BxlmIconEnum.SCATTER, 100),
	B30(2, 12, BxlmIconEnum.SCATTER, 100),
	B31(2, 12, BxlmIconEnum.SCATTER, 100),*/
	
	C1(3, 0, BxlmIconEnum.S1, 100),
	C2(3, 1, BxlmIconEnum.A1, 200),
	C3(3, 2, BxlmIconEnum.A2, 200),
	C4(3, 3, BxlmIconEnum.A3, 200),
	C5(3, 4, BxlmIconEnum.A4, 200),
	C6(3, 5, BxlmIconEnum.A5, 200),
	/*C7(3, 6, BxlmIconEnum.A, 300),
	C8(3, 7, BxlmIconEnum.K, 300),
	C9(3, 8, BxlmIconEnum.Q, 300),
	C10(3, 9, BxlmIconEnum.J, 300),
	C11(3, 10, BxlmIconEnum.T10, 400),
	C12(3, 11, BxlmIconEnum.T9, 400),*/
	C13(3, 12, BxlmIconEnum.SCATTER, 150),
	C14(3, 13, BxlmIconEnum.S1, 100),
	C15(3, 14, BxlmIconEnum.A1, 200),
	C16(3, 15, BxlmIconEnum.A2, 200),
	C17(3, 16, BxlmIconEnum.A3, 200),
	C18(3, 17, BxlmIconEnum.A4, 200),
	C19(3, 18, BxlmIconEnum.A5, 200),
	C20(3, 19, BxlmIconEnum.A, 300),
	C21(3, 20, BxlmIconEnum.K, 300),
	C22(3, 21, BxlmIconEnum.Q, 300),
	C23(3, 22, BxlmIconEnum.J, 300),
	C24(3, 23, BxlmIconEnum.T10, 400),
	C25(3, 24, BxlmIconEnum.T9, 400),
	C26(3, 24, BxlmIconEnum.WILD, 100),
	
	/*C27(3, 12, BxlmIconEnum.SCATTER, 100),
	C28(3, 12, BxlmIconEnum.SCATTER, 100),
	C29(3, 12, BxlmIconEnum.SCATTER, 100),
	C30(3, 12, BxlmIconEnum.SCATTER, 100),
	C31(3, 12, BxlmIconEnum.SCATTER, 100),*/
	
	D1(4, 0, BxlmIconEnum.S1, 100),
	D2(4, 1, BxlmIconEnum.A1, 200),
	D3(4, 2, BxlmIconEnum.A2, 200),
	D4(4, 3, BxlmIconEnum.A3, 200),
	D5(4, 4, BxlmIconEnum.A4, 200),
	D6(4, 5, BxlmIconEnum.A5, 200),
	/*D7(4, 6, BxlmIconEnum.A, 300),
	D8(4, 7, BxlmIconEnum.K, 300),
	D9(4, 8, BxlmIconEnum.Q, 300),
	D10(4, 9, BxlmIconEnum.J, 300),
	D11(4, 10, BxlmIconEnum.T10, 400),
	D12(4, 11, BxlmIconEnum.T9, 400),*/
	D13(4, 12, BxlmIconEnum.SCATTER, 150),
	D14(4, 13, BxlmIconEnum.S1, 100),
	D15(4, 14, BxlmIconEnum.A1, 200),
	D16(4, 15, BxlmIconEnum.A2, 200),
	D17(4, 16, BxlmIconEnum.A3, 200),
	D18(4, 17, BxlmIconEnum.A4, 200),
	D19(4, 18, BxlmIconEnum.A5, 200),
	D20(4, 19, BxlmIconEnum.A, 300),
	D21(4, 20, BxlmIconEnum.K, 300),
	D22(4, 21, BxlmIconEnum.Q, 300),
	D23(4, 22, BxlmIconEnum.J, 300),
	D24(4, 23, BxlmIconEnum.T10, 400),
	D25(4, 24, BxlmIconEnum.T9, 400),
	D26(4, 24, BxlmIconEnum.WILD, 100),
	
	E1(5, 0, BxlmIconEnum.S1, 100),
	E2(5, 1, BxlmIconEnum.A1, 200),
	E3(5, 2, BxlmIconEnum.A2, 200),
	E4(5, 3, BxlmIconEnum.A3, 200),
	E5(5, 4, BxlmIconEnum.A4, 200),
	E6(5, 5, BxlmIconEnum.A5, 200),
	E7(5, 6, BxlmIconEnum.A, 300),
	E8(5, 7, BxlmIconEnum.K, 300),
	E9(5, 8, BxlmIconEnum.Q, 300),
	E10(5, 9, BxlmIconEnum.J, 300),
	E11(5, 10, BxlmIconEnum.T10, 400),
	E12(5, 11, BxlmIconEnum.T9, 400),
	E13(5, 12, BxlmIconEnum.SCATTER, 150),
	E14(5, 13, BxlmIconEnum.S1, 100),
	E15(5, 14, BxlmIconEnum.A1, 200),
	E16(5, 15, BxlmIconEnum.A2, 200),
	E17(5, 16, BxlmIconEnum.A3, 200),
	E18(5, 17, BxlmIconEnum.A4, 200),
	E19(5, 18, BxlmIconEnum.A5, 200),
	E20(5, 19, BxlmIconEnum.A, 300),
	E21(5, 20, BxlmIconEnum.K, 300),
	E22(5, 21, BxlmIconEnum.Q, 300),
	E23(5, 22, BxlmIconEnum.J, 300),
	E24(5, 23, BxlmIconEnum.T10, 400),
	E25(5, 24, BxlmIconEnum.T9, 400),
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private BxlmIconEnum icon;
	//权重
	private int weight;
	
	private BxlmRollerWeightEnum_backup(int id, int index, BxlmIconEnum icon, int weight){
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

	public BxlmIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static BxlmRollerWeightEnum_backup random(int c) {
		List<BxlmRollerWeightEnum_backup> all = new ArrayList<BxlmRollerWeightEnum_backup>();
		for(BxlmRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(BxlmRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(BxlmRollerWeightEnum_backup e : all){
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
	public static WindowInfo windowInfo(int c,int d) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<BxlmRollerWeightEnum_backup> all = new ArrayList<BxlmRollerWeightEnum_backup>();
		for(BxlmRollerWeightEnum_backup e : values()){
			if(e.getId() == c+1){
				all.add(e);
			}
		}
		int total = 0;
		for(BxlmRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		BxlmRollerWeightEnum_backup roller = null;
		Iterator<BxlmRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			BxlmRollerWeightEnum_backup e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num <= w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		ws.add(new WindowInfo(c, d, roller.getIcon()));
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
}
