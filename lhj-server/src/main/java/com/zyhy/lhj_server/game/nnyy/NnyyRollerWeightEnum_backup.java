/**
 * 
 */
package com.zyhy.lhj_server.game.nnyy;

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
public enum NnyyRollerWeightEnum_backup {
	
	A1(1, 1, NnyyIconEnum.SCATTER, 150),
	A3(1, 3, NnyyIconEnum.A2, 200),
	A4(1, 4, NnyyIconEnum.A, 260),
	A5(1, 5, NnyyIconEnum.K, 280),
	A6(1, 6, NnyyIconEnum.A1, 250),
	A7(1, 7, NnyyIconEnum.Q, 200),
	A8(1, 8, NnyyIconEnum.J, 200),
	A9(1, 9, NnyyIconEnum.T10, 250),
	A10(1, 10, NnyyIconEnum.T11, 300),
	A11(1, 11, NnyyIconEnum.A3, 300),
	A12(1, 12, NnyyIconEnum.Q, 200),
	A13(1, 13, NnyyIconEnum.S1, 350),
	A14(1, 14, NnyyIconEnum.J, 200),
	A15(1, 15, NnyyIconEnum.T10, 250),
	A16(1, 16, NnyyIconEnum.T11, 300),
	A17(1, 17, NnyyIconEnum.K, 200),
	A22(1, 17, NnyyIconEnum.J, 200),
	A21(1, 17, NnyyIconEnum.A, 200),
	A18(1, 18, NnyyIconEnum.A3, 200),
	A19(1, 19, NnyyIconEnum.A2, 250),
	A20(1, 20, NnyyIconEnum.A1, 300),
	
	B1(2, 1, NnyyIconEnum.SCATTER, 150),
	B2(2, 0, NnyyIconEnum.WILD, 150),
	B3(2, 4, NnyyIconEnum.A2, 200),
	B4(2, 3, NnyyIconEnum.A1, 250),
	B5(2, 8, NnyyIconEnum.Q, 200),
	B6(2, 2, NnyyIconEnum.S1, 350),
	B7(2, 9, NnyyIconEnum.J, 200),
	B8(2, 10, NnyyIconEnum.T10, 250),
	B9(2, 11, NnyyIconEnum.T11, 300),
	B10(2, 12, NnyyIconEnum.A, 260),
	B11(2, 5, NnyyIconEnum.A3, 300),
	B12(2, 13, NnyyIconEnum.K, 280),
	B13(2, 14, NnyyIconEnum.Q, 200),
	B14(2, 15, NnyyIconEnum.J, 200),
	B15(2, 16, NnyyIconEnum.T10, 250),
	B16(2, 17, NnyyIconEnum.T11, 300),
	B21(2, 14, NnyyIconEnum.A, 200),
	B17(2, 14, NnyyIconEnum.K, 200),
	B22(2, 14, NnyyIconEnum.J, 200),
	B18(2, 15, NnyyIconEnum.A3, 200),
	B19(2, 16, NnyyIconEnum.A2, 250),
	B20(2, 17, NnyyIconEnum.A1, 300),
	
	C2(3, 1, NnyyIconEnum.SCATTER, 150),
	C5(3, 4, NnyyIconEnum.A2, 200),
	C4(3, 3, NnyyIconEnum.A1, 250),
	C9(3, 8, NnyyIconEnum.Q, 200),
	C10(3, 9, NnyyIconEnum.J, 200),
	C11(3, 10, NnyyIconEnum.T10, 250),
	C12(3, 11, NnyyIconEnum.T11, 300),
	C13(3, 12, NnyyIconEnum.A, 260),
	C6(3, 5, NnyyIconEnum.A3, 300),
	C14(3, 13, NnyyIconEnum.K, 280),
	C15(3, 14, NnyyIconEnum.Q, 200),
	C25(3, 2, NnyyIconEnum.S1, 350),
	C16(3, 15, NnyyIconEnum.J, 200),
	C17(3, 16, NnyyIconEnum.T10, 250),
	C18(3, 17, NnyyIconEnum.T11, 300),
	C23(3, 17, NnyyIconEnum.A, 300),
	C19(3, 14, NnyyIconEnum.K, 200),
	C20(3, 15, NnyyIconEnum.A3, 200),
	C24(3, 15, NnyyIconEnum.J, 200),
	C21(3, 16, NnyyIconEnum.A2, 250),
	C22(3, 17, NnyyIconEnum.A1, 300),
	
	D2(4, 1, NnyyIconEnum.SCATTER, 150),
	D1(4, 0, NnyyIconEnum.WILD, 150),
	D5(4, 4, NnyyIconEnum.A2, 200),
	D4(4, 3, NnyyIconEnum.A1, 250),
	D9(4, 8, NnyyIconEnum.Q, 200),
	D10(4, 9, NnyyIconEnum.J, 200),
	D11(4, 10, NnyyIconEnum.T10, 250),
	D12(4, 11, NnyyIconEnum.T11, 300),
	D13(4, 12, NnyyIconEnum.A, 260),
	D6(4, 5, NnyyIconEnum.A3, 300),
	D14(4, 13, NnyyIconEnum.K, 280),
	D15(4, 14, NnyyIconEnum.Q, 200),
	D16(4, 15, NnyyIconEnum.J, 200),
	D17(4, 16, NnyyIconEnum.T10, 250),
	D18(4, 17, NnyyIconEnum.T11, 300),
	D3(4, 2, NnyyIconEnum.S1, 350),
	D23(4, 2, NnyyIconEnum.A, 200),
	D19(4, 14, NnyyIconEnum.K, 200),
	D20(4, 15, NnyyIconEnum.A3, 200),
	D24(4, 15, NnyyIconEnum.J, 200),
	D21(4, 16, NnyyIconEnum.A2, 250),
	D22(4, 17, NnyyIconEnum.A1, 300),
	
	E2(5, 1, NnyyIconEnum.SCATTER, 150),
	E5(5, 4, NnyyIconEnum.A2, 200),
	E4(5, 3, NnyyIconEnum.A1, 250),
	E9(5, 8, NnyyIconEnum.Q, 100),
	E10(5, 9, NnyyIconEnum.J, 200),
	E25(5, 2, NnyyIconEnum.S1, 350),
	E11(5, 10, NnyyIconEnum.T10, 250),
	E12(5, 11, NnyyIconEnum.T11, 300),
	E13(5, 12, NnyyIconEnum.A, 260),
	E6(5, 5, NnyyIconEnum.A3, 300),
	E14(5, 13, NnyyIconEnum.K, 280),
	E15(5, 14, NnyyIconEnum.Q, 200),
	E16(5, 15, NnyyIconEnum.J, 300),
	E17(5, 16, NnyyIconEnum.T10, 250),
	E18(5, 17, NnyyIconEnum.T11, 300), 
	E23(5, 17, NnyyIconEnum.A, 300), 
	E24(5, 17, NnyyIconEnum.J, 300), 
	E19(5, 14, NnyyIconEnum.K, 300),
	E20(5, 15, NnyyIconEnum.A3, 300),
	E21(5, 16, NnyyIconEnum.A2, 250),
	E22(5, 17, NnyyIconEnum.A1, 300),
	

	/*A1(1, 0, NnyyIconEnum.S1, 100),
	A2(1, 1, NnyyIconEnum.A1, 200),
	A3(1, 2, NnyyIconEnum.A2, 200),
	A4(1, 3, NnyyIconEnum.A3, 200),
	A5(1, 4, NnyyIconEnum.A, 300),
	A6(1, 5, NnyyIconEnum.K, 300),
	A7(1, 6, NnyyIconEnum.Q, 300),
	A8(1, 7, NnyyIconEnum.J, 300),
	A9(1, 8, NnyyIconEnum.T10, 400),
	A10(1, 9, NnyyIconEnum.T11, 400),
	A11(1, 10, NnyyIconEnum.SCATTER, 50),
	A12(1, 11, NnyyIconEnum.S1, 100),
	A13(1, 12, NnyyIconEnum.A1, 200),
	A14(1, 13, NnyyIconEnum.A2, 200),
	A15(1, 14, NnyyIconEnum.A3, 200),
	A16(1, 15, NnyyIconEnum.A, 300),
	A17(1, 16, NnyyIconEnum.K, 300),
	A18(1, 17, NnyyIconEnum.Q, 300),
	A19(1, 18, NnyyIconEnum.J, 300),
	A20(1, 19, NnyyIconEnum.T10, 400),
	A21(1, 20, NnyyIconEnum.T11, 400),
	
	B1(2, 0, NnyyIconEnum.S1, 100),
	B2(2, 1, NnyyIconEnum.A1, 200),
	B3(2, 2, NnyyIconEnum.A2, 200),
	B4(2, 3, NnyyIconEnum.A3, 200),
	B5(2, 4, NnyyIconEnum.A, 300),
	B6(2, 5, NnyyIconEnum.K, 300),
	B7(2, 6, NnyyIconEnum.Q, 300),
	B8(2, 7, NnyyIconEnum.J, 300),
	B9(2, 8, NnyyIconEnum.T10, 400),
	B10(2, 9, NnyyIconEnum.T11, 400),
	B11(2, 10, NnyyIconEnum.SCATTER, 50),
	B12(2, 11, NnyyIconEnum.S1, 100),
	B13(2, 12, NnyyIconEnum.A1, 200),
	B14(2, 13, NnyyIconEnum.A2, 200),
	B15(2, 14, NnyyIconEnum.A3, 200),
	B16(2, 15, NnyyIconEnum.A, 300),
	B17(2, 16, NnyyIconEnum.K, 300),
	B18(2, 17, NnyyIconEnum.Q, 300),
	B19(2, 18, NnyyIconEnum.J, 300),
	B20(2, 19, NnyyIconEnum.T10, 400),
	B21(2, 20, NnyyIconEnum.T11, 400),
	B22(2, 21, NnyyIconEnum.WILD, 50),
	
	C1(3, 0, NnyyIconEnum.S1, 100),
	C2(3, 1, NnyyIconEnum.A1, 200),
	C3(3, 2, NnyyIconEnum.A2, 200),
	C4(3, 3, NnyyIconEnum.A3, 200),
	C5(3, 4, NnyyIconEnum.A, 300),
	C6(3, 5, NnyyIconEnum.K, 300),
	C7(3, 6, NnyyIconEnum.Q, 300),
	C8(3, 7, NnyyIconEnum.J, 300),
	C9(3, 8, NnyyIconEnum.T10, 400),
	C10(3, 9, NnyyIconEnum.T11, 400),
	C11(3, 10, NnyyIconEnum.SCATTER, 50),
	C12(3, 11, NnyyIconEnum.S1, 100),
	C13(3, 12, NnyyIconEnum.A1, 200),
	C14(3, 13, NnyyIconEnum.A2, 200),
	C15(3, 14, NnyyIconEnum.A3, 200),
	C16(3, 15, NnyyIconEnum.A, 300),
	C17(3, 16, NnyyIconEnum.K, 300),
	C18(3, 17, NnyyIconEnum.Q, 300),
	C19(3, 18, NnyyIconEnum.J, 300),
	C20(3, 19, NnyyIconEnum.T10, 400),
	C21(3, 20, NnyyIconEnum.T11, 400),
	
	D1(4, 0, NnyyIconEnum.S1, 100),
	D2(4, 1, NnyyIconEnum.A1, 200),
	D3(4, 2, NnyyIconEnum.A2, 200),
	D4(4, 3, NnyyIconEnum.A3, 200),
	D5(4, 4, NnyyIconEnum.A, 300),
	D6(4, 5, NnyyIconEnum.K, 300),
	D7(4, 6, NnyyIconEnum.Q, 300),
	D8(4, 7, NnyyIconEnum.J, 300),
	D9(4, 8, NnyyIconEnum.T10, 400),
	D10(4, 9, NnyyIconEnum.T11, 400),
	D11(4, 10, NnyyIconEnum.SCATTER, 50),
	D12(4, 11, NnyyIconEnum.S1, 100),
	D13(4, 12, NnyyIconEnum.A1, 200),
	D14(4, 13, NnyyIconEnum.A2, 200),
	D15(4, 14, NnyyIconEnum.A3, 200),
	D16(4, 15, NnyyIconEnum.A, 300),
	D17(4, 16, NnyyIconEnum.K, 300),
	D18(4, 17, NnyyIconEnum.Q, 300),
	D19(4, 18, NnyyIconEnum.J, 300),
	D20(4, 19, NnyyIconEnum.T10, 400),
	D21(4, 20, NnyyIconEnum.T11, 400),
	D22(4, 21, NnyyIconEnum.WILD, 50),
	
	E1(5, 0, NnyyIconEnum.S1, 100),
	E2(5, 1, NnyyIconEnum.A1, 200),
	E3(5, 2, NnyyIconEnum.A2, 200),
	E4(5, 3, NnyyIconEnum.A3, 200),
	E5(5, 4, NnyyIconEnum.A, 300),
	E6(5, 5, NnyyIconEnum.K, 300),
	E7(5, 6, NnyyIconEnum.Q, 300),
	E8(5, 7, NnyyIconEnum.J, 300),
	E9(5, 8, NnyyIconEnum.T10, 400),
	E10(5, 9, NnyyIconEnum.T11, 400),
	E11(5, 10, NnyyIconEnum.SCATTER, 50),
	E12(5, 11, NnyyIconEnum.S1, 100),
	E13(5, 12, NnyyIconEnum.A1, 200),
	E14(5, 13, NnyyIconEnum.A2, 200),
	E15(5, 14, NnyyIconEnum.A3, 200),
	E16(5, 15, NnyyIconEnum.A, 300),
	E17(5, 16, NnyyIconEnum.K, 300),
	E18(5, 17, NnyyIconEnum.Q, 300),
	E19(5, 18, NnyyIconEnum.J, 300),
	E20(5, 19, NnyyIconEnum.T10, 400),
	E21(5, 20, NnyyIconEnum.T11, 400),*/
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private NnyyIconEnum icon;
	//权重
	private int weight;
	
	private NnyyRollerWeightEnum_backup(int id, int index, NnyyIconEnum icon, int weight){
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

	public NnyyIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static NnyyRollerWeightEnum_backup random(int c) {
		List<NnyyRollerWeightEnum_backup> all = new ArrayList<NnyyRollerWeightEnum_backup>();
		for(NnyyRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(NnyyRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(NnyyRollerWeightEnum_backup e : all){
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
		List<NnyyRollerWeightEnum_backup> all = new ArrayList<NnyyRollerWeightEnum_backup>();
		for(NnyyRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(NnyyRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		NnyyRollerWeightEnum_backup roller = null;
		Iterator<NnyyRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			NnyyRollerWeightEnum_backup e = it.next();
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
