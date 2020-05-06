/**
 * 
 */
package com.zyhy.lhj_server.game.swk;

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
public enum SwkRollerWeightEnum_backup {
	
//	A1(1, 1, SwkIconEnum.SCATTER, 500),
	A3(1, 3, SwkIconEnum.A2, 200),
	A4(1, 4, SwkIconEnum.A, 260),
	A1(1, 1, SwkIconEnum.SCATTER, 10),
	A5(1, 5, SwkIconEnum.K, 280),
	A6(1, 6, SwkIconEnum.A1, 250),
	A7(1, 7, SwkIconEnum.Q, 300),
	A8(1, 8, SwkIconEnum.J, 200),
	A9(1, 9, SwkIconEnum.T10, 250),
	A10(1, 10, SwkIconEnum.T9, 300),
	A11(1, 11, SwkIconEnum.A3, 300),
	A12(1, 12, SwkIconEnum.Q, 300),
	A13(1, 13, SwkIconEnum.S1, 350),
	A26(1, 13, SwkIconEnum.A4, 350),
	A14(1, 14, SwkIconEnum.J, 200),
	A15(1, 15, SwkIconEnum.T10, 250),
	A16(1, 16, SwkIconEnum.T9, 300),
	A17(1, 17, SwkIconEnum.K, 200),
	A22(1, 17, SwkIconEnum.J, 200),
	A21(1, 17, SwkIconEnum.A, 300),
	A18(1, 18, SwkIconEnum.A3, 200),
	A19(1, 19, SwkIconEnum.A2, 250),
	A20(1, 20, SwkIconEnum.A1, 300),
	A25(1, 20, SwkIconEnum.WILD, 300),
	
	B2(2, 0, SwkIconEnum.WILD, 300),
	B3(2, 4, SwkIconEnum.A2, 200),
	B4(2, 3, SwkIconEnum.A1, 250),
	B5(2, 8, SwkIconEnum.Q, 300),
	B6(2, 2, SwkIconEnum.S1, 350),
	B7(2, 9, SwkIconEnum.J, 200),
	B8(2, 10, SwkIconEnum.T10, 250),
	B9(2, 11, SwkIconEnum.T9, 300),
	B10(2, 12, SwkIconEnum.A, 260),
	//B1(2, 1, SwkIconEnum.SCATTER, 10),
	B26(2, 12, SwkIconEnum.A4, 260),
	B11(2, 5, SwkIconEnum.A3, 300),
	B12(2, 13, SwkIconEnum.K, 280),
	B13(2, 14, SwkIconEnum.Q, 300),
	B14(2, 15, SwkIconEnum.J, 200),
	B15(2, 16, SwkIconEnum.T10, 250),
	B16(2, 17, SwkIconEnum.T9, 300),
	B21(2, 14, SwkIconEnum.A, 300),
	B17(2, 14, SwkIconEnum.K, 200),
	B22(2, 14, SwkIconEnum.J, 200),
	B18(2, 15, SwkIconEnum.A3, 200),
	B19(2, 16, SwkIconEnum.A2, 250),
	B20(2, 17, SwkIconEnum.A1, 300),
	
	C5(3, 4, SwkIconEnum.A2, 200),
	C4(3, 3, SwkIconEnum.A1, 250),
	C27(3, 3, SwkIconEnum.A4, 250),
	C9(3, 8, SwkIconEnum.Q, 300),
	C10(3, 9, SwkIconEnum.J, 200),
	C11(3, 10, SwkIconEnum.T10, 250),
	C12(3, 11, SwkIconEnum.T9, 300),
	C13(3, 12, SwkIconEnum.A, 260),
	C6(3, 5, SwkIconEnum.A3, 300),
	C14(3, 13, SwkIconEnum.K, 280),
	C15(3, 14, SwkIconEnum.Q, 300),
	C25(3, 2, SwkIconEnum.S1, 350),
	C16(3, 15, SwkIconEnum.J, 200),
	C17(3, 16, SwkIconEnum.T10, 250),
	C18(3, 17, SwkIconEnum.T9, 300),
	C23(3, 17, SwkIconEnum.A, 300),
	C19(3, 14, SwkIconEnum.K, 200),
	C20(3, 15, SwkIconEnum.A3, 200),
	C24(3, 15, SwkIconEnum.J, 200),
	C21(3, 16, SwkIconEnum.A2, 250),
	C22(3, 17, SwkIconEnum.A1, 300),
	C26(3, 17, SwkIconEnum.WILD, 300),
	C2(3, 1, SwkIconEnum.SCATTER, 10),
//	C2(3, 1, SwkIconEnum.SCATTER, 500),
	
	D1(4, 0, SwkIconEnum.WILD, 300),
	D5(4, 4, SwkIconEnum.A2, 200),
	//D2(4, 1, SwkIconEnum.SCATTER, 10),
	D4(4, 3, SwkIconEnum.A1, 250),
	D9(4, 8, SwkIconEnum.Q, 300),
	D10(4, 9, SwkIconEnum.J, 200),
	D27(4, 9, SwkIconEnum.A4, 200),
	D11(4, 10, SwkIconEnum.T10, 250),
	D12(4, 11, SwkIconEnum.T9, 300),
	D13(4, 12, SwkIconEnum.A, 260),
	D6(4, 5, SwkIconEnum.A3, 300),
	D14(4, 13, SwkIconEnum.K, 280),
	D15(4, 14, SwkIconEnum.Q, 300),
	D16(4, 15, SwkIconEnum.J, 200),
	D17(4, 16, SwkIconEnum.T10, 250),
	D18(4, 17, SwkIconEnum.T9, 300),
	D3(4, 2, SwkIconEnum.S1, 350),
	D23(4, 2, SwkIconEnum.A, 300),
	D19(4, 14, SwkIconEnum.K, 200),
	D20(4, 15, SwkIconEnum.A3, 200),
	D24(4, 15, SwkIconEnum.J, 200),
	D21(4, 16, SwkIconEnum.A2, 250),
	D22(4, 17, SwkIconEnum.A1, 300),
	
	E5(5, 4, SwkIconEnum.A2, 200),
	E4(5, 3, SwkIconEnum.A1, 250),
	E9(5, 8, SwkIconEnum.Q, 300),
	E10(5, 9, SwkIconEnum.J, 200),
	E25(5, 2, SwkIconEnum.S1, 350),
	E11(5, 10, SwkIconEnum.T10, 250),
	E12(5, 11, SwkIconEnum.T9, 300),
	E13(5, 12, SwkIconEnum.A, 260),
	E6(5, 5, SwkIconEnum.A3, 300),
	E27(5, 5, SwkIconEnum.A4, 300),
	E2(5, 1, SwkIconEnum.SCATTER, 10),
//	E2(5, 1, SwkIconEnum.SCATTER, 500),
	E14(5, 13, SwkIconEnum.K, 280),
	E15(5, 14, SwkIconEnum.Q, 300),
	E16(5, 15, SwkIconEnum.J, 300),
	E17(5, 16, SwkIconEnum.T10, 250),
	E18(5, 17, SwkIconEnum.T9, 300), 
	E23(5, 17, SwkIconEnum.A, 300), 
	E24(5, 17, SwkIconEnum.J, 300), 
	E19(5, 14, SwkIconEnum.K, 300),
	E20(5, 15, SwkIconEnum.A3, 300),
	E21(5, 16, SwkIconEnum.A2, 250),
	E22(5, 17, SwkIconEnum.A1, 300),
	E26(5, 17, SwkIconEnum.WILD, 300),
	
	/*A1(1, 0, SwkIconEnum.S1, 100),
	A2(1, 1, SwkIconEnum.A1, 200),
	A3(1, 2, SwkIconEnum.A2, 200),
	A4(1, 3, SwkIconEnum.A3, 200),
	A5(1, 4, SwkIconEnum.A4, 200),
	A7(1, 6, SwkIconEnum.A, 300),
	A8(1, 7, SwkIconEnum.K, 300),
	A9(1, 8, SwkIconEnum.Q, 300),
	A10(1, 9, SwkIconEnum.J, 300),
	A11(1, 10, SwkIconEnum.T10, 400),
	A12(1, 11, SwkIconEnum.T9, 400),
	A13(1, 12, SwkIconEnum.SCATTER, 20),
	A14(1, 13, SwkIconEnum.S1, 100),
	A15(1, 14, SwkIconEnum.A1, 200),
	A16(1, 15, SwkIconEnum.A2, 200),
	A17(1, 16, SwkIconEnum.A3, 200),
	A18(1, 17, SwkIconEnum.A4, 200),
	A20(1, 19, SwkIconEnum.A, 300),
	A21(1, 20, SwkIconEnum.K, 300),
	A22(1, 21, SwkIconEnum.Q, 300),
	A23(1, 22, SwkIconEnum.J, 300),
	A24(1, 23, SwkIconEnum.T10, 400),
	A25(1, 24, SwkIconEnum.T9, 400),
	A26(1, 25, SwkIconEnum.WILD, 100),
	
	A27(1, 12, SwkIconEnum.SCATTER, 100),
	A28(1, 12, SwkIconEnum.SCATTER, 100),
	A29(1, 12, SwkIconEnum.SCATTER, 100),
	A30(1, 12, SwkIconEnum.SCATTER, 100),
	
	B1(2, 0, SwkIconEnum.S1, 100),
	B2(2, 1, SwkIconEnum.A1, 200),
	B3(2, 2, SwkIconEnum.A2, 200),
	B4(2, 3, SwkIconEnum.A3, 200),
	B5(2, 4, SwkIconEnum.A4, 200),
	B7(2, 6, SwkIconEnum.A, 300),
	B8(2, 7, SwkIconEnum.K, 300),
	B9(2, 8, SwkIconEnum.Q, 300),
	B10(2, 9, SwkIconEnum.J, 300),
	B11(2, 10, SwkIconEnum.T10, 400),
	B12(2, 11, SwkIconEnum.T9, 400),
	B13(2, 12, SwkIconEnum.SCATTER, 20),
	B14(2, 13, SwkIconEnum.S1, 100),
	B15(2, 14, SwkIconEnum.A1, 200),
	B16(2, 15, SwkIconEnum.A2, 200),
	B17(2, 16, SwkIconEnum.A3, 200),
	B18(2, 17, SwkIconEnum.A4, 200),
	B20(2, 19, SwkIconEnum.A, 300),
	B21(2, 20, SwkIconEnum.K, 300),
	B22(2, 21, SwkIconEnum.Q, 300),
	B23(2, 22, SwkIconEnum.J, 300),
	B24(2, 23, SwkIconEnum.T10, 400),
	B25(2, 24, SwkIconEnum.T9, 400),
	B26(2, 24, SwkIconEnum.WILD, 100),
	
	B27(2, 12, SwkIconEnum.SCATTER, 100),
	B28(2, 12, SwkIconEnum.SCATTER, 100),
	B29(2, 12, SwkIconEnum.SCATTER, 100),
	B30(2, 12, SwkIconEnum.SCATTER, 100),
	B31(2, 12, SwkIconEnum.SCATTER, 100),
	
	C1(3, 0, SwkIconEnum.S1, 100),
	C2(3, 1, SwkIconEnum.A1, 200),
	C3(3, 2, SwkIconEnum.A2, 200),
	C4(3, 3, SwkIconEnum.A3, 200),
	C5(3, 4, SwkIconEnum.A4, 200),
	C7(3, 6, SwkIconEnum.A, 300),
	C8(3, 7, SwkIconEnum.K, 300),
	C9(3, 8, SwkIconEnum.Q, 300),
	C10(3, 9, SwkIconEnum.J, 300),
	C11(3, 10, SwkIconEnum.T10, 400),
	C12(3, 11, SwkIconEnum.T9, 400),
	C13(3, 12, SwkIconEnum.SCATTER, 20),
	C14(3, 13, SwkIconEnum.S1, 100),
	C15(3, 14, SwkIconEnum.A1, 200),
	C16(3, 15, SwkIconEnum.A2, 200),
	C17(3, 16, SwkIconEnum.A3, 200),
	C18(3, 17, SwkIconEnum.A4, 200),
	C20(3, 19, SwkIconEnum.A, 300),
	C21(3, 20, SwkIconEnum.K, 300),
	C22(3, 21, SwkIconEnum.Q, 300),
	C23(3, 22, SwkIconEnum.J, 300),
	C24(3, 23, SwkIconEnum.T10, 400),
	C25(3, 24, SwkIconEnum.T9, 400),
	C26(3, 24, SwkIconEnum.WILD, 100),
	
	C27(3, 12, SwkIconEnum.SCATTER, 100),
	C28(3, 12, SwkIconEnum.SCATTER, 100),
	C29(3, 12, SwkIconEnum.SCATTER, 100),
	C30(3, 12, SwkIconEnum.SCATTER, 100),
	C31(3, 12, SwkIconEnum.SCATTER, 100),
	
	D1(4, 0, SwkIconEnum.S1, 100),
	D2(4, 1, SwkIconEnum.A1, 200),
	D3(4, 2, SwkIconEnum.A2, 200),
	D4(4, 3, SwkIconEnum.A3, 200),
	D5(4, 4, SwkIconEnum.A4, 200),
	D7(4, 6, SwkIconEnum.A, 300),
	D8(4, 7, SwkIconEnum.K, 300),
	D9(4, 8, SwkIconEnum.Q, 300),
	D10(4, 9, SwkIconEnum.J, 300),
	D11(4, 10, SwkIconEnum.T10, 400),
	D12(4, 11, SwkIconEnum.T9, 400),
	D13(4, 12, SwkIconEnum.SCATTER, 20),
	D14(4, 13, SwkIconEnum.S1, 100),
	D15(4, 14, SwkIconEnum.A1, 200),
	D16(4, 15, SwkIconEnum.A2, 200),
	D17(4, 16, SwkIconEnum.A3, 200),
	D18(4, 17, SwkIconEnum.A4, 200),
	D20(4, 19, SwkIconEnum.A, 300),
	D21(4, 20, SwkIconEnum.K, 300),
	D22(4, 21, SwkIconEnum.Q, 300),
	D23(4, 22, SwkIconEnum.J, 300),
	D24(4, 23, SwkIconEnum.T10, 400),
	D25(4, 24, SwkIconEnum.T9, 400),
	D26(4, 24, SwkIconEnum.WILD, 100),
	
	E1(5, 0, SwkIconEnum.S1, 100),
	E2(5, 1, SwkIconEnum.A1, 200),
	E3(5, 2, SwkIconEnum.A2, 200),
	E4(5, 3, SwkIconEnum.A3, 200),
	E5(5, 4, SwkIconEnum.A4, 200),
	E7(5, 6, SwkIconEnum.A, 300),
	E8(5, 7, SwkIconEnum.K, 300),
	E9(5, 8, SwkIconEnum.Q, 300),
	E10(5, 9, SwkIconEnum.J, 300),
	E11(5, 10, SwkIconEnum.T10, 400),
	E12(5, 11, SwkIconEnum.T9, 400),
	E13(5, 12, SwkIconEnum.SCATTER, 20),
	E14(5, 13, SwkIconEnum.S1, 100),
	E15(5, 14, SwkIconEnum.A1, 200),
	E16(5, 15, SwkIconEnum.A2, 200),
	E17(5, 16, SwkIconEnum.A3, 200),
	E18(5, 17, SwkIconEnum.A4, 200),
	E20(5, 19, SwkIconEnum.A, 300),
	E21(5, 20, SwkIconEnum.K, 300),
	E22(5, 21, SwkIconEnum.Q, 300),
	E23(5, 22, SwkIconEnum.J, 300),
	E24(5, 23, SwkIconEnum.T10, 400),
	E25(5, 24, SwkIconEnum.T9, 400),
	E26(5, 25, SwkIconEnum.WILD, 100),*/
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private SwkIconEnum icon;
	//权重
	private int weight;
	
	private SwkRollerWeightEnum_backup(int id, int index, SwkIconEnum icon, int weight){
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

	public SwkIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static SwkRollerWeightEnum_backup random(int c) {
		List<SwkRollerWeightEnum_backup> all = new ArrayList<SwkRollerWeightEnum_backup>();
		for(SwkRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(SwkRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(SwkRollerWeightEnum_backup e : all){
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
		List<SwkRollerWeightEnum_backup> all = new ArrayList<SwkRollerWeightEnum_backup>();
		for(SwkRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(SwkRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		SwkRollerWeightEnum_backup roller = null;
		Iterator<SwkRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			SwkRollerWeightEnum_backup e = it.next();
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
}
